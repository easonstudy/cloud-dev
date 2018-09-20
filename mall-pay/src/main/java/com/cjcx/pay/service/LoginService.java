package com.cjcx.pay.service;

import com.cjcx.pay.dao.PermissionsDao;
import com.cjcx.pay.dao.StaffDao;
import com.cjcx.pay.dto.PermissionsDto;
import com.cjcx.pay.dto.StaffDto;
import com.cjcx.pay.framework.service.AdminService;
import com.cjcx.pay.utils.GsonUtil;
import com.cjcx.pay.utils.IpUtil;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService extends AdminService implements ILoginService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StaffDao staffDao;

    @Autowired
    private PermissionsDao permissionsDao;

    public String login(String username, String password) {
        Map<String, Object> outmap = new HashMap<>();
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            String ip = IpUtil.getIp(request);

            logger.info("SP_LOGIN(" + username + ", " + password + ", " + ip + " null)");
            SqlSession session = super.getSqlSession();
            String statesql = "com.cjcx.pay.dao.StaffDao.login";
            Map<String, Object> searchMap = new HashMap<String, Object>();
            searchMap.put("username", username);
            searchMap.put("pwd", password);
            searchMap.put("ip", ip);
            session.selectOne(statesql, searchMap);

            String token = (String) searchMap.get("token_out");
            if (token.length() == 32) {
                outmap.put("errorCode", "0");
                outmap.put("token", token);

                //有效期30分钟
                //JedisUtils.setex("login:staff:token:"+token,  token, 1 * 60 * 30);

                StaffDto staff = staffDao.findByToken(token);
                Map map = new HashMap();
                map.put("id", staff.getId());
                map.put("nickName", staff.getNickName());
                map.put("email", staff.getEmail());
                map.put("userName", staff.getUserName());
                outmap.put("user", map);

                List<PermissionsDto> list =   permissionsDao.getPermissionsByRoleId(staff.getRoleId());
                outmap.put("permissions", list);

                logger.info("login in:" + token);
            } else {
                outmap.put("errorCode", token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return GsonUtil.toJson(outmap);
    }

    @Override
    public String logout() {
        String token = getHttpServletRequeset().getHeader("Authorization");
        logger.info("logout:" + token);
        //JedisUtils.del("login:token:"+token);
        return null;
    }

    @Override
    public StaffDto getUserByToken(String token) {
        StaffDto staff = null;
        try {
            staff = staffDao.findByToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }
}
