import { getStore, removeStore, setStore } from '@/util/store'

import {
  getParseHeaderObj
} from '@/api/comp/compParams'
import {
  fetchStructTypeAll
} from '@/api/libs/structlibs'
const struct = {
  state: {
    pointHFile: {},
    cjAttribute: getStore({ name: 'cjAttribute' }) || {},
    headerFile: getStore({ name: 'headerFile' }) || {
      inputXmlParams: {},
      outputXmlParams: {},
      inputParams: [],
      outputParams: [],
      structParams: {},
      structIdParams: {},
    },
    structType: getStore({ name: 'structType' }) || new Array(),
    xmlEntityVal: {},
    tmpProject: {},
    //保存计算长度
    tmpStructLength: getStore({ name: 'tmpStructLength' }) || [],
    tmpStructContent: getStore({ name: 'tmpStructContent' }) || {},
  },
  actions: {
    //设置临时的头文件中结构体
    setTmpStructContent({ commit }, value) {
      return new Promise(() => {
        commit('SET_TMPSTRUCTCONTENT', value);
      })
    },
    //设置长度
    setTmpStructLength({ commit }, value) {
      return new Promise(() => {
        commit('SET_TMPSTRUCTLENGTH', value);
      })
    },
    setPointHFile({ commit }, value) {
      return new Promise(() => {
        commit('SET_POINTHFILE', value);
      })
    },
    setCjAttribute({ commit }, { key, value }) {
      return new Promise(() => {
        commit('SET_CJATTRIBUTE', { key, value });
      })
    },
    clearCjAttribute({ commit }) {
      return new Promise(() => {
        commit('CLEAR_CJATTRIBUTE');
      })
    },
    //存结构体类型
    setStruceType({ commit }) {
      return new Promise((resolve, reject) => {
        fetchStructTypeAll().then(res => {
          // console.log("fetchStructTypeAll",res.data.data)
          commit('SET_STRUCETYPE', res.data.data);
          resolve()
        }).catch((e) => {
          reject(e)
        })

      })
    },
    setTmpProject({ commit }, tmpProject) {
      return new Promise(() => {
        commit('SET_TMPPROJECT', tmpProject);
      })
    },
    // SetXmlEntityVal({ commit }, xmlEntityVal) {
    //   return new Promise(() => {
    //     // commit('SET_XMLENTITY', xmlEntityVal)
    //   })
    // },
    // ClearXmlEntityVal({ commit }) {
    //   return new Promise(() => {
    //     // commit('SET_XMLENTITY', {})
    //   })
    // },
    clearParseHeaderObj({ commit }) {
      return new Promise(() => {
        commit('CLEAR_HEADERFILE');
      })
    },
    GetParseHeaderObj({
      commit
    }, headerFile
    ) {
      return new Promise((resolve, reject) => {
        getParseHeaderObj({ path: headerFile }).then((res) => {
          const data = res.data.data || {}
          commit('SET_STRUCT', data)
          resolve()
        }).catch((err) => {
          reject(err)
        })
      })
    }
  },

  mutations: {
    SET_TMPSTRUCTCONTENT: (state, params) => {
      // let p = {
      //   n1: {
      //     v1: {}, v2: {}
      //   },
      //   n2: {
      //     v1: {}, v2: {}
      //   }
      // }
      // for (const key in obj) {
      //   if (obj.hasOwnProperty(key)) {
      //     const element = obj[key];
      //   }
      // }
      // if (state.tmpStructContent.hasOwnProperty) {

      // }
    },
    SET_TMPSTRUCTLENGTH: (state, params) => {
      console.log(state.tmpStructLength, "state.tmpStructLength")
      if (state.tmpStructLength.length >= 10) {
        state.tmpStructLength.splice(0, 1)
        state.tmpStructLength.push(params)
      } else {
        state.tmpStructLength.push(params)
      }
      // state.cjAttribute[key] = value
      setStore({
        name: 'tmpStructLength',
        content: state.tmpStructLength,
        type: "session"
      })
    },
    SET_STRUCT: (state, params) => {
      const objChangeMap = (obj) => {
        let newObj = {}
        for (const key in obj) {
          if (obj.hasOwnProperty(key)) {
            newObj[obj[key].dbId] = obj[key]
          }
          return newObj;
        }
      }
      state.headerFile.inputXmlParams = params.inputXmlParams
      state.headerFile.outputXmlParams = params.outputXmlParams
      state.headerFile.inputParams = params.inputParams
      state.headerFile.outputParams = params.outputParams
      state.headerFile.structParams = params.structParams
      state.headerFile.structIdParams = objChangeMap(params.structParams)
      state.headerFile.inputXmlMapParams = params.inputXmlMapParams
      state.headerFile.outputXmlMapParams = params.outputXmlMapParams
      // state.headerFile.structParams = objChangeMap(params.structParams)
      setStore({
        name: 'headerFile',
        content: state.headerFile,
        type: "session"
      })
    },

    CLEAR_LOCK: (state) => {
      state.headerFile = {}
      removeStore({
        name: 'headerFile'
      })
    },
    SET_TMPPROJECT: (state, param) => {
      state.tmpProject = param
      setStore({
        name: 'tmpProject',
        content: state.tmpProject,
        type: "session"
      })
    },
    SET_STRUCETYPE: (state, param) => {
      state.structType = param
      setStore({
        name: 'structType',
        content: state.structType,
        type: "session"
      })
    },
    SET_CJATTRIBUTE: (state, { key, value }) => {
      // state.cjAttribute.set(key, value)
      state.cjAttribute[key] = value
      setStore({
        name: 'cjAttribute',
        content: state.cjAttribute,
        type: "session"
      })
    },
    SET_POINTHFILE: (state, value) => {
      // state.cjAttribute.set(key, value)
      state.pointHFile = value
      setStore({
        name: 'pointHFile',
        content: state.pointHFile,
        type: "session"
      })
    },
    CLEAR_CJATTRIBUTE: (state) => {
      state.cjAttribute = {};
      setStore({
        name: 'cjAttribute',
        content: state.cjAttribute,
        type: "session"
      })
    },
    CLEAR_HEADERFILE: (state) => {
      state.headerFile = {
        inputXmlParams: {},
        outputXmlParams: {},
        inputParams: [],
        outputParams: [],
        structParams: {}
      };
      setStore({
        name: 'headerFile',
        content: state.headerFile,
        type: "session"
      })
    }
    // SET_XMLENTITY: (state, params) => {
    //   state.xmlEntityVal = params
    //   setStore({
    //     name: 'xmlEntityVal',
    //     content: state.xmlEntityVal,
    //     type: "session"
    //   })
    // },
    // CLEAR_LOCK: (state) => {
    //   state.headerFile = {}
    //   removeStore({
    //     name: 'xmlEntityVal'
    //   })
    // }
  }
}

export default struct