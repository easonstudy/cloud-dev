<!-- 积分查看 -->
<template>
  <div>
    <div class="show">
      <div class="title">当前积分</div>
      <div class="num">{{score}}</div>
      <div class="descript">目前明细中只能查询线上产生的积分</div>
    </div>
    <div id="detail">
      <div class="no-record-div" v-show="!listVisible">
        <img src="../../../static/publicImg/no-record.png" alt="">
        <div class="descript">暂无积分明细</div>
      </div>

      <!--<div class="record" v-show="listVisible">
        <div class="content">
          <div v-for="item in rows">
              {{item.amount}}



          </div>
        </div>
      </div>-->
    </div>
  </div>
</template>

<script>
  import Env from '../../api/env';
  import API_MEMBER from '../../api/api_member';
  import {getUser} from '@/utils/session';

  export default {
    name: 'Score',
    data() {
      return {
        listVisible: true,
        user: {},
        score: 0,
        rows: []
      }
    },
    methods: {
      handlerSearch: function () {
        let that = this;
        let params = {'id': that.user.id};
        API_MEMBER.scoreSearch(params).then(function (result) {

          console.log("result:" + JSON.stringify(result));
          if (result && result.errorCode == 0) {
            that.score = result.score;
            that.rows = result.list;
            that.listVisible = that.rows.length > 0 ? true : false;
          }
        }, function (err) {
          alert('err:' + err)
        }).catch(function (error) {
          alert('error:' + error)
        })
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
  @import "../../../static/wechat/css/points/scoreInquire.css";
  @import "../../../static/wechat/css/points/scoreTicketRecord.css";
</style>
