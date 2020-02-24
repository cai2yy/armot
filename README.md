ArmOT
---
边缘计算IOT软件+数据上云web端管理平台

Edge-Side：整合到IOT智能硬件内，可控制设备并在本地进行逻辑处理
- 边缘计算 = 本地计算能力 + 网络能力

Web-Side：一个IOT智能硬件管理平台，可通过RPC方式远程对E端智能设备下达指令或实时同步采集相关数据


知识点
---
#### 1. web服务器框架
基于netty实现的 -> 简单web服务器
- 绑定：
>创建Router -> 绑定各个Controller -> 在各Controller内实现Route方法，
描述url与具体HandlerFunction（该Controller内的方法）的对应关系
- 响应http请求：
> netty读取信息 -> MessageCollector -> dispatcher 
>-> Router(根路由) -> Router（执行路由）-> HandlerFunction
- 部署：
> 部署静态资源
- 作为web容器：
> - 处理连接和接收数据
>  - 底层I/O由netty实现 
> - 制定协议，封装http请求和相应
> - 派发请求，制定路由映射规则
> - 结果渲染，支持html, json
> - Cookie管理
> - 拦截器
> - TODO 集群模式
- 优点：
> - NIO阻塞I/O模型，性能良好
- 缺点：
> - 底层的I/O操作，由netty实现和管理
> - 热部署

#### 2. 应用框架
- IOC+全局单例控制
    - JDK反射
    - JDK注解
- 基于上述机制搭建的 -> 后端MVC框架

#### 3. 应用核心机制
- 全局统筹的异步任务管理机制
- 事件总线和事件-触发机制
- 第三方插件可插拔
- 通过mqtt实现的RPC调用

#### 采用的第三方轮子
- netty -> http容器的底层框架
- netty.util.concurrent: 线程池和Future/Promise机制
- freemarker -> 前端静态模板引擎
- Guava -> 异步任务回调
- mqttv3 -> mqtt模块
- zigbee2mqtt -> 实现zigbee和mqtt的通信协议转换

#### 硬件
- 小米智能插座（zigbee）
- zigbee Dongle（抓包器）

#### 不足
- 高并发的优化
- 没有做基于数据库的持久化
- 轮子都是自己搭的，非常简陋，性能和稳定性难以胜任工业应用场景

#### Demo
通过web端远程读取并控制小米智能插座（相当于云端网关），此外还具有平台管理功能，可管理多个设备


CJHttp
---

### 路由映射规则：

跟路由绑定子路由
```
router.child("/device", new NewsController());
```

子路由类（或称Controller类）
- 需实现Controller接口
- 需重写public Router route()方法，在内部定义子路由映射规则
    - 每个函数都只能绑定一级路径（例如"/", "/edit", "/int")
    - handler()函数第一个参数中的"int"为保留关键字，能映射到所有的数字类型
```
@Overide
public Router route() {
        return new Router()
                .handler("/", "GET", this::showDevice)
                .handler("/int", "GET", this::getDevice);
    
    }
``` 
- 定义具体handler（即实现函数）
    - 传参格式固定为(HttpContext ctx, HttpRequest req)
```
/** 形如"localhost:8080/device/12"的get类型http访问会被映射到该方法 */
public void getDevice(HttpContext ctx, HttpRequest req) {
        
        // 通过对req的处理提取所需参数
        int device = req.path();

        // http错误返回
        if (device == null) {
            ctx.abort(404, "错误！没有找到该设备");
            return;
        }

        // http正确返回
        ctx.render("playground.ftl", params);

    }
``` 
