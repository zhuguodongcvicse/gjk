window.addEventListener("message", this.handleMessageFromParent)
 // 子接收方式二参数
var iframeData; // 子接收方式一参数
//页面加载数据
var dat;
//向父级页面传构件id
var gjid;
//接收属性栏修改后的数据
var proData;
//增加锚点参数
var addPointParam = new Array();
//加载标志
var loadState = false;
//存储加载后的数据
var loadAddPointParam = new Array();
//画布数据(构件基本属性)
var canvasData = new Map()
//存放锚点数据
var endpointMap = new Map()
//复制所保存数据
var copyData = new Array()
//被框选的节点id
var idList = new Array()
//点选选中的构件
var selectNodeId = ""
//初始连线关系数据
var connectionData = []
//是否与vue页面进行数据交互
//var isInteraction = false
//鼠标在当前画布的位置X
var div_rightX = ""
//鼠标在当前画布的位置Y
var div_rightY = ""
//var loadingState = true;
//存储上一次点击的构件id
var lastTimeId = ""
//双击锚点状态
var dblclickPoint = false

var midpoints = [0.7, 0.5, 0.3, 0.1]
var stub = [40, 30, 20, 10]

//ctrlZ时需要保存的数据
//栈,记录用户操作的先后顺序,用来进行撤销操作,数据结构为JSON,其中的copy用来复制部件
//是个二维栈,包括新增/删除/粘贴操作
var chartOperationStack = new Array;
chartOperationStack['add'] = [] //新增
chartOperationStack['delete'] = [] //删除
chartOperationStack['addConnection'] = [] //新增连线
chartOperationStack['deleteConnection'] = [] //删除连线
chartOperationStack['dragZ'] = [] //拖动
chartOperationStack['addPaste'] = [] //粘贴新增
chartOperationStack['deletePaste'] = [] //粘贴删除
//保存用户每一步操作 有,add,delete,paste，连线,移动等
var chartRareOperationStack = new Array;
//ctrlY时需要保存的数据
var ctrlYStack = new Array();
ctrlYStack['add'] = []
ctrlYStack['delete'] = []
ctrlYStack['addConnection'] = []
ctrlYStack['deleteConnection'] = []
ctrlYStack['dragY'] = []
ctrlYStack['addPaste'] = []
ctrlYStack['deletePaste'] = []
//保存ctrlZ时所对应的操作
var ctrlYOperationStack = new Array;

//判断构件是否被删除
var nodeList = []
//判断连线是否为已删除过的连线
var delConnectionMap = new Map()


//判断用户是否手动进行连线
var isConnection = true
//ctrlZ时操作是否为从删除连线开始
var isRemoveConn = false
////ctrlY时操作是否为从删除连线开始
var isRemoveConnY = false
//ctrlZ时保存新生成的连线数组
var newConnection = new Array()
//ctrlY时保存新生成的连线数字
var newConnectionY = new Array()
//记录节点拖动的起始位置
var nodePosition = {}
//撤销粘贴操作时所使用的对象
var addPasteData = {}
//构件更新状态
var compUpdateState = {}
//删除构件列表
gjkCompId = ""
//构件所属部件map
var componentMap = new Map()
// 子向父传参
function handleMessageToParent(cmd, gjIdAndTemId) {
	window.parent.postMessage({
		cmd: cmd,//'returnFormJson',
		params: gjIdAndTemId
	}, "*")
};
//子向父传递保存参数
function handleMessage(connects, type) {
	window.parent.postMessage({
		cmd: type,
		params: connects
	}, "*");
}
//子向父传参数
function handleConnection(connData) {
	window.parent.postMessage({
		cmd: 'returnConnection',
		params: connData
	}, "*");
}
// 子接收父参数
function handleMessageFromParent(event) {
	var data = event.data;
	//console.log("子接收父参数", data)
	switch (data.cmd) {
		case 'getCompDtos':
			// 处理业务逻辑
			iframeData = data
			dat = eval(JSON.stringify(iframeData.params));
			//console.log("子接收父参数", JSON.stringify(dat))
			//console.log("构件状态", compUpdateState);
			connectionData = iframeData.connectionData
			//console.log("connection数据", connectionData)
			break
		case 'getCompDtosData':
			//接收属性栏修改后的数据
			proData = data
			//console.log(JSON.stringify(proData.params));
			updatePoint(proData.params);
			break
		case 'clickCompSave':
			var saveArrow = save();
			var saveflowChartJson = saveFlowchart();
			var saveData = {
				"saveArrow": saveArrow,
				"saveflowChartJson": saveflowChartJson
			};
			//console.log("保存后数据", saveData);
			handleMessage(saveData, 'returnSave');
			break
		case 'clickCompLoading':
			//loadingState = false;
			//console.log("数据", JSON.stringify(data.params));
			loadState = true;
			loadJson(data.params);
			loadAddPointParam = data.params.addPointParam
			//alert("1111111"+loadState)
			break
		case 'sendCompFzData':
			componentMap.set(data.params.compId, data.params.compData)
			break
		case 'alignment':
			var id = ""
			if (idList.length > 0) {
				id = idList[0]
			}
			var top = $("#" + id).offset().top;
			var left = $("#" + id).offset().left;
			switch (data.params) {
				case "1":
					topAlignment(top, id)
					break;
				case "2":
					leftAlignment(left, id)
					break;
				case "3":
					rightAlignment(left, id)
					break;
				case "4":
					bottomAlignment(top, id);
					break;
				case "5":
					verticalCenter(left, id);

					break;
				case "6":
					levelCenter(top, id);
					break;
			}
			break;
		case 'cleanCanvas':
			cleanCanvas();
			break;
		case 'bottonCheckComp':
			if (!data.params) {
				$("#accordion").show()
				$("#gjk").hide()
				$('#update').html("")
				$('#notUpdate').html("")
				compUpdateState = data.compUpdateState;
				appendUpdateDiv()
			} else {
				$("#accordion").hide()
				$("#gjk").show()
			}
			break;
		case 'completeCheck':
			if(!data.state){
				completeCheckFun(data.params)
			}else{
				removeStyle()
			}
			//警告
			//let styleColor = "#F4A460"
			//错误
			//let styleColor = "#ff0000"
			//$('.pa').addClass("warn")
			//endpointCheck(styleColor);
			//connectionCheck(styleColor);
			break;
		case 'startSimulation':
			getSimulationData();
			break;
		case 'endSimulation':
			deleteFzPicture();
			break;
		case 'returnRemoveComp':
			$("#" + data.params).remove()
			break;
	}
};

//(function () {
//折叠画板初始化
// $( "#accordion" ).accordion({
// 	collapsible: true
// });
var area = 'drop-bg'
var areaId = '#' + area
jsPlumb.ready(load);
//构件模板id
var gjTemplateId;

//生成一个随机数
function createNum() {
	let random = ''
	random = Math.ceil(Math.random() * 10000000000000000000).toString().substr(0, 9 || 4)
	random = Date.now() + random
	return random
}


// 放入拖动节点
function dropNode(template, position) {
	//console.log(position);
	position.left -= $('#side-buttons').outerWidth()
	position.left = position.left + $(areaId).scrollLeft()
	position.top = position.top + $(areaId).scrollTop()
	console.log("position+++++++++++++++", position)
	//position.id = uuid.v1()
	position.id = createNum()
	console.log("生成的id", position.id)
	//position.generateId = uuid.v1
	gjTemplateId = position.id;
	var html = renderHtml(template, position)
	//console.log("保存的html", html)
	$(areaId).append(html)
	initSetNode(template, position.id)
}
//获取拖动的构件
function getGj(template) {
	var str = template.charAt(template.length - 1);
	return str;
}
// 初始化节点设置
function initSetNode(template, id) {
	addDraggable(id)

	var gj = getGj(template);
	setEnterPoint(id, gj)
	setExitPoint(id, gj)
}

//获取鼠标坐标
function mousePosition(ev) {
	// ev = ev || window.event; 
	// if(ev.pageX || ev.pageY){ 
	//     return {x:ev.pageX, y:ev.pageY}; 
	// } 
	// return { 
	//     x:ev.clientX + document.body.scrollLeft - document.body.clientLeft, 
	//     y:ev.clientY + document.body.scrollTop - document.body.clientTop 
	// }; 
	var e = ev || window.event;
	var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
	var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
	var x = e.pageX || e.clientX + scrollX
	var y = e.pageY || e.clientY + scrollY
	return { "x": x, "y": y }
}


// 设置入口点
function setEnterPoint(id, gj) {
	var len = dat[gj].inputList.length;
	var anchorNumber = 1 / len;
	var differenceValue = anchorNumber / 2;
	var x = differenceValue;
	var inPoint;
	var midpoints = [0.2, 0.4, 0.6, 0.8]
	var stub = [20, 30, 40, 50]
	for (var i = 0; i < len; i++) {
		var config = JSON.parse(JSON.stringify(getBaseNodeConfig()))
		config.isSource = false
		config.maxConnections = 1
		config.paintStyle = {
			strokeStyle: "#4ECB74",
			fillStyle: "transparent",
			radius: 5,
			lineWidth: 2
		}
		config.connector[1].midpoint = midpoints[i]
		config.connector[1].stub = stub[i]
		inPoint = jsPlumb.addEndpoint(id, {
			anchors: [0, differenceValue, -1, 0],
			uuid: i + '*input*' + id,
			deleteEndpointsOnEmpty: true
		}, config);
		inPoint.bind('dblclick', function (endpoint, originalEvent) {
			dblclickPoint = true
			addDiv(endpoint);
			var endpointId = endpoint.getUuid().split("*")[2]
			var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
			var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
			// var mouse = mousePosition();
			$('.point').css("position", "absolute");
			$('.point').css("left", x - 290);
			$('.point').css("top", y);
		});
		inPoint.bind('mouseout', function (endpoint, originalEvent) {
			dblclickPoint = false;
		});
		inPoint.bind('mousedown', function (endpoint, originalEvent) {
			mousedownState = 1;
		});
		differenceValue += anchorNumber;
		endpointMap.set(i + '*input*' + id, dat[gj].inputList[i])
	}
}
//鼠标悬浮锚点显示div
function addDiv(endpoint) {
	//console.log("端点信息", endpoint)
	var templateid = endpoint.elementId;
	var templateUuid = endpoint.getUuid();
	// //console.log(templateUuid);
	// //console.log(templateUuid);
	// var str = templateUuid.split('*');
	// var tempUuid = str[0];
	// var strTemp = str[1];
	// var temp = strTemp.substr(0, 5);
	// // console.log(tempUuid);
	// // console.log(temp);
	// var b = $('#' + templateid + '-heading')[0].dataset.id;
	//console.log("下标", b)

	var str = '';
	str = hoverDiv(endpointMap.get(templateUuid))
	// if (temp != 'input') {
	// 	str = hoverDiv(dat[b].outputList[tempUuid]);
	// } else {
	// 	//console.log("端点显示数据", dat[b].inputList);
	// 	str = hoverDiv(dat[b].inputList[tempUuid])
	// }
	$('#drop-bg').append(str);
	//$('body').append(str);
	$(".point").bind('click', function (event) {
		//	console.log("+++++++++++++++",event)
		event.stopPropagation();
		$('.point').remove();
	})
	$(".point").bind('mousedown', function (event) {
		mousedownState = 1;
		return false
	})

}

function hoverDiv(inPointData) {
	var str = '';
	if (inPointData.variableStructType != '') {
		str += "<div class = 'point'  style='background:#F0E6BD'>" +
			"<p>参数名称：" + inPointData.variableName + "</p>" +
			"<p>参数类型：" + inPointData.dataTypeName + "</p>" +
			"<p>参数长度：" + inPointData.lengthName + "</p>" +
			"<p>结构体：" + inPointData.variableStructType + "</p>" +
			"</div>";
	} else {
		str += "<div class = 'point'  style='background:#F0E6BD'>" +
			"<p>参数名称：" + inPointData.variableName + "</p>" +
			"<p>参数类型：" + inPointData.dataTypeName + "</p>" +
			"<p>参数长度：" + inPointData.lengthName + "</p>" +
			"</div>";
	}
	return str

}
// 设置出口点
function setExitPoint(id, gj) {
	var len = dat[gj].outputList.length;
	var anchorNumber = 1 / len;
	var differenceValue = anchorNumber / 2;
	var x = differenceValue;
	var outPoint
	// var midpoints = [ 0.7, 0.5, 0.3,0.1]
	// var stub = [40, 30, 20, 10]
	for (var i = 0; i < len; i++) {
		var config = JSON.parse(JSON.stringify(getBaseNodeConfig()))
		config.isTarget = false
		config.maxConnections = -1
		config.connector[1].midpoint = midpoints[i]
		config.connector[1].stub = stub[i]
		outPoint = jsPlumb.addEndpoint(id, {
			anchors: [1, differenceValue, 1, 0],
			uuid: i + '*output*' + id,
			deleteEndpointsOnEmpty: true
		}, config);
		//端点绑定鼠标移入事件
		outPoint.bind('dblclick', function (endpoint, originalEvent) {
			dblclickPoint = true
			addDiv(endpoint);
			var endpointId = endpoint.getUuid().split("*")[2]
			var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
			var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
			$('.point').css("position", "absolute");
			$('.point').css("left", x - 15);
			$('.point').css("top", y);
		});
		//端点绑定鼠标移出事件
		outPoint.bind('mouseout', function (endpoint, originalEvent) {
			dblclickPoint = false
		});
		outPoint.bind('mousedown', function (endpoint, originalEvent) {
			mousedownState = 1;
		});
		differenceValue += anchorNumber;
		endpointMap.set(i + '*output*' + id, dat[gj].outputList[i])
	}

}



// 渲染html
function renderHtml(type, position) {
	return Mustache.render($('#' + type).html(), position)
}

////当前拖动的节点是否为框选的节点
function isNode(id) {
	var isNode = false;
	for (var a = 0; a < idList.length; a++) {
		if (id == idList[a]) {
			isNode = true
		}
	}
	return isNode;
}
//返回当前节点所在的位置
function saveNodePosition(id) {
	nodePosition = {}
	var div = document.getElementById(id)
	nodePosition.top = div.offsetTop
	nodePosition.left = div.offsetLeft
	nodePosition.id = id
	return nodePosition
}

