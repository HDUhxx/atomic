# Doodle

#### 项目介绍
- 项目名称：Doodle
- 所属系列：openharmony 第三方组件适配移植
- 功能：图片涂鸦，具有撤消、缩放、移动、添加文字，贴图等功能。还是一个功能强大，可自定义和可扩展的涂鸦框架、多功能画板。
- 项目移植状态：主功能完成
- 调用差异：无
- 开发版本：sdk6，DevEco Studio2.2 beta1
- 基线版本：Release 5.5.4

#### 效果演示
![doodle_1](/screenshots/doodle_1.gif)

![doodle_01](/screenshots/doodle_01.png)
![doodle_02](/screenshots/doodle_02.png)
![doodle_03](/screenshots/doodle_03.png)

#### 安装教程

在moudle级别下的build.gradle文件中添加依赖 
```
// 添加maven仓库 
repositories { 
    maven { 
        url 'https://s01.oss.sonatype.org/content/repositories/releases/' 
    }
}

// 添加依赖库
dependencies {
    implementation 'com.gitee.chinasoft_ohos:doodle:1.0.1'   
}
```

在sdk6，DevEco Studio 2.2 Beta1项目可直接运行
如无法运行，删除项目.gradle,.idea,build,gradle,build.gradle文件，
并依据自己的版本创建新项目，将新项目的对应文件复制到根目录下

#### 使用说明

// Feature 特性

  * Brush and shape ***画笔及形状***

    The brush can choose hand-painted, mosaic, imitation, eraser, text, texture, and the imitation function is similar to that in PS, copying somewhere in the picture. Shapes can be selected from hand-drawn, arrows, lines, circles, rectangles, and so on. The background color of the brush can be selected as a color, or an image.

    ***画笔可以选择手绘、马赛克、仿制、橡皮擦、文字、贴图，其中仿制功能跟PS中的类似，复制图片中的某处地方。形状可以选择手绘、箭头、直线、圆、矩形等。画笔的底色可以选择颜色，或者一张图片。***

  * Undo/Redo ***撤销/重做***

    Each step of the doodle operation can be undone or redone.
    
    ***每一步的涂鸦操作都可以撤销。***

  * Zoom, move, and rotate ***放缩、移动及旋转***

    In the process of doodle, you can freely zoom, move and rotate the picture with gestures. Also, you can move，rotate and scale the doodle item.
    
    ***在涂鸦的过程中，可以自由地通过手势缩放、移动、旋转图片。可对涂鸦移动、旋转、缩放等。***

  * Zoomer ***放大器***

    In order to doodle more finely, an zoomer can be set up during the doodle process.
    
    ***为了更细微地涂鸦，涂鸦过程中可以设置出现放大器。***

// Usage 用法


There are two ways to use the Doodle library:

***这里有两种方式使用Doodle涂鸦库***

//  A. Launch DoodleAbility directly (the layout is like demo images above). If you need to customize more interactions, please use another method (Way B)．

  ***使用写好的涂鸦界面，直接启动.启动的页面可参看上面的演示图片。如果需要自定义更多的交互方式，则请使用另一种方式(即B方式)。***
```java
       Intent intent = new Intent();
                intent.setUriAndType(uri, null); // 传递进行涂鸦操作的图片Uri
                Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName(getBundleName())
                    .withAbilityName("com.example.doodlelib.DoodleAbility")
                    .build();
                intent.setOperation(operation);
                startAbilityForResult(intent, REQ_CODE_DOODLE);
       DoodleParams params = new DoodleParams(); // 涂鸦参数
       params.mImagePath // 图片路径
```
See [DoodleParams](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleParams.java) for more details.

***查看[DoodleParams](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleParams.java)获取更多涂鸦参数信息。***

//  B. Recommend, use DoodleView and customize your layout. 

***推荐的方法：使用DoodleView，便于拓展，灵活性高，自定义自己的交互界面.***

