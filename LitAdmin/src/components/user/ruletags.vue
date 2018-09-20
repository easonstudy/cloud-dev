<!-- 会员规则-标签设置 -->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>会员规则</el-breadcrumb-item>
        <el-breadcrumb-item>标签设置</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <div v-for="(item,index) in ruletags" :key="index">
        <el-col :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span v-if="item.tagsId == 1">消费等级</span>
              <span v-if="item.tagsId == 2">消费次数</span>
              <span v-if="item.tagsId == 3">促敏消费</span>
              <span v-if="item.tagsId == 4">购买能力</span>
              <!--<span> {{item.categroy}} - {{item.tagsId}} </span>-->
            </div>
            <div v-if="item.tagsId == 1" class="text item">
              <el-form label-width="120px">
                <el-form-item label="低消费等级(元)">
                  <el-input v-model="item.levelLow"></el-input>
                </el-form-item>
                <el-form-item label="中消费等级(元)">
                  <el-input v-model="item.levelMiddle"></el-input>
                </el-form-item>
                <el-form-item label="高消费等级(元)">
                  <el-input v-model="item.levelHigh"></el-input>
                </el-form-item>
              </el-form>
            </div>
            <div v-else-if="item.tagsId == 2" class="text item">
              <el-form label-width="120px">
                <el-form-item label="低消费次数(次)">
                  <el-input v-model="item.levelLow"></el-input>
                </el-form-item>
                <el-form-item label="中消费次数(次)">
                  <el-input v-model="item.levelMiddle"></el-input>
                </el-form-item>
                <el-form-item label="高消费次数(次)">
                  <el-input v-model="item.levelHigh"></el-input>
                </el-form-item>
              </el-form>
            </div>
            <div v-else-if="item.tagsId == 3" class="text item">
              <el-form label-width="120px">
                <el-form-item label="低促敏消费(元)">
                  <el-input v-model="item.levelLow"></el-input>
                </el-form-item>
                <el-form-item label="中促敏消费(元)">
                  <el-input v-model="item.levelMiddle"></el-input>
                </el-form-item>
                <el-form-item label="高促敏消费(元)">
                  <el-input v-model="item.levelHigh"></el-input>
                </el-form-item>
              </el-form>
            </div>

            <div v-else class="text item">
              <el-form label-width="120px">
                <el-form-item label="低购买能力(元)">
                  <el-input v-model="item.levelLow"></el-input>
                </el-form-item>
                <el-form-item label="中购买能力(元)">
                  <el-input v-model="item.levelMiddle"></el-input>
                </el-form-item>
                <el-form-item label="高购买能力(元)">
                  <el-input v-model="item.levelHigh"></el-input>
                </el-form-item>
              </el-form>
            </div>
          </el-card>
        </el-col>
      </div>
      <el-col :span="2">
        <br/><br/><br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<el-button type="primary" @click="submit">提交</el-button>
      </el-col>
    </el-col>

  </el-row>
</template>

<script>
  import API from '../../api/api_user_rule';

  export default {
    data() {
      return {
        loading: false,
        ruletags: []

      }
    },
    methods: {
      handleSearch() {
        this.search();
      },
      //获取用户列表
      search: function () {
        let that = this;
        let params = {};
        that.loading = true;
        API.findTagsList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.ruletags = result.data;
          }
        }, function (err) {
          that.loading = false;
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.loading = false;
          console.log(error);
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },

      submit: function () {
        let that = this;
        //let json = JSON.stringify(this.ruletags);
        //console.log("json:" + json);
        API.mergeTagsList(this.ruletags).then(function (result) {
          //console.log(result);
          if (result && result.errorCode == "0") {
            that.$message.success({showClose: true, message: '修改成功', duration: 1500});
          } else {
            that.$message.error({showClose: true, message: result.msg, duration: 2000});
          }
        }, function (err) {
          that.loading = false;
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.loading = false;
          console.log(error);
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });


      }
    },
    mounted() {
      this.handleSearch();
    }
  }
</script>

<style scoped>

</style>
