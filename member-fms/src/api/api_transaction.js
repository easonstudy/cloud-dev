import * as API from './'

export default {
  //查询获取列表(通过page分页)
  findList: params => {
    return API.GET('/api/v1/transaction/list', params)
  }
}
