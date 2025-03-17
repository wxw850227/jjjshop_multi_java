<template>
    <el-dialog title="编辑服务" v-model="dialogVisible" @close='dialogFormVisible' :close-on-click-modal="false"
               :close-on-press-escape="false">
        <el-form size="small" :model="form" :rules="formRules" ref="form">
            <el-form-item label="名称" :label-width="formLabelWidth" prop="name">
                <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth" prop="description">
              <el-input v-model="form.description" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="图片" prop="logo" :label-width="formLabelWidth">
              <el-row>
                <el-button type="primary" @click="openUpload">选择图片</el-button>
                <div v-if="form.logo!=''" class="img">
                  <img :src="form.logo" width="100" height="100" />
                </div>
              </el-row>
              <div class="gray9">建议图片尺寸为42x42</div>
            </el-form-item>
            <el-form-item label="状态" :label-width="formLabelWidth">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">显示</el-radio>
                <el-radio :label="0">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
        </el-form>
        <template #footer>
        <div class="dialog-footer">
            <el-button @click="dialogFormVisible">取 消</el-button>
            <el-button type="primary" @click="editCategory" :loading="loading">确 定</el-button>
        </div>
        </template>
        <!--上传图片组件-->
        <Upload v-if="isupload" :isupload="isupload" :type="type" @returnImgs="returnImgsFunc">上传图片</Upload>
    </el-dialog>
</template>

<script>
    import SupplierApi from '@/api/supplier.js';
    import Upload from '@/components/file/Upload.vue';
    export default {
      components: {
        Upload
      },
        data() {
            return {
              form: {
                serviceSecurityId: 0,
                name: '',
                logo: '',
                description: '',
                status:1,
              },
              formRules: {
              name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
              description: [{ required: true, message: '请输入描述', trigger: 'blur' }],
              logo: [{ required: true, message: '请请上传图片', trigger: 'blur' }],
              },
                /*左边长度*/
                formLabelWidth: '120px',
                /*是否显示*/
                dialogVisible: false,
                loading: false,
                /*是否上传图片*/
                isupload: false,


            };
        },
        props: ['open_edit', 'editform'],
        created() {
            this.dialogVisible = this.open_edit;
            this.form.serviceSecurityId = this.editform.serviceSecurityId;
            this.form.name = this.editform.name;
            this.form.description = this.editform.description;
            this.form.status = this.editform.status;
            this.form.logo = this.editform.logo;
        },
        methods: {
            /*修改*/
            editCategory() {
                let self = this;
                let params = this.form;
                self.$refs.form.validate((valid) => {
                if (valid) {
                self.loading = true;
                SupplierApi.editSecurity(params, true)
                    .then(data =>
                    {
						self.loading = false;
                        ElMessage({
                            message: '恭喜你，修改成功',
                            type: 'success'
                        });
                        self.dialogFormVisible(true);

                    })
                    .catch(error =>
                    {
						self.loading = false;
                    });
                    }
                    });
            },

            /*关闭弹窗*/
            dialogFormVisible(e) {
                if (e) {
                    this.$emit('closeDialog', {
                        type: 'success',
                        openDialog: false
                    })
                } else {
                    this.$emit('closeDialog', {
                        type: 'error',
                        openDialog: false
                    })
                }
            },
            /*上传*/
            openUpload(e) {
              this.type = e;
              this.isupload = true;
            },
            /*获取图片*/
            returnImgsFunc(e) {
              if (e != null && e.length > 0) {
                this.form.logo = e[0].fileath;
              }
              this.isupload = false;
            },

        }
    };
</script>

<style></style>
