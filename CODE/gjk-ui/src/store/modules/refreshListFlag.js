import { getStore, removeStore, setStore } from '@/util/store'

const refreshListFlag = {
    state: {
      refreshListFlag: getStore({ name: 'refreshListFlag' }) || new Number()
    },
    actions: {
        setRefreshListFlag({
            commit
        }, params) {
            return new Promise((resolve, reject) => {
                commit('SET_REFRESHLISTFLAG', params)
                resolve()
            })
        }
    },
    mutations: {
        SET_REFRESHLISTFLAG: (state, params) => {
              // console.log("params", params)
              state.refreshListFlag = params
              setStore({
                  name: 'refreshListFlag',
                  content: state.refreshListFlag,
                  type: "session"
              })
              // console.log("state", state)
          }
      }
}
export default refreshListFlag