```java
/*
Whether or not to optimize drawing, it is suggested to open, which can optimize the drawing speed and performance.
Note: When item is selected for editing after opening, it will be drawn at the top level, and not at the corresponding level until editing is completed.
是否优化绘制，建议开启，可优化绘制速度和性能.
注意：开启后item被选中编辑时时会绘制在最上面一层，直到结束编辑后才绘制在相应层级
 */
boolean optimizeDrawing = true;
DoodleView mDoodleView = new DoodleView(this, pixelMap, optimizeDrawing, new IDoodleListener() {
            /*
            called when save the doodled iamge. 
            保存涂鸦图像时调用
             */
            @Override
            public void onSaved(IDoodle doodle, PixelMap pixelMap, Runnable callback) {
               //do something
            }

            /*
             called when it is ready to doodle because the view has been measured. Now, you can set size, color, pen, shape, etc. 
             此时view已经测量完成，涂鸦前的准备工作已经完成，在这里可以设置大小、颜色、画笔、形状等。
             */
            @Override
            public void onReady(IDoodle doodle, int width, int height) {
                //do something
            }
        });

mTouchGestureListener = new DoodleOnTouchGestureListener(mDoodleView, new DoodleOnTouchGestureListener.ISelectionListener() {
    /*
     called when the item(such as text, texture) is selected/unselected.
     item（如文字，贴图）被选中或取消选中时回调
     */
    @Override
    public void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean selected) {
        //do something
    }

    /*
     called when you click the view to create a item(such as text, texture).
     点击View中的某个点创建可选择的item（如文字，贴图）时回调
     */
    @Override
    public void onCreateSelectableItem(IDoodle doodle, float x, float y) {
        //do something
        /*
if (mDoodleView.getPen() == DoodlePen.TEXT) {
        IDoodleSelectableItem item = new DoodleText(mDoodleView, "hello", 20 * mDoodleView.getUnitSize(), new DoodleColor(Color.RED), x, y);
        mDoodleView.addItem(item);
} else if (mDoodleView.getPen() == DoodlePen.BITMAP) {
        IDoodleSelectableItem item = new DoodleBitmap(mDoodleView, bitmap, 80 * mDoodle.getUnitSize(), x, y);
        mDoodleView.addItem(item);
}
        */
    }
});

// create touch detector, which dectects the gesture of scoll, scale, single tap, etc.
// 创建手势识别器，识别滚动，缩放，点击等手势
IDoodleTouchDetector detector = new DoodleTouchDetector(getApplicationContext(), mTouchGestureListener);
mDoodleView.setDefaultTouchDetector(detector);

// Setting parameters.设置参数
mDoodleView.setPen(DoodlePen.TEXT);
mDoodleView.setShape(DoodleShape.HAND_WRITE);
mDoodleView.setColor(new DoodleColor(Color.RED));


```
When turning off optimized drawing, you only need to call `addItem(IDoodleItem)` when you create it. When you start optimizing drawing, the created or selected item needs to call `markItemToOptimizeDrawing(IDoodleItem)`, and you should call `notifyItemFinishedDrawing(IDoodleItem)` when you finish drawing. So this is generally used in code:

***当关闭优化绘制时,只需要在创建时调用`addItem(IDoodleItem)`;而当开启优化绘制时，创建或选中的item需要调用`markItemToOptimizeDrawing(IDoodleItem)`,结束绘制时应调用`notifyItemFinishedDrawing(IDoodleItem)`。因此在代码中一般这样使用：***
```java
// when you are creating a item or selecting a item to edit
if (mDoodle.isOptimizeDrawing()) {
   mDoodle.markItemToOptimizeDrawing(item);
} else {
   mDoodle.addItem(item);
}

...

// finish creating or editting
if (mDoodle.isOptimizeDrawing()) {
   mDoodle.notifyItemFinishedDrawing(item);
}
```


Then, add the DoodleView to your layout. Now you can start doodling freely.

 ***把DoodleView添加到布局中，然后开始涂鸦。***

// Demo 实例

Here are other simple examples to teach you how to use the doodle framework.

