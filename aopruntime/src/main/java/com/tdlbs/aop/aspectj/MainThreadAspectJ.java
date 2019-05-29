package com.tdlbs.aop.aspectj;

import android.os.Looper;

import com.tdlbs.aop.logger.TDLogger;
import com.tdlbs.aop.util.AppExecutors;
import com.tdlbs.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MainThreadAspectJ {

    @Pointcut("within(@com.tdlbs.aop.annotation.MainThread *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.tdlbs.aop.annotation.MainThread * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method()")//在连接点进行方法替换
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            joinPoint.proceed();
        } else {
            TDLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E2 [当前线程]:" + Thread.currentThread().getName() + "，正在切换到主线程！");
            AppExecutors.get().mainThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        TDLogger.e(e);
                    }
                }
            });
        }
    }
}