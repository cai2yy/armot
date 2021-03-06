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
package com.cai2yy.armot.api.bean;

import com.cai2yy.armot.utils.consts.ComponentScope;
import com.cai2yy.armot.utils.consts.ComponentType;
import lombok.Data;

import java.lang.reflect.Method;


/**
 * @author Andrew Shvayka
 */
@Data
public class Component {

    private static final long serialVersionUID = 1L;

    private ComponentType type;
    private ComponentScope scope;
    private String name;
    private Class<?> clazz;
    private Method[] actions;

    public Component(Class<?> clazz) {
        this.clazz = clazz;
        this.name = clazz.getSimpleName();
        this.actions = clazz.getDeclaredMethods();
    }

    public Component(Component plugin) {
        this.type = plugin.getType();
        this.scope = plugin.getScope();
        this.name = plugin.getName();
        this.clazz = plugin.getClazz();
        this.actions = plugin.getActions();
    }

    //todo 重写equal()
}
