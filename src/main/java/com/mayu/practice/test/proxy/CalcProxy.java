package com.mayu.practice.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: 马瑜
 * @Date: 2024/1/17 14:47
 * @Description: 生成代理的工具类
 */
public class CalcProxy {
    // 1. JDK中Proxy类可以实现基于接口的动态代理
    // 2. 在程序执行过程中, 动态生成了代理类的字节码, 然后通过字节码生成Class对象, 再生成代理类的实例对象。
    public static Calc getCalcProxy(Calc calcObj) {
        ClassLoader classLoader = calcObj.getClass().getClassLoader();
        Class<?>[] interfaces = calcObj.getClass().getInterfaces();
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            // 核心就是这个`回调方法`，由生成后的代理对象调用 [重点要理解这个方法的调用时机, 且真正被调用时候, 才会用到 `被代理对象calcObj`]
            // 这里的proxy参数就是以后生成的`代理对象`, 代理对象里面通过反射[Class.forName(interfaces.absName)]会拿到接口里面的所有方法签名。
            // 生成的代理对象[的父类Proxy]会持有 invocationHandler, 通过invocationHandler就可以进行回调。
            // 1. 代理对象 extend Proxy implement interfaces
            // 2. 通过代理对象, 可以调用真实对象一摸一样的方法, 因为实现了一样的接口; 【所以要求被代理类必须实现接口】
            // 3. 在代理对象里面的方法中，再通过持有的invocationHandler进行对真实对象的访问
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(calcObj, args);
            }
        };
        Object proxyObj = Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        return (Calc) proxyObj;
    }
}
