import store from '@/store/index.js'
import router from '@/router/router.js'
/**
 * 组件computed: { ...mapGetters(["tagWel", "tagList", "tag", "website"]) },
 * 声明var tag1 = this.tag
 * 调用menuTag(this.$route.path, "remove", this.tagList, tag1);
 * @param {当前路由的url} value
 * @param {"remove"} action
 * @param {store中取} tagList
 * @param {store中取} tag1
 */
export function menuTag(value, action, tagList, tag1) {
  let tagUrl = urlStr(tag1.value)
  if (action === "remove") {
    let { tag, key } = findTag(value, tagList);
    store.commit("DEL_TAG", tag);
    if (tag.value === tagUrl) {
      tag = tagList[key === 0 ? key : key - 1]; //如果关闭本标签让前推一个src\router\router.js
      openTag(tag);
    }
  } if (action === "removeOpen") {
    let { tag } = findTag(value, tagList);
    store.commit("DEL_TAG", tag);
    openTag(findTag(tag1.value, tagList).tag);
  }
}
function findTag(value, tagList) {
  let tag, key;
  tagList.map((item, index) => {
    if (item.value.indexOf('?') != -1) {
      tag = item;
      key = index;
      tag.value = urlStr(item.value)
    } else if (item.value === value) {
      tag = item;
      key = index;
    }
  });
  return { tag: tag, key: key };
}
function urlStr(oldUrl) {
  let ifHaveArgs = oldUrl.indexOf('?')
  let newUrl
  if (ifHaveArgs === -1) {
    newUrl = oldUrl.slice(0, oldUrl.length)
  } else {
    newUrl = oldUrl.slice(0, oldUrl.indexOf('?'))
  }
  return newUrl
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
