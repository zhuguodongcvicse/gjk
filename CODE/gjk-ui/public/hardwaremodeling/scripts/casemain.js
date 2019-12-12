/**
 * 屎山，很多能复用的方法没提，冗余for循环，等等，维护的大哥祝你好运
 */
var caseArr;
var board_json = [];
var caseList = [];
var fJson = []
var bJson = []
var graphList = {fJson, bJson}
var currentBoard
var chipListTemp = []
var allChipToFlow = []
var initialLocation
var moveLocation
var backAllCaseJsonTemp
var linkList = []
var linkGraphList = {datas: []}
var clickCheckedChip
var ChipsWithIPs = []
var caseID = -1

Q.registerImage('rack', 'images/Crate.svg'); //这里可以修改成：机箱.svg，但是位置大小需要做调整，你可以自己修改
Q.registerImage('card', 'images/BeforeTheBoard.svg');
Q.registerImage('behindcard', 'images/AfterTheBoard.svg');
Q.registerImage('cell', 'images/Chip.svg');
Q.registerImage('optical', 'images/OpticalFiberMouth.svg');
Q.registerImage('port', 'images/RoundMouth.svg');
Q.registerImage('serial', 'images/SerialPort.svg');
Q.registerImage('ePort', 'images/InternetAccess.svg');

// 子接收父参数
function handleMessageFromParent(event) {
  // console.log("event.data.params", event.data.params)
  switch (event.data.cmd) {
    case 'getCase':
      caseArr = event.data.params;
      for (const i in caseArr) {
        var caseTemp = JSON.parse(caseArr[i].frontCase)
        caseTemp.datas[0].json.properties.bdNum = caseArr[i].bdNum
        caseArr[i].frontCase = JSON.stringify(caseTemp)
      }
      // console.log("caseArr", caseArr)
      allJson = caseArr;
      //console.log("event.data.params",event.data.params)
      //console.log(caseArr[0].boardJson)
      //主板json
      for (var i = 0; i < caseArr.length; i++) {
        board_json[i] = {
          image: 'images/CaseIco.jpg',
          json: caseArr[i],
          ondrop: 'ondropLoadJSON', //do any thing by your self when drop into the graph
          showLabel: true
        }
      }
      init();
  }
};

function RectElement() {
  Q.doSuperConstructor(this, RectElement, arguments);
  this.resizable = false;
  this.rotatable = false;
  this.addOutProperty('width');
  this.addOutProperty('height');
  this.addOutProperty('radius');
  this.anchorPosition = Q.Position.LEFT_TOP;
  this.setStyle(Q.Styles.SHAPE_FILL_COLOR, "#EEE");
  this.setStyle(Q.Styles.SHAPE_STROKE, 1);
  this.setStyle(Q.Styles.SHAPE_STROKE_STYLE, '#555');
  this.setStyle(Q.Styles.LABEL_PADDING, 5);
  this.image = null;
}

RectElement.prototype = {
  setBounds: function (x, y, width, height, relativeParent) {
    x = x || 0, y = y || 0;
    if (relativeParent && this.parent) {
      x += this.parent.x, y += this.parent.y;
    }
    this.width = width, this.height = height;
    this.x = x, this.y = y;
    if (!this.image || this.image instanceof Q.Path) {
      this.image = Q.Shapes.getRect(0, 0, width, height, this.radius || 0);
    } else {
      this.size = {
        width: width,
        height: height
      }
    }
  },
  getBounds: function () {
    return {
      x: this.x,
      y: this.y,
      width: this.width,
      height: this.height
    }
  }
}

Q.extend(RectElement, Q.Node);
Q.RectElement = RectElement;
Q.loadClassPath(RectElement, 'Q.RectElement')
//接收父页面数据监听
window.addEventListener("message", this.handleMessageFromParent) // 子接收方式二参数
var postMessageParentData = {
  cmd: "",//用于switch 判断
  params: []//具体参数
};

function init() {
  $('#editor').graphEditor({
    images: [{
      name: '机箱',
      images: board_json
    }
    ],
    callback: initEditor,
  });
}

var EVENT_CREATE_ELEMENT_BY_JSON = 'create.element.by.json';


