import { getStore, removeStore, setStore } from '@/util/store'

const infList = {
    state: {
        infList: new Array()
    },
    actions: {
        //解析主题配置xml将数据存储
        allInfList({
            commit
        }, params) {
            return new Promise((resolve, reject) => {
                commit('SET_InfData', params)
                resolve()
            })
        }
    },
    mutations: {
        SET_InfData: (state, params) => {
            // console.log("params", params)
            state.infList = params
            setStore({
                name: 'infList',
                content: state.infList,
                type: "session"
            })
            // console.log("state", state)
        }
    }
}
export default infList