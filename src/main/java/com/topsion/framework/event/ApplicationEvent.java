package com.topsion.framework.event;

import java.util.EventObject;

/**
 * ApplicationEvent 继承了 Java 工具包内的 EventObject，
 * 我们是在 Java 的事件监听的基础上进行了简单的封装。
 * 后续使用观察者模式解耦代码提供了入口。
 */
public class ApplicationEvent extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
