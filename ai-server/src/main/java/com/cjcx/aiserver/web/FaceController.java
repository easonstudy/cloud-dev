package com.cjcx.aiserver.web;

import com.cjcx.aiserver.ai.config.FaceConfig;
import com.cjcx.aiserver.obj.ResultObject;
import com.cjcx.aiserver.service.IFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/ocr/face")
@RestController
public class FaceController {

    @Autowired
    IFaceService faceService;


    /**
     * 基础的内容
     *
     * @param config 图片的配置
     * @return
     */
    @RequestMapping("/general")
    public ResultObject faceGeneral(FaceConfig config
    ) {
        ResultObject r = new ResultObject();
        r = faceService.faceHandler(config);
        return r;
    }


}
