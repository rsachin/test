package com.example.propertylisting.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid Password - must be 8-20 characters with at least one digit, one lowercase, one uppercase, and one special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
