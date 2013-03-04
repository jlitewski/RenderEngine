package com.hackhalo2.rendering.interfaces.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventMethod {
	public Priority priority() default Priority.NORMAL; //The Priority this EventMethod has
	public boolean cancelOverride() default false; //The flag to ignore a cancelled event
}
