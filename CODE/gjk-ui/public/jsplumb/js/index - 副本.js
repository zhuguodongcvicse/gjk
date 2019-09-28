window.addEventListener("message", this.handleMessageFromParent) // 子接收方式二参数
var iframeData; // 子接收方式一参数
//页面加载所需id
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
// 子向父传参
function handleMessageToParent(cmd,gjIdAndTemId) {
	window.parent.postMessage({
		cmd: cmd,//'returnFormJson',
		params: gjIdAndTemId
	},"*")
};
//子向父传递保存参数
function handleMessage(connects,type) {
	window.parent.postMessage({
		cmd: type,
		params: connects
	},"*");
}
//子向父传参数
function handleConnection(connData) {
	window.parent.postMessage({
		cmd: 'returnConnection',
		params: connData
	},"*");
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
			// console.log("子接收父参数", iframeData)
	 		console.log("dat数据",dat);
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
				"saveArrow":saveArrow,
				"saveflowChartJson" :saveflowChartJson
						};
			console.log("保存后数据",saveData);
			handleMessage(saveData,'returnSave');
			break
		case 'clickCompLoading':
			console.log("数据",JSON.stringify(data.params));
			loadJson(data.params);
			loadAddPointParam = data.params.addPointParam
			loadState = true;
			break
			
	}
};

//(function () {
var area = 'drop-bg'
var areaId = '#' + area
jsPlumb.ready(load);
//构件模板id
var gjTemplateId;

