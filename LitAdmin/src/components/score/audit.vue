<!--
  积分审核
-->
<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分审核</el-breadcrumb-item>
        <el-breadcrumb-item>积分审核</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="loading" element-loading-text="拼命加载中">

      <!-- 未审核列表 -->
      <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="phone" label="手机号" sortable>
        </el-table-column>
        <el-table-column prop="url" label="图片地址">
          <template slot-scope="scope">
            <img :src="scope.row.url" @click="viewImage(scope.row.url)" alt=""
                 style="cursor:pointer; width: 50px;height: 50px"/>
          </template>
        </el-table-column>
        <el-table-column prop="userRemark" label="会员备注">
        </el-table-column>
        <el-table-column prop="state" :formatter="formatState" label="状态" sortable>
        </el-table-column>
        <el-table-column prop="createTime" :formatter="dateFormat" label="创建时间" sortable>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="pull(scope.$index, scope.row)">拉取</el-button>
            <!--<el-button size="small" @click="delRow(scope.$index, scope.row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>
      </el-col>
    </el-col>

    <el-col :span="24" class="warp-main" v-loading="wait_loading" element-loading-text="拼命加载中">

      <!-- 待审核列表 -->
      <el-table stripe :data="wait_rows" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="60">
        </el-table-column>
        <el-table-column prop="phone" label="会员名" sortable>
        </el-table-column>
        <el-table-column prop="url" label="图片" style="">
          <template slot-scope="scope">
            <img :src="scope.row.url" @click="viewImage(scope.row.url)" alt=""
                 style="cursor:pointer; width: 50px;height: 50px"/>
          </template>
        </el-table-column>
        <el-table-column prop="userRemark" label="会员备注">
        </el-table-column>
        <el-table-column prop="state" :formatter="formatState" label="状态" sortable>
        </el-table-column>
        <el-table-column prop="createTime" :formatter="dateFormat" label="创建时间" sortable>
        </el-table-column>
        <el-table-column fixed="right" label="操作">
          <template slot-scope="scope">
            <el-button size="small" @click="showEditDialog(scope.$index, scope.row)">审核兑积分</el-button>
            <!--<el-button size="small" @click="delRow(scope.$index, scope.row)">删除</el-button>-->
          </template>
        </el-table-column>
      </el-table>

      <!-- 审核编辑窗口 -->
      <el-dialog :title="editTitle" :visible.sync="editFormVisible" :before-close="editDiaglogClose"
                 :close-on-click-modal="false">
        <el-form :model="editForm" label-width="100px" ref="editForm">
          <el-row>
            <el-col :span="12">
              <el-form-item label="会员电话:" prop="phone">
                {{editForm.phone}}
              </el-form-item>
              <!--<el-form-item label="会员备注:" prop="userRemark">
                {{editForm.userRemark}}
              </el-form-item>-->
              <el-form-item label="小票号:" prop="receiptNum">
                <el-input type="text" v-model="editForm.receiptNum" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="总金额:" prop="amount">
                <el-input type="number" v-model="editForm.amount" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="积分:" prop="score">
                <el-input type="number" v-model="editForm.score" auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="备注:" prop="staffRemark">
                <el-input v-model="editForm.staffRemark" auto-complete="off"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="1"></el-col>
            <el-col :span="10">
              <el-card :body-style="{ padding: '0px' }">
                <img :src="editForm.url" height="200px" class="image">
                <!--<img src="http://element-cn.eleme.io/static/hamburger.50e4091.png" height="200px" class="image">-->
                <div style="padding: 14px;">
                  <div class="bottom clearfix">
                    <time class="time">{{ currentDate }}</time>
                    <el-button type="text" @click="viewImage(editForm.url)" class="button">查看大图</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="1"></el-col>
          </el-row>

          <el-form-item>
            <el-button @click.native="editFormVisible = false">取消</el-button>
            <el-button type="primary" @click.native="editSubmit(1)">通过</el-button>
            <el-button type="danger" @click.native="editSubmit(3)">不通过</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>

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
  import {getUser} from '@/utils/session'
  import moment from 'moment';

  export default {
    data() {
      return {
        loginUser: {},

        loading: false,
        rows: [],
        //分页数据
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        wait_loading: false,
        wait_rows: [],


        //新增 或者编辑
        isNew: true,
        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: API.entity,

        imgViewVisible: false,
        imgViewUrl: ''
      }
    },
    methods: {
      //显示转换
      formatState: function (row, column) {
        return row.state == 0 ? '未审核' : (row.state == 2 ? '待审核' : '其他');
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
      searchTask() {
        let that = this;
        that.search();

        setInterval(function () {
          that.search()
        }, 1000 * 30);
      },
      //获取列表
      search: function () {
        // console.log("search come in.");
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: that.limit,
          state: 0
        };
        that.loading = true;
        API.findList(params).then(function (result) {
          that.loading = false;
          if (result && result.data) {
            that.total = result.data.total;
            that.rows = result.data.list;

            that.rows.forEach(row => {
              row.url = Env.imageAuditUrl + row.url;
            })

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
      //拉取
      pull: function (index, row) {
        let that = this;
        that.loading = true;

        let params = {
          id: row.id
        }

        API.pull(params).then(function (result) {
          that.loading = false;
          if (result && parseInt(result.errorCode) == 0) {
            that.search();
            that.wait_rows.push(row);
          } else {
            that.$message.error({showClose: true, message: "已被处理", duration: 2000});
            let i = that.rows.findIndex((m) => m.id == row.id);
            if (i != -1) {
              that.rows.splice(i, 1);
            }
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

      },
      //获取列表
      searchWaitAudit: function () {
        let that = this;
        let params = {
          state: 2
        };
        that.wait_loading = true;
        API.findList(params).then(function (result) {
          that.wait_loading = false;
          if (result && result.data) {
            that.wait_rows = result.data;
            that.wait_rows.forEach(row => {
              row.url = Env.imageAuditUrl + row.url;
            })
          }
        }, function (err) {
          that.wait_loading = false;
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.wait_loading = false;
          console.log(error);
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },
      //显示编辑界面
      showEditDialog: function (index, row) {
        this.editFormVisible = true;
        this.editTitle = "审核-兑换积分";
        this.isNew = false;
        //, {'url': Env.imageServerUrl + row.url
        this.editForm = Object.assign({}, row);
      },
      editSubmit: function (value) {
        let that = this;
        this.$refs.editForm.validate((valid) => {
          if (valid) {
            let params = Object.assign({}, this.editForm, {'state': value});
            console.log("submit data:" + value + ":" + JSON.stringify(params));
            this.wait_loading = true;
            API.audit(params).then(function (result) {
              that.wait_loading = false;
              if (result && parseInt(result.errorCode) == 0) {
                that.$message.success({showClose: true, message: '成功', duration: 2000});
                that.$refs['editForm'].resetFields();
                that.editFormVisible = false;
                that.searchWaitAudit();
              } else {
                that.$message.error({showClose: true, message: '失败', duration: 2000});
              }
            }, function (err) {
              that.wait_loading = false;
              that.$message.error({showClose: true, message: err.toString(), duration: 2000});
            }).catch(function (error) {
              that.wait_loading = false;
              console.log(error);
              that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
            });
          }
        });
      },
      viewImage: function (url) {
        this.imgViewVisible = true;
        this.imgViewUrl = url;
      }
    },
    mounted() {
      let user = getUser();
      if (user) {
        this.loginUser = JSON.parse(user);
      }
      this.searchTask();
      this.searchWaitAudit();
    }
  }

</script>

<style scoped>
  .message {
    font-size: 18
  }
</style>
