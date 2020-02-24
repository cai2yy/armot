package com.cai2yy.armot.components;

import com.alibaba.fastjson.JSONObject;
import com.cai2yy.armot.utils.mqtt.MqttClient;

import javax.inject.Singleton;

/**
 * @author Cai2yy
 * @date 2020/2/21 23:57
 */
@Singleton
public class XiaomiSwitch {

    public boolean onOff;
    public int power;
    public int electricity;

    public void turnOn() {
        MqttClient mqttClient = new MqttClient();
        JSONObject payload = new JSONObject();
        payload.put("on", "on");
        String topic = "";
        mqttClient.publishMessage(topic, payload.toString(), 1);
    }

    public void turnOff() {
        System.out.println("小米插座关闭了");
    }

    public void turnSwitch() {

    }
}
