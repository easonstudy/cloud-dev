import * as API from './'
import Env from './env'

let authorizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
  "appid=wx621a5c621c43da89" +
  "&redirect_uri=" + encodeURIComponent("http://api.android-pos.cn/wx/authorize") +
  "&response_type=code" +
  "&scope=snsapi_base" +
  "&state=customState#wechat_redirect";

export default {
  jssdk: (params) => {
    return API.WX_POST(`${Env.openURL}/api/v1/jssdk`, params)
  },
  getImageUrl: (params) => {
    return API.WX_GET(`${Env.openURL}/api/v1/images/url`, params)
  },
  authorization(customState) {
    alert("authorizeUrl:" + authorizeUrl);
    return API.WX_GET(authorizeUrl.replace("customState", customState));
  }
}
