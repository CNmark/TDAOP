package com.tdlbs.aop;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.tdlbs.aop.checker.IThrowableHandler;
import com.tdlbs.aop.checker.Interceptor;
import com.tdlbs.aop.logger.ILogger;
import com.tdlbs.aop.logger.TDLogger;
import com.tdlbs.aop.util.Strings;


public final class TDAOP {

    private static Context sContext;

    /**
     * 自定义拦截切片的拦截器接口
     */
    private static Interceptor sInterceptor;

    /**
     * 自定义的异常处理者接口
     */
    private static IThrowableHandler sIThrowableHandler;

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        sContext = application.getApplicationContext();
    }

    /**
     * 获取全局上下文
     *
     * @return
     */
    public static Context getContext() {
        testInitialize();
        return sContext;
    }

    private static void testInitialize() {
        if (sContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 TDAOP.init() 初始化！");
        }
    }



    //============自定义拦截器设置=============//

    /**
     * 设置自定义拦截切片的拦截器接口
     *
     * @param sInterceptor 自定义拦截切片的拦截器接口
     */
    public static void setInterceptor(@NonNull Interceptor sInterceptor) {
        TDAOP.sInterceptor = sInterceptor;
    }

    public static Interceptor getInterceptor() {
        return sInterceptor;
    }

    //============自定义捕获异常处理=============//

    /**
     * 设置自定义捕获异常处理
     *
     * @param sIThrowableHandler 自定义捕获异常处理
     */
    public static void setIThrowableHandler(@NonNull IThrowableHandler sIThrowableHandler) {
        TDAOP.sIThrowableHandler = sIThrowableHandler;
    }

    public static IThrowableHandler getIThrowableHandler() {
        return sIThrowableHandler;
    }

    //============日志打印设置=============//

    /**
     * 设置是否打开调试
     *
     * @param isDebug
     */
    public static void debug(boolean isDebug) {
        TDLogger.debug(isDebug);
    }

    /**
     * 设置调试模式
     *
     * @param tag
     */
    public static void debug(String tag) {
        TDLogger.debug(tag);
    }

    /**
     * 设置打印日志的等级（只打印改等级以上的日志）
     *
     * @param priority
     */
    public static void setPriority(int priority) {
        TDLogger.setPriority(priority);
    }

    /**
     * 设置日志打印时参数序列化的接口方法
     *
     * @param sISerializer
     */
    public static void setISerializer(@NonNull Strings.ISerializer sISerializer) {
        TDLogger.setISerializer(sISerializer);
    }

    /**
     * 设置日志记录者的接口
     *
     * @param logger
     */
    public static void setLogger(@NonNull ILogger logger) {
        TDLogger.setLogger(logger);
    }

}
