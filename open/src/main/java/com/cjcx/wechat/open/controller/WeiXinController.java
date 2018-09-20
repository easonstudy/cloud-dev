package com.cjcx.wechat.open.controller;


import com.cjcx.wechat.open.service.member.ITransactionService;
import com.cjcx.wechat.open.utils.WeixinUtil;
import com.cjcx.wechat.open.web.ResultObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import weixin.popular.api.MediaAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.media.Media;
import weixin.popular.bean.media.MediaType;
import weixin.popular.bean.message.massmessage.MassImageMessage;
import weixin.popular.bean.user.Group;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.net.URL;
import java.util.HashSet;

/**
 * 接收 微信服务器 消息
 */
@RestController
@RequestMapping("/wx")
public class WeiXinController extends AdminSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ITransactionService transactionService;

    /**
     * 网页授权回调地址 扫码信息接收
     *
     * @param code  获取用户微信信息
     * @param state 参数  系统小票标识，注册标识,...
     *              返回界面
     */
    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public ResultObject authorize(@RequestParam("code") String code, @RequestParam("state") String state) {
        ResultObject result = new ResultObject();
        long s = System.currentTimeMillis();
        try {
            result = transactionService.handleAuthorize(code, state);

            /*Integer errorCode = Integer.parseInt(map.get(ResultObject.PARAM_ERRCODE).toString());
            result.setErrCode(errorCode + "");
            if (errorCode == 0) {
                result.setMsg("兑换成功, 总积分:" + map.get(ResultObject.PARAM_MSG).toString());
            } else {
                result.setMsg("兑换失败: " + map.get(ResultObject.PARAM_MSG).toString());
            }
            request.setAttribute("value", result);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //logger.info("总用时:" + (System.currentTimeMillis() - s));
        //ModelAndView mv = new ModelAndView("scanResult");
        return result;
    }

    /**
     * 网页授权回调地址  认证方法
     *
     * @return
     */
    @RequestMapping(value = "/authorize/MP_verify_rdO44DvVYQq6wMWI.txt", method = RequestMethod.GET)
    public ModelAndView authorizeFile() {
        String fileName = "MP_verify_rdO44DvVYQq6wMWI.txt";

        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);// 设定输出文件头
        response.setContentType("text/x-plain");// 定义输出类型
        try {
            ServletOutputStream out = response.getOutputStream();

            String path = System.getProperty("java.io.tmpdir") + "/" + fileName;
            logger.debug("path:" + path);
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(fos, "utf-8");

            String text = "rdO44DvVYQq6wMWI";
            writer.write(text);
            writer.close();
            fos.close();

            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream(4096);

            byte[] cache = new byte[4096];
            for (int offset = fis.read(cache); offset != -1; offset = fis.read(cache)) {
                byteOutputStream.write(cache, 0, offset);
            }

            byte[] bt = null;
            bt = byteOutputStream.toByteArray();

            out.write(bt);
            out.flush();
            out.close();
            fis.close();
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送二维码图像到 公众号
     *
     * @param receiptId
     * @param isCharactor
     * @return
     */
    @RequestMapping(value = "/sendQrcode", method = RequestMethod.GET)
    public ResultObject sendQrcode(@RequestParam("receiptId") String receiptId,
                                   @RequestParam(value = "isCharactor") boolean isCharactor) {
        ResultObject result = new ResultObject();
        try {
            //接口凭证
            String access_token = WeixinUtil.getAccessToken();

            //生成临时参数二维码
            String param = "123";
            String imgurl = WeixinUtil.createTempQrcode(isCharactor, param);
            if (imgurl == null) {
                result.setErrCode("-1");
                return result;
            }

            // 上传图片到素材管理  https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738726
            URL url = new URL(imgurl);
            Media media = MediaAPI.mediaUpload(access_token, MediaType.image, url.openStream());
            if (media == null) {
                result.setErrCode("-2");
                return result;
            }

            //群发消息  https://github.com/liyiorg/weixin-popular/wiki/MessageAPI
            //获取分组ID
            Group group = UserAPI.groupsGet(access_token);
            String gid = group.getGroups().get(0).getId();

            //媒体，图片类型  先上传图片文件 获取media_id
            String media_id = media.getMedia_id();
            MassImageMessage imageMessage = new MassImageMessage(media_id);
            //imageMessage.setFilter(new Filter(false,gid));
            //MessageAPI.messageMassSendall(access_token, imageMessage);
            imageMessage.setTouser(new HashSet<String>());
            imageMessage.getTouser().add("ohwkW0igK5S96F7ZUN4xXvmqtsgQ");
            imageMessage.getTouser().add("ohwkW0gX1cXLBa2CeIzyTDI8IZhU");
            MessageAPI.messageMassSend(access_token, imageMessage);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            result.setErrCode("-5");
        }
        return result;
    }


}
