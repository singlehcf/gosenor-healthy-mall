package com.gosenor.service;

import com.gosenor.request.UserLoginRequest;
import com.gosenor.request.UserRegisterRequest;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.UserVO;

public interface UserService {
    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    public JsonData register(UserRegisterRequest registerRequest);

    /**
     * 用户登陆
     * @param userLoginRequest
     * @return
     */
    public JsonData login(UserLoginRequest userLoginRequest);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    public UserVO findUserById(Long id);
}
