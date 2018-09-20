<!-- 自助积分首页 -->
<template>
  <div>
    <div class="scan">
      <span class="pub-mid-assist"></span>
      <div class="div  left" @click="goScan()">
        <img src="../../../static/publicImg/recommend-scan.png" alt="" id="scan-qrCode"/><br>
        <span class="title">扫码积分</span><br>
        <span class="details">支持二维码小票</span>
      </div>
      <div class="div right" @click="goView()">
        <img src="../../../static/publicImg/recommend-photo.png" alt="" id="photo-ticket"/><br>
        <span class="title">拍照积分</span><br>
        <span class="details">上传小票照片即可</span>
      </div>
    </div>
    <div class="items">
      <div class="item" id="points-history" @click="goTo('/score')">
        <span class="pub-mid-assist"></span>
        <div class="content">
          <div class="title">查询积分</div>
          <div class="details">笔笔明细,查找更容易</div>
        </div>
      </div>
      <div class="item " id="points-guide" @click="goTo('/help')">
        <span class="pub-mid-assist"></span>
        <div class="content">
          <div class="title">积分指南</div>
          <div class="details">积分疑惑在此帮忙您解答</div>
        </div>
      </div>
      <div class="item " id="points-ticket" @click="goTo('/record')">
        <span class="pub-mid-assist"></span>
        <div class="content">
          <div class="title">小票记录</div>
          <div class="details">自助积分审核时时知</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import wx from 'weixin-js-sdk';
  import Env from '../../api/env';

  import API_MEMBER from '../../api/api_member';
  import API_WX from '../../api/api_wx';
  import {getUser} from '@/utils/session';

  export default {
    name: 'Self',
    data() {
      return {
        wx: wx,
        user: {},
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
      initConfig: function () {
        let that = this;
        let params = {'url': 'http://android-pos.cn:8085/'};
        API_WX.jssdk(params).then(function (result) {
          //console.log("r:" + result);
          if (result && result.errorCode == "0") {
            that.wx.config({
              debug: false,            // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
              appId: result.appId,    // 必填，公众号的唯一标识
              timestamp: result.timestamp,  // 必填，生成签名的时间戳
              nonceStr: result.nonceStr, // 必填，生成签名的随机串
              signature: result.signature,// 必填，签名
              jsApiList: ['scanQRCode', 'chooseImage', 'uploadImage'] // 必填，需要使用的JS接口列表
            });
          }
        }, function (err) {
          console.log("err:" + err)
        }).catch(function (error) {
          console.log("error:" + error)
        });
      },
      goTo: function (url) {
        console.log(Env.basePathPre + url);
        this.$router.push({path: Env.basePathPre + url});
      },
      goView: function () {
        let that = this;
        that.wx.chooseImage({
          count: 1, // 默认9
          sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
          sourceType: ['album', 'camera'],      // 可以指定来源是相册还是相机，默认二者都有
          success: function (res) {
            let localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            // 兼容浏览器 延迟100ms
            setTimeout(() => {
              that.wx.uploadImage({
                localId: localIds[0],   // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1,  // 默认为1，显示进度提示
                success: function (res) {
                  var serverId = res.serverId;        // 返回图片的服务器端ID
                  let data = {'serverId': serverId, 'local': 0};

                  //公众号服务器 通过serverId(media_id) 返回图片真实URL
                  API_WX.getImageUrl(data).then(function (result) {
                    //alert("Weixin system result:" + JSON.stringify(result));
                    if (result && result.errorCode == "0") {
                      //上传给积分服务器
                      let data = {'phone': that.user.phone, 'userId': that.user.id, 'url': result.data};
                      API_MEMBER.imageaudit(data, {}).then(function (result) {
                        if (result.errorCode == "0") {
                          alert("自动积分成功, 请点击小票记录查看.");
                        } else if (result.errorCode == "-10001") {
                          alert("提交审核失败.");
                        } else {
                          alert("自动积分不通过, 待审核, 请点击小票记录查看");
                          //" 错误码:" + result.errorCode + ", 原因:" + result.msg);
                        }
                      }, function (err) {
                        alert("上传失败:err:" + err);
                      }).catch(function (error) {
                        alert("上传失败:error:" + error);
                      });
                    }
                  }, function (err) {
                    alert("err:" + err)
                  }).catch(function (error) {
                    alert("error:" + error)
                  });
                }
              });
            }, 100)
          }
        });
      },
      goScan: function () {
        let that = this;
        that.wx.scanQRCode({
          needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
          scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
          success: function (res) {
            var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
            // 积分服务器 获取积分
            let params = {'phone': that.user.phone, 'receiptId': result};
            API_MEMBER.transaction(params).then(function (result) {
              //alert("score system transaction result:" + JSON.stringify(result));
              if (result && result.errorCode == "0") {
                alert("积分成功, 请点击积分记录查看.");
              } else {
                alert("积分失败, 错误码:" + result.errorCode + ", 原因:" + result.msg);
              }
            }, function (err) {
              console.log("err:" + err)
            }).catch(function (error) {
              console.log("error:" + error)
            });
          }
        });
      }
    },
    mounted() {
      let user = getUser();
      if (user) {
        this.user = JSON.parse(user);
      }
      this.initConfig();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../../static/wechat/css/points/scoreSelf.css";
</style>