// 让元素可拖动
var topList = {};
var leftList = {};
//拖动前的位置数组
var oldPosition = new Array()
//拖动后的位置数组
var newPosition = new Array()
//存储拖动时的id及相应的top、left
var dragNode = {}
function addDraggable(id) {
	jsPlumb.draggable(id, {
		containment: 'parent',
		//正在移动
		drag: function () {
			if (isNode(id)) {
				var t0 = $(this).context.offsetTop;
				var h0 = $(this).context.offsetLeft;
				var arr = $(".ui-selected").toArray();
				for (var i = 0; i < arr.length; i++) {
					//console.log(222)
					$a = $(arr[i]);
					$("#" + $a.context.id).draggable({ containment: "parent" });
					if ($a.context.id == $(this).context.id) {
						continue;
					}
					var t = topList[i];
					var h = leftList[i];
					if (t0 + t < 0 || h0 + h < 150) {
						return
					}
					$a.offset({ "top": t0 + t - $(areaId).scrollTop(), "left": h0 + h - $(areaId).scrollLeft() });
					jsPlumb.setSuspendDrawing(false, true);

				}
			}

		},
		//开始移动
		start: function () {
			dragNode = {}
			oldPosition = []
			//console.log("拖动时的idList数组", idList)
			if (isNode(id)) {
				for (var a = 0; a < idList.length; a++) {
					oldPosition.push(saveNodePosition(idList[a]))
				}
				dragNode.oldPosition = oldPosition
				var t0 = $(this).context.offsetTop;
				var h0 = $(this).context.offsetLeft;
				//console.log("开始移动",$(this))
				var arr = $(".ui-selected").toArray();
				for (var i = 0; i < arr.length; i++) {
					$a = $(arr[i]);
					if ($a.context.id == $(this).context.id) {
						continue;
					}
					var t1 = $a.context.offsetTop;
					var h1 = $a.context.offsetLeft;
					topList[i] = t1 - t0;
					leftList[i] = h1 - h0 + 145; //220
				}
			} else {
				oldPosition.push(saveNodePosition(id))
				dragNode.oldPosition = oldPosition
			}
		},
		// //停止移动
		stop: function () {
			newPosition = []
			if (isNode(id)) {
				for (var a = 0; a < idList.length; a++) {
					newPosition.push(saveNodePosition(idList[a]))
				}
				dragNode.newPosition = newPosition
			} else {
				newPosition.push(saveNodePosition(id))
				dragNode.newPosition = newPosition
			}
			chartOperationStack['dragZ'].push(dragNode)
			chartRareOperationStack.push('dragZ')
			limitZ();

		},

	})
	// $( "#"+id ).draggable({ containment: "parent",scroll: true });
	// jsPlumb.setSuspendDrawing(false,true);
}

var int;
//启动定时器
function load() {
	int = self.setInterval(checkDat, 1000);
	//a = 555;

}
function checkDat() {
	//console.log("进入定时器");
	let flag = 0;
	let iX;
	let BR;
	let dw;
	$(".border").mousedown(function (e) {
		mousedownState = 1;
		iX = e.clientX - this.offsetLeft;
		BR = e.clientX - $(".div_left").outerWidth();
		//左div的宽度，也就是right到left的值
		dw = $(".row").outerWidth(); //外div的宽度
		flag = 1;
		return false;
	})
	$(".row").mouseup((e) => {
		$('#selectionRect').hide()
		flag = 0;
	})
	$('.row').mousemove(function (e) {
		if (flag) {
			let f = $(".div_left").width();
			// console.log("1111",e.clientX - iX)
			// console.log("222",dw - (e.clientX - BR))
			// console.log("3333",(e.clientX - BR))
			if (e.clientX - iX > 130) {
				$(".border").css("left", e.clientX - iX + "px")
				$(".div_left").css("right", dw - (e.clientX - BR) + "px")
				$(".div_right").css("left", (e.clientX - BR) + "px")
			} else {
				$(".border").css("left", 12 + "%")
				$(".div_left").css("right", 87 + "%")
				$(".div_right").css("left", 13 + "%")
			}
			$(".div_right").css("width", 85.5 + "%")
			//console.log($(".div_right").width()+(e.clientX - BR)-100)
			//根据鼠标事件相对位置计算出div的position

			return false;
		}
	})
	if (dat.length > 0) {
		main();
		//$("#gjk").load();	
	}
}
//获取构件列表所有构件id
// function getCompIdList(){

// 	let divArray = $("#gjk").find(".btn")
// 	for(let i=0;i<divArray.length;i++){
// 		console.log("构件列表构件",$(divArray[i]).attr("id"))
// 	}
// }

function appendUpdateDiv() {
	var strUpdate = ""
	var notUpdate = ""
	for (var i = 0; i < dat.length; i++) {
		for (let key in compUpdateState) {
			if (compUpdateState[key] == "0") { //已更新
				if (dat[i].id == key) {
					strUpdate += "<div class = 'btn' style = 'margin:5px' data-template='tpl-menu" + i + "'>" + dat[i].compImg + "</div></br>"
				}
			} else {
				if (dat[i].id == key) {
					notUpdate += "<div class = 'btn' style = 'margin:5px' data-template='tpl-menu" + i + "'>" + dat[i].compImg + "</div></br>"
				}
			}
		}
	}
	$('#update').append(strUpdate)
	$('#notUpdate').append(notUpdate)
}

//循环添加构件并生成相应模板
function appendDiv() {
	var str = "";
	var Template = "";
	var strUpdate = ""
	var notUpdate = ""
	for (var i = 0; i < dat.length; i++) {

		//console.log("dat[i].compImg", dat[i].compImg);
		// var s = dat[i].compImg.substr(dat[i].compImg.indexOf("src='") + 5);
		// var img = s.substr(0, s.indexOf("' "));
		//str += "<div class = 'btn'  data-template='tpl-menu" + i + "'><img src='"+img+"'><br/>" + dat[i].compName + "</div></br>"
		str += "<div class = 'btn' id = '" + dat[i].id + "' style = 'margin:5px' data-template='tpl-menu" + i + "'>" + dat[i].compImg + "</div></br>"
		//str += "<a class='btn btn-success btn-controler'  data-template='tpl-menu" + i + "' role='button'>" + dat[i].compName + "</a></br><br>";
		Template += "<script id='tpl-menu" + i + "' type='text/html'>" +
			"<div class='pa' id='{{id}}' style='top:{{top}}px;left:{{left}}px'>" +
			//"<span class='delete-node pull-right' data-type='deleteNode' data-id='{{id}}' id='del" + i + "'>X</span>" +
			"<div class='responsive'>" +
			"<div id='{{id}}-heading' data-id='" + i + "'>" +
			//"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>构件001</div></div>" +
			//"<div style='text-align:center;height:82px;width:160px;border:6px dashed #2727E0;border-radius:5px;background-color: #16F2DC;display: block;'><img src='./gjk/image/u6.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>构件002</div></div>"
			dat[i].compImg +
			//"<div style='text-align:center;height:80px;width:160px;border:1px none ;border-radius:6px;background-color: #BB2D2D;display: block;'><img src='./img/u" + i + ".png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>显示名</div></div>"+
			//"<img src='./img/u" + i + ".png' width='100' height='50'>" +
			//"<div class='desc' id='" + i + "'>" + dat[i].compName + "</div>" +
			"</div>" +
			"</div>" +
			"</div>" +
			"</div></script>";
		for (let key in compUpdateState) {
			if (compUpdateState[key] == "0") { //已更新
				if (dat[i].id == key) {
					strUpdate += "<div class = 'btn' style = 'margin:5px' data-template='tpl-menu" + i + "'>" + dat[i].compImg + "</div></br>"
				}
			} else {
				if (dat[i].id == key) {
					notUpdate += "<div class = 'btn' style = 'margin:5px' data-template='tpl-menu" + i + "'>" + dat[i].compImg + "</div></br>"
				}
			}
		}
	}
	$('#gjk').append(str);
	// $('#update').append(strUpdate)
	// $('#notUpdate').append(notUpdate)
	$('body').append(Template);
}

var mousedownState = 0;
var canvasState = false

//入口方法
function main() {
	let dropDiv = document.getElementById("drop-bg")
	dropDiv.scrollTop = 1800
	dropDiv.scrollLeft = 2300
	window.clearInterval(int);
	jsPlumb.setContainer("drop-bg");
	appendDiv();
	$('.btn').draggable({
		helper: 'clone',
		scope: 'ss'
	})

	$(areaId).droppable({
		scope: 'ss',
		drop: function (event, ui) {
			//console.log("+++++++++", $(areaId).scrollTop())
			dropNode(ui.draggable[0].dataset.template, ui.position)
			//$('#propertybar').show();
			var gj = getGj(ui.draggable[0].dataset.template);
			gjid = dat[gj].id;
			var a = gjTemplateId;
			//添加画布数据
			canvasData.set(a, JSON.parse(JSON.stringify(dat[$('#' + a + '-heading')[0].dataset.id])))
			console.log("画布构件基本数据", canvasData)
			console.log("锚点数据", endpointMap)
			//放置画布后将新增的构件保存到栈中
			chartOperationStack['add'].push(saveNodeJson(a));
			//添加新增操作
			chartRareOperationStack.push('add');
			limitZ()
			//清空ctrlY时的数据
			//clean();
			//console.log("用户所做操作", chartRareOperationStack)
			//console.log("ui数据", $('#' + a + '-heading')[0].dataset.id)
			var endpoints = jsPlumb.getEndpoints(gjTemplateId);
			var uuidList = new Array();
			$.each(endpoints, function (n, val) {
				uuidList.push(val.getUuid());
			});
			//console.log("uuid集合",uuidList);
			var gjidAndTemid = [];
			gjidAndTemid.push({
				//构件ID
				gjId: gjid,
				//构件模板ID
				tmpId: gjTemplateId,
				//状态
				state: 0,
				//节点所有锚点uuid
				uuidList: uuidList
			});
			//console.log("参数：",JSON.stringify(gjidAndTemid));
			//gjIdAndTemId = gjidAndTemid;
			handleMessageToParent("returnFormJson", gjidAndTemid);
			//添加画布构件点击事件
			//$('#' + a).off("click").on("click", function () { });
			$('#' + a).bind('click', function (event) {
				//alert("节点点击事件");
				event.stopPropagation();
				if (lastTimeId != "") {
					$('#' + lastTimeId).removeClass("nodeStyle")
					$.each(jsPlumb.getEndpoints(lastTimeId), function (n, v) {
						v.removeClass("nodeStyle")
					});
				}
				$('#' + a).addClass("nodeStyle")
				var points = jsPlumb.getEndpoints(a);
				//console.log("锚点信息",points)
				$.each(points, function (n, val) {
					val.addClass("nodeStyle")
				});
				lastTimeId = a
				// jsPlumb.setSuspendDrawing(false,true)
				connectionObjClick = {};
				selectNodeId = a;
				if (idList.length <= 0) {
					idList = []
					idList.push(a)
				} else {
					if (idList.length == 1) {
						if (idList[0] != a) {
							idList = []
							idList.push(a)
						}
					}
				}
				//$('#' + a).css('border', '1px solid black');
				var b = $('#' + a + '-heading')[0].dataset.id;
				gjid = dat[b].id;
				gjidAndTemid = [];
				gjidAndTemid.push({
					//构件ID
					gjId: gjid,
					//构件模板id(nodeID)
					tmpId: a,
					//状态
					state: 2
				});
				//gjIdAndTemId = gjidAndTemid;
				handleMessageToParent("returnFormJson", gjidAndTemid);
				$('#' + a).attr('tabindex', 0);
				$('#' + a).focus();
				//	document.getElementById(a).onkeydown=function (e){
				// if(e && e.keyCode==46){//delet删除
				// 	//删除元素将删除数据保存到栈中
				// 	chartOperationStack['delete'].push(saveNodeJson(a));
				// 	//添加删除操作
				// 	chartRareOperationStack.push('delete');
				// 	//清空ctrlY时的数据
				// 	clean();
				// 	jsPlumb.remove(a)
				// 	var gjidAndTemid = [];
				// 	gjidAndTemid.push({
				// 		gjId: gjid,
				// 		tmpId: a,
				// 		state: 1
				// 	});
				// 	handleMessageToParent("returnFormJson",gjidAndTemid);
				// }
				//else 
				// if(e && e.keyCode==67){//复制
				// 	var gjidAndTemid = []
				// 	gjidAndTemid.push({
				// 		gjId: gjid,
				// 		tmpId: [a],
				// 		state: 3
				// 	})
				// 	handleMessageToParent("returnFormJson",gjidAndTemid);
				// 	var copyJson = saveNodeJson(a)
				// 	copyData.push(copyJson);
				// }else
				//  if(e && e.keyCode==88){ //剪切
				// 	var copyJson = saveNodeJson(a)
				// 	copyData.push(copyJson);
				// 	jsPlumb.remove(a)
				// 	var gjidAndTemid = [];
				// 	gjidAndTemid.push({
				// 		gjId: gjid,
				// 		tmpId: a,
				// 		state: 1
				// 	});
				// 	handleMessageToParent("returnFormJson",gjidAndTemid);
				// }
				//	}

				// document.onkeyup=function(event){
				// 	console.log(11111111111);
				// 	var e = event || window.event || arguments.callee.caller.arguments[0];
				// 	if(e && e.keyCode==46){

				// 		jsPlumb.remove(a)
				// 		var gjidAndTemid = [];
				// 		gjidAndTemid.push({
				// 			gjId: gjid,
				// 			tmpId: a,
				// 			state: 1
				// 		});
				// 		handleMessageToParent("returnFormJson",gjidAndTemid);
				// 	}
				// }
			});

			$('#' + a).bind('mousedown', function (event) {
				mousedownState = 1;
				return false
			})

			//删除节点
			$(".delete-node").off("click").on("click", function () { });
			$(".delete-node").bind('click', function (event) {
				event.stopPropagation();
				eventHandler(event.target.dataset);
			});

		}
	})
	//避免同一构件输出点连接输入点
	jsPlumb.bind('beforeDrop', function (info) {
		if (info.sourceId === info.targetId) {
			return false
		} else {
			return true
		}
	})

	//删除构件列表构件
	//$('.btn').off("click").on("click", function () { });
	$('.btn').bind('click', function (event) {
		//console.log(event.currentTarget.id)
		gjkCompId = event.currentTarget.id
		$('#' + event.currentTarget.id).attr('tabindex', 0);
		document.getElementById(event.currentTarget.id).style.outline = "0"
		$('#' + event.currentTarget.id).focus();
		document.getElementById(event.currentTarget.id).onkeydown = function (e) {
			if (e.keyCode == 46) {
				//$("#" + event.currentTarget.id).remove()
				//console.log("参数：",JSON.stringify(gjidAndTemid));
				//gjIdAndTemId = gjidAndTemid;
				handleMessageToParent("removeComp", event.currentTarget.id);
			}
		}

	})
}

//折叠面板点击事件
// $( "#accordion" ).on( "accordionbeforeactivate", function( event, ui ) {
// 	console.log("折叠面板标题",ui.newHeader.text())
// } );

$('.panel').on('show.bs.collapse', function (event, ui) {
	var panel = event.currentTarget.children[0].innerText;
	//console.log(panel)
})

