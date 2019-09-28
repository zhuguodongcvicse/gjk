import store from '@/store/index.js'
import router from '@/router/router.js'
/**路由跳转需要变为name
 * 组件computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
 * 声明var tag1 = this.tag
 * 调用menuTag(this.$route.path, "remove", this.tagList, tag1);
 * @param {当前路由的url} value 
 * @param {"remove"} action 
 * @param {store中取} tagList 
 * @param {store中取} tag1 
 */
export function menuTag(value, action, tagList, tag1) {
    if (action === "remove") {
        let { tag, key } = findTag(value, tagList);
        store.commit("DEL_TAG", tag);
        if (tag.value === tag1.value) {
          tag = tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个src\router\router.js
          openTag(tag);
        }
      }
}
function findTag(value, tagList) {
  // console.log("value",value)
  // console.log("tagList",tagList)
    let tag, key;
    tagList.map((item, index) => {
      if (item.value === value) {
        tag = item;
        key = index;
      }
    });
    return { tag: tag, key: key };
}
function openTag(item) {
    let tag;
    if (item.name) {
      tag = findTag(item.name).tag;
    } else {
      tag = item;
    }
    router.push({
      path: router.$avueRouter.getPath({
        name: tag.label,
        src: tag.value
      }),
      query: tag.query
    });
}