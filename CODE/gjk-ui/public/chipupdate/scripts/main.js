var inf_json = [];
var inf_uuid;
var chipArr;
var infArr;
var infListTemp = []
Q.registerImage('rack', 'images/机箱.svg'); //这里可以修改成：机箱.svg，但是位置大小需要做调整，你可以自己修改
Q.registerImage('card', 'images/前板卡.svg');
Q.registerImage('behindcard', 'images/后板卡.svg');
Q.registerImage('cell', 'images/芯片.svg');
Q.registerImage('optical', 'images/光纤口.svg');
Q.registerImage('port', 'images/圆口.svg');
Q.registerImage('serial', 'images/串口.svg');
Q.registerImage('ePort', 'images/网口.svg');

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
	infArr = event.data.params[0]
	chipArr = event.data.params[1]

	switch (event.data.cmd) {
		case 'updateChip':
			console.log(event.data.params);
			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 3) {
					//		console.log("infArr[i].chipInf",infArr[i].chipInf)		
					inf_json[i] = {
						properties: {
							size: {
								width: 20,
								height: 20
							},
							zIndex: 50
						},
						showLabel: true,
						clientProperties: {
							type: 'inf',
							//inf_uuid: infArr[i].id,
							infName: infArr[i].infName,
							infRate: infArr[i].infRate,
							infType: infArr[i].infType,
							id: infArr[i].id
						},
						type: 'Q.RectElement',
						image: 'images/黄色圆口.svg',
					}
				}
			}
			init();
	}
};

function deepClone(data){
	var type = getObjType(data)
	var obj
	if (type === 'array') {
	  obj = []
	} else if (type === 'object') {
	  obj = {}
	} else {
	  // 不再具有下一层次
	  return data
	}
	if (type === 'array') {
	  for (var i = 0, len = data.length; i < len; i++) {
		obj.push(deepClone(data[i]))
	  }
	} else if (type === 'object') {
	  for (var key in data) {
		obj[key] = deepClone(data[key])
	  }
	}
	return obj
}

function getObjType(obj){
	var toString = Object.prototype.toString
	var map = {
	  '[object Boolean]': 'boolean',
	  '[object Number]': 'number',
	  '[object String]': 'string',
	  '[object Function]': 'function',
	  '[object Array]': 'array',
	  '[object Date]': 'date',
	  '[object RegExp]': 'regExp',
	  '[object Undefined]': 'undefined',
	  '[object Null]': 'null',
	  '[object Object]': 'object'
	}
	if (obj instanceof Element) {
	  return 'element'
	}
	return map[toString.call(obj)]
}

function init() {
	$('#editor').graphEditor({
		images: [{
			name: '芯片接口',
			images: inf_json
		}
		],
		callback: initEditor
	});
}
var EVENT_CREATE_ELEMENT_BY_JSON = 'create.element.by.json';

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