//点选Ctrl+C保存单个节点json
//存储复制数据
function saveNodeJson(nodeId) {
	copyData = []
	//节点信息
	var nodes = []
	//锚点信息
	var jsonendpoints = [];
	//console.log(mouse)
	//var $elem = document.getElementById(nodeId);
	//i用于定位使用什么数据
	var i = $("#" + nodeId).find("div").find("div")[0].dataset.id;
	var endpoints = jsPlumb.getEndpoints(nodeId);
	//console.log(endpoints);
	//循环构件上的锚点
	$.map(endpoints, function (endpoint) {
		jsonendpoints.push({
			anchorX: endpoint.anchor.x,
			anchorY: endpoint.anchor.y,
			uuid: endpoint.getUuid(),
			id: nodeId
		});
	});
	//console.log("当前画布的数据+++++++++++++++++",canvasData)
	nodes.push({
		blockId: nodeId,
		//nodetype: $elem.attr('data-nodetype'),
		positionX: parseInt($("#" + nodeId).css("left"), 10),
		positionY: parseInt($("#" + nodeId).css("top"), 10),
		imageId: i,
		//text: $('#' + $elem.attr('id') + '-heading').find("div").find("div").html(),
		outPointsData: canvasData.get(nodeId).outputList,
		inPointsData: canvasData.get(nodeId).inputList,
		compImg: canvasData.get(nodeId).compImg,
		compId: canvasData.get(nodeId).id,
		nodeData: canvasData.get(nodeId)

	});
	var connections = [];
	$.each(jsPlumb.getConnections(), function (idx, connection) {
		//console.log(connection);
		connections.push({
			connectionId: connection.id,
			pageSourceId: connection.sourceId,
			pageTargetId: connection.targetId,
			anchors: $.map(connection.endpoints, function (endpoint) {
				return [[endpoint.anchor.x,
				endpoint.anchor.y,
				endpoint.anchor.orientation[0],
				endpoint.anchor.orientation[1],
				endpoint.anchor.offsets[0],
				endpoint.anchor.offsets[1]]]
			}),
			sourceUuid: connection.endpoints[0].getUuid(),
			targetUuid: connection.endpoints[1].getUuid()
		});
	});
	var copyDataJson = {}
	copyDataJson.nodes = nodes;
	copyDataJson.connections = connections;
	copyDataJson.jsonendpoints = jsonendpoints;
	//copyDataJson.endpointMap = ""
	copyDataJson.endpointMap = JSON.stringify(strMapToObj(endpointMap));
	//console.log(copyDataJson);
	return copyDataJson;
}

//中间画布事件
// let flag = false;
// var mouseDown = 0
// var mouseMove = 0
// $("#drop-bg").mousedown(function(e) {
// 	flag = true;
// 	console.log(566666)
// 	mouseDown = mousePosition();

// })
// $("#drop-bg").mouseup((e) => {
// 	flag = false
// 	console.log(flag);
// })
// $('#drop-bg').mousemove(function(e) {
// 	if(flag) {
// 		mouseMove = mousePosition();
// 		//if(mouseDown.x - mouseMove.x > 0){
// 			console.log(111)
// 			$(".pa").each(function (idx, elem) {
// 				console.log(222)
// 				var $elem = $(elem);
// 				var elemLeft = $elem.offset().left
// 				$elem.css("left",mouseDown.x - mouseMove.x)
// 				$elem.css("top",mouseDown.y - mouseMove.y)
// 				jsPlumb.repaintEverything()
// 			})
// 		//}
// 	}
// })

//保存
$('#save').on('click', function () {
	handleMessageSave(save());
});

//加载
$('#load').on('click', function () {
	//state = true;
	loadJson();
});

function addPointDiv(proPream) {
	var str = "";
	if (proPream.data.variableStructType != '') {
		str += "<div class = 'point' style='background:#F0E6BD'>" +
			"<p>参数名称：" + proPream.data.variableName + "</p>" +
			"<p>参数类型：" + proPream.data.dataTypeName + "</p>" +
			"<p>参数长度：" + proPream.data.lengthName + "</p>" +
			"<p>结构体：" + proPream.data.variableStructType + "</p>" +
			"</div>";
	} else {
		str += "<div class = 'point' style='background:#F0E6BD'>" +
			"<p>参数名称：" + proPream.data.variableName + "</p>" +
			"<p>参数类型：" + proPream.data.dataTypeName + "</p>" +
			"<p>参数长度：" + proPream.data.lengthName + "</p>" +
			"</div>";
	}
	$('#drop-bg').append(str);
	//$('body').append(str);
	$(".point").bind('click', function (event) {
		//	console.log("+++++++++++++++",event)
		event.stopPropagation();
		$('.point').remove();
	})
	$(".point").bind('mousedown', function (event) {
		mousedownState = 1;
		return false
	})
}


//动态增加删除锚点
function updatePoint(proPream) {
	console.log("进入增加删除锚点", proPream);
	var endpoints = jsPlumb.getEndpoints(proPream.compId);
	//console.log(endpoints.length);
	var inputUuidList = new Array();
	var outputUuidList = new Array();
	$.each(endpoints, function (n, val) {
		if (val.getUuid().split('*')[1] == 'input') {
			inputUuidList.push(val)
		} else {
			outputUuidList.push(val);
		}
	});
	var b = $('#' + proPream.compId + '-heading')[0].dataset.id;
	if (proPream.addOrDel == 'add') {
		//将所有增加的参数保存
		addPointParam.push(proPream);
		var config = JSON.parse(JSON.stringify(getBaseNodeConfig()))
		endpointMap.set(proPream.uid, proPream.data)
		console.log("动态增加锚点后的数据", endpointMap)
		if (proPream.inOrOut == 'input') {
			//修改画布数据
			canvasData.get(proPream.compId).inputList.push(proPream.data)
			//console.log("修改数据", canvasData)
			var len = inputUuidList.length + 1;
			var anchorNumber = 1 / len;
			var differenceValue = anchorNumber / 2;
			$.each(endpoints, function (n, val) {
				if (val.anchor.x == 0) {
					val.anchor.y = differenceValue;
					differenceValue += anchorNumber
				}
			});
			config.isSource = false
			config.maxConnections = 1
			config.paintStyle = {
				strokeStyle: "#4ECB74",
				fillStyle: "transparent",
				radius: 5,
				lineWidth: 2
			}
			var addInPoint = jsPlumb.addEndpoint(proPream.compId, {
				anchors: [0, differenceValue, -1, 0],
				uuid: proPream.uid,
				deleteEndpointsOnEmpty: true
			}, config);
			addInPoint.addClass("nodeStyle")
			addInPoint.bind('dblclick', function (endpoint, originalEvent) {
				dblclickPoint = true
				addDiv(endpoint);
				//addPointDiv(proPream);
				//var mouse = mousePosition();
				var endpointId = endpoint.getUuid().split("*")[2]
				var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
				var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
				$('.point').css("position", "absolute");
				$('.point').css("left", x - 290);
				$('.point').css("top", y);
				//$('.point').css("border", "1px solid red");
			});
			addInPoint.bind('mousedown', function (endpoint, originalEvent) {
				mousedownState = 1;
			});
			addInPoint.bind('mouseout', function () {
				dblclickPoint = false
			});
		} else {
			canvasData.get(proPream.compId).outputList.push(proPream.data)
			//console.log("画布数据", canvasData);
			var len = outputUuidList.length + 1;
			var anchorNumber = 1 / len;
			var differenceValue = anchorNumber / 2;
			$.each(endpoints, function (n, val) {
				if (val.anchor.x == 1) {
					val.anchor.y = differenceValue;
					differenceValue += anchorNumber
				}
			});
			config.isTarget = false
			config.maxConnections = -1
			var addOutPoint = jsPlumb.addEndpoint(proPream.compId, {
				anchors: [1, differenceValue, 1, 0],
				uuid: proPream.uid,
				deleteEndpointsOnEmpty: true
			}, config);
			addOutPoint.addClass("nodeStyle")
			addOutPoint.bind('dblclick', function (endpoint, originalEvent) {
				dblclickPoint = true
				addDiv(endpoint);
				//addPointDiv(proPream);
				//var mouse = mousePosition();
				var endpointId = endpoint.getUuid().split("*")[2]
				var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
				var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
				$('.point').css("position", "absolute");
				$('.point').css("left", x - 15);
				$('.point').css("top", y);
			});
			addOutPoint.bind('mouseout', function () {
				dblclickPoint = false;
			});
			addOutPoint.bind('mousedown', function (endpoint, originalEvent) {
				mousedownState = 1;
			});
		}
		// jsPlumb.connect({ uuids: [endpoints[0].getUuid(), endpoints[0].getUuid()] })
		// var conn = jsPlumb.getConnections({
		// 	source: proPream.compId,
		// 	target: proPream.compId
		// });
		// if (conn[0]) {
		// 	jsPlumb.detach(conn[0]);
		// }
		jsPlumb.setSuspendDrawing(false, true);

	} else {
		var del = false;
		//console.log("需删除的锚点id",removeUuid(proPream))
		$.each(endpoints, function (n, val) {
			if (removeUuid(proPream) == val.getUuid()) {
				del = true;
				jsPlumb.deleteEndpoint(endpoints[n]);
			}
		});
		if (del) {
			var points = jsPlumb.getEndpoints(proPream.compId);
			if (proPream.inOrOut == 'input') {
				canvasData.get(proPream.compId).inputList.splice(proPream.compId.split("*")[0], 1)
				console.log("画布数据", canvasData)
				console.log("inputList", proPream.compId.split("*")[0])
				var len = inputUuidList.length - 1;
				//console.log(len);
				var anchorNumber = 1 / len;
				var differenceValue = anchorNumber / 2;
				$.each(points, function (n, val) {
					if (val.anchor.x == 0) {
						val.anchor.y = differenceValue;
						//console.log(val.anchor.y);
						differenceValue += anchorNumber
					}
				});
			} else {
				canvasData.get(proPream.compId).outputList.splice(proPream.compId.split("*")[0], 1)
				//console.log("画布数据", canvasData)
				var len = outputUuidList.length - 1;
				var anchorNumber = 1 / len;
				var differenceValue = anchorNumber / 2;
				$.each(points, function (n, val) {
					if (val.anchor.x == 1) {
						val.anchor.y = differenceValue
						differenceValue += anchorNumber
					}
				});
			}
			// jsPlumb.connect({ uuids: [points[0].getUuid(), points[0].getUuid()] })
			// var conn = jsPlumb.getConnections({
			// 	source: proPream.compId,
			// 	target: proPream.compId
			// });
			// if (conn[0]) {
			// 	jsPlumb.detach(conn[0]);
			// }
			jsPlumb.setSuspendDrawing(false, true);
		}
	}

	var newEndpoints = jsPlumb.getEndpoints(proPream.compId);
	var index = -1
	$.each(newEndpoints, function (n, val) {
		if (val.getUuid().split('*')[1] == 'input') {

		} else {
			index++
			val.connector[1].midpoint = midpoints[index]
			val.connector[1].stub = stub[index]
			//console.log("index++",index)
		}
	});
}

//获取需要删除的锚点id
function removeUuid(delData){
	let endpointUUid = ""
	for (let [k, v] of endpointMap) {
		console.log(k.split("*")[2] , delData.compId)
		console.log(k.split("*")[1] , delData.inOrOut)
		console.log(v.variableName ,delData.data.categoryName)
		if(k.split("*")[2] == delData.compId && k.split("*")[1] == delData.inOrOut && v.variableName == delData.data.variableName){
			endpointUUid = k
		}
	}
	return endpointUUid
}

//删除锚点
$('#delete').on('click', function () {
	var endpoints = jsPlumb.getEndpoints(gjTemplateId);
	jsPlumb.deleteEndpoint(endpoints[0]);
	//$.map(endpoints, function (endpoint) {
	endpoints[1].anchor.x = 0;
	endpoints[1].anchor.y = 0.5;
	jsPlumb.connect({ uuids: [endpoints[3].getUuid(), endpoints[3].getUuid()] })
})
//删除画布构件
function eventHandler(data) {
	if (data.type === 'deleteNode') {
		var b = $('#' + data.id + '-heading')[0].dataset.id;
		//console.log(data.id);
		//console.log(b);
		gjid = dat[b].id;
		var gjidAndTemid = [];
		gjidAndTemid.push({
			gjId: gjid,
			tmpId: data.id,
			state: 1
		});
		//gjIdAndTemId = gjidAndTemid;
		//console.log(gjIdAndTemId);
		emptyNode(data.id)
		//console.log("传值");
		handleMessageToParent("returnFormJson", gjidAndTemid);
		//	handleMessageToParent();
	}
}
// 删除一个节点以及
function emptyNode(id) {
	jsPlumb.remove(id)
}
//连线触发事件
//需保存到ctrlZ数组中的对象
var connInfoObj = {}
jsPlumb.bind("connection", function (connInfo, originalEvent) {
	if (isConnection) {
		connInfoObj = {}
		//console.log(connInfo)
		connInfoObj.conn = connInfo;
		connInfoObj.connSourceUUid = connInfo.sourceEndpoint.getUuid();
		connInfoObj.connTargetUUid = connInfo.targetEndpoint.getUuid()
		chartOperationStack['addConnection'].push(connInfoObj)
		chartRareOperationStack.push("addConnection");
		limitZ()
	}
	isConnection = true
	//console.log("保存连线对象",connectionObj)

})

//删除仿真图标
function deleteFzPicture() {
	var arr = $(".fz").toArray();
	arr.forEach(arrTmp => {
		$a = $(arrTmp);
		$("#" + $a.context.id).remove()
	})
}

//获取所有仿真所需数据
var simulationData = []
function getSimulationData() {
	simulationData = []
	let index = 0;
	$.each(jsPlumb.getConnections(), function (idx, connection) {
		console.log("connection.getLabel()",connection.getLabel())
		if (connection.getLabel() != null &&  connection.getLabel() != "") {
			index++
			// let simulation = {}
			let startId = connection.sourceId
			let endId = connection.targetId
			let startName = endpointMap.get(connection.endpoints[0].getUuid()).variableName
			let endName = endpointMap.get(connection.endpoints[1].getUuid()).variableName
			let simulation = startId +":"+ startName +"|" + endId + ":" + endName
		//	let simulation = startId +"|" + endId 
			if (index == 1) {
				simulationData.push(simulation)
			} else {
				for (let i = 0; i < simulationData.length; i++) {
					// if (connection.sourceId != simulationData[i].startId && connection.targetId != simulationData[i].endId) {
					// 	simulationData.push(simulation)
					// }
					if(simulation != simulationData[i]){
						simulationData.push(simulation)
					}
				}
			}
		}
	})
	handleMessageToParent("returnSimulationData", simulationData);
	console.log("仿真数据", simulationData)
}

