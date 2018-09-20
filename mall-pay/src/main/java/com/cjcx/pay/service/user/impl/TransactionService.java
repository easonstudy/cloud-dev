package com.cjcx.pay.service.user.impl;


import com.cjcx.pay.dao.TransactionDao;
import com.cjcx.pay.dto.TransactionDto;
import com.cjcx.pay.dto.filter.FilterDto;
import com.cjcx.pay.dto.filter.PayDto;
import com.cjcx.pay.framework.orm.IBaseDao;
import com.cjcx.pay.framework.service.BaseService;
import com.cjcx.pay.framework.web.ResultObject;
import com.cjcx.pay.service.user.IPayService;
import com.cjcx.pay.service.user.ITransactionService;
import com.cjcx.pay.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
@Service
public class TransactionService extends BaseService<TransactionDto, Integer> implements ITransactionService {

    @Autowired
    TransactionDao dao;

    @Autowired
    IPayService payService;

    @Override
    protected IBaseDao<TransactionDto, Integer> getDao() {
        return dao;
    }

    @Override
    public List<TransactionDto> getTransactionList(FilterDto filterDto) {
        return dao.getList(filterDto);
    }

    @Override
    @Transactional
    public ResultObject storedTransaction(PayDto pay) {
        ResultObject r = new ResultObject();
        try {
            log.info("请求参数{}", pay.toString());
            //log.info("设备Id:{},条码:{}, 日期:{}", pay.getDeviceId(), pay.getBarcode(), pay.getDate());
            TransactionDto dto = new TransactionDto();
            dto.setBarcode(pay.getBarcode().trim());
            dto.setDeviceId(pay.getDeviceId().trim());

            log.info("查询DB参数:{}", dto.toString());
            List<TransactionDto> list = dao.getVerfiyList(dto);
            log.info("查询DB结果, 条数:{}", list.size());
            if (list.size() == 1) {
                dto = list.get(0);
                log.info(dto.toString());
                Date createTime = new Date();
                dto.setAccountDay(createTime);
                dto.setGrasptingTime(DateUtil.strToDate(pay.getDate(), "yyyyMMddHHmmss"));
                dto.setCreateTime(createTime);
                dto.setCreateBy(pay.getDeviceId());
                //存贮记录到DB
                insert(dto);

                /*ThreadPoolTaskExecutor executor = SpringContextHolder.getBean("taskAsync");
                //提交到支付接口
                FutureTask<Map<String, Object>> future =
                    new FutureTask<Map<String, Object>>(new Callable<Map<String, Object>>() {
                            public Map<String, Object> call() {
                                try {
                                    Thread.sleep(100);
                                    log.info(dto.toString());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("2");
                               return null;
                            }
                        });
                executor.execute(future);*/
                long s = System.currentTimeMillis();
                Future<Map<String, Object>> task = payService.submitToThirdPay(dto);
                log.info("main do something");
                /*
                //若要等待结果进行处理
                while (true){
                    if(task.isDone()) {
                        log.info("break");
                        break;
                    }
                    //如果1秒内没有响应,取消任务
                    if (System.currentTimeMillis() - s > 1000) {
                        logger.info("单号:{}, ====上传超时,5秒钟内K11服务器无返回", );
                        task.cancel(true);
                    }
                }
                long end = System.currentTimeMillis();
                log.info("任务全部完成，总耗时：" + (end - s) + "毫秒");*/

            } else if (list.size() == 0) {
                r.setErrCode("-1");
                r.setMsg("设备,条码参数未能匹配商品");
            } else if (list.size() >= 2) {
                r.setErrCode("-2");
                r.setMsg("设备,条码重复,请联系后台处理 barcode:" + pay.getBarcode() + ", deviceId:" + pay.getDeviceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            r.setErrCode("-10001");
            r.setMsg("异常:" + e.getMessage());
        }
        return r;
    }

}
