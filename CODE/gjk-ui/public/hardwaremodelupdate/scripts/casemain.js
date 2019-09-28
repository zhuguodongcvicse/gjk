var caseArr;
var board_json = [];
var frontCaseJSON;
var backCaseJSON;
var frontCaseJSONStr;
var backCaseJSONStr;
var backJson;
var caseList = [];
var fJson = []
var bJson = []
var graphList = { fJson, bJson }
var clickChipList = []
var currentBoard
var chipListTemp = []
var hardwareArr;
var allChipToFlow = []
Q.registerImage('rack', 'images/机箱.svg'); //这里可以修改成：机箱.svg，但是位置大小需要做调整，你可以自己修改
Q.registerImage('card', 'images/前板卡.svg');
Q.registerImage('cell', 'images/芯片.svg');
Q.registerImage('optical', 'images/光纤口.svg');
Q.registerImage('port', 'images/圆口.svg');
Q.registerImage('serial', 'images/串口.svg');
Q.registerImage('ePort', 'images/网口.svg');

// 子接收父参数
function handleMessageFromParent(event) {

	console.log("event.data.params", event.data)
	switch (event.data.cmd) {
		case 'getHardwarelibs':
			caseArr = event.data.params[1];
			hardwareArr = event.data.params[0];
			for (const i in caseArr) {
				var caseTemp = JSON.parse(caseArr[i].frontCase)
				caseTemp.datas[0].json.properties.bdNum = caseArr[i].bdNum
				caseArr[i].frontCase = JSON.stringify(caseTemp)
			}
			allJson = caseArr;
			//console.log(caseArr[0].boardJson)
			//主板json
			for (var i = 0; i < caseArr.length; i++) {
				board_json[i] = {
					image: 'images/机箱图标.jpg',
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
var caseID = 0
function ondropLoadJSON(evt, graph, center, options) {
	caseID++
	var uuidRandom = uuid(15, 62)
	var frontjson = JSON.parse(options.json.frontCase);
	var bJsonObj = JSON.parse(options.json.backCase);
	for (const i in frontjson.datas[0].json.properties.frontBoardList) {
		if (frontjson.datas[0].json.properties.frontBoardList[i].chipList != null && frontjson.datas[0].json.properties.frontBoardList[i].chipList.length != 0) {
			for (const j in frontjson.datas[0].json.properties.frontBoardList[i].chipList) {
				frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].chipList[j].uniqueId
			}
		}
		frontjson.datas[0].json.properties.frontBoardList[i].uniqueId = uuidRandom + '_' + frontjson.datas[0].json.properties.frontBoardList[i].uniqueId
	}

	//校验机箱上的图元不许移动
	for (index in frontjson.datas) {
		var image = frontjson.datas[index].json.image;
		if (image == 'images/芯片.svg' || image == 'images/光纤口.svg' || image == 'images/网口.svg'
			|| image == 'images/圆口.svg' || image == 'images/串口.svg' || image == 'images/前板卡.svg'
			|| image == 'images/后板卡.svg' ) {
			frontjson.datas[index].json.movable = false;
		}
		if (frontjson.datas[index].json.properties.chipName != null) {
			frontjson.datas[index].json.properties.uniqueId = uuidRandom + '_' + frontjson.datas[index].json.properties.uniqueId
		}
	}
	var json = JSON.stringify(frontjson)
	//校验后机箱上的图元不许移动
	for (index in bJsonObj.datas) {
		var image = bJsonObj.datas[index].json.image;
		if (image == 'images/芯片.svg' || image == 'images/光纤口.svg' || image == 'images/网口.svg'
			|| image == 'images/圆口.svg' || image == 'images/串口.svg' || image == 'images/前板卡.svg'
			|| image == 'images/后板卡.svg'|| image == 'rack') {
			bJsonObj.datas[index].json.movable = false;
		}
	}
	//给后面板赋值
	bJsonObj.datas[0].json.properties.uniqueId = uuidRandom
	for (const i in bJsonObj.datas[0].json.properties.backBoardList) {
		if (bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId.indexOf(uuidRandom) == -1) {
			bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId
		}
		for (const j in bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList) {
			if (bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId.indexOf(uuidRandom) == -1) {
				bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId
			}
			/* for (const k in bJsonObj.datas) {
				if (bJsonObj.datas[k].json.properties != null && bJsonObj.datas[k].json.properties.infName != null) {
					if (bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId.indexOf(bJsonObj.datas[k].json.properties.uniqueId) == -1) {
						bJsonObj.datas[k].json.properties.uniqueId = bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId
					}
				}
			} */
		}
	}
	for (const i in bJsonObj.datas) {
		if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.infName != null) {
			if (bJsonObj.datas[i].json.properties.uniqueId.indexOf(uuidRandom) == -1) {
				bJsonObj.datas[i].json.properties.uniqueId = uuidRandom + '_' + bJsonObj.datas[i].json.properties.uniqueId
			}
		}
		if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.caseName != null) {
			bJsonObj.datas[i].json.properties.ID = caseID
		}
	}

	console.log("frontjson", frontjson)
	console.log("bJsonObj", bJsonObj)
	//console.log("uuidRandom",uuidRandom)
	var fJsonStr = JSON.stringify(frontjson)
	var bJsonStr = JSON.stringify(bJsonObj)
	options.json.frontCase = fJsonStr
	options.json.backCase = bJsonStr
	caseList.push(options.json)
	graphList.fJson.push(JSON.parse(caseList[caseList.length - 1].frontCase))
	graphList.bJson.push(JSON.parse(caseList[caseList.length - 1].backCase))
	if (!json) {
		return;
	}
	var result = loadJSONInParent(graph, json, center);

	if (!result || !result.length) {
		return;
	}
	var roots = [];
	result.forEach(function (e) {
		if (e.parent === graph.currentSubnetwork) {
			roots.push(e);
		}
	})
	//给前面板赋值
	roots[0].properties.uniqueId = uuidRandom
	result[0].properties.uniqueId = uuidRandom
	result[0].properties.ID = caseID
	for (const i in result[0].properties.frontBoardList) {
		if (result[0].properties.frontBoardList[i].uniqueId.indexOf(uuidRandom) == -1) {
			result[0].properties.frontBoardList[i].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].uniqueId
		}
		for (const j in result[0].properties.frontBoardList[i].chipList) {
			if (result[0].properties.frontBoardList[i].chipList[j].uniqueId.indexOf(uuidRandom) == -1) {
				result[0].properties.frontBoardList[i].chipList[j].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].chipList[j].uniqueId
			}
			for (const k in result[0].properties.frontBoardList[i].chipList[j].infOfChipList) {
				if (result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId.indexOf(uuidRandom) == -1) {
					result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].chipList[j].infOfChipList[k].uniqueId
				}
			}
		}
		if (result[0].properties.frontBoardList[i].InternalLink != null && result[0].properties.frontBoardList[i].InternalLink.length != 0) {
			for (const j in result[0].properties.frontBoardList[i].InternalLink) {
				if (result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId.indexOf(uuidRandom) == -1) {
					result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].InternalLink[j][0].uniqueId
				}
				if (result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId.indexOf(uuidRandom) == -1) {
					result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId = uuidRandom + '_' + result[0].properties.frontBoardList[i].InternalLink[j][1].uniqueId
				}
			}
		}

	}
	for (const i in result[0].properties.frontBoardList) {
		clickChipList.concat(result[0].properties.frontBoardList[i].chipList)
	}

	//console.log('roots',roots)
	//console.log("result", result)

	graph.interactionDispatcher.onEvent({
		kind: EVENT_CREATE_ELEMENT_BY_JSON,
		datas: result,
		roots: roots,
		event: evt
	})

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