//双击连线出现仿真图标
let fzMap = new Map()
jsPlumb.bind("dblclick", function (conn, originalEvent) {
	console.warn("双击连线出现仿真图标",dblclickPoint)
	if(!dblclickPoint){
		connectionObjClick = {};
		let startId = conn.sourceId
		let endId = conn.targetId
		if (componentMap.get(startId) === componentMap.get(endId)) {
			handleMessageToParent("returnFZInfo", "所选连线俩端构件属于同一组件");
		} else {
			let startName = endpointMap.get(conn.endpoints[0].getUuid()).variableName
			let endName = endpointMap.get(conn.endpoints[1].getUuid()).variableName
			let TempData = { startId: startId, endId: endId, startName:startName,endName:endName}
			fzMap.set(conn.id,TempData)
			let id = createNum()
			conn.setLabel(function () {
				return '<div class ="fz" id = "' + conn.id + '" style="width:20px;height:12px;"></div>'
			})
			console.log("仿真图标",$('#' + conn.id))
			$('#' + conn.id).attr('tabindex', 0);
			$('#' + conn.id).focus();
			$("#" + conn.id).bind("click", function () {
				document.getElementById(conn.id).onkeydown = function (e) {
					if (e.keyCode == 46) {
						$("#" + conn.id).remove()
						conn.setLabel('')
					}
				}
				return false
			})
			$("#" + conn.id).bind("dblclick", function (event) {
				//console.log("asdasdd",event.currentTarget.id)
				handleMessageToParent("returnFZ", fzMap.get(event.currentTarget.id));
			})
		}

	}
	
})
//var removeConnection; //点击连线所获取的对象
var connectionObj = {};//连线关系
var connectionObjClick = {}
//删除连线
jsPlumb.bind("click", function (conn, originalEvent) {
	//alert("单机连线")
	connectionObjClick = {};
	//console.log(connectionObjClick)
	//conn.getPaintStyle().stroke = "red"
	//conn.getPaintStyle().strokeStyle = "red"
	console.log("连线单击触发")
	connectionObjClick.removeConnection = conn
	//console.log(conn)
	connectionObjClick.connSourceUUid = conn.endpoints[0].getUuid();
	connectionObjClick.connTargetUUid = conn.endpoints[1].getUuid()


	//console.log(connectionObjClick)
	//console.log(conn)
	// document.onkeydown=function(event){
	// 	//var e = event || window.event || arguments.callee.caller.arguments[0];
	// 	if(event && event.keyCode==46){ // delete删除
	// 		alert(1111)
	// 		chartOperationStack['deleteConnection'].push(conn)
	// 		chartRareOperationStack.push("deleteConnection");
	// 		jsPlumb.detach(conn);
	// 	 }            
	// 	 return false;
	// }; 
	// console.log(conn)
	// console.log(originalEvent)
	// if (confirm("确定删除吗?")) {
	// 	chartOperationStack['deleteConnection'].push(conn)
	// 	chartRareOperationStack.push("deleteConnection");
	// 	jsPlumb.detach(conn);
	// }
});


// function connectionBeforeDropCheck(info) {
// 	if (!info.connection.source.dataset.id) {
// 		return true
// 	}
// 	return info.connection.source.dataset.id !== info.connection.target.dataset.id
// }

// 获取基本配置
function getBaseNodeConfig() {
	return Object.assign({}, visoConfig.baseStyle)
}

//获取连线锚点参数
// function anchorParameter(info, operating) {
// 	//连线源锚点uuid
// 	var outUuid = info.connection.endpoints[0].getUuid();
// 	//连线目标锚点uuid
// 	var inUuid = info.connection.endpoints[1].getUuid();
// 	var outgk = info.sourceId;
// 	var ingk = info.targetId;
// 	if (outgk != '' && ingk != '') {
// 		var indexOut = $('#' + outgk + '-heading').find("div").attr("id");
// 		var indexIn = $('#' + ingk + '-heading').find("div").attr("id");
// 		var outPramenter = dat[indexOut].outputList[outUuid];
// 		outPramenter['gjId'] = dat[indexOut].id;
// 		outPramenter['state'] = operating;
// 		//向父级页面传源锚点参数
// 		handleConnection(outPramenter);
// 		var inPramenter = dat[indexIn].inputList[inUuid];
// 		inPramenter['gjId'] = dat[indexIn].id;
// 		inPramenter['state'] = operating;
// 		//向父级页面传目标锚点参数
// 		handleConnection(inPramenter);
// 	}
// }

//连线触发事件进行校验
jsPlumb.bind("connectionDragStop", function (info) {
	var source = info.sourceId;
	var target = info.targetId;
	if (source != target) {
		var sourceUid = info.endpoints[0].getUuid();
		//console.log("源锚点UUid",sourceUid);
		var targetUid = info.endpoints[1].getUuid();
		//console.log("目标锚点",info.endpoints[1]);
		if (loadState) {
			addPointParam = addPointParam.concat(loadAddPointParam)
			//console.log("合并增加数据",addPointParam);
			loadState = false;
		}
		var sourceType = "";
		var targetType = "";
		sourceType = endpointMap.get(sourceUid).dataTypeName
		targetType = endpointMap.get(targetUid).dataTypeName
		//判断源锚点是否为新增
		//var sourcePointState = false;
		//新增源锚点数据下标
		//var sourceIndex = 0;
		// if (addPointParam.length > 0) {
		// 	$.each(addPointParam, function (index1, addPoinr) {
		// 		if (sourceUid == addPoinr.uid) {
		// 			sourcePointState = true;
		// 			sourceIndex = index1;
		// 		}
		// 	});
		// }
		// if (sourcePointState) {
		// 	sourceType = addPointParam[sourceIndex].data.dataTypeName;
		// 	//	console.log("源节点属性if",sourceType);
		// } else {
		// 	//获取源节点数据下标
		// 	var sourceGj = $('#' + source + '-heading')[0].dataset.id;
		// 	//sourceType = dat[sourceGj].outputList[sourceUid.split("*")[0]].dataTypeName;
		// 	sourceType = canvasData.get(source).outputList[sourceUid.split("*")[0]].dataTypeName
		// 	//console.log("源节点属性else",sourceType);
		// }

		//判断目标锚点是否为新增
		// targetPointState = false;
		// //新增目标节点数据下标
		// var targetIndex = 0;
		// if (addPointParam.length > 0) {
		// 	$.each(addPointParam, function (index2, addPoinr) {
		// 		//console.log("进入目标节点循环");
		// 		if (targetUid == addPoinr.uid) {
		// 			targetPointState = true;
		// 			targetIndex = index2;
		// 		}
		// 	});
		// }
		// if (targetPointState) {
		// 	targetType = addPointParam[targetIndex].data.dataTypeName
		// 	//console.log("目标节点属性if",targetType);
		// } else {
		// 	//获取目标节点数据下标
		// 	var targetGj = $('#' + target + '-heading')[0].dataset.id;
		// 	targetType = canvasData.get(target).inputList[targetUid.split("*")[0]].dataTypeName 
		// 	//targetType = dat[targetGj].inputList[targetUid.split("*")[0]].dataTypeName;
		// 	//console.log("目标节点属性else",targetType);
		// }
		// console.log("源节点类型",sourceType);
		// console.log("目标节点类型",targetType);
		// console.log("连线关系",connectionData)
		var isConnect = false
	
		sourceType = sourceType.replace("*","")
		targetType = targetType.replace("*","")
		console.log("源锚点数据类型",sourceType)
		console.log("目标锚点数据类型",targetType)
		if (sourceType == targetType) {
			isConnect = true
		}
		for (var i = 0; i < connectionData.length; i++) {
			// console.log("111",connectionData[i].label)
			// console.log("222",connectionData[i].value)
			if ((sourceType == connectionData[i].label && targetType == connectionData[i].value) || (sourceType == connectionData[i].value && targetType == connectionData[i].label)) {
				isConnect = true
			}
		}
		if (!isConnect) {
			jsPlumb.detach(info);
			handleMessageToParent("nodeTypeNotMatch", "节点类型不匹配");
		}
	}

});

