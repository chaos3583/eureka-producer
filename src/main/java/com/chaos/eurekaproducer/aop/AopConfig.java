package com.chaos.eurekaproducer.aop;

import com.chaos.eurekaproducer.domain.StoreTransactionLog;
import com.chaos.eurekaproducer.service.IStoreTransactionLogService;
import com.chaos.eurekaproducer.service.StoreTransactionLogServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author liaopeng
 * @title: AopConfig
 * @projectName eureka-producer
 * @description: TODO
 * @date 2021/1/279:11 下午
 */
@Component
@Aspect
public class AopConfig {


//    @DeclareParents(value="com.chaos.eurekaproducer.service.*", defaultImpl= StoreTransactionLogServiceImpl.class)
//    public static IStoreTransactionLogService ser;

    /**
     * 使用execution表达式定义连接点
     */
    @Pointcut("execution(* com.chaos.eurekaproducer.service.*.*(..))")
    public void pointCut(){}

    /**
     * 使用within表达式定义连接点
     */
    @Pointcut("within(com.chaos.eurekaproducer.service.*)")
    public void pointCutWithin(){}

    /**
     * 使用args表达式定义连接点，所有参数类型为String的且只有一个参数
     */
    @Pointcut("args(java.lang.String)")
    public void pointCutArgs(){}

    /**
     * 使用@within定义连接点，匹配所有标有Transaction注解的类
     */
    @Pointcut("@within(com.chaos.eurekaproducer.annotation.Transaction)")
    public void pointCutWithin2(){}

//    @Pointcut("@target(com.chaos.eurekaproducer.annotation.Transaction)")
//    public void pointCutTarget2(){}

    /**
     * 前置通知
     */
    @Before(value = "pointCutWithin2()")
    public void  before(){
        System.out.println("before ");
    }

    @AfterReturning(value = "pointCut()",returning = "retVal")
    public void afterReturning(Object retVal){
        System.out.println("afterReturning "+retVal);
    }

    @AfterThrowing(value = "pointCut()",throwing = "ex")
    public void afterThrowing(Exception ex){
        System.out.println("afterThrowing "+ex.getMessage());
    }

    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint){
        System.out.println(joinPoint.getArgs());
        System.out.println("after");
    }

    @Around(value = "pointCut()")
    public void around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("start around");
        Object proceed = point.proceed();
        System.out.println("end around");
        System.out.println("around result:"+proceed.toString());
    }
}
