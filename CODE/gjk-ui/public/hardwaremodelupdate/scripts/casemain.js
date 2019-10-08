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
var link
var graphList = { fJson, bJson, link }
var clickChipList = []
var currentBoard
var chipListTemp = []
var hardwareArr;
var allChipToFlow = []
var initialLocation
var moveLocation
var nextMoveLocation
var backAllCaseJsonTemp
var linkList = []
var linkGraphList = { datas: [] }
var clickCheckedChip
var frontCaseForDeployment
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
			var linkTemp = JSON.parse(hardwareArr.link)
			// console.log("linkTemp",linkTemp)
			for (const i in hardwareArr.frontJson) {
				graphList.fJson.push(hardwareArr.frontJson[i])
				graphList.bJson.push(hardwareArr.backJson[i])
			}
			if (linkTemp.length != 0) {
				graphList.link = linkTemp
				linkGraphList = linkTemp
			}
			// console.log("linkGraphList",linkGraphList)
			for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
								for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
									//第二次
									for (const m in graphList.fJson[n].datas) {
										if (graphList.fJson[n].datas[m].json.properties != null && graphList.fJson[n].datas[m].json.properties.chipName != null
											&& graphList.fJson[n].datas[m].json.properties.uniqueId == graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId) {
											graphList.fJson[n].datas[m].json.properties.IP = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].IP
										}
									}
								}
							}
						}
					}
				}
			}
			console.log("graphList", graphList)
			console.log("linkGraphList", linkGraphList)
			graphList.fJson = JSON.parse(JSON.stringify(graphList.fJson))
			graphList.bJson = JSON.parse(JSON.stringify(graphList.bJson))
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
	//机箱ID赋值
	caseID++
	//声明机箱的唯一标识
	var uuidRandom = uuid(15, 62)
	//反序列化
	var frontjson = JSON.parse(options.json.frontCase);
	var bJsonObj = JSON.parse(options.json.backCase);
	//芯片和板卡标识拼接上机箱的唯一标识
	frontjson.datas[0].json.properties.uniqueId = uuidRandom
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
			|| image == 'images/后板卡.svg') {
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
			|| image == 'images/后板卡.svg' || image == 'rack') {
			bJsonObj.datas[index].json.movable = false;
		}
	}
	//后板卡和后板卡的接口拼接机箱的唯一标识
	bJsonObj.datas[0].json.properties.uniqueId = uuidRandom
	for (const i in bJsonObj.datas[0].json.properties.backBoardList) {
		if (bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId.indexOf(uuidRandom) == -1) {
			bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].uniqueId
		}
		for (const j in bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList) {
			if (bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId.indexOf(uuidRandom) == -1) {
				bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId = uuidRandom + '_' + bJsonObj.datas[0].json.properties.backBoardList[i].backBoardInfList[j].uniqueId
			}
		}
	}
	for (const i in bJsonObj.datas) {
		if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.infName != null) {
			if (bJsonObj.datas[i].json.properties.uniqueId.indexOf(uuidRandom) == -1) {
				bJsonObj.datas[i].json.properties.uniqueId = uuidRandom + '_' + bJsonObj.datas[i].json.properties.uniqueId
			}
		}
		if (bJsonObj.datas[i].json.properties != null && bJsonObj.datas[i].json.properties.caseName != null) {
			bJsonObj.datas[i].json.properties.uniqueId = uuidRandom
			bJsonObj.datas[i].json.properties.ID = caseID
		}
	}
	// console.log("frontjson", frontjson)
	// console.log("bJsonObj", bJsonObj)
	//console.log("uuidRandom",uuidRandom)
	//序列化
	var fJsonStr = JSON.stringify(frontjson)
	var bJsonStr = JSON.stringify(bJsonObj)
	options.json.frontCase = fJsonStr
	options.json.backCase = bJsonStr
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
	//给前板卡、芯片、接口拼接机箱唯一标识
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
		//内部互联解耦添加唯一标识
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
	//放到数组中
	caseList.push(options.json)
	graphList.fJson.push(JSON.parse(caseList[caseList.length - 1].frontCase))
	graphList.bJson.push(JSON.parse(caseList[caseList.length - 1].backCase))
	initialLocation = result[0].location
	//放到画布上初始坐标赋值
	for (const i in graphList.fJson) {
		if (graphList.fJson[i].datas[0].json.properties.uniqueId == result[0].properties.uniqueId) {
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
		if (graphList.bJson[i].datas[0].json.properties.uniqueId == result[0].properties.uniqueId) {
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
	console.log("caseList", caseList)
	console.log("graphList", graphList)
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
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	var cpuNodeID = 0
	button.textContent = '机箱保存';
	button.className = 'boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function (evt) {
		var graphName = graph.name
		console.log("graphName", graphName)
		if (graphName == null) {
			// console.log("graph", graph.toJSON())
			frontCaseForDeployment = graph.toJSON()
			//连线关系
			for (const i in graphList.bJson) {
				for (const j in graphList.bJson[i].datas) {
					if (graphList.bJson[i].datas[j].json.properties == null) {
						for (const k in graphList.bJson[i].datas) {
							if (graphList.bJson[i].datas[k].json.properties != null) {
								if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[i].datas[k]._refId) {
									for (const m in graphList.bJson[i].datas) {
										if (graphList.bJson[i].datas[m].json.properties != null) {
											if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[i].datas[m]._refId) {
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
			//配置的ip nodeID 
			for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
								for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
									for (const m in chipListTemp) {
										if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId.indexOf(chipListTemp[m].uniqueId) != -1) {
											graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k] = chipListTemp[m]
										}
									}
									graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID = cpuNodeID++
									//给部署图所需数据赋nodeid
									for (const p in frontCaseForDeployment.datas) {
										if (frontCaseForDeployment.datas[p].json.properties.chipName != null
											&& frontCaseForDeployment.datas[p].json.properties.uniqueId == graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId) {
											frontCaseForDeployment.datas[p].json.properties.nodeID = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
											frontCaseForDeployment.datas[p].json.properties.IP = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].IP
										}
									}
									allChipToFlow.push(graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k])
								}
							}
						}
					}
				}
			}
			graphList.fJson = JSON.parse(JSON.stringify(graphList.fJson))
			var linkArr = Array.from(linkMap)
			var linkStr = JSON.stringify(linkArr)
			graphList.link = JSON.stringify([])
			frontCaseForDeployment = JSON.stringify(frontCaseForDeployment)
			postMessageParentData.cmd = "submitCaseJSON";
			postMessageParentData.params = [graphList, linkStr, allChipToFlow, frontCaseForDeployment]
			window.parent.postMessage(postMessageParentData, "*")
			console.log("postMessageParentData--first", postMessageParentData)
			return
		} else if (graphName == '背部视图') {
			//第一次切换到背面就点保存时执行以下，重新划线保存，能提出来但是老子懒得提
			backAllCaseJsonTemp = graph.toJSON()
			// console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
			for (const i in backAllCaseJsonTemp.datas) {
				if (backAllCaseJsonTemp.datas[i].json.properties == null) {
					for (const j in backAllCaseJsonTemp.datas) {
						if (backAllCaseJsonTemp.datas[i].json.from._ref == backAllCaseJsonTemp.datas[j]._refId) {
							console.log("起始接口-外", backAllCaseJsonTemp.datas[j])
							for (const k in backAllCaseJsonTemp.datas) {
								if (backAllCaseJsonTemp.datas[k].json.properties != null && backAllCaseJsonTemp.datas[i].json.to._ref == backAllCaseJsonTemp.datas[k]._refId) {
									console.log("末端接口-外", backAllCaseJsonTemp.datas[k])
									var startStr = backAllCaseJsonTemp.datas[j].json.properties.uniqueId.slice(0, 15)
									var endStr = backAllCaseJsonTemp.datas[k].json.properties.uniqueId.slice(0, 15)
									if (startStr != endStr) {
										var linkListStr = JSON.stringify(linkList)
										if (linkListStr.indexOf(backAllCaseJsonTemp.datas[j].json.properties.uniqueId) == -1) {
											linkList.push([backAllCaseJsonTemp.datas[j], backAllCaseJsonTemp.datas[k]])
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
			//删除连线
			for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
				if (backAllCaseJsonTemp.datas[i].json.properties == null) {
					removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
					i--
				}
			}
			//创建连线
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
			for (const i in linkList) {
				linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
				linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
				edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
				edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
				// console.log("edgejson",edgejson)
				var linkGraphListStr = JSON.stringify(linkGraphList)
				if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) == -1) {
					console.log("*-*-*")
					linkGraphList.datas.push(linkList[i][0])
					linkGraphList.datas.push(linkList[i][1])
					linkGraphList.datas.push(edgejson)
				}
				// console.log("linkGraphList",linkGraphList)
			}
			linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
			//连线关系
			for (const i in graphList.bJson) {
				for (const j in graphList.bJson[i].datas) {
					if (graphList.bJson[i].datas[j].json.properties == null) {
						for (const k in graphList.bJson[i].datas) {
							if (graphList.bJson[i].datas[k].json.properties != null) {
								if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[i].datas[k]._refId) {
									for (const m in graphList.bJson[i].datas) {
										if (graphList.bJson[i].datas[m].json.properties != null) {
											if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[i].datas[m]._refId) {
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
			//配置的ip nodeID 
			for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
								for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
									for (const m in chipListTemp) {
										if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId.indexOf(chipListTemp[m].uniqueId) != -1) {
											graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k] = chipListTemp[m]
										}
									}
									graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID = cpuNodeID++
									//给部署图所需数据赋nodeid
									for (const p in frontCaseForDeployment.datas) {
										if (frontCaseForDeployment.datas[p].json.properties.chipName != null
											&& frontCaseForDeployment.datas[p].json.properties.uniqueId == graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId) {
											frontCaseForDeployment.datas[p].json.properties.nodeID = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
											frontCaseForDeployment.datas[p].json.properties.IP = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].IP
										}
									}
									allChipToFlow.push(graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k])
								}
							}
						}
					}
				}
			}
			//照顾部署图
			/* for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
								for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
									for (const m in graphList.fJson) {
										for (const p in graphList.fJson[m].datas) {
											if (graphList.fJson[m].datas[p].json.properties != null && graphList.fJson[m].datas[p].json.properties.chipName != null) {
												if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId.indexOf(graphList.fJson[m].datas[p].json.properties.uniqueId) != -1) {
													graphList.fJson[m].datas[p].json.properties.nodeID = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
												}
											}
										}
									}
								}
							}
						}
					}
				}
			} */
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
			console.log("postMessageParentData--back", postMessageParentData)
			return
		} else if (graphName == '正面视图') {
			frontCaseForDeployment = graph.toJSON()
			//第一次切换回正面就点保存时执行以下，重新划线保存，能提出来但是老子懒得提
			if (linkGraphList.datas.length == 0) {
				linkList = []
				// console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
				for (const i in backAllCaseJsonTemp.datas) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						for (const j in backAllCaseJsonTemp.datas) {
							if (backAllCaseJsonTemp.datas[i].json.from._ref == backAllCaseJsonTemp.datas[j]._refId) {
								console.log("起始接口", backAllCaseJsonTemp.datas[j])
								for (const k in backAllCaseJsonTemp.datas) {
									if (backAllCaseJsonTemp.datas[k].json.properties != null && backAllCaseJsonTemp.datas[i].json.to._ref == backAllCaseJsonTemp.datas[k]._refId) {
										console.log("末端接口", backAllCaseJsonTemp.datas[k])
										var startStr = backAllCaseJsonTemp.datas[j].json.properties.uniqueId.slice(0, 15)
										var endStr = backAllCaseJsonTemp.datas[k].json.properties.uniqueId.slice(0, 15)
										if (startStr != endStr) {
											linkList.push([backAllCaseJsonTemp.datas[j], backAllCaseJsonTemp.datas[k]])
											linkMap.set(backAllCaseJsonTemp.datas[j].json.properties, backAllCaseJsonTemp.datas[k].json.properties)
											break
										}
									}
								}
								break
							}
						}
					}
				}
				console.log("linkList", linkList)
				//删除连线
				for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
						i--
					}
				}
				//创建连线
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
				if (linkList.length != 0) {
					for (const i in linkList) {
						linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
						linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
						edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
						edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
						// console.log("edgejson",edgejson)
						var linkGraphListStr = JSON.stringify(linkGraphList)
						if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) == -1) {
							console.log("*-*-*ffff")
							linkGraphList.datas.push(linkList[i][0])
							linkGraphList.datas.push(linkList[i][1])
							linkGraphList.datas.push(edgejson)
						}
						linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
						// console.log("linkGraphList",linkGraphList)
					}
				}
				//连线关系
				for (const i in graphList.bJson) {
					for (const j in graphList.bJson[i].datas) {
						if (graphList.bJson[i].datas[j].json.properties == null) {
							for (const k in graphList.bJson[i].datas) {
								if (graphList.bJson[i].datas[k].json.properties != null) {
									if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[i].datas[k]._refId) {
										for (const m in graphList.bJson[i].datas) {
											if (graphList.bJson[i].datas[m].json.properties != null) {
												if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[i].datas[m]._refId) {
													linkMap.set(graphList.bJson[i].datas[k].json.properties, graphList.bJson[i].datas[m].json.properties)
												}
											}
										}
									}
								}
							}
						}
					}
				}
				console.log("linkMap", linkMap)
			}/*  else {
				for (let i = 0; i < linkGraphList.datas.length; i++) {
					if (linkGraphList.datas[i]._className == "Q.Edge") {
						linkMap.set(linkGraphList.datas[i - 2].json.properties, linkGraphList.datas[i - 1].json.properties)
					}
				}
			} */
			//配置的ip nodeID
			var maxNodeId
			for (const n in graphList.fJson) {
				for (const i in graphList.fJson[n].datas) {
					if (graphList.fJson[n].datas[i].json.properties != null && graphList.fJson[n].datas[i].json.properties.caseName != null) {
						for (const j in graphList.fJson[n].datas[i].json.properties.frontBoardList) {
							if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList != null) {
								for (const k in graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList) {
									for (const m in chipListTemp) {
										if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId.indexOf(chipListTemp[m].uniqueId) != -1) {
											graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k] = chipListTemp[m]
										}
									}
									if (graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID != null) {
										if (typeof maxNodeId != 'undefined') {
											if (maxNodeId < graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID) {
												maxNodeId = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
											}
										} else {
											maxNodeId = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
										}
									} else {
										graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID = cpuNodeID++
									}
									//给部署图所需数据赋nodeid
									for (const p in frontCaseForDeployment.datas) {
										if (frontCaseForDeployment.datas[p].json.properties.chipName != null
											&& frontCaseForDeployment.datas[p].json.properties.uniqueId == graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].uniqueId) {
											frontCaseForDeployment.datas[p].json.properties.nodeID = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].nodeID
											frontCaseForDeployment.datas[p].json.properties.IP = graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k].IP
										}
									}
									allChipToFlow.push(graphList.fJson[n].datas[i].json.properties.frontBoardList[j].chipList[k])
								}
							}
						}
					}
				}
			}
			graphList.fJson = JSON.parse(JSON.stringify(graphList.fJson))
			var linkArr = Array.from(linkMap)
			var linkStr = JSON.stringify(linkArr)
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
			console.log("postMessageParentData--front", postMessageParentData)
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
				console.log("linkGraphListfffffffffffff", linkGraphList)
				backAllCaseJsonTemp = graph.toJSON()
				console.log("frontCaseForDeployment", frontCaseForDeployment)
				console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
				//找到连线的两个接口放到数组
				for (const i in backAllCaseJsonTemp.datas) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						for (const j in backAllCaseJsonTemp.datas) {
							if (backAllCaseJsonTemp.datas[i].json.from._ref == backAllCaseJsonTemp.datas[j]._refId) {
								console.log("起始接口-机箱外部", backAllCaseJsonTemp.datas[j])
								for (const k in backAllCaseJsonTemp.datas) {
									if (backAllCaseJsonTemp.datas[k].json.properties != null && backAllCaseJsonTemp.datas[i].json.to._ref == backAllCaseJsonTemp.datas[k]._refId) {
										console.log("末端接口-机箱外部", backAllCaseJsonTemp.datas[k])
										var startStr = backAllCaseJsonTemp.datas[j].json.properties.uniqueId.slice(0, 15)
										var endStr = backAllCaseJsonTemp.datas[k].json.properties.uniqueId.slice(0, 15)
										if (startStr != endStr) {
											var linkListStr = JSON.stringify(linkList)
											if (linkListStr.indexOf(backAllCaseJsonTemp.datas[j].json.properties.uniqueId) == -1) {
												linkList.push([backAllCaseJsonTemp.datas[j], backAllCaseJsonTemp.datas[k]])
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
				console.log("linkList", linkList)
				graph.clear();
				for (var i = 0; i < graphList.fJson.length; i++) {
					graph.parseJSON(graphList.fJson[i], { transform: false })
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

				for (const i in graphList.bJson) {
					graph.parseJSON(graphList.bJson[i], { transform: false });
				}
				if (typeof graphList.link != 'undefined') {
					graph.parseJSON(graphList.link)
				}

				setEditable(false);
			}
			//进入背面
			if (graph.name == '正面视图') {
				graph.name = '背部视图';
				// console.log("33", graph.name)
				frontCaseForDeployment = graph.toJSON()
				// console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
				//找到连线的两个接口放到数组
				for (const i in backAllCaseJsonTemp.datas) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						for (const j in backAllCaseJsonTemp.datas) {
							if (backAllCaseJsonTemp.datas[i].json.from._ref == backAllCaseJsonTemp.datas[j]._refId) {
								console.log("起始接口-机箱外部", backAllCaseJsonTemp.datas[j])
								for (const k in backAllCaseJsonTemp.datas) {
									if (backAllCaseJsonTemp.datas[k].json.properties != null && backAllCaseJsonTemp.datas[i].json.to._ref == backAllCaseJsonTemp.datas[k]._refId) {
										console.log("末端接口-机箱外部", backAllCaseJsonTemp.datas[k])
										var startStr = backAllCaseJsonTemp.datas[j].json.properties.uniqueId.slice(0, 15)
										var endStr = backAllCaseJsonTemp.datas[k].json.properties.uniqueId.slice(0, 15)
										if (startStr != endStr) {
											var linkListStr = JSON.stringify(linkList)
											if (linkListStr.indexOf(backAllCaseJsonTemp.datas[j].json.properties.uniqueId) == -1) {
												linkList.push([backAllCaseJsonTemp.datas[j], backAllCaseJsonTemp.datas[k]])
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
				//删除qunee自己生成的连线数据
				for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null) {
						removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
						i--
					}
				}
				for (const i in graphList.bJson) {
					for (const j in graphList.bJson[i].datas) {
						if (graphList.bJson[i].datas[j].json.properties == null) {
							for (const k in graphList.bJson) {
								for (const m in graphList.bJson[k].datas) {
									if (graphList.bJson[i].datas[j].json.from._ref == graphList.bJson[k].datas[m]._refId) {
										console.log("起始接口-机箱内部", graphList.bJson[k].datas[m]._refId)
										for (const p in graphList.bJson[k].datas) {
											if (graphList.bJson[k].datas[p].json.properties != null) {
												if (graphList.bJson[i].datas[j].json.to._ref == graphList.bJson[k].datas[p]._refId) {
													console.log("终止接口-机箱内部", graphList.bJson[k].datas[p]._refId)
													var linkListStr = JSON.stringify(linkList)
													if (linkListStr.indexOf(graphList.bJson[k].datas[m].json.properties.uniqueId) == -1) {
														linkList.push([graphList.bJson[k].datas[m], graphList.bJson[k].datas[p]])
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
				console.log("linkList", linkList)
				for (const i in graphList.bJson) {
					for (let j = 0; j < graphList.bJson[i].datas.length; j++) {
						if (graphList.bJson[i].datas[j].json.properties == null) {
							removeByValue(graphList.bJson[i].datas, graphList.bJson[i].datas[j])
							j--
						}
					}
				}
				graph.clear();
				for (const i in graphList.bJson) {
					graph.parseJSON(graphList.bJson[i], { transform: false })
				}
				// graph.parseJSON(backAllCaseJsonTemp)
				//创建自己的连线
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
				console.log("linkGraphList", linkGraphList)
				//赋值连线的refid，将连线和两个重新画出的接口放到数组
				for (const i in linkList) {
					linkList[i][0]._refId = '1' + parseInt(1500 * Math.random())
					linkList[i][1]._refId = '1' + parseInt(1500 * Math.random())
					edgejson.json.from._ref = parseInt(linkList[i][0]._refId)
					edgejson.json.to._ref = parseInt(linkList[i][1]._refId)
					// console.log("edgejson",edgejson)
					//依附到板卡上，数组中无板卡数据无法依附
					/* for (const j in graphList.bJson) {
						for (const k in graphList.bJson[j].datas) {
							if (graphList.bJson[j].datas[k].json.properties != null && graphList.bJson[j].datas[k].json.properties.infName != null) {
								if (linkList[i][0].json.properties.uniqueId == graphList.bJson[j].datas[k].json.properties.uniqueId) {
									graphList.bJson[j].datas[k]._refId = linkList[i][0]._refId
								}
								if (linkList[i][1].json.properties.uniqueId == graphList.bJson[j].datas[k].json.properties.uniqueId) {
									graphList.bJson[j].datas[k]._refId = linkList[i][1]._refId
								}
							}
						}
					} */
					var linkGraphListStr = JSON.stringify(linkGraphList)
					if (linkGraphListStr.indexOf(linkList[i][0].json.properties.uniqueId) == -1) {
						linkGraphList.datas.push(linkList[i][0])
						linkGraphList.datas.push(linkList[i][1])
						linkGraphList.datas.push(edgejson)
					}
					linkGraphList = JSON.parse(JSON.stringify(linkGraphList))
				}
				//画出接口和连线
				graph.parseJSON(linkGraphList)
				console.log("linkGraphList", linkGraphList)
				setEditable(false);
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
			console.log("2626", data.from)
			if (data.from != null) {
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
					removeByValue(caseList, caseList[i])
					removeByValue(graphList.fJson, graphList.fJson[i])
					removeByValue(graphList.bJson, graphList.bJson[i])
				}
			}
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
			for (let i = 0; i < linkGraphList.datas.length; i++) {
				if (linkGraphList.datas[i].waitDelete != null) {
					removeByValue(linkGraphList.datas, linkGraphList.datas[i])
					i--
				}
			}
			/* if (linkGraphList.datas.length != 0) {
				for (let i = 0; i < linkGraphList.datas.length; i++) {
					if (linkGraphList.datas[i]._className == "Q.RectElement" && linkGraphList.datas[i].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
						if (linkGraphList.datas[i + 2]._className == "Q.Edge") {
							console.log("+++")
							console.log("linkGraphList.datas[i]",linkGraphList.datas[i])
							console.log("linkGraphList.datas[i + 1]",linkGraphList.datas[i + 1])
							console.log("linkGraphList.datas[i + 2]",linkGraphList.datas[i + 2])
							removeByValue(linkGraphList.datas, linkGraphList.datas[i])
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 1])
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 2])
							i = i - 3
							break
						}
						if (linkGraphList.datas[i + 1]._className == "Q.Edge") {
							console.log("---")
							removeByValue(linkGraphList.datas, linkGraphList.datas[i - 1])
							removeByValue(linkGraphList.datas, linkGraphList.datas[i])
							removeByValue(linkGraphList.datas, linkGraphList.datas[i + 1])
							i = i - 3
							break
						}
					}
				}
			} */
			linkMap.forEach(function (value, key) {
				if (key.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					linkMap.delete(key)
				}
				if (value.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					linkMap.delete(key)
				}
			});
			//第一次切换回正面
			if (typeof backAllCaseJsonTemp != 'undefined' && backAllCaseJsonTemp.datas.length != 0) {
				for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
					if (backAllCaseJsonTemp.datas[i].json.properties == null || backAllCaseJsonTemp.datas[i]._className == "Q.Edge") {
						removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
						i--
					}
				}
			}
			if (typeof backAllCaseJsonTemp != 'undefined' && backAllCaseJsonTemp.datas.length != 0) {
				for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
					if (backAllCaseJsonTemp.datas[i].json.properties != null && backAllCaseJsonTemp.datas[i].json.properties.infName != null
						&& backAllCaseJsonTemp.datas[i].json.properties.uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
						removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
						i--
					}
				}
			}
			/* for (let i = 0; i < frontCaseForDeployment.datas.length;) {
				if (frontCaseForDeployment.datas[i].json.properties.caseName != null && frontCaseForDeployment.datas[i].json.properties.uniqueId == selection[0].properties.uniqueId) {
					while (frontCaseForDeployment.datas[i + 1].json.properties.caseName == null) {
						removeByValue(frontCaseForDeployment.datas, frontCaseForDeployment.datas[i])
						i++
						if (i == frontCaseForDeployment.datas.length) {
							removeByValue(frontCaseForDeployment.datas, frontCaseForDeployment.datas[i])
							break
						}
					}
					break
				} else {
					i++
				}
			} */
			console.log("backAllCaseJsonTemp", backAllCaseJsonTemp)
			console.log("caseList", caseList)
			console.log("graphList", graphList)
			console.log("linkList", linkList)
			console.log("linkGraphList", linkGraphList)
			console.log("linkMap", linkMap)
		}, this);
	}
	function removeJsonDatas(n) {
		removeByValue(frontCaseForDeployment.datas, frontCaseForDeployment.datas[n])
		return ++n
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
			//	console.log("data",JSON.stringify(data.properties))
			console.log("data", data)
			/* var parent = document.getElementsByClassName('graph-editor__property')
			var child = document.getElementsByClassName('btn btn-primary')
			// console.log("parent",parent)
			// console.log("child",child)
			// console.log("++",parent[0] instanceof jQuery)
			// console.log("--",parent[0] instanceof HTMLElement)
			if (child.length != 0) {
				parent[0].removeChild(child[0]);
			} */

			if (data.properties.chipName != null) {
				//给ip输入框添加失去聚焦属性
				document.getElementById('IP').childNodes.item(0).setAttribute("onblur", "upperCase()")
				clickCheckedChip = data.properties
				//将板卡对应卡槽的slotnum赋给fSlotNum
				// console.log("propertySheet",propertySheet)
				/* var submitButton = document.createElement('input');
				submitButton.className = 'btn btn-primary '
				submitButton.value = 'Submit';
				propertySheet.dom.parentNode.appendChild(submitButton);
				submitButton.onclick = function () {
					var node = propertySheet.datas[0];
					// console.log("node",node)
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
					parent[0].removeChild(child[0]);
					console.log("chipListTemp", chipListTemp)
				} */
			}
			if (data.properties.chipName != null) {
				data.set('chipName', data._mn3.chipName);
				data.set('coreNum', data._mn3.coreNum);
				data.set('memSize', data._mn3.memSize);
				data.set('hrTypeName', data._mn3.hrTypeName);
				data.set('recvRate', data._mn3.recvRate);
			}
			if (data.properties.infName != null) {
				data.set('infName', data._mn3.infName);
				data.set('infRate', data._mn3.infRate);
				data.set('opticalNum', data._mn3.opticalNum);
			}
			if (data.properties.boardType != null) {
				if (data._mn3.boardType == 0) {
					data.set('showBoardType', 'calculateBoard');
				}
				if (data._mn3.boardType == 1) {
					data.set('showBoardType', 'FpgaBoard');
				}
				if (data._mn3.boardType == 2) {
					data.set('showBoardType', 'exchangeBoard');
				}
				if (data._mn3.boardType == 3) {
					data.set('showBoardType', 'interfaceBoard');
				}
				data.set('boardName', data._mn3.boardName);
			}
			// data.set('rackname', data._mn3.caseName);
			// data.set('boardnum', data._mn3.bdnum);
		}
		if (image == 'images/芯片.svg') {
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
		if (image == 'images/光纤口.svg') {
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
		if (image == 'images/前板卡.svg' || image == 'images/后板卡.svg') {
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
			if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END) {//data.properties.caseName != null && 
				// console.log("data", data)
				moveLocation = data.location
				if (data.properties.frontBoardList != null) {
					//正面机箱坐标
					// data.location
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