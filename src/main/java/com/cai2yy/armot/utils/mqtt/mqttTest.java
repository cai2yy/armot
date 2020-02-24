package com.cai2yy.armot.utils.mqtt;

import com.cai2yy.armot.core.ArmIot;

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
        MqttClient mqttClient = new MqttClient();
        Date date = new Date();
        mqttClient.publishMessage("world", msg + date.getTime() + " 1", 1);
        Thread.sleep(timeInterval);
        mqttClient.publishMessage("world", msg + date.getTime() + " 2", 1);
        Thread.sleep(timeInterval);
        mqttClient.publishMessage("world", msg + date.getTime() + " 3", 1);
    }

    public static void subscribe(String topic, long timeInterval) throws InterruptedException {

        ArmIot armIot = new ArmIot();

        //MyMqttReceiveMessage myMqttReceiveMessage = new MyMqttReceiveMessage(armIot);

        MqttReceiveMessage.receive(topic);
        while (true) {
            Thread.sleep(timeInterval);
        }
    }
}
