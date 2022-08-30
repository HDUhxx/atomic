# Banner_ohos

**本项目是基于开源项目 Banner 进行鸿蒙化的移植和开发的，通过项目标签以及github地址（ https://github.com/youth5201314/banner ）追踪到原安卓项目版本,该项目的讲解介绍已在社区发布，可以通过网址（ https://harmonyos.51cto.com/posts/3341 ）访问相关内容。**

#### 项目介绍

- 项目名称：广告图片轮播控件
- 所属系列：鸿蒙的第三方组件适配移植
- 功能：广告轮播，循环轮播
- 项目移植状态：主功能
- 调用差异：无
- 开发版本：DevEco Studio 3.0 Beta2, SDK 5.0
- 项目发起作者：陈丛笑
- 邮箱：isrc_hm@iscas.ac.cn
- 原项目Doc地址：https://github.com/youth5201314/banner

#### 项目介绍

- 编程语言：Java 

基于PageSlide的广告图片轮播控件


#### 安装教程

1. 下载Banner的har包Banner.har（位于output文件夹下）。
2. 启动 DevEco Studio，将下载的har包，导入工程目录“entry->libs”下。



3. 在moudle级别下的build.gradle文件中添加依赖，在dependences标签中增加对libs目录下jar包的引用。
```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar', '*.har'])
	……
}
```
4. 在导入的har包上点击右键，选择“Add as Library”对包进行引用，选择需要引用的模块，并点击“OK”即引用成功。

在sdk5，DevEco Studio2.1 beta3下项目可直接运行
如无法运行，删除项目.gradle,.idea,build,gradle,build.gradle文件
并依据自己的版本创建新项目，将新项目的对应文件复制到根目录下

#### 使用说明

1. banner是一种基于鸿蒙pageslide的实现循环播放多个广告图片和手动滑动循环等功能的界面，目前虽然不支持设置翻页样式，但是功能已满足大部分要求。
原组件使用的第三方图片加载器来加载图片，本组件我们直接用list来包装图片，传入list来使用banner。
``` 
List<Integer> list=new ArrayList<>();
        list.add(ResourceTable.Media_b1);
        list.add(ResourceTable.Media_b2);
        list.add(ResourceTable.Media_b3);
        list.add(ResourceTable.Media_4);
        list.add(ResourceTable.Media_5);
         banner.setImages(list).start();
```
2.使用方法
```
setBannerStyle(int bannerStyle)	设置轮播样式（默认为CIRCLE_INDICATOR，有五种样式可以选择，其中数字样式有点小bug待修复）	
isAutoPlay(boolean isAutoPlay)	设置是否自动轮播（默认自动）	
setViewPagerIsScroll(boolean isScroll)	设置是否允许手动滑动轮播图（默认true）
update(List<?> imageUrls,List titles)	更新图片和标题	
update(List<?> imageUrls)	更新图片	
startAutoPlay()	开始轮播	1.4开始，此方法只作用于banner加载完毕-->需要在start()后执行
stopAutoPlay()	结束轮播	1.4开始，此方法只作用于banner加载完毕-->需要在start()后执行
start()	开始进行banner渲染（必须放到最后执行）			
setBannerTitles(List titles)	设置轮播要显示的标题和图片对应（如果不传默认不显示标题）	
setDelayTime(int time)	设置轮播图片间隔时间（单位毫秒，默认为2000）	
setImages(Object[]/List<?> images)	设置轮播图片(所有设置参数方法都放在此方法之前执行)	
setOnBannerClickListener(this)	设置点击事件，下标是从1开始	（废弃了）
setOnBannerListener(this)	设置点击事件，下标是从0开始	
setImageLoader(Object implements ImageLoader)	设置图片加载器	（等三方库完善可以加载网络图片）

```
#### 效果演示
![xiaoguo](https://images.gitee.com/uploads/images/2021/0310/111551_853c8754_8539978.gif "01循环播放-真机压缩版本.gif")

#### 版本迭代


- v0.2.0-alpha
1.修复banner indicator指示器
2.修复界面跳转的平滑问题
3.修复命名问题
4.优化sample


#### 版权和许可信息
banner_ohos经过[Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)授权许可.




