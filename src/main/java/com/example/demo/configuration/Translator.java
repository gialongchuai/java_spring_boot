package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {
    private static MessageSource messageSource;

    private Translator(@Autowired MessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }
}
