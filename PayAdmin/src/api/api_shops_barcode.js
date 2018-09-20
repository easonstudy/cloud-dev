import * as API from './'

export default {
  entity: {
    id: 0,
    shopsId: '',
    shopsName: '',
    name: '',
    wechatCode: '',
    amount: 0,
    barcode: '',
    isDelete: 0,
    updateBy: '',
    createBy: ''
  },
  //单个删除
  remove: id => {
    return API.DELETE(`/api/v1/shops_barcode/${id}`)
  },
  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/shops_barcode/merge', params)
  },
  //查询获取 列表(通过page分页)
  findList: params => {
    return API.POST('/api/v1/shops_barcode/list', params)
  }

}
