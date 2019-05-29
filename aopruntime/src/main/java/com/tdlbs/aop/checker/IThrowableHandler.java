package com.tdlbs.aop.checker;

public interface IThrowableHandler {

    /**
     * 处理异常
     *
     * @param flag      异常的标志
     * @param throwable 捕获到的异常
     * @return Obj
     */
    Object handleThrowable(String flag, Throwable throwable);
}