package com.iot.vehicle.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.dto.ChangePasswordDTO;
import com.iot.vehicle.api.dto.UpdateUserDTO;
import com.iot.vehicle.api.entity.SysUser;
import com.iot.vehicle.api.vo.UserVO;
import com.iot.vehicle.common.core.annotation.RequirePermission;
import com.iot.vehicle.common.core.result.Result;

import com.iot.vehicle.common.core.result.ResultCode;
import com.iot.vehicle.common.mybatis.result.PageResult;
import com.iot.vehicle.service.service.UserService;
import com.iot.vehicle.common.web.util.UserContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理接口
 *
 * @author dongxiang.wu
 */
@Tag(name = "用户管理", description = "用户信息的增删改查接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser() {
        Long userId = UserContextUtil.getCurrentUserId();
        UserVO userVO = userService.getCurrentUserInfo(userId);
        return Result.success(userVO);
    }

    @Operation(summary = "根据ID查询用户", description = "查询指定用户的详细信息")
    @GetMapping("/{userId}")
    @RequirePermission("system:user:view")
    public Result<UserVO> getUserById(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    @Operation(summary = "分页查询用户列表", description = "分页查询用户列表，支持按用户名和状态筛选")
    @GetMapping("/page")
    @RequirePermission("system:user:list")
    public Result<PageResult<UserVO>> getUserPage(
            @Parameter(description = "页码") @RequestParam(value = "current", defaultValue = "1") Long current,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") Long size,
            @Parameter(description = "用户名") @RequestParam(value = "username", required = false) String username,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) Integer status) {
        Page<UserVO> page = userService.getUserPage(current, size, username, status);
        PageResult<UserVO> pageResult = PageResult.of(page);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新用户信息", description = "更新用户的基本信息")
    @PutMapping("/{userId}")
    @RequirePermission("system:user:edit")
    public Result<String> updateUser(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        SysUser user = new SysUser();
        user.setNickname(updateUserDTO.getNickname());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone());
        user.setAvatar(updateUserDTO.getAvatar());
        
        userService.updateUser(userId, user);
        return Result.success("更新成功");
    }

    @Operation(summary = "删除用户", description = "逻辑删除指定用户")
    @DeleteMapping("/{userId}")
    @RequirePermission("system:user:delete")
    public Result<String> deleteUser(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return Result.success("删除成功");
    }

    @Operation(summary = "启用/禁用用户", description = "修改用户状态")
    @PutMapping("/{userId}/status")
    @RequirePermission("system:user:edit")
    public Result<String> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId,
            @Parameter(description = "状态：0-禁用，1-正常") @RequestParam(value = "status") Integer status) {
        userService.updateUserStatus(userId, status);
        return Result.success("更新状态成功");
    }

    @Operation(summary = "重置密码", description = "管理员重置用户密码为默认密码")
    @PostMapping("/{userId}/reset-password")
    @RequirePermission("system:user:reset")
    public Result<String> resetPassword(
            @Parameter(description = "用户ID") @PathVariable("userId") Long userId) {
        // 重置为默认密码
        userService.resetPassword(userId, "123456");
        return Result.success("密码已重置为：123456");
    }

    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    @PostMapping("/change-password")
    public Result<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        Long userId = UserContextUtil.getCurrentUserId();
        if (userId == null) {
            return Result.fail(ResultCode.UNAUTHORIZED);
        }
        
        userService.changePassword(userId, changePasswordDTO.getOldPassword(), 
                changePasswordDTO.getNewPassword());
        return Result.success("密码修改成功");
    }
}

