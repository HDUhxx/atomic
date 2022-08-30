# RushOrm
RushOrm replaces the need for SQL by mapping java classes to SQL tables.

### 概述

### What projects is it right for?
<ul>
    <li>Projects with complex data structures</li>
    <li>Projects that want to implement database storage in the minimum amount of time</li>
    <li>Projects that talk to a rest api</li>
</ul>
<hr>

### Why was RushOrm written?
<ul>
    <li>Complex relationships - RushObjects support Lists of other RushObjects</li>
    <li>SQL free migration</li>
    <li>Easily extendable</li>
    <li>No dependencies</li>
    <li>Support asynchronous call</li>
    <li>Be fast through compound inserts and selects</li>
    <li>Support importing and exporting data to and from JSON</li>
    <li>Unique ids to support merging databases</li>
    <li>Supports conflict resolution when importing data</li>
</ul>
While there are a number of other ORMs, the areas many seem to fall short is the support of 'one to many' relationships, migration and extensions. While claiming all the same basic feature of most other ORMs RushOrm supports 'List' properties without having to add the parent object to it's children. It also handles migrating the class structure without any SQL scripts being required by the developer. Finally it is designed with the understanding that not every situation can be anticipated so instead it can be easily customized.
<hr>

### 演示效果
<img src="screenshot/rushorm.gif" width="50%"/>

### 集成

```javascript
方式一：
通过library生成har包，添加har包到libs文件夹内
在entry的gradle内添加如下代码
implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])

方式二：
allprojects{
    repositories{
        mavenCentral()
    }
}
implementation 'io.openharmony.tpc.thirdlib:RushOrm_rushohos:1.0.6' 
```

## entry运行要求
   通过DevEco studio,并下载SDK
   将项目中的build.gradle文件中dependencies→classpath版本改为对应的版本（即你的IDE新建项目中所用的版本）



### 示例

```java
1.

public class Car extends RushObject {

    public String color;
    public Engine engine;

    public String anotherField;

    @RushList(classType = Wheel.class)
    public List<Wheel> wheels;

    public Car(){
        /* Empty constructor required */
    }

    public Car(String color, Engine engine){
        this.color = color;
        this.engine = engine;
    }
}


public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(Car.class);
        classes.add(Engine.class);
        classes.add(Wheel.class);
        OhosInitializeConfig ohosInitializeConfig = new OhosInitializeConfig(getApplicationContext(), classes);
        RushOhos.initialize(ohosInitializeConfig);
    }
}



2.
        Button btnSave = (Button) findComponentById(ResourceTable.Id_btn_save);
        Button btnSelect = (Button) findComponentById(ResourceTable.Id_btn_select);
        Button btnDel = (Button) findComponentById(ResourceTable.Id_btn_del);
        Text tvShow = (Text) findComponentById(ResourceTable.Id_text_show);
        //保存
        btnSave.setClickedListener(component -> {
            final long startTime = System.currentTimeMillis();
            final int numberToSave = 100;
            List<Car> cars = new ArrayList<>(numberToSave);
            for (int i = 0; i < numberToSave; i++) {
                Car car = new Car("Red", new Engine());
                car.wheels = new ArrayList<>();
                for (int j = 0; j < 4; j++) {
                    car.wheels.add(new Wheel("Michelin"));
                }
                cars.add(car);
            }

            RushCore.getInstance().save(cars, () -> {
                long endTime = System.currentTimeMillis();
                final double saveTime = (endTime - startTime) / 1000.0;
                getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        tvShow.setText("保存成功!总耗时:"+String.format("%1$,.2f s", saveTime));
                    }
                });
            });
        });
        //查询
        btnSelect.setClickedListener(component -> {
            new Thread(() -> {
                List<Car> cars = new RushSearch().find(Car.class);
                getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.error("test", "Rush main select complete====");
                        tvShow.setText("查询成功:"+JSON.toJSONString(cars));
                    }
                });
            }).start();
        });
        //删除
        btnDel.setClickedListener(component -> new Thread(() -> {
            RushCore.getInstance().deleteAll(Car.class);
            RushCore.getInstance().deleteAll(Engine.class);
            RushCore.getInstance().deleteAll(Wheel.class);
            getAbility().getContext().getUITaskDispatcher().asyncDispatch(new Runnable() {
                @Override
                public void run() {
                    tvShow.setText("删除成功");
                }
            });
        }).start());
```

### Licence Apache License, Version 2.0
Copyright (C) 2015 Stuart Campbell Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.



