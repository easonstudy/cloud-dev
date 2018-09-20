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
    return API.POST('/api/v1/score/ratio/merge', params)
  },
  //查询获取 列表(通过page分页)
  findList: params => {
    return API.POST('/api/v1/score/ratio/list', params)
  },
  findAll: () => {
    return API.POST('/api/v1/score/ratio/list/all', {})
  }


}
