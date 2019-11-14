<template>
  <div>
    <div>
      <el-form
        class="login-form"
        status-icon
        :rules="loginRules"
        ref="loginForm"
        :model="loginForm"
        label-width="0"
      >
        <el-form-item prop="username">
          <el-input
            size="small"
            @keyup.enter.native="handleLogin"
            v-model="loginForm.username"
            auto-complete="off"
            placeholder="请输入用户名"
          >
            <i slot="prefix" class="icon-yonghu"></i>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            size="small"
            @keyup.enter.native="handleLogin"
            :type="passwordType"
            v-model="loginForm.password"
            auto-complete="off"
            placeholder="请输入密码"
          >
            <i class="el-icon-view el-input__icon" slot="suffix" @click="showPassword"></i>
            <i slot="prefix" class="icon-mima"></i>
          </el-input>
        </el-form-item>
        <!-- <el-form-item prop="code">
      <el-row :span="24">
        <el-col :span="16">
          <el-input size="small"
                    @keyup.enter.native="handleLogin"
                    :maxlength="code.len"
                    v-model="loginForm.code"
                    auto-complete="off"
                    placeholder="请输入验证码">
            <i slot="prefix"
               class="icon-yanzhengma"></i>
          </el-input>
        </el-col>
        <el-col :span="8">
          <div class="login-code">
            <span class="login-code-img"
                  @click="refreshCode"
                  v-if="code.type == 'text'">{{code.value}}</span>
            <img :src="code.src"
                 class="login-code-img"
                 @click="refreshCode"
                 v-else/>
          </div>
        </el-col>
      </el-row>

        </el-form-item>-->
        <!---------@click.native.prevent="handleLogin"---------->
        <el-form-item>
          <el-button
            type="primary"
            size="small"
            @click.native.prevent="handleLogin"
            class="login-submit"
          >登录</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <span class="fl_14s w100_14s text_align_right_14s reg_a_14s" @click="isRegistered=true"><a href="###">立即注册</a></span>

      <el-dialog class="dialog_reg " title="注册用户" :visible.sync="isRegistered" v-if="isRegistered" :modal-append-to-body="false">
        <el-form :model="form" status-icon :rules="rules" ref="form" label-width="100px">
          <el-form-item label="工号" prop="username">
            <el-input v-model="form.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="form.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPassword">
            <el-input type="password" v-model="form.checkPassword" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="所属部门" prop="deptId">
            <avue-input
              v-model="form.deptId"
              type="tree"
              placeholder="请选择所属部门"
              :dic="depts"
              :props="defaultProps"
            ></avue-input>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="form.phone" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="registered()">确 定</el-button>
          <el-button @click="cancel()">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { randomLenNum } from "@/util/util";
import { registered } from "@/api/admin/user";
import { fetchTree } from "@/api/admin/dept";
import { getDetails } from "@/api/admin/user";
import { mapGetters } from "vuex";
export default {
  name: "userlogin",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.form.checkPassword !== "") {
          this.$refs.form.validateField("checkPassword");
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    var validateUsername = (rule, value, callback) => {
      getDetails(value).then(response => {
        if (window.boxType === "edit") callback();
        let result = response.data.data;
        if (result !== null) {
          callback(new Error("用户名已经存在"));
        } else {
          callback();
        }
      });
    };
    return {
      defaultProps: {
        label: "name",
        value: "id"
      },
      depts: [],
      isRegistered: false,
      form: {
        username: "",
        password: "",
        checkPassword: "",
        name: "",
        deptId: undefined,
        phone: "",
        role: [18],
        lockFlag: "0"
      },
      loginForm: {
        username: "admin",
        password: "123456",
        code: "",
        redomStr: ""
      },
      checked: false,
      code: {
        src: "/code",
        value: "",
        len: 4,
        type: "image"
      },
      loginRules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" }
        ],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 4, max: 4, message: "验证码长度为4位", trigger: "blur" }
        ]
      },
      rules: {
        password: [
          { validator: validatePass, trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" }
        ],
        checkPassword: [
          { validator: validatePass2, trigger: "blur" },
          { min: 6, message: "密码长度最少为6位", trigger: "blur" }
        ],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 6, message: "工号最少为6位", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" }
        ],
        deptId: [{ required: true, message: "请选择部门", trigger: "blur" }],
        phone: [
          { min: 11, max: 20, message: "长度在 11 个字符", trigger: "blur" },
          { required: true, message: "请输入手机号", trigger: "blur" }
        ],
        name: [{ message: "请输入姓名", trigger: "blur" }]
      },
      passwordType: "password"
    };
  },
  created() {
    this.refreshCode();
    fetchTree().then(response => {
      this.depts = response.data.data;
    });
  },
  mounted() {},
  computed: {
    ...mapGetters(["tagWel"])
  },
  props: [],
  methods: {
    refreshCode() {
      this.loginForm.code = "";
      this.loginForm.randomStr = randomLenNum(this.code.len, true);
      this.code.type === "text"
        ? (this.code.value = randomLenNum(this.code.len))
        : (this.code.src = `${this.codeUrl}?randomStr=${this.loginForm.randomStr}`);
    },
    showPassword() {
      this.passwordType == ""
        ? (this.passwordType = "password")
        : (this.passwordType = "");
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.$store
            .dispatch("LoginByUsername", this.loginForm)
            .then(() => {
              this.$router.push({ path: this.tagWel.value });
            })
            .catch(() => {
              this.refreshCode();
            });
        }
      });
    },
    cancel() {
      this.form = {
        username: "",
        password: "",
        checkPassword: "",
        name: "",
        deptId: undefined,
        phone: "",
        role: [18],
        lockFlag: "0"
      };
      this.isRegistered = false;
    },

    registered() {
      this.$refs.form.validate(valid => {
        if (valid) {
          registered(this.form).then(response => {
            this.form = {
              username: "",
              password: "",
              checkPassword: "",
              name: "",
              deptId: undefined,
              phone: "",
              role: [18],
              lockFlag: "0"
            };
            this.isRegistered = false;
            if (response.data.data) {
              this.$confirm("注册成功!请登录", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(() => {});
            } else {
              this.$confirm("注册失败!请重新注册", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(() => {});
            }
          });
        }
      });
    }
  }
};
</script>

<style>

</style>
