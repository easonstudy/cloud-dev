import * as API from './'

export default {
  entity: {
    id:0,
    shopsId: 0,
    shopsName: '',
    deviceId: ''
  },
  //单个删除
  remove: id => {
    return API.DELETE(`/api/v1/shops_device/${id}`)
  },
  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/shops_device/merge', params)
  },
  //查询获取 列表(通过page分页)
  findList: params => {
    return API.POST('/api/v1/shops_device/list', params)
  }

}
