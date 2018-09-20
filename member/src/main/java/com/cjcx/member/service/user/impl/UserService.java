package com.cjcx.member.service.user.impl;


import com.cjcx.member.dao.UserDao;
import com.cjcx.member.dto.TransactionDto;
import com.cjcx.member.dto.UserBalanceDto;
import com.cjcx.member.dto.UserDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.security.AESCrypt;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.WxEventMessage;
import com.cjcx.member.service.user.ITransactionService;
import com.cjcx.member.service.user.IUserBalanceService;
import com.cjcx.member.service.user.IUserService;
import com.cjcx.member.utils.GsonUtil;
import com.cjcx.member.utils.Md5Util;
import com.cjcx.member.utils.Sms1Util;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserService extends BaseService<UserDto, Integer> implements IUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Autowired
    private IUserBalanceService userBalanceService;

    @Autowired
    private ITransactionService transactionService;

    @Override
    protected IBaseDao<UserDto, Integer> getDao() {
        return userDao;
    }


    @Override
    public ResultObject login(Map<String, Object> map) {
        ResultObject r = new ResultObject();
        try {
            Integer method = Integer.parseInt(map.get("m").toString());
            //手机登入
            if (method == 1) {
                logger.info("手机登入");
                String phone = map.get("phone").toString();
                String code = map.get("code").toString();
                logger.info("电话:{}, 验证码:{}", phone, code);
                UserDto user = userDao.findByLoginName(phone);
                logger.info("DB 验证码:{}, 验证码一致:{}", code, user.getCode().equals(code));
                if (!user.getCode().equals(code)) {
                    r.setErrCode("-2");
                    r.setMsg("验证码错误");
                    return r;
                }
                Map umap = new HashMap();
                umap.put("id", user.getId());
                umap.put("email", user.getEmail());
                umap.put("phone", user.getPhone());

                r.put("user", umap);
                String md5Code = phone + code;
                r.put("token", Md5Util.getHexMD5Str(md5Code));
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
            r.setMsg("异常:" + e.getMessage());
        }
        return r;
    }

    @Override
    @Deprecated
    public UserDto searchUserById(String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return null;
            }

            UserDto condition = new UserDto();

            UserDto userDto = userDao.findByLoginName(id);
            if (userDto != null)
                return userDto;

            condition.setPhone(id);
            List<UserDto> list = userDao.findByCondition(condition);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserDto getUserByLoginName(String loginName) {
        try {
            if (StringUtils.isEmpty(loginName)) {
                return null;
            }
            UserDto userDto = userDao.findByLoginName(loginName);
            return userDto;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("登录名:" + loginName + " 登入失败");
            logger.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<UserDto> findUserListByCondition(UserDto userDto) {
        return userDao.findUserListByCondition(userDto);
    }

    /**
     * 完善资料
     *
     * @param userDto
     * @return
     */
    public Map<String, Object> perfect(UserDto userDto) {
        Map<String, Object> outmap = new HashMap();
        try {
            String content = AESCrypt.decrypt(userDto.getPerfectKey());
            String[] carr = content.split(":");

            String openId = carr[0];
            Long keyCreateTime = Long.valueOf(carr[1]);
            logger.info("完善资料 openId:" + openId + ", 创建时间:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(keyCreateTime)));

            Long maxAvalidTime = keyCreateTime + 30 * 60 * 1000L;
            if (maxAvalidTime < System.currentTimeMillis()) {
                outmap.put(ResultObject.PARAM_ERRCODE, -1);
                outmap.put(ResultObject.PARAM_MSG, "链接已失效");
                return outmap;
            }

            //UserService userService = (UserService)AopContext.currentProxy();
            UserDto user = this.getUserByLoginName(openId);
            user.setNickName(userDto.getNickName());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            user.setUpdateTime(new Date());

            updateById(user);
            outmap.put(ResultObject.PARAM_ERRCODE, 0);
        } catch (Exception e) {
            e.printStackTrace();
            outmap.put(ResultObject.PARAM_ERRCODE, -10001);
        }
        return outmap;
    }


    @Override
    public ResultObject register(WxEventMessage eventMessage) {
        ResultObject r = new ResultObject();
        try {
            logger.info("注册消息:" + GsonUtil.toJson(eventMessage));

            UserDto user = new UserDto();
            user.setOpenId(eventMessage.getFromUserName());
            List<UserDto> list = findByCondition(user);

            if (list.size() > 0) {
                user = list.get(0);
                user.setIsDelete(false);
                updateById(user);
            } else {
                user.setWechatId(eventMessage.getToUserName());
                user.setOpenId(eventMessage.getFromUserName());
                user.setIsDelete(false);
                //创建时间(整形)
                Long createTime = eventMessage.getCreateTime() * 1000L;
                //logger.info("关注 系统时间:" + System.currentTimeMillis());
                //logger.info("关注 创建时间Long:" + createTime);
                user.setCreateTime(new Date(createTime));
                insert(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
        }
        return r;
    }

    @Override
    public ResultObject unbind(String username) {
        ResultObject r = new ResultObject();
        try {
            UserDto user = new UserDto();
            user.setOpenId(username);
            List<UserDto> list = findByCondition(user);

            if (list.size() > 0) {
                user = list.get(0);
                user.setIsDelete(true);
                updateById(user);
            }
            logger.info("解绑 OpenId:" + user.getOpenId());
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
        }
        return r;
    }

    @Override
    public ResultObject getUserInfo(String username) {
        ResultObject r = new ResultObject();
        try {
            UserDto userDto = getUserByLoginName(username);
            if (userDto == null) {
                r.setErrCode("4001");
                r.put("user", null);
                r.put("balance", null);
                r.setMsg("未找到用户");
                return r;
            }
            r.setData(userDto);
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
        }
        return r;
    }

    @Override
    public ResultObject sendSmsCode(String phone, Integer type) {
        ResultObject r = new ResultObject();
        try {
            //发送短信
            boolean isOk = false;

            /*String json = Sms1Util.sendSms("【】您的验证码是", phone);
            JSONObject jsonObject = new JSONObject(json);*/


            String code = ((int)(Math.random() * 1000000))+"";
            isOk = true;

            //发送成功
            if (isOk) {
                Calendar c = Calendar.getInstance();
                UserDto userDto = userDao.findByLoginName(phone);
                if (userDto == null) {
                    userDto = new UserDto();
                    userDto.setCreateBy(phone);
                    userDto.setCreateTime(c.getTime());
                    userDto.setPhone(phone);

                    c.add(Calendar.MINUTE, 30);
                    userDto.setIsDelete(Boolean.FALSE);
                    userDto.setCode(code);
                    userDto.setCodeExpireTime(c.getTime());
                    userDao.insert(userDto);
                } else {
                    c.add(Calendar.MINUTE, 30);
                    userDto.setIsDelete(Boolean.FALSE);
                    userDto.setCode(code);
                    userDto.setCodeExpireTime(c.getTime());
                    userDao.updateById(userDto);
                }
                r.put("code", code);
            } else {
                r.setErrCode("-2");
                r.setMsg("手机号不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
            r.setMsg("系统异常");
        }
        return r;
    }

    @Override
    public ResultObject wxAppScoreSearch(Map<String, Object> map) {
        ResultObject r = new ResultObject();
        try {
            Integer id = Integer.parseInt(map.get("id").toString());

            //剩余积分
            UserBalanceDto userBalanceDto = userBalanceService.findById(id);
            Double score = userBalanceDto == null ? 0.0 : userBalanceDto.getScore();
            r.put("score", score);

            //积分列表 最近10次记录
            PageHelper.startPage(1, 10);
            PageHelper.orderBy("create_time desc");

            TransactionDto conditon = new TransactionDto();
            conditon.setUserId(id);
            List<TransactionDto> list = transactionService.findByCondition(conditon);
            r.put("list", list);

        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-1");
        }
        return r;
    }
}

