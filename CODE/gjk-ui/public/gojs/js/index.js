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
var flagSimulation = true;
//构件更新状态
var compUpdateState = {}
//多选数据
var nodeOrLinkList;
var nodeOrLinkFirst
//仿真数据
var simulationData = [];
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
    case 'getCompDtosData':
      //接收属性栏修改后的数据
      proData = data
      //console.log(JSON.stringify(proData.params));
      updatePoint(proData.params);
      break
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
    case 'alignment':
      // var id = ""
      // if (idList.length > 0) {
      // 	id = idList[0]
      // }
      // var top = $("#" + id).offset().top;
      // var left = $("#" + id).offset().left;
      switch (data.params) {
        case "1":
          topAlignment();
          break;
        case "2":
          leftAlignment();
          break;
        case "3":
          rightAlignment();
          break;
        case "4":
          bottomAlignment();
          break;
        case "5":
          verticalCenter();
          break;
        case "6":
          levelCenter();
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
      if (!data.state) {
        completeCheckFun(data.params)
      } else {
        removeStyle()
      }
      break;
    case 'startSimulation':
      getSimulationData();
      break;
    case 'endSimulation':
      endSimulation();
      break;
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
        "grid.visible": true, //开启网格背景
        initialPosition: new go.Point(0, 0),
        //  autoScale:go.Diagram.Uniform,//自适应
        //  autoScale:go.Diagram.UniformToFill,//自适应
        "ChangedSelection": ChangedSelection
      });


  var simulationData = [];

  //  myDiagram.contextMenu =
  //   $$(go.Adornment, "Vertical",
  //       makeButton("开始仿真",
  //                  //function(e, obj) { e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint); },
  //                  //function(o) { return o.diagram.commandHandler.canPasteSelection();
  //                 // }
  //                  )
  //   );


  var nodeMenu =  // context menu for each Node
    $$(go.Adornment, "Vertical",
      //  makeButton("Copy",
      //             function(e, obj) { e.diagram.commandHandler.copySelection(); }),
      //  makeButton("Delete",
      //             function(e, obj) { e.diagram.commandHandler.deleteSelection(); }),
      // makeButton("开始仿真",
      //   function (e, obj) {
      //     flag = false;
      //     console.log("***************", e)
      //     console.log("obj***************", obj)
      //     var nodeOrLinkList = myDiagram.selection;
      //     nodeOrLinkList.each(function (nodeOrLink) {
      //       if (nodeOrLink instanceof go.Node) {
      //         //获取选中节点
      //         var key = nodeOrLink.data.key;
      //       } else if (nodeOrLink instanceof go.Link) {

      //         if (componentMap.get(nodeOrLink.data.from) === componentMap.get(nodeOrLink.data.to)) {
      //           handleMessageToParent("returnFZInfo", "所选连线中存在俩端构件属于同一组件不可进行仿真");
      //         } else {
      //           //获取选中的连线
      //           var from = nodeOrLink.data.fromPort;
      //           var to = nodeOrLink.data.toPort;
      //           //   console.log("from***************",from)
      //           //   console.log("to***************",to)
      //           //   console.log("findPortData(from,to)***************",findPortData(from,to))
      //           //  console.log("_______________",JSON.parse( myDiagram.model.toJson()))
      //           let linkNodeData = findPortData(from, to);
      //           let startId = nodeOrLink.data.from;
      //           let endId = nodeOrLink.data.to;
      //           let startName = linkNodeData.fromNodeData.variableName;
      //           let endName = linkNodeData.toNodeData.variableName
      //           let simulation = startId + ":" + startName + "|" + endId + ":" + endName

      //           simulationData.push(simulation)
      //         }



      //       }
      //     });
      //     handleMessageToParent("returnSimulationData", simulationData);

      //     console.log("传vue数据linkdata", simulationData)
      //   }),
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
              // if (componentMap.get(startId) === componentMap.get(endId)) {
              //   handleMessageToParent("returnFZInfo", "所选连线俩端构件属于同一组件");
              // } else 

              var linkList =  nodeOrLinkList.qd
              // 设置空数组
              let questionListType = [] // 存储新的数组
              // 遍历arr对象，将数据插入到数组中
              for (let name in linkList) {
                questionListType.push(linkList[name + ''])
              }
              questionListType.forEach(element => {
               var fromKey = element.value.Zd.from;
               var toKey = element.value.Zd.to;
               var fromPortKey = element.value.Zd.fromPort;
               var toPortKey = element.value.Zd.toPort;
              var fromValue = componentMap.get(fromKey);
              var toValue = componentMap.get(toKey);
              if(fromValue == toValue){
               showMessage('不满足要求连线不可进行仿真', 'error', 3000)
             }
             if (!flagSimulation && fromValue != toValue) {
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
            })
             
             if (flagSimulation == true) {
                showMessage('请先开始仿真业务', 'error', 2000)
              } 
            } 
            //flagSimulation = true
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
    $$(go.Node, "Vertical",
      {
        //是否显示阴影
        //isShadowed:true,
        //是否显示选择框
        // selectionAdorned: false,
        locationObjectName: "BODY",
        locationSpot: go.Spot.Center,
        selectionObjectName: "BODY"
      },
      new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
      $$(go.TextBlock,
        {
          margin: 0,
          textAlign: "center",
          font: "14px  Segoe UI,sans-serif",
          // stroke: "red", 
          editable: false,
          visible: true,

        },
        new go.Binding("text", "stateText"),
        new go.Binding("stroke", "planColor")
      ),
      //new go.Binding("visible", "planVisible"),
      // the body
      // $$(go.Panel, "Auto",
      // $$(go.Shape, "Rectangle",
      // {
      //   //stroke: null, strokeWidth: 100,
      //  // desiredSize: new go.Size(80, 80),
      //  // margin: new go.Margin(1, 0)
      //  width: 120, height: 100,
      // //  fill: "#1F4963",
      // //  visible:false
      // },
      // new go.Binding("fill","planColor"),
      // new go.Binding("visible","planVisible"),  
      //  ),  
      $$(go.Panel, "Horizontal",
        {
          row: 1, column: 1, name: "BODY",
          stretch: go.GraphObject.Fill,
        },
        //左侧锚点
        $$(go.Panel, "Vertical",
          new go.Binding("itemArray", "leftArray"),
          {
            row: 1, column: 2,
            toolTip:
              $$(go.Adornment, "Auto",
                $$(go.Shape, { fill: "#EFEFCC" }),
                $$(go.TextBlock, { margin: 6, width: 140, textAlign: 'left', },
                  new go.Binding("text", "", infoString).ofObject())
              ),
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
                $$(go.Shape, "TriangleRight",
                  {
                    stroke: null, strokeWidth: 0,
                    desiredSize: new go.Size(10, 8),
                    margin: new go.Margin(2, 0)
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

        //中间图片
        $$(go.Picture,
          {
            margin: 0, column: 0,
          },
          // Picture.source参数值与模型数据中的"source"字段绑定
          new go.Binding("source", "source"),
          new go.Binding("width", "width"),
          new go.Binding("height", "height"),
        ),

        //右侧锚点
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
                  fromSpot: go.Spot.Right,
                  toSpot: go.Spot.Right,
                  fromLinkable: true,
                  toLinkable: false,
                  cursor: "pointer",
                  toMaxLinks: 1,
                  mouseHover: function (e, node) {
                    portId = node.Is
                  }
                },
                new go.Binding("portId", "portId"),
                $$(go.Shape, "TriangleRight",
                  {
                    stroke: null, strokeWidth: 0,
                    desiredSize: new go.Size(12, 8),
                    margin: new go.Margin(2, 0)
                  },
                  new go.Binding("fill", "portColor")),

              )  // end itemTemplate
          }
        ),  // end Vertical Panel

        //        $$(go.Shape, "Rectangle",
        //          { fill: "#AC193D", stroke: null, strokeWidth: 0,
        //            minSize: new go.Size(56, 56) }),
        //  $$(go.TextBlock,
        //    { margin: 10, textAlign: "center", font: "14px  Segoe UI,sans-serif", stroke: "white", editable: true },
        //    new go.Binding("text", "text").makeTwoWay())
      ),  // end Auto Panel body
      //   ),

      //构件名称
      $$(go.TextBlock, "Vertical", {
        text: "",
        font: '10pt sans-serif',
        //background: #fff0f0,
        // overflow: go.TextBlock.OverflowEllipsis,
        maxLines: 1,
        margin: 2,
        //width: 90
      },
        new go.Binding("text", "text"),
      ),
    );

  myDiagram.linkTemplate =
    $$(go.Link,
      {
        routing: go.Link.AvoidsNodes, corner: 5,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        selectable: true,
        contextMenu: nodeMenu,
        layerName: "Background"  // 不要在任何节点前面交叉
      },
      $$(go.Shape, { stroke: "black", strokeWidth: 2 }),
      $$(go.Shape, { stroke: "black", fill: "black", toArrow: "Standard" }),

    );

  myDiagram.linkTemplateMap.add("lineColorred",
    $$(go.Link,
      {
        routing: go.Link.AvoidsNodes, corner: 5,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        selectable: true,
        contextMenu: nodeMenu,
        layerName: "Background"  // 不要在任何节点前面交叉
      },
      $$(go.Shape, { stroke: "red", strokeWidth: 2 }),
      $$(go.Shape, { stroke: "red", fill: "red", toArrow: "Standard" }),
    )
  );
  myDiagram.linkTemplateMap.add("lineColoryellow",
    $$(go.Link,
      {
        routing: go.Link.AvoidsNodes, corner: 5,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        selectable: true,
        contextMenu: nodeMenu,
        layerName: "Background"  // 不要在任何节点前面交叉
      },
      $$(go.Shape, { stroke: "yellow", strokeWidth: 2 }),
      $$(go.Shape, { stroke: "yellow", fill: "yellow", toArrow: "Standard" }),
    )
  );
  myDiagram.linkTemplateMap.add("lineColorgreen",
    $$(go.Link,
      {
        routing: go.Link.AvoidsNodes, corner: 5,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        selectable: true,
        contextMenu: nodeMenu,
        layerName: "Background"  // 不要在任何节点前面交叉
      },
      $$(go.Shape, { stroke: "green", strokeWidth: 2 }),
      $$(go.Shape, { stroke: "green", fill: "green", toArrow: "Standard" }),
    )
  );

  myDiagram.model
  $$(go.GraphLinksModel,
    {
      linkFromPortIdProperty: "fromPort",  // required information:
      linkToPortIdProperty: "toPort",      // identifies data property names
      //  nodeDataArray: loadData.nodeDataArray,
      //  linkDataArray: loadData.linkDataArray
    }

  );
  //myDiagram.model.addLinkData({"color":"red"});
  //      myDiagram.linkTemplateMap.add("lineColor",
  //             objGo(go.Link,
  //                 {
  //                   selectable: false,      // 用户不能选择链接
  //                   curve: go.Link.Bezier,
  //                   layerName: "Background"  // 不要在任何节点前面交叉
  //                 },
  //                 objGo(go.Shape,  // 此形状仅在突出显示时才显示
  //                   { isPanelMain: true, stroke: "red", strokeWidth: 2 },
  //                   new go.Binding("stroke", "isHighlighted", function(h) { return h ? "red" : null; }).ofObject()),
  //                 objGo(go.Shape,
  //                   // mark each Shape to get the link geometry with isPanelMain: true
  //                   { isPanelMain: true, stroke: "red", strokeWidth: 1 },
  //                   new go.Binding("stroke", "color")),
  //                 objGo(go.Shape, { toArrow: "Standard",stroke: "red"})
  //             )

  //         );
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
      //plan颜色
      var planColor = "red"
      //plan是否显示值
      var stateText = ""
      // path = takeScreenshot(ui.draggable.attr("id"))
      var div = $("#" + ui.draggable.attr("id"));
      var width = div[0].offsetWidth
      var height = div[0].offsetHeight
      console.log("宽++++++", div[0].offsetHeight)

      html2canvas(document.getElementById(ui.draggable.attr("id"))).then(canvas => {
        for (var value of dat) {
          if (ui.draggable.attr("id") == value.id) {
            //输入锚点集合数据
            var rightArray = new Array()
            //输出锚点集合数据
            var leftArray = new Array()
            var uuidList = new Array();
            dropData = value
            compName = value.compName
            compNameText = value.compName + "_V" + value.compVersion
            //document.body.appendChild(canvas)
            path = canvas.toDataURL("image/png");
            //inputList = value.inputList.concat()
            //outputList = value.outputList.concat()
            console.log(value.compName)
            console.log("锚点数据", value.inputList)
            for (var i = 0; i < value.inputList.length; i++) {
              var inputData = {}
              let inputStr = ""
              if (value.inputList[i].variableStructType != "") {
                inputStr = "参数名称:" + value.inputList[i].variableName + "\n" + "参数类型:" + value.inputList[i].dataTypeName + "\n" + "参数长度:" + value.inputList[i].lengthName + "\n" + "结构体:" + value.inputList[i].variableStructType
              } else {
                inputStr = "参数名称:" + value.inputList[i].variableName + "\n" + "参数类型:" + value.inputList[i].dataTypeName + "\n" + "参数长度:" + value.inputList[i].lengthName
              }
              inputData.portColor = "#00AF00"
              inputData.portId = i + '*input*' + random
              inputData.description = inputStr
              inputData.portDat = value.inputList[i]
              leftArray.push(inputData)
              uuidList.push(i + '*input*' + random)
            }
            console.log("左边锚点集合", leftArray)
            for (var j = 0; j < value.outputList.length; j++) {
              var outputData = {}
              let outputStr = ""
              if (value.outputList[j].variableStructType != "") {
                outputStr = "参数名称:" + value.outputList[j].variableName + "\n" + "参数类型:" + value.outputList[j].dataTypeName + "\n" + "参数长度:" + value.outputList[j].lengthName + "\n" + "结构体:" + value.outputList[j].variableStructType
              } else {
                outputStr = "参数名称:" + value.outputList[j].variableName + "\n" + "参数类型:" + value.outputList[j].dataTypeName + "\n" + "参数长度:" + value.outputList[j].lengthName
              }
              outputData.portColor = "#00AF00"
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
              text: compNameText,
              loc: go.Point.stringify(q),
              source: path,
              rightArray: rightArray,
              leftArray: leftArray,
              inputList: inputList,
              outputList: outputList,
              planColor: planColor,
              stateText: stateText,
              width: width,
              height, height

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

          }
        }
      });
    }
  });

  //循环添加构件并生成相应模板
  function appendDiv() {
    var str = "";
    for (var i = 0; i < dat.length; i++) {
      // str += "<div id = '" + dat[i].id + "' >" + dat[i].compImg + "</div></br>"
      str += dat[i].compImg + "<span >" + dat[i].compName + "_V" + dat[i].compVersion + "</span>"
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
    let uuidList = new Array()
    if (e.subject.part.Zd.leftArray != undefined || e.subject.part.Zd.rightArray != undefined) {
      for (let leftData of e.subject.part.Zd.leftArray) {
        uuidList.push(leftData.portId)
      }
      for (let rightData of e.subject.part.Zd.rightArray) {
        uuidList.push(rightData.portId)
      }
    var gjidAndTemid = [];
    gjidAndTemid.push({
      //构件ID
      gjId: e.subject.part.Zd.gjId,
      //构件模板id(nodeID)
      tmpId: e.subject.part.Zd.key,
      //状态
      state: 2,
      //锚点集合
      uuidList: uuidList
    });
    //gjIdAndTemId = gjidAndTemid;
    handleMessageToParent("returnFormJson", gjidAndTemid);
  }
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
//检查更新
function appendUpdateDiv() {
  var strUpdate = ""
  var notUpdate = ""
  for (var i = 0; i < dat.length; i++) {
    for (let key in compUpdateState) {
      if (compUpdateState[key] == "0") { //已更新
        if (dat[i].id == key) {
          strUpdate += dat[i].compImg
        }
      } else {
        if (dat[i].id == key) {
          notUpdate += dat[i].compImg + "<span >" + dat[i].compName + "_V" + dat[i].compVersion + "</span>"
        }
      }
    }
  }
  $('#update').append(strUpdate)
  $('#notUpdate').append(notUpdate)
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
      if (linkDataArray[i].fromPort.split("*")[2] == nodeData.key) { //出口点
        if (linkDataArray[i].fromPort.split("*")[1] == "output") {
          for (var rightPortData of nodeData.rightArray) {
            if (linkDataArray[i].fromPort == rightPortData.portId) {
              sourDataTypeName = rightPortData.portDat.dataTypeName
              sourVariableName = rightPortData.portDat.variableName
              sourLength = rightPortData.portDat.lengthName
              sourCompName = nodeData.text
            }
          }
        }
      }
      if (linkDataArray[i].toPort.split("*")[2] == nodeData.key) { //入口点
        if (linkDataArray[i].toPort.split("*")[1] == "input") {
          for (var leftPortData of nodeData.leftArray) {
            console.log("比较", linkDataArray[i].toPort, leftPortData.portId)
            if (linkDataArray[i].toPort == leftPortData.portId) {
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
var backupJsonData
//完备性检查
function completeCheckFun(data) {
  console.log("完备性检查数据", data);
  console.log("当前画布json111", JSON.parse(myDiagram.model.toJson()));
  //获取当前画布json
  backupJsonData = JSON.parse(myDiagram.model.toJson());
  var jsonData = JSON.parse(myDiagram.model.toJson());
  //逻辑处理
  //完备性检查
  if (data.m_checkType == 0) {
    data.m_checkInfoList.forEach(element => {
      //判断m_checkType是否是0为修改构件
      if (element.m_checkType == 0) {
        jsonData.nodeDataArray.forEach(e => {
          if (element.m_spbId == e.key) {
            if (element.m_modifyState == 0) {
              e.stateText = "构件正常";
              e.planColor = "green";
            } else if (element.m_modifyState == 1) {
              e.stateText = "构件警告";
              e.planColor = "#ffcc00";
            } else if (element.m_modifyState == 2) {
              e.stateText = "构件错误";
              e.planColor = "#e6005c";
            }

          }
        });
      }
      //判断m_checkType是否是0为修改端点
      if (element.m_checkType == 2) {

        jsonData.nodeDataArray.forEach(e => {
          //判断是输入端口
          if (element.m_spbId == e.key && element.m_paramType == "输入") {
            e.leftArray.forEach(l => {
              if (element.m_paramName == l.portDat.variableName) {
                console.log("77878787878", l)
                if (element.m_modifyState == 0) {
                  l.portColor = "#0dba34";
                } else if (element.m_modifyState == 1) {
                  l.portColor = "#ffcc00";
                } else if (element.m_modifyState == 2) {
                  l.portColor = "#e6005c";
                }
              }
            });
          }
          //判断是输出端口
          if (element.m_spbId == e.key && element.m_paramType == "输出") {
            e.rightArray.forEach(r => {
              if (element.m_paramName == r.portDat.variableName) {
                if (element.m_modifyState == 0) {
                  r.portColor = "#0dba34";
                } else if (element.m_modifyState == 1) {
                  r.portColor = "#ffcc00";
                } else if (element.m_modifyState == 2) {
                  r.portColor = "#e6005c";
                }
              }
            });
          }
        });
      }
      //判断是接口
      if (element.m_checkType == 0) {
        jsonData.linkDataArray.forEach(link => {
          //连线黄色样式
          link.category = "lineColoryellow"
          //连线红色样式
          link.category = "lineColorred"
          //连线绿色样式
          link.category = "lineColorgreen"

          
          // if(element.m_spbId ==  e.key){
          //     if(element.m_modifyState == 0){
          //       e.stateText= "构件正常";
          //       e.planColor= "green";
          //     }else if(element.m_modifyState == 1 ){
          //       e.stateText= "构件警告";
          //       e.planColor= "#ffcc00";
          //     }else if(element.m_modifyState == 2){
          //       e.stateText= "构件错误";
          //       e.planColor= "#e6005c";
          //     }    
          // }

        });
      }

    });
  }
  myDiagram.model = go.Model.fromJson({})
  myDiagram.model = go.Model.fromJson(jsonData);
}
//还原完备性检查
function removeStyle() {
  myDiagram.model = go.Model.fromJson({})
  myDiagram.model = go.Model.fromJson(backupJsonData);
}

//上对齐
function topAlignment() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstLoc = nodeOrLinkFirst.Zd.loc;
  var firstX = nodeOrLinkFirst.Ui.x;
  var firstY = nodeOrLinkFirst.Ui.y;
  var firstWidth = nodeOrLinkFirst.Uc.width;
  var firstHeight = nodeOrLinkFirst.Uc.height;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var x = element.value.Ui.x;
      var y = element.value.Ui.y;
      var width = element.value.Uc.width;
      var height = element.value.Uc.height;
      var node = myDiagram.model.findNodeDataForKey(key);
      var a = firstY + height * 1 / 2
      var loc = x + " " + a;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}
//下对齐
function bottomAlignment() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstLoc = nodeOrLinkFirst.Zd.loc;
  var firstX = nodeOrLinkFirst.Ui.x;
  var firstY = nodeOrLinkFirst.Ui.y;
  var firstWidth = nodeOrLinkFirst.Uc.width;
  var firstHeight = nodeOrLinkFirst.Uc.height;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var x = element.value.Ui.x;
      var y = element.value.Ui.y;
      var width = element.value.Uc.width;
      var height = element.value.Uc.height;
      var node = myDiagram.model.findNodeDataForKey(key);
      var coordinates = firstY - height * 1 / 2
      var loc = x + " " + coordinates;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}

//左对齐
function leftAlignment() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstX = nodeOrLinkFirst.Ui.x;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var y = element.value.Ui.y;
      var height = element.value.Uc.height;
      var node = myDiagram.model.findNodeDataForKey(key);
      var coordinates = firstX + height * 1 / 2;
      var loc = coordinates + " " + y;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}
//右对齐
function rightAlignment() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstLoc = nodeOrLinkFirst.Zd.loc;
  var firstX = nodeOrLinkFirst.Ui.x;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var y = element.value.Ui.y;
      var height = element.value.Uc.height;
      var node = myDiagram.model.findNodeDataForKey(key);
      var coordinates = firstX - height * 1 / 2;
      var loc = coordinates + " " + y;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}
//水平居中
function verticalCenter() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstY = nodeOrLinkFirst.Ui.y;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var x = element.value.Ui.x;
      var node = myDiagram.model.findNodeDataForKey(key);
      var loc = x + " " + firstY;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}
//垂直居中
function levelCenter() {
  var firtsKey = nodeOrLinkFirst.Zd.key;
  var firstX = nodeOrLinkFirst.Ui.x;
  var nodeList = nodeOrLinkList.qd;
  // 设置空数组
  let questionListType = [] // 存储新的数组
  // 遍历arr对象，将数据插入到数组中
  for (let name in nodeList) {
    questionListType.push(nodeList[name + ''])
  }
  questionListType.forEach(element => {
    if (key != firtsKey) {
      var key = element.value.Zd.key;
      var y = element.value.Ui.y;
      var node = myDiagram.model.findNodeDataForKey(key);
      var loc = firstX + " " + y;
      myDiagram.model.setDataProperty(node, 'loc', loc);
    }

  });
}
function ChangedSelection(e) {//选择事件
  nodeOrLinkList = myDiagram.selection;
  nodeOrLinkFirst = myDiagram.selection.first()
};

//动态增加删除锚点
function updatePoint(proParam) {
  console.log("进入增加删除锚点", proParam);
  //构件id
  var nodeId = proParam.compId
  //锚点为输入端口还是输出端口
  var portType = proParam.inOrOut
  //锚点id
  var pid = proParam.uid
  if (proParam.addOrDel == 'add') { //增加锚点
    myDiagram.startTransaction("addPort");
    var nodeData = myDiagram.model.findNodeDataForKey(nodeId);
    console.log("节点数据", nodeData)
    let str = ""
    if (proParam.data.variableStructType != "" && proParam.data.variableStructType != undefined) {
      str = "参数名称:" + proParam.data.variableName + "\n" + "参数类型:" + proParam.data.dataTypeName + "\n" + "参数长度:" + proParam.data.lengthName + "\n" + "结构体:" + proParam.data.variableStructType
    } else {
      str = "参数名称:" + proParam.data.variableName + "\n" + "参数类型:" + proParam.data.dataTypeName + "\n" + "参数长度:" + proParam.data.lengthName
    }
    var newPortData = {
      portId: pid,
      portColor: "#923951",
      description: str,
      portDat: proParam.data
    };
    if (portType === 'input') {
      myDiagram.model.insertArrayItem(nodeData.leftArray, -1, newPortData);
    } else {
      myDiagram.model.insertArrayItem(nodeData.rightArray, -1, newPortData);
    }
    myDiagram.commitTransaction("addPort");
  } else { //删除锚点
    myDiagram.startTransaction("removePort");
    //根据节点id查找数据
    let modelNodeData = myDiagram.model.nodeDataArray
    for (let val of modelNodeData) {
      if (val.key == proParam.compId) {
        if (proParam.inOrOut == 'input') {
          //console.log("进入input",pid)
          let inputArr = val.leftArray
          console.log("输入数组", inputArr)
          for (var i = 0; i < inputArr.length; i++)
            if (inputArr[i].portId === pid) {
              myDiagram.model.removeArrayItem(inputArr, i);
              break;
            }
        } else {
          let outArr = val.rightArray
          for (var i = 0; i < outArr.length; i++)
            if (outArr[i].portId === pid) {
              myDiagram.model.removeArrayItem(outArr, i);
              break;
            }
        }
      }
    }
  }
  myDiagram.commitTransaction("removePort");
}
//开始仿真
var backupJsonData =null ;
function  getSimulationData(){
  if(nodeOrLinkList == undefined){
    showMessage('请选择连线', 'error', 3000)
  }
  //备份当前画布json   
  backupJsonData = JSON.parse(myDiagram.model.toJson());
  flagSimulation = false;
 var linkList =  nodeOrLinkList.qd
 // 设置空数组
 let questionListType = [] // 存储新的数组
 // 遍历arr对象，将数据插入到数组中
 for (let name in linkList) {
   questionListType.push(linkList[name + ''])
 }
 questionListType.forEach(element => {
  var fromKey = element.value.Zd.from;
  var toKey = element.value.Zd.to;
  var fromPortKey = element.value.Zd.fromPort;
  var toPortKey = element.value.Zd.toPort;
 var fromValue = componentMap.get(fromKey);
 var toValue = componentMap.get(toKey);
 if(fromValue == toValue){
  showMessage('所选连线中存在俩端构件属于同一部件不可进行仿真', 'error', 3000)
  var linkData=myDiagram.model.findNodeDataForKey(element.value.Zd.__gohashid);
  myDiagram.model.setDataProperty(linkData, 'color', "#e6005c");
  drawNode('error',fromKey,toKey);
  return false;
}
if(fromValue != toValue){
  let linkNodeData = findPortData(fromPortKey, toPortKey);
  let startId = fromKey;
  let endId = toKey;
  let startName = linkNodeData.fromNodeData.variableName;
  let endName = linkNodeData.toNodeData.variableName
  let simulation = startId + ":" + startName + "|" + endId + ":" + endName
  simulationData.push(simulation)
  drawNode('success',fromKey,toKey);
  handleMessageToParent("returnSimulationData", simulationData);
  console.log("传vue数据linkdata", simulationData)
}
 });

}



//结束仿真
function endSimulation(){
  if(flagSimulation == false){
    flagSimulation = true;
    myDiagram.model = go.Model.fromJson({})
    myDiagram.model = go.Model.fromJson(backupJsonData);
  }
}

function drawNode(message,fromKey,toKey){
  var jsonData = JSON.parse(myDiagram.model.toJson());
    cleanCanvas();
       jsonData.linkDataArray.forEach(link => {
        if(fromKey ==  link.from && toKey == link.to ){
          switch(message){
            case 'success':        
              link.category = "lineColorgreen"
            break;
            case 'error':        
            link.category = "lineColorred"
          break;
          }
        };
        console.log("**************",link)
        });
        myDiagram.model = go.Model.fromJson({})
        myDiagram.model = go.Model.fromJson(jsonData);
}
//信息提示
function showMessage(message, type, time) {
	let str = ''
	switch (type) {
		case 'success':
			str = '<div class="success-message" style="width: 400px;height: 40px;text-align: center;background-color:#daf5eb;;color: rgba(59,128,58,0.7);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;z-index: 9999">\n' +
				'    <span class="mes-text">' + message + '</span></div>'
			break;
		case 'error':
			str = '<div class="error-message" style="width: 400px;height: 40px;text-align: center;background-color: #f5f0e5;color: rgba(238,99,99,0.8);position: fixed;left: 43%;top: 10%;line-height: 40px;border-radius: 5px;;z-index: 9999">\n' +
				'    <span class="mes-text">' + message + '</span></div>'
	}
	$('body').append(str)
	setTimeout(function () {
		$('.' + type + '-message').remove()
	}, time)
}
