package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    User selectUser(@Param(value = "username") String username, @Param(value = "pwd") String pwd);

    String selectQuestionByUsername(String username);

    int checkQuestionAnswer(@Param(value = "username") String username, @Param(value = "question") String question, @Param(value = "answer") String answer);

    int updatePasswordByUsername(@Param(value = "username") String username, @Param("passwordNew") String passwordNew);

    int selectCountByPasswordAndUserId(@Param("password") String password, @Param("userId") Integer userId);

    int selectCountByEmailAndUserId(@Param("email") String email, @Param("userId") Integer userId);
}