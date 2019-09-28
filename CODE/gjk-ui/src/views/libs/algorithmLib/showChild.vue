<template>
  <div class="app-container calendar-list-container libs_algorithmlib_index_14s libs_algorithmlib_showchild_14s">
    <basic-container>
      <el-form :model="childForm" :inline="true">
        <el-form-item label="">
          <textarea v-model="textContext" ></textarea>
        </el-form-item>
      </el-form>
    </basic-container>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
  props: ["filePath", "textContext"],
    data() {
      return {
        inputValue: '',
        childForm:{}
      }
    },
    watch: {
      filePath:function(){
        console.log("this.filePath",this.filePath)
      }
    },
    created() {

    },
    computed: {
    },
    methods: {
      
    }
  }
</script>

