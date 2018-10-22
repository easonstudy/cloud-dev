<!--
  礼品设置
-->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分管理</el-breadcrumb-item>
        <el-breadcrumb-item>礼品设置</el-breadcrumb-item>
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
        <!--<el-table-column prop="type" label="类型" sortable>
        </el-table-column>-->
        <el-table-column prop="code" label="编号" sortable>
        </el-table-column>
        <el-table-column prop="name" label="名称" sortable>
        </el-table-column>
        <el-table-column prop="score" label="积分" sortable>
        </el-table-column>
        <el-table-column prop="number" label="数量" sortable>
        </el-table-column>
        <el-table-column prop="expiredTime" :formatter="dateFormat" label="过期时间" sortable>
        </el-table-column>
        <el-table-column prop="isDelete" :formatter="formatState" label="状态" sortable>
        </el-table-column>
        <el-table-column prop="createTime" :formatter="dateFormat" label="创建时间" sortable>
        </el-table-column>
        <el-table-column prop="createBy" label="创建人" sortable>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button type="danger" @click="delRow(scope.$index, scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>
      </el-col>

      <el-dialog :title="editTitle" :visible.sync="editFormVisible" :before-close="editDiaglogClose"
                 :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">
          <el-row>
            <el-col :span="12">
              <el-form-item label="名称" prop="name">
                <el-input v-model="editForm.name" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="编号" prop="code">
                <el-input v-model="editForm.code" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="积分" prop="score">
                <el-input v-model="editForm.score" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="数量" prop="number">
                <el-input v-model="editForm.number" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="图片" prop="url">
                <el-input v-model="editForm.url" readonly auto-complete="off"></el-input>
                <!--action="https://jsonplaceholder.typicode.com/posts/"-->
                <el-upload
                  class="upload-demo"
                  :action="uploadURL"
                  :data="upLoadData"
                  name="fileUpload"
                  :on-error="uploadError"
                  :on-success="uploadSuccess"
                  :before-upload="beforeUpload">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="过期时间" prop="expiredTimeStr">
                <el-date-picker
                  v-model="editForm.expiredTimeStr"
                  type="date"
                  format="yyyy-MM-dd"
                  placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12"></el-col>
          </el-row>

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
  import Env from '../../api/env';
  import API from '../../api/api_gift';
  import moment from 'moment';

  export default {
    data() {
      return {
        loading: false,
        rows: [],
        //分页数据
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //新增 或者编辑
        isNew: true,
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: API.entity,

        uploadURL: Env.imageServerUrl + '/images/upload',
        uploadData: {
          local: 1  //0 服务器,  1:非图片服务器
        }
      }
    },
    methods: {
      //显示转换
      formatState: function (row, column) {
        return row.isDelete ? '1' : '0';
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
      //获取等级列表
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
        this.editFormVisible = true;
        if (row == undefined) {
          this.editTitle = "新增礼品";
          this.isNew = true;
          this.editForm = API.entity;
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
      uploadError: function () {

      },
      uploadSuccess: function (response, file, fileList) {
        console.log('response:', response)
        if (response && response.errorCode == "0") {
          this.editForm.url = response.errorMsg;
        } else {
          this.$message.error({showClose: true, message: response.errorMsg, duration: 2000});
        }
      },
      beforeUpload: function (file) {
        let that = this;
        console.log(file);
        const {name, type, size} = file;
        if (type.indexOf("image") == -1) {
          that.$message.error({showClose: true, message: '文件类型不正确', duration: 2000});
          return !1;
        }
        let sizeKB = size / 1024; //单位KB
        if (sizeKB > 500) {
          that.$message.error({showClose: true, message: '文件大小超出500KB', duration: 2000});
          return !1;
        }
        that.editForm.url = name;
        return !0;

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
