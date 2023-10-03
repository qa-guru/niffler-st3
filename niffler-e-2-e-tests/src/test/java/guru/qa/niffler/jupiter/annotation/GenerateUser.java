package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface GenerateUser {

    boolean handleAnnotation() default true;

    Friend friends() default @Friend(handleAnnotation = false);

    IncomeInvitation incomeInvitations() default @IncomeInvitation(handleAnnotation = false);

    OutcomeInvitation outcomeInvitations() default @OutcomeInvitation(handleAnnotation = false);
}
