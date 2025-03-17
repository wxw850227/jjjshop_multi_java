<template>
  <el-dialog
    :title="title"
    v-model="dialogVisible"
    @close="dialogFormVisible"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form size="small">
      <div class="table-wrap">
        <div class="operation-wrap">
          <p>配置说明：</p>
          <p>1、短信模板里有的字段才勾选，如果没有请勿勾选。</p>
          <p>2、模板变量替换成短信模板里的字段。</p>
        </div>
        <div>
          <el-form-item label="模板id：">
            <el-input
              size="small"
              class="max-w460"
              v-model="templateId"
              placeholder="请填写申请的短信模板code"
            ></el-input>
          </el-form-item>
        </div>
        <el-table
          border
          ref="fieldTable"
          :data="fieldList"
          @selection-change="handleSelectionChange"
          v-loading="loading"
        >
          <el-table-column type="selection" width="55"> </el-table-column>
          <el-table-column label="字段名称">
            <template #default="scope">
              <label v-text="scope.row.fieldName"></label>
            </template>
          </el-table-column>
          <el-table-column label="模板变量名">
            <template #default="scope">
              <el-input
                size="small"
                prop="fieldNewEname"
                v-model="scope.row.fieldNewEname"
              ></el-input>
            </template>
          </el-table-column>
          <el-table-column label="模板内容">
            <template #default="scope">
              <el-input
                size="small"
                prop="filedNewValue"
                :disabled="scope.row.isVar === 1"
                v-model="scope.row.filedNewValue"
              ></el-input>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible">取 消</el-button>
        <el-button type="primary" @click="saveTemplate" :loading="loading"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>

<script>
import MessageApi from "@/api/message.js";
export default {
  data() {
    return {
      /*左边长度*/
      formLabelWidth: "120px",
      /*是否显示*/
      dialogVisible: false,
      loading: false,
      /*是否上传图片*/
      isupload: false,
      fieldList: [],
      title: "设置短信模板",
      checkList: [],
      /*设置*/
      settings: {},
      templateId: "",
    };
  },
  props: ["open_sms", "messageModel"],
  created() {
    this.dialogVisible = this.open_sms;
    this.title = this.title + "(" + this.messageModel.messageName + ")";
    this.getData();
  },
  methods: {
    getData: function () {
      let self = this;
      self.loading = true;
      MessageApi.fieldList(
        {
          messageId: self.messageModel.messageId,
          messageType: "sms",
        },
        true
      )
        .then((res) => {
          res.data.list.forEach(function (field) {
            field["fieldNewEname"] = field.fieldEname;
            field["filedNewValue"] = field.filedValue;
          });
          self.fieldList = res.data.list;
          //设置字段
          if (res.data.settings == null || res.data.settings.length == 0) {
            self.settings = {};
            self.templateId = "";
          } else {
            self.settings = res.data.settings;
            self.templateId = res.data.settings["templateId"];
          }
          self.loading = false;
          self.$nextTick(function () {
            self.initChecked();
          });
        })
        .catch((error) => {});
    },
    /*保存*/
    saveTemplate() {
      let self = this;
      self.loading = true;
      MessageApi.saveSettings({
        fieldList: self.checkList,
        messageId: self.messageModel.messageId,
        messageType: "sms",
        templateId: self.templateId,
      })
        .then((data) => {
          self.loading = false;
          ElMessage({
            message: "保存成功",
            type: "success",
          });
          self.dialogFormVisible(true);
        })
        .catch((error) => {
          self.loading = false;
        });
    },

    /*关闭弹窗*/
    dialogFormVisible(e) {
      if (e) {
        this.$emit("closeDialog", {
          type: "success",
          openDialog: false,
        });
      } else {
        this.$emit("closeDialog", {
          type: "error",
          openDialog: false,
        });
      }
    },
    handleSelectionChange(val) {
      this.checkList = val;
    },
    /*初始化选中*/
    initChecked: function () {
      let self = this;
      if (JSON.stringify(self.settings) == "{}") {
        return;
      }
      Object.keys(self.settings.varData).forEach(function (key) {
        self.fieldList.forEach(function (field) {
          if (field.fieldEname == key) {
            self.$refs.fieldTable.toggleRowSelection(field, true);
            field.fieldNewEname = self.settings.varData[key].fieldName;
            field.filedNewValue = self.settings.varData[key].filedValue;
          }
        });
      });
    },
  },
};
</script>
