package com.tdlbs.aop.aspectj;

import android.os.Looper;

import com.tdlbs.aop.annotation.IOThread;
import com.tdlbs.aop.logger.TDLogger;
import com.tdlbs.aop.util.AppExecutors;
import com.tdlbs.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.Callable;

@Aspect
public class IOThreadAspectJ {


    @Pointcut("within(@com.tdlbs.aop.annotation.IOThread *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.tdlbs.aop.annotation.IOThread * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(ioThread)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, IOThread ioThread) throws Throwable {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return joinPoint.proceed();
        } else {
            TDLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E2 [当前线程]:" + Thread.currentThread().getName() + "，正在切换到子线程！");
            Object result = null;
            switch (ioThread.value()) {
                case Single:
                case Disk:
                    result = AppExecutors.get().singleIO().submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return getProceedResult(joinPoint);
                        }
                    }).get();
                    break;
                case Fixed:
                case Network:
                    result = AppExecutors.get().poolIO().submit(new Callable() {
                        @Override
                        public Object call() throws Exception {
                            return getProceedResult(joinPoint);
                        }
                    }).get();
                    break;
                default:
            }
            TDLogger.d(Utils.getMethodDescribeInfo(joinPoint) + " \u21E0 [执行结果]:" + Utils.toString(result));
            return result;
        }
    }

    /**
     * 获取执行结果
     *
     * @param joinPoint
     */
    private Object getProceedResult(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            TDLogger.e(e);
        }
        return null;
    }
}