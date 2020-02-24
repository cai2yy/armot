package com.cai2yy.armot.utils.mqtt;


import lib.cjioc.iockids.Injector;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.cai2yy.armot.core.ArmIot;


public class MqttReceiveMessage {

    private static int QoS = 1;
    private static String Host = "tcp://127.0.0.1:1884";
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;
    private static MqttClient mqttClient  = null;
    private static ArmIot armIot = null;

    MqttReceiveMessage() {
        MqttReceiveMessage.armIot = Injector.getInjector().getArmIot();
    }

    public static void init(String clientId) {
        mqttConnectOptions = new MqttConnectOptions();
        memoryPersistence = new MemoryPersistence();
        if(null != memoryPersistence && null != clientId && null != Host) {
            try {
                mqttClient = new MqttClient(Host, clientId, memoryPersistence);
            } catch (MqttException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            System.out.println("memoryPersistence clientId Host 有空值");
        }

        if(null != mqttConnectOptions) {
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setConnectionTimeout(30);
            mqttConnectOptions.setKeepAliveInterval(45);
            if(null != mqttClient && !mqttClient.isConnected()) {
                MqttReceiveCallback mqttReceiveCallback = new MqttReceiveCallback(armIot);
                System.out.println("创建途径2");
                //todo
                mqttClient.setCallback(mqttReceiveCallback);
                try {
                    mqttClient.connect();
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                System.out.println("mqttClient is error");
            }
        }else {
            System.out.println("mqttConnectOptions is null");
        }
    }


    public static void receive(String topic) {
        int[] Qos = {QoS};
        String[] topics = {topic};
        if(null != mqttClient && mqttClient.isConnected()) {
            if(null!=topics && null!=Qos && topics.length>0 && Qos.length>0) {
                try {
                    mqttClient.subscribe(topics, Qos);
                } catch (MqttException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                System.out.println("there is error");
            }
        }else {
            init("123444");
            receive(topic);
        }
    }


}

