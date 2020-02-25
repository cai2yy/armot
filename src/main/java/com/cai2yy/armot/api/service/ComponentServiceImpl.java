/**
 * Copyright © 2016-2019 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cai2yy.armot.api.service;

import com.cai2yy.armot.api.bean.Component;
import com.cai2yy.armot.core.ArmIot;
import lib.cjioc.iockids.Injector;
import lib.cjioc.utils.Scanner;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
@Named("ComponentService")
public class ComponentServiceImpl implements ComponentService {

    @Inject
    private ArmIot armIot;

    private String componentsPackage;

    private Map<Integer, String> componentNameDict = new HashMap<>();

    private Map<String, Component> components = new HashMap<>();

    public Optional<Component> getComponent(String clazz) {
        return Optional.ofNullable(components.get(clazz));
    }

    public String getComponentName(int ComponentId) {
        return componentNameDict.get(ComponentId);
    }

    public Class<?> getComponentType(String componentName) {
        Component component = components.get(componentName);
        return component.getClazz();
    }

    public Method getMethod(String componentName, int methodNum) {
        Component component = components.get(componentName);
        return component.getActions()[methodNum];
    }

    // 在服务器加载Servlet的时候运行，并且只会被服务器调用一次
    public int init() {
        this.armIot = Injector.getInjector().getArmIot();
        //todo 参数可修改化
        componentsPackage = "com.cai2yy.armot.components";
        return discoverComponents();
    }

    public int discoverComponents() {
        int count = scanAndPersistComponent();
        armIot.setComponents(this.components);
        return count;
    }

    private int scanAndPersistComponent() {
        List<Class<?>> componentClasses = Scanner.getClasses(componentsPackage);
        int num = 0;
        var var1 = 0;
        for (Class<?> clazz : componentClasses) {
            // 初始化component实例并放入injector缓存中
            Object obj = Injector.getInjector().getInstance(clazz);
            if (obj == null) {
                continue;
            }
            Component newComponent = new Component(clazz);
            String componentName = clazz.getSimpleName();
            componentNameDict.put(var1, componentName);
            components.put(componentName, newComponent);
            var1 += 1;
        }
        return var1;
    }

}
