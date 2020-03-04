package com.cai2yy.armot.components;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cai2yy.armot.utils.Conf;
import com.cai2yy.armot.utils.mqttclient.MqttClientAOT;

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

    public static void main(String[] args) {
        toggle();
    }

    public static void toggle() {
        MqttClientAOT mqttClientAOT = new MqttClientAOT();
        String topic = "zigbee2mqtt/" + Conf.XIAOMISWITCH_FRIENDLYNAME + "/set";
        String playloadStr = "{\"state\":\"TOGGLE\"}";
        mqttClientAOT.publishMessage(topic, playloadStr, 1);
    }

    public void turnOff() {
        System.out.println("小米插座关闭了");
    }

    public void turnSwitch() {

    }
}
