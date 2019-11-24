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
var num  = 0.4;
var num1 = 0.4;
var num2 = 0.4;
var num3 = 0.4;
var num4 = 0.4;
var numbak = 0.4;
var numbak1 = 0.4;
var numbak2 = 0.4;
var numbak3 = 0.4;
var numbak4 = 0.4;
var components;
var verdict = false;
var movedate;
var CHIPdate = [];
var deploymentsJsonbak;
var bakstartIds = [];
var bakendIds = [];
var GAT;

// 子接收父参数
function handleMessageFromParent(event) {
	deployment = event.data.params[0].frontCaseForDeployment;
	json = JSON.parse(deployment);
	linkArray = event.data.params[1][0];
	arrows = event.data.params[1][1];
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
//绘画备份连线
function creatbakedge(startId, endId) {
	var edgejson = {
		"_className": "Q.Edge",
		"json": {
			"zIndex": 200,
			"styles": {
				"edge.color": "#03b6ff",
				"arrow.to": true,
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
				"arrow.to": true,
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

function creatbakgj(compName, cpux, cpuy, refid, compid, parttype, components, GAT) {
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
					if (gjjsonx >= cpux + 42) {
						gjjsony = cpuy + 46;
						gjjsonx = cpux + 40;
					}
				}
			}
		}
	}
	var name = compName;
	var math = parseInt(100000 * Math.random());
	//绘画备份组件中的构建
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
				"label.background.color": "#03b6ff",
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
				"cpuid": 11,
				'GAT': GAT,
				'Direction':"from"
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
function creatgj(compName, cpux, cpuy, refid, compid, parttype, components, GAT) {
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
					if (gjjsonx >= cpux + 42) {
						gjjsony = cpuy + 26;
						gjjsonx = cpux + 40 ;		
					}
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
				"cpuid": 11,
				'GAT': GAT,
				'Direction':"from"
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
			//真数据放开 并nodeID替换成nodeID
			if (json.datas[l].json.image == 'images/Chip.svg') {
				if (linkArray[i].nodeName == json.datas[l].json.properties.nodeID) {
					//if (linkArray[i].nodeName == json.datas[l].json.properties.nodeID) {
					var math = parseInt(100000 * Math.random());
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
					GAT = parseInt(1000000000 * Math.random());
					creatgj(compName, cpux, cpuy, refid, compid, parttype, components, GAT);
				}
			}
		}

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
					GAT = parseInt(1000000000 * Math.random());
					creatbakgj(compName, cpux, cpuy, refid, compid, parttype, components, GAT);
				}
			}
		}
		//变量恢复初始值
		 rootnum = -0.5;
		 rootnum1 = 0.5;
		 rootnum2 = 0.5;
		 rootnum3 = 0.5;
		 rootnum4 = 0.5;
		 baknum = -0.5;
		 baknum1 = 0.5;
		 baknum2 = 0.5;
		 baknum3 = 0.5;
		 baknum4 = 0.5;
	}
	//遍历arrow绘画连线
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

	//console.log('dataJsondataJsondataJsondataJson', dataJson)
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
					var bakstartrefId = {};
					bakstartrefId.startId = dataJson[m]._refId;
					bakstartIds.push(bakstartrefId);
				}
			}
		}
		if (res == true) {
			for (var m in dataJson) {
				if (dataJson[m].json.properties != null) {
					if (dataJson[m].json.properties.parttype == 'part' && dataJson[m].json.properties.compid == startcomp) {
						var bakstartrefId = {};
						bakstartrefId.startId = dataJson[m]._refId;

						for (var i in arrows) {
							if (arrows[i].startItem == startcomp) {
								var endItem1 = arrows[i].endItem;
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
						for (var i = 1; i <= bakIds.length; i++) {
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
		if (res == true) {
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
						for (var i = 1; i <= bakIds.length; i++) {
							bakendIds.push(bakendId);
						}
					}
				}
			}
		}
	}
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
	button.textContent = '保存';
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
					comps.compId = data.datas[i].json.properties.compid;
					var rootParts = [];
					var rootPart = {};
					rootPart.partName = data.datas[i].json.properties.partname;
					//遍历芯片
					for (var j in data.datas) {
						if (data.datas[j].json.image == 'images/Chip.svg' && data.datas[j]._refId != null) {
							refid = data.datas[j]._refId;
							nodeID = data.datas[j].json.properties.nodeID;
						}
						if (data.datas[i].json.host._ref == refid) {
							rootPart.cpuid = nodeID;
						}
					}
					rootParts.push(rootPart);
					comps.rootPart = rootParts;
					compAllArray.push(comps);

				}
				if (data.datas[i].json.properties.group == 'B' && data.datas[i].json.properties.group != null) {
					var comps = {};
					var bakParts = [];
					var bakPart = {};
					comps.compId = data.datas[i].json.properties.compid;
					bakPart.partName = data.datas[i].json.properties.partname;

					//遍历芯片
					for (var j in data.datas) {
						if (data.datas[j].json.image == 'images/Chip.svg' && data.datas[j]._refId != null) {
							refid = data.datas[j]._refId;
							nodeID = data.datas[j].json.properties.nodeID;
						}
						if (data.datas[i].json.host._ref == refid) {
							bakPart.cpuid = nodeID;
						}
					}
					bakParts.push(bakPart);
					comps.bakPart = bakParts;
					bakcompAllArray.push(comps);
				}
				console.log('compAllArray', compAllArray);
				console.log('bakcompAllArray.comps', bakcompAllArray)
			}
		}
		var json = JSON.stringify(data);
		postMessageParentData.cmd = "submitJSON";
		postMessageParentData.params[0] = json;
		postMessageParentData.params[1] = compAllArray;
		postMessageParentData.params[2] = bakcompAllArray;
		window.parent.postMessage(postMessageParentData, "*")
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
	//芯片显示按钮
	var toolbarDiv = editor.toolbar;
	var button = document.createElement('button');
	button.textContent = '画布跳转';
	button.className = 'boarddesign_board_14s';
	toolbarDiv.appendChild(button)
	button.onclick = function () {
		showchip()
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
			console.log("这个图元的信息",data)
			if (data != null) {
				if (data.from == null) {
					//console.log("data", data);
					data.set('chipname', data.properties.chipName);
					data.set('corenum', data.properties.coreNum);
					data.set('memsize', data.properties.memSize);
					data.set('boardname', data.properties.boardName);
					data.set('infname', data.properties.infName);
					data.set('ifrate', data.properties.ifRate);
					data.set('opticalnum', data.properties.opticalNum);
					data.set('compname', data.name);
					data.set('rackname', data.properties.caseName);
					data.set('bdNum', data.properties.bdNum);
					data.set('IP', data.properties.IP);
					data.set('hrTypeName', data.properties.hrTypeName);
				} else {
					data.set('startcomp', data.from.name);
					data.set('endcomp', data.to.name);
				}
			}
		}
		if (image == "images/Chip.svg"  || type=='chip') {
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
	//交互效果
	function initInteraction() {
		var currentElement;
		var oldFillColor;
		var highlightColor = "#828282";

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
			//var xy = element.location;
			var type = element.get('type');

			function canDrop(data) {
				return data.get('type') == 'slot' && data.get('childType') == type;
			}
			var slot;
		}
		function adaptBounds(element, slot) {
			element.parent = element.host = slot;
			var bounds = slot.getBounds();
			/* graph.moveElements([element], bounds.x - element.x, bounds.y - element.y) */
		}
		var dragInfo = {};
		var startData;
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
				if (type && (type == 'card' || type == 'port' || type == 'item' || type == null || type == 'chip')) {
					dragInfo = {
						data: data,
						x: data.x,
						y: data.y
					};
					startData = dragInfo.data.parent;
					console.log("刚开始移动", startData);
					//graph.sendToTop(data);
					if (startData != null) {
						for (const i in startData.children.datas) {
							if (startData.children.datas[i].id != data.id) {
								if (startData.children.datas[i].properties.partname == data.properties.partname) {
									if (startData != data.parent) {
										verdict = false;
									}


								}
							}
						}
					}
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
				var oldSlot = data.parent;
				var slot = findSlot(data, evt);
				console.log("鼠标移动后的对象",data)
				//所点击当前构件的所属部件
				var partname = data.properties.partname;
				var host = findCellHost(evt, data);
				if (startData != null) {
					for (const i in startData.children.datas) {
						if (startData.children.datas[i].id != data.id) {
							if (startData.children.datas[i].properties.partname == data.properties.partname) {
								if( data.parent != null){

									if (startData.id != data.parent.id  ) {
										showMessage('该芯片上还有其他同部件构件请一起移动', 'error', 2000)
										graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
										data.parent = startData;
										data.host = startData;
									}
								}
							}
						}
					}
					//校验芯片上构件位置
					if(data.properties.Direction == 'from'){
						if (data.x > startData.x + 45 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.x < startData.x + 7.5 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.y > startData.y + 45 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.y < startData.y + 7.5 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
					}else if (data.properties.Direction == 'back'){
						if (data.x > startData.x + 80 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.x < startData.x  && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.y > startData.y + 80 && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
						if (data.y < startData.y  && verdict == false) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
							data.parent = startData;
							data.host = startData;
						}
					}

				}
				console.log("++++++++++++", data)
				console.log("++++++++++++", data.host)

				/* if(data.host != null){ */
				if (data.host == null && verdict == true) {
					showMessage('请将构件移动到芯片上', 'error', 5000);
					if(data.properties.Direction == 'from'){
					if (data.x > data.host.x + 45 || data.x < data.host.x + 7.5 ||
						data.y > data.host.y + 45 || data.y < data.host.y + 7.5) {
						for (var i = 0; i < movedate.length; i++) {
							movedate[i].parent = startData;
							movedate[i].host = startData;
							graph.moveElements([movedate[i]], dragInfo.x - movedate[i].x, dragInfo.y - movedate[i].y + i * 3)
						}
					}}else if(data.properties.Direction == 'back'){
						if (data.x > data.host.x + 80 || data.x < data.host.x  ||
							data.y > data.host.y + 80 || data.y < data.host.y ) {
							for (var i = 0; i < movedate.length; i++) {
								movedate[i].parent = startData;
								movedate[i].host = startData;
								graph.moveElements([movedate[i]], dragInfo.x - movedate[i].x, dragInfo.y - movedate[i].y + i * 3)
							}
						}
					}
				} else if (verdict == true) {
					//showMessage('已成功移动到IP为'+data.parent.properties.IP+'芯片上', 'success', 5000);
					if(data.properties.Direction == 'from'){
					if (data.x > data.host.x + 45 || data.x < data.host.x+ 7.5   ||
						data.y > data.host.y + 45 || data.y < data.host.y  + 7.5) {
						for (var i = 0; i < movedate.length; i++) {
							movedate[i].parent = startData;
							movedate[i].host = startData;
							graph.moveElements([movedate[i]], dragInfo.x - movedate[i].x, dragInfo.y - movedate[i].y + i * 3)
						}
					}}else if(data.properties.Direction == 'back'){
						if (data.x > data.host.x + 80 || data.x < data.host.x ||
							data.y > data.host.y + 80 || data.y < data.host.y) {
							for (var i = 0; i < movedate.length; i++) {
								movedate[i].parent = startData;
								movedate[i].host = startData;
								graph.moveElements([movedate[i]], dragInfo.x - movedate[i].x, dragInfo.y - movedate[i].y + i * 3)
							}
						}
					}
				}
				verdict = false;
				graph.selectionModel.clear();
			}
		})
	}
	function findCellHost(evt, element) {
		var xy = graph.toLogical(evt.event);
		var cell;
		function canDrop(data) {
			console.log("666666666666",data)
			return data.image == 'images/Chip.svg' || data.properties.type == "chip";
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
		movedate = evt.datas;
		//console.log('EVT',evt.datas)//这里可以获取当前移动的所有图元
		if (evt.kind != Q.InteractionEvent.ELEMENT_CREATED && evt.kind != Q.InteractionEvent.ELEMENT_MOVE_END) {
			return;
		}
		var data = evt.data;
		if (!data || data.get('type') !== 'item') {
			return;
		}
		var host = findCellHost(evt, data);
		//		console.log('host',host);
		data.host = data.parent = host;
		evt.datas.forEach(function (data) { data.host = data.parent = host; })
		if (host != null) {
			data.properties.cpuid = host.properties.nodeID;
		}

		/* 	for(var i in data){
			
				console.log('CPUid',	data.properties.cpuid);
				data.properties.cpuid  = host.properties.nodeID;
			} */
		//	console.log('22222EVT',evt.datas)

	})

