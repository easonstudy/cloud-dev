<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item>管理员列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <div>
        <el-button type="primary" @click="showEditDialog()" size="small">新增</el-button>
      </div>

      <!--列表-->
      <el-table stripe :data="users" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="40"></el-table-column>
        <el-table-column prop="userName" label="用户名" width="120" sortable></el-table-column>
        <el-table-column prop="roleId" label="角色" width="80" sortable></el-table-column>
        <el-table-column prop="nickName" label="昵称" width="120" sortable></el-table-column>
        <el-table-column prop="email" label="邮箱" width="120" sortable></el-table-column>
        <el-table-column prop="isDelete" label="状态" width="80" :formatter="formatStatus" sortable></el-table-column>
        <el-table-column prop="loginTime" label="登入时间" width="160" :formatter="dateFormat"
                         sortable></el-table-column>
        <el-table-column prop="loginIp" label="登入IP" sortable></el-table-column>
        <!--<el-table-column prop="lastLoginTime" label="上次登入时间" :formatter="dateFormat" min-width="120"
                         sortable></el-table-column>-->
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button size="small" @click="showPasswordDialog(scope.$index, scope.row)">修改密码</el-button>

            <el-button v-if="scope.row.isDelete" type="success" @click="delStaff(scope.$index, scope.row)" size="small">启用</el-button>
            <el-button v-else="!scope.row.isDelete" type="danger" @click="delStaff(scope.$index, scope.row)" size="small">停用</el-button>
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
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="editForm.userName" auto-complete="off" :disabled="!isNew"></el-input>
          </el-form-item>

          <el-form-item v-if="isNew" label="密码" prop="password">
            <el-input type="password" v-model="editForm.password" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="isNew" label="重复密码" prop="repeatPassword">
            <el-input type="password" v-model="editForm.repeatPassword" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="选择角色" prop="roleId">
            <el-select v-model="editForm.roleId" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="editForm.nickName" auto-complete="off"></el-input>
          </el-form-item>
          <!--<el-form-item label="电话" prop="phone">
            <el-input v-model="editForm.phone" auto-complete="off"></el-input>
          </el-form-item>-->
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="editForm.email" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click.native="editFormVisible = false">取消</el-button>
            <el-button type="primary" @click.native="editSubmit">提交</el-button>
            <el-button v-if="isNew" @click="resetForm('editForm')">重置</el-button>
          </el-form-item>
        </el-form>
        <!--<div slot="footer" class="dialog-footer">
          <el-button @click.native="editFormVisible = false">取消</el-button>
          <el-button type="primary" @click.native="editSubmit">提交</el-button>
          <el-button v-if="isNew" @click="resetForm('editForm')">重置</el-button>
        </div>-->
      </el-dialog>

      <el-dialog title="修改密码" :visible.sync="editPasswordFormVisible" :close-on-click-modal="false">
        <el-form :model="editPasswordForm" :rules="editPasswordRules" label-width="100px" ref="editPasswordForm">
          <el-form-item label="用户名" prop="userName">
            <el-input v-model="editPasswordForm.userName" auto-complete="off" disabled></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="editPasswordForm.password" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="重复密码" prop="repeatPassword">
            <el-input type="password" v-model="editPasswordForm.repeatPassword" auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button @click.native="editPasswordFormVisible = false">取消</el-button>
            <el-button type="primary" @click.native="editPasswordSubmit">提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

    </el-col>
  </el-row>
</template>
<script>
  import API from '../../api/api_staff';
  import API_ROLE from '../../api/api_role';
  import moment from 'moment';

  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.editPasswordForm.repeatPassword !== '') {
            this.$refs.editPasswordForm.validateField('repeatPassword');
          }
          callback();
        }
      };
      var validateNewPass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.editPasswordForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        //过滤相关数据
        loading: false,
        users: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //新增 或者编辑
        isNew: true,
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editTitle: '',
        editForm: {
          id: 0,
          userName: '',
          password: '',
          repeatPassword: '',
          roleId: 1,
          nickName: '',
          phone: '',
          email: ''
        },
        editPasswordFormVisible: false, //编辑密码界面,
        editPasswordForm: {
          id: 0,
          userName: '',
          password: '',
          repeatPassword: ''
        },
        editPasswordRules: {
          password: [
            {validator: validatePass, trigger: 'blur'}
          ],
          repeatPassword: [
            {validator: validateNewPass, trigger: 'blur'}
          ]
        },
        options: []
      }
    },
    methods: {
      //状态显示转换
      formatStatus: function (row, column) {
        return row.isDelete ? '停用' : '启用';
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
      //初始角色集合
      handleOptions() {
        let that = this;
        let params = {
          pageNum: 1,
          pageSize: 100
        };
        API_ROLE.findList(params).then(function (result) {
          if (result && result.data) {
            let rolelist = result.data.list;
            let opts = [];
            rolelist.forEach((item) => {
              let r = {};
              ({id: r.value, name: r.label} = item);
              opts.push(r);
            });
            that.options = opts;
          }
        }, function (err) {
        }).catch(function (error) {
          console.log("加载角色列表异常")
        });
      },
      //获取用户列表
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

      //关闭编辑界面前的调用
      editDiaglogClose: function (done) {
        this.$refs['editForm'].resetFields();
        done();
      },

      //删除
      delStaff: function (index, row) {
        let that = this;
        let boot = row.isDelete ? true : false;
        let parmas = {'id': row.id, 'isDelete': !boot};

        this.$confirm('确认' + (boot ? '启用' : '停用') + '该管理员吗?', '提示', {type: 'warning'}).then(() => {
          that.loading = true;
          API.update(parmas).then(function (result) {
            that.loading = false;
            if (result && parseInt(result.errorCode) == 0) {
              that.$message.success({showClose: true, message: '更新成功', duration: 1500});
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
        if (row == undefined) {
          this.editTitle = "新增";
          this.isNew = true;
          this.editForm = {
            id: 0,
            userName: '',
            password: '',
            repeatPassword: '',
            roleId: 1,
            nickName: '',
            phone: '',
            email: ''
          }
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
            let account = Object.assign({}, this.editForm);
            let params;
            if (!that.isNew) {
              params = {id: account.id, nickName: account.nickName, email: account.email, roleId: account.roleId};
            } else {
              params = account;
            }
            //console.log("param:" + JSON.stringify(params));
            API.update(params).then(function (result) {
              that.loading = false;
              if (result && parseInt(result.errorCode) == 0) {
                that.$message.success({showClose: true, message: '操作成功', duration: 2000});
                that.$refs['editForm'].resetFields();
                that.editFormVisible = false;
                that.search();
              } else {
                that.$message.error({showClose: true, message: '操作失败', duration: 2000});
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

      showPasswordDialog: function (index, row) {
        this.editPasswordFormVisible = true;
        this.editPasswordForm.id = row.id;
        this.editPasswordForm.userName = row.userName;
      },
      editPasswordSubmit: function () {
        let that = this;
        this.$refs.editPasswordForm.validate((valid) => {
          if (valid) {

            let params = Object.assign({}, this.editPasswordForm);
            API.changePassword(params).then(function (result) {
              that.loading = false;
              if (result && parseInt(result.errorCode) == 0) {
                that.$message.success({showClose: true, message: '修改成功', duration: 2000});
                that.$refs['editPasswordForm'].resetFields();
                that.editPasswordFormVisible = false;
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
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted() {
      this.handleOptions();
      this.handleSearch();
    }
  }


</script>
<style scoped>

</style>
