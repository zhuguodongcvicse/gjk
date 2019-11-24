var chip_json = [];
var infArr;
var chipArr;
var boardArr;
var infArr_optical = [];
var infArr_ePort = [];
var infArr_Port = [];
var infArr_serial = [];
var infId = []
var backBoardInfList = []
var startId
var chip_id;
var infCount = 1
var infOfChip = []
var startInfNameBySelect = []
var endInfNameBySelect = []
var infOfExchangeCpuArr = []
var ExchangeCpu
var dragCpuList = []
var selectedStartInfs = []
var selectInfCount = 0
var showStartCpuList = []
var showEndCpuList = []
var showStartInfList = []
var showEndInfList = []
var showExternalInf = []
var StartInfOfChip = new Array()
var EndInfOfChip = []
var selectStartCpuValue
var selectEndCpuValue
var sel5value
var linkTypeList = []
var ioTypeList = []
var chipList = []
var chipIDNum = 0
var infIDNum = 0
var ifDeleteFlag = 0
var allInfList = []
var InternalLinkArr = []
var calculateBoardLinkType
var calculateBoardIoType
Q.registerImage('rack', 'images/Crate.svg'); //这里可以修改成：机箱.svg，但是位置大小需要做调整，你可以自己修改
Q.registerImage('card', 'images/BeforeTheBoard.svg');
Q.registerImage('behindcard', 'images/AfterTheBoard.svg');
Q.registerImage('cell', 'images/Chip.svg');
Q.registerImage('optical', 'images/OpticalFiberMouth.svg');
Q.registerImage('port', 'images/RoundMouth.svg');
Q.registerImage('serial', 'images/SerialPort.svg');
Q.registerImage('ePort', 'images/InternetAccess.svg');

function RectElement() {
	Q.doSuperConstructor(this, RectElement, arguments);
	this.resizable = false;
	this.rotatable = false;
	this.addOutProperty('width');
	this.addOutProperty('height');
	this.addOutProperty('radius');
	this.anchorPosition = Q.Position.LEFT_TOP;
	this.setStyle(Q.Styles.SHAPE_FILL_COLOR, "#EEE");
	this.setStyle(Q.Styles.SHAPE_STROKE, 0);
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

// 子接收父参数
function handleMessageFromParent(event) {
	allInfList = event.data.params[1]
	for (const i in allInfList) {
		allInfList[i].ifShowInSelect = 0
	}
	infArr = event.data.params[1]
	chipArr = event.data.params[0]
	boardArr = event.data.params[2]
	calculateBoardLinkType = event.data.params[3]
	calculateBoardIoType = event.data.params[4]
	/* for (const i in infArr) {
		if(infArr[i].infType == 3){
			infOfChip.push(infArr[i])
		}
	} */
	// console.log("chipArr", chipArr)
	switch (event.data.cmd) {
		case 'boarddesign':
			//芯片json
			for (var i = 0; i < chipArr.length; i++) {
				var chipData = JSON.parse(chipArr[i].chipData)
				//console.log("chipData",chipData)
				infId = chipData.datas[0].json.properties.infList
				chip_json[i] = {
					properties: {
						//	name: chipArr[i].chipName,
						size: {
							width: 52,
							height: 50
						},
							editable: false
					},
					clientProperties: {
						type: 'port',
						chip_id: chipArr[i].id,
						id: chipArr[i].id,
						chipName: chipArr[i].chipName,
						coreNum: chipArr[i].coreNum,
						memSize: chipArr[i].memSize,
						recvRate: chipArr[i].recvRate,
						hrTypeName: chipArr[i].hrTypeName,
						infOfChipList: chipData.datas[0].json.properties.infOfChipList,
						infId: infId
					},
					showLabel: true,
					type: 'Q.RectElement',
					image: 'images/Chip.svg',
					styles: {
						'label.color': '#FFF',
						'label.position': 'cm',
						'label.anchor.position': 'cm',
						'label.font.size': 8
					},
				}
			}
			//console.log("chip_json", chip_json)
			//光纤json
			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 2) {
					infArr_optical[i] = {
						properties: {
							size: {
								width: 24,
								height: 23
							}
						},
						//_refId = infArr[i].id,
						clientProperties: {
							type: 'port',
							id: infArr[i].id,
							infName: infArr[i].infName,
							infRate: infArr[i].infRate,
							infType: infArr[i].infType,
							ioType: infArr[i].ioType,
							opticalNum: infArr[i].opticalNum,
						},
						showLabel: true,
						type: 'Q.RectElement',
						image: 'images/OpticalFiberMouth.svg',
					}
				}
			}
			//网口json
			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 0) {
					infArr_ePort[i] = {
						properties: {
							size: {
								width: 24,
								height: 23
							}
						},
						clientProperties: {
							type: 'port',
							id: infArr[i].id,
							infName: infArr[i].infName,
							infRate: infArr[i].infRate,
							ioType: infArr[i].ioType,
							infType: infArr[i].infType
						},
						showLabel: true,
						type: 'Q.RectElement',
						image: 'images/InternetAccess.svg',
					}
				}
			}
			//圆口json
			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 4) {
					infArr_Port[i] = {
						properties: {
							size: {
								width: 24,
								height: 23
							}
						},
						clientProperties: {
							type: 'port',
							id: infArr[i].id,
							infName: infArr[i].infName,
							infRate: infArr[i].infRate,
							ioType: infArr[i].ioType,
							infType: infArr[i].infType
						},
						showLabel: true,
						type: 'Q.RectElement',
						image: 'images/RoundMouth.svg',
					}
				}
			}
			//串口json
			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 1) {
					infArr_serial[i] = {
						properties: {
							size: {
								width: 24,
								height: 23
							}
						},
						clientProperties: {
							type: 'port',
							id: infArr[i].id,
							infName: infArr[i].infName,
							ioType: infArr[i].ioType,
							infRate: infArr[i].infRate,
							infType: infArr[i].infType
						},
						showLabel: true,
						type: 'Q.RectElement',
						image: 'images/SerialPort.svg',
					}
				}
			}
			if (boardArr.boardType == 0 || boardArr.boardType == 1) {
				init();
			} else {
				initbehind();
			}
	}
};

