package lib.cjeventEmitter;

import java.util.List;
import java.util.Map;

/**
 * @author Cai2yy
 * @date 2020/2/15 15:48
 */

public interface EventEmitter {

    Map<String, List<Func>> registry();

    /**
     * register a listener(function) on a holding object with a keyword
     *
     * @param emitter a holding object, the caller of emit()
     * @param event a string as keyword for calling event listeners
     * @param funcName the name of listener(a function)
     */
    void on(EventEmitter emitter, String event, String funcName) throws Exception;

    /**
     * register a listener(function) on a holding object with a keyword, which listener can only be triggered once
     *
     * @param emitter a holding object, the caller of emit()
     * @param event a string as keyword for calling event listeners
     * @param funcName the name of listener(a function)
     */
    void once(EventEmitter emitter, String event, String funcName) throws Exception;

    /**
     * remove the specified listener(function) bound on the caller by the keyword
     *
     * @param event a string as keyword for calling event listeners
     * @param funcName the name of listener(a function)
     */
    void removeListener(String event, String funcName) throws Exception;

    /**
     * remove all listeners(function) bound on the caller by the keyword
     *
     * @param event a string as keyword for calling event listeners
     */
    void off(String event) throws Exception;

    /**
     * trigger the listener(function) bound on this event keyword and execute with arguments
     *
     * @param event a string as keyword for calling event listeners
     * @param args listener`s execution arguments
     */
    void emit(String event, Object... args) throws Exception;

    void onAddListener() throws Exception;

    void onRemoveListener() throws Exception;

    static interface Func {
        EventEmitter Emitter();
        String FuncName();
        boolean flash();
    }

}
