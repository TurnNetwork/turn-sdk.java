package com.bubble.rlp.wasm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RLP {
	// the order of fields in rlp list
	// starts from 0 and increment strictly
	int value() default 0;
}
