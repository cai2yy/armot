package com.cai2yy.armot.utils.mqtt;


import lib.cjioc.iockids.Injector;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.cai2yy.armot.core.ArmOT;

/**
 * 接受mqtt数据的工具类，提供自动初始化封装
 */
public class MqttReceiveMessage {

    private static int QoS = 1;
    private static String Host = "tcp://127.0.0.1:1884";
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;
    private static MqttClient mqttClient  = null;
    private static ArmOT armOT = null;

    MqttReceiveMessage() {
        MqttReceiveMessage.armOT = Injector.getInjector().getInstance(ArmOT.class);
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
                MqttReceiveCallback mqttReceiveCallback = new MqttReceiveCallback(armOT);
                System.out.println("由接收数据工具类创建mqttClient");
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
        }
        // 通过该静态方法直接初始化并重新接收数据
        else {
            init(String.valueOf(MqttClientAOT.mqttMaxId.getAndIncrement()));
            receive(topic);
        }
    }


}

