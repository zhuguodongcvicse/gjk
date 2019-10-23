import { getStore, removeStore, setStore } from '@/util/store'
import { remote } from "@/api/admin/dict";
import { fetchStrInPointer } from "@/api/libs/structlibs";
const comp = {
    state: {
        saveXmlMaps: getStore({ name: 'saveXmlMaps' }) || {},
        compChineseMapping: getStore({ name: 'compChineseMapping' }) || [],
        selectBindValue: getStore({ name: 'selectBindValue' }) || {},
        analysisBaseFile: getStore({ name: 'analysisBaseFile' }) || [],
        cjUnitParam: getStore({ name: 'cjUnitParam' }) || {},
        strInPointer: getStore({ name: 'strInPointer' }) || [],
    },
    actions: {
        //绑定下拉框的值
        setAnalysisCjUnitParam({ commit }, { key, value }) {
            return new Promise((resolve, reject) => {
                commit('SET_ANALYSISCJUNITPARAM', { key, value });
                resolve();
            })
        },
        //存结构体类型
        setFetchStrInPointer({ commit }) {
            return new Promise((resolve, reject) => {
                fetchStrInPointer().then(res => {
                    commit('SET_FETCHSTRPOINTER', res.data.data);
                    resolve()
                }).catch((e) => {
                    reject(e)
                })

            })
        },
        clearAnalysisCjUnitParam({ commit }) {
            return new Promise((resolve, reject) => {
                commit('CLEAR_ANALYSISCJUNITPARAM');
                resolve();
            })
        },
        //设置临时的头文件中结构体
        setSaveXmlMaps({ commit }, xmlMaps) {
            return new Promise((resolve, reject) => {
                try {
                    commit('SET_SAVEXMLMAPS', xmlMaps);
                    resolve();
                } catch (e) {
                    reject(e)
                }
            })
        },
        setChineseMapping({ commit }, param) {
            return new Promise((resolve, reject) => {
                remote(param).then(res => {
                    commit('SET_CHINESEMAPPING', res.data.data);
                    resolve()
                }).catch((e) => {
                    reject(e)
                })
            })
        },
        setAnalysisBaseFile({ commit }, value) {
            return new Promise(() => {
                commit('SET_ANALYSISBASEFILE', value);
            })
        },
        clearAnalysisBaseFile({ commit }) {
            return new Promise((resolve, reject) => {
                try {
                    commit('CLEAR_ANALYSISBASEFILE');
                    resolve();
                } catch (e) {
                    reject(e)
                }
            })
        },
    },
    mutations: {
        SET_SAVEXMLMAPS: (state, xmlMaps) => {
            state.saveXmlMaps = xmlMaps
            setStore({
                name: 'saveXmlMaps',
                content: state.saveXmlMaps,
                type: "session"
            })
        },
        SET_CHINESEMAPPING: (state, param) => {
            state.compChineseMapping = param
            setStore({
                name: 'compChineseMapping',
                content: state.compChineseMapping,
                type: "session"
            })
        },
        SET_SELECTBINDVALUE: (state, { key, value }) => {
            state.selectBindValue[key] = value
            setStore({
                name: 'selectBindValue',
                content: state.selectBindValue,
                type: "session"
            })
        },
        SET_ANALYSISBASEFILE: (state, param) => {
            for (let key in param) {
                state.analysisBaseFile[key] = param[key]
            }
            setStore({
                name: 'analysisBaseFile',
                content: state.analysisBaseFile,
                type: "session"
            })
        },
        CLEAR_ANALYSISBASEFILE: (state) => {
            state.analysisBaseFile = []
            setStore({
                name: 'analysisBaseFile',
                content: state.analysisBaseFile,
                type: "session"
            })
        },
        SET_ANALYSISCJUNITPARAM: (state, { key, value }) => {
            state.cjUnitParam[key] = value
            setStore({
                name: 'cjUnitParam',
                content: state.cjUnitParam,
                type: "session"
            })
        },
        CLEAR_ANALYSISCJUNITPARAM: (state) => {
            state.cjUnitParam = {}
            setStore({
                name: 'cjUnitParam',
                content: state.cjUnitParam,
                type: "session"
            })
        },
        //设置结构体
        SET_FETCHSTRPOINTER: (state, param) => {
            state.strInPointer = param
            setStore({
                name: 'strInPointer',
                content: state.strInPointer,
                type: "session"
            })
        },
        //清空结构体
        CLEAR_FETCHSTRPOINTER: (state, param) => {
            state.strInPointer = []
            setStore({
                name: 'strInPointer',
                content: state.strInPointer,
                type: "session"
            })
        },
    }
}
export default comp
