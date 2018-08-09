package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {

    ServerResponse<User> login(String username, String pwd);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> getForgetQuestion(String username);

    ServerResponse<String> checkQuestionAnswer(String username, String question, String answer);

    ServerResponse<String> forgetReSetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateUser(User user);

    ServerResponse<User> getInformation(Integer userId);

    ServerResponse<String> checkAdminValid(User user);
}
