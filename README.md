ArmOT
---
边缘计算IOT软件+数据上云web端管理平台

Edge-Side：整合到IOT智能硬件内，可控制设备并在本地进行逻辑处理
- 边缘计算 = 本地计算能力 + 网络能力

Web-Side：一个IOT智能硬件管理平台，可通过RPC方式远程对E端智能设备下达指令或实时同步采集相关数据

进度：[|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
----------------] 

　　　Web端框架, E端框架, Iot通信, 小米插座Demo 其他
    
> 当前git页仅包括Web端部分

知识点
---
#### 1. web服务器框架
基于netty实现的 -> 简单web服务器

cjhttp: https://github.com/cai2yy/cjhttp

#### 2. 应用框架
- 基于JDK反射、注解机制实现的 -> IOC+全局单例控制

    cjioc: https://github.com/cai2yy/cjioc
- 基于此搭建的 -> 后端MVC框架

#### 3. 应用核心机制
- 第三方插件可插拔，提供开发脚手架
- 全局统筹的异步任务管理机制
- 事件总线和事件-触发机制
- 通过mqtt实现的RPC调用

#### 4. 采用的第三方轮子
web端:
- netty -> http容器的底层框架,线程池以及Future回调
- freemarker -> 前端静态模板引擎
- mqttv3 -> mqtt模块

E端:
- zigbee2mqtt (node.js) -> 实现zigbee和mqtt的通信协议转换

#### 5. 硬件
- 小米智能插座（zigbee）
- zigbee Dongle（抓包器）

#### 6. 不足
- 高并发的优化
- 没有做基于数据库的持久化
- 轮子都是自己搭的，非常简陋，性能和稳定性难以胜任工业应用场景

Demo
---
通过web端远程读取并控制小米智能插座（相当于云端网关）， 此外还具有平台管理功能，可管理多个设备

测试
---
localhost:8080/armot

localhost:8080/armot/device

localhost:8080/armot/device/0

localhost:8080/armot/device/new

Cai2yy
---
Java, Python, Node.js, Love 

https://github.com/cai2yy

- ArmOT: 边缘计算IOT软件+数据上云web端管理平台
> https://github.com/cai2yy/armot
- CJHttp: 基于netty实现的轻便web框架（http）
> https://github.com/cai2yy/cjhttp
- CJIoc：多功能的轻量级IOC框架
> https://github.com/cai2yy/cjioc
- CJEviter: 模仿node.js中eventEmitter类的JAVA实现
> https://github.com/cai2yy/cjeviter
