import { getStore, removeStore, setStore } from '@/util/store'

const projectTreeShow = {
    state: {
        projectTreeShow: getStore({ name: 'projectTreeShow' }) || new Array()
    },
    actions: {
        //解析主题配置xml将数据存储
        setProjectTreeShow({
            commit
        }, params) {
            return new Promise((resolve, reject) => {
                commit('SET_ProTreeData', params)
                resolve()
            })
        }
    },
    mutations: {
        SET_ProTreeData: (state, params) => {
            state.projectTreeShow = params
            setStore({
                name: 'projectTreeShow',
                content: state.projectTreeShow,
                type: "local"
            })
            console.log("state", state)
        }
    }
}
export default projectTreeShow