/* 	graph.onclick = function (evt) {
		var data = graph.getElement(evt);
		if (data) {
			var children = data.toChildren();
		}
	} */
	//按这种方式导出部分json
	function exportNodeJSON(parent) {
		return graph.toJSON(false, {
			filter: function (data) {
				return data == parent || data.isDescendantOf(parent);
			}
		})
	}
	//右键选中其部件下所有构件功能
	graph.popupmenu.getMenuItems = function (graph, data, evt) {
		if (data) {
			return [
				{
					text: '选中该构件移动', action: function () {
						graph.select(data);
						verdict = true;
						if (data.properties.partname == null && data.image == "images/Chip.svg" || data.image == "rack" || data.image == "images/BeforeTheBoard.svg"
						|| data.image == "images/OpticalFiberMouth.svg" || data.image == "images/InternetAccess.svg" ||  data.image == "images/RoundMouth.svg"
						||  data.image == "images/SerialPort.svg"  || data.image =="images/AfterTheBoard.svg" || data.edgeType == "extend.top" ) {
							showMessage('请选择构件', 'error', 2000)
							graph.selectionModel.clear();
							verdict = false;
						}
					}

				},
				{
					text: '选中同一部件下构件移动', action: function () {
						var data = graph.getElement(evt);
						var partname = data.properties.partname;
						graph.forEach(function (data) {
							if (data.properties.partname == partname) {
								verdict = true;
								graph.select(data);
							}
						})
						if (data.properties.partname == null && data.image == "images/Chip.svg" || data.image == "rack" || data.image == "images/BeforeTheBoard.svg"
						|| data.image == "images/OpticalFiberMouth.svg" || data.image == "images/InternetAccess.svg" ||  data.image == "images/RoundMouth.svg"
						||  data.image == "images/SerialPort.svg"  || data.image =="images/AfterTheBoard.svg" || data.edgeType == "extend.top") {
							showMessage('请选择构件', 'error', 2000)
							graph.selectionModel.clear();
							verdict = false;
						}
					}

				},
				{
					text: '选中跳转的芯片', action: function () {
						var data = graph.getElement(evt);
						var deploymentsJson = graph.toJSON();
						if ( data.image == "images/Chip.svg" ){
							CHIPdate.push(data);
							showMessage('成功选中跳转芯片'+CHIPdate.length+'个', 'success', 2000)
						}else{
							showMessage('请重新选择跳转的芯片', 'error', 2000)

						}
					}
				},
				{
					text: '取消所有选中元素', action: function () {
						graph.selectionModel.clear();
						CHIPdate = [];
						showMessage('成功取消选择', 'error', 2000)
					}

				}
			];

		}
	}
	initInteraction();
}
//显示芯片方法
var falg = true;
var chip;
var compidArrays = [];
var gjdata = [];
var from;
var to;
var RefIdArrays = [];
var fromCompidArrays = [];
var toCompidArrays = [];
function showchip() {
		if (falg && CHIPdate.length != 0) {
			//获取当前画布数据
			deploymentsJsonbak = graph.toJSON();
			for(var i in CHIPdate){
				for(var j in deploymentsJsonbak.datas){
					if(CHIPdate[i].properties.uniqueId == deploymentsJsonbak.datas[j].json.properties.uniqueId){
						if(deploymentsJsonbak.datas[j]._refId == undefined){
							deploymentsJsonbak.datas[j]._refId = parseInt(1000000000 * Math.random());
						}
					}
				}
			}
			//获取所有连线关系的refid数据
			for (var k in deploymentsJsonbak.datas) {
				if (deploymentsJsonbak.datas[k]._className == "Q.Edge") {
					//	console.log("所有构件的连线关系", deploymentsJsonbak.datas[k])
					var RefIds = {};
					RefIds.fromRefId = deploymentsJsonbak.datas[k].json.from._ref;
					RefIds.toRefId = deploymentsJsonbak.datas[k].json.to._ref;
					RefIdArrays.push(RefIds);
				}
			}
			for (var i in RefIdArrays) {
				for (var k in deploymentsJsonbak.datas) {
					if (deploymentsJsonbak.datas[k]._className == "Q.Text") {
	
						if (RefIdArrays[i].fromRefId == deploymentsJsonbak.datas[k]._refId) {
							var fromcompidList = {};
							fromcompidList.fromGAT = deploymentsJsonbak.datas[k].json.properties.GAT;
							fromCompidArrays.push(fromcompidList);
						}
						if (RefIdArrays[i].toRefId == deploymentsJsonbak.datas[k]._refId) {
							var tocompidList = {};
							tocompidList.GAT = deploymentsJsonbak.datas[k].json.properties.GAT;
							toCompidArrays.push(tocompidList);
						}
					}
				}
			}
			graph.clear();
			//遍历组装数据绘画芯片
			for (var i = 0; i < CHIPdate.length; i++) {
				var properties = CHIPdate[i].properties;
				chip = createCard(properties, i * 85, 0);
				graph.moveToCenter();
			//遍历数据绘画构件
				for (var index = 0; index < CHIPdate[i].children.datas.length; index++) {
					//	console.log("----------",CHIPdate[i])
					var name = CHIPdate[i].children.datas[index].name;
					var properties = CHIPdate[i].children.datas[index].properties;
					var refid = CHIPdate[i].children.datas[index].id;
					if (CHIPdate[i].children.datas[index].properties.group == 'A') {
						//num++;
						var gj = createItem({ parent: chip, x: chip.x, y: chip.y, name: name, group: 'A', properties: properties, refid: refid })
						gjdata.push(gj);
					} else {
					//	numbak++;
						var gjbak = createBakItem({ parent: chip, x: chip.x, y: chip.y+20, name: name, group: 'B', properties: properties, refid: refid })
						gjdata.push(gjbak);
					}
				}
				num = 0.4;
				numbak = 0.4;
				num1 = 0.4;
				num2 = 0.4;
				num3 = 0.4;
				num4 = 0.4;
				numbak1 = 0.4;
				numbak2 = 0.4;
				numbak3 = 0.4;
				numbak4 = 0.4;
			}
			for (var i in fromCompidArrays) {
				if(toCompidArrays[i].GAT != null){
		
					fromCompidArrays[i].toGAT = toCompidArrays[i].GAT;
				}
			}
	
	
			for (var k in fromCompidArrays) {
				for (var h in gjdata) {
					if (fromCompidArrays[k].fromGAT == gjdata[h].properties.GAT
					) {
						fromCompidArrays[k].fromdata = gjdata[h]
					}
					if (fromCompidArrays[k].toGAT == gjdata[h].properties.GAT
					) {
						fromCompidArrays[k].todata = gjdata[h]
					}
				}
			}
			for (var i = 0; i < fromCompidArrays.length; i++) {
				if (fromCompidArrays[i].todata != undefined && fromCompidArrays[i].fromdata != undefined) {
					var from = fromCompidArrays[i].fromdata;
					var to = fromCompidArrays[i].todata;
					createEdge({ from: from, to: to })
					//如果连线想变成曲线,注释掉上面方法,调用下面的方法
					//createEdge({ from: from, to: to,edgeType: 'top' })
				}
			}
			CHIPdate = [];
			fromCompidArrays = [];
			falg = false;
		} else if(falg == false && CHIPdate.length == 0){
			compidArrays = [];
			var skipdata = graph.toJSON();
			//console.log("skipdata",skipdata);
			var skipArrays = [];
			for(var i in skipdata.datas){
				if(skipdata.datas[i]._className == "Q.Text"){
					var skipgjdata = {};
					skipgjdata.GAT = skipdata.datas[i].json.properties.GAT;
					skipgjdata._ref = skipdata.datas[i].json.host._ref;
					skipgjdata.group = skipdata.datas[i].json.properties.group;
					skipArrays.push(skipgjdata);
					console.log("skipdata",skipdata.datas[i])
				}
			}
	//赋值chip_id
			for(var i in skipArrays){
				for(var j in skipdata.datas){
					if(skipdata.datas[j]._className  == "Q.RectElement"){
						if(skipArrays[i]._ref == skipdata.datas[j]._refId){
							skipArrays[i].uniqueId = skipdata.datas[j].json.properties.uniqueId;
						}
					}
				}
			}
			//赋值location坐标
			for(var i in skipArrays){
				for(var j in deploymentsJsonbak.datas){
					if(deploymentsJsonbak.datas[j]._className  == "Q.RectElement"){
						if(skipArrays[i].uniqueId  == deploymentsJsonbak.datas[j].json.properties.uniqueId &&deploymentsJsonbak.datas[j]._refId !=  null ){
						//	console.log("赋值location坐标",deploymentsJsonbak.datas[j])
							skipArrays[i].location = deploymentsJsonbak.datas[j].json.location;
							skipArrays[i]._refId = deploymentsJsonbak.datas[j]._refId;
						}
					}



					
				}
				variable = 4;
			}
			//修改跳转前构件坐标数据
			var variable = 4;
			var variable1 = 4;
			var y = 8;
			var baky = 30
			for(var i in skipArrays){	
				for(var j in deploymentsJsonbak.datas){
					//判断是哪个构件
					if(deploymentsJsonbak.datas[j]._className == "Q.Text" && skipArrays[i].GAT  == deploymentsJsonbak.datas[j].json.properties.GAT){
							//判断构件是否备份
						if(skipArrays[i].group == "A"){
						variable+= 3
						var x = skipArrays[i].location.x
						console.log("variable = 4;",variable)
					/* 	console.log("skipArrays[i].location.x+variable",skipArrays[i].location.x + variable)
						console.log("skipArrays[i].location.json.x+45",skipArrays[i].location.json.x+45) */
					/* 	if(skipArrays[i].location.x + variable >= skipArrays[i].location.json.x+45){
							variable = 7;
							y=13;
							alert(666)
						} */
						if( variable >= 43){
							variable = 7;
							y=13;
						}
						deploymentsJsonbak.datas[j].json.location.json.x =  skipArrays[i].location.json.x+variable;
						deploymentsJsonbak.datas[j].json.location.json.y =  skipArrays[i].location.json.y+y;
						//	console.log("skipArrays",skipArrays)
					

							deploymentsJsonbak.datas[j].json.host._ref = skipArrays[i]._refId;
							deploymentsJsonbak.datas[j].json.parent._ref = skipArrays[i]._refId;
							}else{
								variable1+= 3
							/* 	if(skipArrays[i].location.x+variable1 >= skipArrays[i].location.json.x+45){
									variable1 = 7;
									baky= 35;
								} */
								if( variable1 >= 43){
									variable1 = 7;
									baky= 35;
								}
								deploymentsJsonbak.datas[j].json.location.json.x =  skipArrays[i].location.json.x+variable1;
								deploymentsJsonbak.datas[j].json.location.json.y =  skipArrays[i].location.json.y+baky;
								deploymentsJsonbak.datas[j].json.host._ref = skipArrays[i]._refId;
								deploymentsJsonbak.datas[j].json.parent._ref = skipArrays[i]._refId;					
						}
						
					}
				}
			}
		
		
			graph.clear();
			graph.parseJSON(deploymentsJsonbak)
			graph.moveToCenter();
			falg = true;
		}
}
function createEdge(options) {
	var edge = graph.createEdge(options.to, options.from);
    edge.setStyle(Q.Styles.EDGE_COLOR, '#999999');
	edge.setStyle(Q.Styles.ARROW_TO, true); 
	 edge.setStyle(Q.Styles.ARROW_TO_SIZE,1.8);
	edge.properties.type = 'edge';
	 var arrow = new Q.ImageUI(Q.Shapes.getShape(Q.Consts.SHAPE_ARROW_2, 5, 2.5));
    arrow.fillColor = '#99CCCC';
    arrow.position = Q.Position.CENTER_MIDDLE;
    arrow.anchorPosition = Q.Position.CENTER_MIDDLE;
    edge.addUI(arrow)
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

//绘画构件
function createItem(options) {
	var cpux = options.x;
	var cpuy = options.y;
	var j = num += 8;
	var gjjsonx = cpux + j;
	var gjjsony = cpuy + 6;
	//判断坐标到芯片边界	
	if (gjjsonx >= cpux + 78) {
		var k = num1 += 8;
		gjjsonx = cpux + k;
		gjjsony = cpuy + 11;
		if (gjjsonx >= cpux + 78) {
			var k = num2 += 8;
			gjjsonx = cpux + k;
			gjjsony = cpuy + 16;
			if (gjjsonx >= cpux + 78) {
				var k = num3 += 8;
				gjjsonx = cpux + k;
				gjjsony = cpuy + 21;
				if (gjjsonx >= cpux + 78) {
					var k = num4 += 8;
					gjjsonx = cpux + k;
					gjjsony = cpuy + 26;
					if (gjjsonx >= cpux + 78) {
						gjjsonx = cpux +8;
						gjjsony = cpuy + 31;
						
					}
					
				}
			}
		}
	}
	var item = graph.createText(options.name);
	item.set('type', 'item');
//	if (options.parent) {
		//	x = options.parent.x + options.parent.width * x;
		//	y = options.parent.y + options.parent.height * y;
		item.parent = item.host = options.parent;
	//}
	item.x = gjjsonx, item.y = gjjsony;
	item.zIndex = 200;
	item.properties = options.properties;
	item.properties.Direction = 'back';
	item.set('group', options.group);
	item.properties.RefId = options.refid;
	var color = options.group == 'A' ? '#FFFF99' : '#99CCFF';
	item.setStyle(Q.Styles.LABEL_BACKGROUND_COLOR, color);
	var scale = 0.27;
	item.setStyle(Q.Styles.LABEL_FONT_SIZE, 7 * scale);
	item.setStyle(Q.Styles.LABEL_PADDING, 2 * scale);
	item.setStyle(Q.Styles.LABEL_RADIUS, 2 * scale);
	return item;
}

//绘画备份构件
function createBakItem(options) {
	var cpux = options.x, cpuy = options.y;
	var j = numbak += 8;
	var gjjsonx = cpux + j;
	var gjjsony = cpuy + 20;
	//判断坐标到芯片边界	
	if (gjjsonx >= cpux + 78) {
		var k = numbak1 += 8;
		gjjsonx = cpux + k;
		gjjsony = cpuy + 25;
		if (gjjsonx >= cpux + 78) {
			var k = numbak2 += 8;
			gjjsonx = cpux + k;
			gjjsony = cpuy + 30;
			if (gjjsonx >= cpux + 78) {
				var k = numbak3 += 8;
				gjjsonx = cpux + k;
				gjjsony = cpuy + 35;
				if (gjjsonx >= cpux + 78) {
					var k = numbak4 += 8;
					gjjsonx = cpux + k;
					gjjsony = cpuy + 40;
					if (gjjsonx >= cpux + 78) {
						gjjsonx = cpux +8;
						gjjsony = cpuy + 45;
					}
				}
			}
		}
	}
	var item = graph.createText(options.name);
	item.set('type', 'item');
	
	//if (options.parent) {
		//	x = options.parent.x + options.parent.width * x;
		//	y = options.parent.y + options.parent.height * y;
		item.parent = item.host = options.parent;
	//}
	item.x = gjjsonx, item.y = gjjsony;
	item.zIndex = 200;
	item.properties = options.properties;
	item.properties.Direction = 'back';
	item.set('group', options.group);
	item.properties.RefId = options.refid;
	var color = options.group == 'A' ? '#FFFF99' : '#99CCFF';
	item.setStyle(Q.Styles.LABEL_BACKGROUND_COLOR, color);
	var scale = 0.27;
	item.setStyle(Q.Styles.LABEL_FONT_SIZE, 7 * scale);
	item.setStyle(Q.Styles.LABEL_PADDING, 2 * scale);
	item.setStyle(Q.Styles.LABEL_RADIUS, 2 * scale);
	return item;
}
Q.registerImage('group_cloud', {
	cacheable: true,
	width: 100, height: 100,
	draw: function(g){
		g.fillStyle = '#FFD';
		g.fillRect(0, 0, 100, 100);
		g.strokeStyle = '#CCC';
		g.strokeRect(0, 0, 100, 100);
	}
});
//跳转画布绘画芯片
function createCard(properties, x, y) {
	//var bounds = slot.getBounds();
	var port = createNode({
		radius: 5, 
		fillColor: '#eeeeee',
		x: x,
		y: y,
		width: 80,
		height: 80
	});
	port.name = 'IP: ' + properties.IP;
	/* 	port.radius =  "5000";
		port.setStyle(Q.Styles.SHAPE_FILL_COLOR,  '#EEE'); */
	//port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
	//	port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
	port.properties = properties;
	port.set('type', 'chip');
	//port.name = 'DSP芯片';
	//	port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
	//	port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
	port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_TOP);
	port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_BOTTOM);
	port.setStyle(Q.Styles.LABEL_FONT_SIZE, 3);
	return port;
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
function showMessage(message, type, time) {
	let str = ''
	switch (type) {
		case 'success':
			str = '<div class="success-message" style="width: 300px;height: 40px;text-align: center;background-color:#daf5eb;;color: rgba(59,128,58,0.7);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;z-index: 9999">\n' +
				'    <span class="mes-text">' + message + '</span></div>'
			break;
		case 'error':
			str = '<div class="error-message" style="width: 300px;height: 40px;text-align: center;background-color: #f5f0e5;color: rgba(238,99,99,0.8);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;;z-index: 9999">\n' +
				'    <span class="mes-text">' + message + '</span></div>'
	}
	$('body').append(str)
	setTimeout(function () {
		$('.' + type + '-message').remove()
	}, time)
}
