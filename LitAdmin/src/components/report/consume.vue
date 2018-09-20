<!--消费数据统计-->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>报表统计</el-breadcrumb-item>
        <el-breadcrumb-item>消费数据统计</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24">
      <el-tabs type="border-card">
        <el-tab-pane>
          <span slot="label"><i class="el-icon-date"></i></span>
          我的行程
        </el-tab-pane>
        <el-tab-pane label="消息中心">积分</el-tab-pane>
        <el-tab-pane label="角色管理">角色管理</el-tab-pane>
        <el-tab-pane label="定时任务补偿">定时任务补偿</el-tab-pane>
      </el-tabs>

    </el-col>
  </el-row>

</template>

<script>
  export default {
    data(){
      return {
      }
    }
  }

</script>

<style scoped>
  .message{
    font-size: 18
  }
</style>
