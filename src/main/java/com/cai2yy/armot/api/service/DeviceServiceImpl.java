package com.cai2yy.armot.api.service;

import com.cai2yy.armot.api.bean.Device;
import com.cai2yy.armot.core.ArmOT;
import lib.cjioc.iockids.Injector;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author Cai2yy
 * @date 2020/2/21 23:34
 */
@Singleton
@Named("DeviceService")
public class DeviceServiceImpl implements DeviceService {

    Map<Integer, Device> devices;

    public DeviceServiceImpl() {
        ArmOT armOT = Injector.getInjector().getInstance(ArmOT.class);
        devices = armOT.getDevices();
    }

    @Override
    public String getDeviceType(int deviceId) {
        //todo 读取配置yml文件，轻度数据持久化
        return devices.get(deviceId).getType();
    }

    @Override
    public Device getDevice(int deviceId) {
        return devices.get(deviceId);
    }

    @Override
    public Device createDevice(String deviceType) {
        return createDevice(deviceType, "");
    }

    @Override
    public Device createDevice(String deviceType, String deviceName) {
        var num = devices.size();
        while (devices.containsKey(num)) {
            num += 1;
        }
        Device newDevice = null;
        if (deviceName.length() == 0) {
            newDevice = new Device(deviceType, num);
        }
        else {
            newDevice = new Device(deviceType, deviceName, num);
        }
        devices.put(num, newDevice);
        return newDevice;
    }

    @Override
    public Device createDevice(String[] properties, String[] values) {
        var num = devices.size();
        while (devices.containsKey(num)) {
            num += 1;
        }
        Device newDevice = new Device(properties, values, num);
        devices.put(num, newDevice);
        return newDevice;
    }

}
