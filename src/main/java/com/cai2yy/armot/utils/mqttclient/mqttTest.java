package com.cai2yy.armot.utils.mqttclient;

import com.cai2yy.armot.core.ArmOT;

import java.util.Date;

/**
 * @author Cai2yy
 * @date 2020/2/21 21:06
 */

public class mqttTest {

    public static void main(String[] args) throws InterruptedException {
        publish("world", "Hello, World!", 2000);
        System.out.println("end");
    }

    public static void publish(String topic, String msg, long timeInterval) throws InterruptedException {
        // publish方法必须new一个MyMqttClient实例
        MqttClientAOT mqttClientAOT = new MqttClientAOT();
        Date date = new Date();
        mqttClientAOT.publishMessage("world", msg + date.getTime() + " 1", 1);
        Thread.sleep(timeInterval);
        mqttClientAOT.publishMessage("world", msg + date.getTime() + " 2", 1);
        Thread.sleep(timeInterval);
        mqttClientAOT.publishMessage("world", msg + date.getTime() + " 3", 1);
    }

    public static void subscribe(String topic, long timeInterval) throws InterruptedException {

        ArmOT armOT = new ArmOT();

        //MyMqttReceiveMessage myMqttReceiveMessage = new MyMqttReceiveMessage(armIot);

        MqttReceiveMessage.receive(topic);
        while (true) {
            Thread.sleep(timeInterval);
        }
    }
}