//Ctrl+S
document.onkeydown = function (event) {
	if (event.ctrlKey == true && event.keyCode == 83) {//Ctrl+S
		event.returnvalue = false;
		event.preventDefault();
		//	alert(11111)
		var connected = jsPlumb.getConnections();
		//console.log("画布所有连线关系", connected)
		//saveFlowchart();
		//loadJson();
		//$(".fz").show();
		//alert(1111);
	}
}
//Ctrl+V粘贴
var copyTop = 0;
var copyLeft = 0;
document.onkeydown = function () {
	if (event.ctrlKey == true && event.keyCode == 86) {//Ctrl+V
		event.returnvalue = false;
		event.preventDefault();
		copyTop = copyTop + 50;
		copyLeft = copyLeft + 50;
		//console.log(11111111111111)
		if (copyData.length <= 0) {
			copyData = JSON.parse(sessionStorage.getItem("copyData"));
			//console.log("复制数据",copyData[0].endpointMap)
			let copyEndpointMap = objToMap(JSON.parse(copyData[0].endpointMap));
			//将解析数据重新加入endpointMap
			for (let [k, v] of copyEndpointMap) {
				endpointMap.set(k, v)
			}
		}
		for (var i = 0; i < copyData.length; i++) {
			pasteJson(copyData[i]);
		}
		idList = []
		$('.pa').removeClass("ui-selected")

		if (connectionMap.size > 1) {
			connectionMap.forEach(function (value, key) {
				idList.push(value)
				//console.log("2222222222", value)
				$("#" + value).addClass("ui-selected")
			})
		}

		//console.log("输出数组000000000000",idList)
		//粘贴将数据保存回退时操作	
		addPasteData = {}
		addPasteData.copyData = copyData
		addPasteData.idMap = connectionMap
		chartOperationStack['addPaste'].push(addPasteData)
		chartRareOperationStack.push("addPaste")
		limitZ();
		connectionMap = new Map()
	} else if (event.ctrlKey == true && event.keyCode == 67) {
		event.returnvalue = false;
		event.preventDefault();
		copyTop = 0;
		copyLeft = 0
		var copyPream = new Array()
		connectionMap.clear()
		for (var j = 0; j < idList.length; j++) {
			var copyJson = saveNodeJson(idList[j])
			copyPream.push(copyJson);
			copyData = copyPream
		}
		sessionStorage.setItem("copyData", JSON.stringify(copyData));
		var gjidAndTemid = []
		gjidAndTemid.push({
			gjId: "",
			tmpId: idList,
			state: 3
		})
		handleMessageToParent("returnFormJson", gjidAndTemid);
	} else if (event.keyCode == 37) {//向左
		event.preventDefault();
		oldPosition = [];
		newPosition = [];
		dragNode = {}
		for (var i = 0; i < idList.length; i++) {
			var div = document.getElementById(idList[i])
			//保存移动前的位置
			nodePosition = {}
			nodePosition.top = div.offsetTop
			nodePosition.left = div.offsetLeft
			nodePosition.id = idList[i]
			oldPosition.push(nodePosition)
			div.style.left = Math.max(0, div.offsetLeft - 10) + "px"
		}
		dragNode.oldPosition = oldPosition;
		jsPlumb.setSuspendDrawing(false, true);
		//保存移动后的位置
		for (var a = 0; a < idList.length; a++) {
			newPosition.push(saveNodePosition(idList[a]))
		}
		dragNode.newPosition = newPosition
		chartOperationStack['dragZ'].push(dragNode)
		chartRareOperationStack.push('dragZ')
		limitZ()
	} else if (event.keyCode == 39) {//向右
		event.preventDefault();
		oldPosition = [];
		newPosition = [];
		dragNode = {}
		for (var i = 0; i < idList.length; i++) {
			var div = document.getElementById(idList[i])
			//保存移动前的位置
			nodePosition = {}
			nodePosition.top = div.offsetTop
			nodePosition.left = div.offsetLeft
			nodePosition.id = idList[i]
			oldPosition.push(nodePosition)
			//div.style.left = Math.min($(".div_right").width(), div.offsetLeft + 10) + "px"
			div.style.left = div.offsetLeft + 10 + "px"
		}
		dragNode.oldPosition = oldPosition;
		jsPlumb.setSuspendDrawing(false, true);
		//保存移动后的位置
		for (var a = 0; a < idList.length; a++) {
			newPosition.push(saveNodePosition(idList[a]))
		}
		dragNode.newPosition = newPosition
		chartOperationStack['dragZ'].push(dragNode)
		chartRareOperationStack.push('dragZ')
		limitZ()
	} else if (event.keyCode == 38) {//向上
		event.preventDefault();
		oldPosition = [];
		newPosition = [];
		dragNode = {}
		for (var i = 0; i < idList.length; i++) {
			var div = document.getElementById(idList[i])
			//保存移动前的位置
			nodePosition = {}
			nodePosition.top = div.offsetTop
			nodePosition.left = div.offsetLeft
			nodePosition.id = idList[i]
			oldPosition.push(nodePosition)
			div.style.top = Math.max(0, div.offsetTop - 20) + "px"
		}
		dragNode.oldPosition = oldPosition;
		jsPlumb.setSuspendDrawing(false, true);
		//保存移动后的位置
		for (var a = 0; a < idList.length; a++) {
			newPosition.push(saveNodePosition(idList[a]))
		}
		dragNode.newPosition = newPosition
		chartOperationStack['dragZ'].push(dragNode)
		chartRareOperationStack.push('dragZ')
		limitZ();
	} else if (event.keyCode == 40) {//向下
		event.preventDefault();
		oldPosition = [];
		newPosition = [];
		dragNode = {}
		for (var i = 0; i < idList.length; i++) {
			var div = document.getElementById(idList[i])
			var div1 = document.getElementById("drop-bg")
			//保存移动前的位置
			nodePosition = {}
			nodePosition.top = div.offsetTop
			nodePosition.left = div.offsetLeft
			nodePosition.id = idList[i]
			oldPosition.push(nodePosition)
			//div.style.top = Math.min($(".div_right").height(), div.offsetTop + 10) + "px"
			div.style.top = div.offsetTop + 10 +"px"
			//if(div.style.top + div.style.height >= $(".div_right").height()){
			//div1.scrollTop = 10
			//}
			// if(isDropTop()){
			// 	//	div1.scrollTop += 10
			// 	console.log("当前滚动条高度",$(areaId).scrollTop())
			// 	div.style.top = div.offsetTop +10*3 + "px"
			// }else{
			// 	div.style.top = div.offsetTop + 10 +"px"
			// }
		}

		dragNode.oldPosition = oldPosition;
		jsPlumb.setSuspendDrawing(false, true);
		//保存移动后的位置
		for (var a = 0; a < idList.length; a++) {
			newPosition.push(saveNodePosition(idList[a]))
		}
		dragNode.newPosition = newPosition
		chartOperationStack['dragZ'].push(dragNode)
		chartRareOperationStack.push('dragZ')
		limitZ();
	} else if (event.ctrlKey == true && event.keyCode == 90) {//ctrl Z
		console.log("用户所有操作", chartRareOperationStack)
		var rareOperation = chartRareOperationStack.pop()
		console.log("ctrlZ对应操作", rareOperation)
		switch (rareOperation) {
			case "add"://如果是增加操作将删除
				ctrlYOperationStack.push("add")
				var operationJSON = chartOperationStack[rareOperation].pop();
				ctrlYStack['add'].push(operationJSON)
				limitY()
				var id = operationJSON.nodes[0].blockId
				jsPlumb.remove(id)
				var gjidAndTemid = [];
				gjidAndTemid.push({
					gjId: "",
					tmpId: [id],
					state: 1
				});
				handleMessageToParent("returnFormJson", gjidAndTemid);
				break;
			case "delete"://如果删除则为新增
				ctrlYOperationStack.push("delete")
				var operationJSON = chartOperationStack[rareOperation].pop();
				ctrlYStack['delete'].push(operationJSON)
				limitY()
				loadState = false;
				loadJson(operationJSON)
				break;
			case "addConnection":  //新增连线为删除连线
				var operationJSON
				var sourceAndTarget = {}
				console.log("删除连线状态", isRemoveConn)
				console.log("删除连线保存的数据", chartOperationStack["addConnection"])
				//if (!isRemoveConn) {
				var operationJSON1 = chartOperationStack[rareOperation].pop()
				console.log("删除的对象", operationJSON1)
				if (delNodeIsConnection(operationJSON1.conn.sourceId, operationJSON1.conn.targetId) ||
					delConnection(operationJSON1.connSourceUUid, operationJSON1.connTargetUUid)) {
					var connObj = newConnection.pop()
					console.log("删除构件删除的连线对象", connObj)
					operationJSON = connObj.removeConnection
					sourceAndTarget.connSourceUUid = connObj.connSourceUUid
					sourceAndTarget.connTargetUUid = connObj.connTargetUUid
					if (newConnection.length <= 0) {
						isRemoveConn = false
					}
				} else {
					console.log("删除原有连线状态", operationJSON1)
					operationJSON = operationJSON1.conn
					sourceAndTarget.connSourceUUid = operationJSON1.connSourceUUid
					sourceAndTarget.connTargetUUid = operationJSON1.connTargetUUid
				}
				// } else {
				// 	console.log("使用新增连线对象",newConnection)
				// 	chartOperationStack[rareOperation].pop()
				// 	var connObj = newConnection.pop()
				// 	operationJSON = connObj.removeConnection
				// 	sourceAndTarget.connSourceUUid = connObj.connSourceUUid
				// 	sourceAndTarget.connTargetUUid = connObj.connTargetUUid
				// 	if (newConnection.length <= 0) {
				// 		isRemoveConn = false
				// 	}
				// }
				//console.log("66666666",sourceAndTarget)
				ctrlYStack['addConnection'].push(sourceAndTarget)
				ctrlYOperationStack.push("addConnection");
				limitY();
				removeConn(operationJSON);
				//jsPlumb.detach(operationJSON);
				break;
			case "deleteConnection"://删除连线为新增连线
				//console.log(chartOperationStack[rareOperation])
				var operationJSON = chartOperationStack[rareOperation].pop();
				//console.log("删除后回退的对象", operationJSON);
				isConnection = false;
				//isRemoveConn = true;
				var conn = jsPlumb.connect({ uuids: [operationJSON.connSourceUUid, operationJSON.connTargetUUid] })
				//console.log("重新连接后的对象",conn)
				connectionObj = {}
				connectionObj.removeConnection = conn
				connectionObj.connSourceUUid = operationJSON.connSourceUUid
				connectionObj.connTargetUUid = operationJSON.connTargetUUid
				delConnectionMap.set(operationJSON.connTargetUUid, operationJSON.connSourceUUid)
				newConnection.push(connectionObj)
				//alert(111111111)
				ctrlYStack['deleteConnection'].push(connectionObj)
				ctrlYOperationStack.push("deleteConnection");
				limitY()
				break;
			case "dragZ":
				var operationJSON = chartOperationStack[rareOperation].pop();
				//console.log(operationJSON)
				for (var i = 0; i < operationJSON.oldPosition.length; i++) {
					var div = document.getElementById(operationJSON.oldPosition[i].id)
					div.style.top = operationJSON.oldPosition[i].top + "px"
					div.style.left = operationJSON.oldPosition[i].left + "px"
				}
				jsPlumb.setSuspendDrawing(false, true);
				ctrlYStack['dragY'].push(operationJSON)
				ctrlYOperationStack.push("dragY")
				limitY()
				break;
			case "addPaste": //删除复制后的节点
				copyTop = 0;
				copyLeft = 0;
				var operationJSON = chartOperationStack[rareOperation].pop();
				//console.log(operationJSON)
				var idMap = new Map(operationJSON.idMap)
				var pasteIds = new Array()
				//console.log(idMap)
				idMap.forEach(function (value, key) {
					pasteIds.push(value)
					jsPlumb.remove(value)
				})
				var gjidAndTemid = [];
				gjidAndTemid.push({
					gjId: "",
					tmpId: pasteIds,
					state: 1
				});
				handleMessageToParent("returnFormJson", gjidAndTemid);
				idList = []
				var addDivStyleJson;
				var prevIdMap;
				if (chartOperationStack[rareOperation].length == 0) {
					addDivStyleJson = operationJSON
					prevIdMap = new Map(addDivStyleJson.idMap)
					prevIdMap.forEach(function (value, key) {
						idList.push(key)
						$("#" + key).addClass("ui-selected")
					})
				} else {
					addDivStyleJson = chartOperationStack[rareOperation][chartOperationStack[rareOperation].length - 1]
					prevIdMap = new Map(addDivStyleJson.idMap)
					prevIdMap.forEach(function (value, key) {
						idList.push(value)
						$("#" + value).addClass("ui-selected")
					})
				}
				//console.log("回退后的数据", addDivStyleJson)
				var prevIdMap = new Map(addDivStyleJson.idMap)

				ctrlYStack['addPaste'].push(operationJSON)
				ctrlYOperationStack.push("addPaste")
				limitY()
				break;
			case "deletePaste": //删除框选后的节点
				//console.log("删除节点与连线")
				var operationJSON = chartOperationStack[rareOperation].pop();
				loadState = false;
				for (var i = 0; i < operationJSON.length; i++) {
					loadJson(operationJSON[i]);
				}
				ctrlYStack['deletePaste'].push(operationJSON)
				ctrlYOperationStack.push("deletePaste")
				limitY()
		}
	} else if (event.ctrlKey == true && event.keyCode == 89) {//ctrl Y
		var state = ctrlYOperationStack.pop();
		switch (state) {
			case "add":
				var addJSON = ctrlYStack[state].pop();
				loadState = false;
				loadJson(addJSON)
				chartOperationStack['add'].push(saveNodeJson(addJSON.nodes[0].blockId));
				//添加用户新操作
				chartRareOperationStack.push('add');
				break;
			case "delete":
				var deleteJSON = ctrlYStack[state].pop();
				chartOperationStack['delete'].push(saveNodeJson(deleteJSON.nodes[0].blockId));
				jsPlumb.remove(deleteJSON.nodes[0].blockId)
				//添加用户新操作
				chartRareOperationStack.push('delete');
				var gjidAndTemid = [];
				gjidAndTemid.push({
					gjId: "",
					tmpId: [deleteJSON.nodes[0].blockId],
					state: 1
				});
				handleMessageToParent("returnFormJson", gjidAndTemid);
				break;
			case "addConnection":
				//console.log(ctrlYStack[state].length)
				var deleteJSON = ctrlYStack[state].pop();
				//console.log(deleteJSON)
				//isConnection = false
				isRemoveConnY = true
				var conn = jsPlumb.connect({ uuids: [deleteJSON.connSourceUUid, deleteJSON.connTargetUUid] })
				var newConnY = {}
				newConnY.removeConnection = conn
				newConnY.connSourceUUid = deleteJSON.connSourceUUid
				newConnY.connTargetUUid = deleteJSON.connTargetUUid
				newConnectionY.push(newConnY)
				// chartOperationStack['addConnection'].push(conn)
				// chartRareOperationStack.push('addConnection');
				break;
			case "deleteConnection":
				var deleteJSON;
				if (!isRemoveConnY) {
					deleteJSON = ctrlYStack[state].pop();
				} else {
					deleteJSON = newConnectionY.pop()
					if (newConnectionY.length <= 0) {
						isRemoveConnY = false
					}
				}
				//console.log("要删除的连接线",deleteJSON)
				chartOperationStack['deleteConnection'].push(deleteJSON)
				chartRareOperationStack.push("deleteConnection");
				removeConn(deleteJSON.removeConnection)
				break;
			case "dragY":
				var deleteJSON = ctrlYStack[state].pop();
				//console.log(deleteJSON)
				for (var i = 0; i < deleteJSON.newPosition.length; i++) {
					var div = document.getElementById(deleteJSON.newPosition[i].id)
					div.style.top = deleteJSON.newPosition[i].top + "px"
					div.style.left = deleteJSON.newPosition[i].left + "px"
				}
				jsPlumb.setSuspendDrawing(false, true);
				chartOperationStack['dragZ'].push(deleteJSON)
				chartRareOperationStack.push('dragZ')
				break;
			case "addPaste":
				var deleteJSON = ctrlYStack[state].pop();
				var pasteData = deleteJSON.copyData
				copyTop = copyTop + 50;
				copyLeft = copyLeft + 50;
				for (var i = 0; i < pasteData.length; i++) {
					pasteJson(pasteData[i]);
				}
				chartOperationStack['addPaste'].push({ "copyData": pasteData, "idMap": connectionMap })
				chartRareOperationStack.push("addPaste")
				connectionMap = new Map()
				break;
			case "deletePaste":
				var deleteJSON = ctrlYStack[state].pop();
				//console.log(deleteJSON)
				chartOperationStack['deletePaste'].push(deleteJSON)
				chartRareOperationStack.push("deletePaste")
				var tempIds = new Array()
				for (var i = 0; i < deleteJSON.length; i++) {
					tempIds.push(deleteJSON[i].nodes[0].blockId)
					jsPlumb.remove(deleteJSON[i].nodes[0].blockId)
				}
				var gjidAndTemid = [];
				gjidAndTemid.push({
					gjId: "",
					tmpId: tempIds,
					state: 1
				});
				handleMessageToParent("returnFormJson", gjidAndTemid);
				break;
		}
	} else if (event.keyCode == 46) { //delete
		//console.log(connectionObjClick)
		if (Object.keys(connectionObjClick).length > 0) { //删除连线
			chartOperationStack['deleteConnection'].push(connectionObjClick)
			chartRareOperationStack.push("deleteConnection");
			limitZ();
			removeConn(connectionObjClick.removeConnection)
			connectionObjClick = {}
		} else if (idList.length > 0) { //delete删除节点包括框选删除，单点删除
			var copyPream = new Array()
			for (var i = 0; i < idList.length; i++) {
				var copyJson = saveNodeJson(idList[i])
				copyPream.push(copyJson)
				nodeList.push(idList[i])
			}
			//delNodeIsConnection()
			//isRemoveConn = true
			for (var a = 0; a < idList.length; a++) {
				jsPlumb.remove(idList[a])
			}
			//console.log("保存ctrlZ数据")
			chartOperationStack['deletePaste'].push(copyPream)
			chartRareOperationStack.push('deletePaste')
			limitZ();
			var gjidAndTemid = [];
			gjidAndTemid.push({
				gjId: "",
				tmpId: idList,
				state: 1
			});
			handleMessageToParent("returnFormJson", gjidAndTemid);
			idList = []
		}
	} else if (event.ctrlKey == true && event.keyCode == 88) { //剪切功能
		if (idList.length > 0) { //删除框选的节点
			var copyPream = new Array()
			copyTop = 0;
			copyLeft = 0
			for (var i = 0; i < idList.length; i++) {
				var copyJson = saveNodeJson(idList[i])
				copyPream.push(copyJson)
				copyData = copyPream
			}
			sessionStorage.setItem("copyData", JSON.stringify(copyData));
			for (var a = 0; a < idList.length; a++) {
				jsPlumb.remove(idList[a])
			}
			chartOperationStack['deletePaste'].push(copyPream)
			chartRareOperationStack.push('deletePaste')
			limitZ();

			var gjidAndTemid = []
			gjidAndTemid.push({
				gjId: "",
				tmpId: idList,
				state: 3
			})
			handleMessageToParent("returnFormJson", gjidAndTemid);

			var gjidAndTemids = [];
			gjidAndTemids.push({
				gjId: "",
				tmpId: idList,
				state: 1
			});
			handleMessageToParent("returnFormJson", gjidAndTemids);
			idList = []
		}
	}
}
//判断连线是否被删除
function delConnection(sourEndUuid, tarEndUuid) {
	let isDelConnection = false
	for (let [k, v] of delConnectionMap) {
		if (k == tarEndUuid && v == sourEndUuid) {
			isDelConnection = true
		}
	}
	return isDelConnection
}
//判断连线的构件是否被删除
function delNodeIsConnection(sourNodeId, tarNodeId) {
	let nodeIsDel = false
	for (let i = 0; i < nodeList.length; i++) {
		if (sourNodeId == nodeList[i] || tarNodeId == nodeList[i]) {
			nodeIsDel = true
		}
	}
	return nodeIsDel

}

function removeConn(obj) {
	//if(obj.endpoints != null){
	jsPlumb.detach(obj);
	//}	
}
// document.onkeydown = function(e){

// 	var e = window.event || e
// 	switch(e.keyCode){
// 		case 37:
// 			alert(11111);
// 			break;
// 	}
// }