1. **[Mosaic effect](https://gitee.com/chinasoft_ohos/Doodle/tree/master/entry/src/main/java/com/example/doodle/MosaicDemo.java)**
 ***[马赛克效果](https://gitee.com/chinasoft_ohos/Doodle/tree/master/entry/src/main/java/com/example/doodle/MosaicDemo.java)***

2. **[Change text's size by scale gesture](https://gitee.com/chinasoft_ohos/Doodle/tree/master/entry/src/main/java/com/example/doodle/ScaleGestureItemDemoAbility.java)**
 ***[手势缩放文本大小](https://gitee.com/chinasoft_ohos/Doodle/tree/master/entry/src/main/java/com/example/doodle/ScaleGestureItemDemoAbility.java)***

More...

Now I think you should know that DoodleAbility has used DoodleView. You also can customize your layout like DoodleActivity. See [DoodleActivity](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleAbility.java) for more details.

***现在你应该知道DoodleAbility就是使用了DoodleView实现涂鸦，你可以参照[DoodleAbility](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleAbility.java)是怎么实现涂鸦界面的交互来实现自己的自定义页面。***

DoodleView has implemented IDoodle.

***DoodleView实现了IDoodle接口。***
```java
public interface IDoodle {
...
    public float getUnitSize();
    public void setDoodleRotation(int degree);
    public void setDoodleScale(float scale, float pivotX, float pivotY);
    public void setPen(IDoodlePen pen);
    public void setShape(IDoodleShape shape);
    public void setDoodleTranslation(float transX, float transY);
    public void setSize(float paintSize);
    public void setColor(IDoodleColor color);
    public void addItem(IDoodleItem doodleItem);
    public void removeItem(IDoodleItem doodleItem);
    public void save();
    public void topItem(IDoodleItem item);
    public void bottomItem(IDoodleItem item);
    public boolean undo(int step);
...
}
```
// Framework diagram 框架图

![structure](https://raw.githubusercontent.com/1993hzw/common/master/Doodle/structure.png)

// Doodle Coordinate 涂鸦坐标
![coordinate](https://raw.githubusercontent.com/1993hzw/common/master/Doodle/doodle_coordinate.png)

// Extend 拓展

You can create a customized item like [DoodlePath, DoodleText, DoodleBitmap](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib) which extend [DoodleItemBase](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleItemBase.java) or implement [IDoodleItem](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleItem.java). 

***实现[IDoodleItem](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleItem.java)接口或基础[DoodleItemBase](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleItemBase.java)，用于创建自定义涂鸦条目item，比如[DoodlePath, DoodleText, DoodleBitmap](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib)***

You can create a customized pen like DoodlePen which implements [IDoodlePen](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodlePen.java).

***实现[IDoodlePen](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodlePen.java)接口用于创建自定义画笔pen，比如DoodlePen***

You can create a customized shape like DoodleShape which implements [IDoodleShape](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleShape.java). 

***实现[IDoodleShape](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleShape.java)接口用于创建自定义形状shape，比如DoodleShape***

You can create a customized color like DoodleColor which implements [IDoodleColor](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleColor.java). 

***实现[IDoodleColor](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleColor.java)接口用于创建自定义颜色color，比如DoodleColor***

You can create a customized touch gesture detector like [DoodleTouchDetector](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleTouchDetector.java)([GestureListener](https://gitee.com/chinasoft_ohos/Doodle/tree/master/doodlelib/src/main/java/com/example/doodlelib/DoodleOnTouchGestureListener.java)) which implements [IDoodleTouchDetector](https://gitee.com/chinasoft_ohos/Doodle/blob/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleTouchDetector.java). 

***实现[IDoodleTouchDetector](https://gitee.com/chinasoft_ohos/Doodle/blob/master/doodlelib/src/main/java/com/example/doodlelib/core/IDoodleTouchDetector.java)接口用于创建自定义手势识别器，比如DoodleTouchDetector***

**所有属性都可以通过各自的getter和setter在运行时更改它们。**

#### 测试信息

CodeCheck代码测试无异常

CloudTest代码测试无异常

病毒安全检测通过

当前版本demo功能与原组件基本无差

#### 版本迭代

- 1.0.1

#### 版权和许可信息

  ```
  MIT License
  
  Copyright (c) 2018 huangziwei
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  ```
