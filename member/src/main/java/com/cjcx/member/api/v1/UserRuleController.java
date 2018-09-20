package com.cjcx.member.api.v1;

import com.cjcx.member.dto.UserRuleTagsDto;
import com.cjcx.member.entity.Staff;
import com.cjcx.member.framework.annotation.AuthToken;
import com.cjcx.member.framework.controller.AdminSupport;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserRuleTagsService;
import com.cjcx.member.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会员规则
 */
@RestController
@RequestMapping("/api/v1/user/rule")
public class UserRuleController extends AdminSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IUserService userService;

    @Autowired
    IUserRuleTagsService userRuleTagsService;

    /**
     * 标签配置 - 查询
     *
     * @return
     */
    @RequestMapping("/tags/list")
    @AuthToken
    public ResultObject tagsList() {
        ResultObject resultObject = new ResultObject();

        List<UserRuleTagsDto> list = userRuleTagsService.findAll();
        //Stream<UserRuleTagsDto> stream =
        Stream<UserRuleTagsDto> stream = list.stream()
                .map((e) -> {
                    if (e.getLevelHigh() == null) e.setLevelHigh(0);
                    if (e.getLevelMiddle() == null) e.setLevelMiddle(0);
                    if (e.getLevelLow() == null) e.setLevelLow(0);
                    return e;
                });
        //stream to list
        list = stream.collect(Collectors.toList());
        //JSONArray array = new JSONArray(list);
        //resultObject.setData(array.toString());

        resultObject.setData(list);
        return resultObject;
    }

    @RequestMapping("/tags/merge")
    @AuthToken
    public ResultObject tagsMerge(@RequestBody List<UserRuleTagsDto> list) {
        ResultObject resultObject = new ResultObject();
        //list.stream().map((e) -> e.toString()).forEach(System.out::println);
        try {
            Staff staff = super.getStaff();
            Date date = new Date();
            list.stream().forEach((e) -> {
                e.setUpdateBy(staff.getUserName());
                e.setUpdateTime(date);
                userRuleTagsService.updateById(e);
            });
        } catch (Exception e) {
            e.printStackTrace();
            resultObject.setErrCode("-10001");
            resultObject.setMsg("修改错误");
        }
        return resultObject;
    }


}
