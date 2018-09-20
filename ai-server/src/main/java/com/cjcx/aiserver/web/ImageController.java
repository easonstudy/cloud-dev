package com.cjcx.aiserver.web;

import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.obj.ResultObject;
import com.cjcx.aiserver.service.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图像 处理
 */
@RequestMapping("/v1/ocr/image")
@RestController
public class ImageController {

    @Autowired
    IImageService imageService;

    /**
     * 基础的内容
     *
     * @param config 图片的配置
     * @return
     */
    @RequestMapping("/general")
    public ResultObject imageGeneral(ImageConfig config
    ) {
        ResultObject r = new ResultObject();
        //通用版
        config.setLevel(0);
        r = imageService.imageHandler(config);
        return r;
    }

    /**
     * 基础的内容
     *
     * @param config 图片的配置
     * @return
     */
    @RequestMapping("/accurate_basic")
    public ResultObject imageAccurateBasic(ImageConfig config
    ) {
        ResultObject r = new ResultObject();
        //高精度 无位置信息
        config.setLevel(1);
        r = imageService.imageHandler(config);
        return r;
    }

    /**
     * 基础的内容
     *
     * @param config 图片的配置
     * @return
     */
    @RequestMapping("/accurate")
    public ResultObject imageAccurate(ImageConfig config
    ) {
        ResultObject r = new ResultObject();
        //高精度 有位置信息
        config.setLevel(2);
        r = imageService.imageHandler(config);
        return r;
    }
}
