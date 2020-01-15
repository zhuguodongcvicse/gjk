<template>
    <el-dialog
      title="导入库目录结构"
      :visible.sync="showInfo.importLibsDialogVisible"
      width="35%"
      :before-close="dialogBeforeClose"
      :append-to-body="true"
    >
      <i>请上传库目录导出的压缩包(上传文件编码格式为zip)</i>
      <br />
      <br />
      <el-radio-group v-model="importLibsRadio" >
        <el-radio label="allImport">覆盖导入</el-radio>
        <el-radio label="addImport">增量导入</el-radio>
      </el-radio-group>
      <br />
      <br />
      <el-upload
        ref="importLibs"
        class="avatar-uploader"
        action="/admin/test/importLibsZipUpload"
        :before-upload="beforeAvatarUpload"
        :http-request="importLibsFileUploadFunc"
        :on-exceed="onExceed"
        :before-remove="beforeRemove"
        :file-list="importLibsFileList"
        accept=".zip"
        :limit="1"
      >
        <el-button size="small" type="primary">上传压缩包</el-button>
      </el-upload>
      <div slot="footer">
        <el-button @click="closeImportLibsDialog">取 消</el-button>
        <el-button type="primary" @click="importLibsFile">确 定</el-button>
      </div>
    </el-dialog>
</template>

<script>
    import {
        importLibsZipUpload,
        importLibsZipUploadPlatform,
        importLibsZipUploadAlgorithm
    } from "@/api/admin/test";

    export default {
        data() {
            return {
                importLibsFileList: [],
                importLibsRadio: 'addImport',
            };
        },
        props: ["showInfo","whichLib"],
        created() { },
        computed: { },
        methods: {
            dialogBeforeClose(done) {
                done();
                this.$refs.importLibs.clearFiles();
                this.importLibsFileList = [];
            },
            beforeAvatarUpload(file) {
                const isZIP =
                    file.type === "application/x-zip-compressed" || "application/zip";
                if (!isZIP) {
                    this.$message.error(
                        "上传文件格式只能是压缩文件，请传入库目录导出的压缩文件。"
                    );
                }
                return isZIP /*&& isLt2M*/;
            },
            importLibsFileUploadFunc(param) {
                this.importLibsFileList.push(param.file);
            },
            onExceed(file, fileList) {
                this.$message.warning(
                    `当前限制选择1个文件，本次选择了 ${
                        file.length
                    } 个文件，共选择了 ${file.length + fileList.length} 个文件`
                );
            },
            beforeRemove(file, fileList) {
                this.importLibsFileList = [];
            },
            closeImportLibsDialog() {
                this.showInfo.importLibsDialogVisible = false;
                this.$refs.importLibs.clearFiles();
                this.importLibsFileList = [];
            },
            importLibsFile() {
                if (this.importLibsFileList.length == 0) {
                    this.$message.warning("请选择文件上传。");
                } else {
                    let params = new FormData();
                    params.append("file", this.importLibsFileList[0]);
                    params.append("importType", this.importLibsRadio);
                    if(this.whichLib === "algorithm"){
                         importLibsZipUploadAlgorithm(params).then(Response => {
                        if (Response.data.data == -1) {
                            this.$message.warning("上传的压缩包内容错误，请重新选择文件上传。");
                        } else {
                            this.$message.success("导入成功。");
                            this.$emit("callback");
                            this.closeImportLibsDialog();
                        }
                    });
                    }else if(this.whichLib === "test"){
                         importLibsZipUpload(params).then(Response => {
                        if (Response.data.data == -1) {
                            this.$message.warning("上传的压缩包内容错误，请重新选择文件上传。");
                        } else {
                            this.$message.success("导入成功。");
                            this.$emit("callback");
                            this.closeImportLibsDialog();
                        }
                    });
                    }else if(this.whichLib === "platform"){
                         importLibsZipUploadPlatform(params).then(Response => {
                        if (Response.data.data == -1) {
                            this.$message.warning("上传的压缩包内容错误，请重新选择文件上传。");
                        } else {
                            this.$message.success("导入成功。");
                            this.$emit("callback");
                            this.closeImportLibsDialog();
                        }
                    });
                    }
                   
                }
            }
        }
    };
</script>

