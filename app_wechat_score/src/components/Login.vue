<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div style="text-align: center;">
    <div class="formLogin" :data="account">
      <div class="safeCode inputDiv">
        <span class="title">手机号</span>
        <input type="tel" v-model="account.phone" maxlength="11" placeholder="请输入手机号"/>
        <span class="send" id="telSend" style="border-left:none"></span>
        <img src="../../static/publicImg/clear.svg" alt="" id="clearTel" class="clearInput none"/>
      </div>
      <div class="safeCode inputDiv">
        <span class="title">验证码</span>
        <input type="tel" v-model="account.code" maxlength="6" placeholder="请输入验证码"/>
        <span class="send" id="send" @click="sendCode" v-if="!secondVisible">发送验证码</span>
        <span class="send" id="senc" v-else>{{second}}s 重新发送</span>
        <img src="../../static/publicImg/clear.svg" alt="" id="clearInput" class="clearInput none"/>
      </div>
      <div>
        {{msg}}
      </div>
      <div class="note">
        <span>点击快速登录，即表示同意 </span>
        <!--http://app.mixcapp.com/wechat/index.php?r=member/user/agreement&amp;mallNo=0205A101-->
        <a href="#">服务条款和隐私条例</a>
      </div>
      <button type="button"
              :disabled="(account.phone==''&& account.code=='')"
              v-bind:class="{'gree_btn': (account.phone==''&& account.code==''), 'orag_btn':(account.phone!=''&& account.code!='') }"
              @click="doLogin()">快速登录
      </button>
    </div>
    <div style="height:15px; width: auto"></div>
    <div v-bind:class="{'popup':true,  'none': !errorVisible}" id="popup">
      <div class="message">
        {{errorMsg}}
      </div>
    </div>
    <!--
    <div class="mask none" id="mask"></div>
<div class="popup none" id="popup">
      <div class="message">
        发送验证码间隔时间太短， 请于<span class="mesSeconds" id="mesSeconds">32</span>s后重试
      </div>
      <div class="option" id="option">
        知道了
      </div>
    </div>
    <div class="loading-container none" id="loading-container">
      <div class="loading"></div>
      <span id="loading-tips">请求中……</span>
    </div>
    <div class="loading-img none" id="bottom-loading"><img  src="./publicImg/loading.gif" alt=""></div>
    <div class="error-tip none" id="toastErrorTip"></div>-->

  </div>
</template>

<script>
  import {setUser, setToken} from '@/utils/session';
  import API_MEMBER from '../api/api_member';
  import API_WX from '../api/api_wx';
  import Env from '../api/env';

  export default {
    name: 'Login',
    data() {
      return {
        secondVisible: false,
        second: 60,
        account: {
          phone: '',
          code: ''
        },
        msg: '',
        errorVisible: false,
        errorMsg: ''
      }
    },
    beforeCreate: function () {
    },
    methods: {
      sendCode: function () {
        let that = this;
        let params = {type: 0};

        //console.log("v:" + (that.account.phone == '') + ", z:" + that.account.code.length != 6)
        if (that.account.phone == '' || that.account.phone.length != 11) {
          that.errorVisible = true;
          that.errorMsg = '请输入11位手机号';
          setTimeout(() => {
            that.errorVisible = !1;
          }, 2000);
          return;
        }

        API_MEMBER.send(that.account.phone, params).then(function (result) {
          if (result && result.errorCode == "0") {
            that.secondVisible = true;
            that.second = 60;
            if (that.timer) {
              clearInterval(that.timer);  //在Vue实例销毁前，清除我们的定时器
            }
            that.timer = setInterval(function () {
              that.second = that.second - 1;
              if (that.second == -1) {
                that.secondVisible = false;
              }
            }, 1000);
            that.msg = "发送成功:" + result.errorCode + ", 验证码:" + result.code;
          } else {
            that.msg = "错误代码:" + result.errorCode + ", 原因:" + result.msg;
          }
        }, function (err) {
          console.log(err);
        }).catch(function (error) {
          console.log(error);
        })
      },
      doLogin: function () {
        let that = this;
        let loginParams = {
          'm': 1,
          'phone': that.account.phone,
          'code': that.account.code
        };

        if (that.account.phone == '') {
          that.errorVisible = true;
          that.errorMsg = '请输入11位手机号';
          setTimeout(() => {
            that.errorVisible = !1;
          }, 2000);
          return;
        }
        if (that.account.code.length != 6) {
          that.errorVisible = true;
          that.errorMsg = '请输入6位验证码';
          setTimeout(() => {
            that.errorVisible = !1;
          }, 2000);
          return;
        }

        console.log(JSON.stringify(loginParams))
        API_MEMBER.login(loginParams).then(function (result) {
          //alert('login:' + JSON.stringify(result));
          if (result.errorCode == '0') {
            //保存用户资料
            setUser(JSON.stringify(result.user));
            setToken(result.token);

            //获取微信身份
            /*let params = "app_score_self|" + result.user.phone;
            API_WX.authorization(params).then(function (result) {
              alert('wx:' + JSON.stringify(result));
              if (result.errorCode == '0') {

              } else {
                that.$message.error({showClose: true, message: result.errmsg || '登录失败', duration: 2000});
              }
            }, function (err) {
              that.$message.error({showClose: true, message: err.toString(), duration: 2000});
            }).catch(function (error) {
              that.$message.error({showClose: true, message: '请求微信授权出现异常', duration: 2000});
            });*/

            //跳转
            that.$router.push({path: Env.basePathPre + '/self'});
          } else if (result.errorCode == '-2') {
            that.$message.error({showClose: true, message: result.errmsg || '验证码错误', duration: 2000});
          } else {
            that.$message.error({showClose: true, message: result.errmsg || '登录失败', duration: 2000});
          }
        }, function (err) {
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },
      getPoint: function (obj) {
        var tmp = obj.offsetLeft;
        var val = obj.offsetParent;
        while (val != null) {
          tmp += val.offsetLeft;
          val = val.offsetParent;
        }
        return tmp;
      }
    },
    mounted: function () {
      let offsetLeft = document.getElementById("send").clientWidth;
      //alert(offsetLeft);
      document.getElementById("telSend").style.width = offsetLeft - 8 + 'px';


    },
    beforeDestroy: function () {
      if (this.timer) {
        clearInterval(this.timer);  //在Vue实例销毁前，清除我们的定时器
      }

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../static/wechat/css/member/login.css";

  .gree_btn {
    background-color: #cccccc !important;
  }

  .orag_btn {
    background-color: #fe8a3d !important;
  }
</style>
