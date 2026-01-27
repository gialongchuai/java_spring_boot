package com.example.demo.configuration;

import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.example.demo.util.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PreFilter extends OncePerRequestFilter {
    JwtService jwtService;
    UserService userService;
    CustomUserDetailsService customUserDetailsService;

    // nhận request trc khi vào api -> hứng
    // nhưng đối api nằm trong white list không cần token ví dụ auth
    // thì cũng vào đây nên việc cofig phía dưới là nếu
    // token blank hoặc không bắt đầu với Bearer thì filterChain.doFilter(request, response) cho nó đi tiếp luôn.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("==== preFilter ====");

        // Xem thử token gửi trong các phần request
        // tức là phần bearer người dùng gửi kèm theo trong postman
        final String authorization = request.getHeader("Authorization"); // chỉ đụng đúng tên là Authorization trong header nhé !!!
        log.info("Authorization: {}", authorization); // Authorization: Bearer eyJhbGciOiJIUzI1...

        if(StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Nếu có thì
        final String token = authorization.substring("Bearer ".length());
        log.info("Token: {}", token); // Token: eyJhbGciOiJIUzI1NiJ9...

        // Lấy username thông qua subject token
        final String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);
        log.info("Username: {}", username); // sername: sysadmin

        if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Mỗi lần 1 request là 1 lần gội tới db truy vấn thông qua username trả về userDetails
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if(jwtService.isValid(token, TokenType.ACCESS_TOKEN, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
;        }

        filterChain.doFilter(request, response);
    }
}