function loadJSONInParent(graph, json, center, parent) {
	parent = parent || graph.currentSubnetwork;

	var result = graph.parseJSON(json, { transform: false });
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
	//以下保存按钮
	var linkMap = new Map()
	var ifClickSave = 0
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	var cpuNodeID = 0
	button.textContent = '机箱保存';
	button.className = 'boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function (evt) {
		/* console.log("graphList.bJson === Object",Object.prototype.toString.call(graphList.bJson[0]))
		console.log("graphList",graphList.bJson[0]) */
		var graphName = graph.name
		console.log("graphName", graphName)
		// if (ifClickSave == 0) {
		if (graphName == null) {
			alert("未编辑背面！")
			return
		} else if (graphName == '背部视图') {
			ifClickSave = 1
			graphList.bJson.length = 0
			graphList.bJson.push(graph.toJSON())
			//配置的ip赋值
			/* chipListTemp = JSON.parse(JSON.stringify(chipListTemp))
			for (const i in graphList.fJson[0].datas[0].json.properties.frontBoardList) {
				for (const j in graphList.fJson[0].datas[0].json.properties.frontBoardList[i].chipList) {
					for (const k in chipListTemp) {
						if (graphList.fJson[0].datas[0].json.properties.frontBoardList[i].chipList[j].uniqueId.indexOf(chipListTemp[k].uniqueId) != -1) {
							graphList.fJson[0].datas[0].json.properties.frontBoardList[i].chipList[j] = chipListTemp[k]
						}
					}
				}
			} */
			//配置的ip nodeID
			for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
								for (const m in chipListTemp) {
									if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId.indexOf(chipListTemp[m].uniqueId) != -1) {
										graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k] = chipListTemp[m]
									}
								}
								graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID = cpuNodeID++
								allChipToFlow.push(graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k])
							}
						}
					}
				}
			}

			//连线关系
			for (const i in graphList.bJson[0].datas) {
				if (graphList.bJson[0].datas[i].json.properties != null && graphList.bJson[0].datas[i].json.properties.infType != null) {
					for (const j in graphList.bJson[0].datas) {
						if (graphList.bJson[0].datas[j].json.properties == null) {
							if (graphList.bJson[0].datas[j].json.from._ref == graphList.bJson[0].datas[i]._refId) {
								for (const k in graphList.bJson[0].datas) {
									if (graphList.bJson[0].datas[j].json.properties == null) {
										if (graphList.bJson[0].datas[j].json.to._ref == graphList.bJson[0].datas[k]._refId) {
											linkMap.set(graphList.bJson[0].datas[i].json.properties, graphList.bJson[0].datas[k].json.properties)
										}
									}
								}
							}
						}
					}
				}
			}
			var linkArr = Array.from(linkMap)
			var linkStr = JSON.stringify(linkArr)
			// console.log("allChipToFlow", allChipToFlow)
			// console.log("linkArr", linkArr)
			// console.log("graphList.fJson[0]", graphList.fJson[0])
			// console.log("graphList.bJson[0]", graphList.bJson[0])
			graphList.fJson[0] = JSON.stringify(graphList.fJson[0])
			graphList.bJson[0] = JSON.stringify(graphList.bJson[0])
			postMessageParentData.cmd = "submitCaseJSON";
			postMessageParentData.params = [graphList, linkStr, allChipToFlow]
			window.parent.postMessage(postMessageParentData, "*")
			//console.log("postMessageParentData",postMessageParentData)
			return
		} else if (graphName == '正面视图') {
			/* while (Object.prototype.toString.call(graphList.fJson) === '[object Array]') {
				graphList.fJson = JSON.stringify(graphList.fJson)
			}
			while (Object.prototype.toString.call(graphList.bJson) === '[object Array]') {
				graphList.bJson = JSON.stringify(graphList.bJson)
			} */
			alert("请回到背面进行保存")
			return
		}
		/* } else {
			alert("已保存过，若要更改请去编辑")
			return
		} */
	}
	function initToolbar() {

		var toolbar = editor.toolbar;

		var button = document.createElement('button');
		button.textContent = '切换视图';
		button.className = 'boarddesign_board_14s';
		button.onclick = function () {
			//进入正面
			if (graph.name == '背部视图') {
				graph.name = '正面视图';
				console.log("11", graph.name)
				graphList.bJson.length = 0
				caseList = [];
				graphList.bJson.push(graph.toJSON());
				graph.clear();
				/* 	for (var i = 0; i < graphList.fJson.length; i++) {
						graph.parseJSON(graphList.fJson[i], { transform: false })
					} */
				setEditable(true);
				return;
			}
			//进入背面
			if (graph.name == null) {
				graphList.fJson.length = 0
				graphList.fJson.push(graph.toJSON());
				graph.clear();

				//首次登陆回显机箱回显背面面机箱
				for (var i in hardwareArr.backJson) {
					graph.parseJSON(hardwareArr.backJson[i], { transform: false });
				}
				graph.parseJSON(hardwareArr.link, { transform: false });
				graph.name = '背部视图'
				console.log("22", graph.name)
				setEditable(false);
			}
			//进入背面
			if (graph.name == '正面视图') {
				graph.name = '背部视图';
				console.log("33", graph.name)
				graphList.fJson.length = 0
				graphList.fJson.push(graph.toJSON());
				graph.clear();

				var coord = [];

				/* 		for (var k in graphList.fJson[0].datas) {
							if (graphList.fJson[0].datas[k].json.image == 'rack') {
								if (graphList.fJson[0].datas[k].json.properties.sign != 1) {
									graphList.fJson[0].datas[k].json.properties.sign = 1;
									var x = graphList.fJson[0].datas[k].json.location.json.x;
									var y = graphList.fJson[0].datas[k].json.location.json.y;
									var coords = {};
									coords.x = x;
									coords.y = y;
									coord.push(coords);
								}
							}
						} */
				//遍历机箱后
				/* 		var caseLIstTemp1 = []
						for (var i = 0; i < caseList.length; i++) {
							caseLIstTemp1.push(JSON.parse(caseList[i].backCase))
		
						} */
				/* 		for (var j in coord) {
							var x = coord[j].x;
							var y = coord[j].y;
							for (index in caseLIstTemp1[j].datas) {
								if (caseLIstTemp1[j].datas[index]._className != "Q.Edge") {
									caseLIstTemp1[j].datas[index].json.location.x = x;
									caseLIstTemp1[j].datas[index].json.location.y = y;
									if (caseLIstTemp1[j].datas[index].json.location.json != null) {
										caseLIstTemp1[j].datas[index].json.location.json.x = caseLIstTemp1[j].datas[index].json.location.json.x + x;
										caseLIstTemp1[j].datas[index].json.location.json.y = caseLIstTemp1[j].datas[index].json.location.json.y + y;
									}
								}
							}
						} */
				//连线关系
				var dege = [];
				/* 		if (graphList.bJson[0].datas != null) {
							for (var x in graphList.bJson[0].datas) {
								if (graphList.bJson[0].datas[x]._className == "Q.Edge") {
									dege.push(graphList.bJson[0].datas[x]);
								}
							}
						} */


				/* 	for (const i in caseLIstTemp1) {
						graph.parseJSON(caseLIstTemp1[i], { transform: false })
					} */

				/* 		graph.parseJSON(graphList.bJson[0], { transform: false })
						setEditable(false); */
				return;
			}
		}


		toolbar.appendChild(button)
		//网状画布 
		var graph = editor.graph;
		//不可改变形状大小
		graph.editable = true;

		var defaultStyles = graph.styles = {};
		defaultStyles[Q.Styles.ARROW_TO] = false;

		var background = new GridBackground(graph);

		var currentCell = 10;

		function snapToGrid(x, y) {
			var gap = currentCell;
			x = Math.round(x / gap) * gap;
			y = Math.round(y / gap) * gap;
			return [x, y];
		}

	}
	//首次登陆回显机箱回显正面机箱
	for (var i in hardwareArr.frontJson) {
		graph.parseJSON(hardwareArr.frontJson[i], { transform: false });
	}

	graph.popupmenu.getMenuItems = function (graph, data, evt) {
		if (data) {
			console.log("2626",data.from)
			if(data.from != null){
				return [
					{
						text: '删除连线', action: function () {
							var data = graph.getElement(evt);
							graph.removeSelectionByInteraction(data);
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
		if (!selection || selection.length == 0) {
			return false;
		}
		Q.confirm("是否 确认删除", function () {
			var selection = this.removeSelection();
			console.log("selection", selection)
			for (const i in graphList.bJson) {
				if (graphList.bJson[i].datas[0].json.properties.uniqueId == selection[0].properties.uniqueId) {
					console.log("+++")
					removeByValue(caseList, caseList[i])
					removeByValue(graphList.fJson, graphList.fJson[i])
					removeByValue(graphList.bJson, graphList.bJson[i])
				}
			}
			for (const i in linkList) {
				if (linkList[i][0].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					console.log("---")
					removeByValue(linkList, linkList[i])
				}
				if (linkList[i][1].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					console.log("***")
					removeByValue(linkList, linkList[i])
				}
			}
			if (linkGraphList.datas.length != 0) {
				for (let i = 0; i < linkGraphList.datas.length; i++) {
					if (linkGraphList.datas[i]._className == "Q.RectElement" && linkGraphList.datas[i].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
						console.log("///")
						if (linkGraphList.datas[i + 1]._className == "Q.Edge") {
							removeByValue(linkGraphList.datas, linkGraphList.datas[i - 1])
							i--
							removeByValue(linkGraphList.datas, linkGraphList.datas[i])
							i--
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 1])
							i--
							break
						}
						if (linkGraphList.datas[i + 2]._className == "Q.Edge") {
							removeByValue(linkGraphList.datas, linkGraphList.datas[i])
							i--
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 1])
							i--
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 2])
							i--
							break
						}
					}
				}
			}
			//第一次切换回正面
			/* if (backAllCaseJsonTemp.datas.length != 0) {
				for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						removeByValue(linkGraphList.datas, linkGraphList.datas[i])
						i--
					}
				}
			} */

			console.log("caseList", caseList)
			console.log("graphList", graphList)
			console.log("linkList", linkList)
			console.log("linkGraphList", linkGraphList)
		}, this);
	}
	//右侧属性面板
	var propertySheet = editor.propertyPane;
	propertySheet.showDefaultProperties = false;

	//自定义属性面板
	propertySheet.getCustomPropertyDefinitions = function (data) {
		clickObj = data
		var type = data.get('type');
		var image = data.image;
		// console.log("propertySheet",propertySheet)
		// console.log("data",data)
		// console.log("type",type)
		//这里可以获得当前点击的图元对象
		graph.onclick = function (evt) {
			var data = graph.getElement(evt);
			// console.log("data", JSON.stringify(data.properties))
			// console.log("data",data)
			var parent = document.getElementsByClassName('graph-editor__property')
			var child = document.getElementsByClassName('btn btn-primary')
			// console.log("parent",parent)
			// console.log("child",child)
			// console.log("++",parent[0] instanceof jQuery)
			// console.log("--",parent[0] instanceof HTMLElement)
			if (child.length != 0) {
				parent[0].removeChild(child[0]);
			}
			if (data.properties.chipName != null) {
				//将板卡对应卡槽的slotnum赋给fSlotNum
				// console.log("propertySheet",propertySheet)
				var submitButton = document.createElement('input');
				submitButton.className = 'btn btn-primary '
				submitButton.value = 'Submit';
				propertySheet.dom.parentNode.appendChild(submitButton);
				submitButton.onclick = function () {
					var node = propertySheet.datas[0];
					// console.log("node.properties.IP", node.properties.IP)
					for (const m in graphList.fJson) {
						for (const k in graphList.fJson[m].datas) {
							if (graphList.fJson[m].datas[k].json.properties != null && graphList.fJson[m].datas[k].json.properties.caseName != null) {
								for (const i in graphList.fJson[m].datas[k].json.properties.frontBoardList) {
									for (const j in graphList.fJson[m].datas[k].json.properties.frontBoardList[i].chipList) {
										//找到数组中点击的当前芯片
										if (graphList.fJson[m].datas[k].json.properties.frontBoardList[i].chipList[j].uniqueId.indexOf(data.properties.uniqueId) != -1) {
											//当前配置栏输入的IP赋给数组中芯片的属性
											graphList.fJson[m].datas[k].json.properties.frontBoardList[i].chipList[j].IP = node.properties.IP
											//将该赋值后的芯片放到临时的芯片数组中
											chipListTemp.push(graphList.fJson[m].datas[k].json.properties.frontBoardList[i].chipList[j])
										}
									}
								}
							}
						}
					}
					//深拷贝
					chipListTemp = JSON.parse(JSON.stringify(chipListTemp))
					//	console.log("chipListTemp",chipListTemp)
				}
				//给点击选中的当前板卡标位0，其他没选中的标为1
				/* for (const i in clickChipList) {
					if(clickChipList[i].uniqueId == data.properties.uniqueId){
						clickChipList[i].ifClick = 0
						//把当前选中板卡赋给变量
						currentBoard = clickChipList[i]
					} else {
						clickChipList[i].ifClick = 1
					}
				}
				selectCount = 0
				console.log("clickChipList",JSON.parse(JSON.stringify(clickChipList))) */
			}


			data.set('chipname', data._mn3.chipName);
			data.set('corenum', data._mn3.coreNum);
			data.set('memsize', data._mn3.memSize);
			data.set('boardname', data._mn3.boardName);
			data.set('fibername', data._mn3.infName);
			data.set('fiberspeed', data._mn3.infRate);
			data.set('fibernum', data._mn3.opticalNum);
			// data.set('rackname', data._mn3.caseName);
			// data.set('boardnum', data._mn3.bdnum);
		}
		if (image == 'images/芯片.svg') {
			return {
				group: '芯片属性',
				properties: [{
					client: 'chipname',
					displayName: '芯片名称'
				},
				{
					client: 'corenum',
					displayName: '内核数量'
				},
				{
					client: 'memsize',
					displayName: '内存大小'
				},
				{
					client: 'IP',
					displayName: 'IP'
				}
				]
			}
		}
		// console.log("data",data)
		if (image == 'images/光纤口.svg') {
			return {
				group: '接口属性',
				properties: [{
					client: 'fibername',
					displayName: '接口名称'
				},
				{
					client: 'fiberspeed',
					displayName: '接口速率'
				},
				{
					client: 'fibernum',
					displayName: '光纤数量'
				}
				]
			}
		}
		if (image == 'images/前板卡.svg' || image == 'images/后板卡.svg') {
			return {
				group: '主板属性',
				properties: [{
					client: 'boardname',
					displayName: '主板名称'
				}
				]
			}
		}

		if (type == 'port') {
			return {
				group: '接口属性',
				properties: [{
					client: 'fibername',
					displayName: '接口名称'
				},
				{
					client: 'fiberspeed',
					displayName: '接口速率'
				}
				]
			}
		}
		if (image == 'rack') {
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
				console.log("currentElement", currentElement)

				var slot = findSlot(data, evt);
				if (slot) {
					adaptBounds(data, slot);
				}
				return;
			}
			if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED && evt.data instanceof Q.Edge) {
				//console.log('evt',evt)
				//连线条件限制
				var edge = evt.data;
				//校验只能接口连线
				if (evt.data.from.image != 'images/光纤口.svg' && evt.data.from.image != 'images/圆口.svg' &&
					evt.data.from.image != 'images/网口.svg' && evt.data.from.image != 'images/串口.svg'
				) {
					graph.removeElement(edge);
				}
				if (evt.data.to.image != 'images/光纤口.svg' && evt.data.to.image != 'images/圆口.svg' &&
					evt.data.to.image != 'images/网口.svg' && evt.data.to.image != 'images/串口.svg'
				) {
					graph.removeElement(edge);
				}

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
				var slot = findSlot(data, evt);
				if (!slot || slot == oldSlot) {
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