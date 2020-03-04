package com.cai2yy.armot.utils.mqttclient;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.cai2yy.armot.api.bean.Device;
import com.cai2yy.armot.api.service.DeviceService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Arrays;

/**
 * @author Cai2yy
 * @date 2020/2/21 23:02
 */
@Singleton
public class MqttDecoder {

    @Inject
    @Named("deviceService")
    DeviceService deviceService;

    public Device decodeJson(MqttMessage msg) {
        String msgStr = Arrays.toString(msg.getPayload());
        if (!isJson(msgStr)) {
            //todo 其他的解码方式
            return null;
        }
        String[] data = msgStr.split(",");
        String[] properties = new String[data.length];
        String[] values = new String[data.length];
        int var1 = 0;
        //todo 用fastjson优化
        for (String datum : data) {
            datum = datum.trim();
            String property = datum.split(":")[0];
            property = property.substring(1, property.length() - 1);
            properties[var1] = property;
            String value = datum.split(":")[1];
            value = value.substring(1, property.length() - 1);
            values[var1++] = value;
        }
        return deviceService.createDevice(properties, values);

    }

    public boolean isJson(String msg) {
        if (msg.charAt(0) == '{' && msg.charAt(msg.length() - 1) == '}') {
            return true;
        }
        return false;
    }
}
