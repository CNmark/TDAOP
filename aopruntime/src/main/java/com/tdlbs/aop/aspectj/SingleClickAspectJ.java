package com.tdlbs.aop.aspectj;

import android.view.View;

import com.tdlbs.aop.annotation.SingleClick;
import com.tdlbs.aop.logger.TDLogger;
import com.tdlbs.aop.util.ClickUtils;
import com.tdlbs.aop.util.Utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SingleClickAspectJ {

    @Pointcut("within(@com.xuexiang.xaop.annotation.SingleClick *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xuexiang.xaop.annotation.SingleClick * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    }  //方法切入点

    @Around("method() && @annotation(singleClick)")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint, SingleClick singleClick) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) {
                view = (View) arg;
                break;
            }
        }
        if (view != null) {
            if (!ClickUtils.isFastDoubleClick(view, singleClick.value())) {
                joinPoint.proceed();//不是快速点击，执行原方法
            } else {
                TDLogger.d(Utils.getMethodDescribeInfo(joinPoint) + ":发生快速点击，View id:" + view.getId());
            }
        }
    }
}
