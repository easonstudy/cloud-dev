import * as API from './'

export default {
  entity: {
    id: 0,
    type: 0,
    code: '',
    name: '',
    score: 0,
    number: 0,
    url: '',
    isDelete: 0,
    updateBy: '',
    createBy: ''
  },
  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/gift/merge', params)
  },
  //单个删除
  remove: id => {
    return API.DELETE(`/api/v1/gift/${id}`)
  },
  //查询获取 列表(通过page分页)
  findList: params => {
    return API.POST('/api/v1/gift/list', params)
  },

  //查询获取 列表(通过page分页)
  searchList: params => {
    return API.POST('/api/v1/gift/searchList', params)
  },
  //兑换礼品
  exchange: params => {
    return API.POST_JSON('/api/v1/gift/exchange', params, {})
  },
  //兑换礼品历史
  findExchangeList: (data, params) => {
    return API.POST_JSON('/api/v1/gift/exchange/list', data, params)
  }


}
