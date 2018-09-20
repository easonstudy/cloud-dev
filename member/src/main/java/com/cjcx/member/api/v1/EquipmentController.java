package com.cjcx.member.api.v1;

import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.equipment.IEquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1")
public class EquipmentController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IEquipmentService equipmentService;

    @AuthToken
    @RequestMapping(value = "/equipment/{equipmentId}", method = RequestMethod.GET)
    public ResultObject port(@PathParam("equipmentId") String equipmentId) {
        ResultObject result = new ResultObject();


        return result;
    }


}
