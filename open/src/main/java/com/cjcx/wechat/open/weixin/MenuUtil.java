package com.cjcx.wechat.open.weixin;

import com.cjcx.wechat.open.utils.WeixinUtil;
import weixin.popular.bean.menu.Button;
import weixin.popular.bean.menu.MenuButtons;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义菜单 => 数据
 */
public class MenuUtil extends WeixinUtil {


    public static MenuButtons initMenuButtons(){
        MenuButtons menuButtons = new MenuButtons();
        //一级菜单
        Button[] buttons = new Button[3];
        buttons[0] = getButton0();
        buttons[1] = getButton1();
        buttons[2] = getButton2();
        menuButtons.setButton(buttons);
        return menuButtons;
    }

    /**
     * 往期回顾
     * @return
     */
    private static Button getButton0(){
        Button btn = new Button();
        btn.setName("解决方案");
        List<Button> list = new ArrayList<Button>();
        list.add(box());
        list.add(qrPlan());
        list.add(successPlan());
        btn.setSub_button(list);
        return btn;
    }

    /**
     * 功能演示
     * @return
     */
    private static Button getButton1(){
        Button btn = new Button();
        btn.setName("功能演示");
        List<Button> list = new ArrayList<Button>();
        list.add(funtionDetail());
        list.add(profile());
        list.add(perfect());
        list.add(scoreQrcode());
        list.add(scancdoe());
        btn.setSub_button(list);
        return btn;
    }
    /**
     * 服务
     * @return
     */
    private static Button getButton2(){
        Button btn = new Button();
        btn.setName("服务");
        //3位置 子菜单
        List<Button> list = new ArrayList<Button>();
        list.add(selfHelpScore());
        list.add(requestTest());
        list.add(contactUs());
        btn.setSub_button(list);
        return btn;
    }

    /**
     * 联系我们
     * @return
     */
    private static Button contactUs(){
        Button btn = new Button();
        btn.setName("联系我们");
        btn.setType("view");
        btn.setUrl("http://mp.weixin.qq.com/s/xUyGCoFv7L3VKgGtaEkMVQ");
        return btn;
    }
    /**
     * 申请测试
     * @return
     */
    private static Button requestTest(){
        Button btn = new Button();
        btn.setName("申请测试");
        btn.setType("click");
        btn.setKey("request");
        return btn;
    }

    /**
     * 自助积分
     * @return
     */
    private static Button selfHelpScore(){
        Button btn = new Button();
        btn.setName("自助积分");
        btn.setType("view");
        String url  = null;
        /*try {
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid="+WeixinUtil.APPID+
                    "&redirect_uri="+ URLEncoder.encode("http://api.android-pos.cn:8085/#/app/wechat/login", "UTF-8") +
                    "&response_type=code" +
                    "&scope=snsapi_base" +
                    "&state=user#wechat_redirect";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        url ="http://android-pos.cn:8085/#/app/wechat/login";
        //String url ="http://www.yunpiaobox.cn:8085/#/app/wechat/login";
        btn.setUrl(url);
        //scope=snsapi_base     是用来获取进入页面的用户的openid的，并且是静默授权并自动跳转到回调页的。
        //                      用户感知的就是直接进入了回调页（往往是业务页面）
        //scope=snsapi_userinfo 是用来获取用户的基本信息的。但这种授权需要用户手动同意
        /*
        appid	是	公众号的唯一标识
        redirect_uri	是	授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理
        response_type	是	返回类型，请填写code
        scope	是	应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
        state	否	重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
        #wechat_redirect	是	无论直接打开还是做页面302重定向时候，必须带此参数*/

        return btn;
    }
    /**
     * 个人信息, 积分查询
     * @return
     */
    private static Button profile(){
        Button btn = new Button();
        btn.setName("积分查询");
        btn.setType("click");
        btn.setKey("profile");
        return btn;
    }

    /**
     * 功能演示
     * @return
     */
    private static Button funtionDetail(){
        Button btn = new Button();
        btn.setName("功能演示说明");
        btn.setType("view");
        btn.setUrl("https://mp.weixin.qq.com/s/cZmWspcgwFFWv8hehxp7Mg");
        return btn;
    }
    /**
     * 完善资料
     * @return
     */
    private static Button perfect(){
        Button btn = new Button();
        btn.setName("完善资料");
        btn.setType("click");
        btn.setKey("perfect");
        return btn;
    }
    /**
     * 扫码积分
     * @return
     */
    private static Button scancdoe(){
        Button btn = new Button();
        btn.setName("扫码积分");
        btn.setType("scancode_waitmsg");
        //btn.setType("scancode_push");
        btn.setKey(WeixinUtil.SCANCODE_PUSH_KEY_ADD_SCORE);
        return btn;
    }
    /**
     * 积分二维码
     * @return
     */
    private static Button scoreQrcode(){
        Button btn= new Button();
        btn.setName("生成演示二维码");
        btn.setType("click");
        btn.setKey("scoreImage");
        return btn;
    }

    /**
     * 盒子方案
     * @return
     */
    private static Button box(){
        Button btn = new Button();
        btn.setName("盒子采集方案");
        btn.setType("view");
        btn.setUrl("http://mp.weixin.qq.com/s/JWHzjJREG4E0ORTOXiUwmQ");
        return btn;
    }

    /**
     * 二维码方案
     * @return
     */
    private static Button qrPlan(){
        Button btn = new Button();
        btn.setName("二维码方案");
        btn.setType("view");
        btn.setUrl("http://mp.weixin.qq.com/s/FwakJRsYttTvZbYLI6zo0Q");
        return btn;
    }

    /**
     * 成功案例
     * @return
     */
    private static Button successPlan(){
        Button btn = new Button();
        btn.setName("成功案例");
        btn.setType("view");
        btn.setUrl("http://mp.weixin.qq.com/s/h00pg8iLxzMvIBBvh7nEWg");
        return btn;
    }

    /**
     * 最新文章
     * @return
     */
    private static Button history(){
        Button btn = new Button();
        btn.setName("最新文章");
        btn.setType("click");
        btn.setKey("history");
        return btn;
    }

    /**
     * 主页
     * @return
     */
    private static Button home(){
        Button btn = new Button();
        btn.setName("主页");
        btn.setType("view");
        btn.setUrl("http://mp.weixin.qq.com/mp/homepage?__biz=MzUyNDY1OTU5MQ==&hid=1&sn=6e275148fe9ec6857b47c4571ad9ab8c#wechat_redirect");
        return btn;
    }
}
