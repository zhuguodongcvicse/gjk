<template>
    <el-form :model="ruleForm1"
                     :rules="rules1"
                     ref="ruleForm1"
                     label-width="100px"
                     class="demo-ruleForm">
              <el-form-item label="原密码"
                            prop="password">
                <el-input type="password"
                          v-model="ruleForm1.password"
                          auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="新密码"
                            prop="newpassword1">
                <el-input type="password"
                          v-model="ruleForm1.newpassword1"
                          auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item label="确认密码"
                            prop="newpassword2">
                <el-input type="password"
                          v-model="ruleForm1.newpassword2"
                          auto-complete="off"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary"
                           @click="submitForm('ruleForm1')">提交
                </el-button>
                <el-button @click="resetForm('ruleForm1')">重置</el-button>
              </el-form-item>
            </el-form>
</template>


<script>
  import {handleDown} from "@/api/admin/user";
  import {mapState} from 'vuex'
  import store from "@/store";
  import request from '@/router/axios'
  import { validatePhone } from "@/util/rules"
  export default {
    props: [
      "username"
    ],
    data() {
      var validatePass = (rule, value, callback) => {
        console.log("==================")
        if (this.ruleForm1.password !== '') {
          console.log(value !== this.ruleForm1.newpassword1)
          if (value !== this.ruleForm1.newpassword1) {
            callback(new Error('两次输入密码不一致!'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      }
      return {
        switchStatus: '',
        avatarUrl: '',
        show: false,
        headers: {
          Authorization: 'Bearer ' + store.getters.access_token
        },
        ruleForm1:{
          username: '',
          password: '',
          newpassword1: '',
          newpassword2: '',
        },
        rules1:{
          password: [{required: true, min: 6, message: '原密码不能为空且不少于6位', trigger: 'change'}],
          newpassword1: [{required: true, min: 6, message: '不少于6位', trigger: 'change'}],
          newpassword2: [{required: true, validator: validatePass, trigger: 'blur'}],
        }
      }
      
    },   
    computed: {
      ...mapState({
        userInfo: state => state.user.userInfo
      }),
    },
    methods: {
      submitForm(formName) {
        this.ruleForm1.username = this.username
        this.$refs[formName].validate(valid => {
          if (valid) {
            request({
              url: '/admin/user/edit',
              method: 'put',
              data: this.ruleForm1
            }).then(response => {
              if (response.data.data) {
                this.$notify({
                  title: '成功',
                  message: '修改成功',
                  type: 'success',
                  duration: 2000
                })
                // 修改密码之后强制重新登录
                  this.$store.dispatch('LogOut').then(() => {
                    location.reload() // 为了重新实例化vue-router对象 避免bug
                  })
              } else {
                this.$notify({
                  title: '失败',
                  message: response.data.msg,
                  type: 'error',
                  duration: 2000
                })
              }
            }).catch(() => {
              this.$notify({
                title: '失败',
                message: '修改失败',
                type: 'error',
                duration: 2000
              })
            })
          } else {
            return false
          }
        })
      },
      resetForm(formName) {
        this.$refs[formName].resetFields()
      }
    }
  }
</script>
<style>
  
</style>
