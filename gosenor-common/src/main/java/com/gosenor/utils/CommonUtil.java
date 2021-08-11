package com.gosenor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Random;

/**
 * @program: gosenor-healthy-mall
 * @description: 公共工具类
 * @author: hcf
 * @create: 2021-07-22 11:12
 */
@Slf4j
public class CommonUtil {
    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null ||
                    ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress =
                        request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null ||
                    ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress =
                        request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null ||
                    ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress =
                        request.getRemoteAddr();
                if
                (ipAddress.equals("127.0.0.1")) {
                    // 根据⽹卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet =
                                InetAddress.getLocalHost();
                    } catch
                    (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress =
                            inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第⼀个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null &&
                    ipAddress.length() > 15) {
                // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0)
                {
                    ipAddress =
                            ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        return ipAddress;
    }
    public static String MD5(String data) {
        try {
            java.security.MessageDigest md =
                    MessageDigest.getInstance("MD5");
            byte[] array =
                    md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new
                    StringBuilder();
            for (byte item : array) {

                sb.append(Integer.toHexString((item & 0xFF) |
                        0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;
    }

    /**
     * 获取验证码随机数
     * @param length
     * @return
     */
    public static String getRandomCode(int length){

        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int j=0; j<length; j++){
            sb.append(sources.charAt(random.nextInt(9)));
        }
        return sb.toString();
    }

    public static Long getCurrentTimestamp(){
        return System.currentTimeMillis();
    }

    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 获取随机长度的串
     * @param length
     * @return
     */
    public static String getStringNumRandom(int length){
        //生成随机数字和字母,
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        return saltString.toString();
    }

    public static void sendJsonMessage(HttpServletResponse response,Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter printWriter = response.getWriter()){
            printWriter.print(objectMapper.writeValueAsString(object));
            response.flushBuffer();
        }catch (IOException e){
            log.warn("响应json数据给前端异常:{}",e);
        }
    }
}
