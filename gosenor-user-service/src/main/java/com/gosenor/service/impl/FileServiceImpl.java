package com.gosenor.service.impl;

import com.google.gson.Gson;
import com.gosenor.enums.BizCodeEnum;
import com.gosenor.exception.BizException;
import com.gosenor.service.FileService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @program: gosenor-healthy-mall
 * @description:
 * @author: hcf
 * @create: 2021-07-23 14:19
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(MultipartFile file) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "r6fMsrKzHtZG3VWMoA7OeoTxAZVhBsAwnNCEPK0D";
        String secretKey = "1osg8B7k1KZTnsSvqBgbAjhVvEIJNq851v0sulDx";
        String bucket = "healthy-mall";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        String returnPath = "";
        try {
            Response response = uploadManager.put(file.getInputStream(), key, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
            returnPath = "http://img.qn.hcflovelss.top" + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BizException(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
        }
        return returnPath;
    }
}
