 /*
  * Copyright (c) 2021 Huawei Device Co., Ltd.
  * <p>
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  * <p>
  * http://www.apache.org/licenses/LICENSE-2.0
  * <p>
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.cooltechworks.scratchview.demo;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityLifecycleExecutor;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.aafwk.ability.delegation.IAbilityDelegator;
import ohos.aafwk.ability.delegation.IAbilityMonitor;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Component;
import ohos.agp.render.Path;
import ohos.agp.render.PathMeasure;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.miscservices.timeutility.Time;
import ohos.multimodalinput.event.EventCreator;
import ohos.multimodalinput.event.KeyEvent;
import ohos.multimodalinput.event.TouchEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

 public class EventHelper {
     public static final int ACTION_DOWN = 0;
     public static final int ACTION_UP = 1;
     public static final int ACTION_MOVE = 2;

     private static IAbilityDelegator sAbilityDelegator = AbilityDelegatorRegistry.getAbilityDelegator();
     private static List<Ability> abilities = new ArrayList<>();
     private static EventHandler eventHandler = new EventHandler(EventRunner.getMainEventRunner()) {
         @Override
         protected void processEvent(InnerEvent event) {
             super.processEvent(event);
         }
     };

     public static void clearAbilities() {
         for (int i = abilities.size() - 1; i >= 0; i--) {
             Ability ability = abilities.remove(i);
             if (ability != null && !ability.isTerminating()) {
                 ability.terminateAbility();
             }
         }
     }

     /**
      * 启动Ability
      *
      * @param abilityClz Ability的class
      * @param <T>        接受类型 Ability或者子类
      * @return Ability实例 或者空 如果非空会自动强转为接收类型
      */
     public static <T extends Ability> T startAbility(Class abilityClz) {
         Context context = sAbilityDelegator.getAppContext();
         IAbilityMonitor abilityMonitor = sAbilityDelegator.addAbilityMonitor(abilityClz.getCanonicalName());

         Intent intent = new Intent();
         Operation operation =
                 new Intent.OperationBuilder().withBundleName(context.getBundleName()).withAbilityName(abilityClz).build();
         intent.setOperation(operation);
         sAbilityDelegator.startAbilitySync(intent);
         Ability result = abilityMonitor.waitForAbility();
         abilities.add(result);
         sAbilityDelegator.removeAbilityMonitor(abilityMonitor);
         return (T) result;
     }

     /**
      * 等待Ability 走到onActive状态，貌似没多大用，后面还是最好sleep几秒钟，否则UI控件还是可能处于没有绘制的状态
      *
      * @param ability          ability
      * @param durationInSecond 最大等待时长
      */
     public static void waitForActive(Ability ability, int durationInSecond) {
         if (ability.getState() == AbilityLifecycleExecutor.LifecycleState.ACTIVE) {
             return;
         }
         AbilityDelegatorRegistry.getAbilityDelegator().invokeAbilityOnActive(ability);
         int count = 0;
         while (count < durationInSecond) {
             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             if (ability.getState() == AbilityLifecycleExecutor.LifecycleState.ACTIVE) {
                 return;
             }
         }
         throw new RuntimeException("can not active ability in " + durationInSecond + "s");
     }

     /**
      * 触发按键事件
      *
      * @param ability 当前ability
      * @param keyCode 键值，注意是Ohos的键值 {@link KeyEvent}
      */
     public static void triggerKeyEvent(Ability ability, int keyCode) {
         sAbilityDelegator.triggerKeyEvent(ability, EventCreator.createKeyEvent(KeyEvent.KEY_PRESSED, keyCode).get());
         sAbilityDelegator.triggerKeyEvent(ability, EventCreator.createKeyEvent(KeyEvent.KEY_RELEASED, keyCode).get());
     }

     /**
      * 触发点击事件
      *
      * @param ability   当前ability
      * @param component 需要点击的控件
      */
     public static void triggerClickEvent(Ability ability, Component component) {
         sAbilityDelegator.triggerClickEvent(ability, component);
     }

     /**
      * 触发长按
      *
      * @param ability   当前ability
      * @param component 需要长按的控件
      */
     public static void triggerLongClickEvent(Ability ability, Component component) {
         component.executeLongClick();
     }

     /**
      * 触发长按
      *
      * @param ability   当前ability
      * @param component 需要双击的控件
      */
     public static void triggerDoubleClickEvent(Ability ability, Component component) {
         component.executeDoubleClick();
     }


     /**
      * 模拟直线滑动事件 目前只支持模拟匀速滑动
      *
      * @param ability  ability
      * @param x0       起点横坐标 以屏幕左上角为原点(0, 0)
      * @param y0       起点纵坐标
      * @param x1       终点横坐标
      * @param y1       终点纵坐标
      * @param duration 滑动时长
      */
     public static void inputSwipe(Ability ability, int x0, int y0, int x1, int y1, long duration) {
         Path path = new Path();
         path.moveTo(x0, y0);
         path.lineTo(x1, y1);
         inputSwipe(ability, path, duration);
     }

     /**
      * 模拟滑动事件 目前只支持模拟匀速滑动
      *
      * @param ability  ability
      * @param path     滑动路径 支持自定义曲线路径 其坐标系为以屏幕左上角为原点的坐标系
      * @param duration 滑动时长
      */
     public static void inputSwipe(Ability ability, Path path, long duration) {
         PathMeasure pathMeasure = new PathMeasure(path, false);
         float length = pathMeasure.getLength();
         float[] position = new float[2];
         float[] tan = new float[2];

         pathMeasure.getPosTan(0, position, tan);
         long startTime = Time.getRealActiveTime();
         long curTime = startTime;
         TouchEvent downEvent = obtainTouchEvent(startTime, startTime, ACTION_DOWN, position[0], position[1]);
         sAbilityDelegator.triggerTouchEvent(ability, downEvent);
         while (curTime < startTime + duration) {
             try {
                 Thread.sleep(Math.min(16, duration));
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             curTime = Time.getRealActiveTime();
             if (curTime - startTime <= duration) {
                 pathMeasure.getPosTan(length * (curTime - startTime) / duration, position, tan);
                 TouchEvent moveEvent = obtainTouchEvent(startTime, curTime, ACTION_MOVE, position[0], position[1]);
                 sAbilityDelegator.triggerTouchEvent(ability, moveEvent);
             }
         }

         pathMeasure.getPosTan(length, position, tan);
         TouchEvent upEvent = obtainTouchEvent(startTime, Time.getRealActiveTime(), ACTION_UP, position[0], position[1]);
         sAbilityDelegator.triggerTouchEvent(ability, upEvent);
     }

     /**
      * 模拟直线滑动事件 目前只支持模拟匀速滑动
      *
      * @param ability   ability
      * @param component 施加滑动的component
      * @param x0        起点横坐标 以component左上角为原点(0, 0)
      * @param y0        起点纵坐标
      * @param x1        终点横坐标
      * @param y1        终点纵坐标
      * @param duration  滑动时长
      */
     public static void inputSwipe(Ability ability, Component component, int x0, int y0, int x1, int y1, long duration) {
         Path path = new Path();
         path.moveTo(x0, y0);
         path.lineTo(x1, y1);
         inputSwipe(ability, component, path, duration);
     }

     /**
      * 模拟滑动事件 目前只支持模拟匀速滑动
      *
      * @param ability   ability
      * @param component 施加滑动的component
      * @param path      滑动路径 支持自定义曲线路径 其坐标系为以component左上角为原点的坐标系
      * @param duration  滑动时长
      */
     public static void inputSwipe(Ability ability, Component component, Path path, long duration) {
         PathMeasure pathMeasure = new PathMeasure(path, false);
         float length = pathMeasure.getLength();
         float[] position = new float[2];
         float[] tan = new float[2];
         int offsetX = component.getLocationOnScreen()[0];
         int offsetY = component.getLocationOnScreen()[1];

         pathMeasure.getPosTan(0, position, tan);
         long startTime = Time.getRealActiveTime();
         long curTime = startTime;
         TouchEvent downEvent = obtainTouchEvent(startTime, startTime, ACTION_DOWN, position[0] + offsetX,
                 position[1] + offsetY);
         sAbilityDelegator.triggerTouchEvent(ability, downEvent);
         while (curTime < startTime + duration) {
             try {
                 Thread.sleep(Math.min(16, duration));
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             curTime = Time.getRealActiveTime();
             if (curTime - startTime <= duration) {
                 pathMeasure.getPosTan(length * (curTime - startTime) / duration, position, tan);
                 TouchEvent moveEvent = obtainTouchEvent(startTime, curTime, ACTION_MOVE, position[0] + offsetX,
                         position[1] + offsetY);
                 sAbilityDelegator.triggerTouchEvent(ability, moveEvent);
             }
         }

         pathMeasure.getPosTan(length, position, tan);
         TouchEvent upEvent = obtainTouchEvent(startTime, Time.getRealActiveTime(), ACTION_UP, position[0] + offsetX,
                 position[1] + offsetY);
         sAbilityDelegator.triggerTouchEvent(ability, upEvent);
     }

     private static final String TOUCH_EVENT_CLZ_NAME = "ohos.multimodalinput.eventimpl.TouchEventImpl";
     private static final String MOTION_EVENT_CLZ_NAME = new StringBuilder().append("and")
             .append("roid.view.MotionEvent").toString();

     public static TouchEvent obtainTouchEvent(long downTime, long eventTime, int action, float x, float y) {
         TouchEvent result = null;
         try {
             Class touchEventClz = Class.forName(TOUCH_EVENT_CLZ_NAME);
             Class motionEventClz = Class.forName(MOTION_EVENT_CLZ_NAME);
             Method motionEventObtainer = motionEventClz.getDeclaredMethod("obtain", long.class, long.class, int.class
                     , float.class, float.class, int.class);
             Constructor touchEventConstructor = touchEventClz.getDeclaredConstructor(motionEventClz);
             touchEventConstructor.setAccessible(true);
             Object motionEvent = motionEventObtainer.invoke(null, downTime, eventTime, action, x, y, 0);
             result = (TouchEvent) touchEventConstructor.newInstance(motionEvent);
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
             e.printStackTrace();
         } catch (InstantiationException e) {
             e.printStackTrace();
         }
         return result;
     }

     public static void simulateMoveOnScreen(Ability ability, Component component, int distance) {
         int[] pos = component.getLocationOnScreen();
         pos[1] += component.getHeight() / 2;

         long downTime = Time.getRealActiveTime();
         long upTime = downTime + 800;
         TouchEvent ev1 = EventHelper.obtainTouchEvent(downTime, downTime, EventHelper.ACTION_DOWN, pos[0], pos[1]);
         sAbilityDelegator.triggerTouchEvent(ability, ev1);
         try {
             Thread.sleep(800);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         TouchEvent ev2 = EventHelper.obtainTouchEvent(downTime, downTime, EventHelper.ACTION_MOVE, pos[0] + distance, pos[1]);
         sAbilityDelegator.triggerTouchEvent(ability, ev2);
         TouchEvent ev3 = EventHelper.obtainTouchEvent(downTime, upTime, EventHelper.ACTION_UP, pos[0] + distance, pos[1]);
         sAbilityDelegator.triggerTouchEvent(ability, ev3);
     }

 }
