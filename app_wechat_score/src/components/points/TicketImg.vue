<!-- 积分指南 -->
<template>
  <img :src="goImg"/>
</template>

<script>
  export default {
    name: 'TicketImg',
    data() {
      return {
        goImg: ''
      }
    },
    methods: {},
    mounted() {
      console.log("Ticket Image Url:" + JSON.stringify(this.$route.params.id));
      this.goImg = this.$route.params.id;
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../../static/wechat/css/points/scoreInquire.css";
  @import "../../../static/wechat/css/points/scoreTicketRecord.css";
</style>
