import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import common from './modules/common'
import tags from './modules/tags'
import getters from './getters'
import struct from './modules/struct'
import CustomConfiguration from './modules/CustomConfiguration'
import textEdits from './modules/textEdits'
import gjkConsole from './modules/gjkConsole'
import comp from './modules/comp'
import infList from './modules/infList'
import chipsOfHardwarelibs from './modules/chipsOfHardwarelibs'
import closeRouter from './modules/closeRouter'

Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    user,
    common,
    tags,
    struct,
    CustomConfiguration,
 	  textEdits,
    infList,
    chipsOfHardwarelibs,
    comp,
    gjkConsole  
  },
  getters
})

export default store
