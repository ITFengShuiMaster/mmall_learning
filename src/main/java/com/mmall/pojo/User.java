package com.mmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private Integer id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Date createTime;

    private Date updateTime;

}