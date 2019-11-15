<!--  -->
<template>
  <div  style="margin-top:10px">
    <div >
      <div style="margin:20px 0px 0px 20px">
        <!-- <div class="textedits_btn_14s">
          <el-button
            type="primary"
            icon="el-icon-thirdsave"
            size="mini"
            @click.native="save"
          >保存</el-button>
        </div> -->
        <div class="code-editor-container" style="margin-top:20px">
            <iframe :src="src" width="100%" height="700px">
              This browser does not support PDFs. Please download the PDF to view it: <a href="/test.pdf" rel="external nofollow" >Download PDF</a>
            </iframe>
        </div>
      </div>
    </div>
    <!--<div class="textedits_inp_14s">
       控制台标签div
      <div id="textedits_lable">
        <button type="button" style="float:right;" onclick="textedits_window(this)" class="el-button el-button--primary el-button--mini"><i class="el-icon-thirddown"></i></button>
      </div>
      控制台内容div
      <div id="textedits_log">
        //<el-input type="textarea" v-model="inputText" rows="10"></el-input>
      </div>
    </div> -->
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import { mapGetters } from "vuex";
import PDFJS from 'pdfjs-dist'
import pdf from "vue-pdf"
import { getFileStream } from "@/api/libs/threelibs";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: {
    pdf
  },
  //props用于接收父级传值
  props: [],
  //监控data中的数据变化
  watch: {},
  data() {
    //这里存放数据
    return {
      threeLibsFilePathDTO: {},
      src: "",
      currentPage: 0, // pdf文件页码
      pageCount: 0, // pdf文件总页数
      fileType: 'pdf', // 文件类型
    };
  },
  //监听属性 类似于data概念
  computed: {
    ...mapGetters([]),
    },
  //方法集合
  methods: {
    // 改变PDF页码,val传过来区分上一页下一页的值,0上一页,1下一页
      changePdfPage (val) {
        // console.log(val)
        if (val === 0 && this.currentPage > 1) {
          this.currentPage--
          // console.log(this.currentPage)
        }
        if (val === 1 && this.currentPage < this.pageCount) {
          this.currentPage++
          // console.log(this.currentPage)
        }
      },
    loadPdfHandler (e) {
        this.currentPage = 1 // 加载的时候先加载第一页
    }
  },
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
      console.log(this.$route.query.filePath)
      var filePath = this.$route.query.filePath
      var path = filePath.replace(/\\/g,"!")
      this.src = "/libs/threelibs/getFileStream/"+path
      // this.threeLibsFilePathDTO.filePath = this.$route.query.filePath;
      // getFileStream(this.threeLibsFilePathDTO).then(res => {
      //   alert(res)
      // })
  },
  //生命周期 - 挂载完y成（可以访问DOM元素）
  mounted() {
  },
  beforeCreate() {}, //生命周期 - 创建之前
  beforeMount() {}, //生命周期 - 挂载之前
  beforeUpdate() {}, //生命周期 - 更新之前
  updated() {}, //生命周期 - 更新之后
  beforeDestroy() {}, //生命周期 - 销毁之前
  destroyed() {}, //生命周期 - 销毁完成
  activated() {} //如果页面有keep-alive缓存功能，这个函数会触发
};
</script>
<style lang='scss' scoped>
//@import url(); 引入公共css类
</style>