package com.example.demo.controller;

import com.example.demo.configuration.Translator;
import com.example.demo.dto.request.EmailForgotPasswordDTO;
import com.example.demo.dto.request.ResetPasswordRequestDTO;
import com.example.demo.dto.request.SignInRequestDTO;
import com.example.demo.dto.request.TokenResetPasswordDTO;
import com.example.demo.dto.response.ResponseData;
import com.example.demo.dto.response.ResponseError;
import com.example.demo.dto.response.TokenResponse;
import com.example.demo.service.impl.AuthenticationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class AuthenticationController {
    AuthenticationServiceImpl authenticationService;

    // user login sẽ cấp cho 2 cái : accesstoken + refreshtoken
    // dựa vòa yml build cho type token này ACCESS và REFRESH
    // phân loại goọi tới api phải dùng ACCTOKEN đc cấp
    // chứ không được dùng REFRESH
    @Operation(summary = "user login", description = "API login user!")
    @PostMapping("/access")
    public ResponseData<TokenResponse> login(@RequestBody SignInRequestDTO signInRequest, HttpServletRequest httpRequest) {
        System.out.println(httpRequest);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.login.authenticated"), authenticationService.authenticate(signInRequest));
        } catch (Exception e) {
            log.error("Username or Password is incorrect, error login user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // dùng refreshtoken để yêu cầu cấp accesstoken mới do accesstoken có time ít hơn refresh
    // và bị hết hạn và tất nhiên không thể dùng type ACCESSTOKEN y/c cấp access rồi !!! (type)
    // access mới và refresh vẫn như cũ gửi thông qua header !!!
    @Operation(summary = "user refresh token", description = "API for user refresh token!")
    @PostMapping("/refresh")
    public ResponseData<TokenResponse> refresh(HttpServletRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.token.refresh.success"), authenticationService.refresh(request));
        } catch (Exception e) {
            log.error("Error refresh user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // khi người dùng muốn logout sẽ gửi kèm accessToken vào request y/c xóa token khỏi db Token
    // thường lúc gửi api khác y/c kềm accToken mà việc api hiện tại xóa token rồi
    // nhưng lúc truy cập api khác chưa kiểm tra xem token đã còn trong db hay không,
    // chẩn là ví dụ getUser/1 là phải kiểm tra existToken ... có lẻ vậy !!! Code hiện tại
    // chỉ kiểm tra username kèm trong object của token và expiry nên login thoải mái nếu token còn valid
    @Operation(summary = "user logout", description = "API for user logout!")
    @PostMapping("/logout")
    public ResponseData<String> logout(HttpServletRequest request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.logout.success"), authenticationService.logout(request));
        } catch (Exception e) {
            log.error("Error logout user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // ======================= ÔI =======================
    // Người dùng quên mật khẩu gửi kèm 1 cái email y/c thay đổi mật khẩu
    // api này tạo 1 curl tới email user như sau:

//    curl --location 'http://localhost:8080/auth/reset-password' \
//            --header 'accept: */*' \
//            --header 'Content-Type: application/json' \
//            --header 'Accept-Language: vi-VN' \
//            --data '{
//            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoZWhlIiwiaWF0IjoxNzY5MDQ0MDg3LCJleHAiOjE3NjkwNDc2ODd9.IQuWo2LUpDZcGnjzm5cGuzeXUwn9MK2PYv62flaO280"
    //}'
    // token thuộc type tokenReset có hạn trong 1 tiếng
    @Operation(summary = "user forgot password", description = "API for user when they forgot password!")
    @PostMapping("/forgot-password")
    public ResponseData<String> forgotPassword(@RequestBody EmailForgotPasswordDTO request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.password.change.info.sent.success"), authenticationService.forgotPassword(request));
        } catch (Exception e) {
            log.error("Error change information user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // người dùng bấm vào curl trên trong email của mình có kèm token
    // sau đó nếu validate đúng hết tự động nhảy đến form điền newPass và confirmNewPass bên dưới
    @Operation(summary = "user reset password", description = "API for user reset password!")
    @PostMapping("/reset-password")
    public ResponseData<String> resetPassword(@RequestBody TokenResetPasswordDTO tokenResetPassword) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.password.change.info.sent.success"), authenticationService.resetPassword(tokenResetPassword));
        } catch (Exception e) {
            log.error("Error change information user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    // validate 2 cái pass gửi, còn token được gửi kèm khi người dùng bấm vào nút change password của mình.
    @Operation(summary = "user change password", description = "API for user change password!")
    @PostMapping("/change-password")
    public ResponseData<String> changePassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), Translator.toLocale("auth.password.change.success"), authenticationService.changePassword(request));
        } catch (Exception e) {
            log.error("Error change password user: {} {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }
}
