var inf_json;
var inf_uuid;
var chipArr;
var infArr;
Q.registerImage('lamp', Q.Shapes.getShape(Q.Consts.SHAPE_CIRCLE, -80, -80, 16, 16));
var lampGradient = new Q.Gradient(Q.Consts.GRADIENT_TYPE_RADIAL, [Q.toColor(0xAAFFFFFF), Q.toColor(0x33EEEEEE), Q.toColor(0x44888888), Q.toColor(0x33666666)], [0.1, 0.3, 0.7, 0.9], 0, -0.2, -0.2);

function createLampStyles(color) {
	//信息框大小
	var styles = {};
	styles[Q.Styles.SHAPE_FILL_COLOR] = color;
	styles[Q.Styles.SHAPE_STROKE] = 0.5;
	styles[Q.Styles.SHAPE_STROKE_STYLE] = '#CCC';
	styles[Q.Styles.LABEL_BACKGROUND_COLOR] = '#FF0';
	styles[Q.Styles.SHAPE_FILL_COLOR] = color;
	styles[Q.Styles.LABEL_SIZE] = {
		width: 100,
		height: 200
	};
	styles[Q.Styles.LABEL_PADDING] = 5;
	styles[Q.Styles.LABEL_OFFSET_Y] = -10;
	styles[Q.Styles.SHAPE_FILL_GRADIENT] = lampGradient;
	styles[Q.Styles.LABEL_POSITION] = Q.Position.CENTER_TOP;
	styles[Q.Styles.LABEL_ANCHOR_POSITION] = Q.Position.LEFT_BOTTOM;
	return styles;
}
window.addEventListener("message", this.handleMessageFromParent) // 子接收方式二参数
var postMessageParentData = {
	cmd: "",//用于switch 判断
	params: []//具体参数
};

// 子接收父参数
function handleMessageFromParent(event) {
	infArr = event.data.params[0]
	chipArr = event.data.params[1]
	/* console.log("event.data.params",event.data.params);
	var aaa = JSON.parse(chipArr.chipData)
	console.log("aaa",aaa) */
	switch (event.data.cmd) {
		case 'getChipAndInf':
			inf_json = [];
			//console.log(event.data.params[1])

			for (var i = 0; i < infArr.length; i++) {
				if (infArr[i].infType == 3) {
					//	console.log("infArr[i].chipInf",infArr[i].chipInf)		
					inf_json[i] = {
						enableTooltip: true,
						image: 'lamp',
						br: false,
						styles: createLampStyles('#FF0'),
						clientProperties: {
							type: 'inf',
							inf_uuid: infArr[i].id,
							infName: infArr[i].infName,
							ifRate: infArr[i].ifRate,
							infType: infArr[i].infType
						}
						//	tooltip :infArr.id
					}
				}
			}
			init();
	}
};
function init() {
	$('.graph-editor').graphEditor(
		{
			images: [{
				name: '接口模板',
				imageWidth: 30,
				imageHeight: 30,
				images: inf_json
			}],
			callback: function (editor) {
				var graph = editor.graph;
				graph.parseJSON(chipArr.chipData);
				
				/* var bbb = JSON.parse(chipArr.chipData)
				console.log("bbb",bbb) */
				//以下保存按钮
				var toolbarDiv = editor.toolbar;
				var button = document.createElement('button');
				button.textContent = '芯片更新';
				toolbarDiv.appendChild(button)
				button.onclick = function () {
					var json = graph.toJSON()
					var chipJsonstr = JSON.stringify(json)
					//console.log("++++",chipArr)
					postMessageParentData.cmd = "submitJSON";
					postMessageParentData.params = chipJsonstr;
					window.parent.postMessage(postMessageParentData,"*")
				}
				/* var graph = editor.graph;
				graph.parseJSON(chipArr.chipData);
				var bbb = JSON.parse(chipArr.chipData)
				console.log("bbb",bbb) */
				graph.moveToCenter();
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

				graph.interactionDispatcher.addListener(function (evt) {
					//拐点编辑结束事件
					if (evt.kind == Q.InteractionEvent.POINT_MOVE_END) {
						var line = evt.data;
						Q.log(evt.point);
						var segment = evt.point.segment;
						segment.points = snapToGrid(segment.points[0], segment.points[1]);
						line.invalidate();
						return;
					}
					//创建图元交互事件类型
					if (evt.kind == Q.InteractionEvent.ELEMENT_CREATED) {
						var node = evt.data;
						if (!(node instanceof Q.Node)) {
							return
						}
						var currentElement = graph.getElement(evt.event);
						if (currentElement && currentElement.name === 'cpu') {
							node.host = currentElement;
						} else {
							graph.removeElement(node);
						}
						var ps = snapToGrid(node.x, node.y);
						node.setLocation(ps[0], ps[1]);

						return;
					}

					//开始移动
					if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
						//console.log("开始移动")
						Q.Node.prototype.addFollower = function (follower) {

						};
						return;
					}
					//网元移动中
					if (evt.kind == Q.InteractionEvent.ELEMENT_MOVING) {
						//console.log("网元移动中")

						return;
					}
					//网元移动完成  
					if (evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END) {
						var datas = evt.datas;
						datas.forEach(function (node) {
							if (!(node instanceof Q.Node) || node instanceof Q.Group) {
								return
							}
							var ps = snapToGrid(node.x, node.y);
							console.log(ps);
							node.setLocation(ps[0], ps[1]);
						});
						//console.log("移动结束");
						return;
					}
				});
				var propertySheet = editor.propertyPane;
				propertySheet.showDefaultProperties = false;

				//自定义属性面板
				propertySheet.getCustomPropertyDefinitions = function (data) {
					var type = data.get('type');
					//这里可以获得当前点击的图元对象
					graph.onclick = function (evt) {
						var data = graph.getElement(evt);
						for (let index in infArr) {
							var inf_uuid = data.get('inf_uuid');
							if (inf_uuid == infArr[index].id) {
								var infName = infArr[index].infName
								var ifRate = infArr[index].ifRate
								var infType = infArr[index].infType
								data.set('infname', infName);
								data.set('infrate', ifRate);
								data.set('inftype', infType);
							}
						};
						for (let index in chipArr) {
							var chipName = chipArr.chipName;
							var coreNum = chipArr.coreNum;
							var memSize = chipArr.memSize;
							console.log(chipName);
							data.set('chipname', chipName);
							data.set('corenum', coreNum);
							data.set('memsize', memSize);
						};
					}

					if (type == 'inf') {
						return {
							group: '接口属性',
							properties: [{
								client: 'infname',
								displayName: '接口名称',
							},
							{
								client: 'infrate',
								displayName: '接口速率'
							},
							{
								client: 'inftype',
								displayName: '接口类型'
							}]
						}
					}

					if (type == 'chip') {
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
							}
							]
						}
					}
				}

			}
		});
}