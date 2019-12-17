<template>
  <div class="app-container calendar-list-container admin_user_info_14s">
    <basic-container>
      <template>
        <el-tabs @tab-click="switchTab">
          <el-tab-pane label='信息管理' name='userManager'/>
          <el-tab-pane label='密码管理' name='passwordManager'/>
        </el-tabs>
      </template>
      <el-row>
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <el-form :model="ruleForm2"
                     :rules="rules2"
                     ref="ruleForm2"
                     label-width="100px"
                     v-if="switchStatus==='userManager'"
                     class="demo-ruleForm">
              <el-form-item label="用户名"
                            prop="username">
                <el-input type="text"
                          v-model="ruleForm2.username"
                          disabled></el-input>
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="ruleForm2.phone" placeholder="请输入手机号"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary"
                           @click="submitForm('ruleForm2')">提交
                </el-button>
                <el-button @click="resetForm('ruleForm2')">重置</el-button>
              </el-form-item>
            </el-form>
            <updatePassword v-if="switchStatus==='passwordManager'" :username="ruleForm2.username"></updatePassword>
          </div>
        </el-col>
      </el-row>
    </basic-container>
  </div>
</template>


<script>
  import {handleDown} from "@/api/admin/user";
  import {mapState} from 'vuex'
  import store from "@/store";
  import request from '@/router/axios'
  import updatePassword from "./updatePassword"
  import { validatePhone } from "@/util/rules"
  export default {
    components: {
      updatePassword
    },
    data() {
      return {
        switchStatus: '',
        avatarUrl: '',
        show: false,
        headers: {
          Authorization: 'Bearer ' + store.getters.access_token
        },
        ruleForm2: {
          username: '',
          avatar: '',
          phone: ''
        },
        rules2: {
          phone: [
            {required: true, message: "手机号不能为空 ", trigger:'blur'},
            {required: true, validator: validatePhone, trigger:'blur'}
            ]
        }
      }
    },
    created() {
      this.ruleForm2.username = this.userInfo.username
      this.ruleForm2.phone = this.userInfo.phone
      this.switchStatus = 'userManager'
    },
    computed: {
      ...mapState({
        userInfo: state => state.user.userInfo
      }),
    },
    methods: {
      switchTab(tab, event) {
        this.switchStatus = tab.name
      },
      submitForm(formName) {
        this.$refs[formName].validate(valid => {
          if (valid) {
            request({
              url: '/admin/user/edit',
              method: 'put',
              data: this.ruleForm2
            }).then(response => {
              if (response.data.data) {
                this.$notify({
                  title: '成功',
                  message: '修改成功',
                  type: 'success',
                  duration: 2000
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
