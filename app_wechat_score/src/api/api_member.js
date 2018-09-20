import * as API from './'

/**
 * 会员积分系统
 */
export default {
  //登入
  login: params => {
    return API.POST(`/api/v1/user/login`, params)
  },
  //发送短信
  send: (phone, params) => {
    return API.POST(`/api/v1/sms/${phone}`, params)
  },
  //积分查询
  scoreSearch: params => {
    return API.POST(`/api/v1/user/score`, params)
  },
  //审核小票记录
  receiptRecord: (data, params) => {
    return API.POST_JSON(`/api/v1/score/audit/history`, data, params)
  },
  //扫码积分
  transaction: (params) => {
    return API.POST(`/api/v1/transaction/score`, params)
  },
  //审核图片提交 - 先从微信服务器拉取图片名字
  imageaudit: (data, params) => {
    return API.POST_JSON(`/api/v1/score/audit/image`, data, params)
  }


}
