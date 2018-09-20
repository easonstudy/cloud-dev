<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <!---->
      <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
        <el-form :inline="true" :model="filters">
          <el-form-item>
            <el-input v-model="filters.nickName" placeholder="用户名/姓名/昵称" style="min-width: 240px;"
                      @keyup.enter.native="handleSearch"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <!--列表-->
      <el-table stripe :data="users" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <!--<el-table-column prop="openId" label="OpenId" width="275" sortable>
        </el-table-column>-->
        <el-table-column prop="nickName" label="昵称" width="100" sortable>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="120" sortable>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="100" sortable>
        </el-table-column>
        <el-table-column prop="score" label="积分" sortable>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" :formatter="dateFormat" min-width="120" sortable>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button v-if="permissions.edit" size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button v-if="permissions.delete" type="danger" @click="delUser(scope.$index, scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>

        <!--<el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-size="limit"
          layout="prev, pager, next, jumper"
          :total="total">
        </el-pagination>-->
      </el-col>

      <el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="editForm.nickName" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-input v-model="editForm.phone" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="简介" prop="email">
            <el-input v-model="editForm.email" auto-complete="off"></el-input>
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
  import API from '../../api/api_user';
  import {getPermissions} from '@/utils/session';
  import moment from 'moment';

  export default {
    data() {
      return {
        //详细权限
        permissions: {edit:true, delete:true},
        //过滤相关数据
        filters: {
          nickName: ''
        },
        loading: false,
        users: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: {
          id: 0,
          nickName: '',
          phone: '',
          email: ''
        }
      }
    },
    methods: {
      //性别显示转换
      formatSex: function (row, column) {
        return row.sex == 1 ? '男' : row.sex == 0 ? '女' : '未知';
      },
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
      //获取用户列表
      search: function () {
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: that.limit,
          nickName: that.filters.nickName.trim()
        };

        that.loading = true;
        API.findList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.total = result.data.total;
            that.users = result.data.list;
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
      delUser: function (index, row) {
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
        this.editFormVisible = true;
        this.editForm = Object.assign({}, row);
      },
      editSubmit: function () {
        let that = this;
        this.$refs.editForm.validate((valid) => {
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
      },
      handlePermission() {
        let that = this;
        let obj = this.findRolePermissions(that.$route.path, JSON.parse(getPermissions()));
        //console.log("obj :" + JSON.stringify(obj));
        if (obj != undefined) {
         if (obj.details != undefined && obj.details != "") {
           obj.details.split(',').forEach((p) => that.permissions[p] = true);
          }
          console.log("permissions :" + JSON.stringify(that.permissions));
        } else {
          console.log("Not find current permissions details :");
        }
      },
      findRolePermissions(currentPath, arr) {
        let that = this;
        return arr.find((item) => {
          if (item.path == currentPath) {
            //console.log("Find current permissions details :");
            return item;
          }
          if (item.children) {
            that.findRolePermissions(currentPath, item.children);
          }
        })
      }
    },
    mounted() {
      this.handleSearch();
      //this.handlePermission();
    }
  }
</script>

<style scoped>

</style>
