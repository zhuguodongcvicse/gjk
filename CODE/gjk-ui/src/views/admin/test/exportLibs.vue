<template>
  <el-dialog
    width="35%"
    title="选择导出项"
    :visible.sync="showInfo.dialogExportVisible">
    <el-checkbox v-model="exportLibsCheckbox" label="testLib">测试库结构</el-checkbox>
    <el-checkbox v-model="exportLibsCheckbox" label="algorithmLib">算法库结构</el-checkbox>
    <el-checkbox v-model="exportLibsCheckbox" label="platformLib">平台库结构</el-checkbox>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showInfo.dialogExportVisible = false">取 消</el-button>
      <el-button type="primary" @click="exportLibDirectory">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
    import { createZipFile } from "@/api/admin/test";

    export default {
        data() {
            return {
                exportLibsCheckbox: ['testLib', 'algorithmLib', 'platformLib'],
            };
        },
        props: ["showInfo"],
        created() { },
        computed: { },
        methods: {
            exportLibDirectory(){
                if(this.exportLibsCheckbox.length == 0){
                    this.$message.warning("请选择导出项");
                    return
                }
                createZipFile(this.exportLibsCheckbox)
                this.showInfo.dialogExportVisible = false
            },
        }
    };
</script>

