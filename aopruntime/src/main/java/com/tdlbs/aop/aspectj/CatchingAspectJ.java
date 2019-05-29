package com.tdlbs.aop.aspectj;

import android.text.TextUtils;

import com.tdlbs.aop.TDAOP;
import com.tdlbs.aop.annotation.Catching;
import com.tdlbs.aop.logger.TDLogger;
import com.tdlbs.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * <pre>
 *     desc   : 自动try-catch的注解切片处理
 *     author : xuexiang
 *     time   : 2018/5/14 下午10:39
 * </pre>
 */
@Aspect
public class CatchingAspectJ {

    @Pointcut("within(@com.tdlbs.aop.annotation.Catching *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.tdlbs.aop.annotation.Catching * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(safe)")//在连接点进行方法替换
    public Object aroundJoinPoint(final ProceedingJoinPoint joinPoint, Catching safe) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            if (TDAOP.getIThrowableHandler() != null) {
                String flag = safe.value();
                if (TextUtils.isEmpty(flag)) {
                    flag = Utils.getMethodName(joinPoint);
                }
                result = TDAOP.getIThrowableHandler().handleThrowable(flag, e);
            } else {
                TDLogger.e(e); //默认不做任何处理
            }
        }
        return result;
    }
}