/**
 * 右侧配置界面可拖动
 */
/* var theobject = null; //This gets a value as soon as a resize start

function resizeObject() {
    this.el = null; //pointer to the object
    this.dir = "";      //type of current resize (n, s, e, w, ne, nw, se, sw)
    this.grabx = null;     //Some useful values
    this.graby = null;
    this.width = null;
    this.height = null;
    this.left = null;
    this.top = null;
}

//Find out what kind of resize! Return a string inlcluding the directions
function getDirection(el) {
    // console.log("getDirection")
    var xPos, yPos, offset, dir;
    dir = "";

    xPos = window.event.offsetX;
    yPos = window.event.offsetY;

    offset = 8; //The distance from the edge in pixels

    if (yPos < offset) dir += "n";
    else if (yPos > el.offsetHeight - offset) dir += "s";
    if (xPos < offset) dir += "w";
    else if (xPos > el.offsetWidth - offset) dir += "e";

    return dir;
}

function doDown() {
    // console.log("doDown")
    // console.log("event",event)
    var el = getReal(event.srcElement, "className", "graph-editor__property");
    // console.log("el",el)

    if (el == null) {
        theobject = null;
        return;
    }

    dir = getDirection(el);
    if (dir == "") return;

    theobject = new resizeObject();
    // console.log("theobject",theobject)
    theobject.el = el;
    theobject.dir = dir;

    theobject.grabx = window.event.clientX;
    theobject.graby = window.event.clientY;
    theobject.width = el.offsetWidth;
    theobject.height = el.offsetHeight;
    theobject.left = el.offsetLeft;
    theobject.top = el.offsetTop;

    window.event.returnValue = false;
    window.event.cancelBubble = true;
}

function doUp() {
    // console.log("doUp")
    if (theobject != null) {
        theobject = null;
    }
}

function doMove() {
    var el, xPos, yPos, str, xMin, yMin;
    xMin = 8; //The smallest width possible
    yMin = 8; //             height

    el = getReal(event.srcElement, "className", "graph-editor__property");
    // console.log("el",el)
    if (el.className == "graph-editor__property") {
        str = getDirection(el);
        //Fix the cursor 
        if (str == "") str = "default";
        else str += "-resize";
        el.style.cursor = str;
    }

    //Dragging starts here
    if (theobject != null) {
        // console.log("theobject",theobject)
        if (dir.indexOf("e") != -1)
            theobject.el.style.width = Math.max(xMin, theobject.width + window.event.clientX - theobject.grabx) + "px";

        if (dir.indexOf("s") != -1)
            theobject.el.style.height = Math.max(yMin, theobject.height + window.event.clientY - theobject.graby) + "px";

        if (dir.indexOf("w") != -1) {
            theobject.el.style.left = Math.min(theobject.left + window.event.clientX - theobject.grabx, theobject.left + theobject.width - xMin) + "px";
            theobject.el.style.width = Math.max(xMin, theobject.width - window.event.clientX + theobject.grabx) + "px";
        }
        if (dir.indexOf("n") != -1) {
            theobject.el.style.top = Math.min(theobject.top + window.event.clientY - theobject.graby, theobject.top + theobject.height - yMin) + "px";
            theobject.el.style.height = Math.max(yMin, theobject.height - window.event.clientY + theobject.graby) + "px";
        }

        window.event.returnValue = false;
        window.event.cancelBubble = true;
    }
} */

/* function getReal(el, type, value) {
    temp = el;
    while ((temp != null) && (temp.tagName != "BODY")) {
        if (eval("temp." + type) == value) {
            el = temp;
            return el;
        }
        temp = temp.parentNode;
    }
    return temp;
}

document.onmousedown = doDown;
document.onmouseup = doUp;
document.onmousemove = doMove; */

/**
 * 表删除行
 * @param {*} obj 
 */
//得到行对象 
function getRowObj(obj) {
    var i = 0;
    while (obj.tagName.toLowerCase() != "tr") {
        obj = obj.parentNode;
        if (obj.tagName.toLowerCase() == "table") return null;
    }
    return obj;
}

//根据得到的行对象得到所在的行数 
function getRowNo(obj) {
    var trObj = getRowObj(obj);
    var trArr = trObj.parentNode.children;
    for (var trNo = 0; trNo < trArr.length; trNo++) {
        if (trObj == trObj.parentNode.children[trNo]) {
            alert(trNo + 1);
        }
    }
}

/**
 * 删除数组中某元素
 */
function removeByValue(arr, val) {
    for (var i = 0; i < arr.length; i++) {
        if (arr[i] == val) {
            arr.splice(i, 1);
            break;
        }
    }
}

/**
 * 截取字符串
 */

function getCaption(obj) {
    var index = obj.lastIndexOf("\+");
    obj = obj.substring(index + 1, obj.length);
    //  console.log(obj);
    return obj;
}