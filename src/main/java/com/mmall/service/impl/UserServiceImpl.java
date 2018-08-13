package com.mmall.service.impl;

import com.mmall.common.Constants;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.JedisPoolUtil;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String pwd) {
        if (userMapper.checkUsername(username) == 0) {
            return ServerResponse.createByErrorMessage("不存在该用户");
        }

        User user = userMapper.selectUser(username, MD5Util.MD5EncodeUtf8(pwd));
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> response = this.checkValid(user.getUsername(), Constants.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }

        response = this.checkValid(user.getEmail(), Constants.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }

        //用户身份添加
        user.setRole(Constants.ROLE.ROLE_CUSTOMER);
        //密码md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        if (userMapper.insert(user) == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isBlank(str)) {
            return ServerResponse.createByErrorMessage("参数不能为空");
        }

        if (StringUtils.isNotBlank(type)) {
            if (Constants.EMAIL.equals(type)) {
                if (userMapper.checkEmail(str) > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }

            if (Constants.USERNAME.equals(type)) {
                if (userMapper.checkUsername(str) > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("校验失败");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> getForgetQuestion(String username) {
        if (this.checkValid(username, Constants.USERNAME).isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("问题为空");
    }

    @Override
    public ServerResponse<String> checkQuestionAnswer(String username, String question, String answer) {
        if (userMapper.checkQuestionAnswer(username, question, answer) > 0) {
            String forgetToken = UUID.randomUUID().toString();
            JedisPoolUtil.setEx(Constants.TOKEN_PREFIX + username, forgetToken, 60 * 60 * 12);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案不正确");
    }

    @Override
    public ServerResponse<String> forgetReSetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("token不能为空");
        }

        if (this.checkValid(username, Constants.USERNAME).isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = JedisPoolUtil.get(Constants.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if (StringUtils.equals(token, forgetToken)) {
            if (userMapper.updatePasswordByUsername(username, MD5Util.MD5EncodeUtf8(passwordNew)) > 0) {
                return ServerResponse.createBySuccessMessage("密码更新成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token不正确");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        if (userMapper.selectCountByPasswordAndUserId(passwordOld, user.getId()) == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            return ServerResponse.createBySuccessMessage("更新密码成功");
        }

        return ServerResponse.createByErrorMessage("更新密码失败");
    }

    @Override
    public ServerResponse<User> updateUser(User user) {
        //查询邮箱是否存在
        if (userMapper.selectCountByEmailAndUserId(user.getEmail(), user.getId()) > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在，请更换重试");
        }

        if (userMapper.updateByPrimaryKeySelective(user) > 0) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User reUser = userMapper.selectByPrimaryKey(userId);
        if (reUser == null) {
            return ServerResponse.createByErrorMessage("找不到该用户");
        }

        reUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(reUser);
    }

    @Override
    public ServerResponse<String> checkAdminValid(User user) {
        if (user != null && user.getRole().intValue() == Constants.ROLE.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("不是管理员，无权限操作");
    }
}
