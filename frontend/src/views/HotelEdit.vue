<template>
  <a-drawer
    title="修改酒店信息"
    :maskClosable="false"
    width="600"
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="staffEditVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="酒店名称" v-bind="formItemLayout">
        <a-input v-decorator="['name']"/>
      </a-form-item>
<!--      <a-form-item label="性别" v-bind="formItemLayout">-->
<!--        <a-select-->
<!--          :allowClear="true"-->
<!--          style="width: 100%"-->
<!--          v-decorator="['sex',{rules: [{ required: true, message: '请选择性别' }]}]">-->
<!--          <a-select-option v-for="r in sexData" :key="r.keyy">{{r.valuee}}</a-select-option>-->
<!--        </a-select>-->
<!--      </a-form-item>-->
      <a-form-item label="酒店地址" v-bind="formItemLayout">
        <a-input v-decorator="['address']"/>
      </a-form-item>
      <a-form-item label="联系电话" v-bind="formItemLayout">
        <a-input v-decorator="['phone']"/>
      </a-form-item>
      <a-form-item label="经纬度" v-bind="formItemLayout">
        <a-input v-decorator="['location']"/>
      </a-form-item>
      <a-form-item label="酒店设施" v-bind="formItemLayout">
        <a-textarea :rows="4" v-decorator="['facility']"/>
      </a-form-item>
      <a-form-item label="酒店介绍" v-bind="formItemLayout">
        <a-textarea :rows="4" v-decorator="['content']"/>
      </a-form-item>
      <a-form-item label="住房提示" v-bind="formItemLayout">
        <a-textarea :rows="4" v-decorator="['tip']"/>
      </a-form-item>
      <a-form-item label='酒店图册' v-bind="formItemLayout">
        <a-upload
          name="avatar"
          action="http://127.0.0.1:9527/file/fileUpload/"
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
import {mapState, mapMutations} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 18 }
}
export default {
  name: 'StaffEdit',
  props: {
    staffEditVisiable: {
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
    setFormValues ({...staff}) {
      this.rowId = staff.id
      let fields = ['name', 'address', 'location', 'phone', 'facility', 'content', 'tip']
      let obj = {}
      Object.keys(staff).forEach((key) => {
        if(key === "pic") {
          this.fileList = []
          this.proPicInit(staff["pic"])
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = staff[key]
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
          let staff = this.form.getFieldsValue()
          staff.pic = projectImage.length > 0 ? projectImage.join(",") : null
          staff.id = this.rowId
          this.$put('/cos/hotel', {
            ...staff
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
<style scoped>
/deep/ .ant-col-18 {
  width: 85%;
}
</style>
