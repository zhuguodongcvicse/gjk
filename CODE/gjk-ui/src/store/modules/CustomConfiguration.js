import { getStore, removeStore, setStore } from '@/util/store'

const CustomConfiguration = {
    state : {
      subMapCustomConfig : new Map(),
      pubMapCustomConfig : new Map(),
      themeData : getStore({ name: 'themeData' })||{},
      netWorkIn : new Map(),
      netWorkData : getStore({ name: 'netWorkData' })||{},
      netWorkOut : new Map(),
      partList : new Array(),
    },
    actions: {
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
        },params) {
          return new Promise((resolve, reject) => {
            commit('SET_XMLData', params)
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
        SET_XMLData: (state, params) => {
          console.log(1111111111);
          state.themeData = params.themeData
          state.netWorkData = params.netWorkData
          state.partList = params.partList
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