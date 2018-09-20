<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分审核</el-breadcrumb-item>
        <el-breadcrumb-item>审核历史</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <!-- 查询表单 -->

      <!-- 审核历史 -->
      <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="userPhone" label="会员" sortable>
        </el-table-column>
        <el-table-column prop="url" label="图片" style="">
          <template slot-scope="scope">
            <img :src="scope.row.url" @click="viewImage(scope.row.url)" alt=""
                 style="cursor:pointer; width: 50px;height: 50px"/>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额">
        </el-table-column>
        <el-table-column prop="score" label="积分">
        </el-table-column>
        <el-table-column prop="updateBy" label="审核人">
        </el-table-column>
        <el-table-column prop="staffRemark" label="审核人备注">
        </el-table-column>
        <el-table-column prop="state" :formatter="formatState" label="状态" sortable>
        </el-table-column>
        <el-table-column prop="updateTime" :formatter="dateFormat" label="审核时间" sortable>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>
      </el-col>

      <!-- 图片窗口 -->
      <el-dialog title="图片" :visible.sync="imgViewVisible">
        <img :src="imgViewUrl">
      </el-dialog>
    </el-col>


  </el-row>
</template>

<script>
  import Env from '../../api/env';
  import API from '../../api/api_audit';
  import moment from 'moment';

  export default {
    data() {
      return {
        filters: {
          ticketType: 2
        },

        loading: false,
        rows: [],
        //分页数据
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        imgViewVisible: false,
        imgViewUrl: ''
      }
    },
    methods: {
      //显示转换
      formatState: function (row, column) {
        return row.state == 1 ? '通过' : (row.state == 3 ? '不通过' : '其他');
      },
      //时间格式化
      dateFormat: function (row, column) {
        var date = row[column.property];
        if (date == undefined) {
          return "";
        }
        return moment(date).format("YYYY-MM-DD HH:mm:ss");
      },
      //分页方法
      handleSizeChange(val) {
        this.limit = val();
        this.search();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.search();
      },
      //初始化查询mounted后调用
      handleSearch() {
        this.total = 0;
        this.page = 1;
        this.search();
      },
      //获取列表
      search: function () {
        // console.log("search come in.");
        let that = this;
        let params = Object.assign({
          pageNum: that.page,
          pageSize: that.limit
        }, that.filter);
        that.loading = true;

        API.searchList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.total = result.data.total;
            that.rows = result.data.list;

            that.rows.forEach(row => {
              row.url = Env.imageServerUrl + '/images/receipt_audit/' + row.url;
            })
            console.log(JSON.stringify(that.rows));
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
      viewImage: function (url) {
        this.imgViewVisible = true;
        this.imgViewUrl = url;
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
