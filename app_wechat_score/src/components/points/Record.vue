<!-- 积分查看 -->
<template xmlns:v-bind="http://www.w3.org/1999/xhtml">
  <div>
    <div class="no-record-div" v-show="!listVisible">
      <img src="../../../static/publicImg/no-record.png" alt="">
      <div class="descript">暂无记录</div>
    </div>

    <div v-for="item in rows" v-show="listVisible">
      <div
        v-bind:class="{'todo': item.state==0 || item.state==2 ,'fail': item.state == 3, 'sucess': item.state == 1 ,'record':true}">
        <div class='record-time'><span>{{item.createTime | formatAccountDay}}</span></div>

        <div class="score" v-if="item.state == 1">
          <span class="pub-mid-assist"></span>
          <div class="descript">
            <div class="title"> 获得积分数</div>
            <div class="details">
              <span class="num">{{item.score}}</span>
              <span class="name">积分</span>
            </div>
          </div>
        </div>
        <div class="reason" v-else-if="item.state == 3">
          <span class="pub-mid-assist"></span>
          <div class="div">
            <div class="title status">积分失败</div>
            <div class="descript">非官方票</div>
          </div>
        </div>

        <div class="content">
          <span class="pub-mid-assist"></span>
          <div class="begin descript">
            <span class="status" v-if="item.state == 0 || item.state == 2" style="color: #000;">等待审核</span>
            <span class="status" v-if="item.state == 1 || item.state == 3">已审核</span>
            <div class="status-time">
              <span class="time1">{{item.createTime | formatBeginDate}}</span>
            </div>
          </div>
          <div class="end descript todop">
            <span class="status" v-if="item.state == 1">积分成功</span>
            <span class="status" v-if="item.state == 3">积分失败</span>
            <div class="status-time">
              <span v-if="item.state == 0" style="color: black">预计处理时间<br/></span>
              <span class="time1" v-if="item.state == 0">
                {{item.createTime | formatSuccessDate}}
              </span>
              <span class="time1" v-else>
                {{item.updateTime | formatSuccessDate}}
              </span>
            </div>
          </div>
          <span class="line"></span>
        </div>
        <div class="go">
          <img dom="gotoImg" src="../../../static/publicImg/arrowsDown.png" alt="">
          <span dom="gotoImg" @click="goImg(item.url)" class="descript">查看小票照片 </span>
        </div>
      </div>

    </div>
  </div>

</template>

<script>
  import Env from '../../api/env';
  import API_MEMBER from '../../api/api_member';
  import moment from 'moment';
  import {getUser} from '@/utils/session';
  import {formatDate} from '../../utils/date';

  export default {
    name: 'Record',
    filters: {
      formatBeginDate(time) {
        //let date = new Date(time);
        //return formatDate(date, 'yyyy/MM/dd hh:mm:ss');
        //return date.Format('yyyy/MM/dd hh:mm:ss');
        return moment(time).format("YYYY-MM-DD HH:mm:ss");
      },
      formatSuccessDate(time) {
        //let date = new Date(time);
        //date.setDate(date.getDate() + 3)
        //return formatDate(date, 'yyyy/MM/dd hh:mm:ss');
        //return date.Format('yyyy/MM/dd hh:mm:ss');
        return moment(time).add(3, 'days').format("YYYY-MM-DD HH:mm:ss");
      },
      formatAccountDay(time) {
        //let date = new Date(time);
        //return date.Format('yyyy/MM/dd');
        return moment(time).format("YYYY-MM-DD");
      }
    },
    data() {
      return {
        listVisible: true,
        filters: {},
        rows: []
      }
    },
    methods: {
      handlerSearch: function () {
        let that = this;
        let data = Object.assign({'userId': that.user.id}, {ticketType: 0});
        let params = {pageNum: 1, pageSize: 999};
        //console.log("params:" + JSON.stringify(params));
        API_MEMBER.receiptRecord(data, params).then(function (result) {
          console.log("handlerSearch receiptRecord result:" + JSON.stringify(result));
          if (result && result.errorCode == 0) {
            that.rows = result.data.list;
            that.listVisible = that.rows.length > 0 ? true : false;
          }
        }, function (err) {
          alert('err:' + err)
        }).catch(function (error) {
          alert('error:' + error)
        })
      },
      goImg: function (url) {
        console.log("goImg url:" + url);
        // path: Env.basePathPre + '/ticketImg',
        this.$router.push({
            name: 'TicketImg',
            params: {id: Env.imageServerUrl + "/images/receipt_audit/" + url}
          }
        );
      }
    },
    mounted() {
      let user = getUser();
      if (user) {
        this.user = JSON.parse(user);
      }
      this.handlerSearch();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../../static/wechat/css/points/scoreTicketRecord.css";
  @import "../../../static/wechat/css/points/scoreReceiptRecord.css";
</style>
