package com.iot.vehicle.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.vehicle.api.entity.SysUser;
import com.iot.vehicle.api.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author dongxiang.wu
 */
public interface UserService {

    /**
     * 根据ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserVO getUserByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param username 用户名（模糊查询，可选）
     * @param status   状态（可选）
     * @return 用户分页列表
     */
    Page<UserVO> getUserPage(Long current, Long size, String username, Integer status);

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param user   用户信息
     */
    void updateUser(Long userId, SysUser user);

    /**
     * 删除用户（逻辑删除）
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 启用/禁用用户
     *
     * @param userId 用户ID
     * @param status 状态（0-禁用，1-正常）
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 重置用户密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 获取指定用户信息（用于获取当前登录用户）
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getCurrentUserInfo(Long userId);
}

