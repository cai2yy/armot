package com.cai2yy.armot.utils.mymqtt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Cai2yy
 * @date 2020/2/29 22:42
 */

public class MqttService {

    public static void main(String[] args) throws InterruptedException {
        MqttService mqttService = new MqttService();
        System.out.println("end");
    }

    public MqttService() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MqttTransportHandler());

        Channel serverChannel = b.bind("127.0.0.1", 1885).sync().channel();


    }


}
