# JustWe-WebServer
Ohos手机上的Http服务器，可以用于内网／外网的数据交换。
是JustWeEngine游戏框架中处理网络事件的一部分。

## 快速入门
1.初始化\打开\关闭： 
```java
public class MainAbility extends Ability implements OnLogResult {
    private WebServer server;
    @Override
    public void onStart(Intent intent) {
        server = new WebServer(this, this);
        server.initWebService();
    }
}
```
初始化的时候推荐实现一个OnLogResult用于接受log日志和错误。
OnLogResult的返回数据是线程安全的，可以直接传送到View中打印出来。
当然也有其他的构造方法：
```java
    public WebServer(Activity engine)；
    public WebServer(Activity engine, OnLogResult logResult, int webPort)； // 端口
```
初始化之后：
```java
    server.startWebService();
    server.stopWebService();
```
使用该方法打开监听\关闭。

2.添加路由：
```java
    server.apply("/lfk", new OnWebStringResult() {
        @Override
        public String OnResult() {
            return "=======";
        }
    });

    server.apply("/main", new OnWebFileResult() {
        @Override
        public File returnFile() {
            return new File(WebServerDefault.WebServerFiles + "/" + "welcome.html");
        }
    });
```
可以通过此种方法添加路由，并返回数据或者文件。
需要表单提交的如Post可以使用如下接口，返回一个HashMap存储key和value。
```java
    server.apply("/lfkdsk", new OnPostData() {
        @Override
        public String OnPostData(HashMap<String, String> hashMap) {
            String S = hashMap.get("LFKDSK");
            return "=_=";
        }
    });
```

3.获取／提交数据：
向服务器提交数据，只需使用正常的get / post即可。

原组件地址 : [https://github.com/lfkdsk/JustWeEngine](https://github.com/lfkdsk/JustWeEngine)

IDE版本 : 2.1.0.301

SDK版本 : 2.1.1.18
