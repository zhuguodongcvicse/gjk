var visoConfig = {
  visoTemplate: {}
}

visoConfig.visoTemplate.playAudioNode = '<div class="pa" id="{{id}}" style="top:{{top}}px;left:{{left}}px"><a class="btn btn-success" href="#" role="button">放音</a></div>'

// 基本连接线样式
visoConfig.connectorPaintStyle = {
  lineWidth: 2,
  strokeStyle: '#436D9F',
  joinstyle: 'round',
  fill: 'pink',
  outlineColor: '',
  outlineWidth: 2
}

// 鼠标悬浮在连接线上的样式
visoConfig.connectorHoverStyle = {
  lineWidth: 2,
  strokeStyle: '#62A5F6',
  outlineWidth: 10,
  outlineColor: ''
}

visoConfig.baseStyle = {
  endpoint: ["Dot", {
    radius: 6,
    fill: 'pink',
  }], // 端点的形状
 // endpoint:["Image",{src : "./img/u0.png"}],
  //endpoint:"Dot",
  connectorStyle: visoConfig.connectorPaintStyle, // 连接线的颜色，大小样式
  connectorHoverStyle: visoConfig.connectorHoverStyle,
  paintStyle: {
    strokeStyle: '#4ECB74',
    //stroke: '#4ECB74',
    fill: 'pink',
    fillStyle: '#4ECB74',
    radius: 5 ,
    lineWidth: 2
  }, // 端点的颜色样式
  // hoverPaintStyle: {
  //   outlineStroke: 'pink'
  // },
  //hoverPaintStyle: { stroke: 'blue' },
  isSource: true, // 是否可以拖动（作为连线起点）
 // connector: ['Flowchart', { gap: 0, cornerRadius: 5,stub:10 }],  // 连接线的样式种类有[Bezier],[Flowchart],[StateMachine ],[Straight ]
  connector: ["Flowchart", { stub: [-5,-5], gap: 0,midpoint:5, cornerRadius: -5, alwaysRespectStubs: true }],
  //connector:["Flowchart", { curviness:50 }],
  isTarget: true, // 是否可以放置（连线终点）
  maxConnections: -1, // 设置连接点最多可以连接几条线
  connectorOverlays: [
    ['Arrow', {
      width: 10,
      length: 10,
      location: 0.1
    }],
    ['Arrow', {
      width: 10,
      length: 10,
      location: 0.3
    }],
    ['Arrow', {
      width: 10,
      length: 10,
      location: 0.7
    }],
    ['Label', {
      label:'<div class ="fz" style="width:16px;height:9px; display:none"></div>',//<div style="border:1px solid red">11</div>
      cssClass: '',
      labelStyle: {
        color: 'red'
      },
      events: {
        click: function (labelOverlay, originalEvent) {
         // console.log('click on label overlay for :' + labelOverlay.component)
          console.log(labelOverlay)
          console.log(originalEvent)
        }
      }
    }]
  ]
}

visoConfig.baseArchors = ['RightMiddle', 'LeftMiddle']