//设置绘画芯片
function createCard(slot) {
	//var bounds = slot.getBounds();
	var port = createNode({
		image: 'images/cpu.svg',
		//x: 0,
		//	y: 0,
		width: 400,
		height: 400
	}, slot);
	port.set('type', 'port');
	port.set('chipName', chipArr.chipName);
	port.set('coreNum', chipArr.coreNum);
	port.set('memSize', chipArr.memSize);
	port.set('recvRate', chipArr.recvRate);
	port.set('infOfChipList', infOfChipList);
	port.name = 'DSP芯片';
	port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
	//	port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
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
	//以下保存按钮
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	button.textContent = '芯片保存';
	button.className='boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function () {
		var json = graph.toJSON()
		//console.log("infListTemp", JSON.stringify(infListTemp))
		//如果用户拖拽添加了接口，则将合并后的数组赋值给json
		if(infListTemp.length != 0){
			json.datas[0].json.properties.infOfChipList = []
			json.datas[0].json.properties.infOfChipList = infListTemp
		}
		//遍历芯片上所有的接口，给接口赋自增ID和默认iotype
		var IDNum = 0
		for (const i in json.datas[0].json.properties.infOfChipList) {
			json.datas[0].json.properties.infOfChipList[i].ID = IDNum++
			json.datas[0].json.properties.infOfChipList[i].ioType = 2
		}
		// console.log("json", JSON.stringify(json))
		var jsonStr = JSON.stringify(json)
		console.log("json",json)
		postMessageParentData.cmd = "submitJSON";
		postMessageParentData.params = jsonStr;
		window.parent.postMessage(postMessageParentData, "*")
	}
	function initToolbar() {
		//网状画布 
		var graph = editor.graph;
		//不可改变形状大小
		//graph.editable = false; 

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

	//绘画芯片
	var chipObj = JSON.parse(chipArr.chipData);
	graph.parseJSON(chipObj);
	graph.moveToCenter();

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
				//拖拽触发
				//data.datas[0]._refId = 6666
				evt.data.properties.uniqueId = uuid(15, 62)
				console.log("graph.toJSON()",graph.toJSON())
				infListTemp = deepClone(graph.toJSON().datas[0].json.properties.infOfChipList)
				infListTemp = deepClone(infListTemp.concat(evt.data.properties))
				console.log("infListTemp",infListTemp)
				var node = evt.data;
				if (!(node instanceof Q.Node)) {
					return
				}
				var currentElement = graph.getElement(evt.event);
				if (currentElement && currentElement.name === 'DSP芯片') {
					node.host = currentElement;
				} else {
					graph.removeElement(node);
				}
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
				var slot = findSlot(data, evt);
				if (!slot || slot == oldSlot) {
					//	graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
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
		Q.confirm("是否 确认删除", function () {
			var selection = this.removeSelection();
			console.log("selection",selection)
			/* for (const i in graph.toJSON().datas[0].json.properties.infOfChipList) {
				if (selection[0].) {
					const element = object[i];
					
				}
			} */
			/* if (selection) {
				var event = new InteractionEvent(this, InteractionEvent.ELEMENT_REMOVED, evt, selection);
				this.onInteractionEvent(event);
			} */
		}, this);
	}

	var propertySheet = editor.propertyPane;
	propertySheet.showDefaultProperties = false;
	//自定义属性面板
	propertySheet.getCustomPropertyDefinitions = function (data) {
		//console.log("propertySheet",propertySheet)
		var type = data.get('type');
		var image = data.image;
		//这里可以获得当前点击的图元对象
		graph.onclick = function (evt) {
			// console.log("graph.toJSON()", graph.toJSON())
			var data = graph.getElement(evt);
			console.log("data",data)
			var infName = data._mn3.infName;
			// var infRate = data._mn3.infRate;
			var id = data._mn3.id;
			// var infType = data._mn3.infType;
			var ioType = data._mn3.ioType;
			data.set('infname', infName);
			// data.set('infRate', infRate);
			data.set('id', id);
			// data.set('inftype', infType);
			data.set('ioType', ioType);
			var chipName = chipArr.chipName;
			var coreNum = chipArr.coreNum;
			var memSize = chipArr.memSize;
			var recvRate = chipArr.recvRate;
			data.set('chipName', chipName);
			data.set('coreNum', coreNum);
			data.set('memSize', memSize);
			data.set('recvRate', recvRate);
		}

		if (type == 'inf') {
			return {
				group: '接口属性',
				properties: [{
					client: 'infname',
					displayName: '接口名称',
				},
				{
					client: 'infRate',
					displayName: '接口速率'
				}/* ,
				{
					client: 'inftype',
					displayName: '接口类型'
				} */]
			}
		}

		if (type == 'port') {
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
				}/* ,
				{
					client: 'hrTypeName',
					displayName: 'hrTypeName'
				} */
				]
			}
		}
	}
	initInteraction();

}
