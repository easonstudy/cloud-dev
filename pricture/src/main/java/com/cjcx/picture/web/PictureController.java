package com.cjcx.picture.web;

import com.cjcx.picture.service.IPictureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class PictureController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPictureService pictureService;

    /**
     * 文件上传
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public String pictureUpload(HttpServletRequest request,
                                @RequestParam(value = "fileUpload") MultipartFile file
    ) {
        String json = "";
        try {
            /*String p = request.getParameter("local");
            logger.info("upload local:" + p);
            Integer local = p == null ? 1 : Integer.parseInt(p);*/
            Map result = pictureService.uploadPicture(file, -1);
            // 浏览器擅长处理json格式的字符串，为了减少因为浏览器内核不同导致的bug，建议用json
            json = new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 上传未审核小票
     *
     * @param request
     * @param url     图片网络地址
     * @return
     */
    @RequestMapping("/receipt_audit")
    public String pictureUpload(HttpServletRequest request,
                                @RequestParam(value = "url") String url,
                                @RequestParam(value = "local", required = false) Integer local) {
        String json = "";
        try {
            Map result = pictureService.uploadPicture(url, local);
            // 浏览器擅长处理json格式的字符串，为了减少因为浏览器内核不同导致的bug，建议用json
            json = new ObjectMapper().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
