<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>消息管理</el-breadcrumb-item>
        <el-breadcrumb-item>消息列表</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">
      <div>
        <el-button type="primary" @click="showEditDialog()" size="small">新增</el-button>
      </div>

      <!--列表-->
      <el-table stripe :data="users" highlight-current-row style="width: 100%;">
        <el-table-column fixed type="index" width="40"></el-table-column>
        <el-table-column prop="msgtype" width="100" label="类型" sortable></el-table-column>
        <el-table-column prop="title" label="标题" sortable></el-table-column>
        <el-table-column prop="description" label="描述" sortable></el-table-column>
        <el-table-column prop="url" label="跳转链接" sortable></el-table-column>
        <el-table-column prop="picurl" label="图片链接" sortable></el-table-column>
        <el-table-column prop="replyKey" label="输入值" sortable></el-table-column>
        <el-table-column prop="content" label="被动回复内容" sortable></el-table-column>
        <el-table-column prop="state" label="状态" :formatter="formatStatus" sortable></el-table-column>
        <el-table-column prop="createTime" label="创建时间" :formatter="dateFormat"
                         sortable></el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">编辑</el-button>
            <el-button v-if="scope.row.state==1" type="success" @click="delMessage(scope.$index, scope.row)"
                       size="small">启用
            </el-button>
            <el-button v-else="scope.row.state==0" type="danger" @click="delMessage(scope.$index, scope.row)"
                       size="small">停用
            </el-button>
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
        <el-form :model="editForm" :rules="editFormRules" label-width="100px" ref="editForm">
          <el-form-item label="消息类型" prop="msgtype">
            <el-select v-model="editForm.msgtype" @change="msytypeChange" placeholder="请选择">
              <el-option
                v-for="item in optionsMsgtype"
                :key="item.value"
                :label="item.label"
                :value="item.value"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>


          <div v-if="editForm.msgtype=='news'">
            <el-form-item label="标题" prop="title">
              <el-input v-model="editForm.title" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input type="textarea" v-model="editForm.description" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="跳转URL" prop="url">
              <el-input v-model="editForm.url" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="图片URL" prop="picurl">
              <el-input v-model="editForm.picurl" auto-complete="off"></el-input>
            </el-form-item>
          </div>

          <div v-if="editForm.msgtype=='reply'">
            <el-form-item label="定义值" prop="replyKey">
              <el-input v-model="editForm.replyKey" auto-complete="off"></el-input>
            </el-form-item>
            <el-form-item label="被动回复内容" prop="content">
              <el-input v-model="editForm.content" auto-complete="off"></el-input>
            </el-form-item>
          </div>

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
  import API from '../../api/api_message';
  import moment from 'moment';

  export default {
    data() {
      return {
        //过滤相关数据
        loading: false,
        users: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //新增或者编辑
        isNew: true,
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editTitle: '',
        editForm: {
          id: 0,
          msgtype: 'news',
          title: '',
          description: '',
          url: '',
          picurl: '',
          replyKey: '',
          content: ''
        },
        editFormRules: {},
        optionsMsgtype: [
          {
            value: 'news',
            label: '图文-文章'
          }, {
            value: 'reply',
            label: '自定义回复'
          }]
      }
    },
    methods: {
      //状态显示转换
      formatStatus: function (row, column) {
        return row.state == 1 ? '停用' : '启用';
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

      //获取列表
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
      delMessage: function (index, row) {
        let that = this;
        let boot = row.state == 1 ? true : false;
        let parmas = {'id': row.id, 'state': !boot ? 1 : 0};

        this.$confirm('确认' + (boot ? '启用' : '停用') + '该消息吗?', '提示', {type: 'warning'}).then(() => {
          that.loading = true;
          API.update(parmas).then(function (result) {
            that.loading = false;
            if (result && parseInt(result.errorCode) == 0) {
              that.$message.success({showClose: true, message: '操作成功', duration: 1500});
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
            msgtype: 'news',
            title: '',
            description: '',
            url: '',
            picurl: '',
            replyKey: '',
            content: ''
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
            let params = account;
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
      msytypeChange(query) {
        if (query !== '') {
          this.editForm.msgtype = query;
        }
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted() {
      this.handleSearch();
    }
  }


</script>
<style scoped>

</style>
