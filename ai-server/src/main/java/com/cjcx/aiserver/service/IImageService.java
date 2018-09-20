package com.cjcx.aiserver.service;

import com.cjcx.aiserver.ai.config.ImageConfig;
import com.cjcx.aiserver.obj.ResultObject;

public interface IImageService {

    ResultObject imageHandler(ImageConfig config);

}
