import { getStore, removeStore, setStore } from '@/util/store'

const chipsOfHardwarelibs = {
    state: {
        chipsOfHardwarelibs: getStore({ name: 'chipsOfHardwarelibs' }) || new Array()
    },
    actions: {
        //解析主题配置xml将数据存储
        allChipsOfHardwarelibs({
            commit
        }, params) {
            // console.log("params", params)
            return new Promise((resolve, reject) => {
                commit('SET_ChipsData', params)
                resolve()
            })
        }
    },
    mutations: {
        SET_ChipsData: (state, params) => {
            // console.log("params", params)
            state.chipsOfHardwarelibs = params
            setStore({
                name: 'chipsOfHardwarelibs',
                content: state.chipsOfHardwarelibs,
                type: "local"
            })
            // console.log("state", state)
        }
    }
}
export default chipsOfHardwarelibs