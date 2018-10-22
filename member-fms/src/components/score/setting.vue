<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分规则</el-breadcrumb-item>
        <el-breadcrumb-item>动态设置</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <div>
        <el-form :inline="false" :model="editForm" ref="editForm">
          <el-row>
            <el-col :span="12">
              <el-form-item label="名称" prop="name">
                <el-input v-model="editForm.name" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="比列" prop="value">
                <el-input v-model="editForm.value" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
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
</style>
