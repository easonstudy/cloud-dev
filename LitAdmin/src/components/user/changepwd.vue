<template>
  <el-row class="warp">
    <el-col :span="24" class="warp-breadcrum">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }"><b>首页</b></el-breadcrumb-item>
        <el-breadcrumb-item>设置</el-breadcrumb-item>
        <el-breadcrumb-item>修改密码</el-breadcrumb-item>
      </el-breadcrumb>
    </el-col>

    <el-col :span="24" class="warp-main">
      <el-form ref="form" :rules="rules" :model="form" label-width="120px">
        <el-form-item label="原密码" prop="oldPwd">
          <el-input type="password" v-model="form.oldPwd"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPwd">
          <el-input type="password" v-model="form.newPwd"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPwd">
          <el-input type="password" v-model="form.confirmPwd"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">提交</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>
<script>
  import API from '../../api/api_user';

  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.from.confirmPwd !== '') {
            this.$refs.from.validateField('confirmPwd');
          }
          callback();
        }
      };
      var validateNewPass = (rule, value, callback) => {
        debugger;
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.form.newPwd) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        form: {
          oldPwd: '',
          newPwd: '',
          confirmPwd: ''
        },
        rules: {
          oldPwd: [
            {validator: validatePass, trigger: 'blur'}
          ],
          newPwd: [
            {validator: validatePass, trigger: 'blur'}
          ],
          confirmPwd: [
            {validator: validateNewPass, trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      submitForm(formName) {
        let that = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            that.loading = true;
            let args = {
              oldPwd: that.form.oldPwd,
              newPwd: that.form.newPwd
            };
            API.changePassword(args).then(function (result) {
              that.loading = false;
              if (result && parseInt(result.errorCode) == 0) {
                //修改成功
                that.form.oldPwd = "";
                that.form.confirmPwd = "";
                that.form.newPwd = "";
                that.$message.success({showClose: true, message: '修改成功', duration: 2000});
              } else if (result && parseInt(result.errorCode) == -1) {
                that.$message.error({showClose: true, message: '旧密码不匹配', duration: 2000});
              } else {
                that.$message.error({showClose: true, message: '修改异常', duration: 2000});
              }
            })
          } else {
            console.log('Error submit!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }

    }
  }
</script>
