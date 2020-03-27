window.addEventListener("message", this.handleMessageFromParent)

//构件列表数据
var dat = [];

//锚点id
var portId = ""
//流程图数据
var loadData;
//构件所属部件map
var componentMap = new Map()
//开始仿真
var flag = true;
//子向父传参数
function handleMessageToParent(cmd, gjIdAndTemId) {
  window.parent.postMessage({
    cmd: cmd,//'returnFormJson',
    params: gjIdAndTemId
  }, "*")
};
function handleMessage(connects, type) {
  window.parent.postMessage({
    cmd: type,
    params: connects
  }, "*");
}
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
      connectionData = iframeData.connectionData
      console.log("构件列表数据", dat)
      break
    // case 'getCompDtosData':
    // 	//接收属性栏修改后的数据
    // 	proData = data
    // 	//console.log(JSON.stringify(proData.params));
    // 	updatePoint(proData.params);
    // 	break
    case 'clickCompSave':
      save();
      break
    case 'clickCompLoading':
      loadData = data.params
      console.log("加载流程图数据", data.params)
      break
    case 'import':
      loadData = data.params
      reduction();
      break
    case 'sendCompFzData':
      componentMap.set(data.params.compId, data.params.compData)
      break
    // case 'alignment':
    // 	var id = ""
    // 	if (idList.length > 0) {
    // 		id = idList[0]
    // 	}
    // 	var top = $("#" + id).offset().top;
    // 	var left = $("#" + id).offset().left;
    // 	switch (data.params) {
    // 		case "1":
    // 			topAlignment(top, id)
    // 			break;
    // 		case "2":
    // 			leftAlignment(left, id)
    // 			break;
    // 		case "3":
    // 			rightAlignment(left, id)
    // 			break;
    // 		case "4":
    // 			bottomAlignment(top, id);
    // 			break;
    // 		case "5":
    // 			verticalCenter(left, id);

    // 			break;
    // 		case "6":
    // 			levelCenter(top, id);
    // 			break;
    // 	}
    // 	break;
    case 'cleanCanvas':
      cleanCanvas();
      break;
    // case 'bottonCheckComp':
    // 	if (!data.params) {
    // 		$("#accordion").show()
    // 		$("#gjk").hide()
    // 		$('#update').html("")
    // 		$('#notUpdate').html("")
    // 		compUpdateState = data.compUpdateState;
    // 		appendUpdateDiv()
    // 	} else {
    // 		$("#accordion").hide()
    // 		$("#gjk").show()
    // 	}
    // 	break;
    // case 'completeCheck':
    // 	if(!data.state){
    // 		completeCheckFun(data.params)
    // 	}else{
    // 		removeStyle()
    // 	}
    // 	break;
    // case 'startSimulation':
    // 	getSimulationData();
    // 	break;
    // case 'endSimulation':
    // 	deleteFzPicture();
    // 	break;
    // case 'returnRemoveComp':
    // 	$("#" + data.params).remove()
    // 	break;
  }
};



var int;
function load() {
  int = self.setInterval(checkDat, 1000);

}
function checkDat() {
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
    init();
    //$("#gjk").load();	
  }
}

