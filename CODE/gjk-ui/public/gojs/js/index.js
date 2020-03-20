function init(){	
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
     
     
     
     
         myDiagram.nodeTemplate =
      $$(go.Node, "Table",
        { locationObjectName: "BODY",
          locationSpot: go.Spot.Center,
          selectionObjectName: "BODY",
         // contextMenu: nodeMenu
        },
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),

        // the body
        $$(go.Panel, "Auto",
          { row: 1, column: 1, name: "BODY",
            stretch: go.GraphObject.Fill },
            $$(go.Picture,
      { margin: 0, width: 110, height: 80,column: 0 },
      // Picture.source参数值与模型数据中的"source"字段绑定
     	new go.Binding("source","source"),
		 ),
            
            
//        $$(go.Shape, "Rectangle",
//          { fill: "#AC193D", stroke: null, strokeWidth: 0,
//            minSize: new go.Size(56, 56) }),
          $$(go.TextBlock,
            { margin: 10, textAlign: "center", font: "14px  Segoe UI,sans-serif", stroke: "white", editable: true },
            new go.Binding("text", "text").makeTwoWay())
        ),  // end Auto Panel body
       
        // the Panel holding the right port elements, which are themselves Panels,
        // created for each item in the itemArray, bound to data.rightArray
        $$(go.Panel, "Vertical",
          new go.Binding("itemArray", "rightArray"),
          { row: 1, column: 2,
            itemTemplate:
              $$(go.Panel,
                { _side: "right",
                  fromSpot: go.Spot.Right,toSpot: go.Spot.Right,
                  fromLinkable: true, toLinkable: true, cursor: "pointer",
                  },
                new go.Binding("portId", "portId"),
                $$(go.Shape, "Rectangle",
                  { stroke: null, strokeWidth: 0,
                    desiredSize: new go.Size(8, 8),
                    margin: new go.Margin(1, 0) },
                  new go.Binding("fill", "portColor")),
               
              )  // end itemTemplate
          }
        ),  // end Vertical Panel

       $$(go.Panel, "Vertical",
          new go.Binding("itemArray", "leftArray"),
          { row: 1, column: 0,
            itemTemplate:
              $$(go.Panel,
                { _side: "left",  // internal property to make it easier to tell which side it's on
                  fromSpot: go.Spot.Left,toSpot: go.Spot.Left,
                  fromLinkable: true, toLinkable: true, cursor: "pointer",
                  },
                new go.Binding("portId", "portId"),
                $$(go.Shape, "Rectangle",
                  { stroke: null, strokeWidth: 0,
                    desiredSize: new go.Size(8, 8),
                    margin: new go.Margin(1,0) },
                  new go.Binding("fill", "portColor"))
              )  // end itemTemplate
          }
        ),  // end Vertical Panel
       
      );
      
       myDiagram.linkTemplate =
      $$(go.Link,
        {
          routing: go.Link.Orthogonal, corner: 5,
          relinkableFrom: true, 
          relinkableTo: true,
          reshapable: true,
          resegmentable: true,
        },
        $$(go.Shape, { stroke: "gray", strokeWidth: 2 }),
        $$(go.Shape, { stroke: "gray", fill: "gray", toArrow: "Standard" })
      );
      
      myDiagram.model =
    $$(go.GraphLinksModel,
      { linkFromPortIdProperty: "fromPort",  // required information:
        linkToPortIdProperty: "toPort",      // identifies data property names
        nodeDataArray: [
         
        ],
        linkDataArray: [
          // no predeclared links
        ] });


        
//      myDiagram.nodeTemplate =
//    $$(go.Node, "Auto",
//      //{ locationSpot: go.Spot.Center },
//      new go.Binding("location", "loc", go.Point.parse),
//		 $$(go.Picture,
//    { margin: 10, width: 110, height: 80 },
//    // Picture.source参数值与模型数据中的"source"字段绑定
//   	new go.Binding("source","source"),
//		 ),
//  	$$(go.TextBlock,
//    	 new go.Binding("text", "text").makeTwoWay()),
////      $$(go.Shape,  "Ellipse",
////          { width: 6, height: 6, fill: "green",alignment:new go.Spot(0.8, 1, 0, 0),}),
//		
//		
//		 $$(go.Panel, "Vertical",
//        new go.Binding("itemArray", "rightArray"),
//        { row: 1, column: 2,
//          itemTemplate:
//            $$(go.Panel,
//              { _side: "right",
//                fromSpot: go.Spot.Right, toSpot: go.Spot.Right,
//                fromLinkable: true, toLinkable: true, cursor: "pointer"
//                },
//              new go.Binding("portId", "portId"),
//              $$(go.Shape, "Rectangle",
//                { stroke: null, strokeWidth: 0,
//                  desiredSize: new go.Size(8, 8),
//                  margin: new go.Margin(1, 0) },
//                new go.Binding("fill", "portColor")),
//             
//            )  // end itemTemplate
//        }
//      ),  // end Vertical Panel
//
//
//
//    );


 $(".btn").draggable({
      stack: "#myDiagramDiv",
      revert: true,
      revertDuration: 0
      
    });

    $("#myDiagramDiv").droppable({
      drop: function(event, ui) {
      	console.log("数据",ui)
      	console.log("数据1",event)
      	
        var elt = ui.draggable.first();
     	var text = "1108";
     	var path = "img/bbb.png"
        var x = ui.offset.left - $(this).offset().left;
        var y = ui.offset.top - $(this).offset().top;
        var p = new go.Point(x, y);
	      var q = myDiagram.transformViewToDoc(p);
		var random = ''
		random = Math.ceil(Math.random() * 10000000000000000000).toString().substr(0, 9 || 4)
		random = Date.now() + random
		console.log(random)
	      var model = myDiagram.model;
	      model.startTransaction("drop");
	        model.addNodeData({
	          key : random,
	          text: text,
	          loc: go.Point.stringify(q),
	          source :path,
	          rightArray:[ {portColor:"#923951", portId:"right0"},{portColor:"#ef3768", portId:"right1"} ],
	          leftArray:[ {portColor:"#425e5c", portId:"left0"} ]
	        });
	        model.commitTransaction("drop");
    }
    });
    
    
    
    
    
    
}
