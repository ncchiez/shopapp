package com.project.shopapp.controller;


import com.nimbusds.jose.JOSEException;
import com.project.shopapp.dto.AuthenticationDTO;
import com.project.shopapp.dto.IntrospectRequest;
import com.project.shopapp.dto.LogoutRequest;
import com.project.shopapp.dto.RefreshRequest;
import com.project.shopapp.response.ApiResponse;
import com.project.shopapp.response.AuthenticationResponse;
import com.project.shopapp.response.IntrospectResponse;
import com.project.shopapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     Đăng nhập
     **/
    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> authentication(@RequestBody AuthenticationDTO request) {
        //log.info("dang nhap !");
        ApiResponse apiResponse = new ApiResponse();
        var result = authenticationService.authenticate(request);
        return apiResponse.<AuthenticationResponse>builder()
                .success(true)
                .payload(result)
                .build();

    }

    /**
    Check token
     **/
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        ApiResponse apiResponse = new ApiResponse();
        var result = authenticationService.introspect(request);
        return apiResponse.<IntrospectResponse>builder()
                .success(true)
                .payload(result)
                .build();
    }

    /**
    Đăng xuất
     **/
    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        ApiResponse apiResponse = new ApiResponse();
        authenticationService.logout(request);
        return apiResponse.<Void>builder()
                .success(true)
                .build();
    }


    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> logout(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        ApiResponse apiResponse = new ApiResponse();
        var result=  authenticationService.refreshToken(request);
        return apiResponse.<AuthenticationResponse>builder()
                .success(true)
                .payload(result)
                .build();
    }

    @PostMapping("/outbound/authentication")
    ApiResponse<AuthenticationResponse> outboundAuthenticate(
            @RequestParam("code") String code
    ){
        var result = authenticationService.outboundAuthenticate(code);
        return ApiResponse.<AuthenticationResponse>builder().payload(result).build();
    }

}
