import * as API from './'

export default {
  entity: {
    id: 0,
    name: '',
    ratio: 1,
    isDelete: 0,
    remark: '',
    updateBy: '',
    createBy: ''
  },
  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/score/audit/merge', params)
  },
  //查询获取 列表(通过page分页)
  findList: params => {
    return API.POST('/api/v1/score/audit/list', params)
  },
  pull: params => {
    return API.POST('/api/v1/score/audit/pull', params)
  },
  audit: (data) => {
    return API.POST_JSON('/api/v1/score/audit/submit', data, {})
  },
  searchList: (params) => {
    return API.POST('/api/v1/score/audit/history', params)
  }
}
