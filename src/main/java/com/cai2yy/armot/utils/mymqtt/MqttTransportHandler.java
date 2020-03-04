package com.cai2yy.armot.utils.mymqtt;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.mqtt.*;

/**
 * @author Cai2yy
 * @date 2020/2/29 21:34
 */

public class MqttTransportHandler extends ChannelInboundHandlerAdapter {

    /*
    mosquitto_pub -h localhost -p 1884 -t "sensors" -m "{\"serialNumber\":\"SN-002\", \"model\":\"T1000\", \"temperature\":34}"

    mosquitto_sub -h localhost -p 1884 -t "sensors/#"

    mosquitto.exe -c mosquitto.conf
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MqttMessage) {
            System.out.println("接收到mqtt信息了");
        }
        else {
            ctx.close();
        }
    }

    private void processMqtt(ChannelHandlerContext ctx, MqttMessage msg) {

        MqttMessageType messageType = msg.fixedHeader().messageType();
        switch (messageType) {
            case CONNECT:
                processConnect(ctx, (MqttConnectMessage) msg);
                break;
            case PUBLISH:
                processPublish(ctx, (MqttPublishMessage) msg);
                break;
            case SUBSCRIBE:
                processSubscribe(ctx, (MqttSubscribeMessage) msg);
                break;
            case UNSUBSCRIBE:
                processUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
                break;
            case PINGREQ:
                break;
            case DISCONNECT:
                processDisconnect(ctx, msg);
                break;
            default:
                break;
        }
    }

    private void processConnect(ChannelHandlerContext ctx, MqttConnectMessage msg) {

    }

    private void processPublish(ChannelHandlerContext ctx, MqttPublishMessage msg) {

    }

    private void processSubscribe(ChannelHandlerContext ctx, MqttSubscribeMessage msg) {
        for (MqttTopicSubscription subscription : msg.payload().topicSubscriptions()) {
            String topic = subscription.topicName();
            MqttQoS reqQoS = subscription.qualityOfService();
            try {

            } catch (Exception e) {
            }
        }
    }

    private void processUnsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage msg) {

    }

    private void processDisconnect(ChannelHandlerContext ctx, MqttMessage msg) {

    }

}