function init() {
	$('#editor').graphEditor({
		images: [{
			name: '芯片',
			images: chip_json
		}
		],
		callback: initEditor
	});
}

function initbehind() {
	$('#editor').graphEditor({
		images: [{
			name: '光纤口',
			images: infArr_optical
		}, {
			name: '网口',
			images: infArr_ePort
		}, {
			name: '圆口',
			images: infArr_Port
		},
		{
			name: '串口',
			images: infArr_serial
		}
		],
		callback: initEditor

	});
}
var EVENT_CREATE_ELEMENT_BY_JSON = 'create.element.by.json';

function ondropLoadJSON(evt, graph, center, options) {
	var url = options.url;
	if (!url) {
		return;
	}
	Q.loadJSON(url, function (json) {
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
		graph.interactionDispatcher.onEvent({
			kind: EVENT_CREATE_ELEMENT_BY_JSON,
			datas: result,
			roots: roots,
			event: evt
		})
	})
}

function loadJSONInParent(graph, json, center, parent) {
	parent = parent || graph.currentSubnetwork;
	var result = graph.parseJSON(json);
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
	slot.setStyle(Q.Styles.SHAPE_STROKE_STYLE, Q.toColor(0x01000000));
	slot.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
	slot.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
	if (!parent._slots) {
		parent._slots = {};
	}
	return parent._slots[id] = slot;
}

function createRack() {
	var width = 700,
		height = 500;
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
		itemHeight = 380,
		count = 10;
	var i = 0;
	while (i++ < count) {
		addSlot(rack, i, startX, startY, itemWidth, itemHeight, 'card');
		startX += itemWidth + gap;
	}
	return rack;
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

function removeByValue(arr, val) {
	for (var i = 0; i < arr.length; i++) {
		if (arr[i] == val) {
			arr.splice(i, 1);
			break;
		}
	}
}

//uniqueId = uuid(10,62)
//设置绘画后主板
function createBehindCard(slot) {

	//var bounds = slot.getBounds();
	var width = 62,
		height = 400;
	var card = createNode({
		image: 'images/AfterTheBoard.svg',
		x: width.x,
		y: height.y,
		width: width,
		height: height,
		radius: 8,
		fillColor: '#CDF'
	}, slot);
	card.set('type', 'card');
	card.set('boardName', boardArr.boardName);
	card.set('boardType', boardArr.boardType);
	card.set('backBoardInfList', backBoardInfList);
	card.set('id', boardArr.id);
	//card.set('uniqueId', uniqueId);
	//card.name = '后面主板';
	card.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_TOP);
	card.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_TOP);

	var startX = 5,
		startY = 40,
		gap = 10,
		itemWidth = width - 40,
		itemHeight = 20,
		startX1 = 35,
		itemWidth1 = width - 40,
		itemHeight1 = 20,
		count = 12;
	var i = 0;
	while (i++ < count) {
		addSlot(card, i, startX, startY, itemWidth, itemHeight, 'port');
		addSlot(card, i, startX1, startY, itemWidth1, itemHeight1, 'port');
		startY += itemHeight + gap;
	}
	return card;
}
//设置绘画前主板
function createCard(slot) {
	//var bounds = slot.getBounds();
	var width = 62,
		height = 400;
	var card = createNode({
		image: 'images/BeforeTheBoard.svg',
		x: width.x,
		y: height.y,
		width: width,
		height: height,
		radius: 8,
		fillColor: '#CDF',
	}, slot);
	card.set('type', 'card');
	card.set('boardName', boardArr.boardName);
	card.set('cpuNum', boardArr.cpuNum);
	card.set('boardType', boardArr.boardType);
	card.set('id', boardArr.id);
	//card.name = '正面主板';
	card.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_TOP);
	card.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_TOP);

	var startX = 5,
		startY = 40,
		gap = 10,
		itemWidth = width - 10,
		itemHeight = 50,
		count = 4;
	var i = 0;
	while (i++ < count) {
		addSlot(card, i, startX, startY, itemWidth, itemHeight, 'port');
		startY += itemHeight + gap;
	}
	return card;
}

