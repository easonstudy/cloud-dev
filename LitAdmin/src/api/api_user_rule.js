/**
 * 会员规则
 *
 */
import * as API from './'

export default {

  //获取标签配置
  findTagsList: params => {
    return API.GET('/api/v1/user/rule/tags/list', params)
  },

  //获取标签配置
  mergeTagsList: data => {
    return API.POST_JSON('/api/v1/user/rule/tags/merge', data, {})
  }
}