//拖拽触发的方法
function ondropLoadJSON(evt, graph, center, options) {
  //机箱ID赋值
  caseID++
  //声明随机唯一标识
  let uuidRandom = uuid(15, 62)
  //转换成对象
  let frontjson = JSON.parse(options.json.frontCase);
  let bJsonObj = JSON.parse(options.json.backCase);
  //机箱赋值唯一标识
  frontjson.datas[0].json.properties.uniqueId = uuidRandom
  frontjson.datas[0].json.properties.id = caseID
  //找到机箱中前板卡
  for (const i in frontjson.datas[0].json.properties.frontBoardList) {
    //找到前板卡中的芯片
    if (frontjson.datas[0].json.properties.frontBoardList[i].chipList != null && frontjson.datas[0].json.properties.frontBoardList[i].chipList.length !== 0) {
      for (const j in frontjson.datas[0].json.properties.frontBoardList[i].chipList) {
        //给芯片赋值唯一标识，拼接上机箱的唯一标识
        frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].uniqueId
        if (frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].infOfChipList !== undefined) {
          for (const k in frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].infOfChipList) {
            frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId
          }
        }
      }
    }
    //内部互联解耦添加唯一标识
    if (frontjson.datas[0].json.properties.frontBoardList[i].InternalLink != null && frontjson.datas[0].json.properties.frontBoardList[i].InternalLink.length !== 0) {
      for (const j in frontjson.datas[0].json.properties.frontBoardList[i].InternalLink) {
        if (frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][0].uniqueId.indexOf(uuidRandom) === -1) {
          frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][0].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][0].uniqueId
        }
        if (frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][1].uniqueId.indexOf(uuidRandom) === -1) {
          frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][1].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].InternalLink[j][1].uniqueId
        }
      }
    }
    //给板卡拼接上机箱的唯一标识
    frontjson.datas[0].json.properties.frontBoardList[i].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].uniqueId
  }
  //校验机箱上的图元不许移动
  for (index in frontjson.datas) {
    let image = frontjson.datas[index].json.image;
    if (image === 'images/Chip.svg' || image === 'images/OpticalFiberMouth.svg' || image === 'images/InternetAccess.svg'
      || image === 'images/RoundMouth.svg' || image === 'images/SerialPort.svg' || image === 'images/BeforeTheBoard.svg'
      || image === 'images/AfterTheBoard.svg') {
      frontjson.datas[index].json.movable = false;
    }
    //给数据中的芯片拼接唯一标识
    if (frontjson.datas[index].json.properties.chipName != null) {
      frontjson.datas[index].json.properties.uniqueId = uuidRandom + '_' + frontjson.datas[index].json.properties.uniqueId
    }
  }
  let json = JSON.stringify(frontjson)
  //校验后机箱上的图元不许移动
  for (const index in bJsonObj.datas) {
    let image = bJsonObj.datas[index].json.image;
    if (image === 'images/Chip.svg' || image === 'images/OpticalFiberMouth.svg' || image === 'images/InternetAccess.svg'
      || image === 'images/RoundMouth.svg' || image === 'images/SerialPort.svg' || image === 'images/BeforeTheBoard.svg'
      || image === 'images/AfterTheBoard.svg' || image === 'rack') {
      bJsonObj.datas[index].json.movable = false;
    }
  }
  //后板卡和后板卡的接口拼接机箱的唯一标识
  //机箱背面赋值唯一标识
  bJsonObj.datas[0].json.properties.uniqueId = uuidRandom
  bJsonObj.datas[0].json.properties.id = caseID
  for (const i in bJsonObj.datas[0].json.properties.backBoardList) {
    //判断后板卡是否已经拼接机箱唯一标识
    if (bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId.indexOf(uuidRandom) === -1) {
      //后板卡拼接机箱唯一标识
      bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId
    }
    for (const j in bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList) {
      //判断后板卡上的接口是否已经拼接机箱唯一标识
      if (bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId.indexOf(uuidRandom) === -1) {
        //后板卡上的接口拼接机箱唯一标识
        bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId
      }
    }
  }
  //给背面机箱数据的接口拼接唯一标识
  for (const i in bJsonObj.datas) {
    if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.infName != null) {
      if (bJsonObj.datas[i].json.properties.uniqueId.indexOf(uuidRandom) === -1) {
        bJsonObj.datas[i].json.properties.uniqueId = uuidRandom + '_' + bJsonObj.datas[i].json.properties.uniqueId
      }
    }
    if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.caseName != null) {
      bJsonObj.datas[i].json.properties.uniqueId = uuidRandom
      //机箱ID赋值
      bJsonObj.datas[i].json.properties.ID = caseID
    }
  }
  // console.log("frontjson", frontjson)
  // console.log("bJsonObj", bJsonObj)
  //console.log("uuidRandom",uuidRandom)
  //序列化
  options.json.frontCase = JSON.stringify(frontjson)
  options.json.backCase = JSON.stringify(bJsonObj)
  if (!json) {
    return;
  }
  let result = loadJSONInParent(graph, json, center);

  if (!result || !result.length) {
    return;
  }
  var roots = [];
  result.forEach(function (e) {
    if (e.parent === graph.currentSubnetwork) {
      roots.push(e);
    }
  })
  //给前板卡、芯片、接口拼接机箱唯一标识
  roots[0].properties.uniqueId = uuidRandom
  result[0].properties.uniqueId = uuidRandom
  result[0].properties.id = caseID
  /* for (const i in result[0].properties.frontBoardList) {
     if (result[0].properties.frontBoardList[i].uniqueId.indexOf(uuidRandom) === -1) {
       result[0].properties.frontBoardList[i].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].uniqueId
     }
     for (const j in result[0].properties.frontBoardList[i].chipList) {
       if (result[0].properties.frontBoardList[i].chipList[j].uniqueId.indexOf(uuidRandom) === -1) {
         result[0].properties.frontBoardList[i].chipList[j].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].chipList[j].uniqueId
       }
       for (const k in result[0].properties.frontBoardList[i].chipList[j].infOfChipList) {
         if (result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId.indexOf(uuidRandom) === -1) {
           result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId
         }
       }
     }
     //内部互联解耦添加唯一标识
     if (result[0].properties.frontBoardList[i].InternalLink != null && result[0].properties.frontBoardList[i].InternalLink.length !== 0) {
       for (const j in result[0].properties.frontBoardList[i].InternalLink) {
         if (result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId.indexOf(uuidRandom) === -1) {
           result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId
         }
         if (result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId.indexOf(uuidRandom) === -1) {
           result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId
         }
       }
     }
   }*/
  // console.log("options",options)
  //放到数组中
  caseList.push(options.json)
  graphList.fJson.push(JSON.parse(caseList[caseList.length - 1].frontCase))
  graphList.bJson.push(JSON.parse(caseList[caseList.length - 1].backCase))
  initialLocation = result[0].location
  //放到画布上初始坐标赋值
  for (const i in graphList.fJson) {
    if (graphList.fJson[i].datas[0].json.properties.uniqueId === result[0].properties.uniqueId) {
      for (const j in graphList.fJson[i].datas) {
        if (graphList.fJson[i].datas[j].json.location != null) {
          if (graphList.fJson[i].datas[j].json.location.json != null) {
            graphList.fJson[i].datas[j].json.location.json.x = graphList.fJson[i].datas[j].json.location.json.x + initialLocation.x
            graphList.fJson[i].datas[j].json.location.json.y = graphList.fJson[i].datas[j].json.location.json.y + initialLocation.y
          } else {
            graphList.fJson[i].datas[j].json.location.x = graphList.fJson[i].datas[j].json.location.x + initialLocation.x
            graphList.fJson[i].datas[j].json.location.y = graphList.fJson[i].datas[j].json.location.y + initialLocation.y
          }
        }
      }
    }
  }
  for (const i in graphList.bJson) {
    if (graphList.bJson[i].datas[0].json.properties.uniqueId === result[0].properties.uniqueId) {
      for (const j in graphList.bJson[i].datas) {
        if (graphList.bJson[i].datas[j].json.location != null) {
          if (graphList.bJson[i].datas[j].json.location.json != null) {
            graphList.bJson[i].datas[j].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x + initialLocation.x
            graphList.bJson[i].datas[j].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y + initialLocation.y
          } else {
            graphList.bJson[i].datas[j].json.location.x = graphList.bJson[i].datas[j].json.location.x + initialLocation.x
            graphList.bJson[i].datas[j].json.location.y = graphList.bJson[i].datas[j].json.location.y + initialLocation.y
          }
        }
      }
    }
  }
  // graphList.fJson.push(frontjson)
  // graphList.bJson.push(bJsonObj)
  // console.log("caseList", caseList)
  // console.log("graphList", graphList)
  // console.log("graphList", graphList)
  //console.log('roots',roots)
  // console.log("result", result)
  graph.interactionDispatcher.onEvent({
    kind: EVENT_CREATE_ELEMENT_BY_JSON,
    datas: result,
    roots: roots,
    event: evt
  })

}

//生成UUID
function uuid(len, radix) {
  var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split("");
  var uuid = [], i;
  radix = radix || chars.length;
  if (len) {
    for (i = 0; i < len; i++)
      uuid[i] = chars[0 | (Math.random() * radix)];
  } else {
    var r;
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
    uuid[14] = "4";
    for (i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | (Math.random() * 16);
        uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
      }
    }
  }
  return uuid.join("");
}

function loadJSONInParent(graph, json, center, parent) {
  parent = parent || graph.currentSubnetwork;

  var result = graph.parseJSON(json, {transform: false});
  if (!result) {
    return
  }
  var bounds = new Q.Rect();
  result.forEach(function (e) {
    if (e instanceof Q.Node) {
      bounds.add(e.x, e.y);
    }
    if (parent && !e.parent) {
      e.parent = parent;
    }
  })
  if (!bounds.isEmpty()) {
    var xOffset = center.x - bounds.cx;
    var yOffset = center.y - bounds.cy;

    result.forEach(function (e) {
      if (e.location) {
        e.x += xOffset;
        e.y += yOffset;
      }
    })
  }
  return result;
}

