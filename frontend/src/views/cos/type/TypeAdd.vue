<template>
  <a-drawer
    title="新增房间类型"
    :maskClosable="false"
    width=600
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="typeAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="名 称：" v-bind="formItemLayout">
        <a-input v-decorator="['typeName',{rules: [{ required: true, message: '请填写房间名称' }]}]"/>
      </a-form-item>
      <a-form-item label="价 格：" v-bind="formItemLayout">
        <a-input-number v-decorator="['price',{rules: [{ required: true, message: '请填写所需价格' }]}]" :min="1" :step="1" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="设 备：" v-bind="formItemLayout">
        <a-textarea v-decorator="['facilities',{rules: [{ required: true, message: '请填写配套设备' }]}]"/>
      </a-form-item>
      <a-form-item label='图 册：' v-bind="formItemLayout">
        <a-upload
          name="avatar"
          action="http://localhost:9527/file/fileUpload/"
          list-type="picture-card"
          :file-list="fileList"
          @preview="handlePreview"
          @change="picHandleChange"
        >
          <div v-if="fileList.length < 8">
            <a-icon type="plus" />
            <div class="ant-upload-text">
              Upload
            </div>
          </div>
        </a-upload>
        <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
          <img alt="example" style="width: 100%" :src="previewImage" />
        </a-modal>
      </a-form-item>
    </a-form>
      <div class="drawer-bootom-button">
        <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
          <a-button style="margin-right: .8rem">取消</a-button>
        </a-popconfirm>
        <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
      </div>
  </a-drawer>
</template>
<script>
function getBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result);
    reader.onerror = error => reject(error);
  });
}
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 21 }
}
export default {
  name: 'TypeAdd',
  props: {
    typeAddVisiable: {
      default: false
    }
  },
  data () {
    return {
      type: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this),
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    handleCancel() {
      this.previewVisible = false;
    },
    picHandleChange({ fileList }) {
      this.fileList = fileList;
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let projectImage = []
      this.fileList.forEach(image => {
        projectImage.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.pic = projectImage.length > 0 ? projectImage.join(",") : null
        if (!err) {
          this.loading = true
          this.type = values
          this.$post('/cos/room-type', {
            ...this.type
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  },
  watch: {}
}
</script>
