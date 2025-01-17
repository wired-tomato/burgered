package net.wiredtomato.burgered.api.event

import java.lang.invoke.MethodHandles
import java.lang.reflect.Proxy

object EventFactory {
    inline fun <reified T> loop(): Event<T> = object : Event<T> {
        private val listeners = mutableListOf<T>()
        private val invoker =
            Proxy.newProxyInstance(this::class.java.classLoader, arrayOf(T::class.java)) { _, method, args ->
                listeners.forEach { listener ->
                    MethodHandles.lookup().unreflect(method).bindTo(listener).invokeWithArguments(*args)
                }
            } as T

        override fun invoker(): T = invoker

        override fun register(listener: T) {
            listeners.add(listener)
        }

        override fun unregister(listener: T) {
            listeners.remove(listener)
        }

        override fun isRegistered(listener: T): Boolean {
            return listeners.contains(listener)
        }

        override fun clearListeners() {
            listeners.clear()
        }
    }
}