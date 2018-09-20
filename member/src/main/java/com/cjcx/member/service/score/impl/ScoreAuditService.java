package com.cjcx.member.service.score.impl;

import com.cjcx.member.cache.CacheHolder;
import com.cjcx.member.dao.ScoreAuditDao;
import com.cjcx.member.dto.ScoreAuditDto;
import com.cjcx.member.dto.ScoreAuditReceiptDto;
import com.cjcx.member.dto.SystemParamDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.entity.Staff;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.object.ScanReceiptData;
import com.cjcx.member.service.score.IScoreAuditReceiptService;
import com.cjcx.member.service.score.IScoreAuditService;
import com.cjcx.member.utils.GsonUtil;
import com.cjcx.member.utils.JacksonUtil;
import com.cjcx.member.utils.RequestUtil;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 积分审核
 */
@Service
public class ScoreAuditService extends BaseService<ScoreAuditDto, Integer> implements IScoreAuditService {

    Lock lock = new ReentrantLock();


    @Autowired
    ScoreAuditDao dao;

    @Autowired
    CacheHolder cacheHolder;

    @Autowired
    IScoreAuditReceiptService scoreAuditReceiptService;


    @Override
    public Integer pull(Integer id, Staff staff) {
        try {
            lock.lock();
            ScoreAuditDto dto = dao.findById(id);
            if (dto.getState() == 0) {
                //进入待审核状态
                dto.setStaffId(staff.getId());
                dto.setState(2);
                dto.setUpdateBy(staff.getUserName());
                dao.updateById(dto);
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return -1;
    }

    @Override
    public ResultObject addScoreAudit(ScoreAuditDto dto) {
        ResultObject r = new ResultObject();
        try {
            dto.setState(0);
            dto.setCreateBy(dto.getPhone());
            dto.setCreateTime(new Date());
            dto.setUpdateTime(new Date());
            dao.insert(dto);
            logger.info("插入小票审核记录(未审核)");
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-10001");
            r.setMsg("异常 插入 小票审核记录");
            return r;
        }
        try {
            return this.autoAudit(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 自动审核
     *
     * @param dto
     * @return
     * @throws Exception
     */
    private ResultObject autoAudit(ScoreAuditDto dto) {
        ResultObject r = new ResultObject();
        ScanReceiptData sr = null;
        Integer state = 1;

        //取当前时间作为唯一单号
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String serialNo = "N" + dtf.format(LocalDateTime.now());

        //存贮小票图片分析后的结果
        ScoreAuditReceiptDto receipt = new ScoreAuditReceiptDto();
        try {

            SystemParamDto imgUrlDto = cacheHolder.getSystemParamMap().get("SCAN_RECEIPT_IMAGE_URL");
            if(imgUrlDto == null){
                r.setErrCode("1");
                r.setMsg("请配置 扫图-图片地址");
                return r;
            }

            //小票图片
            String imgUrl = imgUrlDto.getParamValue() + dto.getUrl();
            //解析地址
            SystemParamDto systemParamDto = cacheHolder.getSystemParamMap().get("SCAN_RECEIPT_ANALYSIS_URL");
            if (systemParamDto == null) {
                r.setErrCode("2");
                r.setMsg("请配置 扫图-分析地址");
                return r;
            }

            String reqUrl = systemParamDto.getParamValue(); //"http://www.yunpiaobox.cn:18080/mall/interface/uploadReceiptImage.service";
            Map map = new HashMap();
            map.put("serialNo", serialNo);
            map.put("url", imgUrl);
            String result = RequestUtil.doPostByJson(reqUrl, GsonUtil.toJson(map));
            logger.info("分析小票图片内容服务器 结果:" + result);

            JSONObject json = new JSONObject(result);
            Integer returnCode = json.getInt("returnCode");
            //自动审核-解析成功
            if (returnCode == 0) {
                sr = JacksonUtil.json2Bean(json.get("data").toString(), ScanReceiptData.class);
                logger.info("小票信息:" + sr.toString());

                dto.setState(1);
                dto.setShopsId(sr.getShopsId());
                dto.setAmount(sr.getTotalPrice());
                dto.setScore(sr.getTotalPrice());
                dto.setUpdateTime(new Date());

                if (StringUtils.isEmpty(sr.getReceiptNum())) {
                   r.setErrCode("-100");
                   r.setMsg("单号未能识别");
                   return r;
                }

                //检查单号是否已经积分过
                receipt.setReceiptNum(sr.getReceiptNum());
                receipt.setState(0);
                List<ScoreAuditReceiptDto> list = scoreAuditReceiptService.findByCondition(receipt);
                if(list.size() != 0){
                    r.setErrCode("-200");
                    r.setMsg("该单号已积分.");
                    return r;
                }

                r = this.doScoreAudit(dto);
                if (r.getErrCode().equals(ResultObject.ERRCODE_OK)) {
                    state = 0;
                    logger.info("小票自动积分成功");
                } else {
                    logger.info("小票自动积分失败.");
                }
            } else {
                state = returnCode;
                r.setErrCode(returnCode + "");
                r.setMsg(json.getString("message"));
                logger.info("小票自动积分失败..");
            }
        } catch (IOException e) {
            e.printStackTrace();
            r.setErrCode("-10002");
            r.setMsg("异常:");
        } finally {
            receipt.setAuditId(dto.getId());
            receipt.setSerialNo(serialNo);
            if (sr != null) {
                //验证小票单号
                receipt.setTotalPrice(sr.getTotalPrice());
                receipt.setTotalNum(sr.getTotalNum());
                receipt.setReceiptNum(sr.getReceiptNum());
                receipt.setOrderNum(sr.getOrderNum());
                receipt.setShopsId(sr.getShopsId());
                receipt.setShopsName(sr.getShopsName());
                receipt.setPayFlag(sr.getPayFlag());
                receipt.setCreateTime(sr.getCreateTime());
            }
            receipt.setState(state);
            receipt.setRemark(r.getMsg());
            int z = scoreAuditReceiptService.insert(receipt);
            if (z == 1) {
                logger.info("t_score_audit_receipt 插入数据成功, state:" + state);
            } else {
                logger.info("t_score_audit_receipt 插入数据失败, state:" + state);
            }
        }
        return r;
    }


    @Override
    public ResultObject doScoreAudit(ScoreAuditDto dto) {
        ResultObject r = new ResultObject();
        try {
            //审核执行过程
            logger.info("SP_TRANSACTION_AUDIT(" + dto.getId() + "," + dto.getState() + ", " + dto.getShopsId() + ", " + dto.getAmount() + ", " + dto.getScore() + ", @out)");
            SqlSession session = super.getSqlSession();
            String statesql = "com.cjcx.member.dao.ScoreAuditDao.storageScoreAudit";
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("t_audit_id", dto.getId());
            // 审核结果 1通过 3拒绝
            searchMap.put("t_state", dto.getState());
            searchMap.put("t_shops_id", dto.getShopsId());
            searchMap.put("t_amount", dto.getAmount());
            searchMap.put("t_score", dto.getScore());
            session.selectOne(statesql, searchMap);

            String result = searchMap.get("state").toString();
            logger.info("SP_TRANSACTION_AUDIT Storage out:" + result);
            r.setErrCode(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return r;
    }

    @Override
    protected IBaseDao<ScoreAuditDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<ScoreAuditDto> getScoreAuditDtoList(FilterDto dto) {
        try {
            return dao.getScoreAuditList(dto);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
