import { getStore, removeStore, setStore, strMapToObj } from '@/util/store'
import { remote } from "@/api/admin/dict";

const CustomConfiguration = {
    state : {
      subMapCustomConfig : new Map(),
      pubMapCustomConfig : new Map(),
      themeData : getStore({ name: 'themeData' })||{},
      netWorkIn : new Map(),
      netWorkData : getStore({ name: 'netWorkData' })||{},
      netWorkOut : new Map(),
      partList : getStore({name: 'partList'}) || new Array(),
      xmlDataMap : getStore({name: 'xmlDataMap'}) || new Map(),
      themeCustomConfigData: new Map(),
      themeChineseMapping: getStore({ name: 'themeChineseMapping' }) || [],
    },
    actions: {
        setThemeCustomConfigData({ commit }, params){
          return new Promise(resolve =>{
            var key = params.key;
            var value = params.value
            commit("SET_ThemeData",{key, value})
            resolve()
          })
        },
        // 删除主题配置中的topic
        delThemeCustomConfigDataTopic({ commit }, params){
          return new Promise(resolve =>{
            var sysId = params.sysId
            var titleType = params.titleType
            var topicKey = params.topicKey
            commit("DEL_ThemeTopic",{sysId, titleType, topicKey})
            resolve()
          })
        },
        // 删除主题配置中的dataStream
        delThemeCustomConfigDataDataStream({ commit }, params){
          return new Promise(resolve =>{
            var sysId = params.sysId
            var titleType = params.titleType
            var topicKey = params.topicKey
            var dskey = params.dskey
            commit("DEL_ThemeDataStream",{sysId, titleType, topicKey, dskey})
            resolve()
          })
        },
        setThemeChineseMapping({ commit }, param) {
          return new Promise((resolve, reject) => {
            remote(param).then(res => {
              commit('SET_THEMECHINESEMAPPING', res.data.data);
              resolve()
            }).catch((e) => {
              reject(e)
            })
          })
        },
        //存储subscribe整体数据
        getSubMapCustomConfig({
          commit
        }, subTopic) {
          return new Promise(resolve =>{
            var key = subTopic.key;
            var value = subTopic.value
            commit("SET_SubMapCustomConfig",{key,value})
            resolve()
          })
        },
        //删除subscribe数据
        delSubTopicData({
          commit
        },subTopicKey){
          return new Promise(resolve =>{
            commit("DELETE_SUBTopicData",subTopicKey)
            resolve()
          })
        },
        //删除subscribe中DataStream数据
        delSubDataStream({
          commit
        },subTopicDataStream){
          return new Promise(resolve =>{
            var subTopicKey = subTopicDataStream.topicKey
            var DataStreamKey = subTopicDataStream.DataStreamKey
            console.log("11111111111",subTopicKey,DataStreamKey)
            commit("DELETE_SUBTopicDataStream",{subTopicKey,DataStreamKey})
            resolve()
          })
        },
        //存储publish数据
        getPubMapCustomConfig({
          commit
        }, pubTopic) {
          return new Promise(resolve =>{
            var key = pubTopic.key;
            var value = pubTopic.value
            commit("SET_PubMapCustomConfig",{key,value})
            resolve()
          })
        },
        //删除pubLish数据
        delPubTopicData({
          commit
        },pubTopicKey){
          return new Promise(resolve =>{
            commit("DELETE_PUBTopicData",pubTopicKey)
            resolve()
          })
        },
        //删除publish中DataStream数据
        delPubDataStream({
          commit
        },pubTopicDataStream){
          return new Promise(resolve =>{
            var pubTopicKey = pubTopicDataStream.topicKey
            var DataStreamKey = pubTopicDataStream.DataStreamKey
            commit("DELETE_PUBTopicDataStream",{pubTopicKey,DataStreamKey})
            resolve()
          })
        },
        //解析主题配置xml将数据存储
        AnalysisXML({
          commit
        },xmlParams) {
          return new Promise((resolve, reject) => {
            var params = xmlParams.params
            var id = xmlParams.id
            console.log("params",params,id)
            commit('SET_XMLData', {params,id})
            resolve()
          })
        },

        //存储netWorkIn数据
        getNetworkIn({
          commit
        },params) {
          return new Promise((resolve,reject) =>{
            commit('SET_NetWorkIn',params)
            resolve()
          })
        },
        //删除netWorkIn数据
        delNetworkIn({
          commit
        },params) {
          return new Promise(resolve =>{
            commit('DEL_NetWorkIn',params)
            resolve()
          })
        },
        //存储netWorkOut数据
        getNetworkOut({
          commit
        },params) {
          return new Promise((resolve,reject) =>{
            commit('SET_NetWorkOut',params)
            resolve()
          })
        },
        //删除netWorkOut数据
        delNetworkOut({
          commit
        },params) {
          return new Promise(resolve =>{
            commit('DEL_NetWorkOut',params)
            resolve()
          })
        },
    },
    mutations: {
      SET_ThemeData: (state, {key,value}) => {
        // state.themeCustomConfigData[key] = value;
        state.themeCustomConfigData.set(key, value)
        setStore({
          name: 'themeCustomConfigData',
          content: state.themeCustomConfigData,
          type:"session"
        })
      },
      DEL_ThemeTopic: (state,{sysId, titleType, topicKey}) =>{
        let projectData = state.themeCustomConfigData.get(sysId)
        let titleData = projectData[titleType]
        titleData.delete(topicKey)
      },
      DEL_ThemeDataStream: (state,{sysId, titleType, topicKey, dskey}) =>{
        let projectData = state.themeCustomConfigData.get(sysId)
        let titleData = projectData[titleType]
        let topicData = titleData.get(topicKey)
        topicData.dataStream.delete(dskey)
      },
      SET_THEMECHINESEMAPPING: (state, param) => {
        state.themeChineseMapping = param
        setStore({
          name: 'themeChineseMapping',
          content: state.themeChineseMapping,
          type: "session"
        })
      },
      SET_SubMapCustomConfig: (state, {key,value}) => {
          state.subMapCustomConfig.set(key, JSON.parse(JSON.stringify(value)));
          //console.log("商店SUB", JSON.stringify(state.subMapCustomConfig));
          setStore({
            name: 'subMapCustomConfig',
            content: state.subMapCustomConfig,
            type:"session"
          })
        },
        SET_PubMapCustomConfig: (state, {key,value}) => {
          state.pubMapCustomConfig.set(key, JSON.parse(JSON.stringify(value)));
          //console.log("商店PUB", JSON.stringify(state.pubMapCustomConfig));
          setStore({
            name: 'pubMapCustomConfig',
            content: state.pubMapCustomConfig,
            type:"session"
          })
        },
        SET_XMLData: (state, {params,id}) => {
          console.log(1111111111,id);
          state.themeData = params.themeData
          state.netWorkData = params.netWorkData
          state.partList = params.partList
          // state.xmlDataMap.set(id,params)
          // var xmlDataObj = {}
          // xmlDataObj[id] = params
          // state.xmlDataMap.push(xmlDataObj)
          // state.xmlDataMap[id] = params
          state.xmlDataMap.set(id, params)

          let xmlDataMapObj = strMapToObj(state.xmlDataMap)

          setStore({
            name: 'themeData',
            content: state.themeData,
            type:"session"
          })
          setStore({
            name: 'netWorkData',
            content: state.netWorkData,
            type:"session"
          })
          setStore({
            name: 'partList',
            content: state.partList,
            type:"session"
          })
          setStore({
            name: 'xmlDataMap',
            content: xmlDataMapObj,
            type:"session"
          })
          console.log("商店state.xmlDataMap++++++++++++",state.xmlDataMap)
        },
        SET_NetWorkIn :(state,{key,value}) =>{
          state.netWorkIn.set(key,JSON.parse(JSON.stringify(value)));
          console.log("商店SET_NetWorkIn", JSON.stringify(state.netWorkIn));
          setStore({
            name : 'netWorkIn',
            content : state.netWorkIn,
            type : "session"
          })
        },
        SET_NetWorkOut :(state,{key,value}) =>{
          state.netWorkOut.set(key,JSON.parse(JSON.stringify(value)));
         // console.log("商店111", JSON.stringify(state.netWorkOut));
          setStore({
            name : 'netWorkOut',
            content : state.netWorkOut,
            type : "session"
          })
        },

        DELETE_SUBTopicData :(state,params) =>{
          for(var [key, value] of  state.subMapCustomConfig){
            if(key == params){
              state.subMapCustomConfig.delete(key)
            }
          }
        },
        DELETE_SUBTopicDataStream :(state,{subTopicKey,DataStreamKey}) =>{
          console.log("商店中输出",subTopicKey,DataStreamKey)

        //  for(var [key, value] of  state.subMapCustomConfig){
        //   if(key == subTopicKey){
        //     for(var i = 0;i<value.dataStream.length;i++){
        //       if(value.dataStream[i][0] == DataStreamKey){
        //         value.dataStream.splice(i,1)
        //       }
        //       console.log("删除",value.dataStream)
        //     }
        //   }
        //  }
        for(var i = 0;i<state.subMapCustomConfig.get(subTopicKey).dataStream.length;i++){
          if(state.subMapCustomConfig.get(subTopicKey).dataStream[i][0] == DataStreamKey){
            state.subMapCustomConfig.get(subTopicKey).dataStream.splice(i,1)
          }
        }
         console.log(state.subMapCustomConfig)
        //  setStore({
        //   name: 'subMapCustomConfig',
        //   content: state.subMapCustomConfig,
        //   type:"session"
        // })
        },
        DELETE_PUBTopicData :(state,params) =>{
          for(var [key, value] of  state.pubMapCustomConfig){
            if(key == params){
              state.pubMapCustomConfig.delete(key)
            }
          }
        },
        DELETE_PUBTopicDataStream :(state,{pubTopicKey,DataStreamKey}) =>{
          for(var [key, value] of  state.pubMapCustomConfig){
           if(key == pubTopicKey){
             for(var i = 0;i<value.funcConfig.length;i++){
               if(value.funcConfig[i][0] == DataStreamKey){
                 value.funcConfig.splice(i,1)
               }
             }
           }
          }
         },
         DEL_NetWorkIn :(state,params) =>{
          for(var [key, value] of  state.netWorkIn){
            if(key == params){
              state.netWorkIn.delete(key)
              console.log("删除后商店数据IN",state.netWorkIn)
            }
          }
         },
         DEL_NetWorkOut :(state,params) =>{
          for(var [key, value] of  state.netWorkOut){
            if(key == params){
              state.netWorkOut.delete(key)
            }
          }
          console.log("删除后数据",state.netWorkOut)
         },
        CLEAR_LOCK: (state) => {
         // state.mapCustomConfig = {}
          removeStore({
            name: 'subMapCustomConfig'
          })
        }

      }
}
export default CustomConfiguration
