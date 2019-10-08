var deployment;
var json;
var link;
var linkArray;
var compName;
var cpux;
var cpuy;
var refid;
var compid;
var partname;
var arrows;
var rootnum = -0.5;
var rootnum1 = 0.5;
var rootnum2 = 0.5;
var rootnum3 = 0.5;
var rootnum4 = 0.5;
var baknum = -0.5;
var baknum1 = 0.5;
var baknum2 = 0.5;
var baknum3 = 0.5;
var baknum4 = 0.5;
var components;
// 子接收父参数
function handleMessageFromParent(event) {
	console.log("event.data", event.data)
	deployment = event.data.params[0].frontCaseForDeployment;
//	console.log('deployment',deployment);
	json = JSON.parse(deployment);
	console.log('json', json)
	linkArray = event.data.params[1][0];
	arrows = event.data.params[1][1];
	console.log('link---', linkArray);
	console.log('arrows', arrows)
	switch (event.data.cmd) {
		case 'getAllList':
			init();
			break;
	}
};
window.addEventListener("message", this.handleMessageFromParent) // 子接收方式二参数
var postMessageParentData = {
	cmd: "",//用于switch 判断
	params: []//具体参数
};
Q.registerImage('rack', 'images/机箱.svg'); //这里可以修改成：机箱.svg，但是位置大小需要做调整，你可以自己修改
Q.registerImage('card', 'images/前板卡.svg');
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

