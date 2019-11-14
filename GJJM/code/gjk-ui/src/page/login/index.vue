<template>
  <div class="login-container"
       @keyup.enter.native="handleLogin">
    <div class="login-weaper  animated bounceInDown">
      <div class="login-left"></div>
      <div class=" login-border">
          <div class="fl_14s w100_14s login_top_14s">
            <img class="img"
                src="/img/theme/night/logo/logo_14s.png"
                alt="">
            <span>中国电子科技集团第十四研究所  </span>  
          </div>
          <div class="fl_14s login-main">
            <p class="title">{{website.infoTitle}}</p>
            <userLogin v-if="activeName==='user'"></userLogin>
          </div>
          <div class="fl_14s w100_14s login_footer_14s">
          <p>{{website.inforbusName}} {{website.startTime}} {{website.versionNum}}</p>
          </div>
      </div>
    </div>
  </div>
</template>
<script>
  import '@/styles/login.scss'
  import userLogin from "./userlogin";
  import {mapGetters} from "vuex";
  import {getStore, setStore} from "@/util/store";
  import {dateFormat} from "@/util/date";
  import {validatenull} from "@/util/validate";

  export default {
    name: "login",
    components: {
      userLogin
    },
    data() {
      return {
        activeName: "user"
      };
    },
    watch: {},
    created() {
    },
    mounted() {
    },
    computed: {
      ...mapGetters(["website"])
    },
    props: [],
    methods: {}
  };
</script>

