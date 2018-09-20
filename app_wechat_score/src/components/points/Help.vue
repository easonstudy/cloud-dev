<!-- 积分指南 -->
<template>
  <article class="score">
    <section class="body">
      <div class="item">
        <div class="">扫码积分：</div>
        <p class="">
          1、会员在绑定本人会员卡后，自消费日起七日内可扫描店铺购物小票上二维码进行自助积分。
        </p>
        <p class="">
          2、使用扫码积分时，请保持购物小票平整，将扫码框对准购物小票下方二维码进行扫描，扫码成功后系统将自动录入会员积分。
        </p>
      </div>
      <div class="item">
        <div class="">拍照积分：</div>
        <p class="">
          注意：单张购物小票金额超过1,000元（含1000元），或购物小票面积过大、过长，需会员本人持购物小票前往服务台或会员中心办理积分。
        </p>
        <p class="">
          1、会员绑定本人会员卡后，可凭购物小票七日内自助积分。（如：会员1月1日产生消费，可于1月7日前完成拍照上传。）
        </p>
        <p class="">
          2、自助积分凭证仅限购物中心内指定店铺购物小票，其他票据如：银行刷卡单、店铺发票、充值类小票、收据、手写票据等凭证不参与积分。
        </p>
        <p class="">
          3、购物小票需拍摄完整、清晰，且销售店铺、销售时间、消费金额三者缺一不可，如购物小票拍摄不清晰导致积分不成功，需拍摄清晰后重新上传。
        </p>
        <p class="">
          4、银行积分兑换、优惠券支付部分，不计入积分。
        </p>
        <p class="">
          5、会员可在消息中心查看积分是否补录成功。
        </p>
        <p class="">
          6、会员可随时通过“个人中心”-“我的积分”查询已有积分数量及拍照积分明细。
        </p>
        <p class="">
          7、单张购物小票仅限拍照积分1次，请勿重复上传已完成积分补录的购物小票或持他人购物小票进行拍照积分，以免计入违规积分。注意，恶意积分将冻结会员卡账号。
        </p>
        <p class="">
          8、如上述方式仍无法完成积分，请于消费之日起七日内至购物中心服务台进行积分。
        </p>
        <p class="">
          如有任何疑问，请致电 <a href="tel:0755 xxxxxxxx">0755-xxxxxxxx</a>
        </p>
      </div>
    </section>
  </article>
</template>

<script>
  export default {
    name: 'Help',
    data() {
      return {
        msg: 'Welcome to Your Vue.js App'
      }
    },
    methods: {
    },
    mounted() {

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  @import "../../../static/wechat/css/points/scoreInquire.css";
  @import "../../../static/wechat/css/points/scoreTicketRecord.css";
</style>
