<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>积分管理</el-breadcrumb-item>
        <el-breadcrumb-item>礼品兑换</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>


    <el-col :span="24" class="warp-breadcrum">
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>查找会员</span>
          </div>
          <!-- class="learn-form-inline" -->
          <el-form :inline="true" :model="user">
            <el-form-item label="会员">
              <el-input v-model="user.userUniqueId" placeholder="电话/ID/唯一标记"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSearchSubmit">查找</el-button>
            </el-form-item>
            <br/>
            <el-form-item label="昵称:">
              {{user.nickName}}
              <!--<el-input v-model="user.nickName" disabled></el-input>-->
            </el-form-item>
            <el-form-item label="电话:">
              {{user.phone}}
              <!--<el-input v-model="user.phone" disabled></el-input>-->
            </el-form-item>
            <el-form-item label="积分:">
              {{user.score}}
              <!--<el-input v-model="user.score" disabled></el-input>-->
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>查找礼品</span>
          </div>
          <div>
            <el-col :span="24" class="toolbar" v-loading="loading" style="padding-bottom: 0px;"
                    element-loading-text="拼命加载中">
              <!-- 表单 -->
              <el-form :inline="true" :model="filters">
                <el-form-item>
                  <el-input v-model="filters.filterStr" placeholder="商品(编号/名称)" style="min-width: 240px;"
                            @keyup.enter.native="onFilterSubmit"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="search">查询</el-button>
                </el-form-item>
              </el-form>

              <!--列表-->
              <el-table stripe :data="rows" highlight-current-row style="width: 100%;">
                <el-table-column prop="code" label="商品编号" sortable>
                </el-table-column>
                <el-table-column prop="name" label="商品名称" sortable>
                </el-table-column>
                <el-table-column prop="score" label="所需积分" sortable>
                </el-table-column>
                <el-table-column prop="number" label="剩余数量" sortable>
                </el-table-column>
                <el-table-column fixed="right" label="操作">
                  <template slot-scope="scope">
                    <el-button type="primary" plain size="small" @click="showEditDialog(scope.$index, scope.row)">
                      兑换
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

              <!--兑换礼品-->
              <el-dialog :title="editTitle" :visible.sync="editFormVisible" :before-close="editDiaglogClose"
                         :close-on-click-modal="false">
                <el-form :model="editForm" :rules="editRules" label-width="100px" ref="editForm">

                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="礼品名称:">
                        {{editForm.name}}
                        <!--<el-input v-model="editForm.name" auto-complete="off"></el-input>-->
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="礼品编号:">
                        {{editForm.code}}
                        <!--<el-input v-model="editForm.code" auto-complete="off"></el-input>-->
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="所需积分">
                        {{editForm.score}}
                        <!--<el-input v-model="editForm.score" readonly auto-complete="off"></el-input>-->
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="会员:" prop="userUniqueId">
                        {{editForm.userUniqueId}}
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="可用积分:" prop="userScore">
                        {{editForm.userScore}}
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :span="12">
                      <el-form-item label="兑换数量">
                        <el-input type="number" v-model="editForm.number" auto-complete="off"></el-input>
                      </el-form-item>
                    </el-col>

                    <el-col :span="12">
                      <el-form-item label="总花费积分:">
                        <!--<el-input v-model="editForm.totalScore" readonly auto-complete="off"></el-input>-->
                        {{editForm.number * editForm.score}}
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item>
                    <el-button @click.native="editFormVisible = false">取消</el-button>
                    <el-button type="primary" @click.native="editSubmit">兑换</el-button>
                  </el-form-item>
                </el-form>
              </el-dialog>


            </el-col>

          </div>
        </el-card>
      </el-col>
    </el-col>


  </el-row>
</template>

<script>
  import API from '../../api/api_gift';
  import API_USER from '../../api/api_user';

  export default {
    data() {
      return {
        //会员
        user: {
          userUniqueId: '',
          nickName: '',
          phone: '',
          score: ''
        },

        //列表 - 查询表单
        filters: {
          filterStr: ''
        },

        //列表 - 集合
        rows: [],
        //分页数据
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //编辑相关数据
        editFormVisible: false,//编辑界面是否显示
        editForm: {
          userUniqueId: '',
          userScore: 0,
          id: 0,
          code: '',
          name: '',
          score: 0,
          number: 0
        },

        editRules: {}
      }
    },
    methods: {
      onSearchSubmit: function () {
        let that = this;
        this.user.nickName = '';
        this.user.phone = '';
        this.user.score = '';

        if(that.user.userUniqueId == ''){
          that.$message.error({showClose: true, message: '请输入会员信息', duration: 2000});
          return;
        }

        API_USER.search(that.user.userUniqueId).then(function (result) {
          console.log("search user:" + JSON.stringify(result));
          if (result == undefined || result == null) {
            that.$message.error({showClose: true, message: '未找到该用户', duration: 2000});
          } else {
            that.user.nickName = result.nickName;
            that.user.phone = result.phone;
            that.user.score = result.userBalance != null ? result.userBalance.score : 0;
          }
        }, function (err) {
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },


      handleSizeChange(val) {
        this.limit = val();
        this.search();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.search();
      },
      search: function () {
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: that.limit
        };
        that.loading = true;

        let condition = {filterStr: that.filters.filterStr};
        params = Object.assign(params, condition);

        console.log(JSON.stringify(params));
        API.searchList(params).then(function (result) {
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

      showEditDialog: function (index, row) {
        if (row.number == 0) {
          this.$message({
            showClose: true,
            message: '礼品已兑完',
            type: 'error'
          });
          return;
        }
        if (this.user.phone == '') {
          this.$message({
            showClose: true,
            message: '请先查找会员',
            type: 'error'
          });
          return;
        }
        this.editFormVisible = true;
        this.editTitle = "兑换礼品";
        this.editForm = Object.assign({}, row);
        this.editForm.userUniqueId = this.user.phone;
        this.editForm.userScore = this.user.score;
        this.editForm.number = 1;
      },
      editSubmit: function () {
        let that = this;

        let totalScore = that.editForm.score * that.editForm.number;
        if (totalScore > that.editForm.userScore) {
          that.$message.error({showClose: true, message: '积分不足', duration: 2000});
          return;
        }


        this.$refs.editForm.validate((valid) => {
          if (valid) {
            let params = Object.assign({}, that.editForm);
            API.exchange(params).then(function (result) {
              console.log("result:" + JSON.stringify(result));
              if (result && parseInt(result.errorCode) == 0) {
                that.$message.success({showClose: true, message: "兑换成功", duration: 3000});
                that.editFormVisible = false;
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
          }
        })
      }

    },
    mounted() {

    }
  }

</script>

<style scoped>
  .message {
    font-size: 18
  }
</style>
