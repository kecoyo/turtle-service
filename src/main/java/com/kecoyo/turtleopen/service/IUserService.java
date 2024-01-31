package com.kecoyo.turtleopen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kecoyo.turtleopen.domain.dto.UserLoginDto;
import com.kecoyo.turtleopen.domain.entity.User;

public interface IUserService extends IService<User> {

    UserLoginDto getLoginData(String username);

    User loadUserByUsername(String username);

    User createUser(User user);

    void updateUser(User user);

    void deleteUser(Integer id);

    void changePassword(String username, String oldPassword, String newPassword);

}