//存储连线关系
var connectionMap = new Map();
//粘贴
function pasteJson(pasteDataJson) {
	var nodes = pasteDataJson.nodes;
	var endpoints = pasteDataJson.jsonendpoints;
	//console.log(nodes);
	$.each(nodes, function (index, elem) {
		//var newId = uuid.v1()
		var newId = createNum()
		//console.log("画布数据",canvasData,elem.blockId);
		canvasData.set(newId, elem.nodeData)
		connectionMap.set(elem.blockId, newId)
		// console.log("画布数据",canvasData);	
		// console.log("节点ID",elem.blockId)
		var top0 = elem.positionY + copyTop
		var left0 = elem.positionX + copyLeft
		var Template1 = ""
		Template1 +=
			"<div class='pa' id='" + newId + "' style='top:" + top0 + "px;left:" + left0 + "px'>" +
			// "<span class='delete-node pull-right' data-type='deleteNode' data-id='" + elem.blockId + "'>X</span>" +
			"<div class='responsive'>" +
			"<div id='" + newId + "-heading' data-id='" + elem.imageId + "'>" +
			elem.compImg +
			//"<div style='text-align:center;height:80px;width:160px;border:1px none ;border-radius:6px;background-color: #BB2D2D;display: block;'><img src='./img/u" + elem.imageId + ".png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + elem.imageId + "'>" + elem.text + "</div></div>"+
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>";
		$('#drop-bg').append(Template1);

		addDraggable(newId)

		$('#' + newId).bind('click', function (event) {
			event.stopPropagation();
			if (idList.length <= 0) {
				idList = []
				idList.push(newId)
			} else {
				if (idList.length == 1) {
					if (idList[0] != newId) {
						idList = []
						idList.push(newId)
					}
				}
			}
			if (lastTimeId != "") {
				$('#' + lastTimeId).removeClass("nodeStyle")
				$.each(jsPlumb.getEndpoints(lastTimeId), function (n, v) {
					v.removeClass("nodeStyle")
				});
			}

			$('#' + newId).addClass("nodeStyle")
			var loadendpoints = jsPlumb.getEndpoints(newId);
			var uuidList = new Array();
			$.each(loadendpoints, function (n, val) {
				val.addClass("nodeStyle")
				uuidList.push(val.getUuid());
			});

			lastTimeId = newId
			//console.log("uuid集合", uuidList);
			var TempData = [];
			TempData.push({
				//构件ID
				gjId: "",
				//构件模板ID
				tmpId: newId,
				//状态
				state: 2,
				//锚点uuid集合
				uuidList: uuidList
			});
			handleMessageToParent("returnFormJson", TempData);
			$('#' + newId).attr('tabindex', 0);
			$('#' + newId).focus();
			connectionObjClick = {};
			if (idList.length <= 0) {
				idList = []
				idList.push(newId)
			}
			// document.getElementById(newId).onkeydown=function (e){
			// 	if(e && e.keyCode==46){
			// 		//删除元素将删除数据保存到栈中
			// 		chartOperationStack['delete'].push(saveNodeJson(newId));
			// 		//添加删除操作
			// 		chartRareOperationStack.push('delete');
			// 		jsPlumb.remove(newId)
			// 		var gjidAndTemid = [];
			// 		gjidAndTemid.push({
			// 			gjId: elem.compId,
			// 			tmpId: newId,
			// 			state: 1
			// 		});
			// 		handleMessageToParent("returnFormJson",gjidAndTemid);
			// 	}else if(e && e.keyCode==67){
			// 		var copyJson = saveNodeJson(newId)
			// 		copyData.push(copyJson);
			// 	}else if(e && e.keyCode==88){
			// 		var copyJson = saveNodeJson(newId)
			// 		copyData.push(copyJson);
			// 		jsPlumb.remove(newId)
			// 		var gjidAndTemid = [];
			// 		gjidAndTemid.push({
			// 			gjId: elem.compId,
			// 			tmpId: newId,
			// 			state: 1
			// 		});
			// 		handleMessageToParent("returnFormJson",gjidAndTemid);
			// 	}
			// }
		});
		$('#' + newId).bind('mousedown', function (event) {
			mousedownState = 1;
			return false
		})
		// jsPlumb.draggable(newId, {
		// 	containment: 'parent'
		// })
		$('.delete-node').off("click").on("click", function () { });
		$('.delete-node').bind('click', function (event) {
			event.stopPropagation();
			var removeTemp = [];
			removeTemp.push({
				//构件ID
				gjId: $('#' + event.target.dataset.id + '-heading')[0].dataset.id,
				//构件模板ID
				tmpId: event.target.dataset.id,
				//状态
				state: 1
			});
			//console.log(removeTemp);
			jsPlumb.remove(event.target.dataset.id)
			handleMessageToParent("returnFormJson", removeTemp);
		});
		var inputIndex = -1;
		var outIndex = -1
		$.each(endpoints, function (index, endpoint) {
			var config = JSON.parse(JSON.stringify(getBaseNodeConfig()))
			if (endpoint.anchorX == 0) {
				var midpoints1 = [0.2, 0.4, 0.6, 0.8]
				var midpoints = [30, 40, 50, 60]
				inputIndex++
				config.isSource = false
				config.maxConnections = 1
				config.paintStyle = {
					strokeStyle: "#4ECB74",
					fillStyle: "transparent",
					radius: 5,
					lineWidth: 2
				}
				config.connector[1].midpoint = midpoints1[inputIndex]
				config.connector[1].stub = midpoints[inputIndex]
				var inPoint = jsPlumb.addEndpoint(newId, {
					anchors: [0, endpoint.anchorY, -1, 0],
					uuid: endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + newId,
				}, config)
				endpointMap.set(endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + newId,
					JSON.parse(JSON.stringify(endpointMap.get(
						endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + elem.blockId
					))))
				inPoint.bind('dblclick', function (endpoint, originalEvent) {
					dblclickPoint = true
					addDiv(endpoint);
					//addTemDiv(endpoint, canvasData.get(newId).inputList);
					var endpointId = endpoint.getUuid().split("*")[2]
					var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
					var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
					$('.point').css("position", "absolute");
					$('.point').css("left", x - 290);
					$('.point').css("top", y);
				});
				inPoint.bind('mouseout', function (endpoint, originalEvent) {
					dblclickPoint = false
				});
				inPoint.bind('mousedown', function (endpoint, originalEvent) {
					mousedownState = 1;
				});
			} else {
				var midpoints = [0.7, 0.5, 0.3, 0.1]
				var stub = [40, 30, 20, 10]
				outIndex++
				config.isTarget = false
				config.maxConnections = -1
				console.log(midpoints)
				config.connector[1].midpoint = midpoints[outIndex]
				config.connector[1].stub = stub[outIndex]
				var outPoint = jsPlumb.addEndpoint(newId, {
					anchors: [1, endpoint.anchorY, 1, 0],
					uuid: endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + newId,
				}, config)
				endpointMap.set(endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + newId,
					JSON.parse(JSON.stringify(endpointMap.get(
						endpoint.uuid.split("*")[0] + "*" + endpoint.uuid.split("*")[1] + "*" + elem.blockId
					))))
				outPoint.bind('dblclick', function (endpoint, originalEvent) {
					dblclickPoint = true
					//addTemDiv(endpoint, canvasData.get(newId).outputList);
					addDiv(endpoint);
					var endpointId = endpoint.getUuid().split("*")[2]
					var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
					var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
					$('.point').css("position", "absolute");
					$('.point').css("left", x - 15);
					$('.point').css("top", y);
				});
				outPoint.bind('mouseout', function (endpoint, originalEvent) {
					dblclickPoint = false
				});
				outPoint.bind('mousedown', function (endpoint, originalEvent) {
					mousedownState = 1;
				});
			}
		});
		var gjidAndTemid = [];
		var b = $('#' + newId + '-heading')[0].dataset.id;
		var points = jsPlumb.getEndpoints(newId);
		var uuidList = new Array();
		$.each(points, function (n, val) {
			uuidList.push(val.getUuid());
		});
		gjidAndTemid.push({
			//构件ID
			gjId: dat[b].id,
			//构件模板新ID
			newTmpId: newId,
			//构件模板旧id
			oldTmpId: elem.blockId,
			//状态
			state: 4,
			//节点所有锚点uuid
			uuidList: uuidList
		});
		//console.log("参数：",JSON.stringify(gjidAndTemid));
		//gjIdAndTemId = gjidAndTemid;
		//console.log("粘贴后进行传值", gjidAndTemid)
		handleMessageToParent("returnFormJson", gjidAndTemid);
	});
	var connections = pasteDataJson.connections;
	$.each(connections, function (index, elem) {
		var sourceData = elem.sourceUuid.split("*")
		var targetData = elem.targetUuid.split("*")
		var sourceUUid = sourceData[0] + "*" + sourceData[1] + "*" + connectionMap.get(sourceData[2])
		var targetUUid = targetData[0] + "*" + targetData[1] + "*" + connectionMap.get(targetData[2])
		isConnection = false;
		jsPlumb.connect({ uuids: [sourceUUid, targetUUid] })
	});
	console.log("锚点数据", endpointMap)

}

//保存arrow集合
function save() {
	var connected = jsPlumb.getConnections();
	var connect = [];
	var conn = [];
	var connects = {};
	var con = [];
	var map = new Map();
	if (loadState) {
		addPointParam = addPointParam.concat(loadAddPointParam)
		//console.log("合并增加数据", addPointParam);
		loadState = false;
	}
	$.each(connected, function (id, connection) {
		//console.log(id);
		//获取画布源节点id
		var source = connection.sourceId;
		//获取源构件中数据id
		var sourceGj = $('#' + source + '-heading')[0].dataset.id;
		//console.log("sourceGj", sourceGj);
		//var dataSouGjId = dat[sourceGj].id
		//获取画布源锚点uuid
		var sourceuid = connection.endpoints[0].getUuid();

		//设置当前连接线源锚点是否为新增
		// var addsourcePointState = false;
		// //设置新增源锚点在addPointParam集合中的下标
		// var addSPointParamindex = 0;
		// if (addPointParam.length > 0) {
		// 	$.each(addPointParam, function (index1, addPoinr) {
		// 		if (sourceuid == addPoinr.uid) {
		// 			addsourcePointState = true;
		// 			addSPointParamindex = index1;
		// 		}
		// 	});
		// }
		var sourDataTypeName = "";
		var sourVariableName = "";
		var sourLength = "";
		sourDataTypeName = endpointMap.get(sourceuid).dataTypeName
		sourVariableName = endpointMap.get(sourceuid).variableName
		// if (!reg.test(sourVariableName)) {
		// 	let strTemp = sourVariableName.replace("*")
		// 	console.log("临时数据",strTemp)
		// }
		sourLength = endpointMap.get(sourceuid).lengthName
		// if (addsourcePointState) {
		// 	sourDataTypeName = addPointParam[addSPointParamindex].data.dataTypeName;
		// 	sourVariableName = addPointParam[addSPointParamindex].data.variableName;
		// 	sourLength = addPointParam[addSPointParamindex].data.lengthName;
		// } else {
		// 	var str = sourceuid.split('*');
		// 	//var s1 =  sourceuid.substr(0,4);
		// 	var sourceUuid = str[0];
		// 	//获取数据输出参数
		// 	//var sourcePrame = dat[sourceGj].outputList[sourceUuid].dataTypeName;
		// 	// sourDataTypeName = dat[sourceGj].outputList[sourceUuid].dataTypeName;
		// 	// sourVariableName = dat[sourceGj].outputList[sourceUuid].variableName;
		// 	// sourLength = dat[sourceGj].outputList[sourceUuid].lengthName;
		// 	sourDataTypeName = canvasData.get(str[2]).outputList[sourceUuid].dataTypeName;
		// 	sourVariableName = canvasData.get(str[2]).outputList[sourceUuid].variableName;
		// 	sourLength = canvasData.get(str[2]).outputList[sourceUuid].lengthName;
		// }
		//console.log("arrow数据11111111",dat[sourceGj])
		// var sourCompName = dat[sourceGj].functionName;
		// console.log("数据比较",sourCompName)
		var sourCompName = canvasData.get(source).functionName;
		//console.log("数据比较+++++++++",sourCompName)
		var sourcePrame = sourDataTypeName + " " + sourVariableName + "_" + sourCompName
		let reg = /\w+\[[0-9]+\]/i; //
		if (reg.test(sourVariableName)) {
			let strTemp = sourVariableName.split("[")
			console.log(strTemp)
			sourcePrame = sourDataTypeName + " " + strTemp[0] + "_"+ sourCompName + "[" + strTemp[1]
		}
		if(sourVariableName.indexOf("->")!= -1){
			let strTemp = sourVariableName.split("->")
			sourcePrame = sourDataTypeName + " " + strTemp[0] + "_"+ sourCompName + "->" + strTemp[1]
		}
		console.log("sourVariableName",sourVariableName)
		console.log("sourVariableName",sourVariableName.indexOf("->"))


		//获取画布目标节点id
		var target = connection.targetId;
		//获取目标构件中数据id
		var targetGj = $('#' + target + '-heading')[0].dataset.id;
		//var dataTarGjId = dat[targetGj].id;
		//获取画布目标锚点uuid
		var targetid = connection.endpoints[1].getUuid();
		// //设置当前连线目标节点是否为新增
		// var addTargetPointParam = false;
		// ////设置新增目标锚点在addPointParam集合中的下标
		// var addTPointParamindex = 0;
		// if (addPointParam.length > 0) {
		// 	$.each(addPointParam, function (index1, addPoinr) {
		// 		if (targetid == addPoinr.uid) {
		// 			addTargetPointParam = true;
		// 			addTPointParamindex = index1;
		// 		}
		// 	});
		// }
		var tarDataTypeName = "";
		var tarVariableName = "";
		tarDataTypeName = endpointMap.get(targetid).dataTypeName
		tarVariableName = endpointMap.get(targetid).variableName
		// if (addTargetPointParam) {
		// 	tarDataTypeName = addPointParam[addTPointParamindex].data.dataTypeName;
		// 	tarVariableName = addPointParam[addTPointParamindex].data.variableName;
		// } else {
		// 	var str = targetid.split('*');
		// 	//console.log("+++++++++++++++",str)
		// 	var targetUuid = str[0];
		// 	// tarDataTypeName = dat[targetGj].inputList[targetUuid].dataTypeName;
		// 	// tarVariableName = dat[targetGj].inputList[targetUuid].variableName;
		// 	tarDataTypeName = canvasData.get(str[2]).inputList[targetUuid].dataTypeName;
		// 	tarVariableName = canvasData.get(str[2]).inputList[targetUuid].variableName;
		// }

		//获取数据输入参数
		//var tarCompName = dat[targetGj].functionName;
		var tarCompName = canvasData.get(target).functionName
		var targetPrame = tarDataTypeName + " " + tarVariableName + "_" + tarCompName
		console.log("tarVariableName",tarVariableName)
		if (reg.test(tarVariableName)) {
			let strTemp = tarVariableName.split("[")
			console.log(strTemp)
			targetPrame = tarDataTypeName + " " + strTemp[0] + "_"+ tarCompName + "[" + strTemp[1]
		}
		if(tarVariableName.indexOf("->")!= -1){
			let strTemp = tarVariableName.split("->")
			targetPrame = tarDataTypeName + " " + strTemp[0] + "_"+ tarCompName + "->" + strTemp[1]
		}
		//拼接source与target
		var st = source + target;
		map.set(id, st);

		if (map.get(id) != map.get(id - 1)) {
			//console.log("清空"+id);
			connect = [];
			conn = [];
			con1 = {};
			con2 = {};
			con3 = {};
		}
		//组装json
		var con1 = {
			drawing: connect,
			basic: conn
		}
		var con2 = {
			startItem: source,
			endItem: target
		};
		var con3 = {
			start: sourcePrame,
			end: targetPrame,
			length: sourLength,
			id: id,
			endId: targetid,
			stateId: sourceuid
		};
		conn.push(con3);


		if (id == 0 || map.get(id) != map.get(id - 1)) {
			connect.push(con2);
			con.push(con1);
		}

	});
	connects = { arrow: con };
	//console.log("arrow串", JSON.stringify(connects));
	return JSON.stringify(connects);
}
//清空当前画布构件
function cleanCanvas() {
	$(".pa").each(function (index, elem) {
		var $elem = $(elem)
		jsPlumb.remove($elem.attr('id'))
	})
}
//保存流程图为json字符串
function saveFlowchart() {
	//画布节点信息
	var nodes = [];
	//节点锚点信息
	var jsonendpoints = [];
	//循环画布中所有构件
	$(".pa").each(function (idx, elem) {
		var $elem = $(elem);
		//i用于定位使用什么数据
		var i = $elem.find("div").find("div")[0].dataset.id;
		var endpoints = jsPlumb.getEndpoints($elem.attr('id'));
		//console.log(endpoints);
		//循环构件上的锚点
		$.map(endpoints, function (endpoint) {
			jsonendpoints.push({
				anchorX: endpoint.anchor.x,
				anchorY: endpoint.anchor.y,
				uuid: endpoint.getUuid(),
				id: $elem.attr('id')
			});
		});
		nodes.push({
			blockId: $elem.attr('id'),
			//nodetype: $elem.attr('data-nodetype'),
			positionX: parseInt($elem.css("left"), 10),
			positionY: parseInt($elem.css("top"), 10),
			imageId: i,
			//text: $('#' + $elem.attr('id') + '-heading').find("div").find("div").html(),
			outPointsData: canvasData.get($elem.attr('id')).outputList,
			inPointsData: canvasData.get($elem.attr('id')).inputList,
			compImg: canvasData.get($elem.attr('id')).compImg,
			compId: canvasData.get($elem.attr('id')).compId,
			nodeData: canvasData.get($elem.attr('id'))
		});
		//nodes.push(jsonendpoints);
		//outPointsData.push(dat[i].outputList);
		//console.log(JSON.stringify(dat[i].outputList));
		//console.log(outPointsData);
	});
	//console.log(JSON.stringify(nodes));
	var connections = [];
	//循环当前所有连线
	$.each(jsPlumb.getConnections(), function (idx, connection) {
		//console.log(connection);
		connections.push({
			connectionId: connection.id,
			pageSourceId: connection.sourceId,
			pageTargetId: connection.targetId,
			anchors: $.map(connection.endpoints, function (endpoint) {
				return [[endpoint.anchor.x,
				endpoint.anchor.y,
				endpoint.anchor.orientation[0],
				endpoint.anchor.orientation[1],
				endpoint.anchor.offsets[0],
				endpoint.anchor.offsets[1]]]
			}),
			sourceUuid: connection.endpoints[0].getUuid(),
			targetUuid: connection.endpoints[1].getUuid()
		});
	});
	//console.log(connections);
	var flowChart = {};
	flowChart.nodes = nodes;
	flowChart.connections = connections;
	flowChart.jsonendpoints = jsonendpoints;
	flowChart.addPointParam = addPointParam;
	flowChart.endpointMap = JSON.stringify(strMapToObj(endpointMap));
	//flowChart.outPointsData = outPointsData;
	//flowChart.inPointsData = inPointsData;
	var flowChartJson = JSON.stringify(flowChart);
	// console.log("锚点map", flowChart, endpointMap)
	// console.log("流程图数据", flowChartJson);
	return flowChartJson;
}

