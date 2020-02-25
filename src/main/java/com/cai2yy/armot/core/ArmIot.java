package com.cai2yy.armot.core;

import com.cai2yy.armot.api.bean.Device;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lib.cjioc.iockids.Injector;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cai2yy.armot.api.bean.Component;
import com.cai2yy.armot.api.service.ComponentService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cai2yy
 * @date 2020/2/21 19:34
 */

@Singleton
@Data
public class ArmIot {

    @Inject
    Injector injector;

    @Inject
    EventBus eventBus;

    @Inject
    @Named("ComponentService")
    ComponentService componentService;

    EventExecutorGroup executor = new NioEventLoopGroup();

    private final static Logger LOG = LoggerFactory.getLogger(ArmIot.class);

    Map<Integer, Device> devices = new HashMap<>();
    Map<String, Component> components = new HashMap<>();

    public ArmIot() {
        Injector.getInjector().setArmIot(this);
        LOG.info("ArmIot成功创建");
    }

    public ArmIot asyncInit() {
        asyncInitComponent();
        asyncUpdateDevices();
        return this;
    }

    public void asyncInitComponent() {
        LOG.info("开始扫描插件");
        Future<Integer> initComponentFuture = executor.submit(() -> componentService.init());
        initComponentFuture.addListener(new FutureListener<>() {
            @Override
            public void operationComplete(Future<Integer> integerFuture) throws Exception {
                var count = integerFuture.getNow();
                LOG.info("---------- 扫描完毕,共有插件{}个 ----------", count);
                var var1 = 1;
                for (String name : components.keySet()) {
                    LOG.info("插件{}[{}]: {} ", var1++, name, components.get(name));
                }
            }
        });
        //todo 写入yml配置文件
    }

    public void asyncUpdateDevices() {

    }

}
