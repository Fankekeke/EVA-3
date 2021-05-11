<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null: 'fold'">
            <a-col :md="12" :sm="24">
              <a-form-item
                label="房间类型"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.typeName"/>
              </a-form-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-item
                label="床位"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.bed"/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="12" :sm="24">
                <a-form-item
                  label="是否禁烟"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-select v-model="queryParams.smoke" allowClear>
                    <a-select-option value="1">禁止</a-select-option>
                    <a-select-option value="2">允许</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24">
                <a-form-item
                  label="WIFI"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-select v-model="queryParams.wifi" allowClear>
                    <a-select-option value="1">无</a-select-option>
                    <a-select-option value="2">有</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="12" :sm="24">
                <a-form-item
                  label="居住人数"
                  :labelCol="{span: 4}"
                  :wrapperCol="{span: 18, offset: 2}">
                  <a-input v-model="queryParams.resident"/>
                </a-form-item>
              </a-col>
            </template>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
             <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? '收起' : '展开' }}
              <a-icon :type="advanced ? 'up' : 'down'"/>
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button type="primary" ghost @click="add">新增</a-button>
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="tag" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div>{{ text }}</div>
            </template>
            <p style="width: 150px;margin-bottom: 0">{{ text }}</p>
          </a-popover>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon style="margin-left: 5px" type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="edit(record)" title="修 改"></a-icon>
          <a-icon style="margin-left: 5px" v-if="record.onput === 0" type="up-square" theme="twoTone" twoToneColor="#36BF36" @click="onPut(record.id, 1)" title="上 架"></a-icon>
          <a-icon style="margin-left: 5px" v-if="record.onput === 1" type="down-square" theme="twoTone" twoToneColor="#FF9900" @click="onPut(record.id, 0)" title="下 架"></a-icon>
        </template>
      </a-table>
    </div>
    <room-add
      @close="handleRoomAddClose"
      @success="handleRoomAddSuccess"
      :roomAddVisiable="roomAdd.visiable"
      :roomType="roomType">
    </room-add>
    <room-edit
      ref="roomEdit"
      @close="handleRoomEditClose"
      @success="handleRoomEditSuccess"
      :roomEditVisiable="roomEdit.visiable"
      :roomType="roomType">
    </room-edit>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import RoomAdd from './RoomAdd'
import RoomEdit from './RoomEdit'
import {mapState} from 'vuex'
import moment from 'moment'
moment.locale('zh-cn')

export default {
  name: 'Room',
  components: {RoomAdd, RoomEdit, RangeDate},
  data () {
    return {
      advanced: false,
      roomAdd: {
        visiable: false
      },
      roomEdit: {
        visiable: false
      },
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: [],
      roomType: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '房间类型',
        dataIndex: 'typeName'
      }, {
        title: '价格',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          return text + ' ￥'
        }
      }, {
        title: '床位',
        dataIndex: 'bed'
      }, {
        title: '房间大小',
        dataIndex: 'size'
      }, {
        title: '楼层',
        dataIndex: 'floor',
        customRender: (text, row, index) => {
          return text + ' 层'
        }
      }, {
        title: '上下架',
        dataIndex: 'onput',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag color="#87d068">上架中</a-tag>
            case 0:
              return <a-tag color="#2db7f5">下架中</a-tag>
          }
        },
      },  {
        title: '备注',
        dataIndex: 'remaker',
        scopedSlots: {customRender: 'tag'}
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
    this.getRoomType()
  },
  methods: {
    onPut (roomId, flag) {
      this.$get('/cos/room/onPut', {
        roomId: roomId, flag: flag
      }).then(() => {
        this.$message.success('房间状态修改成功')
        this.fetch()
      })
    },
    getRoomType () {
      this.$get('/cos/room-type/list').then((r) => {
        this.roomType = r.data.data
      })
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
      if (!this.advanced) {
        this.queryParams.createTimeFrom = ''
        this.queryParams.createTimeTo = ''
      }
    },
    add () {
      this.roomAdd.visiable = true
    },
    handleRoomAddClose () {
      this.roomAdd.visiable = false
    },
    handleRoomAddSuccess () {
      this.roomAdd.visiable = false
      this.$message.success('新增房间成功')
      this.search()
    },
    edit (record) {
      this.$refs.roomEdit.setFormValues(record)
      this.roomEdit.visiable = true
    },
    handleRoomEditClose () {
      this.roomEdit.visiable = false
    },
    handleRoomEditSuccess () {
      this.roomEdit.visiable = false
      this.$message.success('修改房间成功')
      this.search()
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    handleDateChange (value) {
      if (value) {
        this.queryParams.createTimeFrom = value[0]
        this.queryParams.createTimeTo = value[1]
      }
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/room/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortOrder
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortOrder = sortedInfo.order
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      // 清空部门树选择
      this.$refs.deptTree.reset()
      // 清空时间选择
      if (this.advanced) {
        this.$refs.createTime.reset()
      }
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      if (params.wifi === undefined) {
        delete params.wifi
      }
      if (params.smoke === undefined) {
        delete params.smoke
      }
      this.$get('/cos/room', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
