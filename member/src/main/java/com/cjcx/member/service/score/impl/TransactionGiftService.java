package com.cjcx.member.service.score.impl;

import com.cjcx.member.dao.TransactionGiftDao;
import com.cjcx.member.dto.GiftDto;
import com.cjcx.member.dto.TransactionGiftDto;
import com.cjcx.member.dto.filter.FilterDto;
import com.cjcx.member.framework.orm.IBaseDao;
import com.cjcx.member.framework.service.BaseService;
import com.cjcx.member.framework.web.ResultObject;
import com.cjcx.member.service.score.IGiftService;
import com.cjcx.member.service.score.ITransactionGiftService;
import com.cjcx.member.service.user.IUserService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionGiftService extends BaseService<TransactionGiftDto, Integer> implements ITransactionGiftService {


    Lock lock = new ReentrantLock();

    @Autowired
    TransactionGiftDao dao;

    @Autowired
    IUserService userService;

    @Autowired
    IGiftService giftService;

    @Override
    protected IBaseDao<TransactionGiftDto, Integer> getDao() {
        return dao;
    }


    /**
     * 积分兑换礼品
     *
     * @param dto
     * @return
     */
    @Override
    public ResultObject doTransactionGift(GiftDto dto) {
        ResultObject r = new ResultObject();
        try {
            //会员
            //serDto user = userService.getUserByLoginName(dto.getUserId());

            lock.lock();
            //礼品更新
            //GiftDto gift = giftService.findById(dto.getId());
            //gift.setNumber(gift.getNumber() - 1);
            //giftService.updateById(gift);

            //兑换记录插入
            logger.info("SP_TRANSACTION_GIFT('" + dto.getUserUniqueId() + "', " + dto.getId() + ", " + dto.getNumber() + ", @out)");
            SqlSession session = super.getSqlSession();
            String statesql = "com.cjcx.member.dao.TransactionGiftDao.storageGiftExchange";
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("user_unique_id", dto.getUserUniqueId());
            searchMap.put("gift_id", dto.getId());
            searchMap.put("gift_number", dto.getNumber());
            session.selectOne(statesql, searchMap);

            String result = searchMap.get("state").toString();
            logger.info("SP_TRANSACTION_GIFT Storage out:" + result);
            r.setErrCode(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return r;
    }

    @Override
    public List<TransactionGiftDto> getTransactionGiftList(FilterDto dto) {
        List<TransactionGiftDto> list = new ArrayList<TransactionGiftDto>();
        try {
            list = dao.getTransactionGiftList(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