var graph;

function createNode(options, parent) {
  var item = new RectElement();
  if (options.image) {
    item.image = options.image;
  } else {
    options.radius = options.radius || 5;
    item.setStyle(Q.Styles.SHAPE_FILL_COLOR, options.fillColor || '#EEE');
  }
  item.setBounds(options.x || 0, options.y || 0, options.width || 40, options.height || 40);
  graph.addElement(item);
  if (parent) {
    item.parent = item.host = parent;
  }
  return item;
}

function getSlot(parent, id) {
  if (parent._slots) {
    return parent._slots[id];
  }
}

function addSlot(parent, id, x, y, width, height, childType) {
  var slot = createNode({
    x: parent.x + x,
    y: parent.y + y,
    width: width,
    height: height
  }, parent);
  slot.setStyle(Q.Styles.SHAPE_FILL_COLOR, null);
  slot.set('type', 'slot');
  slot.set('childType', childType);
  slot.movable = false;
  slot.selectable = false;
  // slot.setStyle(Q.Styles.SHAPE_STROKE_STYLE, Q.toColor(0x01000000));
  // slot.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
  // slot.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
  if (!parent._slots) {
    parent._slots = {};
  }
  return parent._slots[id] = slot;
}

function createRack() {
  var width = 700,
    height = 520;
  // 468,295
  var rack = createNode({
    image: 'rack',
    width: width,
    height: height
  });
  var startX = 32,
    startY = 23,
    gap = 1.8,
    itemWidth = 62,
    itemHeight = 400,
    count = 10;
  var i = 0;
  while (i++ < count) {
    addSlot(rack, i, startX, startY, itemWidth, itemHeight, 'card');
    startX += itemWidth + gap;
  }
  return rack;
}

