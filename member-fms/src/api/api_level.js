
import * as API from  './'

export default {
  //新增&修改
  update: (params) => {
    return API.POST('/api/v1/userlevel/merge', params)
  },
  //单个删除
  remove: id => {
    return API.DELETE(`/api/v1/userlevel/${id}`)
  },
  //查询获取 等级列表(通过page分页)
  findList: params => {
    return API.GET('/api/v1/userlevel/list', params)
  }
}
