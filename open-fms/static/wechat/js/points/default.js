var localIds = [];
var serverIds = [];

window.onload = function() {
  $('#points-guide').on('click', function() {
    //location.href = 'index.php?r=points/guide';
    homeSiteHeader('points/guide')
  });

  $('#points-history').on('click', function() {
    //location.href = 'index.php?r=points/history';
    homeSiteHeader('points/history')
  });
  $('#points-ticket').on('click', function() {
    //location.href = 'index.php?r=points/ticket';
    homeSiteHeader('points/ticket')
  });

    if(wechat_js){
        var wechat_js_obj = JSON.parse(wechat_js);

        if(wechat_js_obj){
            showLoading('正在请求中……');
            wx.config({
                debug: wechat_js_obj.debug,
                appId: wechat_js_obj.appId,
                timestamp: wechat_js_obj.timestamp,
                nonceStr: wechat_js_obj.nonceStr,
                signature: wechat_js_obj.signature,
                jsApiList: wechat_js_obj.jsApiList
            });
            wx.error(
                function(res){
                    hideLoading();
                }
            );
        }



    }




    function scanQrCode(m, d) {
    proRequest({
        api: 'points_scanQrcode',
        m: m,
        d: d
      },
      function() {

      },
      function(jsonObj) {
        if (jsonObj.code == 0) {
          var points = jsonObj.data.points;
          showToast("成功获取" + points + "积分");

        } else if (jsonObj.code == 102) {
          showToast("已经领取过积分");

        } else if (jsonObj.code == -1) {
          showToast("领取失败");
        }

      },
      function() {
        showToast('亲网络不给力，请稍后重试');
      });
  }

  function handerTicket(serverIds) {
    var t = new Date().getTime();

    $.ajax({
      url: makeHeaderUrl('points/default/ticket', {
        't': t
      }),
      timeout: 30000,
      type: 'post',
      data: {
        'serverIds': serverIds
      },
      dataType: 'json',
      beforeSend: function() {
        //showLoading('正在请求中……');
      },
      success: function(data) {
        if (data.code == 0) {
            hideLoading();
          showToast("拍照小票成功，请等待审核");
          homeSiteHeader('points/ticket');

        } else {
            hideLoading();
          showToast(data.msg);
        }

      },
      error: function() {
          hideLoading();
        showToast('亲网络不给力，请稍后重试');

      }
    });
  }

  function uploadImg() {
    if(localIds.length  == 0){
      var serverIdsstr = '';
      for(var i=0;i<serverIds.length;i++){
          serverIdsstr += serverIds[i] + "," ;
      }
        handerTicket(serverIdsstr);
    }else {
        var localId = localIds.pop()
        wx.uploadImage({
            localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: 0, // 默认为1，显示进度提示
            success: function (res) {
                var s = res.serverId;
                serverIds.push(s);
                uploadImg();
            }
        });
    }
  }



  function parseQueryString(url) {
    var obj = {};
    var keyvalue = [];
    var key = "",
      value = "";
    var start = url.indexOf("?") + 1;
    var end = url.indexOf('#');
    if (end == -1) {
      end = url.length;
    }

    var paraString = url.substring(start, end).split("&");
    for (var i in paraString) {
      keyvalue = paraString[i].split("=");
      key = keyvalue[0];
      value = keyvalue[1];
      obj[key] = value;
    }
    return obj;
  }


  wx.ready(function() {
    $('#scan-qrCode').on('touchend', function() {
      sendDataEvent("points",'scan_click','');
      wx.scanQRCode({
        needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
        scanType: ["qrCode"], // 可以指定扫二维码还是一维码，默认二者都有
        success: function(res) {
          var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
          if (result) {
            var params = parseQueryString(result);
            var mallNo = getMallNo();
            if (params.d == undefined || params.m == undefined) {
              hideLoading();
              showToast("不是本商场的小票二维码");
            } else if (params.m != mallNo) {
              hideLoading();
              showToast("不是本商场的小票二维码");
            } else {
              scanQrCode(mallNo, params.d);
            }

          }
        }
      });
    });

    $('#photo-ticket').on('touchend', function() {
      sendDataEvent("points",'photo_click','');
      wx.chooseImage({
        count: 3,
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function(res) {
          //var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
          //var serverIds = "";
         // var count = 0;
          showLoading('正在请求中……');
          //sendDataEvent("points",'photo_album_click','');

            localIds = res.localIds;
            serverIds = [];

            uploadImg();


        }
      });
    });

    hideLoading();
  });


    sendDataEvent("points",'menu_click','');
}

