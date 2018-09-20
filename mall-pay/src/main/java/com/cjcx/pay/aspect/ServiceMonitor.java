package com.cjcx.pay.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceMonitor {

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容
     *
     * @param joinPoint
     */
    @Before("execution(* com..*Service.demo(..))")
    public void Before(JoinPoint joinPoint) {
        log.info("Before Completed: " + joinPoint);
        log.info("joinPoint toString(): " + joinPoint.toString());

        Object obj = joinPoint.getThis();
        log.info("joinPoint getThis: " + obj);

        Object target = joinPoint.getTarget();
        log.info("joinPoint getTarget: " + target);

        Signature signature = joinPoint.getSignature();
        log.info("joinPoint getSignature: " + signature.toString());
        log.info("joinPoint signature getName: " + signature.getName());
        log.info("joinPoint signature getModifiers: " + signature.getModifiers());
        log.info("joinPoint signature getDeclaringType: " + signature.getDeclaringType());
        log.info("joinPoint signature getDeclaringTypeName: " + signature.getDeclaringTypeName());
        log.info("joinPoint getArgs: " + Arrays.asList(joinPoint.getArgs()));
    }


    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管是否发生异常。
     *
     * @param joinPoint
     */
    @After("execution(* com..*Service.demo(..))")
    public void After(JoinPoint joinPoint) {
        log.info("After Completed: " + joinPoint);
    }

    /**
     * 返回通知：目标方法正常执行完毕时执行以下代码
     *
     * @param joinPoint
     */
    @AfterReturning(value = "execution(* com..*Service.demo(..))", returning = "result")
    public void AfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning Completed: " + joinPoint);
        log.info("AfterReturning returning result" + result);
    }


    /**
     * 异常通知：目标方法发生异常的时候执行以下代码
     *
     * @param joinPoint
     */
    @AfterThrowing(value = "execution(* com..*Service.demo(..))", throwing = "e")
    public void AfterThrowing(JoinPoint joinPoint, Exception e) {
        log.info("AfterThrowing Completed: " + joinPoint);
        log.info("AfterThrowing Exception: " + e.getMessage());
    }

    /**
     * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
     *
     * @param jp
     */
    /*@Around("execution(* com..*Service.demo(..))")
    public Object Around(ProceedingJoinPoint jp) {
        log.info("Around Completed: " + jp);
        String methodName = jp.getSignature().getName();
        Object result = null;
        try {
            System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
            //执行目标方法
            result = jp.proceed();
            System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with " + result);
        } catch (Throwable e) {
            System.out.println("【环绕通知中的--->异常通知】：the method 【" + methodName + "】 occurs exception " + e);
        }

        System.out.println("【环绕通知中的--->后置通知】：-----------------end.----------------------");
        return result;
    }*/


}
