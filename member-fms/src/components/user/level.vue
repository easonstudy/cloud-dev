<!--
  用户等级
-->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户等级</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <div>
        <el-button type="primary" @click="showEditDialog()" size="small">新增</el-button>
      </div>

      <!--列表-->
      <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="name" label="名称" sortable>
        </el-table-column>
        <el-table-column prop="level" label="等级" sortable>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button type="danger" @click="delRow(scope.$index, scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog :title="editTitle" :visible.sync="editFormVisible" :before-close="editDiaglogClose"
                 :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">
          <el-form-item label="名称" prop="name">
            <el-input v-model="editForm.name" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item label="等级" prop="level">
            <el-input v-model="editForm.level" auto-complete="off"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button @click.native="editFormVisible = false">取消</el-button>
            <el-button type="primary" @click.native="editSubmit">提交</el-button>
            <el-button v-if="isNew" @click="resetForm('editForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>


    </el-col>
  </el-row>
</template>

<script>
  import API from '../../api/api_level';

  export default {
    data() {
      return {
        loading: false,
        rows: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //新增 或者编辑
        isNew: true,
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: {
          id: 0,
          name: '',
          level: 0
        }
      }
    },
    methods: {
      handleSearch() {
        this.search();
      },
      //获取等级列表
      search: function () {
        let that = this;
        let params = {};
        that.loading = true;
        API.findList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.rows = result.data;
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
          debugger;
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
      }
    },
    mounted() {
      this.handleSearch();
    }
  }
</script>

<style scoped>

</style>
