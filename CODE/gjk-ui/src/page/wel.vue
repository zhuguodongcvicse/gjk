<template>
  <div class="page_wel_14s">
    <basic-container>
      <div class="banner-text">
        <!-- <span>
          <img src="https://img.shields.io/badge/Spring%20Boot-2.1.2.RELEASE-yellowgreen.svg" alt="Downloads">
          <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.RELEASE-blue.svg" alt="Coverage Status">
        </span>-->
        <br />
        <span>
          <avue-data-panel :option="messageNotification" v-if="isApplyShow" />
          <el-collapse v-model="activeNames">
            <el-collapse-item title="完整的微服务架构" name="1">
              <div>基于Spring Cloud Greenwich.RELEASE</div>
              <div>基于Spring Boot 2.1.2.RELEASE</div>
            </el-collapse-item>
            <el-collapse-item title="完美的容器化支持" name="2">
              <div>支持docker部署</div>
              <div>支持Rancher2 + Kubernetes部署</div>
            </el-collapse-item>
            <el-collapse-item title="最终一致性分布式事务" name="3">
              <div>基于开源LCN 分布式事务解决方案深度定制</div>
              <div>优化集群部署，提升性能</div>
            </el-collapse-item>
          </el-collapse>
        </span>
      </div>
    </basic-container>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getUnprocessedRecord } from "@/api/libs/approval";
import { getUserhasApplyAuto } from "@/api/admin/user";
export default {
  name: "wel",
  data() {
    return {
      activeNames: ["1", "2", "3", "4"],
      DATA: [],
      text: "",
      actor: "",
      count: 0,
      isText: false,

      messageNotification: {
        span: 8,
        data: [
          {
            // click: function(item) {
            //   alert(JSON.stringify(item));
            // },
            title: "审批待处理通知",
            href: "/#/library/ce",
            count: "",
            icon: "el-icon-message",
            color: "#ffffff"
          }
        ]
      },

      dialogVisible: false,
      isApplyShow: false
    };
  },
  computed: {
    ...mapGetters(["website", "userInfo"])
  },
  methods: {
    getData() {
      if (this.count < this.DATA.length - 1) {
        this.count++;
      } else {
        this.count = 0;
      }
      this.isText = true;
      this.actor = this.DATA[this.count];
    },
    setData() {
      let num = 0;
      let count = 0;
      let active = false;
      let timeoutstart = 5000;
      let timeoutend = 1000;
      let timespeed = 10;
      setInterval(() => {
        if (this.isText) {
          if (count == this.actor.length) {
            active = true;
          } else {
            active = false;
          }
          if (active) {
            num--;
            this.text = this.actor.substr(0, num);
            if (num == 0) {
              this.isText = false;
              setTimeout(() => {
                count = 0;
                this.getData();
              }, timeoutend);
            }
          } else {
            num++;
            this.text = this.actor.substr(0, num);
            if (num == this.actor.length) {
              this.isText = false;
              setTimeout(() => {
                this.isText = true;
                count = this.actor.length;
              }, timeoutstart);
            }
          }
        }
      }, timespeed);
    }
  },
  created() {
    getUnprocessedRecord({ applyUserId: this.userInfo.userId }).then(
      Response => {
        this.messageNotification.data[0].count = Response.data.data.length;
      }
    );
    getUserhasApplyAuto().then(Response => {
      for (let user of Response.data.data) {
        if (this.userInfo.userId == user.userId) {
          this.isApplyShow = true;
        }
      }
    });
  }
};
</script>

<style scoped="scoped" lang="scss">

</style>
