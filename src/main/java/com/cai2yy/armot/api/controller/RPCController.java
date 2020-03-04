package com.cai2yy.armot.api.controller;

import com.cai2yy.armot.api.bean.Component;
import com.cai2yy.armot.api.service.ComponentService;
import lib.cjhttp.server.Controller;
import lib.cjhttp.server.HttpContext;
import lib.cjhttp.server.HttpRequest;
import lib.cjhttp.server.Router;
import lib.cjioc.iockids.Injector;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cai2yy
 * @date 2020/2/24 12:02
 */

public class RPCController implements Controller {

    int functionId;

    @Inject
    @Named("ComponentService")
    ComponentService componentService;

    @Override
    public Router route() {
        return new Router()
                .handler("/int", "GET", this::rpc);
    }

    public static void main(String[] args) {
        int x = 65538;
        int low = 0;
        int high = 1;
        System.out.println(Integer.toBinaryString(65535));
        System.out.println(x >> 16);
        System.out.println(x & 65535);
    }

    public void rpc(HttpContext ctx, HttpRequest req) {
        String path = req.path();
        int pathId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        int componentId = pathId >> 16;
        int methodId = pathId & 65535;
        Method method = componentService.getMethod(componentId, methodId);
        if (method.getParameterCount() > 0) {
            ctx.abort(403, "请输入参数");
            return;
        }
        try {
            System.out.println("执行" + componentService.getComponentName(componentId) + "." + method.getName());
            method.invoke(null);
        } catch (Exception ignored) {
        }
    }



}
