package com.cai2yy.armot.core;

import com.cai2yy.armot.api.bean.Device;
import lib.cjioc.iockids.Injector;

import java.util.Random;

/**
 * @author Cai2yy
 * @date 2020/2/26 12:57
 */

//todo 容器化思路
public class ArmOTContextBus {

    public static void initNewContext() {
        Injector injector = new Injector();
        ArmOT armOT = injector.getInstance(ArmOT.class).asyncInit();
        System.out.println(new Random().nextInt() + " 初始化新连接...");
        armOT.getDevices().put(0, new Device("XiaomiSwitch", 0));
    }

    private static void registerAssemble() {

    }

}
