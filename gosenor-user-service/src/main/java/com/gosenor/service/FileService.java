package com.gosenor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * 上传附件
     * @return
     */
    public String uploadFile(MultipartFile file) ;
}
