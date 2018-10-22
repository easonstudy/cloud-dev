<template>
  <el-form ref="AccountFrom" :model="account" :rules="rules" label-position="left" label-width="0px"
           class="demo-ruleForm login-container">
    <h3 class="title">管理员登录</h3>
    <!--<el-form-item prop="identity">
      <el-select v-model="account.identity" auto-complete="off" placeholder="请选择身份类型">
        <el-option label="管理员" value="1"></el-option>
        <el-option label="用户"   value="0"></el-option>
      </el-select>
    </el-form-item>-->
    <el-form-item prop="username">
      <el-input type="text" v-model="account.username" @keyup.13="handleLogin" auto-complete="off"
                placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="pwd">
      <el-input type="password" v-model="account.pwd" @keyup.13="handleLogin" auto-complete="off"
                placeholder="密码"></el-input>
    </el-form-item>
    <!--<el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>-->
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @keyup.13="handleLogin" @click.native.prevent="handleLogin"
                 :loading="loading">登录
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import API from '../api/api_user';
  import {setUser, setToken, setRouters, setPermissions} from '@/utils/session'
  import menuData from '@/data.json'
  import MenuUtils from '@/utils/MenuUtils'


  var routers = [];

  export default {
    data() {
      return {
        loading: false,
        account: {
          identity: '1',
          username: 'admin',
          pwd: 'bb123456'
        },
        rules: {
          username: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            //{ validator: validaePass }
          ],
          pwd: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            //{ validator: validaePass2 }
          ]
        },
        checked: true
      };
    },
    methods: {
      compare(obj1, obj2) {
        var val1 = obj1.name;
        var val2 = obj2.name;
        if (val1 < val2) {
          return -1;
        } else if (val1 > val2) {
          return 1;
        } else {
          return 0;
        }
      },

      menu(permissions) {
        let data = [];

        permissions.sort((obj1, obj2) => {
          let r = obj1.level - obj2.level;
          if (r !== 0) {
            return r;
          }

          r = obj1.ordering - obj2.ordering;
          return r;
        });

        permissions.forEach((item) => {
          //console.log("LEVEL:" + item.level + ":" + (item.level == 1));
          if (item.level == 1) {
            if (item.redirect == null) {
              delete item.redirect;
            }
            item.children = [];
            data.push(item);
          } else {
            let p = data.find((dobj) => dobj.id == item.parentId);
            if (item.redirect == null) {
              delete item.redirect;
            }
            if (p == undefined) {
              p = {
                "path": "/",
                "component": "Home",
                "name": "",
                "iconCls": "el-icon-menu",
                "menuShow": true,
                "leaf": true,
                "children": []
              }
              p.name = item.parentId + "_" + item.name;
              p.id = item.parentId;
              data.push(p);
            }
            p.children.push(item);
            console.log("p c:" + JSON.stringify(p));
          }
        })

        /*//一级菜单
        for (var i in permissions) {
          let item = permissions[i];
          if (Number(item.level) == 1) {
            if (item.redirect == null) {
              delete item.redirect;
            }

            item.children = [];
            data.push(item);
          }
        }

        //二级菜单
        for (var i in permissions) {
          let item = permissions[i];
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

            if (JSON.stringify(p) == "{}") {
              p = {
                "path": "/",
                "component": "Home",
                "name": " ",
                "iconCls": "el-icon-menu",
                "menuShow": true,
                "leaf": true,
                "children": []
              }
              p.id = item.parentId;
              data.push(p);
            }
            p.children.push(item);
          }
        }*/

        //404
        let notfound = {
          "path": "/",
          "component": "Home",
          "redirect": "*",
          "menuShow": false,
          "leaf": true,
          "children": [
            {"path": "*", "component": "404", "name": "", "menuShow": false, "leaf": true}
          ]
        }
        data.push(notfound)

        setRouters(JSON.stringify(data));
        MenuUtils(routers, data);

        /*
        测试数据
        console.log("login menu:" + JSON.stringify(menuData.menus));
        setRouters(JSON.stringify(menuData.menus));
        MenuUtils(routers, menuData.menus);
        */
      },
      handleLogin() {
        let that = this;
        this.$refs.AccountFrom.validate((valid) => {
          if (valid) {
            this.loading = true;
            let loginParams = {
              'username': this.account.username,
              'pwd': this.account.pwd,
              'identity': this.account.identity
            };
            API.login(loginParams).then(function (result) {
              that.loading = false;
              if (result.errorCode == '0') {
                setUser(JSON.stringify(result.user));
                setToken(result.token);
                setPermissions(JSON.stringify(result.permissions));
                that.menu(result.permissions);

                console.log(JSON.stringify(routers));
                that.$router.addRoutes(routers);
                that.$router.push({path: '/'});
              } else if (result.errorCode == '-1') {
                that.$message.error({showClose: true, message: result.errmsg || '用户名错误', duration: 2000});
              } else if (result.errorCode == '-2') {
                that.$message.error({showClose: true, message: result.errmsg || '密码错误', duration: 2000});
              } else {
                that.$message.error({showClose: true, message: result.errmsg || '登录失败', duration: 2000});
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
    }
  }
</script>
<style>
  body {
    background: #DFE9FB;
  }
</style>
<style lang="scss" scoped>
  .login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 160px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;

    background: -ms-linear-gradient(top, #ace, #00C1DE); /* IE 10 */
    background: -moz-linear-gradient(top, #ace, #00C1DE); /*火狐*/
    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#ace), to(#00C1DE)); /*谷歌*/
    background: -webkit-linear-gradient(top, #ace, #00C1DE); /*Safari5.1 Chrome 10+*/
    background: -o-linear-gradient(top, #ace, #00C1DE);

  /*Opera 11.10+*/

  .title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .remember {
    margin: 0px 0px 35px 0px;
  }

  }
</style>
