<template>
  <a-drawer
    title="新增房间"
    :maskClosable="false"
    width=650
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="roomAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form">
      <a-form-item label="房间类型" v-bind="formItemLayout">
        <a-select
          :allowClear="true"
          style="width: 100%"
          v-decorator="['type',{rules: [{ required: true, message: '请选择房间类型' }]}]">
          <a-select-option v-for="r in roomType" :key="r.id">{{r.typeName}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="床 位" v-bind="formItemLayout">
        <a-input v-decorator="['bed',{rules: [{ required: true, message: '请填写床位' }]}]"/>
      </a-form-item>
      <a-form-item label="房间大小" v-bind="formItemLayout">
        <a-input v-decorator="['size',{rules: [{ required: true, message: '请填写房间大小' }]}]"/>
      </a-form-item>
      <a-form-item label="楼 层" v-bind="formItemLayout">
        <a-input-number v-decorator="['floor']" :min="1" :step="1" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="WIFI" v-bind="formItemLayout">
        <a-radio-group v-decorator="['wifi']" defaultValue="1">
          <a-radio-button value="0">
            无
          </a-radio-button>
          <a-radio-button value="1">
            有
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="吸 烟" v-bind="formItemLayout">
        <a-radio-group v-decorator="['smoke']" defaultValue="0">
          <a-radio-button value="0">
            禁 止
          </a-radio-button>
          <a-radio-button value="1">
            允 许
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="宽 带" v-bind="formItemLayout">
        <a-radio-group v-decorator="['broadband']" defaultValue="1">
          <a-radio-button value="0">
            无
          </a-radio-button>
          <a-radio-button value="1">
            有
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item label="居住人数" v-bind="formItemLayout">
        <a-input-number v-decorator="['resident']" :min="1" :step="1" style="width: 100%"/>
      </a-form-item>
      <a-form-item label="备 注" v-bind="formItemLayout">
        <a-textarea placeholder="Basic usage" :rows="4" v-decorator="['remaker']"/>
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
const formItemLayout = {
  labelCol: { span: 3 },
  wrapperCol: { span: 21 }
}
export default {
  name: 'RoomAdd',
  props: {
    roomAddVisiable: {
      default: false
    },
    roomType: {
      type: Array
    }
  },
  data () {
    return {
      room: {},
      loading: false,
      formItemLayout,
      form: this.$form.createForm(this)
    }
  },
  methods: {
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          this.room = values
          this.$post('/cos/room', {
            ...this.room
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
