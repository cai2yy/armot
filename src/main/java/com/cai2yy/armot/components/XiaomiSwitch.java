package com.cai2yy.armot.components;

import com.alibaba.fastjson.JSONObject;
import com.cai2yy.armot.utils.mqtt.MqttClientAOT;

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
        MqttClientAOT mqttClientAOT = new MqttClientAOT();
        JSONObject payload = new JSONObject();
        payload.put("on", "on");
        String topic = "";
        mqttClientAOT.publishMessage(topic, payload.toString(), 1);
    }

    public void turnOff() {
        System.out.println("小米插座关闭了");
    }

    public void turnSwitch() {

    }
}
