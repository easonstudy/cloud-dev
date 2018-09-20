<!--
  店铺列表
-->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>店铺管理</el-breadcrumb-item>
        <el-breadcrumb-item>店铺列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <!-- 查询表单 -->
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
        <el-form :inline="true" :model="filters"> <!--size="mini"-->
          <el-form-item label="店铺名称">
            <el-input v-model="filters.shopsName"></el-input>
          </el-form-item>
          <el-form-item label="电话">
            <el-input v-model="filters.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="search" size="small">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="showEditDialog()" size="small">新增</el-button>
          </el-form-item>
        </el-form>

      </el-col>

      <!--列表-->
      <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="name" label="店铺名称" sortable>
        </el-table-column>
        <!--<el-table-column prop="deviceIds" :formatter="formatDevice" label="设备" sortable>
        </el-table-column>-->
        <el-table-column prop="deviceIds" width="90" :formatter="formatDevice"  label="设备">
        </el-table-column>
        <el-table-column prop="phone" label="电话" sortable>
        </el-table-column>
        <el-table-column prop="address" label="地址" sortable>
        </el-table-column>
        <el-table-column prop="isDelete" :formatter="formatState" label="状态" sortable>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="160" :formatter="dateFormat"
                         sortable></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" :formatter="dateFormat"
                         sortable></el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button type="danger" @click="delRow(scope.$index, scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>
      </el-col>

      <el-dialog :title="editTitle" :visible.sync="editFormVisible" :before-close="editDiaglogClose"
                 :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">

          <el-row>
            <el-col :span="12">
              <el-form-item label="店铺名称" prop="name">
                <el-input v-model="editForm.name" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12"></el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="电话" prop="level">
                <el-input v-model="editForm.phone" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="地址" prop="level">
                <el-input v-model="editForm.address" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button @click.native="editFormVisible = false">取消</el-button>
            <el-button type="primary" @click.native="editSubmit">提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>


    </el-col>
  </el-row>
</template>

<script>
  import API from '../../api/api_shops';
  import moment from 'moment';

  export default {
    data() {
      return {
        filters: {
          shopsName: null,
          phone: null
        },

        loading: false,
        rows: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //新增 或者编辑
        isNew: true,
        editTitle:'',
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: {
          id: 0,
          name: '',
          level: 0
        },
        options: []
      }
    },
    methods: {
      //显示转换
      formatState: function (row, column) {
        return row.isDelete ? '1' : '0';
      },
      //设备显示格式化
      formatDevice: function (row, column) {
        let devicesIds = row[column.property];
        if(devicesIds != null) {

          //已添加设备
          let arr = devicesIds.split(',');
          devicesIds = '';
          for (let i in arr) {
            devicesIds += arr[i] + "\r\n";
          }
        } else {
          devicesIds = '未配置';
        }
        return devicesIds;
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
      handleSearch() {
        this.total = 0;
        this.page = 1;
        this.search();
      },
      //初始化查询mounted后调用
      handleSearch() {
        this.search();
      },
      init: function () {
        let that = this;


      },
      //获取列表
      search: function () {
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: that.limit
        };
        that.loading = true;
        params = Object.assign(params, that.filters);

        console.log(JSON.stringify(params));
        API.findList(params).then(function (result) {
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
      },
      //删除
      delRow: function (index, row) {
        let that = this;
        this.$confirm('确认删除该记录吗?', '提示', {type: 'warning'}).then(() => {
          that.loading = true;
          API.remove(row.id).then(function (result) {
            that.loading = false;
            if (result && parseInt(result.errorCode) == 0) {
              that.$message.success({showClose: true, message: '删除成功', duration: 1500});
              that.search();
            }
          }, function (err) {
            that.loading = false;
            that.$message.error({showClose: true, message: err.toString(), duration: 2000});
          }).catch(function (error) {
            that.loading = false;
            console.log(error);
            that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
          });
        }).catch(() => {
        });
      },
      //显示编辑界面
      showEditDialog: function (index, row) {
        debugger;
        this.editFormVisible = true;
        if (row == undefined) {
          this.editTitle = "新增";
          this.isNew = true;
          this.editForm = {
            id: 0,
            name: '',
            level: ''
          };
        } else {
          this.editTitle = "编辑";
          this.isNew = false;
          this.editForm = Object.assign({}, row);
        }
      },
      editSubmit: function () {
        let that = this;
        this.$refs.editForm.validate((valid) => {
          if (valid) {
            this.loading = true;
            let para = Object.assign({}, this.editForm);
            console.log("submit data:" + JSON.stringify(para));
            API.update(para).then(function (result) {
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
      },
      //关闭编辑界面前的调用
      editDiaglogClose: function (done) {
        this.$refs['editForm'].resetFields();
        done();
      }
    },
    mounted() {
      this.handleSearch();
      this.init()
    }
  }
</script>

<style scoped>
  .el-table .cell {
    white-space: pre-line;
  }
</style>
