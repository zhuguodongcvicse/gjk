<template>
  <div class="avue-tags gjk_tags_14" v-if="showTag">
    <!-- tag盒子 -->
    <div
      v-if="contextmenuFlag"
      class="avue-tags__contentmenu"
      :style="{left:contentmenuX+'px',top:contentmenuY+'px'}"
      @mouseleave="changeCount()"
    >
      <div class="item" @click="closeOthersTags">关闭其他</div>
      <div class="item" @click="closeAllTags">关闭全部</div>
    </div>
    <div class="avue-tags__box" :class="{'avue-tags__box--close':!website.isFirstPage}">
      <el-tabs
        v-model="active"
        type="card"
        @contextmenu.native="handleContextmenu"
        :closable="tagLen!==1"
        @tab-click="openTag"
        @edit="menuTag"
      >
        <el-tab-pane
          :key="item.value"
          v-for="item in tagList"
          :label="item.label"
          :name="item.value"
        ></el-tab-pane>
      </el-tabs>
      <div class="avue-tags__menu el-dropdown" style="right:70px;">
        <el-button class="btn_more_14" type="primary" size="mini" @click="open_console">控制台</el-button>
      </div>
      <el-dropdown class="avue-tags__menu">
        <el-button class="btn_more_14" type="primary" size="mini">
          更多
          <i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="closeOthersTags">关闭其他</el-dropdown-item>
          <el-dropdown-item @click.native="closeAllTags">关闭全部</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="提示" :visible.sync="dialogVisibleOfCloseRouter" width="30%">
      <!--  :before-close="handleClose" -->
      <span>是否保存当前模型？</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeRouter()">不保存并关闭</el-button>
        <el-button type="primary" @click="saveFlowData()">保存并关闭</el-button>
      </span>
    </el-dialog>
  </div>

