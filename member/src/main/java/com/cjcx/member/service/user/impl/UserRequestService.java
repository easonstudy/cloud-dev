package com.cjcx.member.service.user.impl;

import com.cjcx.member.cache.CacheHolder;
import com.cjcx.member.dao.UserRequestDao;
import com.cjcx.member.dto.SystemParamDto;
import com.cjcx.member.dto.UserRequestDto;
import com.cjcx.member.entity.SystemParam;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.security.AESCrypt;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.utils.MailUtil;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.user.IUserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserRequestService extends BaseService<UserRequestDto, Integer> implements IUserRequestService {

    @Autowired
    UserRequestDao userRequestDao;

    @Autowired
    CacheHolder cacheHolder;

    @Override
    protected IBaseDao<UserRequestDto, Integer> getDao() {
        return userRequestDao;
    }

    @Override
    public Map<String, Object> doRequestTest(UserRequestDto userRequestDto) {
        Map<String, Object> outmap = new HashMap<>();
        try {
            String userKey = AESCrypt.decrypt(userRequestDto.getUserKey());
            String[] carr = userKey.split(":");

            String openId = carr[0];
            Long keyCreateTime = Long.valueOf(carr[1]);
            logger.info("申请测试 openId:" + openId + ", 创建时间:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(keyCreateTime)));
            Long maxAvalidTime = keyCreateTime + 30 * 60 * 1000L;
            if (maxAvalidTime < System.currentTimeMillis()) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                outmap.put(ResultObject.PARAM_MSG, "链接已失效");
                return outmap;
            }

            /*if (StringUtils.isEmpty(userRequestDto.getEmail())) {
                outmap.put(ResultObject.PARAM_ERRCODE, -2);
                outmap.put(ResultObject.PARAM_MSG, "请填写邮箱地址");
                return outmap;
            }*/

            //检查 申请次数
            UserRequestDto c = new UserRequestDto();
            c.setOpenId(openId);
            c.setCreateTime(new Date());
            List<UserRequestDto> list = userRequestDao.findByUserRequest(c);
            if(list.size() >= 5){
                outmap.put(ResultObject.PARAM_ERRCODE, -3);
                outmap.put(ResultObject.PARAM_MSG, "申请已提交, 请勿重复多次申请");
                return outmap;
            }

            //插入到DB记录
            userRequestDto.setOpenId(openId);
            userRequestDto.setCreateTime(new Date());
            userRequestDao.insert(userRequestDto);

            //邮件处理
            String toEmail = null;
            try {
                ConcurrentMap<String, SystemParamDto> sysmap = cacheHolder.getSystemParamMap();
                toEmail = sysmap.get(SystemParam.KEY_REQUEST_EMAIL).getParamValue();
            } catch (Exception e) {
                //e.printStackTrace();
                logger.info("请配置收件邮箱地址, 系统参数REQUEST_EMAIL ");
            }
            if (toEmail != null) {
                String titile = "公众号-申请测试";
                String content = "昵称:" + userRequestDto.getNickName() + ", 电话:" + userRequestDto.getPhone() == null ? "" : userRequestDto.getPhone();
                new MailUtil().send(userRequestDto.getEmail(), titile, content);
            }

            outmap.put(ResultObject.PARAM_ERRCODE, 0);
            outmap.put(ResultObject.PARAM_MSG, "申请提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, -10001);
            outmap.put(ResultObject.PARAM_MSG, "系统异常");
        }
        return outmap;
    }
}
