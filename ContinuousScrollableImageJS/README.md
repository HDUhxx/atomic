# ContinuousScrollableImageJs
一个连续滚动图像效果的组件库

可自定义图像源、缩放类型、持续时间和方向等高效地显示具有连续滚动效果的图像。

### 效果图
<img src="gif/scroll.gif"/>

### 使用
1、引用自定义组件：
<element name='continuous-drawable' src='../../common/component/continuous/ContinuousScrollableImageView.hml'></element>
2、设置组件相关属性
<continuous-drawable class="plane"
                     src-resource="common/images/plane.png"
                     src-height="100px"
                     src-width="300px"
                     scale-style="contain"
                     scale-duration="9000"
                     scale-direction="normal"
                     scale-orientation="h"
            >
            
属性说明

|自定义属性| 属性说明 |默认值|
|---|---|---|
|src-resource|图片相对地址/绝对地址||
|src-height|图片高度|100px|
|src-width|图片宽度|100px|
|scale-style|图片放缩类型|none|
|src-duration|动画持续时间|3000ms|
|src-direction|动画正(normal)/反(reverse)方向|normal|
|src-orientation|竖直(v)/水平(h)|h|
注：
* 水平正方向：动画从左向右；
* 水平反方向：动画从右向左；
* 垂直正方向：动画从上向下；
* 垂直反方向：动画从下向上；

### 运行要求
SDK 6+

### LICENSE
~~~

   Copyright (c) 2021 shanghaozhi
   ContinuousScrollableImageJS is licensed under Mulan PSL v2.
   You can use this software according to the terms and conditions of the Mulan PSL v2.
   You may obtain a copy of Mulan PSL v2 at:
            http://license.coscl.org.cn/MulanPSL2
   THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
   See the Mulan PSL v2 for more details.

~~~