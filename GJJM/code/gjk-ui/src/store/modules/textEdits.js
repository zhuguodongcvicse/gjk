import { getStore, removeStore, setStore } from '@/util/store'

const textEdits = {
    state : {
        textLog :{}
    },
    actions: {
        saveTextLog({
            commit
          }, textLog) {
            return new Promise(resolve =>{
              commit("SET_saveTextLog",textLog)
              resolve()
            })
          },
    },
    mutations: {
        SET_saveTextLog :(state,value) =>{
            state.textLog = value
            console.log("商店存入日志",state.textLog);
            setStore({
              name : 'textLog',
              content : state.textLog,
              type : "session"
            })
          },
    }
}
export default textEdits