// 放入拖动节点
function dropNode(template, position) {
	//console.log(position);
	//position.left -= $('#side-buttons').outerWidth()
	position.id = uuid.v1()
	//position.generateId = uuid.v1
	gjTemplateId = position.id;
	var html = renderHtml(template, position)
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
function mousePosition(ev){ 
    ev = ev || window.event; 
    if(ev.pageX || ev.pageY){ 
        return {x:ev.pageX, y:ev.pageY}; 
    } 
    return { 
        x:ev.clientX + document.body.scrollLeft - document.body.clientLeft, 
        y:ev.clientY + document.body.scrollTop - document.body.clientTop 
    }; 
}


// 设置入口点
function setEnterPoint(id, gj) {
	var config = getBaseNodeConfig()

	config.isSource = false
	config.maxConnections = 1

	var len = dat[gj].inputList.length;
	var anchorNumber = 1 / len;
	var differenceValue = anchorNumber / 2;
	var x = differenceValue;
	var inPoint;
	for (var i = 0; i < len; i++) {
		inPoint = jsPlumb.addEndpoint(id, {
			anchors: [0, differenceValue, 0, -1],
			uuid: i + '*input*' + id,
			deleteEndpointsOnEmpty: true
		}, config);
		inPoint.bind('mouseover', function (endpoint, originalEvent) {
			addDiv(endpoint);
			var mouse = mousePosition();
			$('.point').css("position", "absolute");
			$('.point').css("left", mouse.x - 210);
			$('.point').css("top", mouse.y +5);
			//$('.point').css("border", "1px solid red");
		});
		inPoint.bind('mouseout', function (endpoint, originalEvent) {
			$('.point').remove();
		});
		differenceValue += anchorNumber;
	}
}
//鼠标悬浮锚点显示div
function addDiv(endpoint) {
	var templateid = endpoint.elementId;
	var templateUuid = endpoint.getUuid();
	//console.log(templateUuid);
	var str = templateUuid.split('*');
	var tempUuid = str[0];
	var strTemp = str[1];
	var temp = strTemp.substr(0, 5);
	// console.log(tempUuid);
	// console.log(temp);
	var b = $('#' + templateid + '-heading')[0].dataset.id;
	var str = '';
	if (temp != 'input') {
		str += "<div class = 'point'>" +
			"<p>参数名称：" + dat[b].outputList[tempUuid].variableName + "</p>" +
			"<p>参数类型：" + dat[b].outputList[tempUuid].dataTypeName + "</p>" +
			"<p>参数长度：" + dat[b].outputList[tempUuid].lengthName + "</p>" +
			"<p>结构体：" + dat[b].outputList[tempUuid].variableStructType + "</p>" +
			"</div>";
	} else {
		str += "<div class = 'point'>" +
			"<h6>参数名称：" + dat[b].inputList[tempUuid].variableName + "</h6>" +
			"<h6>参数类型：" + dat[b].inputList[tempUuid].dataTypeName + "</h6>" +
			"<h6>参数长度：" + dat[b].inputList[tempUuid].lengthName + "</h6>" +
			"<h6>结构体：" + dat[b].inputList[tempUuid].variableStructType + "</h6>" +
			"</div>";
	}
	$('#drop-bg').append(str);
}
// 设置出口点
function setExitPoint(id, gj) {
	var config = getBaseNodeConfig()

	config.isTarget = false
	config.maxConnections = -1

	var len = dat[gj].outputList.length;
	var anchorNumber = 1 / len;
	var differenceValue = anchorNumber / 2;
	var x = differenceValue;
	var outPoint
	for (var i = 0; i < len; i++) {
		outPoint = jsPlumb.addEndpoint(id, {
			anchors: [1, differenceValue, 0, 1],
			uuid: i + '*output*' + id,
			deleteEndpointsOnEmpty: true
		}, config);
		//端点绑定鼠标移入事件
		outPoint.bind('mouseover', function (endpoint, originalEvent) {
			addDiv(endpoint);
			var mouse = mousePosition();
			$('.point').css("position", "absolute");
			$('.point').css("left", mouse.x -90);
			$('.point').css("top", mouse.y +5);
		});
		//端点绑定鼠标移出事件
		outPoint.bind('mouseout', function (endpoint, originalEvent) {
			$('.point').remove();
		});
		differenceValue += anchorNumber;

	}

}

// 渲染html
function renderHtml(type, position) {
	return Mustache.render($('#' + type).html(), position)
}

// 让元素可拖动
function addDraggable(id) {
	jsPlumb.draggable(id, {
		containment: 'parent'
	})
}

var int;
//启动定时器
function load() {
	int = self.setInterval(checkDat, 1000);
	//a = 555;

}
function checkDat() {
	//console.log("进入定时器");
	if (dat.length > 0) {
		main();
		//$("#gjk").load();	
	}
}

//循环添加构件并生成相应模板
function appendDiv() {
	var str = "";
	var Template = "";
	for (var i = 0; i < dat.length; i++) {
		str += "<a class='btn btn-success btn-controler'  data-template='tpl-menu" + i + "' role='button'>" + dat[i].compName + "</a></br><br>";
		Template += "<script id='tpl-menu" + i + "' type='text/html'>" +
			"<div class='pa' id='{{id}}' style='top:{{top}}px;left:{{left}}px'>" +
			"<span class='delete-node pull-right' data-type='deleteNode' data-id='{{id}}' id='del" + i + "'>X</span>" +
			"<div class='responsive'>" +
			"<div id='{{id}}-heading' data-id='"+i+"'>" +
			//"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>构件001</div></div>" +
			//"<div style='text-align:center;height:82px;width:160px;border:6px dashed #2727E0;border-radius:5px;background-color: #16F2DC;display: block;'><img src='./gjk/image/u6.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>构件002</div></div>"
			dat[i].compImg+

			//"<div style='text-align:center;height:80px;width:160px;border:1px none ;border-radius:6px;background-color: #BB2D2D;display: block;'><img src='./img/u" + i + ".png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + i + "'>显示名</div></div>"+
			//"<img src='./img/u" + i + ".png' width='100' height='50'>" +
			//"<div class='desc' id='" + i + "'>" + dat[i].compName + "</div>" +
			"</div>"+
			"</div>" +
			"</div>" +
			"</div></script>";
	}
	$('#gjk').append(str);
	$('body').append(Template);
}
//入口方法
function main() {
	window.clearInterval(int);
	appendDiv();
	$('.btn-controler').draggable({
		helper: 'clone',
		scope: 'ss'
	})

	$(areaId).droppable({
		scope: 'ss',
		drop: function (event, ui) {
			dropNode(ui.draggable[0].dataset.template, ui.position)
			//$('#propertybar').show();
		
			var gj = getGj(ui.draggable[0].dataset.template);
			gjid = dat[gj].id;
			var a = gjTemplateId;
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
			handleMessageToParent("returnFormJson",gjidAndTemid);
			//添加画布构件点击事件
			$('#' + a).bind('click', function () {
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
				handleMessageToParent("returnFormJson",gjidAndTemid);
				//handleMessageToParent();
			});
			//删除节点
			$(".delete-node").off("click").on("click", function () { });
			$(".delete-node").bind('click', function (event) {
				event.stopPropagation();
				eventHandler(event.target.dataset);
			});
			// $('#' + a).mousedown(function(e){
			// 	if(3==e.which){
			// 		alert(111111111);
			// 	}
			// })
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

	//鼠标右击
	jsPlumb.bind('contextmenu',function(component,originalEvent){
		alert(111);
	});
}

//保存
$('#save').on('click', function () {
	handleMessageSave(save());
});

//加载
$('#load').on('click', function () {
	//state = true;
	loadJson();
});

function addPointDiv(proPream){
	var str = "";
		str += "<div class = 'point'>" +
			"<p>参数名称：" + proPream.data.variableName + "</p>" +
			"<p>参数类型：" + proPream.data.dataTypeName + "</p>" +
			"<p>参数长度：" + proPream.data.lengthName + "</p>" +
			"<p>结构体：" + proPream.data.variableStructType + "</p>" +
			"</div>";
	$('#drop-bg').append(str);
}


//动态增加删除锚点
function updatePoint(proPream) {
	console.log("进入增加删除锚点",proPream);
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
		//console.log("增加的参数",addPointParam);
		//console.log(proPream.inOrOut);
		var config = getBaseNodeConfig()
		if (proPream.inOrOut == 'input') {
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
			var addInPoint = jsPlumb.addEndpoint(proPream.compId, {
				anchors: [0, differenceValue, 0, -1],
				uuid:  proPream.uid,
				deleteEndpointsOnEmpty: true
			}, config);
			addInPoint.bind('mouseover', function (endpoint, originalEvent) {
				addPointDiv(proPream);
				var mouse = mousePosition();
				$('.point').css("position", "absolute");
				$('.point').css("left", mouse.x - 210);
				$('.point').css("top", mouse.y +5);
				//$('.point').css("border", "1px solid red");
			});

			addInPoint.bind('mouseout',function(){
				$('.point').remove();
			});
		} else {
			var len = outputUuidList.length + 1;
			var anchorNumber = 1 / len;
			var differenceValue = anchorNumber / 2;
			$.each(endpoints, function (n, val) {
				if (val.anchor.x == 1) {
					val.anchor.y = differenceValue;
					differenceValue += anchorNumber
				}
			});
			config.isSource = true
			config.maxConnections = -1
			var addOutPoint = jsPlumb.addEndpoint(proPream.compId, {
				anchors: [1, differenceValue, 0, -1],
				uuid: proPream.uid,
				deleteEndpointsOnEmpty: true
			}, config);
			addOutPoint.bind('mouseover', function (endpoint, originalEvent) {
				addPointDiv(proPream);
				var mouse = mousePosition();
				$('.point').css("position", "absolute");
				$('.point').css("left", mouse.x -90);
				$('.point').css("top", mouse.y + 5);	
			});
			addOutPoint.bind('mouseout',function(){
				$('.point').remove();
			});
		}
		jsPlumb.connect({ uuids: [endpoints[0].getUuid(), endpoints[0].getUuid()] })
		var conn = jsPlumb.getConnections({
			source: proPream.compId,
			target: proPream.compId
		});
		if (conn[0]) {
			jsPlumb.detach(conn[0]);
		}

	} else {
		var del = false;
		$.each(endpoints, function (n, val) {
			if (proPream.uid == val.getUuid()) {
				del = true;
				jsPlumb.deleteEndpoint(endpoints[n]);
			}
		});
		if(del){
			var points = jsPlumb.getEndpoints(proPream.compId);
			if (proPream.inOrOut == 'input') {
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
			jsPlumb.connect({ uuids: [points[0].getUuid(), points[0].getUuid()] })
			var conn = jsPlumb.getConnections({
				source: proPream.compId,
				target: proPream.compId
			});
			if (conn[0]) {
				jsPlumb.detach(conn[0]);
			}
		}
	}
}

//删除锚点
$('#delete').on('click', function () {
	var endpoints = jsPlumb.getEndpoints(gjTemplateId);
	jsPlumb.deleteEndpoint(endpoints[0]);
	//$.map(endpoints, function (endpoint) {
	endpoints[1].anchor.x = 0;
	endpoints[1].anchor.y = 0.5;
	jsPlumb.connect({ uuids: [endpoints[3].getUuid(), endpoints[3].getUuid()] })
	//jsPlumb.connect({ uuids: [endpoints[3].getUuid(), endpoints[2].getUuid()] })
	//});
	// var points = jsPlumb.getEndpoints(gjTemplateId);
	// console.log(points);
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
		handleMessageToParent("returnFormJson",gjidAndTemid);
	//	handleMessageToParent();
	}
}
// 删除一个节点以及
function emptyNode(id) {
	jsPlumb.remove(id)
}
//双击删除连线
jsPlumb.bind("dblclick", function (conn, originalEvent) {
	if (confirm("确定删除吗?")) {
		jsPlumb.detach(conn);
	}
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

//连线触发事件
jsPlumb.bind("connection", function (info) {
	console.log(info);
	var source = info.sourceId;
	var target = info.targetId;
	if(source != target){
		console.log("进入");
	var sourceUid =  info.connection.endpoints[0].getUuid();
	console.log("源锚点UUid",sourceUid);
	var targetUid =  info.connection.endpoints[1].getUuid();
	if(loadState){
		addPointParam = addPointParam.concat(loadAddPointParam)
		console.log("合并增加数据",addPointParam);
		loadState = false;
	}
	var sourceType = "";
	var targetType = "";
	//判断源锚点是否为新增
	var sourcePointState = false;
	//新增源锚点数据下标
	var sourceIndex = 0;
	if(addPointParam.length > 0){
		console.log("进入源节点循环");
		$.each(addPointParam,function(index1,addPoinr){
			if(sourceUid == addPoinr.uid){
				sourcePointState = true;
				sourceIndex = index1;
			}
		});
	}
	if(sourcePointState){
		sourceType = addPointParam[sourceIndex].data.dataTypeName;
		console.log("源节点属性if",sourceType);
	}else{
	//获取源节点数据下标
	var sourceGj = $('#' + source + '-heading')[0].dataset.id;
	sourceType =  dat[sourceGj].outputList[sourceUid.split("*")[0]].dataTypeName;
	console.log("源节点属性else",sourceType);
	}

	//判断目标锚点是否为新增
	targetPointState = false;
	//新增目标节点数据下标
	var targetIndex = 0;
	if(addPointParam.length > 0){
		$.each(addPointParam,function(index2,addPoinr){
			console.log("进入目标节点循环");
			if(targetUid == addPoinr.uid){
				targetPointState = true;
				targetIndex = index2;
			}
		});
	}
	if(targetPointState){
		targetType = addPointParam[targetIndex].data.dataTypeName
		console.log("目标节点属性if",targetType);
	}else{
		//获取目标节点数据下标
		var targetGj = $('#' + target + '-heading')[0].dataset.id;
		targetType = dat[targetGj].inputList[targetUid.split("*")[0]].dataTypeName;
		console.log("目标节点属性else",targetType);
	}
	console.log("源节点类型",sourceType);
	console.log("目标节点类型",targetType);
	if(sourceType != targetType){
		jsPlumb.detach(info);
		alert("节点类型不匹配");
	}
	}
	
});
//删除连线触发事件
// jsPlumb.bind("connectionDetached", function (conn, originalEvent) {
// 	var operating = "delete"
// 	//anchorParameter(conn, operating);
// });


//Ctrl+S
document.onkeydown = function () {
	if (event.ctrlKey == true && event.keyCode == 83) {//Ctrl+S
		event.returnvalue = false;
		event.preventDefault();
		//saveFlowchart();
		//loadJson();
		//$(".fz").show();
		alert(1111);
	}else if(event.keyCode == 46){
		alert(2222);
	}
}

//保存arrow集合
function save() {
	var connected = jsPlumb.getConnections();
	var connect = [];
	var conn = [];
	var connects = {};
	var con = [];
	var map = new Map();
	if(loadState){
		addPointParam = addPointParam.concat(loadAddPointParam)
		console.log("合并增加数据",addPointParam);
		loadState = false;
	}
	$.each(connected, function (id, connection) {
		//console.log(id);
		//获取画布源节点id
		var source = connection.sourceId;
		//获取源构件中数据id
		var sourceGj = $('#' + source + '-heading')[0].dataset.id;
		console.log("sourceGj",sourceGj);
		//var dataSouGjId = dat[sourceGj].id
		//获取画布源锚点uuid
		var sourceuid = connection.endpoints[0].getUuid();

		//设置当前连接线源锚点是否为新增
		var addsourcePointState = false;
		//设置新增源锚点在addPointParam集合中的下标
		var addSPointParamindex = 0;
		if(addPointParam.length > 0){
			$.each(addPointParam,function(index1,addPoinr){
				if(sourceuid == addPoinr.uid){
					addsourcePointState = true;
					addSPointParamindex = index1;
				}
			});
		}
		var sourDataTypeName = "";
		var sourVariableName = "";
		var sourLength = "";
		if(addsourcePointState){
			sourDataTypeName = addPointParam[addSPointParamindex].data.dataTypeName;
			sourVariableName =  addPointParam[addSPointParamindex].data.variableName;
			sourLength = addPointParam[addSPointParamindex].data.lengthName;
		}else{
			var str = sourceuid.split('*');
			//var s1 =  sourceuid.substr(0,4);
			var sourceUuid = str[0];
			//获取数据输出参数
			//var sourcePrame = dat[sourceGj].outputList[sourceUuid].dataTypeName;
			sourDataTypeName =  dat[sourceGj].outputList[sourceUuid].dataTypeName;
			sourVariableName =  dat[sourceGj].outputList[sourceUuid].variableName;
			sourLength = dat[sourceGj].outputList[sourceUuid].lengthName;
		}
		var sourCompName = dat[sourceGj].compName;
		var sourcePrame = sourDataTypeName +" "+ sourVariableName+"_"+sourCompName

		//获取画布目标节点id
		var target = connection.targetId;
		//获取目标构件中数据id
		var targetGj = $('#' + target + '-heading')[0].dataset.id;
		//var dataTarGjId = dat[targetGj].id;
		//获取画布目标锚点uuid
		var targetid = connection.endpoints[1].getUuid();
		//设置当前连线目标节点是否为新增
		var addTargetPointParam = false;
		////设置新增目标锚点在addPointParam集合中的下标
		var addTPointParamindex = 0;
		if(addPointParam.length > 0){
			$.each(addPointParam,function(index1,addPoinr){
				if(targetid == addPoinr.uid){
					addTargetPointParam = true;
					addTPointParamindex = index1;
				}
			});
		}
		var tarDataTypeName = "";
		var tarVariableName = "";
		if(addTargetPointParam){
			tarDataTypeName = addPointParam[addTPointParamindex].data.dataTypeName;
			tarVariableName =  addPointParam[addTPointParamindex].data.variableName;
		}else{
			var str = targetid.split('*');
			var targetUuid = str[0];
			tarDataTypeName = dat[targetGj].inputList[targetUuid].dataTypeName;
			tarVariableName = dat[targetGj].inputList[targetUuid].variableName;
		}

		//获取数据输入参数
		var tarCompName = dat[targetGj].compName;
		var targetPrame = tarDataTypeName +" "+ tarVariableName+"_"+tarCompName
		
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
			id: id
		};
		conn.push(con3);


		if (id == 0 || map.get(id) != map.get(id - 1)) {
			connect.push(con2);
			con.push(con1);
		}

	});
	connects = { arrow: con };
	console.log("arrow串", JSON.stringify(connects));
	return JSON.stringify(connects);
}

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
			outPointsData: dat[i].outputList,
			inPointsData: dat[i].inputList,
			compImg:dat[i].compImg,
			compId:dat[i].compId
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
	//flowChart.outPointsData = outPointsData;
	//flowChart.inPointsData = inPointsData;
	var flowChartJson = JSON.stringify(flowChart);
	console.log("流程图数据",flowChartJson);
	return flowChartJson;
}

//添加锚点信息(还原)
function addTemDiv(endpoint, PointsData) {
	var templateUuid = endpoint.getUuid().split('*');
	var tempUuid = templateUuid[0];
	var str = '';
		str += "<div class = 'point'>" +
			"<p>参数名称：" + PointsData[tempUuid].variableName + "</p>" +
			"<p>参数类型：" + PointsData[tempUuid].dataTypeName + "</p>" +
			"<p>参数长度：" + PointsData[tempUuid].lengthName + "</p>" +
			"<p>结构体：" + PointsData[tempUuid].variableStructType + "</p>" +
			"</div>";
	$('#drop-bg').append(str);
}
// function addTemplate(){

// }
//解析json字符串还原流程图
function loadJson(loadJson) {
	//var loadJson = {"nodes":[{"blockId":"2521ced0-8455-11e9-8180-6b92e201c8cd","positionX":120,"positionY":72,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"25cbca70-8455-11e9-8180-6b92e201c8cd","positionX":536,"positionY":69,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"268f1a70-8455-11e9-8180-6b92e201c8cd","positionX":486,"positionY":245,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"},{"blockId":"278672c0-8455-11e9-8180-6b92e201c8cd","positionX":188,"positionY":243,"imageId":"0","outPointsData":[{"variableName":"rr","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"1","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"inPointsData":[{"variableName":"asf","variableStructType":"","dataTypeName":"int*","orderNumName":"","lengthName":"4","categoryName":"DATA","voluationName":"","selectionVariableName":"","parameterNumName":{}}],"compImg":"<div style='text-align:center;height:80px;width:160px;border:4px solid ;border-radius:1px;background-color: #E7DC08;display: block;'><img src='./gjk/image/u7.png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='\" + i + \"'>构件001</div></div>"}],"connections":[{"connectionId":"con_28","pageSourceId":"2521ced0-8455-11e9-8180-6b92e201c8cd","pageTargetId":"25cbca70-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*25cbca70-8455-11e9-8180-6b92e201c8cd"},{"connectionId":"con_33","pageSourceId":"25cbca70-8455-11e9-8180-6b92e201c8cd","pageTargetId":"268f1a70-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*25cbca70-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*268f1a70-8455-11e9-8180-6b92e201c8cd"},{"connectionId":"con_38","pageSourceId":"2521ced0-8455-11e9-8180-6b92e201c8cd","pageTargetId":"278672c0-8455-11e9-8180-6b92e201c8cd","anchors":[[1,0.5,0,1,0,0],[0,0.5,0,-1,0,0]],"sourceUuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","targetUuid":"0*input*278672c0-8455-11e9-8180-6b92e201c8cd"}],"jsonendpoints":[{"anchorX":0,"anchorY":0.5,"uuid":"0*input*2521ced0-8455-11e9-8180-6b92e201c8cd","id":"2521ced0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*2521ced0-8455-11e9-8180-6b92e201c8cd","id":"2521ced0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*25cbca70-8455-11e9-8180-6b92e201c8cd","id":"25cbca70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*25cbca70-8455-11e9-8180-6b92e201c8cd","id":"25cbca70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*268f1a70-8455-11e9-8180-6b92e201c8cd","id":"268f1a70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*268f1a70-8455-11e9-8180-6b92e201c8cd","id":"268f1a70-8455-11e9-8180-6b92e201c8cd"},{"anchorX":0,"anchorY":0.5,"uuid":"0*input*278672c0-8455-11e9-8180-6b92e201c8cd","id":"278672c0-8455-11e9-8180-6b92e201c8cd"},{"anchorX":1,"anchorY":0.5,"uuid":"0*output*278672c0-8455-11e9-8180-6b92e201c8cd","id":"278672c0-8455-11e9-8180-6b92e201c8cd"}]}
	var nodes = loadJson.nodes;
	var endpoints = loadJson.jsonendpoints;
	var addPoinrData = loadJson.addPointParam;
	//console.log(nodes);
	$.each(nodes, function (index, elem) {
		var Template1 = ""
		Template1 +=
		"<div class='pa' id='" + elem.blockId + "' style='top:" + elem.positionY + "px;left:" + elem.positionX + "px'>" +
		"<span class='delete-node pull-right' data-type='deleteNode' data-id='" + elem.blockId + "'>X</span>" +
		"<div class='responsive'>" +
		"<div id='" + elem.blockId + "-heading' data-id='" + elem.imageId + "'>" +
		elem.compImg+
		//"<div style='text-align:center;height:80px;width:160px;border:1px none ;border-radius:6px;background-color: #BB2D2D;display: block;'><img src='./img/u" + elem.imageId + ".png' style='vertical-align: middle;width: 150px; height:75px;border-radius:5px;'><i style='display: inline-block;height: 100%;vertical-align: middle;'></i><div class='desc' id='" + elem.imageId + "'>" + elem.text + "</div></div>"+
		"</div>"+
		"</div>" +
		"</div>" +
		"</div>";
		$('#drop-bg').append(Template1);
		
		$('#'+elem.blockId).bind('click',function(event){
			var loadendpoints = jsPlumb.getEndpoints(elem.blockId);
			var uuidList = new Array();
			$.each(loadendpoints, function (n, val) {
				uuidList.push(val.getUuid());
			});
			console.log("uuid集合",uuidList);
			var TempData = [];
			TempData.push({
				//构件ID
				gjId: elem.compId,
				//构件模板ID
				tmpId: elem.blockId,
				//状态
				state: 2,
				//锚点uuid集合
				uuidList : uuidList
			});
			handleMessageToParent("returnFormJson",TempData);
		});
		jsPlumb.draggable(elem.blockId, {
			containment: 'parent'
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
			handleMessageToParent("returnFormJson",removeTemp);
		});
		//设置锚点是否为新增
		var addinPointState = false;
		var addoutPointState = false;
		//设置当前锚点显示参数下标
		var addPoinrIndex = 0;
		$.each(endpoints, function (index, endpoint) {
			//console.log("idsss"+endpoint.id);
			if (endpoint.id == elem.blockId) {
				var config = getBaseNodeConfig()
				if (endpoint.anchorX == 0) {
					config.isSource = false
					config.maxConnections = 1
					var inPoint = jsPlumb.addEndpoint(elem.blockId, {
						anchors: [0, endpoint.anchorY, 0, -1],
						uuid: endpoint.uuid,
					}, config)
					inPoint.bind('mouseover', function (endpoint, originalEvent) {
						$.each(addPoinrData,function(index1,addPoinr){
							if(endpoint.getUuid() == addPoinr.uid){
								addinPointState = true;
								addPoinrIndex = index1;
							}
						});
						if(addinPointState){
							addPointDiv(addPoinrData[addPoinrIndex]);
						}else{
							addTemDiv(endpoint, elem.inPointsData);
						}
						var mouse = mousePosition();
						$('.point').css("position", "absolute");
						$('.point').css("left", mouse.x - 210);
						$('.point').css("top", mouse.y +5);
					});
					inPoint.bind('mouseout', function (endpoint, originalEvent) {
						$('.point').remove();
					});
				} else {
					config.isTarget = true
					config.maxConnections = -1
					var outPoint = jsPlumb.addEndpoint(elem.blockId, {
						anchors: [1, endpoint.anchorY, 0, 1],
						uuid: endpoint.uuid,
					}, config)
					outPoint.bind('mouseover', function (endpoint, originalEvent) {
						$.each(addPoinrData,function(index1,addPoinr){
							if(endpoint.getUuid() == addPoinr.uid){
								addoutPointState = true;
								addPoinrIndex = index1;
							}
						});
						if(addoutPointState){
							addPointDiv(addPoinrData[addPoinrIndex]);
						}else{
							addTemDiv(endpoint, elem.outPointsData);
						}
						var mouse = mousePosition();
						$('.point').css("position", "absolute");
						$('.point').css("left", mouse.x - 90);
						$('.point').css("top", mouse.y + 5);
					});
					outPoint.bind('mouseout', function (endpoint, originalEvent) {
						$('.point').remove();
					});
				}
			}

		});
	});
	var connections = loadJson.connections;
	$.each(connections, function (index, elem) {
		jsPlumb.connect({ uuids: [elem.sourceUuid, elem.targetUuid] })
	});
}

// })();
