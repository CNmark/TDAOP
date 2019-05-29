package com.tdlbs.aop.aspectj;

import com.tdlbs.aop.TDAOP;
import com.tdlbs.aop.annotation.Intercept;
import com.tdlbs.aop.logger.TDLogger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class InterceptAspectJ {

    @Pointcut("within(@com.tdlbs.aop.annotation.Intercept *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.tdlbs.aop.annotation.Intercept * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.tdlbs.aop.annotation.Intercept *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    } //构造器切入点

    @Around("(method() || constructor()) && @annotation(intercept)")
    public Object aroundJoinPoint(ProceedingJoinPoint joinPoint, Intercept intercept) throws Throwable {
        if (TDAOP.getInterceptor() == null) {
            return joinPoint.proceed(); //没有拦截器不执行切片拦截
        }
        //执行拦截操作
        boolean result = proceedIntercept(intercept.value(), joinPoint);
        TDLogger.d("拦截结果:" + result + ", 切片" + (result ? "被拦截！" : "正常执行！"));
        return result ? null : joinPoint.proceed();
    }

    /**
     * 执行拦截操作
     *
     * @param types     拦截的类型集合
     * @param joinPoint 切片
     * @return {@code true}: 拦截切片的执行 <br>{@code false}: 不拦截切片的执行
     */
    private boolean proceedIntercept(int[] types, JoinPoint joinPoint) throws Throwable {
        for (int type : types) {
            if (TDAOP.getInterceptor().intercept(type, joinPoint)) { //拦截执行
                return true;
            }
        }
        return false;
    }

}