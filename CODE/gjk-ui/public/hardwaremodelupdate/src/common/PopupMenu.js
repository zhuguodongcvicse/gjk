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
            console.log("data",data)
           if(data.image === "rack" || data.name === " "){
            if(true){
              graph.removeElement(data);
              //删除逻辑
              for (const i in graphList.bJson) {
                if (graphList.bJson[i].datas[0].json.properties.uniqueId === data.properties.uniqueId) {
                  removeByValue(caseList, caseList[i])
                  removeByValue(graphList.fJson, graphList.fJson[i])
                  removeByValue(graphList.bJson, graphList.bJson[i])
                }
              }
              if (linkList.length != 0) {
                for (let i = 0; i < linkList.length; i++) {
                  if (linkList[i] != null && linkList[i][0].json.properties.uniqueId.indexOf(data.properties.uniqueId) !== -1) {
                    removeByValue(linkList, linkList[i])
                  }
                  if (linkList[i] != null && linkList[i][1].json.properties.uniqueId.indexOf(data.properties.uniqueId) !== -1) {
                    removeByValue(linkList, linkList[i])
                  }
                }
              }
              if (linkGraphList.datas.length !== 0) {
                for (let i = 0; i < linkGraphList.datas.length; i++) {
                  if ((i + 1) % 3 === 0) {
                    if (linkGraphList.datas[i - 1].json.properties.uniqueId !== undefined && linkGraphList.datas[i - 2].json.properties.uniqueId === data.$from._mg0.uniqueId) {
                      linkGraphList.datas.splice(i - 2, 3)
                      i = i - 2
                    }
                    /*if (linkGraphList.datas[i - 1].json.properties.uniqueId !== undefined && linkGraphList.datas[i - 1].json.properties.uniqueId === data.$to.properties.uniqueId) {
                      linkGraphList.datas.splice(i - 2, 3)
                      i = i - 2
                    }*/
                  }
                }
                /*for (const i in linkGraphList.datas) {
                  if (linkGraphList.datas[i]._className === "Q.RectElement" && linkGraphList.datas[i].json.properties.uniqueId.indexOf(data.properties.uniqueId) !== -1) {
                    for (const j in linkGraphList.datas) {
                      if (linkGraphList.datas[j]._className === "Q.Edge") {
                        if (linkGraphList.datas[j].json.from._ref == linkGraphList.datas[i]._refId) {
                          for (const k in linkGraphList.datas) {
                            if (linkGraphList.datas[k]._className === "Q.RectElement" && linkGraphList.datas[j].json.to._ref == linkGraphList.datas[k]._refId) {
                              linkGraphList.datas[k].waitDelete = 0
                            }
                          }
                          linkGraphList.datas[j].waitDelete = 0
                        }
                        if (linkGraphList.datas[j].json.to._ref == linkGraphList.datas[i]._refId) {
                          for (const k in linkGraphList.datas) {
                            if (linkGraphList.datas[k]._className == "Q.RectElement" && linkGraphList.datas[j].json.from._ref == linkGraphList.datas[k]._refId) {
                              linkGraphList.datas[k].waitDelete = 0
                            }
                          }
                          linkGraphList.datas[j].waitDelete = 0
                        }
                      }
                    }
                    linkGraphList.datas[i].waitDelete = 0
                  }
                }*/
              }
              /*for (let i = 0; i < linkGraphList.datas.length; i++) {
                if (linkGraphList.datas[i].waitDelete != null) {
                  removeByValue(linkGraphList.datas, linkGraphList.datas[i])
                  i--
                }
              }*/
              linkMap.forEach(function (value, key) {
                if (key.uniqueId.indexOf(data.properties.uniqueId) !== -1 || data.$from.properties.uniqueId === key.uniqueId) {
                  linkMap.delete(key)
                }
                if (value.uniqueId.indexOf(data.properties.uniqueId) !== -1 || data.$to.properties.uniqueId === key.uniqueId) {
                  linkMap.delete(value)
                }
              });
              //第一次切换回正面
              if (typeof backAllCaseJsonTemp != 'undefined' && backAllCaseJsonTemp.datas.length !== 0) {
                for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
                  if (backAllCaseJsonTemp.datas[i].json.properties == null || backAllCaseJsonTemp.datas[i]._className === "Q.Edge") {
                    removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
                    i--
                  }
                }
              }
              if (typeof backAllCaseJsonTemp != 'undefined' && backAllCaseJsonTemp.datas.length !== 0) {
                for (let i = 0; i < backAllCaseJsonTemp.datas.length; i++) {
                  if (backAllCaseJsonTemp.datas[i].json.properties != null && backAllCaseJsonTemp.datas[i].json.properties.infName != null
                    && backAllCaseJsonTemp.datas[i].json.properties.uniqueId.indexOf(data.properties.uniqueId) !== -1) {
                    removeByValue(backAllCaseJsonTemp.datas, backAllCaseJsonTemp.datas[i])
                    i--
                  }
                }
              }
              caseID--
              //删除map中存在的ip
              checkIPMap.forEach((value, key) => {
                if (key.indexOf(data.properties.uniqueId) !== -1) {
                  checkIPMap.delete(key)
                }
              })
            }
          }
         } });

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
