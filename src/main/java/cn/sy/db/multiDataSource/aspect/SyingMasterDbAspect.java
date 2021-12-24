package cn.sy.db.multiDataSource.aspect;

import cn.sy.db.multiDataSource.DbContextHolder;
import cn.sy.db.multiDataSource.annotation.SyingMasterDbAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SyingMasterDbAspect implements Ordered {
    @Around("@annotation(syingMasterDbAnnotation)")
    public Object proceed(ProceedingJoinPoint joinPoint, SyingMasterDbAnnotation syingMasterDbAnnotation) throws Throwable {
        try {
            DbContextHolder.setDbType(DbContextHolder.DbType.MASTER);
            return joinPoint.proceed();
        } finally {
            DbContextHolder.clearType();
        }

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
