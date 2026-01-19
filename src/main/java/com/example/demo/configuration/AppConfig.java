package com.example.demo.configuration;

import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppConfig implements WebMvcConfigurer {

    UserService userService;
    PreFilter preFilter;
    String[] WHITE_LIST = {"/auth/**"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:63342")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // Mã hóa mật khẩu password
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Sau khi thêm depen security thì swagger yêu cầu phải username + password
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                // thiết lập api đc phép request non token
                // ex: ngoại trừ auth thì phải kèm token
                .authorizeHttpRequests(author
                        -> author.requestMatchers(WHITE_LIST).permitAll().anyRequest().authenticated())
                .sessionManagement(manager
                        // ko cho phép lưu token vòa session server
                        -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // prefilter lọc trước khi cho phép tới các api
                // gội tớ provider tới userDetails cho phép
                // truy vấn vào database kiểm tra userByUsername
                .authenticationProvider(provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // Thiết lập bảo vệ web cho phép swagger hiển thị
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity
                .ignoring()
                .requestMatchers(
                        "/actuator/**",
                        "/v3/**",
                        "/webjars/**",
                        "/swagger-ui*/*swagger-initializer.js",
                        "/swagger-ui*/**",
                        "/favicon.ico"
                );
    }

    // Có thể là role user truy cập hệ thống -> quản lý
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    // Cho phép truy cập tầng DAO truy vấn DB
    // Cho user impl UserDetail của spring security
    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService.userDetailsService());
        provider.setPasswordEncoder(getPasswordEncoder());

        return provider;
    }
}
