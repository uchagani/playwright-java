package com.microsoft.playwright.junit;

import com.microsoft.playwright.junit.impl.BrowserExtension;
import com.microsoft.playwright.junit.impl.OptionsExtension;
import com.microsoft.playwright.junit.impl.PlaywrightExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith({OptionsExtension.class, PlaywrightExtension.class, BrowserExtension.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UsePlaywright {
  Class<? extends OptionsFactory> optionsFactory() default DefaultOptionsFactory.class;
}
