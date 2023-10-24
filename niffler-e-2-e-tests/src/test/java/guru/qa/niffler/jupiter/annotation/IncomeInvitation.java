package guru.qa.niffler.jupiter.annotation;

public @interface IncomeInvitation {

	boolean handleAnnotation() default true;

	int count() default 1;
}
