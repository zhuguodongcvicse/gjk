$(function() {
	$('.graph-editor').graphEditor({
		images: [{
				name: '芯片',
				imageWidth: 30,
				imageHeight: 30,
				root: 'images/',
				images: [{
					image: '芯片.svg',
					properties: {size: {width: 300}},
					properties: {zIndex: 500},
					clientProperties: {
						type: 'port'
					}
				}],
			},
			{
				name: '光纤口',
				root: 'images/',
				images: ['光纤口.svg'],
				imageWidth: 30,
				imageHeight: 30
			},
			{
				name: '网口',
				root: 'images/',
				images: ['网口.svg'],
				imageWidth: 30,
				imageHeight: 30
			},
			{
				name: '圆口',
				root: 'images/',
				images: ['圆口.svg'],
				imageWidth: 30,
				imageHeight: 30
			},
			{
				name: '串口',
				root: 'images/',
				images: ['串口.svg'],
				imageWidth: 30,
				imageHeight: 30
			}

		],

		callback: function(editor) {
			var graph = editor.graph;
			function RectElement(x, y, width, height, radius) {
				Q.doSuperConstructor(this, RectElement);
				this.anchorPosition = Q.Position.LEFT_TOP;
				this.setStyle(Q.Styles.SHAPE_FILL_COLOR, "#EEE");
				this.setStyle(Q.Styles.SHAPE_STROKE, 1);
				this.setStyle(Q.Styles.SHAPE_STROKE_STYLE, '#555');
				this.setStyle(Q.Styles.LABEL_PADDING, 5);
				this.radius = radius;
				this.setBounds(x, y, width, height);
			}

			RectElement.prototype = {
				setBounds: function(x, y, width, height, relativeParent) {
					x = x || 0, y = y || 0;
					if(relativeParent && this.parent) {
						x += this.parent.x, y += this.parent.y;
					}
					this.width = width, this.height = height;
					this.x = x, this.y = y;
					this.image = Q.Shapes.getRect(0, 0, width, height, this.radius || 5);
				},
				getBounds: function() {
					return {
						x: this.x,
						y: this.y,
						width: this.width,
						height: this.height
					}
				}
			}

			Q.extend(RectElement, Q.Node);

			function init() {
				function createNode(options, parent) {
					var item = new RectElement(options.x || 0, options.y || 0, options.width || 40, options.height || 40, options.radius);
					graph.addElement(item);
					item.setStyle(Q.Styles.SHAPE_FILL_COLOR, options.fillColor || '#EEE');
					if(parent) {
						item.parent = item.host = parent;
					}
					return item;
				}

				function getSlot(parent, id) {
					if(parent._slots) {
						//console.log(parent._slots[id]);
						return parent._slots[id];

					}
				}

				function addSlot(parent, id, x, y, width, height, childType) {
					var slot = createNode({
						x: parent.x + x,
						y: parent.y + y,
						width: width,
						height: height,
						radius: 8,
						fillColor: '#BBB'
					}, parent);
					slot.set('type', 'slot');
					slot.zindex=-100;
					slot.set('childType', childType);
					slot.movable = false;
					slot.selectable = false;
					slot.setStyle(Q.Styles.SHAPE_STROKE_STYLE, '#AAA');
					slot.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
					slot.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
					// slot.setStyle()
					slot.name = ' ';
					if(!parent._slots) {
						parent._slots = {};
					}
					return parent._slots[id] = slot;
				}

				/*				function createRack() {
									var width = 700,
										height = 500;
									var rack = createNode({
										width: width,
										height: height,
										radius: 20,
										fillColor: '#DDD'
									});

									var count = 8,
										gap = 10,
										padding = 40;
									var cardWidth = (width - padding - padding + gap) / count - gap,
										cardHeight = height - padding - padding;
									var i = 0;
									while(i++ < count) {
										var x = padding + (i - 1) * (cardWidth + gap),
											y = padding;
										addSlot(rack, i, x, y, cardWidth, cardHeight, 'card');
									}
									return rack;
								}*/

				function createCard() {
					//var bounds = slot.getBounds();
					var width = 70,
						height = 400;
					var card = createNode({
						/*x: bounds.x,
						y: bounds.y,*/
						width: width,
						height: height,
						radius: 8,
						fillColor: '#CDF'
					});
					card.set('type', 'card');
					//card.name = '芯片';
					card.image = 'images/前板卡.svg';
					//card.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_TOP);
					//card.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_TOP);

					var count = 4,
						vGap = 10,
						padding = 70,
						hGap = 5;
					var width = width - hGap - hGap,
						height = (height - padding - padding) / count;
					var i = 0;
					while(i++ < count) {
						var y = padding + (i - 1) * height
						addSlot(card, i, hGap, y, width, height - vGap, 'port');
					}
					return card;
				}

				function createPort(slot) {
					var bounds = slot.getBounds();
					var port = createNode({
						x: bounds.x,
						y: bounds.y,
						width: bounds.width,
						height: bounds.height,
						radius: 10,
						fillColor: '#8F8'
					}, slot);
					port.set('type', 'port');
					//port.name = '001';
					port.zIndex=1000;
					port.image = 'images/芯片.svg';
					//port.size = {width: 60};
					//port.setStyle(Q.Styles.LABEL_ANCHOR_POSITION, Q.Position.CENTER_MIDDLE);
					//port.setStyle(Q.Styles.LABEL_POSITION, Q.Position.CENTER_MIDDLE);
					return port;
				}

				/*			var rack = createRack();*/

				var card = createCard()
				var port1 = createPort(getSlot(card, 1))
				var port2 = createPort(getSlot(card, 2))
			}

			function initInteraction() {
				var currentElement;
				var oldFillColor;
				var highlightColor = "#FF0";

				function unhighlight() {
					if(currentElement) {
						currentElement.setStyle(Q.Styles.SHAPE_FILL_COLOR, oldFillColor);
					}
				}

				function highlight(element) {
					if(currentElement == element) {
						return;
					}
					unhighlight(currentElement);
					currentElement = element;
					if(!currentElement) {
						return;
					}
					oldFillColor = currentElement.getStyle(Q.Styles.SHAPE_FILL_COLOR);
					currentElement.setStyle(Q.Styles.SHAPE_FILL_COLOR, highlightColor);
				}

				function findSlot(element, evt) {
					var type = element.get('type');
					var xy = graph.toLogical(evt.event);

					function canDrop(data) {
						return data.get('type') == 'slot' && data.get('childType') == type;
					}

					var slot;
					graph.forEachReverseVisibleUI(function(ui) {
						if(ui.data == element) {
							return;
						}
						var bounds = graph.getUIBounds(ui);
						if(bounds.intersects(xy.x, xy.y)) {
							if(canDrop(ui.data)) {
								slot = ui.data;
							}
							return false;
						}
					});
					return slot;
				}

				function updateLocation(element, slot) {
					element.parent = slot;
					var bounds = slot.getBounds();
					graph.moveElements([element], bounds.x - element.x, bounds.y - element.y)
				}

				var dragInfo = {};
				graph.interactionDispatcher.addListener(function(evt) {
					var data = evt.data;
					//移动开始
					if(evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
						if(data.parent && data.parent.get('type') == 'slot') {
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
					if(!dragInfo) {
						return
					}
					//移动中
					if(evt.kind == Q.InteractionEvent.ELEMENT_MOVING) {
						var slot = findSlot(data, evt);
						if(!slot) {
							unhighlight();
						} else {
							highlight(slot);
						}
						return;
					}
					//移动结束
					if(evt.kind == Q.InteractionEvent.ELEMENT_MOVE_END) {
						unhighlight();
						var data = dragInfo.data;
						var oldSlot = data.parent;
						var slot = findSlot(data, evt);
						if(!slot || slot == oldSlot) {
							graph.moveElements([data], dragInfo.x - data.x, dragInfo.y - data.y)
						} else {
							updateLocation(data, slot);
						}
						dragInfo = null;

					}
				
				//==============================================
				
				//创建图元交互事件类型
		/*		if(evt.kind == Q.InteractionEvent.ELEMENT_CREATED) {
							var node = evt.data;
							if(!(node instanceof Q.Node)) {
								return
							}
							var currentElement = graph.getElement(evt.event);
							if(currentElement && currentElement.name === ' '){
								node.host =  currentElement;
							}else{
								graph.removeElement(node);
							}
							return;
				}*/

/*				//开始移动
				if(evt.kind == Q.InteractionEvent.ELEMENT_MOVE_START) {
					console.log("开始移动")
					Q.Node.prototype.addFollower = function(follower) {
						alert(156161);
					};
					return;
				}
				//网元移动中
				if(evt.kind == Q.InteractionEvent.ELEMENT_MOVING) {
					console.log("网元移动中")

					return;
				}*/
	
				})
			}

			init();
			initInteraction();
			graph.moveToCenter();
			//以下保存按钮
			var toolbarDiv = editor.toolbar;
			var button = document.createElement('button');
			button.textContent = '芯片保存';
			toolbarDiv.appendChild(button)
			button.onclick = function() {
				alert("保存");
			}
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

		
			var propertySheet = editor.propertyPane;
			propertySheet.showDefaultProperties = false;

			//自定义属性面板
			propertySheet.getCustomPropertyDefinitions = function(data) {

				console.log(data);
				var zindex= data.get('zIndex');
				console.log(zindex);
				var type = data.get('type');
				if(type == 'inf') {
					return {
						group: '接口属性',
						properties: [{
								client: 'a',
								displayName: '接口名称'
							},
							{
								client: 'b',
								displayName: '接口速率'
							},
							{
								client: 'c',
								displayName: '接口类型'
							}
						]

					}

				}
				if(type == 'chip') {
					return {
						group: '芯片属性',
						properties: [{
								client: 'a',
								displayName: '芯片名称'
							},
							{
								client: 'b',
								displayName: '内核数量'
							},
							{
								client: 'c',
								displayName: '内存大小'
							}
						]
					}
				}
			}

		}
	});
});