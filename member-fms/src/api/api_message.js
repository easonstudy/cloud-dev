import * as API from './'

export default {

  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/message/merge', params)
  },

  remove: (id) => {
    return API.DELETE(`/api/v1/message/${id}`)
  },

  //查询获取列表(通过page分页)
  findList: params => {
    return API.GET('/api/v1/message/list', params)
  }
}