function init() {
	$('#editor').graphEditor({

		callback: initEditor,
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
//创建构建
function createItem(options) {
	var item = graph.createText('构件' + options.name);
	item.set('type', 'cell.item');
	var x = options.x, y = options.y;
	if (options.parent) {
		x = options.parent.x + options.parent.width * x;
		y = options.parent.y + options.parent.height * y;
		item.parent = item.host = options.parent;
	}
	item.x = x, item.y = y;

	item.set('group', options.group);
	var color = options.group == 'A' ? '#88FF88' : '#8888FF';
	item.setStyle(Q.Styles.LABEL_BACKGROUND_COLOR, color);

	var scale = 0.33;
	item.setStyle(Q.Styles.LABEL_SIZE, new Q.Size(15, 5));
	item.setStyle(Q.Styles.LABEL_FONT_SIZE, 12 * scale);
	item.setStyle(Q.Styles.LABEL_PADDING, 2 * scale);
	item.setStyle(Q.Styles.LABEL_RADIUS, 2 * scale);
	return item;
}

//创建连线
function createEdge(options) {
	var edge = graph.createEdge(options.from, options.to);
	edge.setStyle(Q.Styles.EDGE_COLOR, '#F88');
	edge.setStyle(Q.Styles.ARROW_TO, false);
	if (options.edgeType == 'top') {
		edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_TOP;
	} else if (options.edgeType == 'bottom') {
		edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_BOTTOM;
	} else if (options.edgeType == 'left') {
		edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_LEFT;
	} else if (options.edgeType == 'right') {
		edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_RIGHT;
	} else if (options.edgeType) {
		edge.edgeType = options.edgeType;
	}
	return edge;
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
//设置绘画主板
function createCard(slot) {
	//var bounds = slot.getBounds();
	var width = 62,
		height = 400;
	var card = createNode({
		image: 'card',
		x: width.x,
		y: height.y,
		width: width,
		height: height,
		radius: 8,
		fillColor: '#CDF'
	}, slot);
	card.set('type', 'card');
	card.name = '正面主板';
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
//绘画bak连线
function creatbakedge(startId, endId) {
	var edgejson = {
		"_className": "Q.Edge",
		"json": {
			"zIndex": 200,
			"styles": {
				"edge.color": "#03b6ff",
				//"arrow.to": true,
				"arrow.to.size": 1.8,
			},
			"from": {
				"_ref": startId
			},
			"to": {
				"_ref": endId

			},
			"edgeType": "extend.bottom",
			"properties": {
				"type": "edge",

			},
		}
	}
	dataJson.push(edgejson);
}

//绘画连线
function createdge(startId, endId) {
	var edgejson = {
		"_className": "Q.Edge",
		"json": {
			"zIndex": 200,
			"styles": {
				"edge.color": "#5bf000",
				//"arrow.to": true,
				"arrow.to.size": 1.8,
			},
			"from": {
				"_ref": startId
			},
			"to": {
				"_ref": endId

			},
			"edgeType": "extend.top",
			"properties": {
				"type": "edge",

			},
		}
	}
	dataJson.push(edgejson);
}

function creatbakgj(compName, cpux, cpuy, refid, compid, parttype, components) {
	/* 	//位置算法		
		var j = 9 * (baknum++);
		var gjjsonx = cpux + 5 + j;
		var gjjsony = cpuy + 30;
		if (gjjsonx >= cpux + 42) {
			gjjsony = cpuy + 35;
			var k = 5 * (baknum1++);
			gjjsonx = cpux + 5 + k;
			if (gjjsonx >= cpux + 42) {
				gjjsony = cpuy + 40;
				var k = 5 * (baknum2++);
				gjjsonx = cpux + 5 + k;
			}
		} */
	//位置算法
	var j = 4.5 * (baknum++);
	var gjjsonx = cpux + 5 + j;
	var gjjsony = cpuy + 30;
	if (gjjsonx >= cpux + 42) {
		gjjsony = cpuy + 34;
		var k = 4.5 * (baknum1++);
		gjjsonx = cpux + 5 + k;
		if (gjjsonx >= cpux + 42) {
			gjjsony = cpuy + 38;
			var k = 4.5 * (baknum2++);
			gjjsonx = cpux + 5 + k;
			if (gjjsonx >= cpux + 42) {
				gjjsony = cpuy + 42;
				var k = 4.5 * (baknum3++);
				gjjsonx = cpux + 5 + k;
				if (gjjsonx >= cpux + 42) {
					gjjsony = cpuy + 46;
					var k = 4.5 * (baknum4++);
					gjjsonx = cpux + 5 + k;
				}
			}
		}
	}
	var name = compName;
	var math = parseInt(10000 * Math.random());
	//json
	var bakgjjson = {
		"_className": "Q.Text",
		"json": {
			"zIndex": 200,
			"name": name,
			"parent": {
				"_ref": refid
			},
			"host": {
				"_ref": refid
			},
			"styles": {
				"label.background.color": "#000080",
				"label.font.size": 1,
				"label.padding": 0.3,
				"label.radius": 0.3,
				"label.size": {
					"_className": "Q.Size",
					"json": {
						"width": 6.5,
						"height": 2
					}
				},
			},
			"properties": {
				"type": "item",
				"group": "B",
				"compid": compid,
				"partname": partname,
				"parttype": parttype,
				"components": components,
				"cpuid": 11
			},
			"location": {
				"_className": "Q.Point",
				"json": {
					"x": gjjsonx,
					"y": gjjsony,
					"rotate": 0
				}
			}
		},
		"_refId": math
	}
	dataJson.push(bakgjjson);
}
//绘画根组件下的构件
function creatgj(compName, cpux, cpuy, refid, compid, parttype, components) {
	//位置算法
	var j = 4.5 * (rootnum++);
	var gjjsonx = cpux + 5 + j;
	var gjjsony = cpuy + 10;
	if (gjjsonx >= cpux + 42) {
		gjjsony = cpuy + 14;
		var k = 4.5 * (rootnum1++);
		gjjsonx = cpux + 5 + k;
		if (gjjsonx >= cpux + 42) {
			gjjsony = cpuy + 18;
			var k = 4.5 * (rootnum2++);
			gjjsonx = cpux + 5 + k;
			if (gjjsonx >= cpux + 42) {
				gjjsony = cpuy + 22;
				var k = 4.5 * (rootnum3++);
				gjjsonx = cpux + 5 + k;
				if (gjjsonx >= cpux + 42) {
					gjjsony = cpuy + 26;
					var k = 4.5 * (rootnum4++);
					gjjsonx = cpux + 5 + k;
				}
			}
		}
	}
	var name = compName;
	var math = parseInt(10000 * Math.random());
	//json
	var gjjson = {
		"_className": "Q.Text",
		"json": {
			"zIndex": 200,
			"name": name,
			"parent": {
				"_ref": refid
			},
			"host": {
				"_ref": refid
			},
			"styles": {
				"label.background.color": "#71e61c",
				"label.font.size": 1,
				"label.padding": 0.3,
				"label.radius": 0.3,
				"label.size": {
					"_className": "Q.Size",
					"json": {
						"width": 6.5,
						"height": 2
					}
				},
			},
			"properties": {
				"type": "item",
				"group": "A",
				"compid": compid,
				"partname": partname,
				"parttype": parttype,
				"components": components,
				"cpuid": 11
			},
			"location": {
				"_className": "Q.Point",
				"json": {
					"x": gjjsonx,
					"y": gjjsony,
					"rotate": 0
				}
			}
		},
		"_refId": math
	}
	dataJson.push(gjjson);
}



//回显绘画画布,监控
function initEditor(editor) {
	graph = editor.graph;
	dataJson = json.datas;
	//遍历构件进行转换赋值[测试代码]
/* 	var a = 1;
	for (index in json.datas) {
		if (json.datas[index].json.image == 'images/芯片.svg') {
			json.datas[index].json.properties.nodeID = a++;
		}
	} */
	//遍历构件数据
	for (var i in linkArray) {
		for (var l in json.datas) {
	//		console.log('json.datas[l].json.properties.nodeID',json.datas[l].json.properties.nodeID);
			//真数据放开 并nodeID替换成nodeID
			if(json.datas[l].json.image == 'images/芯片.svg'){
				if (linkArray[i].nodeName == json.datas[l].json.properties.nodeID) {
					//if (linkArray[i].nodeName == json.datas[l].json.properties.nodeID) {
					
					var math = parseInt(1000 * Math.random());
					//遍历dataJson取_refId
					refid = json.datas[l]._refId = math;
					cpux = json.datas[l].json.location.json.x;
					cpuy = json.datas[l].json.location.json.y;
				}
			}
		
		}
		//遍历构件
		for (var j in linkArray[i].rootPart) {
			//变量自增
			rootnum++;
			if (linkArray[i].rootPart[0].partName != null) {
				partname = linkArray[i].rootPart[j].partName;
				for (var k in linkArray[i].rootPart[j].components) {
					compName = linkArray[i].rootPart[j].components[k].compName;
					compid = linkArray[i].rootPart[j].components[k].compId;
					parttype = 'part'
					linkArray[i].rootPart[j]
					components = linkArray[i].rootPart[j].components[k];
					creatgj(compName, cpux, cpuy, refid, compid, parttype, components);
				}
			}
		}
			//变量恢复初始值
			rootnum = 0.5;
		//遍历备份构件
		for (var n in linkArray[i].backupParts) {
			baknum++;
			if (linkArray[i].backupParts[0].partName != null) {
				partname = linkArray[i].backupParts[n].partName;
				for (var m in linkArray[i].backupParts[n].components) {
					compName = linkArray[i].backupParts[n].components[m].compName;
					compid = linkArray[i].backupParts[n].components[m].compId;
					parttype = 'bakpart'
					components = linkArray[i].backupParts[n].components[m];
					creatbakgj(compName, cpux, cpuy, refid, compid, parttype, components);
				}
			}
		}
		//变量恢复初始值
		
		baknum = -0.5;
	}
	//遍历arrow绘画连线
	//console.log("dataJson", dataJson);
	//console.log("arrows", arrows);
	for (var k in arrows) {
		var startcomp = arrows[k].startItem;
		var endcomp = arrows[k].endItem;
		var startId = 0;
		var endId = 0;
		//遍历dataJson取_refId
		for (var m in dataJson) {
			if (dataJson[m].json.properties != null) {
				if (dataJson[m].json.properties.parttype == 'part') {
					if (dataJson[m].json.properties.compid == startcomp) {
						startId = dataJson[m]._refId;
					}
					if (dataJson[m].json.properties.compid == endcomp) {
						endId = dataJson[m]._refId;
					}
				}
			}
		}
		createdge(startId, endId);
	}

	//遍历bakarrow
	var bakstartIds = [];
	var bakendIds = [];	
console.log('dataJsondataJsondataJsondataJson',dataJson)
	//遍历arrows数据取得startcomp
	for (var k in arrows) {
		var startcomp = arrows[k].startItem;
		var endcomp = arrows[k].endItem;
		//遍历dataJson取_refId
		var res = true;
		for (var m in dataJson) {
			if (dataJson[m].json.properties != null) {
			if (dataJson[m].json.properties.parttype == 'bakpart' && dataJson[m].json.properties.compid === startcomp) {
				res = false;
				var bakstartrefId ={};	
					bakstartrefId.startId = dataJson[m]._refId;
					bakstartIds.push(bakstartrefId);
				}
			}	
		}	
		if( res == true ){
			console.log('```',arrows)
			for (var m in dataJson) {
			if (dataJson[m].json.properties != null) {						
				if (dataJson[m].json.properties.parttype == 'part' && dataJson[m].json.properties.compid == startcomp) {
					var bakstartrefId ={};	
					bakstartrefId.startId = dataJson[m]._refId;
					
					for(var i in arrows){
						if(arrows[i].startItem == startcomp ){
							var endItem1 = arrows[i].endItem;
							console.log('++arrows[i]',endItem1);
						}
					}
				var bakIds = [];
				for (var m in dataJson) {
					if (dataJson[m].json.properties != null) {					
					if (dataJson[m].json.properties.parttype == 'bakpart' && dataJson[m].json.properties.compid === endcomp) {
							var bakId = {};
							bakId.endId = dataJson[m]._refId;
							bakIds.push(bakId);
						}
					}	
				
				}		
						for( var i =1;i<=bakIds.length;i++){
							bakstartIds.push(bakstartrefId);
						}
					}			
				}	
			}
		}

				//遍历dataJson取_refId
				var res = true;
				for (var m in dataJson) {
					if (dataJson[m].json.properties != null) {
						
					if (dataJson[m].json.properties.parttype == 'bakpart' && dataJson[m].json.properties.compid === endcomp) {
						res = false;
							var bakendId = {};
							bakendId.endId = dataJson[m]._refId;
							bakendIds.push(bakendId);
						}
					}	
				
				}
				if( res == true ){
					for (var m in dataJson) {
					if (dataJson[m].json.properties != null) {						
						if (dataJson[m].json.properties.parttype == 'part' && dataJson[m].json.properties.compid == endcomp) {
								var bakendId = {};
								bakendId.endId = dataJson[m]._refId;
								var bakIds = [];
								for (var m in dataJson) {
									if (dataJson[m].json.properties != null) {					
									if (dataJson[m].json.properties.parttype == 'bakpart' && dataJson[m].json.properties.compid === startcomp) {
											var bakId = {};
											bakId.endId = dataJson[m]._refId;
											bakIds.push(bakId);
										}
									}	
								
								}	
								for( var i =1;i<=bakIds.length;i++){
									bakendIds.push(bakendId);
								}
							}			
						}	
					}
				}
	}
	console.log('bakstartIds',bakstartIds);
	console.log('bakendIds',bakendIds);
	for (var i in bakstartIds) {
		var startId = bakstartIds[i].startId;
		var endId = bakendIds[i].endId;
			creatbakedge(startId, endId);
	}

	graph.parseJSON(json);
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

		//   graph.interactionMode = null;
		// graph.interactionMode = editable ? Q.Consts.INTERACTION_MODE_DEFAULT : Q.Consts.INTERACTION_MODE_VIEW;

		$('.layout').borderLayout();
		graph.updateViewport();
	}
	setEditable(false);
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


	initToolbar();
	//以下保存按钮
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	button.textContent = '生成XML文件';
	button.className = 'boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function (evt) {

		var refid;
		var compAllArray = [];
		var rootParts = [];
		var backupParts = []
		var component;
		var nodeID;
		var rootPartsobj;
		var backupPartsobj;
		var components = [];
		var partname;
		var bakcompAllArray = [];
		var data = graph.toJSON();
		console.log("data", data);
		for (var i in data.datas) {

			if (data.datas[i].json.properties.type == 'item' && data.datas[i].json.properties.type != null) {
				if (data.datas[i].json.properties.group == 'A' && data.datas[i].json.properties.group != null) {
					var comps = {};
					//		console.log('data.datas[j]', data.datas[i]);					
					comps.compId = data.datas[i].json.properties.compid;
					var rootParts = [];
					var rootPart = {};
					rootPart.partName = data.datas[i].json.properties.partname;
					//	console.log('1111',data.datas[i].json.host._ref);

					//遍历芯片
					for (var j in data.datas) {
						if (data.datas[j].json.image == 'images/芯片.svg' && data.datas[j]._refId != null) {
							refid = data.datas[j]._refId;
							nodeID = data.datas[j].json.properties.nodeID;
						}
						if (data.datas[i].json.host._ref == refid) {
							rootPart.cpuid = nodeID;
							//		console.log('nodeID',nodeID)
						}
					}
					rootParts.push(rootPart);
					comps.rootPart = rootParts;
					compAllArray.push(comps);
					//	console.log('compAllArray', compAllArray);

				}
				//for (var k in data.datas) {
				if (data.datas[i].json.properties.group == 'B' && data.datas[i].json.properties.group != null) {
					var comps = {};
					var bakParts = [];
					var bakPart = {};
					comps.compId = data.datas[i].json.properties.compid;
					bakPart.partName = data.datas[i].json.properties.partname;

					//遍历芯片
					for (var j in data.datas) {
						if (data.datas[j].json.image == 'images/芯片.svg' && data.datas[j]._refId != null) {
							refid = data.datas[j]._refId;
							nodeID = data.datas[j].json.properties.nodeID;
						}
						if (data.datas[i].json.host._ref == refid) {
							bakPart.cpuid = nodeID;
							//	console.log('nodeID',nodeID)
						}
					}
					bakParts.push(bakPart);
					//	console.log('bakParts',bakParts);
					comps.bakPart = bakParts;
					bakcompAllArray.push(comps);
				}
				console.log('compAllArray', compAllArray);
				console.log('bakcompAllArray.comps', bakcompAllArray)
			}
		}
		var json = JSON.stringify(data);
		//	console.log("子向父传参=====", data);
		//	console.log("子向父传参");
		postMessageParentData.cmd = "submitJSON";
		postMessageParentData.params[0] = json;
		postMessageParentData.params[1] = compAllArray;
		postMessageParentData.params[2] = bakcompAllArray;
		window.parent.postMessage(postMessageParentData, "*")
		/* 		postMessageParentData.cmd = "sendXmlMap";
				postMessageParentData.params = compAllMap;
				window.parent.postMessage(postMessageParentData, "*") */
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

	function initToolbar() {

		//网状画布 
		var graph = editor.graph;
		//不可改变形状大小
		graph.editable = false;

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
	//右侧属性面板
	var propertySheet = editor.propertyPane;
	propertySheet.showDefaultProperties = false;
	//自定义属性面板
	propertySheet.getCustomPropertyDefinitions = function (data) {
		var type = data.get('type');
		var image = data.image;
		//	 console.log("data", data)
		//这里可以获得当前点击的图元对象
		graph.onclick = function (evt) {
			var data = graph.getElement(evt);
			if (data != null) {
				if (data.from == null) {
					console.log("data", data);
					data.set('chipname', data._mn3.chipName);
					data.set('corenum', data._mn3.coreNum);
					data.set('memsize', data._mn3.coreNum);
					data.set('boardname', data._mn3.boardName);
					data.set('infname', data._mn3.infName);
					data.set('ifrate', data._mn3.ifRate);
					data.set('opticalnum', data._mn3.opticalNum);
					data.set('compname', data.name);
					data.set('rackname', data._mn3.caseName);
					data.set('bdNum', data._mn3.bdNum);
					data.set('IP', data._mn3.IP);
					data.set('hrTypeName', data._mn3.hrTypeName);
				} else {
					data.set('startcomp', data.from.name);
					data.set('endcomp', data.to.name);
				}
			}
		}
		if (image == "images/芯片.svg") {
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
		if (type == 'port') {
			return {
				group: '接口属性',
				properties: [{
					client: 'infname',
					displayName: '接口名称'
				},
				{
					client: 'infRate',
					displayName: '接口速率'
				},
				{
					client: 'opticalnum',
					displayName: '光纤数量'
				}
				]
			}
		}
		if (type == 'card') {
			return {
				group: '主板属性',
				properties: [{
					client: 'boardname',
					displayName: '主板名称'
				}
				]
			}
		}

		if (image == "rack") {
			return {
				group: '机箱属性',
				properties: [{
					client: 'rackname',
					displayName: '机箱名称'
				},
				{
					client: 'bdNum',
					displayName: '主板数量'
				},
				]
			}
		}
		if (type == 'item') {
			return {
				group: '构件属性',
				properties: [/* {
					client: 'compid',
					displayName: '构件ID'
				}, */
				{
					client: 'compname',
					displayName: '构件名称'
				},
				{
					client: 'partname',
					displayName: '所属部件'
				}
				]
			}
		}
		if (type == 'edge') {
			return {
				group: '关系属性',
				properties: [{
					client: 'startcomp',
					displayName: 'start构件名称'
				},
				{
					client: 'endcomp',
					displayName: 'end构件名称'
				}
				]
			}
		}
	}

	graph.onPropertyChange('scale', function (evt) {
		graph.invalidateVisibility();
		var scale = graph.scale
		// Q.log(scale + ', ' + graph.scale);
		graph.graphModel.forEach(function (element) {
			// if(element.get('type') == 'cell.item'){
			//     element.setStyle(Q.Styles.ALPHA, )
			// }
			if (element instanceof Q.Edge) {
				element.setStyle(Q.Styles.EDGE_WIDTH, Math.min(3, 2 / scale));
			}
		})
	})

	function createCell(options) {
		options.width = 40;
		options.height = 40;
		var cell = createNode(options)
		return cell;
	}

	function createItem(options) {
		var item = graph.createText('构件' + options.name);
		item.set('type', 'cell.item');
		var x = options.x, y = options.y;
		if (options.parent) {
			x = options.parent.x + options.parent.width * x;
			y = options.parent.y + options.parent.height * y;
			item.parent = item.host = options.parent;
		}
		item.x = x, item.y = y;
		item.zIndex = 200;
		item.set('group', options.group);
		var color = options.group == 'A' ? '#88FF88' : '#8888FF';
		item.setStyle(Q.Styles.LABEL_BACKGROUND_COLOR, color);

		var scale = 0.15;
		item.setStyle(Q.Styles.LABEL_SIZE, new Q.Size(15, 5));
		item.setStyle(Q.Styles.LABEL_FONT_SIZE, 12 * scale);
		item.setStyle(Q.Styles.LABEL_PADDING, 2 * scale);
		item.setStyle(Q.Styles.LABEL_RADIUS, 2 * scale);
		return item;
	}

	function createEdge(options) {
		var edge = graph.createEdge(options.from, options.to);
		edge.setStyle(Q.Styles.EDGE_COLOR, '#F88');
		edge.setStyle(Q.Styles.ARROW_TO, true);
		edge.setStyle(Q.Styles.ARROW_TO_SIZE, '0.5');
		console.log('edge', edge)
		if (options.edgeType == 'top') {
			edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_TOP;
		} else if (options.edgeType == 'bottom') {
			edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_BOTTOM;
		} else if (options.edgeType == 'left') {
			edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_LEFT;
		} else if (options.edgeType == 'right') {
			edge.edgeType = Q.Consts.EDGE_TYPE_EXTEND_RIGHT;
		} else if (options.edgeType) {
			edge.edgeType = options.edgeType;
		}
		return edge;
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
				var slot = findSlot(data, evt);
				if (slot) {
					adaptBounds(data, slot);
				}
				return;
			}
			if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
				var type = data.get('type');
				if (type && (type == 'card' || type == 'port' || type =='item')) {
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
				var host = findCellHost(evt, data);
				//console.log('hostELEMENT_MOVE_START',host);
				//所点击当前构件的所属部件
				var partname = data._mn3.partname;
			//	console.log("data",data.id);
			//	console.log('partname',partname);
			/* 	for (const i in host.children.datas) {
					if(host.children.datas[i].id != data.id ){
						if(host.children.datas[i]._mn3.partname == data._mn3.partname ){ 

							var data = dragInfo.data;
							var oldSlot = data.parent;
							console.log("datadddddddt",data);
							console.log("oldSlot",oldSlot);
							var slot = findSlot(data, evt);
							console.log("slot",slot);
							/* if (!slot) {
								graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							}  *//* else {
								adaptBounds(data, slot);
							} *


							var host = findCellHost(evt, data);
							console.log('hostELEMENT_MOVE_START',host);
							//所点击当前构件的所属部件
							var partname = data._mn3.partname;
						//	console.log("data",data.id);
						//	console.log('partname',partname);
							for (const i in host.children.datas) {
								if(host.children.datas[i].id != data.id ){
									if(host.children.datas[i]._mn3.partname == data._mn3.partname ){ 
										var newsoltparent = data.parent;
										if(newsoltparent == oldSlot){
											alert(6666)
										//	graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
										}


										console.log("newdataparent",newsoltparent);
									
									//	console.log('	variable =============',	variable = false);
									//	if(variable != true){	
										//	showMessage('该芯片上还有其他同部件构件请一起移动','success',2000);
											//variable == false;
								//		}
										//	graph.selemctionModel.clear();
								
									
								}
						}
				
					}
							dragInfo = null;
							
						}}} */


			


			}
		})
	}
	function findCellHost(evt, element) {
		var xy = graph.toLogical(evt.event);
		var cell;
		function canDrop(data) {
			return data.image == 'images/芯片.svg';
		}

		function isItem(data) {
			return data.get('type') == 'item'
		}
		graph.forEachReverseVisibleUI(function (ui) {
			var target = ui.data;
			if (target == element || !(target instanceof Q.Node) || isItem(target) || target.isDescendantOf(element)) {
				return;
			}
			var bounds = graph.getUIBounds(ui);
			if (bounds.intersects(xy.x, xy.y)) {
				if (canDrop(target)) {
					cell = target;
				}
				return false;
			}
		});
		return cell;
	}
	graph.interactionDispatcher.addListener(function (evt) {
		if (evt.kind != Q.InteractionEvent.ELEMENT_CREATED && evt.kind != Q.InteractionEvent.ELEMENT_MOVE_END) {
			return;
		}
		var data = evt.data;
		//			console.log('EVT',evt.datas)//这里可以获取当前移动的所有图元
		if (!data || data.get('type') !== 'item') {
			return;
		}
		var host = findCellHost(evt, data);
		//		console.log('host',host);
		data.host = data.parent = host;
		evt.datas.forEach(function (data) { data.host = data.parent = host; })
		data._mn3.cpuid = host.properties.nodeID;
		/* 	for(var i in data){
			
				console.log('CPUid',	data._mn3.cpuid);
				data._mn3.cpuid  = host.properties.nodeID;
			} */
		//	console.log('22222EVT',evt.datas)
	})

	graph.onclick = function (evt) {
		var data = graph.getElement(evt);
		if (data) {
			var children = data.toChildren();
			console.log("children", children);

		}
	}
	//按这种方式导出部分json
	function exportNodeJSON(parent) {
		return graph.toJSON(false, {
			filter: function (data) {
				return data == parent || data.isDescendantOf(parent);
			}
		})
	}
	//右键选中其部件下所有构件功能
	var chipIds = [];
	var chipData = [];
	graph.popupmenu.getMenuItems = function (graph, data, evt) {
		if (data) {
			return [
				/* 			{
								text: '选中芯片', action: function () {
									var data = graph.getElement(evt);
									var chipId = data.id;
									chipIds.push(chipId);
									console.log('************chipIds',chipIds);
								}
							
							}, */
				{
					text: '选中其部件下所有构件', action: function () {
						var data = graph.getElement(evt);
						var partname = data._mn3.partname;
						graph.forEach(function (data) {
							if (data._mn3.partname == partname) {
								console.log(data);
								graph.select(data);
							}
						})
						if (data._mn3.partname == null && data.image != "images/芯片.svg") {
							alert('请选择构件');
						}
					}

				},
				/* 			{
								text: '显示选中芯片', action: function () {
									var data = graph.getElement(evt);
									var partname = data._mn3.partname;
									graph.clear();
									for(var i in chipIds){
										createCard() ;
				
								/* 		function createCard() {
											//var bounds = slot.getBounds();
											var port = createNode({
												image: 'images/cpu.svg',
												x: 0,
												y: 0,
												width: 400,
												height: 400
											}, slot);
											port.set('type', 'port');
											port.name = 'DSP芯片';
											port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
											port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
											return port;
										} *
									}
								}
							
							}, */
				/* {
					text: '选中芯片显示芯片', action: function () {
						var data = graph.getElement(evt);
					//	if( data.image  == "images/芯片.svg"){
							console.log(data);
						//	graph.select(data);
							var deploymentsJson = graph.toJSON();
							console.log('deploymentsJson',deploymentsJson);
							console.log('chipIds',chipIds);
						
							//	console.log('deploymentsJson.datas[i]._refId',deploymentsJson.datas[i]._refId);
							var deploymentsJsonbak = graph.toJSON();	
					
							for(var j in chipIds){
							for ( var i in deploymentsJson.datas){
								
								console.log('2*********chipIds',chipIds[j]);
								console.log('**deploymentsJson.datas[i]',deploymentsJson.datas[i]);
								if(deploymentsJson.datas[i] != null){
									if(chipIds[j] == deploymentsJson.datas[i]._refId){
										//delete deploymentsJson.datas[i];
									//deploymentsJson.datas = []
									chipData.push(deploymentsJsonbak.datas[i]);
									}
								}
								
								}
							}
							console.log("&&&&chipData",chipData)
							console.log('deploymentsJson222',deploymentsJson);
							deploymentsJson.datas = []
							deploymentsJson.datas=chipData;
							graph.clear();
			
							graph.parseJSON(deploymentsJson, { transform: false })
					//	}
					}
				} */
			];

		}
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