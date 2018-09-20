import * as API from './'

export default {

  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/staff/merge', params)
  },

  //修改管理员-密码
  changePassword: params => {
    return API.PATCH('/api/v1/staff/changePassword', params)
  },

  remove: (id, params) => {
    return API.DELETE(`/api/v1/staff/${id}`, params)
  },

  //查询获取列表(通过page分页)
  findList: params => {
    return API.GET('/api/v1/staff/list', params)
  }
}
