package org.crown.framework.spring.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.crown.common.utils.JacksonUtils;
import org.crown.common.utils.StringUtils;

/**
 * <p>
 * Json校验
 * </p>
 *
 * @author Caratacus
 */
public class JsonConstraintValidator implements ConstraintValidator<Json, String> {

    @Override
    public void initialize(Json json) {
    }

    /**
     * @Description: 自定义校验逻辑
     */
    @Override
    public boolean isValid(String json, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(json)) {
            return true;
        }
        return Objects.nonNull(JacksonUtils.parse(json));
    }
}