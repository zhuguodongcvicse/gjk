import {getStore, removeStore, setStore} from '@/util/store'

const hardwarelibObj = {
  state: {
    hardwarelibObj: getStore({name: 'hardwarelibObj'}) || new Object()
  },
  actions: {
    setHardwarelibObj({
                        commit
                      }, params) {
      return new Promise((resolve, reject) => {
        commit('SET_HARDWARELIBOBJ', params)
        resolve()
      })
    }
  },
  mutations: {
    SET_HARDWARELIBOBJ: (state, params) => {
      // console.log("params", params)
      state.hardwarelibObj = params
      setStore({
        name: 'hardwarelibObj',
        content: state.hardwarelibObj,
        type: "session"
      })
      // console.log("state", state)
    }
  }
}
export default hardwarelibObj
