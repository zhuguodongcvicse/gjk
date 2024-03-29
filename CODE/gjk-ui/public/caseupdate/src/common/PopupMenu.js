;(function (Q) {
  //PopupMenu
    function showDivAt(div, x, y) {
      var body = document.documentElement;
      var bounds = new Q.Rect(window.pageXOffset, window.pageYOffset, body.clientWidth - 2, body.clientHeight - 2);
      var width = div.offsetWidth;
      var height = div.offsetHeight;

      if (x + width > bounds.x + bounds.width) {
        x = bounds.x + bounds.width - width;
      }
      if (y + height > bounds.y + bounds.height) {
        y = bounds.y + bounds.height - height;
      }
      if (x < bounds.x) {
        x = bounds.x;
      }
      if (y < bounds.y) {
        y = bounds.y;
      }
      div.style.left = x + 'px';
      div.style.top = y + 'px';
    }

    function isDescendant(parent, child) {
      var node = child.parentNode;
      while (node != null) {
        if (node == parent) {
          return true;
        }
        node = node.parentNode;
      }
      return false;
    }

    var PopupMenu = function (items) {
      this.items = items || [];
    }

    var menuClassName = 'dropdown-menu';
    PopupMenu.Separator = 'divider';

    PopupMenu.prototype = {
      dom: null,
      _invalidateFlag: true,
      add: function (item) {
        this.items.push(item);
        this._invalidateFlag = true;
      },
      addSeparator: function () {
        this.add(PopupMenu.Separator);
      },
      showAt: function (x, y) {
        if (!this.items || !this.items.length) {
          return false;
        }
        if (this._invalidateFlag) {
          this.render();
        }
        this.dom.style.display = "block";
        document.body.appendChild(this.dom);
        showDivAt(this.dom, x, y);
      },
      hide: function () {
        if (this.dom && this.dom.parentNode) {
          this.dom.parentNode.removeChild(this.dom);
        }
      },
      isShowing: function(){
        return this.dom.parentNode !== null;
      },
      render: function () {
        this._invalidateFlag = false;
        if (!this.dom) {
          this.dom = document.createElement('ul');
          this.dom.setAttribute("role", "menu");
          this.dom.className = menuClassName;
          var startEventName = Q.isTouchSupport ? "touchstart" : "mousedown";

          if (!this.stopEditWhenClickOnWindow) {
            this.stopEditWhenClickOnWindow = function (evt) {
              if (this.isShowing() && !isDescendant(this.dom, evt.target)) {
                this.hide();
              }
            }.bind(this)
          }
          window.addEventListener("mousedown", this.stopEditWhenClickOnWindow, true);
          this.dom.addEventListener(startEventName, function (evt) {
            Q.stopEvent(evt);
          }, false);
        } else {
          this.dom.innerHTML = "";
        }
        for (var i = 0, l = this.items.length; i < l; i++) {
          var item = this.renderItem(this.items[i]);
          this.dom.appendChild(item);
        }
      },
      html2Escape: function (sHtml) {
        return sHtml.replace(/[<>&"]/g, function (c) {
          return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c];
        });
      },
      renderItem: function (menuItem, zIndex) {
        var dom = document.createElement('li');
        dom.setAttribute("role", "presentation");
        if (menuItem == PopupMenu.Separator) {
          dom.className = PopupMenu.Separator;
          dom.innerHTML = " ";
          return dom;
        }
        if (Q.isString(menuItem)) {
          dom.innerHTML = '<a role="menuitem" class="menu_14s" tabindex="-1" href="#">' + this.html2Escape(menuItem) + '</a>';
          return dom;
        }
        if (menuItem.selected) {
          dom.style.backgroundPosition = '3px 5px';
          dom.style.backgroundRepeat = 'no-repeat';
          dom.style.backgroundImage = "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAPklEQVQ4y2P4//8/AyWYYdQA7AYAAZuamlo7ED+H4naQGNEGQDX/R8PtpBjwHIsBz+lqAGVeoDgQR1MiaRgAnxW7Q0QEK0cAAAAASUVORK5CYII=')";
        }
        var a = document.createElement("a");
        a.setAttribute("role", "menuitem");
        a.setAttribute("tabindex", "-1");
        a.setAttribute("href", "javascript:void(0)");
        dom.appendChild(a);

        if (menuItem.html) {
          a.innerHTML = menuItem.html;
        } else {
          var text = menuItem.text || menuItem.name;
          if (text) {
            a.innerHTML = this.html2Escape(text);
          }
        }
        var className = menuItem.className;
        if (className) {
          dom.className = className;
        }
        var call = menuItem.action;
        var self = this;

        var onclick = function (evt) {
          if (call) {
            call.call(menuItem.scope, evt, menuItem);
          }
          if (!Q.isIOS) {
            evt.target.focus();
          }
          setTimeout(function () {
            self.hide();
          }, 100);
        };
        if (Q.isTouchSupport) {
  //            dom.ontouchstart = onclick;
          a.ontouchstart = onclick;
        } else {
          dom.onclick = onclick;
        }
        return dom;
      },
      getMenuItems: function(graph, data, evt){
        var items = [];
        //items.push({
        //  text: '添加主机',
        //  action: function(evt, item){
        //    alert('添加主机');//这里实现弹出页面
        //  }
        //})
        if (data) {
          var isShapeNode = data instanceof Q.ShapeNode;
          var isGroup = data instanceof Q.Group;
          var isNode = !isShapeNode && data instanceof Q.Node;
          var isEdge = data instanceof Q.Edge;

   /*        items.push({
            text: getI18NString('Rename'), action: function (evt, item) {
              Q.prompt(getI18NString('Input Element Name'), data.name || '', function (name) {
                if (name === null) {
                  return;
                }
                data.name = name;
              })
            }
          }); */
          if (isEdge) {
            var isDashLine = data.getStyle(Q.Styles.EDGE_LINE_DASH) || Q.DefaultStyles[Q.Styles.EDGE_LINE_DASH];
            items.push({
              text: isDashLine ? getI18NString('Solid Line') : getI18NString('Dashed Line'), action: function (evt, item) {
                data.setStyle(Q.Styles.EDGE_LINE_DASH, isDashLine ? null : [5, 3]);
              }
            });
            items.push({
              text: getI18NString('Line Width'), action: function (evt, item) {
                Q.prompt(getI18NString('Input Line Width'), data.getStyle(Q.Styles.EDGE_WIDTH) || Q.DefaultStyles[Q.Styles.EDGE_WIDTH], function (lineWidth) {
                  if (lineWidth === null) {
                    return;
                  }
                  lineWidth = parseFloat(lineWidth);
                  data.setStyle(Q.Styles.EDGE_WIDTH, lineWidth);
                })
              }
            });
            items.push({
              text: getI18NString('Line Color'), action: function (evt, item) {
                Q.prompt(getI18NString('Input Line Color'), data.getStyle(Q.Styles.EDGE_COLOR) || Q.DefaultStyles[Q.Styles.EDGE_COLOR], function (color) {
                  if (color === null) {
                    return;
                  }
                  data.setStyle(Q.Styles.EDGE_COLOR, color);
                })
              }
            });
          } else if (data.parent instanceof Q.Group) {
            items.push({
              text: getI18NString('Out of Group'), action: function () {
                data.parent = null;
              }
            })
          }
        //  items.push(Q.PopupMenu.Separator);
  /*         items.push({
            text: getI18NString('Send to Top'), action: function (evt, item) {
              data.zIndex = 1;
              graph.sendToTop(data);
              graph.invalidate();
            }
          }); */
      /*     items.push({
            text: getI18NString('Send to Bottom'), action: function (evt, item) {
              data.zIndex = -1;
              graph.sendToBottom(data);
              graph.invalidate();
            }
          }); */
       /*    items.push({
            text: getI18NString('Reset Layer'), action: function (evt, item) {
              data.zIndex = 0;
              graph.invalidate();
            }
          }); */
         // items.push(Q.PopupMenu.Separator);
        }
     /*    items.push({
          text: getI18NString('Clear Graph'), action: function () {
            graph.clear();
          }
        }) */
     //   items.push(Q.PopupMenu.Separator);

        items.push({
          text: getI18NString('Zoom In'), action: function (evt, item) {
            var localXY = graph.globalToLocal(evt);
            graph.zoomIn(localXY.x, localXY.y, true);
          }
        });
        items.push({
          text: getI18NString('Zoom Out'), action: function (evt, item) {
            var localXY = graph.globalToLocal(evt);
            graph.zoomOut(localXY.x, localXY.y, true);
          }
        });
        items.push({
          text: getI18NString('1:1'), action: function (evt, item) {
            var localXY = graph.globalToLocal(evt);
            graph.scale = 1;
          }
        });
        items.push(Q.PopupMenu.Separator);
        var currentMode = graph.interactionMode;
        var interactons = [
          {text: getI18NString('Pan Mode'), value: Q.Consts.INTERACTION_MODE_DEFAULT},
          {text: getI18NString('Rectangle Select'), value: Q.Consts.INTERACTION_MODE_SELECTION}
        ];
        for (var i = 0, l = interactons.length; i < l; i++) {
          var mode = interactons[i];
          if (mode.value == currentMode) {
            mode.selected = true;
          }
          mode.action = function (evt, item) {
            graph.interactionMode = item.value;
          };
          items.push(mode)
        }
        items.push({
          text: '删除', action: function (evt, item) {
            if(data.image === 'images/BeforeTheBoard.svg' || data.image === 'images/AfterTheBoard.svg' || data.name == " "){
              graph.removeElement(data);
              //删除逻辑
              boardNum--
              //如果是板卡0或者1
              if (data.properties.boardtype == 0 || data.properties.boardType == 1) {
                //移除fBoardList数组中的板卡
                for (const i in fBoardList) {
                  if (fBoardList[i].uniqueId.indexOf(data.properties.uniqueId) != -1) {
                    removeByValue(fBoardList, fBoardList[i])
                  }
                }
                //移除allInfOfFrontBoard数组中此板卡上所有芯片的接口
                for (const i in data.properties.chipList) {
                  for (const j in data.properties.chipList[i].infOfChipList) {
                    for (const k in allInfOfFrontBoard) {
                      if (data.properties.chipList[i].infOfChipList[j].uniqueId.indexOf(allInfOfFrontBoard[k]) != -1) {
                        removeByValue(allInfOfFrontBoard, allInfOfFrontBoard[k])
                      }
                    }
                  }
                }
                //向allInfOfBackBoardt数组中补充添加此板卡对应后板卡上的接口
                if (typeof (bJson) != "undefined") {
                  var oppositeBoardTemp
                  for (const i in bJson.datas) {
                    if (bJson.datas[i].json.properties != null && bJson.datas[i].json.properties.slotNum == fSlotNum + 10) {
                      for (const j in bJson.datas) {
                        if (bJson.datas[j].json.properties != null && bJson.datas[j].json.properties.boardType != null && bJson.datas[j].json.host._ref == bJson.datas[i]._refId) {
                          //console.log("thisboard",bJson.datas[j].json.properties)
                          oppositeBoardTemp = bJson.datas[j]
                        }
                      }
                    }
                  }
                }
                // console.log("oppositeBoardTemp",oppositeBoardTemp)
                for (const i in replenishBackBoardInfList) {
                  if (replenishBackBoardInfList[i].uniqueId.indexOf(oppositeBoardTemp.json.properties.uniqueId) != -1) {
                    allInfOfBackBoard.push(replenishBackBoardInfList[i])
                  }
                }
              }

              //如果是后面机箱的板卡
              if (data.properties.boardType == 3) {
                //移除bBoardList数组中此板卡
                for (const i in bBoardList) {
                  if (bBoardList[i].uniqueId.indexOf(data.properties.uniqueId) != -1) {
                    removeByValue(bBoardList, bBoardList[i])
                  }
                }
                //向allInfOfFrontBoard数组中补充添加此板卡对应卡槽位置的前板卡上的接口
                for (const i in replenishBackBoardInfList) {
                  if (replenishBackBoardInfList[i].uniqueId.indexOf(data.properties.uniqueId) != -1) {
                    allInfOfFrontBoard.push(replenishCpuInfList[i])
                    //移除outLinkArr中的外部互联关系
                    for (const j in clickBoardList) {
                      if (replenishCpuInfList[i].uniqueId.indexOf(clickBoardList[j]) != -1) {
                        clickBoardList[j].outLineArr = []
                      }
                    }
                  }
                }
              }
              //移除点击板卡数组中的此板卡
              for (const i in clickBoardList) {
                if (clickBoardList[i].uniqueId.indexOf(data.properties.uniqueId) != -1) {
                  removeByValue(clickBoardList, clickBoardList[i])
                }
              }
            }

          }
        });

        return items;
      }
    }
    Object.defineProperties(PopupMenu.prototype, {
      items: {
        get: function () {
          return this._items;
        },
        set: function (v) {
          this._items = v;
          this._invalidateFlag = true;
        }
      }
    });

    var _contextmenuListener = {
      onstart: function (evt, graph) {
        graph._popupmenu.hide();
      }
    }
    function getPageXY(evt) {
      if (evt.touches && evt.touches.length) {
        evt = evt.touches[0];
      }
      return {x: evt.pageX, y: evt.pageY};
    }

    function showMenu(evt, graph) {
      var menu = graph.popupmenu;
      var xy = getPageXY(evt);
      var x = xy.x, y = xy.y;

      var items = menu.getMenuItems(graph, graph.getElement(evt), evt);

      if(!items){
        return;
      }
      menu.items = items;
      menu.showAt(x, y);

      Q.stopEvent(evt);
    }
    if(Q.isTouchSupport){
      _contextmenuListener.onlongpress = function (evt, graph) {
        showMenu(evt, graph);
      }
    }

    Object.defineProperties(Q.Graph.prototype, {
      popupmenu: {
        get: function(){
          return this._popupmenu;
        },
        set: function(v){
          if(this._popupmenu == v){
            return;
          }
          this._popupmenu = v;

          if(!this._contextmenuListener){
            this._contextmenuListener = _contextmenuListener;
            this.addCustomInteraction(this._contextmenuListener);
            this.html.oncontextmenu = function (evt) {
              if(!this.popupmenu){
                return;
              }
              showMenu(evt, this);
            }.bind(this);
          }
        }
      }
    });
    Q.PopupMenu = PopupMenu;
  })(Q, jQuery);
