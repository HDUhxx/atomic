## XPopup
![](https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/logo.png)

## 介绍
- 内置几种了常用的弹窗，十几种良好的动画，将弹窗和动画的自定义设计的极其简单；目前还没有出现XPopup实现不了的弹窗效果。
  内置弹窗允许你使用项目已有的布局，同时还能用上XPopup提供的动画，交互和逻辑封装。
- UI动画简洁，遵循Material Design，在设计动画的时候考虑了很多细节，过渡，层级的变化
- 很好的易用性，所有的自定义弹窗只需继承对应的类，实现你的布局，然后像Ability那样，在`onCreate`方法写逻辑即可
- 性能优异，动画流畅；精心优化的动画，让你很难遇到卡顿场景

**设计思路**：
综合常见的弹窗场景，我将其分为几类：
- Center类型，就是在中间弹出的弹窗，比如确认和取消弹窗，Loading弹窗
- Bottom类型，就是从页面底部弹出，比如从底部弹出的分享窗体，知乎的从底部弹出的评论列表
- Attach类型，就是弹窗的位置需要依附于某个Component或者某个触摸点，就像系统的PopDialog效果一样
- Drawer类型，就是从窗体的坐边或者右边弹出，并支持手势拖拽；好处是与界面解耦，可以在任何界面实现DrawerLayout效果
- ImageViewer大图浏览类型，就像掘金那样的图片浏览弹窗
- FullScreen类型，全屏弹窗，看起来和Ability一样，可以设置任意的动画器；适合用来实现登录，选择性的界面效果
- Position自由定位弹窗，弹窗是自由的，你可放在屏幕左上角，右下角，或者任意地方，结合强大的动画器，可以实现各种效果

## 演示
|内置弹窗（支持复用已有布局）|列表Center弹窗|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/inset1.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/inset2.gif" width="75%"/>|

|Bottom列表弹窗 | 自定义Bottom弹窗|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/bottom1.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/bottom2.gif" width="75%"/>|

|Attach弹窗(动画优雅，智能定位，长按支持) | 自定义Attach弹窗（有气泡和没气泡）|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/attach1.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/attach2.gif" width="75%"/>|

|自定义底部弹窗| 全屏弹窗（可作为Ability替代品）|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/input.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/full.gif" width="75%"/>|

|Position自由定位弹窗(放在屏幕任意地方) | Drawer弹窗|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/position.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/drawer.gif" width="75%"/>|

|自定义弹窗和自定义动画 | 内置优雅美观的动画器，可搭配弹窗结合使用|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/custom.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/animators.gif" width="75%"/>|

|联想搜索实现，轻而易举 | ImageViewer大图浏览弹窗|
|:---:|:---:|
|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/search.gif" width="75%"/>|<img src="https://gitee.com/HarmonyOS-tpc/XPopup/raw/master/screenshot/imageviewer1.png" width="75%"/>|

|大图浏览弹窗，支持界面自定义 | 配合PageSlider使用|
|:---:|:---:|
|暂无截图|暂无截图|

|超长图片支持（图像渐变过渡，优雅从容） | 应用后台弹出（一行代码实现权限申请）|
|:---:|:---:|
|暂不支持|暂不支持|

|PartShadow局部阴影弹窗 | 向上向下都可以|
|:---:|:---:|
|暂不支持|暂不支持|

## 依赖
```
allprojects{
    repositories{
        mavenCentral()
    }
}
implementation 'io.openharmony.tpc.thirdlib:XPopup:1.1.4'
```

## entry运行要求
通过DevEco studio,并下载SDK
将项目中的build.gradle文件中dependencies→classpath版本改为对应的版本（即你的IDE新建项目中所用的版本）