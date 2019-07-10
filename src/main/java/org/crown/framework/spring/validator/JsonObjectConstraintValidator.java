package org.crown.framework.spring.validator;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.crown.common.utils.JacksonUtils;
import org.crown.common.utils.StringUtils;

/**
 * <p>
 * JsonObject校验
 * </p>
 *
 * @author Caratacus
 * @since 2019-05-31
 */
public class JsonObjectConstraintValidator implements ConstraintValidator<JsonObject, String> {

    @Override
    public void initialize(JsonObject jsonObject) {
    }

    /**
     * @Description: 自定义校验逻辑
     */
    @Override
    public boolean isValid(String jsonObject, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isBlank(jsonObject)) {
            return true;
        }
        return Objects.nonNull(JacksonUtils.parseObject(jsonObject));
    }
}
