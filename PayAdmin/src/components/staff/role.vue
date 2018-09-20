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
      <!--列表-->
      <el-table stripe :data="users" highlight-current-row style="width: 100%;">
        <el-table-column type="index" width="40"></el-table-column>
        <el-table-column prop="name" label="角色" width="120" sortable></el-table-column>
        <el-table-column prop="remark" label="备注" sortable></el-table-column>
        <el-table-column fixed="right" width="280" label="操作">
          <template slot-scope="scope">
            <el-button size="small"  @click="showPermissionsDialog(scope.$index, scope.row)">编辑权限</el-button>
            <el-button type="danger" @click="delRole(scope.$index, scope.row)" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="limit"
                       :total="total" style="float:right;">
        </el-pagination>
      </el-col>

      <el-dialog title="编辑权限" :visible.sync="editPermissionsFormVisible" :close-on-click-modal="false">
        <input type="hidden" :value="editId"/>

        <el-tree
          :data="treeData"
          show-checkbox
          default-expand-all
          node-key="id"
          ref="tree"
          highlight-current
          :props="defaultProps">
        </el-tree>

        <div slot="footer" class="dialog-footer">
          <el-button @click.native="editPermissionsFormVisible = false">取消</el-button>
          <el-button type="primary" @click.native="editPermissionsSubmit">提交</el-button>
        </div>
      </el-dialog>

    </el-col>
  </el-row>
</template>
<script>
  import API from '../../api/api_role';
  import {getPermissions} from '@/utils/session';

  export default {
    data() {
      return {
        //过滤相关数据
        loading: false,
        permissionsEditLoading: false,
        users: [],
        total: 0,
        page: 1,
        limit: 10,
        currentPage: 1,

        //编辑相关数据
        editFormVisible: false, //编辑界面是否显示
        editPermissionsFormVisible: false, //权限编辑界面是否显示
        editId: 0,
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    methods: {
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

      handleDefaultPermissions() {
        let that = this;
        let params = {
          pageNum: that.page,
          pageSize: 100
        };
        //获取树结构
        API.permissionsList(params).then(function (result) {
          console.log("获取树结构");
          that.permissionsLoading = false;
          if (result && result.errorCode == 0) {
            let list = result.data.list;
            //深度2
            let data = [];
            //一级菜单
            for (var i in list) {
              let item = list[i];
              if (Number(item.level) == 1) {
                if (item.redirect == null) {
                  delete item.redirect;
                }

                item.children = [];
                data.push(item);
              }
            }
            //二级菜单
            for (var i in list) {
              let item = list[i];
              if (Number(item.level) == 2) {
                let p = {};
                for (let d in data) {
                  let dobj = data[d];
                  if (dobj.id == item.parentId) {
                    p = dobj;
                    break;
                  }
                }
                if (item.redirect == null) {
                  delete item.redirect;
                }
                p.children.push(item)
              }
            }
            that.treeData = data;
          }
        }, function (err) {
          that.$message.error({showClose: true, message: err.toString(), duration: 2000});
        }).catch(function (error) {
          that.$message.error({showClose: true, message: '请求出现异常', duration: 2000});
        });
      },

      //显示编辑界面
      showEditDialog: function (index, row) {
        this.editFormVisible = true;
        console.log(row);
      },

      showPermissionsDialog: function (index, row) {
        this.editPermissionsFormVisible = true;
        this.editId = row.id;

        this.permissionsEditLoading = true;
      },

      editPermissionsSubmit: function () {
        let that = this;
        let para = that.$refs.tree.getCheckedKeys();

        that.loading = true;
        API.permissionsUpdate(that.editId, {'keys': para.toString()}).then(function (result) {
          that.loading = false;

          if (result && parseInt(result.errorCode) == 0) {
            that.$message.success({showClose: true, message: '修改成功', duration: 2000});
            that.editPermissionsFormVisible = false;
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


    },
    mounted() {
      this.handleSearch();
      this.handleDefaultPermissions();
    },
    updated() {
      let that = this;
      //console.log("updated" + that.$refs.tree);
      if (that.permissionsEditLoading == true) {
        //勾中角色的权限
        API.permissionsGet(that.editId, {}).then((result) => {
          console.log(JSON.stringify(result));
          that.$refs.tree.setCheckedNodes(result.data);
        });
        /*let obj = [{
          id: 2
        }];
        that.$refs.tree.setCheckedNodes(result);*/
      }
    }
  }


</script>
<style scoped>

</style>