function init() {
  window.clearInterval(int);
  appendDiv();
  if (window.goSamples) goSamples();  // init for these samples -- you don't need to call this   //初始化
  var $$ = go.GraphObject.make;
  myDiagram =
    $$(go.Diagram, "myDiagramDiv",  //Diagram refers to its DIV HTML element by id   图通过ID引用其DIV HTML元素
      {
        //initialContentAlignment: go.Spot.Center,
        // allowDrop: true,  // must be true to accept drops from the Palette 必须正确才能接受调色板中的墨滴
        // "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below  该DiagramEvent侦听器在下面定义
        //"LinkRelinked": showLinkLabel,
        //  scrollsPageOnFocus: false,
        "undoManager.isEnabled": true,  // enable undo & redo  启用撤消和重做
        initialPosition: new go.Point(0, 0),
      });


  var simulationData = [];

     myDiagram.contextMenu =
      $$(go.Adornment, "Vertical",
          makeButton("开始仿真",
                     //function(e, obj) { e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint); },
                     //function(o) { return o.diagram.commandHandler.canPasteSelection();
                    // }
                     )
      );


  var nodeMenu =  // context menu for each Node
    $$(go.Adornment, "Vertical",
      //  makeButton("Copy",
      //             function(e, obj) { e.diagram.commandHandler.copySelection(); }),
      //  makeButton("Delete",
      //             function(e, obj) { e.diagram.commandHandler.deleteSelection(); }),
      makeButton("开始仿真",
        function (e, obj) {
          flag = false;
          console.log("***************", e)
          console.log("obj***************", obj)
          var nodeOrLinkList = myDiagram.selection;
          nodeOrLinkList.each(function (nodeOrLink) {
            if (nodeOrLink instanceof go.Node) {
              //获取选中节点
              var key = nodeOrLink.data.key;
            } else if (nodeOrLink instanceof go.Link) {

              if (componentMap.get(nodeOrLink.data.from) === componentMap.get(nodeOrLink.data.to)) {
                handleMessageToParent("returnFZInfo", "所选连线中存在俩端构件属于同一组件不可进行仿真");
              } else {
                //获取选中的连线
                var from = nodeOrLink.data.fromPort;
                var to = nodeOrLink.data.toPort;
                //   console.log("from***************",from)
                //   console.log("to***************",to)
                //   console.log("findPortData(from,to)***************",findPortData(from,to))
                //  console.log("_______________",JSON.parse( myDiagram.model.toJson()))
                let linkNodeData = findPortData(from, to);
                let startId = nodeOrLink.data.from;
                let endId = nodeOrLink.data.to;
                let startName = linkNodeData.fromNodeData.variableName;
                let endName = linkNodeData.toNodeData.variableName
                let simulation = startId + ":" + startName + "|" + endId + ":" + endName

                simulationData.push(simulation)
              }



            }
          });
          handleMessageToParent("returnSimulationData", simulationData);

          console.log("传vue数据linkdata", simulationData)
        }),
      makeButton("仿真展示",
        function (e, obj) {
          var nodeOrLinkList = myDiagram.selection;
          nodeOrLinkList.each(function (nodeOrLink) {
            if (nodeOrLink instanceof go.Link) {
              //获取选中的连线
              var from = nodeOrLink.data.fromPort;
              var to = nodeOrLink.data.toPort;
              let linkNodeData = findPortData(from, to);
              console.log("linkNodeData2222222222222222222222", linkNodeData);
              let startId = nodeOrLink.data.from;
              let endId = nodeOrLink.data.to;
              console.log("nodeOrLink.data", nodeOrLink)
              if (componentMap.get(startId) === componentMap.get(endId)) {
                handleMessageToParent("returnFZInfo", "所选连线俩端构件属于同一组件");
              } else if (flag = true) {
                handleMessageToParent("returnFZInfo", "请先点击开始仿真");
              } else if (!flag) {
                let startId = nodeOrLink.data.from;
                let endId = nodeOrLink.data.to;
                var from = nodeOrLink.data.fromPort;
                var to = nodeOrLink.data.toPort;
                let linkNodeData = findPortData(from, to);
                let startName = linkNodeData.fromNodeData.variableName;
                let endName = linkNodeData.toNodeData.variableName;
                let TempData = { startId: startId, endId: endId, startName: startName, endName: endName }
                handleMessageToParent("returnFZ", TempData);
              }
            }

          });







        }),
      //  $$(go.Shape, "LineH", { strokeWidth: 2, height: 1, stretch: go.GraphObject.Horizontal }),
      //  makeButton("Add top port",
      //             function (e, obj) { addPort("top"); }),
      //  makeButton("Add left port",
      //             function (e, obj) { addPort("left"); }),
      //  makeButton("Add right port",
      //             function (e, obj) { addPort("right"); }),
      //  makeButton("Add bottom port",
      //             function (e, obj) { addPort("bottom"); })
    );
  function makeButton(text, action, visiblePredicate) {
    return $$("ContextMenuButton",
      $$(go.TextBlock, text),
      { click: action },
      // don't bother with binding GraphObject.visible if there's no predicate
      visiblePredicate ? new go.Binding("visible", "", function (o, e) { return o.diagram ? visiblePredicate(o, e) : false; }).ofObject() : {});
  }

  myDiagram.nodeTemplate =
    $$(go.Node, "Table",
      {
        locationObjectName: "BODY",
        locationSpot: go.Spot.Center,
        selectionObjectName: "BODY",
      },
      new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),

      // the body
      $$(go.Panel, "Auto",
        {
          row: 1, column: 1, name: "BODY",
          stretch: go.GraphObject.Fill
        },
        $$(go.Picture,
          { margin: 0, width: 110, height: 90, column: 0 },
          // Picture.source参数值与模型数据中的"source"字段绑定
          new go.Binding("source", "source"),
        ),


        //        $$(go.Shape, "Rectangle",
        //          { fill: "#AC193D", stroke: null, strokeWidth: 0,
        //            minSize: new go.Size(56, 56) }),
        //  $$(go.TextBlock,
        //    { margin: 10, textAlign: "center", font: "14px  Segoe UI,sans-serif", stroke: "white", editable: true },
        //    new go.Binding("text", "text").makeTwoWay())
      ),  // end Auto Panel body

      // the Panel holding the right port elements, which are themselves Panels,
      // created for each item in the itemArray, bound to data.rightArray
      $$(go.Panel, "Vertical",
        new go.Binding("itemArray", "rightArray"),
        {
          row: 1, column: 2,
          toolTip:
            $$(go.Adornment, "Auto",
              $$(go.Shape, { fill: "#EFEFCC" }),
              $$(go.TextBlock, { margin: 4, width: 140 },
                new go.Binding("text", "", infoString).ofObject())
            ),
          itemTemplate:
            $$(go.Panel,
              {
                _side: "right",
                fromSpot: go.Spot.Right, toSpot: go.Spot.Right,
                fromLinkable: true, toLinkable: false, cursor: "pointer",
                toMaxLinks: 1,
                mouseHover: function (e, node) {
                  portId = node.Is
                }
              },
              new go.Binding("portId", "portId"),
              $$(go.Shape, "Rectangle",
                {
                  stroke: null, strokeWidth: 0,
                  desiredSize: new go.Size(8, 8),
                  margin: new go.Margin(1, 0)
                },
                new go.Binding("fill", "portColor")),

            )  // end itemTemplate
        }
      ),  // end Vertical Panel

      $$(go.Panel, "Vertical",
        new go.Binding("itemArray", "leftArray"),
        {
          row: 1, column: 0,

          itemTemplate:
            $$(go.Panel,
              {
                _side: "left",  // internal property to make it easier to tell which side it's on
                fromSpot: go.Spot.Left, toSpot: go.Spot.Left,
                fromLinkable: false, toLinkable: true, cursor: "pointer",
                toMaxLinks: 1,
                mouseHover: function (e, node) {
                  portId = node.Is
                }
              },
              new go.Binding("portId", "portId"),
              $$(go.Shape, "Rectangle",
                {
                  stroke: null, strokeWidth: 0,
                  desiredSize: new go.Size(8, 8),
                  margin: new go.Margin(1, 0)
                },
                new go.Binding("fill", "portColor"))
            ),  // end itemTemplate
          toolTip:
            $$(go.Adornment, "Auto",
              $$(go.Shape, { fill: "#EFEFCC" }),
              $$(go.TextBlock, { margin: 4, width: 140, textAlign: 'left', },
                new go.Binding("text", "", infoString).ofObject())
            ),
        }
      ),

    );

  myDiagram.linkTemplate =
    $$(go.Link,
      {
        routing: go.Link.Orthogonal, corner: 5,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        selectable: true,
        contextMenu: nodeMenu
      },
      $$(go.Shape, { stroke: "gray", strokeWidth: 2 }),
      $$(go.Shape, { stroke: "gray", fill: "gray", toArrow: "Standard" })
    );

  myDiagram.model =
    $$(go.GraphLinksModel,
      {
        linkFromPortIdProperty: "fromPort",  // required information:
        linkToPortIdProperty: "toPort",      // identifies data property names
        //  nodeDataArray: loadData.nodeDataArray,
        //  linkDataArray: loadData.linkDataArray
      });

 

  function infoString(obj) {
    var part = obj.part;
    if (part instanceof go.Adornment) part = part.adornedPart;
    var msg = "";
    if (part instanceof go.Link) {
      msg = "";
    } else if (part instanceof go.Node) {
      //console.log("999999999",part.data)
      if (portId.split("*")[1] == "input") {
        for (var value of part.data.leftArray) {
          if (portId == value.portId) {
            msg = part.data.text + ":\n\n" + value.description;
          }
        }
      } else {
        for (var value of part.data.rightArray) {
          if (portId == value.portId) {
            msg = part.data.text + ":\n\n" + value.description;
          }
        }
      }


    }
    return msg;
  }


  $(".avatar-uploader").draggable({
    stack: "#myDiagramDiv",
    //  revert: true,
    //  revertDuration: 0,
    helper: 'clone',
  });

  function takeScreenshot(id) {
    html2canvas(document.getElementById(id)).then(canvas => {
      document.body.appendChild(canvas)
      var imgUrl = canvas.toDataURL("image/png");
      console.log("图片信息", imgUrl)
      return imgUrl
    });
  }

  $("#myDiagramDiv").droppable({
    drop: function (event, ui) {
      //var elt = ui.draggable.first();
      //每个构件的数据
      var dropData = {}
      //构件名称
      var text = "";
      //图片路径
      var path = ""
      //输入锚点集合数据
      var rightArray = []
      //输出锚点集合数据
      var leftArray = []
      //构件名称
      var compName = ""
      //输入参数
      var inputList = []
      //输出参数
      var outputList = []
      var random = ''
      random = Math.ceil(Math.random() * 10000000000000000000).toString().substr(0, 9 || 4)
      random = Date.now() + random
      for (var value of dat) {
        if (ui.draggable.attr("id") == value.id) {
          var uuidList = new Array();
          dropData = value
          compName = value.compName
          // path = takeScreenshot(ui.draggable.attr("id"))
          html2canvas(document.getElementById(ui.draggable.attr("id"))).then(canvas => {
            //document.body.appendChild(canvas)
            path = canvas.toDataURL("image/png");
            inputList = value.inputList.concat()
            outputList = value.outputList.concat()
            for (var i = 0; i < value.inputList.length; i++) {
              var inputData = {}
              let inputStr = ""
              if (value.inputList[i].variableStructType != "") {
                inputStr = "参数名称:" + value.inputList[i].variableName + "\n" + "参数类型:" + value.inputList[i].dataTypeName + "\n" + "参数长度:" + value.inputList[i].lengthName + "\n" + "结构体:" + value.inputList[i].variableStructType
              } else {
                inputStr = "参数名称:" + value.inputList[i].variableName + "\n" + "参数类型:" + value.inputList[i].dataTypeName + "\n" + "参数长度:" + value.inputList[i].lengthName
              }
              inputData.portColor = "#923951"
              inputData.portId = i + '*input*' + random
              inputData.description = inputStr
              inputData.portDat = value.inputList[i]
              leftArray.push(inputData)
              uuidList.push(i + '*input*' + random)
            }
            for (var j = 0; j < value.outputList.length; j++) {
              var outputData = {}
              let outputStr = ""
              if (value.outputList[j].variableStructType != "") {
                outputStr = "参数名称:" + value.outputList[j].variableName + "\n" + "参数类型:" + value.outputList[j].dataTypeName + "\n" + "参数长度:" + value.outputList[j].lengthName + "\n" + "结构体:" + value.outputList[j].variableStructType
              } else {
                outputStr = "参数名称:" + value.outputList[j].variableName + "\n" + "参数类型:" + value.outputList[j].dataTypeName + "\n" + "参数长度:" + value.outputList[j].lengthName
              }
              outputData.portColor = "#923951"
              outputData.portId = j + '*output*' + random
              outputData.description = outputStr
              outputData.portDat = value.outputList[j]
              rightArray.push(outputData)
              uuidList.push(j + '*output*' + random)
            }
            var x = ui.offset.left - $(this).offset().left;
            var y = ui.offset.top - $(this).offset().top;
            var p = new go.Point(x, y);
            var q = myDiagram.transformViewToDoc(p);
            var model = myDiagram.model;
            model.startTransaction("drop");
            model.addNodeData({
              compId: ui.draggable.attr("id"),  //数据库对应构件id
              key: random,
              text: compName,
              loc: go.Point.stringify(q),
              source: path,
              rightArray: rightArray,
              leftArray: leftArray,
              inputList: inputList,
              outputList: outputList
            });
            model.commitTransaction("drop");
            var gjidAndTemid = [];
            gjidAndTemid.push({
              //构件ID
              gjId: ui.draggable.attr("id"),
              //构件模板ID
              tmpId: random,
              //状态
              state: 0,
              //节点所有锚点uuid
              uuidList: uuidList
            });
            handleMessageToParent("returnFormJson", gjidAndTemid);
          });
        }
      }
    }
  });

  //循环添加构件并生成相应模板
  function appendDiv() {
    var str = "";
    for (var i = 0; i < dat.length; i++) {
      // str += "<div id = '" + dat[i].id + "' >" + dat[i].compImg + "</div></br>"
      str += dat[i].compImg
    }
    $('#gjk').append(str);
  }

  //删除节点
  myDiagram.addDiagramListener("SelectionDeleting", function (e) {
    e.subject.each(function (part) {
      console.log("删除一个节点", part.Zd)
      //此处处理part
    })
  })

  //插入了一个新的节点
  myDiagram.addDiagramListener("PartCreated", function (e) {
    e.subject.each(function (part) {
      console.log("插入新节点", part.Zd)
      //此处处理part
    })
  })

  //粘贴到画布
  myDiagram.addDiagramListener("ClipboardPasted", function (e) {
    e.subject.each(function (part) {
      console.log("复制到画布数据", part.Zd)
      console.log("新旧id", part.Zd.key.substr(0, part.Zd.key.length - 1))
      var uuidList = new Array()
      for (let leftPort of part.Zd.leftArray) {
        leftPort.portId = leftPort.portId.split("*")[0] + "*" + leftPort.portId.split("*")[1] + "*" + part.Zd.key
        uuidList.push(leftPort.portId)
      }
      for (let rightPort of part.Zd.rightArray) {
        rightPort.portId = rightPort.portId.split("*")[0] + "*" + rightPort.portId.split("*")[1] + "*" + part.Zd.key
        uuidList.push(rightPort.portId)
      }
      var gjidAndTemid = [];
      gjidAndTemid.push({
        //构件ID
        gjId: part.Zd.compId,
        //构件模板新ID
        newTmpId: part.Zd.key,
        //构件模板旧id
        oldTmpId: part.Zd.key.substr(0, part.Zd.key.length - 1),
        //状态
        state: 4,
        //节点所有锚点uuid
        uuidList: uuidList
      });
      handleMessageToParent("returnFormJson", gjidAndTemid);
    })
  })
  //复制/剪切到剪切板
  myDiagram.addDiagramListener("ClipboardChanged", function (e) {
    //console.log("剪切版数据1111",e.subject)
    var idList = []
    e.subject.each(function (part) {
      idList.push(part.Zd.key)
    })
    var gjidAndTemid = []
    gjidAndTemid.push({
      gjId: "",
      tmpId: idList,
      state: 3
    })
    handleMessageToParent("returnFormJson", gjidAndTemid);
  })

  //点击画布事件
  myDiagram.addDiagramListener("BackgroundSingleClicked", function (e, obj) {
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
  })

  //画布节点点击监听
  myDiagram.addDiagramListener("ObjectSingleClicked", function (e) {
    // console.log("当前画布json111",e);
    console.log("点击监听e111", e.subject.part.Zd);
    var gjidAndTemid = [];
    gjidAndTemid.push({
      //构件ID
      gjId: e.subject.part.Zd.gjId,
      //构件模板id(nodeID)
      tmpId: e.subject.part.Zd.key,
      //状态
      state: 2
    });
    //gjIdAndTemId = gjidAndTemid;
    handleMessageToParent("returnFormJson", gjidAndTemid);
  })


  //监听连线完成事件
  myDiagram.addDiagramListener("LinkDrawn", function (e) {
    console.log("监听连线完成事件", e.subject.data)
    //输出端口id
    let outputId = e.subject.data.fromPort
    //输入端口id 
    let inputId = e.subject.data.toPort
    var linkPortData = findPortData(outputId, inputId)
    console.log("55558225484545", linkPortData)
    var sourceType = linkPortData.fromNodeData.dataTypeName.replace("*", "")
    var targetType = linkPortData.toNodeData.dataTypeName.replace("*", "")
    var isConnect = false
    if (sourceType == targetType) {
      isConnect = true
    }
    for (var i = 0; i < connectionData.length; i++) {
      if ((sourceType == connectionData[i].label && targetType == connectionData[i].value) || (sourceType == connectionData[i].value && targetType == connectionData[i].label)) {
        isConnect = true
      }
    }
    if (!isConnect) {
      myDiagram.commandHandler.deleteSelection(e.subject.data)
      handleMessageToParent("nodeTypeNotMatch", "节点类型不匹配");
    }

  });
  if (loadData != "") {
    reduction()
  }

}
//根据端口portId查询对应的数据
function findPortData(fromPort, toPort) {
  let linkNodeData = {}
  let modelData = myDiagram.model
  var linkDataArray = modelData.linkDataArray
  //所有节点的数据
  var nodeDataArray = modelData.nodeDataArray
  for (var nodeData of nodeDataArray) {
    if (fromPort.split("*")[2] == nodeData.key) {
      if (fromPort.split("*")[1] == "output") {
        for (let fromData of nodeData.rightArray) {
          if (fromPort == fromData.portId) {
            linkNodeData.fromNodeData = fromData.portDat
          }
        }

      }
    }
    if (toPort.split("*")[2] == nodeData.key) {
      if (toPort.split("*")[1] == "input") {
        for (let toData of nodeData.leftArray) {
          if (toPort == toData.portId) {
            linkNodeData.toNodeData = toData.portDat
          }
        }
      }
    }
  }
  console.log("****linklist", linkNodeData)
  return linkNodeData
}
function reduction() {
  myDiagram.model = go.Model.fromJson(loadData)
}

