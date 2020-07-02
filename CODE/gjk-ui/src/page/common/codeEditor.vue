<template>
  <el-row>
    <el-col :span="24">
      <div ref="editorDiv" style="margin-bottom:10px">
        <el-form :model="editorForm" inline="inline">
          <el-form-item>
            <el-button type="primary" icon="el-icon-thirdsave" size="mini" @click.native="save">保存</el-button>
          </el-form-item>
          <el-form-item label="编码格式：">
            <el-select
              v-model="editorForm.editorData"
              placeholder="请选择编码格式(默认UTF-8)"
              @change="editorChange"
              style="width:300px"
            >
              <el-option label="UTF-8" value="UTF-8"></el-option>
              <el-option label="Unicode" value="Unicode"></el-option>
              <el-option label="UTF-16BE" value="UTF-16BE"></el-option>
              <el-option label="GBK" value="GBK"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div class="code-editor-container">
        <!-- 程序文本编辑器 -->
<!--        <monaco-editor ref="test" v-model="textContexts" language="c" v-bind:style="editorClass"></monaco-editor>-->
        <monaco-editor ref="monacoEditor"
                       v-bind:style="con"
                       v-model="textContexts"
                       :language="language"
                       @change="monacoEditorChange"
                       @position="monacoEdiorPosition"
                       @editorDidMount="editorDidMount"
                       :options="options">
        </monaco-editor>
      </div>
    </el-col>
  </el-row>
</template>

<script>
// 程序文本编辑器
import MonacoEditor from "vue-monaco";
//  引入文本编辑器上方简单的一些操作按钮
import Toolbar from "@/page/components/code-editor/components/toolbar";
import { saveFileContext } from "@/api/libs/threelibs";
import { readAlgorithmfile } from "@/api/libs/threelibs";

