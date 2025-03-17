<template>
  <div class="user">
    <!--内容-->
    <div class="product-content">
      <div class="table-wrap">
        <el-table
          size="small"
          :data="tableData"
          border
          style="width: 100%"
          v-loading="loading"
        >
          <el-table-column
            prop="messageTypeText"
            label="所属"
          ></el-table-column>
          <el-table-column
            prop="messageName"
            label="通知名称"
          ></el-table-column>
          <el-table-column prop="remark" label="推送规则"></el-table-column>
<!--          <el-table-column label="公众号通知" v-if="message_to == 10">-->
<!--            <template #default="scope">-->
<!--              <el-checkbox-->
<!--                v-model="scope.row.mpStatus"-->
<!--                @change="(checked) => mpStatusChange(checked, scope.row)"-->
<!--                >启用</el-checkbox-->
<!--              >-->
<!--              <el-link-->
<!--                type="primary"-->
<!--                :underline="false"-->
<!--                style="padding-left: 10px"-->
<!--                @click="mpClick(scope.row)"-->
<!--                >设置</el-link-->
<!--              >-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="小程序通知" v-if="message_to == 10">
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.wxStatus"
                @change="(checked) => wxStatusChange(checked, scope.row)"
                >启用</el-checkbox
              >
              <el-link
                type="primary"
                :underline="false"
                style="padding-left: 10px"
                @click="wxClick(scope.row)"
                >设置</el-link
              >
            </template>
          </el-table-column>
          <el-table-column label="短信通知">
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.smsStatus"
                @change="(checked) => smsStatusChange(checked, scope.row)"
                >启用</el-checkbox
              >
              <el-link
                type="primary"
                :underline="false"
                style="padding-left: 10px"
                @click="smsClick(scope.row)"
                >设置</el-link
              >
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <!--公众号-->
    <Mp
      v-if="open_mp"
      :open_mp="open_mp"
      :messageModel="messageModel"
      @closeDialog="closeDialogFunc($event, 'mp')"
    ></Mp>
    <!--小程序-->
    <Wx
      v-if="open_wx"
      :open_wx="open_wx"
      :messageModel="messageModel"
      @closeDialog="closeDialogFunc($event, 'wx')"
    ></Wx>
    <!--短信-->
    <Sms
      v-if="open_sms"
      :open_sms="open_sms"
      :messageModel="messageModel"
      @closeDialog="closeDialogFunc($event, 'sms')"
    ></Sms>
  </div>
</template>

<script>
import MessageApi from "@/api/message.js";
import Mp from "./settings/Mp.vue";
import Wx from "./settings/Wx.vue";
import Sms from "./settings/Sms.vue";

export default {
  components: {
    Mp,
    Wx,
    Sms,
  },
  data() {
    return {
      /*是否加载完成*/
      loading: true,
      /*列表数据*/
      tableData: [],
      /*是否打开添加弹窗*/
      open_mp: false,
      open_wx: false,
      open_sms: false,
      /*当前编辑的对象*/
      messageModel: {},
    };
  },
  props: ["message_to"],
  watch: {
    message_to: function (n, o) {
      if (n != o) {
        /*获取列表*/
        this.getData();
      }
    },
  },
  created() {
    /*获取列表*/
    this.getData();
  },
  methods: {
    /*获取列表*/
    getData() {
      let self = this;
      MessageApi.messageList(
        {
          messageTo: self.message_to,
        },
        true
      )
        .then((res) => {
          self.loading = false;
          self.tableData = res.data;
          self.tableData.forEach(function (message) {
            message.mpStatus = message.mpStatus === 1 ? true : false;
            message.wxStatus = message.wxStatus === 1 ? true : false;
            message.smsStatus = message.smsStatus === 1 ? true : false;
            if (message.messageSettingsId == null) {
              message.messageSettingsId = 0;
            }
          });
        })
        .catch((error) => {});
    },
    /*公众号消息模板设置*/
    mpClick(item) {
      this.messageModel = item;
      this.open_mp = true;
    },
    /*微信小程序消息模板设置*/
    wxClick(item) {
      this.messageModel = item;
      this.open_wx = true;
    },
    /*短信模板设置*/
    smsClick(item) {
      this.messageModel = item;
      this.open_sms = true;
    },
    /*关闭弹窗*/
    closeDialogFunc(e, f) {
      if (f == "mp") {
        this.open_mp = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "wx") {
        this.open_wx = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
      if (f == "sms") {
        this.open_sms = e.openDialog;
        if (e.type == "success") {
          this.getData();
        }
      }
    },
    mpStatusChange: function (checked, row) {
      let self = this;
      console.log(row);
      if (row.messageSettingsId == 0 || row["mpTemplate"] == null) {
        self.$alert("请先点击右边设置，设置模板规则", "提示", {
          confirmButtonText: "确定",
        });
        row.mpStatus = false;
        return;
      }
      self.loading = true;
      MessageApi.updateSettingsStatus(
        {
          messageType: "mp",
          messageSettingsId: row.messageSettingsId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          row.mpStatus = checked;
        })
        .catch((error) => {
          self.loading = false;
          row.mpStatus = !checked;
        });
    },
    wxStatusChange: function (checked, row) {
      let self = this;

      if (row.messageSettingsId == 0 || row["wxTemplate"] == null) {
        self.$alert("请先点击右边设置，设置模板规则", "提示", {
          confirmButtonText: "确定",
        });
        row.wxStatus = false;
        return;
      }
      self.loading = true;
      MessageApi.updateSettingsStatus(
        {
          messageType: "wx",
          messageSettingsId: row.messageSettingsId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          row.wxStatus = checked;
        })
        .catch((error) => {
          self.loading = false;
          row.wxStatus = !checked;
        });
    },
    smsStatusChange: function (checked, row) {
      let self = this;

      if (row.messageSettingsId == 0 || row["smsTemplate"] == null) {
        self.$alert("请先点击右边设置，设置模板规则", "提示", {
          confirmButtonText: "确定",
        });
        row.smsStatus = false;
        return;
      }
      self.loading = true;
      MessageApi.updateSettingsStatus(
        {
          messageType: "sms",
          messageSettingsId: row.messageSettingsId,
        },
        true
      )
        .then((data) => {
          self.loading = false;
          row.smsStatus = checked;
        })
        .catch((error) => {
          self.loading = false;
          row.smsStatus = !checked;
        });
    },
  },
};
</script>

<style>
.operation-wrap {
  border-radius: 8px;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  padding: 15px 15px;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  overflow: hidden;
  background: #909399;
  background-size: 100% 100%;
  color: #fff;
  margin-bottom: 10px;
}
</style>