//回显绘画画布,监控
function initEditor(editor) {

  graph = editor.graph;
  editor.toolbox.hideDefaultGroups();
  graph.styles = {
    'selection.type': Q.Consts.SELECTION_TYPE_BORDER_RECT
  }

  //全屏按钮
  var toolbarDiv = editor.toolbar;
  var button = document.createElement('button');
  button.textContent = '全屏';
  button.className = 'boarddesign_board_14s';
  toolbarDiv.appendChild(button)
  button.onclick = function () {
    toggleFullScreen()
  }
  initToolbar();

  //以下为切换编辑模式
  function setEditable(editable) {
    graph.editable = editable;
    var toolbox = editor.toolbox.html;
    editor.propertyPane.setVisible(editable && editor.propertyPane.datas)

    if (editable) {
      toolbox.style.display = '';
      toolbox.setAttribute('data-options', toolbox.getAttribute('old-data-options'));
    } else {
      toolbox.style.display = 'none';
      toolbox.setAttribute('old-data-options', toolbox.getAttribute('data-options'));
      toolbox.setAttribute('data-options', null);
    }

    //   graph.interactionMode = null;
    // graph.interactionMode = editable ? Q.Consts.INTERACTION_MODE_DEFAULT : Q.Consts.INTERACTION_MODE_VIEW;

    $('.layout').borderLayout();
    graph.updateViewport();
  }

  setEditable(true);
  var toolbarDiv = editor.toolbar;
  /*    var button = Q.createButton({
       type: 'checkbox',
       name: '编辑',
       selected: graph.editable,
       action: function(evt){
         setEditable(evt.target.checked);
       }
     })
     toolbarDiv.appendChild(button) */
  //保存
  var linkMap = new Map()
  var toolbarDiv = editor.toolbar;
  var buttonOfSave = document.createElement('button');
  var buttonOfDelete = document.createElement('button');
  var cpuNodeID = 0
  var frontCaseForDeployment
  buttonOfSave.textContent = '保存硬件建模';
  buttonOfDelete.textContent = '清空硬件建模';
  buttonOfSave.className = 'boarddesign_board_14s';
  buttonOfDelete.className = 'boarddesign_board_14s';
  toolbarDiv.appendChild(buttonOfDelete)
  toolbarDiv.appendChild(buttonOfSave)
  buttonOfDelete.onclick = function (evt) {
    if (graph.name !== '正面视图') {
      graph.name = '正面视图'
      setEditable(true);
    }
    graphList.fJson = []
    graphList.bJson = []
    caseList = []
    allChipToFlow = []
    backAllCaseJsonTemp = undefined
    linkList = []
    linkGraphList = {datas: []}
    ChipsWithIPs = []
    caseID = -1
    graph.clear()
  }
  buttonOfSave.onclick = function (evt) {
    if (graphList.fJson.length === 0) {
      alert("请添加机箱")
      return;
    }
    var graphName = graph.name
    // console.log("graphName", graphName)
    if (graphName == null) {
      frontCaseForDeployment = graph.toJSON()
      //配置的ip nodeID
      // console.log("graphList",graphList)
      // return;
      setNodeIDAndIP()
      let ifSetIPSuccess = checkIP()
      if (!ifSetIPSuccess) {
        return
      }

      // console.log("graph", graph.toJSON())
      //连线关系
      createLinkData()
      graphList.fJson = JSON.parse(JSON.stringify(graphList.fJson))
      var linkArr = Array.from(linkMap)
      var linkStr = JSON.stringify(linkArr)
      graphList.link = JSON.stringify([])
      frontCaseForDeployment = JSON.stringify(frontCaseForDeployment)
      postMessageParentData.cmd = "submitCaseJSON";
      postMessageParentData.params = [graphList, linkStr, allChipToFlow, frontCaseForDeployment]
      window.parent.postMessage(postMessageParentData, "*")
      // console.log("postMessageParentData--first", postMessageParentData)
      return
    } else if (graphName == '背部视图') {
      //配置的ip nodeID
      setNodeIDAndIP()
      let ifSetIPSuccess = checkIP()
      if (!ifSetIPSuccess) {
        return
      }
      //设置linkList
      backAllCaseJsonTemp = graph.toJSON()
      // console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
      setLinkList()
      //删除连线
      /*for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
        if (backAllCaseJsonTemp.datas[i].json.properties == null) {
          removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
          i--
        }
      }*/
      // console.log("linkList",linkList)
      // console.log("linkGraphList",linkGraphList)
      for (const i in linkList) {
        //创建连线
        let edgejson = createEdgejson()
        linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
        linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
        edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
        edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
        // console.log("edgejson",edgejson)
        let linkGraphListStr = JSON.stringify(linkGraphList)
        if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) == -1) {
          linkGraphList.datas.push(linkList[i][0])
          linkGraphList.datas.push(linkList[i][1])
          linkGraphList.datas.push(edgejson)
        }
        // console.log("linkGraphList",linkGraphList)
      }
      linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
      // console.log("linkfraphlist",linkGraphList)
      //连线关系
      createLinkData()
      postMessageToVue()
      return
    } else if (graphName == '正面视图') {
      frontCaseForDeployment = graph.toJSON()
      //配置的ip nodeID
      setNodeIDAndIP()
      let ifSetIPSuccess = checkIP()
      if (!ifSetIPSuccess) {
        return
      }
      //第一次切换回正面就点保存时执行以下，重新划线保存，能提出来但是老子懒得提
      if (linkGraphList.datas.length == 0) {
        linkList = []
        // console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
        //设置linkList
        if (backAllCaseJsonTemp !== undefined) {
          setLinkList()
        }
        // console.log("linkList", linkList)
        //删除连线
        if (backAllCaseJsonTemp !== undefined) {
          for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
            if (backAllCaseJsonTemp.datas[i].json.properties == null) {
              removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
              i--
            }
          }
        }
        //创建连线
        if (linkList.length != 0) {
          for (const i in linkList) {
            let edgejson = createEdgejson()
            linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
            linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
            edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
            edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
            // console.log("edgejson",edgejson)
            var linkGraphListStr = JSON.stringify(linkGraphList)
            if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) == -1) {
              linkGraphList.datas.push(linkList[i][0])
              linkGraphList.datas.push(linkList[i][1])
              linkGraphList.datas.push(edgejson)
            }
            linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
            // console.log("linkGraphList",linkGraphList)
          }
        }
        //连线关系
        createLinkData()
        // console.log("linkMap", linkMap)
      }
      postMessageToVue()
      return
    }
  }

  function initToolbar() {
    var toolbar = editor.toolbar;
    var button = document.createElement('button');
    button.textContent = '切换视图';
    button.className = 'boarddesign_board_14s';
    button.onclick = function () {
      if (graphList.bJson.length == 0) {
        alert("尚未添加机箱")
        return
      }
      //进入正面
      if (graph.name == '背部视图') {
        graph.name = '正面视图';
        backAllCaseJsonTemp = graph.toJSON()
        // console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
        //找到连线的两个接口放到数组
        setLinkList()
        // console.log("linkList", linkList)
        graph.clear();
        //画出正面机箱
        for (var i = 0; i < graphList.fJson.length; i++) {
          graph.parseJSON(graphList.fJson[i], {transform: false})
        }
        setEditable(true);
        // console.log("11", graph.name)
        return;
      }
      //进入背面
      if (graph.name == null) {
        frontCaseForDeployment = graph.toJSON()
        graph.clear();
        graph.name = '背部视图'
        //画出背面机箱
        for (const i in graphList.bJson) {
          graph.parseJSON(graphList.bJson[i], {transform: false});
        }
        setEditable(false);
      }
      //进入背面
      if (graph.name == '正面视图') {
        graph.name = '背部视图';
        // console.log("33", graph.name)
        //部署图要用到的数据
        frontCaseForDeployment = graph.toJSON()
        // console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
        //找到连线的两个接口放到数组
        // setLinkList()
        //删除qunee自己生成的连线数据
        if (backAllCaseJsonTemp !== undefined) {
          for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
            if (backAllCaseJsonTemp.datas[i].json.properties == null) {
              removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
              i--
            }
          }
        }

        //找到机箱内部连线，放到数组为绘画准备数据，同时放到map为生成xml准备数据
        //遍历机箱数组
        for (const i in graphList.bJson) {
          //遍历某机箱背面数据
          for (const j in graphList.bJson[i].datas) {
            //如果是null则是连线数据
            if (graphList.bJson[i].datas[j].json.properties === undefined) {
              //再次遍历该机箱
              for (const k in graphList.bJson) {
                for (const m in graphList.bJson[k].datas) {
                  //找到起始接口
                  if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[k].datas[m]._refId) {
                    // console.log("起始接口-机箱内部", graphList.bJson[k].datas[m]._refId)
                    //再次遍历该机箱
                    for (const p in graphList.bJson[k].datas) {
                      if (graphList.bJson[k].datas[p].json.properties != null) {
                        //找到连线终止接口
                        if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[k].datas[p]._refId) {
                          // console.log("终止接口-机箱内部", graphList.bJson[k].datas[p]._refId)
                          //连线数据转成字符串
                          var linkListStr = JSON.stringify(linkList)
                          //从连线数据字符串中查找起始接口的唯一标识
                          if (linkListStr.indexOf(graphList.bJson[k].datas[m].json.properties.uniqueId) === -1) {
                            //将该连线的两个接口放到数组
                            linkList.push([graphList.bJson[k].datas[m], graphList.bJson[k].datas[p]])
                            //连线关系生成xml数据
                            linkMap.set(graphList.bJson[k].datas[m].json.properties, graphList.bJson[k].datas[p].json.properties)
                            break
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        // console.log("linkList", linkList)
        //删除机箱内部连线
        for (const i in graphList.bJson) {
          for (let j = 0; j < graphList.bJson[i].datas.length; j++) {
            if (graphList.bJson[i].datas[j].json.properties == null) {
              removeByValue(graphList.bJson[i].datas, graphList.bJson[i].datas[j])
              j--
            }
          }
        }
        graph.clear();
        //画出背面机箱
        for (const i in graphList.bJson) {
          graph.parseJSON(graphList.bJson[i], {transform: false})
        }
        // graph.parseJSON(backAllCaseJsonTemp)
        //创建自己的连线
        //赋值连线的refid，将连线和两个重新画出的接口放到数组
        // console.log("linkList",linkList)
        for (const i in linkList) {
          let edgejson = createEdgejson()
          //给接口重新生成refId
          linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
          linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
          //将生成的refId赋值给创建的连线，形成依附关系
          edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
          edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
          // console.log("edgejson",edgejson)
          //将自己生成的要绘画的连线数据，即符合qunee数据格式的数据，转成字符串
          var linkGraphListStr = JSON.stringify(linkGraphList)
          //连线数据的其实接口不重复时执行
          if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) === -1) {
            //将起始接口、终止接口、连线放在数据中
            linkGraphList.datas.push(linkList[i][0])
            linkGraphList.datas.push(linkList[i][1])
            linkGraphList.datas.push(edgejson)
          }
        //深拷贝
          linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
        }
        //画出接口和连线
        graph.parseJSON(linkGraphList)
        // console.log("linkGraphList", linkGraphList)
        setEditable(false);
        return;
      }
    }

    toolbar.appendChild(button)
    //网状画布
    //	var graph = editor.graph;
    //不可改变形状大小
    graph.editable = false;

    //	var defaultStyles = graph.styles = {};
    //	defaultStyles[Q.Styles.ARROW_TO] = false;

    var background = new GridBackground(graph);

    var currentCell = 10;

    function snapToGrid(x, y) {
      var gap = currentCell;
      x = Math.round(x / gap) * gap;
      y = Math.round(y / gap) * gap;
      return [x, y];
    }

  }

  //两线数据放到map，为生成xml准备数据
  function createLinkData() {
    for (const i in graphList.bJson) {
      for (const j in graphList.bJson[i].datas) {
        if (graphList.bJson[i].datas[j].json.properties == null) {
          for (const k in graphList.bJson[i].datas) {
            if (graphList.bJson[i].datas[k].json.properties != null) {
              if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[i].datas[k]._refId) {
                for (const m in graphList.bJson[i].datas) {
                  if (graphList.bJson[i].datas[m].json.properties != null) {
                    if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[i].datas[m]._refId) {
                      //查看是否重复，重复则不添加
                      var linkMapStr = JSON.stringify(Array.from(linkMap))
                      if (linkMapStr.indexOf(graphList.bJson[i].datas[k].json.properties.uniqueId) == -1) {
                        linkMap.set(graphList.bJson[i].datas[k].json.properties, graphList.bJson[i].datas[m].json.properties)
                        break
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  //设置nodeID和IP
  function setNodeIDAndIP() {
    for (const n in graphList.fJson) {
      for (const i in graphList.fJson[n].datas) {
        if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
          for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
            if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
              for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
                graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID = cpuNodeID++
                //给部署图所需数据赋nodeid
                for (const p in frontCaseForDeployment.datas) {
                  if (frontCaseForDeployment.datas[p].json.properties.chipName != null
                    && frontCaseForDeployment.datas[p].json.properties.uniqueId == graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId) {
                    frontCaseForDeployment.datas[p].json.properties.nodeID = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
                    frontCaseForDeployment.datas[p].json.properties.IP = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].IP
                  }
                }
                frontCaseForDeployment.datas = JSON.parse(JSON.stringify(frontCaseForDeployment.datas))
                //流程建模和系统配置所需的芯片数据
                allChipToFlow.push(graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k])
                allChipToFlow = JSON.parse(JSON.stringify(allChipToFlow))
              }
            }
          }
        }
      }
    }
  }

  function checkIP() {
    for (const i in graphList.fJson) {
      for (const j in graphList.fJson[i].datas) {
        if (graphList.fJson[i].datas[j].json.properties != null && graphList.fJson[i].datas[j].json.properties.caseName != null) {
          for (const k in graphList.fJson[i].datas[j].json.properties.frontBoardList) {
            if (graphList.fJson[i].datas[j].json.properties.frontBoardList[k].chipList != null) {
              for (const m in graphList.fJson[i].datas[j].json.properties.frontBoardList[k].chipList) {
                //如果存在未配置IP的芯片则弹出提示
                if (graphList.fJson[i].datas[j].json.properties.frontBoardList[k].chipList[m].IP == null || graphList.fJson[i].datas[j].json.properties.frontBoardList[k].chipList[m].IP == '') {
                  alert("存在未配置IP的芯片")
                  return false
                }
              }
            }
          }
        }
      }
    }
    return true;
  }

  //连线的接口放到数组，为绘画准备数据，同时放到map
  function setLinkList() {
    //遍历背面画布的数据
    for (const i in backAllCaseJsonTemp.datas) {
      //如果是null则说明是连线数据
      if (backAllCaseJsonTemp.datas[i].json.properties === undefined) {
        //再次遍历背面画布数据
        for (const j in backAllCaseJsonTemp.datas) {
          //找到连线的起始接口
          if (backAllCaseJsonTemp.datas[i].json.from._ref == backAllCaseJsonTemp.datas[j]._refId) {
            // console.log("起始接口-机箱外部", backAllCaseJsonTemp.datas[j])
            //再次遍历此数据
            for (const k in backAllCaseJsonTemp.datas) {
              //找到连线终止接口
              if (backAllCaseJsonTemp.datas[k].json.properties != null && backAllCaseJsonTemp.datas[i].json.to._ref == backAllCaseJsonTemp.datas[k]._refId) {
                // console.log("末端接口-机箱外部", backAllCaseJsonTemp.datas[k])
                //截取两个接口的唯一标识
                var startStr = backAllCaseJsonTemp.datas[j].json.properties.uniqueId.slice(0, backAllCaseJsonTemp.datas[j].json.properties.uniqueId.length)
                var endStr = backAllCaseJsonTemp.datas[k].json.properties.uniqueId.slice(0, backAllCaseJsonTemp.datas[k].json.properties.uniqueId.length)
                //判断是否相等
                if (startStr != endStr) {
                  //将连线数据转成字符串
                  var linkListStr = JSON.stringify(linkList)
                  //判断该连线数据中是否有起始接口
                  if (linkListStr.indexOf(backAllCaseJsonTemp.datas[j].json.properties.uniqueId) === -1) {
                    //将两个接口放到数组中
                    linkList.push([backAllCaseJsonTemp.datas[j], backAllCaseJsonTemp.datas[k]])
                    //保存到xml的连线数据
                    linkMap.set(backAllCaseJsonTemp.datas[j].json.properties, backAllCaseJsonTemp.datas[k].json.properties)
                    break
                  }
                }
              }
            }
            break
          }
        }
      }
    }
  }

  //创建连线数据
  function createEdgejson() {
    var edgejson = {
      "_className": "Q.Edge",
      "json": {
        "zIndex": 200,
        "styles": {
          "edge.color": "#2D97F9",
          "arrow.to": true,
          //"arrow.to.size": 1.8,
        },
        "from": {
          "_ref": ''
        },
        "to": {
          "_ref": ''

        },
        "edgeType": "extend.top",
        "properties": {
          "type": "edge"
        },
      }
    }
    return edgejson
  }

  //发送数据到vue
  function postMessageToVue() {
    graphList.fJson = JSON.parse(JSON.stringify(graphList.fJson))
    // console.log("graphList",graphList)
    var linkArr = Array.from(linkMap)
    var linkStr = JSON.stringify(linkArr)
    // console.log("allChipToFlow", allChipToFlow)
    // console.log("linkArr", linkArr)
    // console.log("graphList.fJson", graphList.fJson)
    // console.log("graphList.bJson[0]", graphList.bJson[0])
    /* for (const i in graphList.fJson) {
      graphList.fJson[i] = JSON.stringify(graphList.fJson[i])
      graphList.bJson[i] = JSON.stringify(graphList.bJson[i])
    } */
    graphList.link = linkGraphList
    graphList.link = JSON.stringify(graphList.link)
    frontCaseForDeployment = JSON.stringify(frontCaseForDeployment)
    postMessageParentData.cmd = "submitCaseJSON";
    postMessageParentData.params = [graphList, linkStr, allChipToFlow, frontCaseForDeployment]
    window.parent.postMessage(postMessageParentData, "*")
  }

  graph.popupmenu.getMenuItems = function (graph, data, evt) {
    if (data) {
      if (data.from != null) {
        return [
          {
            text: '删除连线', action: function () {
              var data = graph.getElement(evt);
              // console.log("data",data)
              // graph.removeSelectionByInteraction(data);
              let fromInfStr = data.from.properties.uniqueId.slice(0, 15)
              let toInfStr = data.to.properties.uniqueId.slice(0, 15)
              if (fromInfStr === toInfStr) {
                return
              }
              graph.removeElement(data);
            }
          },
        ];
      }
    }
  }

  //删除
  graph.removeSelectionByInteraction = false;
  graph.removeSelectionByInteraction = function (evt) {
    var selection = this.selectionModel.datas;
    // console.log("selection",selection)
    if (!selection || selection.length == 0) {
      return false;
    }
    for (var i in selection) {
      if (selection[i].properties.type == "case") {
        Q.confirm("是否 确认删除", function () {
          var selection = this.removeSelection();
          // console.log("selection", selection)
          //删除初始机箱数组中的数据
          for (const i in graphList.bJson) {
            if (graphList.bJson[i].datas[0].json.properties.uniqueId == selection[0].properties.uniqueId) {
              removeByValue(caseList, caseList[i])
              removeByValue(graphList.fJson, graphList.fJson[i])
              removeByValue(graphList.bJson, graphList.bJson[i])
            }
          }
          //删除连线数组中的数据
          if (linkList.length != 0) {
            for (let i = 0; i < linkList.length; i++) {
              if (linkList[i] != null && linkList[i][0].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                removeByValue(linkList, linkList[i])
              }
              if (linkList[i] != null && linkList[i][1].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                removeByValue(linkList, linkList[i])
              }
            }
          }
          //找到绘画数组中和删除的机箱有关系的联系及接口，赋予状态码准备删除
          if (linkGraphList.datas.length != 0) {
            for (const i in linkGraphList.datas) {
              if (linkGraphList.datas[i]._className == "Q.RectElement" && linkGraphList.datas[i].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                for (const j in linkGraphList.datas) {
                  if (linkGraphList.datas[j]._className == "Q.Edge") {
                    if (linkGraphList.datas[j].json.from._ref == linkGraphList.datas[i]._refId) {
                      for (const k in linkGraphList.datas) {
                        if (linkGraphList.datas[k]._className == "Q.RectElement" && linkGraphList.datas[j].json.to._ref == linkGraphList.datas[k]._refId) {
                          linkGraphList.datas[k].waitDelete = 0
                        }
                      }
                      linkGraphList.datas[j].waitDelete = 0
                    }
                    if (linkGraphList.datas[j].json.to._ref == linkGraphList.datas[i]._refId) {
                      for (const k in linkGraphList.datas) {
                        if (linkGraphList.datas[k]._className == "Q.RectElement" && linkGraphList.datas[j].json.from._ref == linkGraphList.datas[k]._refId) {
                          linkGraphList.datas[k].waitDelete = 0
                        }
                      }
                      linkGraphList.datas[j].waitDelete = 0
                    }
                  }
                }
                linkGraphList.datas[i].waitDelete = 0
              }
            }
          }
          //删除有状态码的接口和连线
          for (let i = 0; i < linkGraphList.datas.length; i++) {
            if (linkGraphList.datas[i].waitDelete != null) {
              removeByValue(linkGraphList.datas, linkGraphList.datas[i])
              i--
            }
          }
          //删除map中的数据
          linkMap.forEach(function (value, key) {
            if (key.uniqueId.indexOf(selection[0].properties.uniqueId) !== -1) {
              linkMap.delete(key)
            }
            if (value.uniqueId.indexOf(selection[0].properties.uniqueId) !== -1) {
              linkMap.delete(key)
            }
          });
          //第一次切换回正面，删除暂存背面机箱数据中的接口和连线
          // console.log("backAllCaseJsonTemp",backAllCaseJsonTemp)
          if (backAllCaseJsonTemp !== undefined) {
            if (backAllCaseJsonTemp.datas.length !== 0) {
              for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
                if (backAllCaseJsonTemp.datas[i].json.properties == null || backAllCaseJsonTemp.datas[i]._className === "Q.Edge") {
                  removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
                  i--
                }
              }
            }
            if (backAllCaseJsonTemp.datas.length !== 0) {
              for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
                if (backAllCaseJsonTemp.datas[i].json.properties != null && backAllCaseJsonTemp.datas[i].json.properties.infName != null
                  && backAllCaseJsonTemp.datas[i].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) !== -1) {
                  removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
                  i--
                }
              }
            }
          }
          caseID--
          // console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
          // console.log("caseList", caseList)
          // console.log("graphList", graphList)
          // console.log("linkList", linkList)
          // console.log("linkGraphList", linkGraphList)
          // console.log("linkMap", linkMap)
        }, this);
      }
    }
  }

  //右侧属性面板
  var propertySheet = editor.propertyPane;
  propertySheet.showDefaultProperties = false;

  //自定义属性面板
  propertySheet.getCustomPropertyDefinitions = function (data) {
    var type = data.get('type');
    var image = data.image;
    // console.log("propertySheet",propertySheet)
    // console.log("data", data)
    if (data.properties.chipName != null) {
      clickCheckedChip = data.properties
    }
    // console.log("type",type)
    //这里可以获得当前点击的图元对象
    graph.onclick = function (evt) {
      var data = graph.getElement(evt);
      // console.log("data", data)
      if (typeof data == 'undefined') {
        return
      }
      if (data.properties.chipName != null) {
        data.set('chipName', data.properties.chipName);
        data.set('coreNum', data.properties.coreNum);
        data.set('memSize', data.properties.memSize);
        data.set('hrTypeName', data.properties.hrTypeName);
        data.set('recvRate', data.properties.recvRate);
      }
      if (data.properties.infName != null) {
        data.set('infName', data.properties.infName);
        data.set('infRate', data.properties.infRate);
        data.set('opticalNum', data.properties.opticalNum);
      }
      if (data.properties.boardType != null) {
        if (data.properties.boardType == 0) {
          data.set('showBoardType', 'calculateBoard');
        }
        if (data.properties.boardType == 1) {
          data.set('showBoardType', 'FpgaBoard');
        }
        if (data.properties.boardType == 2) {
          data.set('showBoardType', 'exchangeBoard');
        }
        if (data.properties.boardType == 3) {
          data.set('showBoardType', 'interfaceBoard');
        }
        data.set('boardName', data.properties.boardName);
      }
      if (data.properties.caseName != null) {
        // console.log("casePropertyList",casePropertyList)
      }
      // data.set('rackname', data.properties.caseName);
      // data.set('boardnum', data.properties.bdnum);
    }
    if (image === 'images/Chip.svg') {
      return {
        group: '芯片属性',
        properties: [{
          client: 'chipName',
          displayName: '芯片名称'
        },
          {
            client: 'coreNum',
            displayName: '内核数量'
          },
          {
            client: 'memSize',
            displayName: '内存大小'
          },
          {
            client: 'recvRate',
            displayName: '接收速率'
          },
          {
            client: 'hrTypeName',
            displayName: '平台大类'
          },
          {
            client: 'IP',
            displayName: 'IP'
          }
        ]
      }
    }
    // console.log("data",data)
    if (image === 'images/OpticalFiberMouth.svg') {
      return {
        group: '接口属性',
        properties: [{
          client: 'infName',
          displayName: '接口名称'
        },
          {
            client: 'infRate',
            displayName: '接口速率'
          },
          {
            client: 'opticalNum',
            displayName: '光纤数量'
          }
        ]
      }
    }
    if (image === 'images/AfterTheBoard.svg' || image === 'images/BeforeTheBoard.svg') {
      return {
        group: '主板属性',
        properties: [{
          client: 'boardName',
          displayName: '主板名称'
        },
          {
            client: 'showBoardType',
            displayName: '主板类型'
          }
        ]
      }
    }

    if (type === 'port') {
      return {
        group: '接口属性',
        properties: [{
          client: 'infName',
          displayName: '接口名称'
        },
          {
            client: 'infRate',
            displayName: '接口速率'
          }
        ]
      }
    }
    if (image === 'rack') {
      return {
        group: '机箱属性',
        properties: [{
          client: 'caseName',
          displayName: '机箱名称'
        },
          {
            client: 'bdNum',
            displayName: '主板数量'
          }
        ]
      }

    }
    // console.log("propertySheet",propertySheet)
  }

  //交互效果
  function initInteraction() {
    var currentElement;
    var oldFillColor;
    var highlightColor = "#FF0";

    function unhighlight() {
      if (currentElement) {
        currentElement.setStyle(Q.Styles.SHAPE_FILL_COLOR, oldFillColor);
      }
    }

    function highlight(element) {
      if (currentElement == element) {
        return;
      }
      unhighlight(currentElement);
      currentElement = element;
      if (!currentElement) {
        return;
      }
      oldFillColor = currentElement.getStyle(Q.Styles.SHAPE_FILL_COLOR);
      currentElement.setStyle(Q.Styles.SHAPE_FILL_COLOR, highlightColor);
    }

    function findSlot(element, evt) {
      var xy = graph.toLogical(evt.event);
      var type = element.get('type');

      function canDrop(data) {
        return data.get('type') == 'slot' && data.get('childType') == type;
      }

      var slot;
      graph.forEachReverseVisibleUI(function (ui) {
        // console.log("ui",ui)
        // console.log("element",element)
        if (ui.data == element || ui.data.isDescendantOf(element)) {
          return;
        }
        var bounds = graph.getUIBounds(ui);
        if (bounds.intersects(xy.x, xy.y)) {
          if (canDrop(ui.data)) {
            slot = ui.data;
          }
          return false;
        }
      });
      return slot;
    }

    function adaptBounds(element, slot) {
      element.parent = element.host = slot;
      var bounds = slot.getBounds();
      graph.moveElements([element], bounds.x - element.x, bounds.y - element.y)
    }

    var dragInfo = {};
    graph.interactionDispatcher.addListener(function (evt) {
      // console.log('evt',evt)
      // console.log('evt',evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END)
      if (evt.kind === EVENT_CREATE_ELEMENT_BY_JSON) {
        if (evt.roots.length === 1) {
          var element = evt.roots[0];
          var slot = findSlot(element, evt);
          if (slot) {
            adaptBounds(element, slot);
          }
        }
        return;
      }
      var data = evt.data;
      if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED && evt.data instanceof Q.Node) {
        var currentElement = graph.getElement(evt.event);
        var slot = findSlot(data, evt);
        if (slot) {
          adaptBounds(data, slot);
        }
        return;
      }
      if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED && evt.data instanceof Q.Edge) {
        var edge = evt.data;
        // console.log("edge",edge)
        //校验只能接口连线
        if (evt.data.from.image != 'images/OpticalFiberMouth.svg' && evt.data.from.image != 'images/RoundMouth.svg' &&
          evt.data.from.image != 'images/InternetAccess.svg' && evt.data.from.image != 'images/SerialPort.svg'
        ) {
          graph.removeElement(edge);
        }
        if (evt.data.to.image != 'images/OpticalFiberMouth.svg' && evt.data.to.image != 'images/RoundMouth.svg' &&
          evt.data.to.image != 'images/InternetAccess.svg' && evt.data.to.image != 'images/SerialPort.svg'
        ) {
          graph.removeElement(edge);
        }

        //连线条件限制
        var edgeBundle = edge.getEdgeBundle();
        edge.setStyle(Q.Styles.EDGE_COLOR, '#2D97F9');
        edge.name = ' ';
        edge.setStyle(Q.Styles.ARROW_TO, true);
        if (edge.from.edgeCount > 1 || edge.to.edgeCount > 1) {
          graph.removeElement(edge);
        }
        if (edgeBundle.length > 1) {
          graph.removeElement(edge);
        }
        if (edgeBundle.node1 == edgeBundle.node2) {
          graph.removeElement(edge);
        }
      }
      if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
        var type = data.get('type');
        // console.log("type", type)
        if (type && (type == 'card' || type == 'port')) {
          dragInfo = {
            data: data,
            x: data.x,
            y: data.y
          };
          graph.sendToTop(data);
        } else {
          dragInfo = null;
        }
        return;
      }
      //机箱移动结束后刷新对应一面机箱的坐标
      if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END) {
        // console.log("data", data)
        moveLocation = data.location
        if (data.properties.frontBoardList != null) {
          //正面机箱坐标
          for (const i in graphList.fJson) {
            //找到机箱列表中药拖动的那个
            if (graphList.fJson[i].datas[0].json.properties.uniqueId === data.properties.uniqueId) {
              //修改机箱数据
              for (const j in graphList.fJson[i].datas) {
                //找到机箱每个图元的坐标
                if (j != 0 && graphList.fJson[i].datas[j].json.location != null) {
                  //两种数据格式，都修改
                  if (graphList.fJson[i].datas[j].json.location.json != null) {
                    //重新给x,y坐标赋值
                    graphList.fJson[i].datas[j].json.location.json.x = graphList.fJson[i].datas[j].json.location.json.x + (moveLocation.x - graphList.fJson[i].datas[0].json.location.x)
                    graphList.fJson[i].datas[j].json.location.json.y = graphList.fJson[i].datas[j].json.location.json.y + (moveLocation.y - graphList.fJson[i].datas[0].json.location.y)
                  } else {
                    graphList.fJson[i].datas[j].json.location.x = graphList.fJson[i].datas[j].json.location.x + (moveLocation.x - graphList.fJson[i].datas[0].json.location.x)
                    graphList.fJson[i].datas[j].json.location.y = graphList.fJson[i].datas[j].json.location.y + (moveLocation.y - graphList.fJson[i].datas[0].json.location.y)
                  }
                }
              }
              graphList.fJson[i].datas[0].json.location.x = moveLocation.x
              graphList.fJson[i].datas[0].json.location.y = moveLocation.y
            }
          }
          //背面机箱坐标
          for (const i in graphList.bJson) {
            if (graphList.bJson[i].datas[0].json.properties.uniqueId == data.properties.uniqueId) {
              for (const j in graphList.bJson[i].datas) {
                if (j != 0 && graphList.bJson[i].datas[j].json.location != null) {
                  if (graphList.bJson[i].datas[j].json.location.json != null) {
                    graphList.bJson[i].datas[j].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x + (moveLocation.x - graphList.bJson[i].datas[0].json.location.x)
                    graphList.bJson[i].datas[j].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y + (moveLocation.y - graphList.bJson[i].datas[0].json.location.y)
                    if (linkList.length != 0) {
                      for (const k in linkList) {
                        if (linkList[k][0].json.properties != null && linkList[k][0].json.properties.uniqueId == graphList.bJson[i].datas[j].json.properties.uniqueId) {
                          linkList[k][0].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x
                          linkList[k][0].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y
                        }
                        if (linkList[k][1].json.properties != null && linkList[k][1].json.properties.uniqueId == graphList.bJson[i].datas[j].json.properties.uniqueId) {
                          linkList[k][1].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x
                          linkList[k][1].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y
                        }
                      }
                    }
                    //连线的坐标
                    if (linkGraphList.datas.length != 0) {
                      for (const k in linkGraphList.datas) {
                        if (linkGraphList.datas[k].json.properties != null && linkGraphList.datas[k].json.properties.uniqueId == graphList.bJson[i].datas[j].json.properties.uniqueId) {
                          if (linkGraphList.datas[k].json.location != null && linkGraphList.datas[k].json.location.json != null) {
                            linkGraphList.datas[k].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x
                            linkGraphList.datas[k].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y
                          }
                        }
                      }
                    }
                  } else {
                    graphList.bJson[i].datas[j].json.location.x = graphList.bJson[i].datas[j].json.location.x + (moveLocation.x - graphList.bJson[i].datas[0].json.location.x)
                    graphList.bJson[i].datas[j].json.location.y = graphList.bJson[i].datas[j].json.location.y + (moveLocation.y - graphList.bJson[i].datas[0].json.location.y)
                  }
                }
              }
              graphList.bJson[i].datas[0].json.location.x = moveLocation.x
              graphList.bJson[i].datas[0].json.location.y = moveLocation.y
            }
          }
        } else {
          for (const i in graphList.fJson) {
            if (graphList.fJson[i].datas[0].json.properties.uniqueId == data.properties.uniqueId) {
              for (const j in graphList.fJson[i].datas) {
                if (j != 0 && graphList.fJson[i].datas[j].json.location != null) {
                  if (graphList.fJson[i].datas[j].json.location.json != null) {
                    graphList.fJson[i].datas[j].json.location.json.x = graphList.fJson[i].datas[j].json.location.json.x + (moveLocation.x - graphList.fJson[i].datas[0].json.location.x)
                    graphList.fJson[i].datas[j].json.location.json.y = graphList.fJson[i].datas[j].json.location.json.y + (moveLocation.y - graphList.fJson[i].datas[0].json.location.y)
                  } else {
                    graphList.fJson[i].datas[j].json.location.x = graphList.fJson[i].datas[j].json.location.x + (moveLocation.x - graphList.fJson[i].datas[0].json.location.x)
                    graphList.fJson[i].datas[j].json.location.y = graphList.fJson[i].datas[j].json.location.y + (moveLocation.y - graphList.fJson[i].datas[0].json.location.y)
                  }
                }
              }
              graphList.fJson[i].datas[0].json.location.x = moveLocation.x
              graphList.fJson[i].datas[0].json.location.y = moveLocation.y
            }
          }
          for (const i in graphList.bJson) {
            if (graphList.bJson[i].datas[0].json.properties.uniqueId == data.properties.uniqueId) {
              for (const j in graphList.bJson[i].datas) {
                if (j != 0 && graphList.bJson[i].datas[j].json.location != null) {
                  if (graphList.bJson[i].datas[j].json.location.json != null) {
                    graphList.bJson[i].datas[j].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x + (moveLocation.x - graphList.bJson[i].datas[0].json.location.x)
                    graphList.bJson[i].datas[j].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y + (moveLocation.y - graphList.bJson[i].datas[0].json.location.y)
                  } else {
                    graphList.bJson[i].datas[j].json.location.x = graphList.bJson[i].datas[j].json.location.x + (moveLocation.x - graphList.bJson[i].datas[0].json.location.x)
                    graphList.bJson[i].datas[j].json.location.y = graphList.bJson[i].datas[j].json.location.y + (moveLocation.y - graphList.bJson[i].datas[0].json.location.y)
                  }
                  for (const k in linkGraphList.datas) {
                    if (graphList.bJson[i].datas[j].json.properties != null && linkGraphList.datas[k]._className == "Q.RectElement"
                      && graphList.bJson[i].datas[j].json.properties.uniqueId == linkGraphList.datas[k].json.properties.uniqueId) {
                      linkGraphList.datas[k].json.location.json.x = graphList.bJson[i].datas[j].json.location.json.x
                      linkGraphList.datas[k].json.location.json.y = graphList.bJson[i].datas[j].json.location.json.y
                    }
                  }

                }
              }
              graphList.bJson[i].datas[0].json.location.x = moveLocation.x
              graphList.bJson[i].datas[0].json.location.y = moveLocation.y
            }
          }
          // graph.parseJSON(linkGraphList)
        }
        // graphList.fJson
      }
      /* if (evt.kind == Q.InteractionEvent.ELEMENT_MOVING && linkGraphList.length != 0) {
        console.log("graphList.bJson*data", graphList.bJson)
        console.log("ELEMENT_MOVING*data", data)
        console.log("linkGraphList*data", linkGraphList)
        for (const i in linkGraphList.datas) {
          for (const j in graphList.bJson) {
            for (const k in graphList.bJson[j].datas) {
              if (graphList.bJson[j].datas[k].json.properties != null && linkGraphList.datas[i]._className == "Q.RectElement"
                && graphList.bJson[j].datas[k].json.properties.uniqueId == linkGraphList.datas[i].json.properties.uniqueId) {
                  console.log("++++")
                  linkGraphList.datas[i].json.location.json.x = graphList.bJson[j].datas[k].json.location.json.x
                  linkGraphList.datas[i].json.location.json.y = graphList.bJson[j].datas[k].json.location.json.y
              }
            }
          }
        }
        // graph.removeElement(node);
        graph.parseJSON(linkGraphList)
      } */
      if (!dragInfo) {
        return
      }
      if (evt.kind == Q.InteractionEvent.ELEMENT_MOVING) {
        var slot = findSlot(data, evt);
        if (!slot) {
          unhighlight();
        } else {
          highlight(slot);
        }
        return;
      }
      if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END) {
        // console.log("evt",evt)
        unhighlight();
        var data = dragInfo.data;
        var oldSlot = data.parent;
        var slot = findSlot(data, evt);
        // console.log("slot", slot)
        // console.log("data", data)
        if (!slot || slot == oldSlot) {
          // console.log("+++")
          graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
        } else {
          adaptBounds(data, slot);
        }
        dragInfo = null;
      }
    })
  }

  initInteraction();
}

//全屏方法代码
function toggleFullScreen() {
  if (!document.fullscreenElement && // alternative standard method
    !document.mozFullScreenElement && !document.webkitFullscreenElement) {// current working methods
    if (document.documentElement.requestFullscreen) {
      document.documentElement.requestFullscreen();
    } else if (document.documentElement.mozRequestFullScreen) {
      document.documentElement.mozRequestFullScreen();
    } else if (document.documentElement.webkitRequestFullscreen) {
      document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
    }
  } else {
    if (document.cancelFullScreen) {
      document.cancelFullScreen();
    } else if (document.mozCancelFullScreen) {
      document.mozCancelFullScreen();
    } else if (document.webkitCancelFullScreen) {
      document.webkitCancelFullScreen();
    }
  }
}
