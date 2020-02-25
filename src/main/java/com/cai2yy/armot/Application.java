package com.cai2yy.armot;

import com.cai2yy.armot.api.bean.Device;
import com.cai2yy.armot.api.controller.DeviceController;
import com.cai2yy.armot.core.ArmIot;
import lib.cjhttp.server.RequestDispatcher;
import lib.cjhttp.server.Router;
import lib.cjioc.iockids.Injector;
import lib.cjhttp.server.internal.HttpServer;

/**
 * @author Cai2yy
 * @date 2020/2/21 19:16
 */

public class Application {

    /**
     *  项目启动函数
     */
    public static void main(String[] args) {

        /** 1. 初始化必要组件 */
        Injector injector = new Injector();
        ArmIot armIot = injector.getInstance(ArmIot.class).asyncInit();
        System.out.println("主程序异步运行");
        armIot.getDevices().put(0, new Device("XiaomiSwitch", 0));

        /** 2. 启动web服务 */
        // 构建路由，添加静态资源目录和子路由
        var router = new Router((ctx, req) -> {
            ctx.html("Hello, World");})
                .resource("/pub", "/static")
                .child("/device", injector.getInstance(DeviceController.class));
        // 初始化分发器，设定根url路径
        var rd = new RequestDispatcher("", router)
                .templateRoot("/tpl");

        var server = new HttpServer("localhost", 8080, 2, 16, rd);
        // 运行
        server.start();
        // 优雅关闭,为JVM添加关机钩子
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                server.stop();
            }

        });

    }


}
