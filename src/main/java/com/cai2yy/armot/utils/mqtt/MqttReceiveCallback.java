package com.cai2yy.armot.utils.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.cai2yy.armot.core.ArmIot;


public class MqttReceiveCallback  implements MqttCallback {

    ArmIot armIot;

    MqttReceiveCallback(ArmIot armIot) {
        this.armIot = armIot;
        System.out.println(this.armIot);
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("Client 接收消息主题 : " + topic);
        System.out.println("Client 接收消息Qos : " + message.getQos());
        System.out.println("Client 接收消息内容 : " + new String(message.getPayload()));
        //this.armIot.eventBus.fireEvent("receiveMqttMsg", message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
