<template>
  <a-drawer
    title="修改房间类型"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="typeEditVisiable"
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
import {mapState, mapMutations} from 'vuex'

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
  name: 'TypeEdit',
  props: {
    typeEditVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      rowId: '',
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    })
  },
  methods: {
    ...mapMutations({
      setUser: 'account/setUser'
    }),
    handleCancel() {
      this.previewVisible = false;
    },
    async handlePreview(file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    },
    /**
     * 图片展示
     * @param proPic
     */
    proPicInit(proPic) {
      if(proPic !== null && proPic !== '') {
        let imageList = []
        proPic.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: '../../static/image/'+image})
        })
        this.fileList = imageList;
      }
    },
    picHandleChange({ fileList }) {
      this.fileList = fileList;
    },
    onClose () {
      this.loading = false
      this.form.resetFields()
      this.$emit('close')
    },
    setFormValues ({...type}) {
      this.rowId = type.id
      let fields = ['typeName', 'price', 'facilities']
      let obj = {}
      Object.keys(type).forEach((key) => {
        if(key === "pic") {
          this.fileList = []
          this.proPicInit(type["pic"])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = type[key]
        }
      })
      this.form.setFieldsValue(obj)
    },
    handleSubmit () {
      // 获取图片List
      let projectImage = []
      this.fileList.forEach(image => {
        projectImage.push(image.response !== undefined ? image.response : image.name)
      })
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          let type = this.form.getFieldsValue()
          type.id = this.rowId
          type.pic = projectImage.length > 0 ? projectImage.join(",") : null
          this.$put('/cos/room-type', {
            ...type
          }).then((r) => {
            this.loading = false
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