//设置绘画芯片
function createPort(slot) {
	console.log("***")
	var bounds = slot.getBounds();
	var port = createNode({
		image: 'cell',
		x: bounds.x,
		y: bounds.y,
		width: bounds.width,
		height: bounds.height
	}, slot);
	port.set('type', 'port');
	port.name = '001';
	port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
	port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
	return port;
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
	//保存
	//以下保存按钮
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	button.textContent = '板卡保存';
	button.className = 'boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function (evt) {
		//芯片赋值
		var data = graph.toJSON();
		data.datas[0].json.properties.chipList = chipList
		//json串保存交换芯片
		if (boardArr.boardType == 0) {
			data.datas[0].json.properties.ExchangeCpu = infOfExchangeCpuArr
			infOfExchangeCpuArr = []
		} else if (boardArr.boardType == 1) {
			//console.log("showStartInfList",showStartInfList);
			for (const i in showStartInfList) {
				InternalLinkArr.push([showStartCpuList[i], showStartInfList[i], showEndCpuList[i], showEndInfList[i]])
			}
			data.datas[0].json.properties.InternalLink = InternalLinkArr
		}
		var json = JSON.stringify(data);
		postMessageParentData.cmd = "submitJSON";
		postMessageParentData.params = json;
		console.log("data", data);
		//closePane()
		window.parent.postMessage(postMessageParentData, "*")
	}
	function initToolbar() {
		//网状画布 
		/* var graph = editor.graph;
		//不可改变形状大小
		//	graph.editable = true; */

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

	//绘画主板
	if (boardArr.boardType == 0 || boardArr.boardType == 1) {
		createCard();
		graph.moveToCenter();
		//console.log("boardArr.boardType",boardArr.boardType)
	} else {
		createBehindCard();
		graph.moveToCenter();
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
			//var listenGraph =  graph.toJSON()
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
			//判断芯片节口是否在主板上不是移除
			if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED && evt.data instanceof Q.Node) {
				var node = evt.data;
				// console.log("node1",node)
				if (!(node instanceof Q.Node)) {
					return
				}
				var currentElement = graph.getElement(evt.event);
				console.log("currentElement", currentElement)
				if (currentElement && currentElement.image == 'images/BeforeTheBoard.svg') {
					//	node.host = currentElement;
				} else if (currentElement && currentElement.image == 'images/AfterTheBoard.svg') {
					//	node.host = currentElement;
				} else if (currentElement && currentElement.image == 'images/Chip.svg') {
					graph.removeElement(node);
					return
				} else {
					graph.removeElement(node);
					return
				}
				//拖拽触发
				evt.data.properties.num = infCount++
				evt.data.properties.uniqueId = uuid(15, 62)
				// console.log("evt.data", evt.data)
				//给接口赋唯一标识
				if (evt.data.properties.infOfChipList != null) {
					for (const key in evt.data.properties.infOfChipList) {
						evt.data.properties.infOfChipList[key].uniqueId = evt.data.properties.uniqueId + "_" + evt.data.properties.infOfChipList[key].uniqueId
					}
				}
				//判断拖拽的是不是芯片
				if (evt.data.properties.chipName != null) {
					evt.data.properties.ID = chipIDNum++
					evt.data.name = '' + evt.data.properties.ID;
					//向临时数组添加芯片
					dragCpuList.push(evt.data.properties)
					//向数组添加芯片，最终赋给板卡保存
					chipList.push(evt.data.properties)
					//深拷贝
					chipList = JSON.parse(JSON.stringify(chipList))
					//根据板卡类型进行操作
					if (boardArr.boardType == 0) {
						infOfChip = evt.data.properties.infOfChipList
					}
					if (boardArr.boardType == 1 && selectInfCount != 0) {
						for (const i in evt.data.properties.infOfChipList) {
							/* if (evt.data.properties.infOfChipList[i].infName.indexOf(evt.data.properties.infOfChipList[i].uniqueId)) {
								evt.data.properties.infOfChipList[i].infName = evt.data.properties.infOfChipList[i].infName + evt.data.properties.infOfChipList[i].uniqueId
							} */
							StartInfOfChip.push(evt.data.properties.infOfChipList[i])
						}
					}
				}
				//如果是接口，添加到数组
				if (evt.data.properties.infName != null) {
					evt.data.properties.ID = infIDNum++
					backBoardInfList.push(evt.data.properties)
				}

				var slot = findSlot(data, evt);
				if (slot) {
					adaptBounds(data, slot);
				}
				return;
			}
			if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
				var type = data.get('type');
				console.log("type",type)
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
	//不许缩放比例
	//	graph.enableWheelZoom = false;
	//删除
	graph.removeSelectionByInteraction = false;
	graph.removeSelectionByInteraction = function (evt) {
		var selection = this.selectionModel.datas;
		if (!selection || selection.length == 0) {
			return false;
		}
		for (var i in selection) {
			if (selection[i].properties.type == "port") {
		Q.confirm("是否删除？", function () {
			var selection = this.removeSelection();
			// console.log("selection[0].properties", JSON.stringify(selection[0].properties))
			for (const i in chipList) {
				if (chipList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					removeByValue(chipList, chipList[i])
				}
			}
			dragCpuList = JSON.parse(JSON.stringify(dragCpuList))
			if (boardArr.boardType == 1) {
				// console.log("dragCpuList",dragCpuList)
				for (let i = 0; i < showStartCpuList.length;) {
					if (showStartCpuList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
						for (const j in dragCpuList) {
							if (showEndCpuList[i].uniqueId.indexOf(dragCpuList[j].uniqueId) != -1) {
								dragCpuList[j].infOfChipList.push(showEndInfList[i])
							}
						}
						removeByValue(showStartCpuList, showStartCpuList[i])
						removeByValue(showStartInfList, showStartInfList[i])
						removeByValue(showEndCpuList, showEndCpuList[i])
						removeByValue(showEndInfList, showEndInfList[i])
						i = 0
					} else if (showEndCpuList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
						for (const j in dragCpuList) {
							if (showStartCpuList[i].uniqueId.indexOf(dragCpuList[j].uniqueId) != -1) {
								dragCpuList[j].infOfChipList.push(showStartInfList[i])
							}
						}
						removeByValue(showStartCpuList, showStartCpuList[i])
						removeByValue(showStartInfList, showStartInfList[i])
						removeByValue(showEndCpuList, showEndCpuList[i])
						removeByValue(showEndInfList, showEndInfList[i])
						i = 0
					} else {
						i++
					}
				}
			}
			for (const i in dragCpuList) {
				if (dragCpuList[i].uniqueId.indexOf(selection[0].properties.uniqueId) != -1) {
					removeByValue(dragCpuList, dragCpuList[i])
				}
			}
			//  console.log("infOfExchangeCpuArr",infOfExchangeCpuArr)
		}, this);
	}}
	}

	var propertySheet = editor.propertyPane;
	propertySheet.showDefaultProperties = false;

	//自定义属性面板
	propertySheet.getCustomPropertyDefinitions = function (data) {
		//	console.log("data", data)
		//	console.log("propertySheet", propertySheet)
		var type = data.get('type');
		var image = data.image;
		// console.log("tppe", type)
		//这里可以获得当前点击的图元对象
		graph.onclick = function (evt) {
			var data = graph.getElement(evt);
			// console.log("data",data);
			// console.log("typeof(data)",typeof(data));
			if (typeof (data) != 'undefined') {
				if (data.properties.chipName != null) {
					data.set('chipname', data.properties.chipName);
					data.set('corenum', data.properties.coreNum);
					data.set('memsize', data.properties.memSize);
					data.set('recvRate', data.properties.recvRate);
					data.set('hrTypeName', data.properties.hrTypeName);
				}

				// data.set('boardname', data.properties.boardName);
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
				if (data.properties.infName != null) {
					data.set('infName', data.properties.infName);
					data.set('fiberspeed', data.properties.infRate);
					data.set('fibernum', data.properties.opticalNum);
					data.set('ioType', data.properties.ioType);
				}

				console.log("data", data);
			}
		}
		if (image == 'images/Chip.svg') {
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
					client: 'fiberspeed',
					displayName: '接口速率'
				},
				{
					client: 'fibernum',
					displayName: '光纤数量'
				},
				{
					client: 'ioType',
					displayName: 'I/O类型'
				}
				]
			}
		}
		if (type == 'card') {
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
					client: 'fiberspeed',
					displayName: '接口速率'
				},
				{
					client: 'ioType',
					displayName: 'I/O类型'
				}
				]
			}
		}
	}
	initInteraction();
}