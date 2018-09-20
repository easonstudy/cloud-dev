package com.cjcx.mall.controller;

import com.cjcx.mall.dto.EquipmentDto;
import com.cjcx.mall.service.IEquipmentService;
import com.cjcx.mall.web.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/server/equipment")
@RestController
public class EqupmentController {

    @Autowired
    IEquipmentService equipmentService;

    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public ResultObject insert(@RequestBody EquipmentDto equipmentDto) {
        return equipmentService.insert(equipmentDto);
    }

    @RequestMapping(value = "/address", method = RequestMethod.DELETE)
    public ResultObject delete(@RequestBody EquipmentDto equipmentDto) {
        return equipmentService.delete(equipmentDto);
    }

    @RequestMapping(value = "/address", method = RequestMethod.PUT)
    public ResultObject update(@RequestBody EquipmentDto equipmentDto) {
        return equipmentService.update(equipmentDto);
    }


    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public ResultObject select(@RequestParam("equipmentId") String equipmentId) {
        return equipmentService.select(equipmentId);
    }


}
