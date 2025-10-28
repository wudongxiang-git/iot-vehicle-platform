package com.iot.vehicle.web.controller;

import com.iot.vehicle.api.dto.LoginDTO;
import com.iot.vehicle.api.dto.RegisterDTO;
import com.iot.vehicle.api.vo.LoginVO;
import com.iot.vehicle.common.core.result.Result;
import com.iot.vehicle.service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证接口
 *
 * @author dongxiang.wu
 */
@Tag(name = "认证管理", description = "用户注册、登录、登出等接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册", description = "注册新用户账号")
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return Result.success("注册成功");
    }

    @Operation(summary = "用户登录", description = "用户登录获取Token")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success(loginVO);
    }

    @Operation(summary = "用户登出", description = "用户登出并失效Token")
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            authService.logout(token);
        }
        return Result.success("登出成功");
    }

    @Operation(summary = "刷新Token", description = "使用旧Token换取新Token")
    @PostMapping("/refresh")
    public Result<String> refreshToken(@RequestHeader(value = "Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String newToken = authService.refreshToken(token);
        return Result.success("Token刷新成功", newToken);
    }
}

