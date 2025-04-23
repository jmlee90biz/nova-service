package com.sktelecom.nova.service.monolith.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class MethodTraceAspect {
    // ThreadLocal을 이용하여 호출 깊이를 추적
    private static final ThreadLocal<Integer> depth = ThreadLocal.withInitial(() -> 0);

    @Around(
            "execution(* com.sktelecom.nova..*(..)) || " +
            "execution(* com.sktelecom.nova.service.monolith.config.InternalApplicationEventPublisher.pulishEvent(..))"
            //"execution(* org.springframework.context.ApplicationEventPublisher.publishEvent(..))"
            //"execution(* org.springframework.context.ApplicationEventPublisher+..*(..))"
    ) // 패키지 경로 수정 필요
    public Object traceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();

        // 원래의 Repository 인터페이스 찾기
        Object target = joinPoint.getTarget();
        Class<?> actualClass = getRepositoryInterface(target);

        String className = actualClass.getSimpleName(); // 실제 클래스명 가져오기
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs(); // 파라미터 값 가져오기
        int currentDepth = depth.get();

        // Indentation 생성
        String indent = "\t\t".repeat(currentDepth);
        String params = formatArgs(args); // 파라미터를 문자열로 변환

        // 메서드 시작 로그 출력
        System.out.printf("[%s] %s> %s.%s(%s) Started%n", threadName, indent, className, methodName, params);

        // 깊이를 증가
        depth.set(currentDepth + 1);

        Object result = null;
        long endTime;
        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
        } finally {
            endTime = System.currentTimeMillis();
            depth.set(currentDepth); // 호출이 끝나면 깊이 감소
        }

        // 리턴 값이 null인지 확인 후 출력
        String resultStr = (result != null) ? result.toString() : "null";
        resultStr = "";
        long executionTime = endTime - startTime;

//        // **전체 로그 출력 (시간 포함)**
//        System.out.printf("[%s] %s< %s.%s() Finished (%dms) - Return: %s%n",
//                threadName, indent, className, methodName, executionTime, resultStr);

        // **시퀀스 다이어그램용 로그 (시간 제외)**
        System.out.printf("[%s] %s< %s.%s() Finished - Return: %s%n",
                threadName, indent, className, methodName, resultStr);

        return result;
    }

    // 파라미터 배열을 문자열로 변환하는 유틸리티 메서드
    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) return "No args";
        return Arrays.stream(args)
                .map(arg -> (arg != null) ? arg.toString() : "null")
                .collect(Collectors.joining(", "));
    }

    // **Spring Data JPA 프록시 객체에서 원래 인터페이스(`PartnerRepository`) 찾기**
    private Class<?> getRepositoryInterface(Object target) {
        if (target == null) return null;

        // 1️⃣ Spring이 만든 프록시 객체라면 실제 원본 클래스를 찾는다.
        Class<?> actualClass = AopProxyUtils.ultimateTargetClass(target);
        if (actualClass == null) {
            actualClass = ClassUtils.getUserClass(target);
        }

        // 2️⃣ 프록시 객체에서 원래 Repository 인터페이스를 찾는다.
        if (actualClass.getSimpleName().contains("JpaRepositoryImplementation") ||
                actualClass.getSimpleName().contains("SimpleJpaRepository") ||
                actualClass.getSimpleName().contains("$Proxy") ||
                actualClass.getSimpleName().contains("CGLIB")) {

            for (Class<?> interfaceClass : target.getClass().getInterfaces()) {
                if (Repository.class.isAssignableFrom(interfaceClass)) {
                    return interfaceClass; // 원래의 Repository 인터페이스 반환 (예: PartnerRepository)
                }
            }
        }

        return actualClass;
    }
}
