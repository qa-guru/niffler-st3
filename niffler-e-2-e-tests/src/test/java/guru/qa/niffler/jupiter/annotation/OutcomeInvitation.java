package guru.qa.niffler.jupiter.annotation;

public @interface OutcomeInvitation {

	boolean handleAnnotation() default true;

	int count() default 1;
}
