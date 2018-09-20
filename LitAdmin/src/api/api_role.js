/**
 * Created by guo
 * 角色相关api
 */
import * as API from './'

export default {

  //查询获取role列表(通过page分页)
  findList: params => {
    return API.GET('/api/v1/role/list', params)
  },

  //查询获取permission列表
  permissionsList: params => {
    return API.GET('/api/v1/permissions/list', params)
  },

  permissionsUpdate: (id, params) => {
    return API.POST(`/api/v1/role/permissions/update/${id}`, params)
  },

  permissionsGet: (id, params) => {
    return API.GET(`/api/v1/role/permissions/${id}`, params)
  }

}
