const getters = {
  tag: state => state.tags.tag,
  website: state => state.common.website,
  userInfo: state => state.user.userInfo,
  theme: state => state.common.theme,
  themeName: state => state.common.themeName,
  isShade: state => state.common.isShade,
  isCollapse: state => state.common.isCollapse,
  keyCollapse: (state, getters) => getters.screen > 1 ? getters.isCollapse : false,
  screen: state => state.common.screen,
  isLock: state => state.common.isLock,
  isFullScreen: state => state.common.isFullScreen,
  lockPasswd: state => state.common.lockPasswd,
  tagList: state => state.tags.tagList,
  tagWel: state => state.tags.tagWel,
  access_token: state => state.user.access_token,
  refresh_token: state => state.user.refresh_token,
  expires_in: state => state.user.expires_in,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  menu: state => state.user.menu,
  menuAll: state => state.user.menuAll,
  url: state => state.user.url,
  headerFile: state => state.struct.headerFile,
  xmlEntityVal: state => state.struct.xmlEntityVal,
  tmpProject: state => state.struct.tmpProject,
  structType: state => state.struct.structType,
  cjAttribute: state => state.struct.cjAttribute,
  defaultShowStruct: state => state.struct.defaultShowStruct,
  pointHFile: state => state.struct.pointHFile,
  subMapCustomConfig : state => state.CustomConfiguration.subMapCustomConfig,
  pubMapCustomConfig : state => state.CustomConfiguration.pubMapCustomConfig,
  themeData : state => state.CustomConfiguration.themeData,
  netWorkIn : state => state.CustomConfiguration.netWorkIn,
  netWorkData : state => state.CustomConfiguration.netWorkData,
  netWorkOut : state => state.CustomConfiguration.netWorkOut,
  partList : state => state.CustomConfiguration.partList,
  xmlDataMap : state => state.CustomConfiguration.xmlDataMap,
  tmpStructLength: state => state.struct.tmpStructLength,
  textLog : state => state.textEdits.textLog,
  consoleLog : state => state.gjkConsole.consoleLog,
  saveXmlMaps : state => state.comp.saveXmlMaps,
  allBaseTemplate : state => state.comp.allBaseTemplate,
  compChineseMapping : state => state.comp.compChineseMapping,
  selectBindValue : state => state.comp.selectBindValue,
  analysisBaseFile : state => state.comp.analysisBaseFile,
  cjUnitParam : state => state.comp.cjUnitParam,
  strInPointer : state => state.comp.strInPointer,
  chipsOfHardwarelibs : state => state.chipsOfHardwarelibs.chipsOfHardwarelibs,
  closeRouter : state => state.closeRouter,
  projectTreeShow : state => state.projectTreeShow.projectTreeShow,
}
export default getters
