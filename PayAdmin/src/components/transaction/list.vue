<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分管理</el-breadcrumb-item>
        <el-breadcrumb-item>扫码记录</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">

      <!-- 查询表单 -->
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
        <el-form :inline="true" :model="filters"> <!--size="mini"-->
          <el-form-item>
            <el-button type="primary" @click="search" size="small">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>


      <!--列表-->
      <el-table stripe :data="transactions" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="shopsName" label="店铺名称" sortable></el-table-column>
        <el-table-column prop="goodsName" label="商品" sortable></el-table-column>
        <el-table-column prop="deviceId" label="设备ID" width="160" sortable></el-table-column>
        <el-table-column prop="barcode" label="商品编码" sortable></el-table-column>
        <el-table-column prop="state" label="状态" :formatter="stateFormat" sortable></el-table-column>
        <el-table-column prop="remark" label="备注" sortable></el-table-column>
        <el-table-column prop="grasptingTime" label="抓取时间" :formatter="dateFormat" min-width="120" sortable></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="dateFormat" min-width="120" sortable></el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit" :total="total"  style="float:right;">
        </el-pagination>
      </el-col>

      <el-dialog title="编辑" :visible.sync ="editFormVisible" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">
          <el-form-item label="积分" prop="score">
            <el-input v-model="editForm.score" auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click.native="editFormVisible = false">取消</el-button>
          <el-button type="primary" @click.native="editSubmit">提交</el-button>
        </div>
      </el-dialog>

    </el-col>
  </el-row>
</template>
<script>
  import API from '../../api/api_transaction';
  import moment from 'moment';

  export default {
    data() {
      return {
        //过滤相关数据
        loading: false,
        transactions: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage:1 ,

        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: {
          id: 0,
          score: ''
        }
      }
    },
    methods: {
      //
      stateFormat:function (row, column) {
        let state = row[column.property];
        if(state == 0){
          return '待上传至支付';
        } else if(state == 1){
          return '成功';
        } else {
          return '失败';
        }


      },

      //时间格式化
      dateFormat:function(row, column) {
        var date = row[column.property];
        if (date == undefined) {
          return "";
        }
        return moment(date).format("YYYY-MM-DD HH:mm:ss");
      },
      handleSizeChange(val){
        this.limit = val();
        this.search();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.search();
      },
      handleSearch(){
        this.total = 0;
        this.page = 1;
        this.search();
      },
      //获取上传的列表
      search: function () {
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: that.limit
        };

        that.loading = true;
        API.findList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.total = result.data.total;
            that.transactions = result.data.list;
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

      //显示编辑界面
      showEditDialog: function (index, row) {
        this.editFormVisible = true;
        this.editForm = Object.assign({}, row);
      },
      editSubmit: function () {
        let that = this;
        this.$refs.editForm.validate((valid) =>{
          if (valid) {
            this.loading = true;
            let para = Object.assign({}, this.editForm);
            API.update(para.id, para).then(function (result) {
              that.loading = false;
              if (result && parseInt(result.errorCode) == 0) {
                that.$message.success({showClose: true, message: '修改成功', duration: 2000});
                that.$refs['editForm'].resetFields();
                that.editFormVisible = false;
                that.search();
              } else {
                that.$message.error({showClose: true, message: '修改失败', duration: 2000});
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
        });
      }
    },
    mounted() {
      this.handleSearch()
    }
  }


</script>
<style scoped>

</style>
