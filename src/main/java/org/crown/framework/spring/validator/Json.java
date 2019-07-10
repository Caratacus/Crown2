package org.crown.framework.spring.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * <p>
 * Json校验注解
 * </p>
 *
 * @author Caratacus
 * @since 2019-05-30
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JsonConstraintValidator.class)
public @interface Json {

    /**
     * @Description: 错误提示
     */
    String message() default "请输入正确的Json格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
