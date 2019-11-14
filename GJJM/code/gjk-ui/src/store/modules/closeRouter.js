import { getStore, removeStore, setStore } from '@/util/store'

const closeRouter = {
    state: {
        closeRouter: getStore({ name: 'closeRouter' }) || new Array()
    },
    actions: {
        //解析主题配置xml将数据存储
        closeRouterMethod({
            commit
        }, params) {
            // console.log("params", params)
            return new Promise((resolve, reject) => {
                commit('SET_CloseRouterData', params)
                resolve()
            })
        }
    },
    mutations: {
        SET_CloseRouterData: (state, params) => {
            console.log("params", params)
            state.closeRouter = params
            setStore({
                name: 'closeRouter',
                content: state.closeRouter,
                type: "session"
            })
            // console.log("state", state)
        }
    }
}
export default closeRouter