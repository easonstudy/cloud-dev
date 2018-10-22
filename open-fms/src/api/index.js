import Env from './env';
import axios from 'axios'
import {bus} from '../bus.js'
import {getToken} from '@/utils/session';

//axios.defaults.withCredentials = true;
//axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
//axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';//配置请求头

//添加一个请求拦截器
axios.interceptors.request.use(function (config) {
  console.log("token:" + getToken());
  if (getToken() != undefined) {
    config.headers['Authorization'] = getToken();
  }
  return config;
}, function (error) {
  // Do something with request error
  return Promise.reject(error);
});

// 添加一个响应拦截器
axios.interceptors.response.use(function (response) {
  if (response.data) {
    //console.log("errorCode:"+response.data.errorCode);
    if (response.data.errorCode === 40001) {
      //未登录
      bus.$emit('goto', Env.basePathPre + '/login')
    } else if (response.data.errorCode === 40002) {
      //Token失效
      bus.$emit('goto', Env.basePathPre + '/login')
    }
  }

  return response;
}, function (error) {
  // Do something with response error
  return Promise.reject(error);
});

//基地址
let base = Env.baseURL;

//测试使用
export const ISDEV = Env.isDev;

//通用方法
export const POST = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    headers: {
      'Content-type': 'application/x-www-form-urlencoded'
    },
    params: params
  }).then(res => res.data);
  //return axios.post(`${base}${url}`, params).then(res => res.data)
}

export const GET = (url, params) => {
  return axios.get(`${base}${url}`, {params: params}).then(res => res.data)
}

export const PUT = (url, params) => {
  return axios.put(`${base}${url}`, params).then(res => res.data)
}

export const DELETE = (url, params) => {
  return axios.delete(`${base}${url}`, {params: params}).then(res => res.data)
}

export const PATCH = (url, params) => {
  return axios({
    method: 'patch',
    url: `${base}${url}`,
    headers: {
      'Content-type': 'application/x-www-form-urlencoded'
    },
    params: params
  }).then(res => res.data);
  //return axios.patch(`${base}${url}`, params).then(res => res.data)
}

/**
 *
 * @param url
 * @param data  实体
 * @param params 分页参数
 * @returns {Promise.<TResult>}
 * @constructor
 */
export const POST_JSON = (url, data, params) => {
  return axios({
    method: 'POST',
    url: `${base}${url}`,
    headers: {
      'Content-type': 'application/json;charset=utf-8'
    },
    data: JSON.stringify(data),
    params: params
  }).then(res => res.data);
}

export const WX_POST = (url, params) => {
  return axios({
    method: 'post',
    url: `${url}`,
    headers: {
      'Content-type': 'application/x-www-form-urlencoded'
    },
    params: params
  }).then(res => res.data);
}
export const WX_POST_JSON = (url, data, params) => {
  return axios({
    method: 'post',
    url: `${url}`,
    headers: {
      'Content-type': 'application/json;charset=utf-8'
    },
    data: JSON.stringify(data),
    params: params
  }).then(res => res.data);
}
export const WX_GET = (url, params) => {
  return axios.get(`${url}`, {params: params}).then(res => res.data)
}