// 与后端的交互api
//import { getProgram, saveProgram } from '@/api/vdp/component'
export default {
  name: "code-editor",
  components: { MonacoEditor, Toolbar },
  props: ["textContext", "tFilePath"],
  //生命周期 - 创建完成（可以访问当前this实例）
  created() {
     window.addEventListener('resize', this.getHeight);
        this.getHeight()

        
    let _this = this;
    this.$nextTick(r => {
      let width = _this.$refs.editorDiv.offsetWidth;
       console.log(" _this.$refs.editorDiv.$el;", width);

    let lengthData =   document.getElementsByClassName("ivu-split-pane right-pane")
   
    console.log("++++++++++++++++++++++",  lengthData[0].clientWidth)
      //  
      // this.editorClass.width = width + "px";
      // this.$refs.monacoEditor.$el.style.height = "calc(";
      _this.editorClass = { width: lengthData[0].clientWidth-200+"px", height: lengthData[0].clientheight+"px" };
      // console.log("monacoEditor", _this.$refs.monacoEditor.$el.offsetHeight);
    });
  },
  watch: {
    textContext: {
      immediate: true,
      handler: function() {
        //为最新内容时下拉选为空
        this.editorForm.editorData = "";
        this.textContexts = this.textContext;
        this.prefix = this.tFilePath.substring(this.tFilePath.lastIndexOf(".") + 1).toLowerCase();
      },
      deep: true
    },
    prefix: {
        handler: function () {
            console.log(this.prefix)
            if(this.prefix == 'c' || this.prefix == 'h') {
                this.language = 'c'
            }else if(this.prefix == 'js'){
                this.language = 'javascript'
            }else{
                this.language = this.prefix
            }
        },
        deep: true
    }
  },
  data() {
    return {
      con:{
                height:'',
                width:''
            },
      isChange: false,
      editor: null,
      prefix: '',
      language: 'c',
      tagName: '',
      tagNameNew: '',
      lineNumber: 0,
      columnNumber: 0,
      lastPosition: null,
      editorClass: {
        height: "900px"
      },

      editorForm: {
        editorData: ""
      },
      threeLibsFilePathDTO: {},
      textContexts: "",
      code: "const noop = () => {}",
      options: {
        //readOnly: false,
        //编辑器随浏览器窗口自动调整大小
        automaticLayout: true,
        glyphMargin: true
        // 自动缩进
        //autoIndent: true
      }
    };
  },

  methods: {
     getHeight(){
          this.con.height=window.innerHeight-170+'px';
           this.con.width=window.innerWidth-170+'px';
       },
    editorChange() {
      console.log("ppppppp");
      //文件内容
      this.threeLibsFilePathDTO.filePathName = this.tFilePath;
      this.threeLibsFilePathDTO.code = this.editorForm.editorData;
      console.log(this.threeLibsFilePathDTO);
      readAlgorithmfile(this.threeLibsFilePathDTO).then(response => {
        console.log("arrays:::", response);
        //文件内容
        this.textContexts = response.data.data.textContext.split(
          "@%#@*+-+@"
        )[1];
      });
    },
    save() {
      this.threeLibsFilePathDTO.filePath = this.tFilePath;
      this.threeLibsFilePathDTO.filePathName = this.textContexts;
      saveFileContext(this.threeLibsFilePathDTO)
        .then(res => {
          this.$message({
            type: "success",
            message: "保存成功！"
          });
        })
        .catch(err => {
          console.log("err: ", err);
        });
    },
    editorDidMount(editor){
        console.log('editorDidMount::', editor)
        this.editor = editor
    },
    // 内容变化事件
    monacoEditorChange(data, e){
        // console.log('光标坐标： ',this.editor.getPosition().lineNumber, this.editor.getPosition().column)
        // console.log('新内容：'+ this.textContexts)
        console.log('monacoEditorChange')
        // 录入内容是空格 不做处理
        if(e.changes[0].text != ' '){
            this.isChange = true
        }

        // this.lineNumber = this.editor.getPosition().lineNumber
        // this.columnNumber = this.editor.getPosition().column



        // console.log(this.lineNumber, this.columnNumber)

        // this.editor.revealPosition({ lineNumber: 52, column: 33 })
        // this.$refs.monacoEditor.getMonaco().revealPosition({ lineNumber: 2, column: 3 })
        // this.editor.getPosition().setEndPosition(this.lineNumber, this.columnNumber)
        // 添加修饰
        // var decorations = this.editor.deltaDecorations([], [
        //     {
        //         range: new monaco.Range(1,1,1,1),
        //         options: {
        //             isWholeLine: true,
        //             className: 'myContentClass',
        //             glyphMarginClassName: 'myGlyphMarginClass'
        //         }
        //     },
        //     {
        //         range: new monaco.Range(3,1,3,1),
        //         options: {
        //             isWholeLine: true,
        //             className: 'myContentClass',
        //             glyphMarginClassName: 'myGlyphMarginClass'
        //         }
        //     }
        // ]);
        // console.log('====='+ data)
        // this.textContexts = data
    },
    xmlTagAutoComplementHandle(){
        if(this.prefix != 'xml' && this.prefix != 'html'){
            return this.textContexts
        }

        let tagNameNew = this.getXmlTagNameByPosition(this.textContexts)
        let textTmp = this.textContexts

        console.log('tagName::[' + this.tagName + '] -> [' + tagNameNew + ']')
        let positionIndex = this.getPositionIndex()
        // 修改的是开始标签处理
        if(this.tagName.indexOf('/') == -1){
            // 截取光标后面的内容
            let textSub = this.textContexts.substring(positionIndex - 1, this.textContexts.length)

            // 结束标签是/>不进行替换处理
            if(textSub.indexOf('/>') < textSub.indexOf('>')){
                return this.textContexts
            }

            let nameOld = '</' + this.tagName + '>'
            let nameNew = '</' + tagNameNew + '>'
            // 统计有几个结束标签
            let count = (textSub.split(nameOld)).length - 1
            if(count <= 1){
                // 只有一个时直接替换
                textSub = textSub.replace(nameOld, nameNew)
            }
            // 多个结束标签处理
            else{
                let tagi1 = textSub.indexOf(nameOld)
                let tagi2 = textSub.indexOf('<' + this.tagName + '>')
                let tagi3 = textSub.indexOf('<' + this.tagName + ' ')
                // 标签是最内层直接替换
                if((tagi2 == -1 && tagi3 == -1) ||
                    (tagi3 != -1 && tagi2 != -1 && tagi1 < tagi2 && tagi1 < tagi3) ||
                    (tagi3 != -1 && tagi2 == -1 && tagi1 < tagi3) ||
                    (tagi3 == -1 && tagi2 != -1 && tagi1 < tagi2)){
                    textSub = textSub.replace(nameOld, nameNew)

                }else{
                    let textArr = textSub.split('\r\n');
                    let tagCount = 0
                    let tagNameTmp = ""
                    for(let i = 0; i < textArr.length; i++){
                        let line = textArr[i]
                        // console.log(line)

                        // 获取当前行里的标签名
                        if(line.indexOf("<") > -1){
                            let ii1 = line.indexOf(">")
                            let ii2 = line.indexOf(" ")
                            if(ii1 == -1) ii1 = ii2
                            tagNameTmp = line.substring(line.indexOf("<"), ii1)
                        }

                        let i1 = line.indexOf('<' + this.tagName + '>')
                        let i2 = line.indexOf('<' + this.tagName + ' ')
                        let i3 = line.indexOf('/>')
                        let i4 = line.indexOf('>')
                        let i5 = line.indexOf('</' + this.tagName + '>')

                        // 过滤无关内容
                        if(i1 == -1 && i2 == -1 && i3 == -1 && i5 == -1){
                            continue
                        }

                        if(i1 > -1 || i2 > -1){
                            tagCount ++
                        }
                        if(i3 != -1 && i3 < i4 && tagCount > 0 && (tagNameTmp == '<' + this.tagName + '>' || tagNameTmp == '<' + this.tagName + ' ')){
                            tagCount --
                        }
                        // console.log(i1, i2,i3,i4,i5, tagCount)

                        if(i5 > -1 && tagCount == 0 && i1 == -1 && i2 == -1){
                            textArr[i] = textArr[i].replace(nameOld, nameNew)
                            break
                        }else if(i5 > -1){
                            tagCount --
                        }
                        if(tagCount < 0) tagCount = 0
                    }
                    textSub = textArr.join("\r\n")
                }
            }
            textTmp = this.textContexts.substring(0, positionIndex - 1) + textSub
        }

        // 修改的是结束标签处理
        else{
            let textSub = this.textContexts.substring(0, positionIndex - 1)

            let nameOld1 = '<' + this.tagName.replace('/', '') + '>'
            let nameOld2 = '<' + this.tagName.replace('/', '') + ' '
            let nameNew1 = '<' + tagNameNew.replace('/', '') + '>'
            let nameNew2 = '<' + tagNameNew.replace('/', '') + ' '

            // 统计有几个开始标签
            let count1 = (textSub.split(nameOld1)).length - 1
            let count2 = (textSub.split(nameOld2)).length - 1

            textSub = this.xmlEndTagUpdateHandle(textSub, count1, nameOld1, nameNew1)
            textSub = this.xmlEndTagUpdateHandle(textSub, count2, nameOld2, nameNew2)

            textTmp = textSub + this.textContexts.substring(positionIndex - 1, this.textContexts.length)
        }
        return textTmp
    },
    xmlEndTagUpdateHandle(textSub, count1, nameOld1, nameNew1){
        if(count1 <= 1){
            // 只有一个时直接替换
            textSub = textSub.replace(nameOld1, nameNew1)
        }
        // 多个开始标签处理
        else{
            let textArr = textSub.split('\r\n');
            let tagi1 = textSub.lastIndexOf(nameOld1)
            let tagi2 = textSub.lastIndexOf('<' + this.tagName + '>')
            // 标签是最内层直接替换
            if((tagi2 != -1 && tagi1 > tagi2) || tagi2 == -1){
                for(let i = textArr.length - 1; i >= 0; i--){
                    if(textArr[i].indexOf(nameOld1) != -1){
                        textArr[i] = textArr[i].replace(nameOld1, nameNew1)
                        break
                    }
                }

            }else{
                let tagCount = 0
                for(let i = textArr.length - 1; i >= 0; i--){
                    let line = textArr[i]
                    console.log(line)

                    let tmpName = this.tagName.replace('/', '')
                    let i1 = line.indexOf('<' + this.tagName + '>')
                    let i3 = line.indexOf('<' + tmpName + '>')
                    let i4 = line.indexOf('<' + tmpName + ' ')

                    if(i1 == -1 && i3 == -1 && i4 == -1){
                        continue
                    }
                    // console.log(i1, i3, i4, tagCount)

                    if(i1 > -1){
                        tagCount ++
                    }
                    if((i3 > -1 || i4 > -1) && tagCount == 0 && i1 == -1){
                        textArr[i] = textArr[i].replace(nameOld1, nameNew1)
                        break
                    }else if(i3 > -1 || i4 > -1){
                        tagCount --
                    }
                    if(tagCount < 0) tagCount = 0
                }
            }
            textSub = textArr.join("\r\n")
        }
        return textSub
    },
    // 光标位置变动事件
    monacoEdiorPosition(e){
        // console.log('monacoEdiorPosition::', e)
        if(this.isChange){
            // 等光标值改变后再改内容
            this.isChange = false
            // if(this.tagName){
                this.lastPosition = this.editor.getPosition()
                let text = this.xmlTagAutoComplementHandle()
                this.textContexts = text
            // }

        }  else{
            this.tagName = this.getXmlTagNameByPosition(this.textContexts)
            if(this.lastPosition){
                this.editor.setPosition(this.lastPosition)
                this.lastPosition = null
            }
        }
    },
    // 获取光标所在的标签名称
    getXmlTagNameByPosition(textContexts){
        if(this.prefix != 'xml' && this.prefix != 'html'){
            return ''
        }

        let length = this.getPositionIndex()
        let textSub = textContexts.substr(0, length - 1)
        // console.log('textSub::： ', textSub)
        let ltIndex = textSub.lastIndexOf("<")
        let gtIndex = textSub.lastIndexOf(">")
        let nbspIndex = textSub.lastIndexOf(" ")
        // console.log(textSub)
        // 判断是否在标签
        if(ltIndex == -1 || gtIndex > ltIndex || nbspIndex > ltIndex){
            // console.log('光标不在标签中')
            return ""
        }

        // 单标签不需要处理
        textSub = textContexts.substr(length -1 , textContexts.length)
        if(textSub.indexOf('/>') != -1 && textSub.indexOf('/>') < textSub.indexOf('>')){
            return ""
        }

        // console.log('光标在标签中')
        // 获取标签名
        let copyTextSub = textContexts.substr(0, length - 1)
        ltIndex = copyTextSub.lastIndexOf("<")
        // 光标前半段名称
        let tagName = copyTextSub.substring(ltIndex + 1, copyTextSub.length)
        // 光标后半段内容
        copyTextSub = textContexts.substr(length -1 , 50)
        gtIndex = copyTextSub.indexOf(">")
        nbspIndex = copyTextSub.indexOf(" ")
        let endLength = 0
        // 判断后半段名称截取位置
        if(gtIndex != -1 && nbspIndex != -1){
            endLength = nbspIndex > gtIndex ? gtIndex : nbspIndex
        }else if(gtIndex == -1 && nbspIndex == -1){
            endLength = 0
        }else if(gtIndex != -1){
            endLength = gtIndex
        }else if(nbspIndex != -1){
            endLength = nbspIndex
        }
        // 拼接光标后半段名称
        tagName += copyTextSub.substring(0, endLength)
        return tagName
    },
    // 获取光标在整个内容中的下标位置
    getPositionIndex(){
        let lineNumber = this.editor.getPosition().lineNumber
        let colNumber = this.editor.getPosition().column
        // console.log('光标坐标： ',lineNumber, colNumber)

        let length = 0
        let lineArr = this.textContexts.split("\r\n")
        for(let i = 0; i < lineArr.length; i++){
            if(i == lineNumber -1){
                length += colNumber
                break
            }else{
                length += lineArr[i].length + 2
            }
        }
        return length
    },
    // 字符串转xml对象
    String2XML(str){
        if(document.ActiveXObject){
            let xmlDom = new ActiveXObject("Microsoft.XMLDOM")
            xmlDom.loadXML(str)
            return xmlDom
        } else {
            return new DOMParser().parseFromString(str, 'text/xml')
        }
    },
    // xml对象转字符串
    XML2String(xmlObj){
        if(window.ActiveXObject){
            return xmlObj.xml
        }else{
            return (new XMLSerializer()).serializeToString(xmlObj)
        }
    },
    copy() {
      this.$message({
        type: "success",
        message: "复制"
      });
    },
    paste() {
      this.$message({
        type: "success",
        message: "粘贴"
      });
    },
    cut() {
      this.$message({
        type: "success",
        message: "剪切"
      });
    },
    remove() {
      this.$message({
        type: "success",
        message: "删除"
      });
    },
    undo() {
      this.$message({
        type: "success",
        message: "撤销"
      });
    },
    redo() {
      this.$message({
        type: "success",
        message: "重做"
      });
    }
  }
};
</script>

<style lang='scss' scoped>
/* 程序文本编辑器的大小 */
.editor {
  /* width: calc(90% - 120px);*/
  height: 600px;
}
.code-editor,
.code-editor-container {
  top: 25px;
  left: 0px;
  height: 80%;
}
.myGlyphMarginClass {
  background: red;
}
.myContentClass {
  background: #EEA9B8;
}
</style>

