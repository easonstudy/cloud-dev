package com.cjcx.picture.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IPictureService {

    Map uploadPicture(MultipartFile uploadFile, Integer local);

    Map uploadPicture(String netUrl, Integer local);
}
