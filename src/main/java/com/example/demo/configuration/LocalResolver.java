package com.example.demo.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

// Truyền header với Accept-Language : fr-FR ; vi-VN ; en-US

@Configuration
public class LocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {

    List<Locale> locales = List.of(Locale.ENGLISH
            , new Locale("mx")
            , new Locale("vi")
    );

    // Mặc định là tiếng anh, truyền vào header thì lấy ra
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String languageHeader = request.getHeader("Accept-Language");
        // Nếu không truyền gì vào header thì mặc định là lấy tếng anh default
        // còn nêếu có thì parse nó và tìm kiếm
        return StringUtils.hasLength(languageHeader) ? Locale.lookup(Locale.LanguageRange.parse(languageHeader), locales) : Locale.ENGLISH;
    }

//    @Bean
//    public ResourceBundleMessageSource bundleMessageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setBasename("messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        messageSource.setUseCodeAsDefaultMessage(true);
//        messageSource.setCacheSeconds(3600);
//        return messageSource;
//    }
}