//清空当前画布
function cleanCanvas() {
  myDiagram.model = go.Model.fromJson({})
}

//保存arrow集合
function save() {
  var modelJson = myDiagram.model
  //所有的连线数据
  var linkDataArray = modelJson.linkDataArray
  //所有节点的数据
  var nodeDataArray = modelJson.nodeDataArray
  var linkData = []
  for (var i = 0; i < linkDataArray.length; i++) {
    //开始端点类型
    var sourDataTypeName = "";
    //开始端点名称
    var sourVariableName = "";
    //开始端点长度
    var sourLength = "";
    //开始构件名称
    var sourCompName = "";
    //结束端点类型
    var tarDataTypeName = "";
    //结束端点名称
    var tarVariableName = "";
    //结束构件名称
    var tarCompName = ""
    for (var nodeData of nodeDataArray) {
      console.log("每个节点的数据", nodeData.key)
      console.log("port", linkDataArray[i].fromPort.split("*")[1])
      if (linkDataArray[i].fromPort.split("*")[2] == nodeData.key) {
        if (linkDataArray[i].fromPort.split("*")[1] == "output") {
          for (var rightPortData of nodeData.rightArray) {
            if (linkDataArray[i].fromPort == rightPortData.portId) {
              sourDataTypeName = rightPortData.portDat.dataTypeName
              sourVariableName = rightPortData.portDat.variableName
              sourLength = rightPortData.portDat.lengthName
              sourCompName = nodeData.text
            }
          }
        } else {
          for (var leftPortData of nodeData.leftArray) {
            if (linkDataArray[i].fromPort == leftPortData.portId) {
              tarDataTypeName = leftPortData.portDat.dataTypeName
              tarVariableName = leftPortData.portDat.variableName
              tarCompName = nodeData.text
            }
          }
        }
      }
    }
    //拼接开始端点数据
    var sourcePrame = sourDataTypeName + " " + sourVariableName + "_" + sourCompName
    let reg = /\w+\[[0-9]+\]/i; //
    if (reg.test(sourVariableName)) {
      let strTemp = sourVariableName.split("[")
      console.log(strTemp)
      sourcePrame = sourDataTypeName + " " + strTemp[0] + "_" + sourCompName + "[" + strTemp[1]
    }
    if (sourVariableName.indexOf("->") != -1) {
      let strTemp = sourVariableName.split("->")
      sourcePrame = sourDataTypeName + " " + strTemp[0] + "_" + sourCompName + "->" + strTemp[1]
    }
    console.log("sourVariableName", sourVariableName)
    console.log("sourVariableName", sourVariableName.indexOf("->"))
    //拼接结束端点数据
    var targetPrame = tarDataTypeName + " " + tarVariableName + "_" + tarCompName
    console.log("tarVariableName", tarVariableName)
    if (reg.test(tarVariableName)) {
      let strTemp = tarVariableName.split("[")
      console.log(strTemp)
      targetPrame = tarDataTypeName + " " + strTemp[0] + "_" + tarCompName + "[" + strTemp[1]
    }
    if (tarVariableName.indexOf("->") != -1) {
      let strTemp = tarVariableName.split("->")
      targetPrame = tarDataTypeName + " " + strTemp[0] + "_" + tarCompName + "->" + strTemp[1]
    }
    var connect = []
    var conn = []
    var connectionGjId = {
      startItem: linkDataArray[i].from,
      endItem: linkDataArray[i].to
    }
    var connectionPortData = {
      start: sourcePrame,
      end: targetPrame,
      length: sourLength,
      id: i,
      endId: linkDataArray[i].toPort,
      startId: linkDataArray[i].fromPort
    }
    connect.push(connectionGjId)
    conn.push(connectionPortData)
    var con = {
      drawing: connect,
      basic: conn
    }
    linkData.push(con)
  }
  let canvasAllKey = new Array()
  for (let node of nodeDataArray) {
    canvasAllKey.push(node.key)
  }
  var saveData = {
    "saveArrow": JSON.stringify({ arrow: linkData }),
    "saveflowChartJson": myDiagram.model.toJson(),
    "canvasAllKey": canvasAllKey
  };
  handleMessage(saveData, 'returnSave');
  //return JSON.stringify({arrow: linkData });
}







