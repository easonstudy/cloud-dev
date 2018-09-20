package com.cjcx.aiserver.service;

import com.cjcx.aiserver.ai.config.FaceConfig;
import com.cjcx.aiserver.obj.ResultObject;

public interface IFaceService {

    ResultObject faceHandler(FaceConfig config);
}
