package com.gosenor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.enums.SendCodeEnum;
import com.gosenor.exception.BizException;
import com.gosenor.feign.CouponFeignService;
import com.gosenor.mapper.UserMapper;
import com.gosenor.model.LoginUser;
import com.gosenor.model.UserDO;
import com.gosenor.request.NewUserCouponRequest;
import com.gosenor.request.UserLoginRequest;
import com.gosenor.request.UserRegisterRequest;
import com.gosenor.service.NotifyService;
import com.gosenor.service.UserService;
import com.gosenor.utils.CommonUtil;
import com.gosenor.utils.JWTUtil;
import com.gosenor.utils.JsonData;
import com.gosenor.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-26 16:16
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponFeignService couponFeignService;

    @Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
    public JsonData register(UserRegisterRequest registerRequest) {
        //验证验证码
        boolean checkCode = false;
        String account = registerRequest.getMail();
        if (StringUtils.isNotBlank(account)){
            if (notifyService.checkCode(SendCodeEnum.USER_REGISTER,account,registerRequest.getCode())){
                checkCode = true;
            }
        }
        if (!checkCode){
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }

        //设置密码
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(registerRequest, userDO);
        userDO.setCreateTime(new Date());
        userDO.setSecret("$1$"+ CommonUtil.getStringNumRandom(8));
        String pwd = Md5Crypt.md5Crypt(userDO.getPwd().getBytes(),userDO.getSecret());
        userDO.setPwd(pwd);
        //判断账户是否重复
        if (this.checkUnique(account)){
            int rows = userMapper.insert(userDO);
            log.info("rows:{},注册成功:{}", rows, userDO.toString());

            //新用户注册成功，初始化信息，发放福利等 TODO
            userRegisterInitTask(userDO);
            //int b = 1/0;
            return JsonData.buildSuccess();
        }else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {
        if (StringUtils.isBlank(userLoginRequest.getAccount())){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        //获取用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mail",userLoginRequest.getAccount());
        List<UserDO> userDOS = userMapper.selectList(queryWrapper);
        if (userDOS == null || userDOS.size() == 0){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        UserDO userDO = userDOS.get(0);
        //加密密码
        String psw = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(),userDO.getSecret());
        //和用户密码比对
        if (!psw.equals(userDO.getPwd())){
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        }
        //颁发令牌
        LoginUser loginUser = LoginUser.builder().build();
        BeanUtils.copyProperties(userDO,loginUser);
        String token = JWTUtil.geneJsonWebToken(loginUser);
        return JsonData.buildSuccess(token);
    }

    @Override
    public UserVO findUserById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        if (userDO == null){
            return null;
        }
        UserVO userVo = new UserVO();
        BeanUtils.copyProperties(userDO,userVo);
        return userVo;
    }

    /**
     * 验证账户唯一性
     * @param account
     * @return
     */
    private Boolean checkUnique(String account){
        boolean result = true;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("mail",account);
        List<UserDO> list = userMapper.selectList(queryWrapper);
        if (list != null && list.size() > 0){
            result = false;
        }
        return result;
    }

    /**
     * 用户注册，初始化福利信息 TODO
     *
     * @param userDO
     */
    private void userRegisterInitTask(UserDO userDO) {
        NewUserCouponRequest newUserCouponRequest = new NewUserCouponRequest();
        newUserCouponRequest.setUserId(userDO.getId());
        newUserCouponRequest.setName(userDO.getName());
        JsonData jsonData = couponFeignService.addNewUserCoupon(newUserCouponRequest);
        log.info("发放新用户注册优惠券：{},结果:{}",newUserCouponRequest.toString(),jsonData.toString());
    }

}
