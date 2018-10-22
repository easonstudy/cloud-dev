<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分管理</el-breadcrumb-item>
        <el-breadcrumb-item>兑换历史</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>


    <el-col :span="24" class="warp-breadcrum">
      <el-col :span="24" class="toolbar" v-loading="loading" style="padding-bottom: 0px;"
              element-loading-text="拼命加载中">
        <!-- 表单 -->
        <el-form :inline="true" :model="filters">
          <el-form-item>
            <el-input v-model="filters.userUniqueId" placeholder="会员卡/电话" style="min-width: 240px;"
                      @keyup.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.giftCode" placeholder="礼品编码" style="min-width: 240px;"
                      @keyup.enter.native="search"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.giftName" placeholder="礼品名称" style="min-width: 240px;"
                      @keyup.enter.native="search"></el-input>
          </el-form-item>

          <el-form-item>
            <el-date-picker
              v-model="filters.giftTime"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="search">查询</el-button>
          </el-form-item>
        </el-form>

        <!--列表-->
        <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
          <el-table-column prop="phone" label="会员电话" sortable>
          </el-table-column>
          <el-table-column prop="giftCode" label="编号" sortable>
          </el-table-column>
          <el-table-column prop="giftName" label="名称" sortable>
          </el-table-column>
          <el-table-column prop="score" label="积分" sortable>
          </el-table-column>
          <el-table-column prop="number" label="数量" sortable>
          </el-table-column>
          <!-- <el-table-column prop="state" label="结果" sortable>
           </el-table-column>-->
          <el-table-column prop="createTime" label="兑换时间" :formatter="dateFormat" min-width="120" sortable>
          </el-table-column>
        </el-table>

        <!--工具条-->
        <el-col :span="24" class="toolbar">
          <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                         :total="total" style="float:right;">
          </el-pagination>
        </el-col>

      </el-col>

    </el-col>


  </el-row>
</template>

<script>
  import API from '../../api/api_gift';
  import API_USER from '../../api/api_user';
  import moment from 'moment';

  export default {
    data() {
      return {

        //列表 - 查询表单
        filters: {
          userUniqueId: null,
          giftCode: null,
          giftName: null,
          giftTime: ''
        },

        //列表 - 集合
        rows: [],
        //分页数据
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1
      }
    },
    methods: {
      //时间格式化
      dateFormat: function (row, column) {
        var date = row[column.property];
        if (date == undefined) {
          return "";
        }
        return moment(date).format("YYYY-MM-DD HH:mm:ss");
      },

      handleSizeChange(val) {
        this.limit = val();
        this.search();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.search();
      },
      handleSearch() {
        this.total = 0;
        this.page = 1;
        this.search();
      },
      search: function () {
        let that = this;
        let params = {
          pageNum: that.page, pageSize: that.limit
        };
        that.loading = true;
        //整合表单数据

        let handlerFilters = {};
        if(params.giftTime != '') {
          handlerFilters = Object.assign({}, that.filters,
            {'giftStartTime': that.filters.giftTime[0]},
            {'giftEndTime': that.filters.giftTime[1]})
        }
        //console.log("params:" + JSON.stringify(params) + "filters:" + JSON.stringify(that.filters));
        console.log("params:" + JSON.stringify(handlerFilters));

        //API.findExchangeList(that.filters, params).then(function (result) {
        API.findExchangeList(handlerFilters, params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.total = result.data.total;
            that.rows = result.data.list;
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
  .message {
    font-size: 18
  }
</style>