//map转json
function strMapToObj(strMap) {
	let obj = Object.create(null)
	for (let [k, v] of strMap) {
		obj[k] = v
	}
	return obj
}

//json转为map
function objToMap(obj) {
	let strMap = new Map()
	for (let k of Object.keys(obj)) {
		strMap.set(k, obj[k])
	}
	return strMap
}

//添加锚点信息(还原)
function addTemDiv(endpoint, PointsData) {
	//console.log("添加锚点信息(还原)", PointsData);
	var templateUuid = endpoint.getUuid().split('*');
	var tempUuid = templateUuid[0];
	//console.log("信息", tempUuid)
	var str = '';
	if (PointsData[tempUuid].variableStructType != '') {
		str += "<div class = 'point' style='background:#F0E6BD'>" +
			"<p>参数名称：" + PointsData[tempUuid].variableName + "</p>" +
			"<p>参数类型：" + PointsData[tempUuid].dataTypeName + "</p>" +
			"<p>参数长度：" + PointsData[tempUuid].lengthName + "</p>" +
			"<p>结构体：" + PointsData[tempUuid].variableStructType + "</p>" +
			"</div>";
	} else {
		str += "<div class = 'point' style='background:#F0E6BD'>" +
			"<p>参数名称：" + PointsData[tempUuid].variableName + "</p>" +
			"<p>参数类型：" + PointsData[tempUuid].dataTypeName + "</p>" +
			"<p>参数长度：" + PointsData[tempUuid].lengthName + "</p>" +
			"</div>";
	}
	$('#drop-bg').append(str);
	//$('body').append(str);
	$(".point").bind('click', function (event) {
		//	console.log("+++++++++++++++",event)
		event.stopPropagation();
		$('.point').remove();
	})
	$(".point").bind('mousedown', function (event) {
		mousedownState = 1;
		return false
	})
}
// function addTemplate(){

// }
//解析json字符串还原流程图
function loadJson(loadJson) {
	console.log("粘贴", loadJson);
	//var loadJson = {"nodes":[{"blockId":"2521ced0-8455-11e9-8180-6b92e201c8cd","positionX":120,"positionY":72,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"25cbca70-8455-11e9-8180-6b92e201c8cd","positionX":536,"positionY":69,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"268f1a70-8455-11e9-8180-6b92e201c8cd","positionX":486,"positionY":245,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"278672c0-8455-11e9-8180-6b92e201c8cd","positionX":188,"positionY":243,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"}],"connections":[{"connectionId":"con_28","pageSourceId":"2521ced0-8455-11e9-8180-6b92e201c8cd","pageTargetId":"25cbca70-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*25cbca70-8455-11e9-8180-6b92e201c8cd"},{"connectionId":"con_33","pageSourceId":"25cbca70-8455-11e9-8180-6b92e201c8cd","pageTargetId":"268f1a70-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*25cbca70-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*268f1a70-8455-11e9-8180-6b92e201c8cd"},{"connectionId":"con_38","pageSourceId":"2521ced0-8455-11e9-8180-6b92e201c8cd","pageTargetId":"278672c0-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*278672c0-8455-11e9-8180-6b92e201c8cd"}],"jsonendpoints":[{"anchorX":0,"anchorY":0.5,"uuid":"0*input*2521ced0-8455-11e9-8180-6b92e201c8cd","id":"2521ced0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","id":"2521ced0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*25cbca70-8455-11e9-8180-6b92e201c8cd","id":"25cbca70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*25cbca70-8455-11e9-8180-6b92e201c8cd","id":"25cbca70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*268f1a70-8455-11e9-8180-6b92e201c8cd","id":"268f1a70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*268f1a70-8455-11e9-8180-6b92e201c8cd","id":"268f1a70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*278672c0-8455-11e9-8180-6b92e201c8cd","id":"278672c0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*278672c0-8455-11e9-8180-6b92e201c8cd","id":"278672c0-8455-11e9-8180-6b92e201c8cd"}]}
	var nodes = loadJson.nodes;
	var endpoints = loadJson.jsonendpoints;
	//console.log("锚点数据", endpoints)
	var addPoinrData = loadJson.addPointParam;
	if (loadJson.endpointMap != "") {
		var loadEndpointMap = objToMap(JSON.parse(loadJson.endpointMap));
		//将解析数据重新加入endpointMap
		for (let [k, v] of loadEndpointMap) {
			endpointMap.set(k, v)
		}
		console.log("获取解析的json", loadEndpointMap)
		console.log("解析json串---锚点map", endpointMap)
	}
	//console.log(nodes);
	$.each(nodes, function (index, elem) {
		canvasData.set(elem.blockId, elem.nodeData)
		var Template1 = ""
		Template1 +=
			"<div class='pa' id='" + elem.blockId + "' style='top:" + elem.positionY + "px;left:" + elem.positionX + "px'>" +
			// "<span class='delete-node pull-right' data-type='deleteNode' data-id='" + elem.blockId + "'>X</span>" +
			"<div class='responsive'>" +
			"<div id='" + elem.blockId + "-heading' data-id='" + elem.imageId + "'>" +
			elem.compImg +
			//"<div style='text-align:center;height:80px;width:160px;border:1px none ;border-radius:6px;background-color: #BB2D2D;display: block;'><img src='./img/u" + elem.imageId + ".png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + elem.imageId + "'>" + elem.text + "</div></div>"+
			"</div>" +
			"</div>" +
			"</div>" +
			"</div>";
		$('#drop-bg').append(Template1);

		addDraggable(elem.blockId)

		$('#' + elem.blockId).bind('click', function (event) {
			event.stopPropagation();
			connectionObjClick = {};
			if (idList.length <= 0) {
				idList = []
				idList.push(elem.blockId)
			} else {
				if (idList.length == 1) {
					if (idList[0] != elem.blockId) {
						idList = []
						idList.push(elem.blockId)
					}
				}
			}
			var loadendpoints = jsPlumb.getEndpoints(elem.blockId);
			var uuidList = new Array();
			$.each(loadendpoints, function (n, val) {
				val.addClass("nodeStyle")
				uuidList.push(val.getUuid());
			});
			$('#' + elem.blockId).addClass("nodeStyle")
			if (lastTimeId != "") {
				$('#' + lastTimeId).removeClass("nodeStyle")
				$.each(jsPlumb.getEndpoints(lastTimeId), function (n, v) {
					v.removeClass("nodeStyle")
				});
			}
			// $.each(endpoints, function (n, val) {
			// 	val.addClass("nodeStyle")
			// });
			lastTimeId = elem.blockId
			//console.log("uuid集合", uuidList);
			var TempData = [];
			TempData.push({
				//构件ID
				gjId: elem.compId,
				//构件模板ID
				tmpId: elem.blockId,
				//状态
				state: 2,
				//锚点uuid集合
				uuidList: uuidList
			});
			handleMessageToParent("returnFormJson", TempData);
			$('#' + elem.blockId).attr('tabindex', 0);
			$('#' + elem.blockId).focus();
			// document.getElementById(elem.blockId).onkeydown=function (e){
			// 	if(e && e.keyCode==46){
			// 		jsPlumb.remove(elem.blockId)
			// 		var gjidAndTemid = [];
			// 		gjidAndTemid.push({
			// 			gjId: elem.compId,
			// 			tmpId: elem.blockId,
			// 			state: 1
			// 		});
			// 		handleMessageToParent("returnFormJson",gjidAndTemid);
			// 	}
			// }
		});
		// jsPlumb.draggable(elem.blockId, {
		// 	containment: 'parent'
		// })
		$('#' + elem.blockId).bind('mousedown', function (event) {
			mousedownState = 1;
			return false
		})
		$('.delete-node').off("click").on("click", function () { });
		$('.delete-node').bind('click', function (event) {
			event.stopPropagation();
			var removeTemp = [];
			removeTemp.push({
				//构件ID
				gjId: $('#' + event.target.dataset.id + '-heading')[0].dataset.id,
				//构件模板ID
				tmpId: event.target.dataset.id,
				//状态
				state: 1
			});
			//console.log(removeTemp);
			jsPlumb.remove(event.target.dataset.id)
			handleMessageToParent("returnFormJson", removeTemp);
		});
		//设置锚点是否为新增
		var addinPointState = false;
		var addoutPointState = false;
		//设置当前锚点显示参数下标
		var addPoinrIndex = 0;
		var inputIndex = -1
		var outIndex = -1
		$.each(endpoints, function (index, endpoint) {
			if (endpoint.id == elem.blockId) {
				var config = JSON.parse(JSON.stringify(getBaseNodeConfig()))
				if (endpoint.anchorX == 0) {
					var midpoints1 = [0.2, 0.4, 0.6, 0.8]
					var midpoints = [30, 40, 50, 60]
					inputIndex++
					config.isSource = false
					config.maxConnections = 1
					config.paintStyle = {
						strokeStyle: "#4ECB74",
						fillStyle: "transparent",
						radius: 5,
						lineWidth: 2
					}
					config.connector[1].midpoint = midpoints1[inputIndex]
					config.connector[1].stub = midpoints[inputIndex]
					var inPoint = jsPlumb.addEndpoint(elem.blockId, {
						anchors: [0, endpoint.anchorY, -1, 0],
						uuid: endpoint.uuid,
					}, config)
					inPoint.bind('dblclick', function (endpoint, originalEvent) {
						// $.each(addPoinrData, function (index1, addPoinr) {
						// 	if (endpoint.getUuid() == addPoinr.uid) {
						// 		addinPointState = true;
						// 		addPoinrIndex = index1;
						// 	} else {
						// 		addinPointState = false;
						// 	}
						// });
						// if (addinPointState) {
						// 	addPointDiv(addPoinrData[addPoinrIndex]);
						// } else {
						// 	addTemDiv(endpoint, elem.inPointsData);
						// }
						dblclickPoint = true
						//锚点悬浮框显示
						addDiv(endpoint);
						// var mouse = mousePosition();
						// console.log(mouse)
						var endpointId = endpoint.getUuid().split("*")[2]
						var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
						var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
						$('.point').css("position", "absolute");
						$('.point').css("left", x - 290);
						$('.point').css("top", y);
					});
					inPoint.bind('mouseout', function (endpoint, originalEvent) {
						dblclickPoint = false
					});
					inPoint.bind('mousedown', function (endpoint, originalEvent) {
						mousedownState = 1;
					});
				} else {
					var midpoints = [0.7, 0.5, 0.3, 0.1]
					var stub = [40, 30, 20, 10]
					outIndex++
					config.isTarget = false
					config.maxConnections = -1
					config.connector[1].midpoint = midpoints[outIndex]
					config.connector[1].stub = stub[outIndex]
					var outPoint = jsPlumb.addEndpoint(elem.blockId, {
						anchors: [1, endpoint.anchorY, 1, 0],
						uuid: endpoint.uuid,
					}, config)
					outPoint.bind('dblclick', function (endpoint, originalEvent) {
						// $.each(addPoinrData, function (index1, addPoinr) {
						// 	if (endpoint.getUuid() == addPoinr.uid) {
						// 		addoutPointState = true;
						// 		addPoinrIndex = index1;
						// 	} else {
						// 		addoutPointState = false;
						// 	}
						// });
						// if (addoutPointState) {
						// 	addPointDiv(addPoinrData[addPoinrIndex]);
						// } else {
						// 	addTemDiv(endpoint, elem.outPointsData);
						// }
						//var mouse = mousePosition();
						dblclickPoint = true
						addDiv(endpoint);
						var endpointId = endpoint.getUuid().split("*")[2]
						var y = $("#" + endpointId).offset().top + $(areaId).scrollTop();
						var x = $("#" + endpointId).offset().left + $(areaId).scrollLeft();
						$('.point').css("position", "absolute");
						$('.point').css("left", x - 15);
						$('.point').css("top", y);
					});
					outPoint.bind('mouseout', function (endpoint, originalEvent) {
						dblclickPoint = false
					});
					outPoint.bind('mousedown', function (endpoint, originalEvent) {
						mousedownState = 1;
					});
				}
			}

		});
		//console.log("是否执行加载",loadingState)
		if (!loadState) {
			//console.log("22222222222222",elem.blockId)
			var gjidAndTemid = [];
			var b = $('#' + elem.blockId + '-heading')[0].dataset.id;
			var points = jsPlumb.getEndpoints(elem.blockId);
			var uuidList = new Array();
			$.each(points, function (n, val) {
				uuidList.push(val.getUuid());
			});
			gjidAndTemid.push({
				//构件ID
				gjId: dat[b].id,
				//构件模板新ID
				tmpId: elem.blockId,
				//状态
				state: 0,
				//节点所有锚点uuid
				uuidList: uuidList
			});
			handleMessageToParent("returnFormJson", gjidAndTemid);
		}
	});

	var connections = loadJson.connections;
	console.log
	$.each(connections, function (index, elem) {
		isConnection = false;
		var c = jsPlumb.connect({ uuids: [elem.sourceUuid, elem.targetUuid] })
		connectionObj = {}
		connectionObj.removeConnection = c
		connectionObj.connSourceUUid = elem.sourceUuid
		connectionObj.connTargetUUid = elem.targetUuid
		if (c != undefined) {
			delConnectionMap.set(elem.targetUuid, elem.sourceUuid)
			newConnection.push(connectionObj)
		}
		console.log("新连接newConnection", newConnection)
	});
}

//框选
var rector = $('#selectionRect');
//拖选起点
var startX = 0;
var startY = 0;

//选择框核心代码
function resizeToPoint(x, y) {
	var width = Math.abs(x - startX);
	var height = Math.abs(y - startY);
	rector.width(width);
	rector.height(height);
	//当鼠标x方向上为向左拖动 设置left位置
	if (x < startX) {
		rector.css({
			left: (startX - width) + 'px',
		});
	}
	//当鼠标y方向上为向上拖动 设置top位置
	if (y < startY) {
		rector.css({
			top: (startY - height) + 'px',
		});
	}
}