</template>
<script>
    import {mapGetters, mapState} from "vuex";

    export default {
        name: "tags",
        data() {
            return {
                active: "",
                contentmenuX: "",
                contentmenuY: "",
                dialogVisibleOfCloseRouter: false,
                routerPathValue: '',
                routerPathAction: '',
                saveDataIsFirstFlag: 0,
                contextmenuFlag: false
            };
        },
        created() {
        },
        mounted() {
            this.setActive();
        },
        watch: {
            tag() {
                this.setActive();
            },
            contextmenuFlag() {
                window.addEventListener("mousedown", this.watchContextmenu);
            }
        },
        computed: {
            ...mapGetters(["tagWel", "tagList", "tag", "website"]),
            ...mapState({
                showTag: state => state.common.showTag
            }),
            tagLen() {
                return this.tagList.length || 0;
            }
        },
        methods: {
            changeCount() {
                setTimeout(() => {
                    this.contextmenuFlag = false
                }, 500)

            },
            watchContextmenu() {
                if (!this.$el.contains(event.target) || event.button !== 0) {
                    this.contextmenuFlag = false;
                }

                window.removeEventListener("mousedown", this.watchContextmenu);
            },
            handleContextmenu(event) {
                let target = event.target;
                // 解决 https://github.com/d2-projects/d2-admin/issues/54
                let flag = false;
                if (target.className.indexOf("el-tabs__item") > -1) flag = true;
                else if (target.parentNode.className.indexOf("el-tabs__item") > -1) {
                    target = target.parentNode;
                    flag = true;
                }
                if (flag) {
                    event.preventDefault();
                    event.stopPropagation();
                    this.contentmenuX = event.clientX;
                    this.contentmenuY = event.clientY;
                    this.tagName = target.getAttribute("aria-controls").slice(5);
                    this.contextmenuFlag = true;
                }
            },
            //激活当前选项
            setActive() {
                this.active = this.tag.value;
            },
            closeRouter() {
                if (this.routerPathAction === "remove") {
                    this.$store.commit("IS_SAVE", "1");
                    let {tag, key} = this.findTag(this.routerPathValue);
                    this.$store.commit("DEL_TAG", tag);
                    if (tag.value === this.tag.value) {
                        tag = this.tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个
                        this.openTag(tag);
                    }
                }
                this.dialogVisibleOfCloseRouter = false
            },
            saveFlowData() {
                if(this.routerPathAction === "remove"){
                    this.$store.commit("IS_SAVE", "2");
                    setTimeout(() => {
                        let {tag, key} = this.findTag(this.routerPathValue);
                        this.$store.commit("DEL_TAG", tag);
                        if (tag.value === this.tag.value) {
                            tag = this.tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个
                            this.openTag(tag);
                        }
                        this.dialogVisibleOfCloseRouter = false
                    }, 1000);
                }
                //this.$store.commit("IS_SAVE", "2");
            },
            menuTag(value, action) {
                 // console.log("value", value)
                // console.log("action", action)
                if (value.split("?")[0] == "/comp/manager/process"){
                     this.$store.commit("IS_SAVE", "0");
                    this.routerPathValue = value
                    this.routerPathAction = "remove"
                    this.dialogVisibleOfCloseRouter = true
                } else {
                    if (action === "remove") {
                        let {tag, key} = this.findTag(value);
                        this.$store.commit("DEL_TAG", tag);
                        if (tag.value === this.tag.value) {
                            tag = this.tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个
                            this.openTag(tag);
                        }
                    }
                }
            },
            openTag(item) {
                //直接点击标签跳到路由则关闭以下路由标签
                /* if (item.$parent != null) {
                  var closeLastTag = item.$parent.$children;
                  for (const i in closeLastTag) {
                    if (closeLastTag[i].label == "芯片设计" || closeLastTag[i].label == "芯片编辑" || closeLastTag[i].label == "板卡设计"
                     || closeLastTag[i].label == "板卡编辑" || closeLastTag[i].label == "机箱设计" || closeLastTag[i].label == "机箱编辑"
                     || closeLastTag[i].label == "硬件模型新增"|| closeLastTag[i].label == "硬件模型编辑") {
                      let aa = this.findTag(closeLastTag[i].name);
                      this.$store.commit("DEL_TAG", aa.tag);
                    }
                  }
                } */
                // if (item.$options !== undefined && item.$options.propsData.label !== "机箱新增") {
                //     if (this.saveDataIsFirstFlag === 0) {
                //         console.log("保存机箱")
                //         this.saveDataIsFirstFlag = 1
                //     }
                // }
                // if (item.$options !== undefined && item.$options.propsData.label === "机箱新增") {
                //     this.saveDataIsFirstFlag = 0
                // }
                // console.log("item", item);
                let tag;
                if (item.name) {
                    tag = this.findTag(item.name).tag;
                } else {
                    tag = item;
                }
                // console.log("tag",tag)
                this.$router.push({
                    path: this.$router.$avueRouter.getPath({
                        name: tag.label,
                        src: tag.value
                    }),
                    query: tag.query
                });
            },
            closeOthersTags() {
                this.contextmenuFlag = false;
                this.$store.commit("DEL_TAG_OTHER");
            },
            findTag(value) {
                // console.log("this.tagList",this.tagList)
                let tag, key;
                this.tagList.map((item, index) => {
                    if (item.value === value) {
                        tag = item;
                        key = index;
                    }
                });
                // console.log("{ tag: tag, key: key }", { tag: tag, key: key });
                return {tag: tag, key: key};
            },
            closeAllTags() {
                this.contextmenuFlag = false;
                this.$store.commit("DEL_ALL_TAG");
                this.$router.push({
                    path: this.$router.$avueRouter.getPath({
                        src: this.tagWel.value
                    }),
                    query: this.tagWel.query
                });
            },
            //激活控制台
            open_console() {
                this.$emit("ecrollbarparent", "70%");
            }
        }
    };
</script>


