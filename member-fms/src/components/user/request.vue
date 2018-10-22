<template>
  <!-- 申请测试 -->
  <el-form ref="AccountFrom" :model="account" :rules="rules" label-position="top" label-width="80px"
           class="demo-ruleForm login-container">
    <h4 style="margin-left: 70px;">申请测试</h4>
    <el-form-item label="电话" prop="phone">
      <el-input v-model="account.phone"></el-input>
    </el-form-item>
    <el-form-item label="尊称" prop="nickName">
      <el-input v-model="account.nickName"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="account.email"></el-input>
    </el-form-item>
    <el-form-item label="" prop="sex">
      <el-radio-group v-model="account.sex">
        <el-radio :label="1">先生</el-radio>
        <el-radio :label="0">女士</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">申请</el-button>
      <el-button @click="resetForm('AccountFrom')">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import API from '../../api/api_user';

  export default {
    data() {
      return {
        key: '',
        loading: false,
        account: {
          nickName: '',
          sex: 1,
          phone: '',
          email: ''
        },
        rules: {
          nickName: [
            {required: false, message: '请输入昵称称', trigger: 'blur'},
            {min: 1, message: '长度在 1 个字符以上', trigger: 'blur'}
          ],
          phone: [
            {required: true, message: '请输入电话', trigger: 'blur'},
            {
              validator: function (rule, value, callback) {
                if (value.length >= 1 && /^1[34578]\d{9}|\s+$/.test(value) == false) {
                  callback(new Error("请输入正确的手机号"));
                } else {
                  callback();
                }
              }, trigger: 'blur'
            }
          ],
          email: [
            {required: false, message: '请输入邮箱', trigger: 'blur'},
            {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change'}
          ]
        },
        checked: true
      };
    },
    methods: {
      submitForm() {
        let that = this;
        this.$refs.AccountFrom.validate((valid) => {
          if (valid) {
            this.loading = true;
            let params = Object.assign({userKey: this.key}, this.account);
            //console.log(JSON.stringify(params));
            API.request(params).then(function (result) {
              that.loading = false;
              if (result.errorCode == '0') {
                that.resetForm('AccountFrom');
                that.$message({showClose: true, message: result.msg || '申请提交成功', type: 'success', duration: 2000});
              } else {
                that.$message.error({showClose: true, message: result.msg || '申请提交失败', duration: 2000});
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
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted() {
      let params = this.$router.history.current.params.key
      //console.log("key:" + params);
      this.key = params;
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
    /*margin: 100px auto;
    width: 300px;*/
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
