package com.gosenor.controller;

import com.gosenor.enums.BizCodeEnum;
import com.gosenor.interceptor.LoginInterceptor;
import com.gosenor.model.LoginUser;
import com.gosenor.request.UserLoginRequest;
import com.gosenor.request.UserRegisterRequest;
import com.gosenor.service.FileService;
import com.gosenor.service.UserService;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-23 14:29
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @ApiOperation("用户头像上传")
    @PostMapping(value = "/upload")
    public JsonData<String> uploadUserImg(
            @ApiParam(value = "文件上传",required = true)
            @RequestPart("file") MultipartFile file){

        String result = fileService.uploadFile(file);
        return result!=null? JsonData.buildSuccess(result):JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }


    /**
     *  用户注册
     * @param registerRequest
     * @return
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public JsonData register(@RequestBody UserRegisterRequest registerRequest){
        return userService.register(registerRequest);
    }

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public JsonData login(@RequestBody UserLoginRequest userLoginRequest){
        return userService.login(userLoginRequest);
    }

    /**
     * 获取当前登陆用户
     * @return
     */
    @ApiOperation("获取当前登陆用户信息")
    @GetMapping("/detail")
    public JsonData<UserVO> LoginUser(){
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        UserVO userVo = userService.findUserById(loginUser.getId());
        return JsonData.buildSuccess(userVo);
    }
}
