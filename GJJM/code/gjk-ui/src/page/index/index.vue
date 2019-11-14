<template>
  <div class="avue-contail"
       :class="{'avue--collapse':isCollapse}">
      <!-- 顶部导航栏 -->
    <div class="avue-header">
      <top/>
    </div>

    <div class="avue-layout">
      <div class="avue-left">
        <!-- 左侧导航栏 -->
        <sidebar/>
      </div>
      <div class="avue-main">
        <!-- 顶部标签卡 -->
        <tags v-on:ecrollbarparent="updateHeight"/>
        <!-- 主体视图层 -->
        <el-scrollbar :style="{'height':height}">
          <keep-alive>
            <router-view class="avue-view"
                         v-if="$route.meta.$keepAlive"/>
          </keep-alive>
          <router-view class="avue-view"
                       v-if="!$route.meta.$keepAlive"/>
        </el-scrollbar>
         <!-- 底部控制台 -->
        <gjkConsole  
          :style="{'display':display,'height':consoleHeight}" 
          v-on:updateconsole="updateConsole" 
          v-on:fullscreen="fullScreen" 
          v-on:ecrollbarparentc="updateHeight"
        />
      </div>
    </div>
    <div class="avue-shade"
         @click="showCollapse"></div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import tags from './tags'
  import gjkConsole from './gjkConsole'
  import top from './top/'
  import sidebar from './sidebar/'
  import admin from '@/util/admin';
  import {validatenull} from '@/util/validate';
  import {calcDate} from '@/util/date.js';
  import {getStore} from '@/util/store.js';

  export default {
    components: {
      top,
      tags,
      sidebar,
      gjkConsole
    },
    name: 'index',
    data() {
      return {
        //刷新token锁
        refreshLock: false,
        //刷新token的时间
        refreshTime: '',
        height : '100%',
        display : 'none',
        consoleHeight : '30%'
      }
    },
    created() {
      //实时检测刷新token
      this.refreshToken()
    },
    destroyed() {
      clearInterval(this.refreshTime)
    },
    mounted() {
      this.init()
    },
    computed: mapGetters(['userInfo', 'isLock', 'isCollapse', 'website', 'expires_in']),
    props: [],
    methods: {
      showCollapse() {
        this.$store.commit("SET_COLLAPSE")
      },
      // 屏幕检测
      init() {
        this.$store.commit('SET_SCREEN', admin.getScreen())
        window.onresize = () => {
          setTimeout(() => {
            this.$store.commit('SET_SCREEN', admin.getScreen())
          }, 0);
        }
      },
      // 实时检测刷新token
      refreshToken() {
        this.refreshTime = setInterval(() => {
          const token = getStore({
            name: 'access_token',
            debug: true,
          });

          if (validatenull(token)) {
            return;
          }

          if (this.expires_in <= 1000 && !this.refreshLock) {
            this.refreshLock = true
            this.$store
              .dispatch('RefreshToken')
              .catch(() => {
                clearInterval(this.refreshTime)
              });
            this.refreshLock = false
          }
          this.$store.commit("SET_EXPIRES_IN", this.expires_in - 10);
        }, 10000);
      },
      //修改高度
      updateHeight(data){
        this.height=data;
        this.display = "";
      },
      updateConsole(data){
        this.display = data;
        this.height="100%";
      },
      fullScreen(data){
        if(data=="100%"){
          this.consoleHeight = data;
          this.height = "0%";
        }else{
          this.consoleHeight = "30%";
          this.height = "70%";
        }
        
      }
    }
  }
</script>