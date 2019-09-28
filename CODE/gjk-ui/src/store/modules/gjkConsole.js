import { getStore, removeStore, setStore } from '@/util/store'

const gjkConsole = {
    state : {
        consoleLog :{}
    },
    actions: {
        saveConsoleLog({
            commit
          }, consoleLog) {
            return new Promise(resolve =>{
              commit("SET_saveConsoleLog",consoleLog)
              resolve()
            })
          },
    },
    mutations: {
        SET_saveConsoleLog :(state,value) =>{
            state.consoleLog = value
            //console.log("控制台日志",state.consoleLog);
            setStore({
              name : 'consoleLog',
              content : state.consoleLog,
              type : "session"
            })
          },
    }
}
export default gjkConsole