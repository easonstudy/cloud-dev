/**
 * Created by jerry on 2017/11/13.
 * 用户相关api
 */
import * as API from './'

export default {
  //管理员-登录
  login: params => {
    return API.POST('/api/v1/login', params)
  },
  //管理员-登出
  logout: params => {
    return API.GET('/api/v1/logout', params)
  },
  //修改管理员-密码
  changePassword: params => {
    return API.PATCH('/api/v1/changePassword', params)
  },
  //修改管理员-个人信息
  changeProfile: params => {
    return API.PATCH('/api/v1/changeProfile', params)
  },

  //完善用户数据
  perfect: params => {
    return API.POST('/api/v1/user/perfect', params)
  },
  //申请资料用户数据
  request: params => {
    return API.POST('/api/v1/user/request', params)
  },

  //user查询
  search: id => {
    return API.GET(`/api/v1/user/search/${id}`)
  },

  //单个删除user
  remove: id => {
    return API.DELETE(`/api/v1/user/${id}`)
  },
  update: (id, params) => {
    return API.PUT(`/api/v1/user/${id}`, params)
  },
  //查询获取user列表(通过page分页)
  findList: params => {
    //return API.GET('/api/v1/users', params)
    return API.GET('/api/v1/user/list', params)
  }
}
