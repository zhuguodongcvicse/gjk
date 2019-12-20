var boardArr;
var caseData;
var board_json = [];
var frontCaseJSON;
var backCaseJSON;
var frontCaseJSONStr;
var backCaseJSONStr;
// var backBoardIdList = [];
// var frontBoardIdList = [];
var boardObj;
var fBoardList = [];
var bBoardList = [];
var fJson;
var bJson;
var backboard_json = [];
var boardType;
var boardCount
var backBoardInfs = []
var frontBoardCpus = []
var currentBoard
var selectCount = 0
var infOfChip = []
var showCpuList = []
var showCpuInfList = []
var linkTypeList = []
var showBackBoardInfList = []
var replenishCpuInfList = []
var replenishBackBoardInfList = []
var allInfOfBackBoard = []
var addInfCount = 0
var allInfOfFrontBoard = []
var outLineMap = new Map()
var outLineArr
var clickBoardList = []
var oppositeBoard
var boardNum
var fSlotNum
var bSlotNum
var fpgaBoardLinkType
var copyOrEditSymbol
//var uniqueId
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
  console.log("event.data", event.data)
  caseData = event.data.params[0];
  boardArr = event.data.params[1];
  fpgaBoardLinkType = event.data.params[2];
  copyOrEditSymbol = event.data.params[3];
  // console.log("caseData.frontCase", JSON.parse(caseData.frontCase))
  // console.log("caseData.backCase", JSON.parse(caseData.backCase))
  switch (event.data.cmd) {
    case 'updateCase':
      bJson = JSON.parse(caseData.backCase)
      console.log("bJson", bJson)
      boardNum = caseData.bdNum
      var existFrontBoards = JSON.parse(caseData.frontCase).datas[0].json.properties.frontBoardList
      var existBackBoards = JSON.parse(caseData.backCase).datas[0].json.properties.backBoardList
      fBoardList = JSON.parse(JSON.stringify(existFrontBoards))
      bBoardList = JSON.parse(JSON.stringify(existBackBoards))
      //配置界面弹窗的接口显示，去重
      // console.log("existFrontBoards", existFrontBoards)
      // console.log("existBackBoards", existBackBoards)
      for (const i in existFrontBoards) {
        if (existFrontBoards[i].boardType == 1) {
          for (const j in existFrontBoards[i].chipList) {
            allInfOfFrontBoard = allInfOfFrontBoard.concat(existFrontBoards[i].chipList[j].infOfChipList)
          }
          // console.log("allInfOfFrontBoard",allInfOfFrontBoard)
        }
      }
      for (const i in existFrontBoards) {
        if (existFrontBoards[i].InternalLink != null) {
          for (const j in existFrontBoards[i].InternalLink) {
            for (const k in allInfOfFrontBoard) {
              if (allInfOfFrontBoard[k].uniqueId.indexOf(existFrontBoards[i].InternalLink[j][1].uniqueId) != -1) {
                removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[k])
              }
              if (allInfOfFrontBoard[k].uniqueId.indexOf(existFrontBoards[i].InternalLink[j][3].uniqueId) != -1) {
                removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[k])
              }
            }
          }
          for (const j in existFrontBoards[i].outLinkArr) {
            for (const k in allInfOfFrontBoard) {
              if (allInfOfFrontBoard[k].uniqueId.indexOf(existFrontBoards[i].outLinkArr[j][1].uniqueId) != -1) {
                removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[k])
              }
            }
          }
        }
        clickBoardList.push(existFrontBoards[i])
      }
      clickBoardList = JSON.parse(JSON.stringify(clickBoardList))
      // console.log("clickBoardList", JSON.stringify(clickBoardList))
      for (const i in existBackBoards) {
        for (const j in existFrontBoards) {
          if (existFrontBoards[j].boardType == 1 && existBackBoards[i].ID == existFrontBoards[j].ID + 10) {
            allInfOfBackBoard = allInfOfBackBoard.concat(existBackBoards[i].backBoardInfList)
          }
        }
      }
      // console.log("existFrontBoards", existFrontBoards)
      // console.log("allInfOfBackBoard", allInfOfBackBoard)
      for (const i in existFrontBoards) {
        if (existFrontBoards[i].outLinkArr != null) {
          for (const j in existFrontBoards[i].outLinkArr) {
            for (const k in allInfOfBackBoard) {
              if (existFrontBoards[i].outLinkArr[j][3].uniqueId.indexOf(allInfOfBackBoard[k].uniqueId) != -1) {
                removeByValue(allInfOfBackBoard, allInfOfBackBoard[k])
              }
            }
          }
        }
      }
      allInfOfBackBoard = JSON.parse(JSON.stringify(allInfOfBackBoard))
      // console.log("allInfOfFrontBoard", allInfOfFrontBoard)
      for (var i = 0; i < boardArr.length; i++) {
        boardObj = JSON.parse(boardArr[i].boardJson)
        boardtype = boardObj.datas[0].json.properties.boardType
        if (boardtype == '0' || boardtype == '1' || boardtype == '2') {
          boardObj.datas[0].json.properties.id = boardArr[i].id
          var boardstr = JSON.stringify(boardObj)
          board_json[i] = {
            image: 'images/BeforeTheBoardTemplate.svg',
            json: boardstr,
            ondrop: 'ondropLoadJSON',
            showLabel: true,
            clientProperties: {
              boardName: boardArr[i].boardName
            },
          }
        }
        if (boardtype == '3') {
          boardObj.datas[0].json.properties.id = boardArr[i].id
          var boardstr = JSON.stringify(boardObj)
          backboard_json[i] = {
            image: 'images/AfterTheBoardTemplate.svg',
            json: boardstr,
            ondrop: 'ondropLoadJSON', //do any thing by your self when drop into the graph
            showLabel: true,
            clientProperties: {
              boardName: boardArr[i].boardName
            },
          }
          //console.log("boardArr[i].boardJson",board_json[i])
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

window.addEventListener("message", this.handleMessageFromParent) // 子接收方式二参数
var postMessageParentData = {
  cmd: "",//用于switch 判断
  params: []//具体参数
};

function init() {
  /* var json=board_json;
  var json =backboard_json; */
  $('#editor').graphEditor({
    images: [{
      name: '前主板',
      images: board_json
    }
    ],
    callback: initEditor,
    //data: 'json/card.json',
  });
}

function backinit() {
  $('#editor').graphEditor({
    images: [{
      name: '主板',
      images: backboard_json
    }
    ],
    callback: initEditor,
    //data: 'json/card.json',
  });
}

var EVENT_CREATE_ELEMENT_BY_JSON = 'create.element.by.json';

function ondropLoadJSON(evt, graph, center, options) {
  var uuidRandom = uuid(15, 62)
  boardNum++
  //console.log("evt", evt)
  //console.log("graph", graph)
  //console.log("center", center)
  let jsonObj = JSON.parse(options.json)
  //找到带芯片的板卡
  if (jsonObj.datas[0].json.properties.boardType === '0' || jsonObj.datas[0].json.properties.boardType === '1') {
    //向数据中添加芯片的唯一标识
    for (const i in jsonObj.datas) {
      //判断是不是芯片
      if (jsonObj.datas[i].json.properties.chipName != null && jsonObj.datas[i].json.properties.uniqueId.indexOf(uuidRandom) === -1) {
        //给芯片的唯一标识拼接上板卡的唯一标识
        jsonObj.datas[i].json.properties.uniqueId = uuidRandom + '_' + jsonObj.datas[i].json.properties.uniqueId
      }
      //校验主板上的芯片接口不许移动
      let image = jsonObj.datas[i].json.image;
      if (image === 'images/Chip.svg' || image === 'images/OpticalFiberMouth.svg' || image === 'images/InternetAccess.svg'
        || image === 'images/RoundMouth.svg' || image === 'images/SerialPort.svg'
      ) {
        jsonObj.datas[i].json.movable = false;
      }
    }
    // console.log("jsonObj",jsonObj)
    //在自定义数据中添加唯一表示
    for (const i in jsonObj.datas[0].json.properties.chipList) {
      //拖拽的板卡是数据中的第一条，给芯片的唯一标识拼接上板卡的唯一标识
      if (jsonObj.datas[0].json.properties.chipList[i].uniqueId.indexOf(uuidRandom) === -1) {
        jsonObj.datas[0].json.properties.chipList[i].uniqueId = uuidRandom + '_' + jsonObj.datas[0].json.properties.chipList[i].uniqueId
      }
      for (const j in jsonObj.datas[0].json.properties.chipList[i].infOfChipList) {
        //接口赋唯一标识
        if (jsonObj.datas[0].json.properties.chipList[i].infOfChipList[j].uniqueId.indexOf(jsonObj.datas[0].json.properties.uniqueId) === -1) {
          jsonObj.datas[0].json.properties.chipList[i].infOfChipList[j].uniqueId = uuidRandom + '_' + jsonObj.datas[0].json.properties.chipList[i].infOfChipList[j].uniqueId
        }
        //把接口添加到数组
        allInfOfFrontBoard.push(jsonObj.datas[0].json.properties.chipList[i].infOfChipList[j])
      }
    }
  }
  //校验主板上的芯片接口不许移动
  /*for (index in jsonObj.datas) {
    let image = jsonObj.datas[index].json.image;
    if (image === 'images/Chip.svg' || image === 'images/OpticalFiberMouth.svg' || image === 'images/InternetAccess.svg'
      || image === 'images/RoundMouth.svg' || image === 'images/SerialPort.svg'
    ) {
      jsonObj.datas[index].json.movable = false;
    }
  }*/
  //主板类型2的校验
  if (jsonObj.datas[0].json.properties.boardType === '2') {
    let newjson = graph.toJSON();
    for (const i in newjson.datas) {
      if (newjson.datas[i].json.image === 'images/AfterTheBoard.svg' && newjson.datas[i].json.properties.boardType === '2') {
        jsonObj == null;
        alert("该类型主板只允许放置一个")
        return jsonObj;
      }
    }
  }
  var element = graph.getElement(evt);
  // console.log('element', element)
  if (element == null) {
    jsonObj == null;
    return jsonObj;
  }
  var json = JSON.stringify(jsonObj)
  if (!json) {
    return;
  }

  result = loadJSONInParent(graph, json, center);
  if (!result || !result.length) {
    return;
  }
  var roots = [];
  result.forEach(function (e) {
    //console.log('++++',e.parent)
    if (e.parent === graph.currentSubnetwork) {
      //console.log('------------',e.parent)
      roots.push(e);
    }
  })
  //添加唯一标识方法
  roots[0].properties.uniqueId = uuidRandom
  result[0].properties.uniqueId = uuidRandom
  result[0].properties.ifClick = 0

  //给芯片和接口的唯一标识加上板卡的唯一标识  把板卡上各芯片的接口添加到数组
  if (result[0].properties.boardType == 3) {
    bBoardList.push(result[0].properties)
  } else {
    fBoardList.push(result[0].properties)
  }
  if (result[0].properties.InternalLink != null) {
    for (const i in result[0].properties.InternalLink) {
      if (result[0].properties.InternalLink[i][1].uniqueId.indexOf(uuidRandom) === -1) {
        result[0].properties.InternalLink[i][1].uniqueId = uuidRandom + '_' + result[0].properties.InternalLink[i][1].uniqueId
      }
      if (result[0].properties.InternalLink[i][3].uniqueId.indexOf(uuidRandom) === -1) {
        result[0].properties.InternalLink[i][3].uniqueId = uuidRandom + '_' + result[0].properties.InternalLink[i][3].uniqueId
      }
    }
  }
  for (const i in result[0].properties.chipList) {
    //芯片赋唯一标识
    if (result[0].properties.chipList[i].uniqueId.indexOf(result[0].properties.uniqueId) === -1) {
      result[0].properties.chipList[i].uniqueId = uuidRandom + '_' + result[0].properties.chipList[i].uniqueId
    }
    for (const j in result[0].properties.chipList[i].infOfChipList) {
      //接口赋唯一标识
      if (result[0].properties.chipList[i].infOfChipList[j].uniqueId.indexOf(result[0].properties.uniqueId) === -1) {
        result[0].properties.chipList[i].infOfChipList[j].uniqueId = uuidRandom + '_' + result[0].properties.chipList[i].infOfChipList[j].uniqueId
      }
      //把接口添加到数组
      allInfOfFrontBoard.push(result[0].properties.chipList[i].infOfChipList[j])
    }
  }
  if (result[0].properties.InternalLink != null) {
    for (const i in result[0].properties.InternalLink) {
      for (const j in allInfOfFrontBoard) {
        if (allInfOfFrontBoard[j].uniqueId.indexOf(result[0].properties.InternalLink[i][1].uniqueId) !== -1) {
          removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[j])
        }
        if (allInfOfFrontBoard[j].uniqueId.indexOf(result[0].properties.InternalLink[i][3].uniqueId) !== -1) {
          removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[j])
        }
      }
    }
  }
  //把后板卡上的接口添加到数组
  for (const i in result[0].properties.backBoardInfList) {
    //接口赋唯一标识
    if (result[0].properties.backBoardInfList[i].uniqueId.indexOf(result[0].properties.uniqueId) === -1) {
      result[0].properties.backBoardInfList[i].uniqueId = uuidRandom + '_' + result[0].properties.backBoardInfList[i].uniqueId
      //把接口添加到数组
      allInfOfBackBoard.push(result[0].properties.backBoardInfList[i])
      allInfOfBackBoard = JSON.parse(JSON.stringify(allInfOfBackBoard))
    }
  }
  for (const i in result) {
    if (result[i].properties.infName != null) {
      if (result[i].properties.uniqueId.indexOf(uuidRandom) === -1) {
        result[i].properties.uniqueId = uuidRandom + '_' + result[i].properties.uniqueId
      }
    }
  }
  // console.log("result", result)
  // console.log("result[0].parent", result[0].parent)
  // console.log("allInfOfFrontBoard", allInfOfFrontBoard)
  //console.log("allInfOfBackBoard", allInfOfBackBoard)
  graph.interactionDispatcher.onEvent({
    kind: EVENT_CREATE_ELEMENT_BY_JSON,
    datas: result,
    roots: roots,
    event: evt
  })

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

var slotNum = 1

function addSlot(parent, id, x, y, width, height, childType) {
  var slot = createNode({
    x: parent.x + x,
    y: parent.y + y,
    width: width,
    height: height
  }, parent);
  slot.setStyle(Q.Styles.SHAPE_FILL_COLOR, null);
  slot.set('type', 'slot');
  slot.set('slotNum', slotNum++);
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

function uuid(len, radix) {
  var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".split(
    ""
  );
  var uuid = [],
    i;
  radix = radix || chars.length;

  if (len) {
    // Compact form
    for (i = 0; i < len; i++) uuid[i] = chars[0 | (Math.random() * radix)];
  } else {
    // rfc4122, version 4 form
    var r;

    // rfc4122 requires these characters
    uuid[8] = uuid[13] = uuid[18] = uuid[23] = "-";
    uuid[14] = "4";

    // Fill in random data.  At i==19 set the high bits of clock sequence as
    // per rfc4122, sec. 4.1.5
    for (i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | (Math.random() * 16);
        uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
      }
    }
  }

  return uuid.join("");
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
  rack.set('id', caseData.id);
  //rack.set('uniqueId',uniqueId);
  rack.set('type', 'case');
  rack.set('caseName', caseData.caseName);
  rack.set('bdNum', caseData.bdNum);
  // rack.set('backBoardIdList',backBoardIdList);
  // rack.set('frontBoardIdList',frontBoardIdList);
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

    //	graph.interactionMode = null;
    //	graph.interactionMode = editable ? Q.Consts.INTERACTION_MODE_DEFAULT : Q.Consts.INTERACTION_MODE_VIEW;

    $('.layout').borderLayout();
    graph.updateViewport();
  }

  setEditable(true);

  var toolbarDiv = editor.toolbar;

  var button = Q.createButton({
    type: 'checkbox',
    name: '编辑模式',
    selected: graph.editable,
    action: function (evt) {
      setEditable(evt.target.checked);
    }
  })
  toolbarDiv.appendChild(button)
  initToolbar();
  /* function createLampStyles(){
    console.log("frontCaseJSON",frontCaseJSON)
    var idList1
    var chipId
    for (var i = 0; i < frontCaseJSON.datas.length; i++) {
      if(frontCaseJSON.datas[i].json.properties.idList != null && frontCaseJSON.datas[i].json.properties.idList != ""){
        idList1 = frontCaseJSON.datas[i].json.properties.idList
        for (var j = 0; j < idList1.length; j++) {
          chip
        }
      }
    }
  } */
  //保存
  //以下保存按钮
  var toolbarDiv = editor.toolbar;
  var button = document.createElement('button');
  var linkRelation = []
  var linkMap = new Map()
  button.textContent = '机箱保存';
  button.className = 'boarddesign_board_14s';
  toolbarDiv.appendChild(button)
  button.onclick = function (evt) {
    if (graph.name == '正面机箱') {
      fJson = graph.toJSON()
      //合并数组
      concatDataList()
      //给板卡赋ID
      setBoardsID()
    } else if (graph.name == '背部视图') {
      bJson = graph.toJSON()
      //合并数组
      concatDataList()
      //给板卡赋ID
      setBoardsID()
    } else if (graph.name == '正面视图') {
      fJson = graph.toJSON()
      //合并数组
      concatDataList()
      //给板卡赋ID
      setBoardsID()
    }
    // console.log("fJson", fJson)
    // console.log("bJson", bJson)
    let fJsonStr = JSON.stringify(fJson)
    let bJsonStr = JSON.stringify(bJson)
    postMessageParentData.cmd = copyOrEditSymbol;
    postMessageParentData.params = [fJsonStr, bJsonStr, boardNum];
    window.parent.postMessage(postMessageParentData, "*")
  }

  function initToolbar() {
    var toolbar = editor.toolbar;
    var button = document.createElement('button');
    button.textContent = '切换视图';
    button.className = 'boarddesign_board_14s';
    button.onclick = function () {
      if (graph.name === '背部视图') {
        //backCaseJSON = graph.toJSON();
        bJson = graph.toJSON()
        //console.log("bJson", bJson)
        //console.log("backCaseJSON",backCaseJSON)
        graph.clear();
        graph.name = '正面视图';
        console.log("11", graph.name)
        graph.parseJSON(fJson, {transform: false});


        var toolbox = editor.toolbox;
        toolbox.html.innerHTML = "";
        var toolbox_json = [{name: '前主板', images: board_json}];
        toolbox.loadImageBoxes(toolbox_json)
        toolbox.hideDefaultGroups();

        return;
      }
      if (graph.name === '正面机箱') {
        //frontCaseJSON = graph.toJSON();
        fJson = graph.toJSON()
        //console.log("fJson", fJson)
        graph.clear();
        var caseObj = JSON.parse(caseData.backCase);
        graph.parseJSON(caseObj, {transform: false});
        graph.moveToCenter();
        //graph.moveToCenter();
        graph.name = '背部视图'
        console.log("22", graph.name)


        var toolbox = editor.toolbox;
        toolbox.html.innerHTML = "";
        var toolbox_json = [{name: '后主板', images: backboard_json}];
        toolbox.loadImageBoxes(toolbox_json)
        toolbox.hideDefaultGroups();
      }
      if (graph.name === '正面视图') {
        fJson = graph.toJSON()
        //frontCaseJSON = graph.toJSON();
        graph.name = '背部视图';
        console.log("33", graph.name)
        //console.log("frontCaseJSON",frontCaseJSON)
        graph.clear();
        graph.parseJSON(bJson, {transform: false});

        var toolbox = editor.toolbox;
        toolbox.html.innerHTML = "";
        var toolbox_json = [{name: '后主板', images: backboard_json}];
        toolbox.loadImageBoxes(toolbox_json)
        toolbox.hideDefaultGroups();
        return;
      }
    }
    toolbar.appendChild(button)
    //网状画布
    var graph = editor.graph;
    //不可改变形状大小
    graph.editable = true;
    //不可缩放
    //	graph.enableWheelZoom = false
    var defaultStyles = graph.styles = {};
    defaultStyles[Q.Styles.ARROW_TO] = true;
    var background = new GridBackground(graph);

    var currentCell = 10;

    function snapToGrid(x, y) {
      var gap = currentCell;
      x = Math.round(x / gap) * gap;
      y = Math.round(y / gap) * gap;
      return [x, y];
    }

  }

  //给板卡赋ID
  function setBoardsID() {
    for (const i in fJson.datas) {
      if (fJson.datas[i].json.properties != null) {
        for (const j in fJson.datas) {
          if (fJson.datas[j].json.properties != null && fJson.datas[j].json.properties.boardType != null && fJson.datas[j].json.host._ref == fJson.datas[i]._refId) {
            fJson.datas[j].json.properties.ID = fJson.datas[i].json.properties.slotNum
            if (fJson.datas[0].json.properties.frontBoardList != null) {
              for (const k in fJson.datas[0].json.properties.frontBoardList) {
                if (fJson.datas[0].json.properties.frontBoardList[k].uniqueId === fJson.datas[j].json.properties.uniqueId) {
                  fJson.datas[0].json.properties.frontBoardList[k].ID = fJson.datas[i].json.properties.slotNum
                }
              }
            }
          }
        }
      }
    }
    for (const i in bJson.datas) {
      if (bJson.datas[i].json.properties != null) {
        for (const j in bJson.datas) {
          if (bJson.datas[j].json.properties != null && bJson.datas[j].json.properties.boardType != null && bJson.datas[j].json.host._ref == bJson.datas[i]._refId) {
            bJson.datas[j].json.properties.ID = bJson.datas[i].json.properties.slotNum
            if (bJson.datas[0].json.properties.backBoardList != null) {
              for (const k in bJson.datas[0].json.properties.backBoardList) {
                if (bJson.datas[0].json.properties.backBoardList[k].uniqueId === bJson.datas[j].json.properties.uniqueId) {
                  bJson.datas[0].json.properties.backBoardList[k].ID = bJson.datas[i].json.properties.slotNum
                }
              }
            }
          }
        }
      }
    }
  }

  //合并数组
  function concatDataList() {
    // console.log("clickBoardList",clickBoardList)
    // console.log("fBoardList",fBoardList)
    // return
    for (const i in clickBoardList) {
      for (const j in fBoardList) {
        if (clickBoardList[i].uniqueId == fBoardList[j].uniqueId) {
          removeByValue(fBoardList, fBoardList[j])
          fBoardList = fBoardList.concat(clickBoardList[i])
        }
      }
    }
    fBoardList = JSON.parse(JSON.stringify(fBoardList))
    fJson.datas[0].json.properties.frontBoardList = fBoardList
    bJson.datas[0].json.properties.backBoardList = bBoardList
  }

  //右侧属性面板
  /* initPropertyPane();

  function initPropertyPane() {
    var propertySheet = editor.propertyPane;
    propertySheet.showDefaultProperties = false;

    propertySheet.register({
      class: Q.RectElement,
      properties: [{
        client: 'type',
        displayName: 'Type'
      },
      {
        name: 'name',
        displayName: 'Name'
      }
      ]
    })
  } */
  //绘画设备
  var caseObj = JSON.parse(caseData.frontCase);
  graph.parseJSON(caseObj, {transform: false});
  graph.moveToCenter();
  graph.name = '正面机箱';
  //console.log(graph.name);


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
      //连线的互斥性  node.hasEdge()
      //console.log('evt',evt)
      if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED && evt.data instanceof Q.Edge) {
        //连线条件限制
        var edge = evt.data;
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
        //拖拽触发
        /* for (const i in evt.data.properties.backBoardInfList) {
          allInfOfBackBoard.push(evt.data.properties.backBoardInfList[i])
        } */
        //console.log("evt",evt)
        var slot = findSlot(data, evt);
        if (slot) {
          adaptBounds(data, slot);
        }
        return;
      }
      if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {

        var type = data.get('type');
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
        unhighlight();
        var data = dragInfo.data;
        var oldSlot = data.parent;
        //console.log("oldSlot",oldSlot);
        var slot = findSlot(data, evt);
        //console.log("Slot",slot);
        if (!slot || slot == oldSlot) {
          graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y);

        } else {
          adaptBounds(data, slot);
        }
        dragInfo = null;
      }
    })
  }

  //删除
  graph.removeSelectionByInteraction = false;
  graph.removeSelectionByInteraction = function (evt) {
    var selection = this.selectionModel.datas;
    if (!selection || selection.length == 0) {
      return false;
    }
    for (var i in selection) {
      if (selection[i].properties.type == "card") {
        Q.confirm("是否 确认删除", function () {
          var selection = this.removeSelection();
          console.log("selection", selection)
          boardNum--
          //如果是板卡0或者1
          if (selection[0].properties.boardtype == 0 || selection[0].properties.boardType == 1) {
            //移除fBoardList数组中的板卡
            for (const i in fBoardList) {
              if (fBoardList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                removeByValue(fBoardList, fBoardList[i])
              }
            }
            //移除allInfOfFrontBoard数组中此板卡上所有芯片的接口
            for (const i in selection[0].properties.chipList) {
              for (const j in selection[0].properties.chipList[i].infOfChipList) {
                for (const k in allInfOfFrontBoard) {
                  if (selection[0].properties.chipList[i].infOfChipList[j].uniqueId.indexOf(allInfOfFrontBoard[k]) != -1) {
                    removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[k])
                  }
                }
              }
            }
            //向allInfOfBackBoardt数组中补充添加此板卡对应后板卡上的接口
            if (typeof (bJson) != "undefined") {
              var oppositeBoardTemp
              for (const i in bJson.datas) {
                if (bJson.datas[i].json.properties != null && bJson.datas[i].json.properties.slotNum == fSlotNum + 10) {
                  for (const j in bJson.datas) {
                    if (bJson.datas[j].json.properties != null && bJson.datas[j].json.properties.boardType != null && bJson.datas[j].json.host._ref == bJson.datas[i]._refId) {
                      //console.log("thisboard",bJson.datas[j].json.properties)
                      oppositeBoardTemp = bJson.datas[j]
                    }
                  }
                }
              }
            }
            // console.log("oppositeBoardTemp",oppositeBoardTemp)
            for (const i in replenishBackBoardInfList) {
              if (replenishBackBoardInfList[i].uniqueId.indexOf(oppositeBoardTemp.json.properties.uniqueId) != -1) {
                allInfOfBackBoard.push(replenishBackBoardInfList[i])
              }
            }
          }

          //如果是后面机箱的板卡
          if (selection[0].properties.boardType == 3) {
            //移除bBoardList数组中此板卡
            for (const i in bBoardList) {
              if (bBoardList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                removeByValue(bBoardList, bBoardList[i])
              }
            }
            //向allInfOfFrontBoard数组中补充添加此板卡对应卡槽位置的前板卡上的接口
            for (const i in replenishBackBoardInfList) {
              if (replenishBackBoardInfList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
                allInfOfFrontBoard.push(replenishCpuInfList[i])
                //移除outLinkArr中的外部互联关系
                for (const j in clickBoardList) {
                  if (replenishCpuInfList[i].uniqueId.indexOf(clickBoardList[j]) != -1) {
                    clickBoardList[j].outLineArr = []
                  }
                }
              }
            }
          }
          //移除点击板卡数组中的此板卡
          for (const i in clickBoardList) {
            if (clickBoardList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
              removeByValue(clickBoardList, clickBoardList[i])
            }
          }
          /* if (selection) {
            var event = new InteractionEvent(this, InteractionEvent.ELEMENT_REMOVED, evt, selection);
            this.onInteractionEvent(event);
          } */
        }, this);
      }
    }
  }

  var propertySheet = editor.propertyPane;
  propertySheet.showDefaultProperties = false;

  //自定义属性面板
  propertySheet.getCustomPropertyDefinitions = function (data) {
    var type = data.get('type');
    var image = data.image;
    //console.log("data----",data)
    currentBoard = data.properties
    if (data.properties.boardType == 1) {
      data.properties.outLinkArr = []
      //将板卡对应卡槽的slotnum赋给fSlotNum
      if (data.parent !== undefined && data.parent !== null) {
        if (data.parent.properties.slotNum != null) {
          fSlotNum = data.parent.properties.slotNum
        } else {
          fSlotNum = data.parent.properties.ID
        }
      }

      //eval("var board_" + data.properties.uniqueId + "=" + data.properties)
      //初次添加板卡到数组
      if (clickBoardList.length == 0) {
        clickBoardList.push(data.properties)
      }
      //给数组添加是否存在的标识
      var ifExist = 0
      for (const i in clickBoardList) {
        //如果存在则++
        if (clickBoardList[i].uniqueId == data.properties.uniqueId) {
          ifExist++
        }
      }
      //板卡不存在则添加该板卡
      if (ifExist == 0) {
        clickBoardList.push(data.properties)
      }
      //给点击选中的当前板卡标位0，其他没选中的标为1
      for (const i in clickBoardList) {
        if (clickBoardList[i].uniqueId == data.properties.uniqueId) {
          clickBoardList[i].ifClick = 0
          //把当前选中板卡赋给变量
          currentBoard = clickBoardList[i]
        } else {
          clickBoardList[i].ifClick = 1
        }
      }
      selectCount = 0
      // console.log("clickBoardList", JSON.parse(JSON.stringify(clickBoardList)))
    }
    //这里可以获得当前点击的图元对象
    graph.onclick = function (evt) {
      var data = graph.getElement(evt);
      // console.log("graph", graph)
      // console.log("data", data)
      // console.log("data", JSON.stringify(data.properties))
      if (typeof (data) != 'undefined') {
        data.set('chipName', data.properties.chipName);
        data.set('coreNum', data.properties.coreNum);
        data.set('memSize', data.properties.memSize);
        data.set('boardName', data.properties.boardName);
        data.set('recvRate', data.properties.recvRate);
        data.set('hrTypeName', data.properties.hrTypeName);
        if (data.properties.boardType == 0) {
          data.set('showBoardType', 'calculateBoard');
        }
        if (data.properties.boardType == 1) {
          data.set('showBoardType', 'FpgaBoard');
        }
        if (data.properties.boardType == 2) {
          console.log("data*****", data)
          data.set('showBoardType', 'exchangeBoard');
        }
        if (data.properties.boardType == 3) {
          data.set('showBoardType', 'interfaceBoard');
        }
        data.set('infName', data.properties.infName);
        data.set('infRate', data.properties.infRate);
        data.set('opticalNum', data.properties.opticalNum);
        data.set('caseName', data.properties.caseName);
        // data.set('boardnum',data.properties.bdnum);
        // console.log("data", data)
      }
    }
    if (image == 'images/Chip.svg') {
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
          }
        ]
      }
    }
    if (image == 'images/OpticalFiberMouth.svg') {
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
    if (image == 'images/BeforeTheBoard.svg' || image == 'images/AfterTheBoard.svg') {
      return {
        group: '主板属性',
        properties: [{
          client: 'boardName',
          displayName: '主板名称'
        }, {
          client: 'showBoardType',
          displayName: '主板类型'
        }
        ]
      }
    }

    if (type == 'port') {
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
    if (image == 'rack') {
      console.log("666666666666", image)
      return {
        group: '机箱属性',
        properties: [{
          client: 'caseName',
          displayName: '机箱名称'
        }/* ,
					{
						client: 'boardnum',
						displayName: '主板数量'
					} */
        ]
      }

    }
  }


  //initData();
  initInteraction();

}
