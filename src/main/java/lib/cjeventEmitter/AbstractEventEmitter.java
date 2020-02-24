package lib.cjeventEmitter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Cai2yy
 * @date 2020/2/15 13:12
 */

public class AbstractEventEmitter implements EventEmitter {

    Map<String, List<Func>> registry;

    public Map<String, List<Func>> registry() {
        return registry;
    }

    public AbstractEventEmitter() {
        this.registry = new HashMap<String, List<Func>>();
    }

    public void on(EventEmitter emitter, String event, String funcName) {
        doBind(emitter, event, funcName, false);
    }

    public void once(EventEmitter emitter, String event, String funcName) {
        doBind(emitter, event, funcName, true);
    }

    private void doBind(EventEmitter emitter, String event, String funcName, boolean flash) {
        List<Func> listeners = emitter.registry().getOrDefault(event, new ArrayList<Func>());
        listeners.add(new Pair(this, funcName, flash));
        emitter.registry().put(event, listeners);
        // trigger built-in event
        onAddListener();
    }

    public void removeListener(String event, String funcName) {
        List<Func> listeners = this.registry.get(event);
        listeners.removeIf(listener -> listener.Emitter() == this && listener.FuncName().equals(funcName));
        if (listeners.size() == 0) {
            this.registry.remove(event);
        }
        // trigger built-in event
        onRemoveListener();
    }

    public void off(String event) {
        Map<String, List<Func>> listeners = this.registry;
        listeners.remove(event);
        // trigger built-in event
        onRemoveListener();
    }

    public void emit(String event, Object... args) throws Exception {
        if (this.registry.get(event) == null) {
            return;
        }
        List<Func> listeners = this.registry.get(event);
        // trigger every listener
        Func deleteListener = null;
        for (Func listener : listeners) {
            EventEmitter emitter = listener.Emitter();
            String funcName = listener.FuncName();
            var o = new Object();
            Method[] methods = emitter.getClass().getDeclaredMethods();
            Method func = null;
            for (Method method : methods) {
                if (funcName.equals(method.getName()) && method.getParameterCount() == args.length) {
                    int var1 = 0;
                    func = method;
                    for (Class<?> paramType : method.getParameterTypes()) {
                        if (!typeEqual(paramType, args[var1++].getClass())) {
                            func = null;
                            break;
                        }
                    }
                    if (func != null)
                        break;
                }
            }
            if (func == null) {
                throw new Exception("params do not match");
            }
            // remove the listener if this event can only triggered once
            func.setAccessible(true);
            func.invoke(emitter, args);
            if (listener.flash()) {
                deleteListener = listener;
            }
        }
        if (deleteListener != null) {
            listeners.remove(deleteListener);
            if (listeners.size() == 0) {
                this.registry.remove(event);
            }
            onRemoveListener();
        }

    }

    public void onAddListener() {
        if (this.registry.get("addListener") != null) {
            try {
                emit("addListener");
            } catch (Exception ignored) {
            }
        }
        // add default listener for "addListener" event
    }

    public void onRemoveListener() {
        if (this.registry.get("removeListener") != null) {
            try {
                emit("removeListener");
            } catch (Exception ignored) {
            }
        }
        // add default listener for "removeListener" event
    }

    /** 基础类型和封装类型的转换 */
    private boolean typeEqual(Class<?> askedParamType, Class<?> offeredParamType) {
        if (
                (askedParamType.getName().equals("boolean") && offeredParamType.getSimpleName().equals("Boolean")) ||
                (askedParamType.getName().equals("byte") && offeredParamType.getSimpleName().equals("Byte")) ||
                (askedParamType.getName().equals("short") && offeredParamType.getSimpleName().equals("Short")) ||
                (askedParamType.getName().equals("char") && offeredParamType.getSimpleName().equals("Character")) ||
                (askedParamType.getName().equals("int") && offeredParamType.getSimpleName().equals("Integer")) ||
                (askedParamType.getName().equals( "long") && offeredParamType.getSimpleName().equals("Long")) ||
                (askedParamType.getName().equals("double") && offeredParamType.getSimpleName().equals("Double")) ||
                (askedParamType.getName().equals("float") && offeredParamType.getSimpleName().equals("Float"))
        )
            return true;
        return askedParamType.equals(offeredParamType);
    }

    static class Pair implements EventEmitter.Func {
        EventEmitter emitter;
        String funcName;
        boolean flash;

        Pair(EventEmitter listener, String funcName) {
            this(listener, funcName, false);
        }

        Pair(EventEmitter emitter, String funcName, boolean flash) {
            this.emitter = emitter;
            this.funcName = funcName;
            this.flash = flash;
        }

        public EventEmitter Emitter() {
            return emitter;
        }

        public String FuncName() {
            return funcName;
        }

        public boolean flash() {
            return flash;
        }
    }

}