/**
 *核心相交算法
 * @param rect1{x1,y1,x2,y2}
 * @param rect2 {x1,y1,x2,y2}
 */
function isCross(rect1, rect2) {
	var xNotCross = true;//x方向上不重合
	var yNotCross = true;//y方向上不重合
	xNotCross = ((rect1.x1 > rect2.x2) || (rect2.x1 > rect1.x2));
	yNotCross = ((rect1.y1 > rect2.y2) || (rect2.y1 > rect1.y2));
	return !(xNotCross || yNotCross);
}
/**
 *获取元素的矩形的起始点坐标与其对角点坐标
 * @param $el
 * @return {x1,y1,x2,y2}
 */
function getRect($el) {
	var x1 = $el.offset().left;
	var y1 = $el.offset().top;
	var x2 = x1 + $el.outerWidth();
	var y2 = y1 + $el.outerHeight();
	return { x1, x2, y1, y2 };
}

//框选处理 如果元素与选择框相交则设置样式
function handleRectSelection() {
	//idList = []
	var selectionReact = getRect(rector);
	$('.pa').each(function () {
		var rect = getRect($(this));
		var id = $(this).attr("id")
		if (isCross(selectionReact, rect)) {
			if (idList.indexOf(id) == -1) {
				idList.push(id);
			}
			//$("#"+id+"-heading").css({"border":"1px solid"})
			//console.log(parseInt($("#"+id+"-heading").css('borderTopWidth')))
			$("#" + id).addClass("ui-selected")
		}
		// else{
		// 	$("#"+id+"-heading").css({"border":"none"})
		// 	//console.log(parseInt($("#"+id+"-heading").css('borderTopWidth')))
		// }
	});
}

//判断鼠标触发的是点击事件还是移动事件
var key = false;//设置一个标志 false为点击事件 true为鼠标移动事件
var firstTime = 0;
var lastTime = 0;
$('.div_right').bind({
	click: function () {
		var removeTemp = [];
		removeTemp.push({
			//构件ID
			gjId: "",
			//构件模板ID
			tmpId: "",
			//状态
			state: 5
		});
		//console.log(removeTemp);
		handleMessageToParent("returnFormJson", removeTemp);
		if (key) {
			key = false;
			for (var i = 0; i < idList.length; i++) {
				//$("#"+idList[i]+"-heading").css({"box-shadow":"none"})
				$(".pa").removeClass("ui-selected")
			}
			idList = [];
		}
	},
	mousemove: function (e) {
		if (mousedownState != 1 && canvasState) {
			if (e.which === 1) {
				rector.show();
				resizeToPoint(e.pageX, e.pageY);
				handleRectSelection();
			}
		}
	},
	mouseup: function (e) {
		lastTime = new Date().getTime();
		if ((lastTime - firstTime) < 100) {
			key = true
		}
		if (mousedownState != 1 && canvasState) {
			rector.hide();
		}
	},
	mousedown: function (e) {
		firstTime = new Date().getTime();
		mousedownState = 0;
		canvasState = true
		if (mousedownState != 1) {
			for (var i = 0; i < idList.length; i++) {
				//$("#"+idList[i]+"-heading").css({"box-shadow":"none"})
				$(".pa").removeClass("ui-selected")
			}
			if (lastTimeId != "") {
				$('#' + lastTimeId).removeClass("nodeStyle")
				$.each(jsPlumb.getEndpoints(lastTimeId), function (n, v) {
					v.removeClass("nodeStyle")
				});
			}
			$(".point").each(function (idx, elem) {
				var $elem = $(elem);
				$elem.remove()
			})
			idList = []
			startX = e.pageX;
			startY = e.pageY;
			rector.css({
				top: startY + 'px',
				left: startX + 'px'
			});
		}
	}
});

//画布初始比例
// var scale = 1
// $(".div_right").bind('mousewheel DOMMouseScroll', function (event) { //on也可以 bind监听
// 	var wheel = event.originalEvent.wheelDelta;
// 	var detal = event.originalEvent.detail;
// 	if (event.originalEvent.wheelDelta && event.ctrlKey == true) { //判断浏览器IE,谷歌滚轮事件        
// 		//禁止页面触发比例缩放
// 		event.preventDefault()
// 		if (wheel > 0) { //当滑轮向上滚动时 
// 			if (scale < 1.6) {
// 				scale = scale + 0.2
// 				$(".div_right").css({
// 					"-webkit-transform": `scale(${scale})`,
// 					"-moz-transform": `scale(${scale})`,
// 					"-ms-transform": `scale(${scale})`,
// 					"-o-transform": `scale(${scale})`,
// 					"transform": `scale(${scale})`
// 				})
// 				// $('.pa').each(function(index,elem){
// 				// 	var $elem = $(elem);
// 				// 	var pointArray = jsPlumb.getEndpoints($elem.attr('id'))
// 				// 	$.map(pointArray,function(point){
// 				// 		console.log("每个锚点",point)
// 				// 		$(point.canvas).css({
// 				// 			"-webkit-transform": `scale(${scale})`,
// 				// 			"-moz-transform": `scale(${scale})`,
// 				// 			"-ms-transform": `scale(${scale})`,
// 				// 			"-o-transform": `scale(${scale})`,
// 				// 			"transform": `scale(${scale})`
// 				// 		})
// 				// 		jsPlumb.setSuspendDrawing(true)
// 				// 	})
// 				// })
// 				// $.each(jsPlumb.getConnections(), function (idx, connection) {
// 				// 	console.log("每条连线",connection)
// 				// 	$(connection.canvas).css({
// 				// 		"-webkit-transform": `scale(${scale})`,
// 				// 			"-moz-transform": `scale(${scale})`,
// 				// 			"-ms-transform": `scale(${scale})`,
// 				// 			"-o-transform": `scale(${scale})`,
// 				// 			"transform": `scale(${scale})`
// 				// 	})
// 				// })

// 			}

// 		}
// 		if (wheel < 0) { //当滑轮向下滚动时  
// 			console.log(wheel)
// 			if(scale>0.6){
// 				scale = scale - 0.2
// 				$(".div_right").css({
// 					"-webkit-transform": `scale(${scale})`,
// 					"-moz-transform": `scale(${scale})`,
// 					"-ms-transform": `scale(${scale})`,
// 					"-o-transform": `scale(${scale})`,
// 					"transform": `scale(${scale})`
// 				})
// 				jsPlumb.setSuspendDrawing(false, true)
// 			}	
// 		}
// 	} else if (event.originalEvent.detail) {  //Firefox滚轮事件  
// 		if (detal > 0) { //当滑轮向下滚动时  

// 		}
// 		if (detal < 0) { //当滑轮向上滚动时  

// 		}

// 	}

// });

// $("#mySelect").bind("change",function(){
// 	var id = ""
// 	if(idList.length>0){
// 		id = idList[0]
// 	}
// 	var top = $("#"+id).offset().top;
// 	var left = $("#"+id).offset().left;
// 	switch ($("#mySelect").val()) {
// 		case "1":
// 			topAlignment(top,id)
// 			break;
// 		case "2":
// 			leftAlignment(left,id)
// 			break;
// 		case "3":
// 			rightAlignment(left,id)
// 			break;
// 		case "4":
// 			bottomAlignment(top,id);
// 			break;
// 		case "5":
// 			verticalCenter(left,id);
// 			break;
// 		case "6":
// 			levelCenter(top,id);
// 			break;
// 	}
// })

//上对齐
function topAlignment(top, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			//保存移动前的位置
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "top": top });
			//console.log($("#"+idList[i]).offset().top)
			newPosition.push(saveNodePosition(idList[i]))
		}
	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
	//console.log("idList长度",idList)
}
//左对齐
function leftAlignment(left, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			//保存对齐前的位置
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "left": left });
			newPosition.push(saveNodePosition(idList[i]))
		}
	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
}
//右对齐
function rightAlignment(left, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	var width = $("#" + id).width()
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "left": left + (width - $("#" + idList[i]).width()) });
			newPosition.push(saveNodePosition(idList[i]))
		}
	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
}
//下对齐
function bottomAlignment(top, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	var height = $("#" + id).height()
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "top": top + (height - $("#" + idList[i]).height()) });
			newPosition.push(saveNodePosition(idList[i]))
		}
	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
}
//水平居中
function verticalCenter(left, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	var width = $("#" + id).width()
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "left": left + width / 2 - ($("#" + idList[i]).width() / 2) });
			newPosition.push(saveNodePosition(idList[i]))
		}

	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
}
//垂直居中
function levelCenter(top, id) {
	oldPosition = []
	newPosition = []
	dragNode = {}
	var height = $("#" + id).height()
	for (var i = 0; i < idList.length; i++) {
		if (id != idList[i]) {
			oldPosition.push(saveNodePosition(idList[i]))
			$("#" + idList[i]).offset({ "top": top + height / 2 - ($("#" + idList[i]).height() / 2) });
			newPosition.push(saveNodePosition(idList[i]))
		}

	}
	dragNode.oldPosition = oldPosition;
	dragNode.newPosition = newPosition;
	jsPlumb.setSuspendDrawing(false, true);
	chartOperationStack['dragZ'].push(dragNode)
	chartRareOperationStack.push('dragZ')
	limitZ()
}

//清空ctrlY的相应数据
function clean() {
	ctrlYOperationStack = []
	ctrlYStack['add'] = []
	ctrlYStack['delete'] = []
	ctrlYStack['addConnection'] = []
	ctrlYStack['deleteConnection'] = []
	ctrlYStack['dragY'] = []
	ctrlYStack['addPaste'] = []
	ctrlYStack['deletePaste'] = []
}

//限制ctrlZ次数为10
function limitZ() {
	if (chartRareOperationStack.length > 10) {
		//alert("进入判断")
		var state = chartRareOperationStack.shift()
		chartOperationStack[state].splice(0, 1)
	}
}
//限制ctrlY次数为10
function limitY() {
	if (ctrlYOperationStack.length > 10) {
		var state = ctrlYOperationStack.shift()
		ctrlYStack[state].splice(0, 1)
	}
}

//判断节点在移动的过程中是否达到当前画布浏览器窗口的top
function isDropTop() {
	let isDropTop = false
	console.log("浏览器的top", $(".div_right").height())

	for (let i = 0; i < idList.length; i++) {
		let div = document.getElementById(idList[i])
		console.log("节点top", div.offsetTop)
		console.log("节点的height", div.offsetHeight)
		console.log("节点的div", div)
		console.log(idList)
		if ((div.offsetTop + div.offsetHeight) >= $(".div_right").height()) {
			isDropTop = true
		}
	}
	return isDropTop
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

//完备性检查修改锚点样式
function endpointCheck(endpointId,styleColor) {
	//$(".pa").each(function (idx, elem) {
		//var $elem = $(elem);
		var endpoints = jsPlumb.getEndpoints(endpointId.split("*")[2]);
		//循环构件上的锚点
		$.map(endpoints, function (endpoint) {
			if(endpointId == endpoint.getUuid()){
				if(endpointId.split("*")[1] == "input"){
					endpoint.getPaintStyle().strokeStyle = styleColor
				}else{
					endpoint.getPaintStyle().strokeStyle = styleColor
					endpoint.getPaintStyle().fillStyle = styleColor
				}
			}
		});
	//});
	jsPlumb.setSuspendDrawing(false, true);
}

//完备性检查修改连线样式
function connectionCheck(startUuid,endUuid,styleColor) {
	$.each(jsPlumb.getConnections(), function (idx, connection) {
		//console.log(connection.getPaintStyle())
		//connection.addClass("warn")
		if(connection.endpoints[0].getUuid() == startUuid && connection.endpoints[1].getUuid() == endUuid){
			//console.log("68465123654",connection)
			connection.getPaintStyle().lineWidth = "3"
			connection.getPaintStyle().stroke = styleColor
			connection.getPaintStyle().strokeStyle = styleColor
		}	
	});
	jsPlumb.setSuspendDrawing(false, true);
}

//完备性检查
function completeCheckFun(val){
	switch (val.m_checkType) {
		case '0': //检查流程建模xml文件(完备性检查)
			$.each(val.m_checkInfoList,function(index,checkResult){
				//console.log("检查结果",checkResult)
				switch (checkResult.m_checkType) {
					case '0': //修改构件
						if(checkResult.m_modifyState == '0'){ //正常

						}else if(checkResult.m_modifyState == '1'){ //警告
							$('#'+checkResult.m_spbId).addClass("warn")
						}else if(checkResult.m_modifyState == '2'){ //错误
							$('#'+checkResult.m_spbId).addClass("error")
						}
						break;
					case '1': //修改连线
						if(checkResult.m_modifyState == '0'){

						}else if(checkResult.m_modifyState == '1'){ //警告
							connectionCheck(checkResult.m_startPointId,checkResult.m_endPointId,"#F4A460")
						}else if(checkResult.m_modifyState == '2'){ ///错误
							connectionCheck(checkResult.m_startPointId,checkResult.m_endPointId,"#ff0000")
						}
						break;
					case '2': //修改锚点
						if(checkResult.m_modifyState == '0'){ //正常

						}else if(checkResult.m_modifyState == '1'){ //警告
							endpointCheck(endPoint(checkResult),"#F4A460")
						}else if(checkResult.m_modifyState == '2'){ //错误
							endpointCheck(endPoint(checkResult),"#ff0000")
						}
						break;
					default:
						break;
				}
			})
			break;
	
		case '1':
			break;
	}
}

//完备性检查确认锚点id
function endPoint(checkResult){
	let endpointUUid = ""
	// console.log("111111",checkResult)
	// console.log(endpointMap)
	for (let [k, v] of endpointMap) {
		if(k.split("*")[2] == checkResult.m_spbId && k.split("*")[1] == "input" && checkResult.m_paramType =="输入" && v.variableName == checkResult.m_paramName){
			endpointUUid = k
		}else if(k.split("*")[2] == checkResult.m_spbId && k.split("*")[1] == "output" && checkResult.m_paramType =="输出" && v.variableName == checkResult.m_paramName){
			endpointUUid = k
		}
	}
	//console.log("endpointUUid",endpointUUid)
	return endpointUUid
}

//还原完备性检查
function removeStyle(){
	$(".pa").removeClass("error")
	$(".pa").removeClass("warn")
	$(".pa").each(function (idx, elem) {
		var $elem = $(elem);
		//i用于定位使用什么数据
		var i = $elem.find("div").find("div")[0].dataset.id;
		var endpoints = jsPlumb.getEndpoints($elem.attr('id'));
		$.map(endpoints, function (endpoint) {
			//console.log(endpoint)
			if(endpoint.anchor.x == "0"){
				endpoint.getPaintStyle().strokeStyle = "#4ECB74"
			}else{
				endpoint.getPaintStyle().strokeStyle = "#4ECB74"
				endpoint.getPaintStyle().fillStyle = "#4ECB74"
			}
		})
	})

	$.each(jsPlumb.getConnections(), function (idx, connection) {
		connection.getPaintStyle().lineWidth = "2"
		connection.getPaintStyle().stroke = "#436D9F"
		connection.getPaintStyle().strokeStyle = "#436D9F"		
	});


	jsPlumb.setSuspendDrawing(false, true);
}

//})();
