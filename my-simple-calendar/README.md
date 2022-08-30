# MySimpleCalendar

#### 效果演示
![my_calendar](https://images.gitee.com/uploads/images/2021/0806/093305_4bfa5ed5_8627638.gif "my_calendar.gif")
#### 使用说明
日历组件所在文件夹为js/default/common/component中mycalendar,它有引用同文件夹下的dateshow组件。  

1、is-dark属性  
is-dark属性你可以根据自己需要直接传入“0”或者“1”，0表示亮模式白色背景、1表示暗模式黑色背景，默认值为0。

2、select-date属性  
引用组件时传递属性select-date="2021-05-05",日历中会默认选择这个日期。默认值为当前日期。

3、date-click事件 替换成 sel-date事件（OpenHarmony自定义事件含click无反应）  
引用组件时传递事件@sel-date="selDate",selDate，可以从e.detail.date取出选择的日期，格式为yyyy-MM-dd。
```javascript
selDate(e){
        prompt.showToast({
            message:"当前选中的日期时："+e.detail.date,
            duration:2000
        });
    }
```

4、toCurrentDate方法  
组件的toCurrentDate方法，在引用组件页面的js中，使用this.$child('mycalendar').toCurrentDate()可以使日历跳转到当前的月份 ，其中mycalendar为组件的id属性的值。

5、使用方式参照js/default/pages下的index页面：  
```html
<element name="mycalendar"  src="../../common/component/mycalendar/index.hml"></element>
<div class="container">
    <mycalendar id="mycalendar" is-dark="{{ isDark }}" @sel-date="selDate" select-date="2021-05-05"></mycalendar>
    <button on:click="setCalendarModel" >切换日历亮暗模式</button>
    <div class="toCurrentDate_div" on:click="toCurrentDate" >
        <text class="toCurrentDate" >今</text>
    </div>
</div>
``` 
  
```javascript
import prompt from '@system.prompt';
export default {
    data: {
        isDark:0
    },
    onInit() {

    },
    toCurrentDate(){
        this.$child('mycalendar').toCurrentDate();
    },
    /**
     * 选中日期，返回给调用者
     */
    selDate(e){
        prompt.showToast({
            message:"当前选中的日期时："+e.detail.date,
            duration:2000
        });
    },
    /**
     * 切换日历模式
     */
    setCalendarModel(){
        if(this.isDark == 0){
            this.isDark = 1;
        }else{
            this.isDark = 0;
        }
    }

}
```
  
```html
.container {
    flex-direction: column;
    align-items: center;
}
.toCurrentDate_div{
    position: fixed;
    bottom: 100px;
    right: 15px;
    width: 50px;
    height: 50px;
    box-shadow :1px 1px 0px 2px cornflowerblue;
    border-radius: 25px;
    justify-content: center;
    align-items: center;
}
.toCurrentDate{
    width: 28px;
    height: 28px;
    border-width: 2px;
    border-color: cornflowerblue;
    text-align: center;
    font-size: 20px;
    color: cornflowerblue;
}
button{
    margin-top: 40px;
    width: 80%;
    height: 40px;
}
```

#### 实现思路
1、单独展示出某年某月的日历——common/component/dateshow组件  
```html
<dateshow @sel-date="selDate" year="{{year}}" month="{{monthList[0]}}" is-dark="{{this.isDark}}" select-date="{{selectDateState}}"></dateshow>
```
传入年、月、选择的日期、展示模式（亮/暗），即可把此月的日期展示起来。其日期数据处理逻辑，这个每个人都有自己的逻辑思路并非鸿蒙组件的实现，这里不多做介绍。  
2、将某年某月的日历组合起来形成日历——common/component/mycalendar组件    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如何将每个月的日历组合起来形成一个日历呢？这里我们使用了swiper组件，他可以实现左右滑动。我们内置了三个页签，实现0-1、1-2、2-0无限循环的模式来实现日历的左右滑动切换月份，
当然你要判断左滑、右滑来减、加月份展示日期数据。
```html
<swiper id="mySwiper"  style="flex: 1;"  loop="true" indicator="false" @change="changeMonth" >
        <dateshow @sel-date="selDate" year="{{year}}" month="{{monthList[0]}}" is-dark="{{this.isDark}}" select-date="{{selectDateState}}"></dateshow>
        <dateshow @sel-date="selDate" year="{{year}}" month="{{monthList[1]}}" is-dark="{{this.isDark}}" select-date="{{selectDateState}}"></dateshow>
        <dateshow @sel-date="selDate" year="{{year}}" month="{{monthList[2]}}" is-dark="{{this.isDark}}" select-date="{{selectDateState}}"></dateshow>
    </swiper>
```
3、封装成组件供他人使用  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;上面仅仅是将日历展示出来的思路，一个日历展示出来了需要给他人使用，就得考虑哪些东西是使用者可以配置的、哪些事件是使用者可以捕获然后去处理自己的业务逻辑、哪些方法是使用者可能会主动去使用的，从这几个方面考虑。  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日历提供了两种模式亮/暗（常规手机系统支持模式），所以提供is-dark属性给用户配置；用户可能需要默认选中某个日期，所以提供了select-date属性；我们选中某个日期时，用户可能会想获取选中的日期用于项目，所以提供了sel-date事件；
用户滑动，都显示到n年n月了，想回到当前月份，所以我们提供了toCurrentDate方法。  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;最后，用户可能想知道某年某月的日历，但其距离当前月份比较远，点击、滑动需要操作好久，所以我们提供点击头部年月弹出年月选择弹框，选择后我们可直接跳转到对应月份。

#### 仓库地址
[https://gitee.com/chinasoft6_ohos/my-simple-calendar](https://gitee.com/chinasoft6_ohos/my-simple-calendar)

作者：陈巧银
