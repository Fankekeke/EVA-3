<template>
  <div :class="[multipage === true ? 'multi-page':'single-page', 'not-menu-page', 'home-page']">
    <a-row :gutter="8" class="head-info">
      <a-card class="head-info-card" hoverable>
        <a-col :span="12">
          <div class="head-info-avatar">
            <img alt="头像" :src="avatar">
          </div>
          <div class="head-info-count">
            <div class="head-info-welcome">
              {{welcomeMessage}}
            </div>
<!--            <div class="head-info-desc">-->
<!--              <p>{{user.deptName ? user.deptName : '暂无部门'}} | {{user.roleName ? user.roleName : '暂无角色'}}</p>-->
<!--            </div>-->
            <div class="head-info-time">上次登录时间：{{user.lastLoginTime ? user.lastLoginTime : '第一次访问系统'}}</div>
          </div>
        </a-col>
        <a-col :span="12">
          <div>
            <a-row class="more-info">
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4"></a-col>
              <a-col :span="4">
                <head-info title="今日IP" :content="todayIp" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="今日访问" :content="todayVisitCount" :center="false" :bordered="false"/>
              </a-col>
              <a-col :span="4">
                <head-info title="总访问量" :content="totalVisitCount" :center="false" />
              </a-col>
            </a-row>
          </div>
        </a-col>
      </a-card>
    </a-row>
    <a-row :gutter="8" class="count-info">
      <a-col :span="24" class="visit-count-wrapper" style="margin-bottom: 10px">
        <a-card class="visit-count" hoverable>
          <a-button icon="retweet" style="float: right" @click="editHotel">编辑信息</a-button>
          <a-descriptions title="公司信息" bordered>
            <a-descriptions-item label="酒店名称">
              {{ hotelInfo.name !== undefined && hotelInfo.name !== '' ? hotelInfo.name : ' - - ' }}
            </a-descriptions-item>
            <a-descriptions-item label="地址">
              {{ hotelInfo.address !== undefined && hotelInfo.address !== '' ? hotelInfo.address : ' - - ' }}
            </a-descriptions-item>
            <a-descriptions-item label="酒店电话">
              {{ hotelInfo.phone !== undefined && hotelInfo.phone !== '' ? hotelInfo.phone : ' - - ' }}
            </a-descriptions-item>
            <a-descriptions-item label="酒店设施">
              <div v-if="hotelInfo.facility !== undefined && hotelInfo.facility !== ''">
                <a-tooltip>
                  <template slot="title">
                    {{ hotelInfo.facility }}
                  </template>
                  {{ hotelInfo.facility.slice(0, 15) }} ...
                </a-tooltip>
              </div>
              <div v-else> - - </div>
            </a-descriptions-item>
            <a-descriptions-item label="介绍">
              <div v-if="hotelInfo.content !== undefined && hotelInfo.content !== ''">
                <a-tooltip>
                  <template slot="title">
                    {{ hotelInfo.content }}
                  </template>
                  {{ hotelInfo.content.slice(0, 15) }} ...
                </a-tooltip>
              </div>
              <div v-else> - - </div>
            </a-descriptions-item>
            <a-descriptions-item label="订房提示">
              <div v-if="hotelInfo.tip !== undefined && hotelInfo.tip !== ''">
                <a-tooltip>
                  <template slot="title">
                    {{ hotelInfo.tip }}
                  </template>
                  {{ hotelInfo.tip.slice(0, 15) }} ...
                </a-tooltip>
              </div>
              <div v-else> - - </div>
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </a-col>
      <a-col :span="12" class="visit-count-wrapper" style="margin-bottom: 10px">
        <a-card class="visit-count" hoverable>
          <a-carousel autoplay style="height: 300px;" v-if="hotelInfo.pic !== undefined && hotelInfo.pic !== ''">
            <div style="width: 100%;height: 300px" v-for="(item, index) in hotelInfo.pic.split(',')" :key="index">
              <img :src="'../../static/image/'+item" style="width: 100%;height: 100%">
            </div>
          </a-carousel>
        </a-card>
      </a-col>
      <a-col :span="12" class="visit-count-wrapper" style="margin-bottom: 10px">
        <a-card class="visit-count" hoverable>
          <div class="map-area" id="areas"></div>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="8" class="count-info">
      <a-col :span="24" class="visit-count-wrapper">
        <a-card class="visit-count" hoverable>
          <div id="countChart" style="height: 400px;border:1px solid  #f1f1f1;border-radius: 5px" ></div>
        </a-card>
      </a-col>
    </a-row>

    <hotelEdit
      ref="hotelEdit"
      @close="handleStaffEditClose"
      @success="handleStaffEditSuccess"
      :staffEditVisiable="editFlag">
    </hotelEdit>
  </div>
</template>
<script>
import HeadInfo from '@/views/common/HeadInfo'
import hotelEdit from './HotelEdit'
import {mapState} from 'vuex'
import moment from 'moment'
import RunningTask from './common/RunningTask'
moment.locale('zh-cn')
export default {
  name: 'HomePage',
  components: {RunningTask, HeadInfo, hotelEdit},
  data () {
    return {
      series: [],
      projects: [],
      todayIp: '',
      todayVisitCount: '',
      totalVisitCount: '',
      userRole: '',
      userDept: '',
      lastLoginTime: '',
      welcomeMessage: '',
      loadRepo: 0,
      htmlspan: '<span style="display:inline-block;margin-right: 5px;border-radius: 10px;width: 10px;height: 10px;background-color: ',
      legends: ['总数', '您'],
      myChart: {},
      hotelInfo: {},
      editFlag: false,
      map: null
    }
  },
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    }),
    avatar () {
      return `static/avatar/${this.user.avatar}`
    }
  },
  methods: {
    editClose () {
      this.editFlag = false
    },
    editHotel () {
      this.$refs.hotelEdit.setFormValues(this.hotelInfo)
      this.editFlag = true
    },
    handleStaffEditClose () {
      this.editFlag = false
    },
    handleStaffEditSuccess () {
      this.getHotelInfo()
      this.editFlag = false
      this.$message.success('修改信息成功')
    },
    getHotelInfo () {
      this.$get('/cos/hotel/info').then((r) => {
        this.hotelInfo = r.data.data
        if (this.map !== null) {
          let arr = this.hotelInfo.location.split(',')
          this.map.centerAndZoom(new BMap.Point(arr[0], arr[1]), 13);
          let marker = new BMap.Marker(new BMap.Point(arr[0], arr[1]));
          this.map.addOverlay(marker);
        }
      })
    },
    initMap (id) {
      this.map = new BMap.Map(id)
      this.map.centerAndZoom(new BMap.Point(116.404, 39.915), 15)
      this.map.addControl(new BMap.MapTypeControl({
        mapTypes: [
          BMAP_NORMAL_MAP,
          BMAP_HYBRID_MAP
        ]
      }))
      this.map.setCurrentCity('北京')
      this.map.enableScrollWheelZoom(true)

      var overView = new BMap.OverviewMapControl()
      var overViewOpen = new BMap.OverviewMapControl({isOpen: true, anchor: `BMAP_ANCHOR_BOTTOM_RIGHT`})
      this.map.addControl(overView)
      this.map.addControl(overViewOpen)
    },
    welcome () {
      const date = new Date()
      const hour = date.getHours()
      let time = hour < 6 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour <= 18 ? '下午好' : '晚上好')))
      return `${time}，${this.user.username}`
    },
    getRepos () {
      this.$originalGet('https://api.github.com/users/wuyouzhuguli/repos').then((r) => {
        r.data.forEach(repo => {
          let project = {}
          project.id = repo.id
          project.name = repo.name
          project.description = repo.description
          project.avatar = repo.name.substring(0, 1).toUpperCase()
          this.projects.push(project)
        })
      }).catch(error => {
        console.log(error)
      })
    },
    runningProject () {
      let that = this
      this.$get(`index/${this.user.username}`).then((r) => {
        let data = r.data.data
        this.todayIp = data.todayIp
        this.todayVisitCount = data.todayVisitCount
        this.totalVisitCount = data.totalVisitCount
        let dateArr = []
        let totalSeries = {name: '总数', data: [], type: 'bar'}
        let yourSeries = {name: '您', data: [], type: 'bar'}
        for (let i = 6; i >= 0; i--) {
          let time = moment().subtract(i, 'days').format('MM-DD')
          let contain = false
          for (let o of data.lastSevenVisitCount) {
            if (o.days === time) {
              contain = true
              totalSeries.data.push(o.count)
            }
          }
          if (!contain) {
            totalSeries.data.push(0)
          }
          dateArr.push(time)
        }
        this.series.push(totalSeries)
        for (let i = 6; i >= 0; i--) {
          let time = moment().subtract(i, 'days').format('MM-DD')
          let contain = false
          for (let o of data.lastSevenUserVisitCount) {
            if (o.days === time) {
              contain = true
              yourSeries.data.push(o.count)
            }
          }
          if (!contain) {
            yourSeries.data.push(0)
          }
        }
        this.series.push(yourSeries)
        this.myChart.setOption({
          title: {
            text: '近7日系统访问记录',
            show: true,
            left: 10,
            top: 10
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'line'
            },
            formatter: function name (params) {
              let htmlTip = ''
              for (let i = 0; i < params.length; i++) {
                if (i === 0) {
                  htmlTip += params[i].axisValue + '<br />'
                }
                if (i === (params.length - 1)) {
                  htmlTip += (that.htmlspan + params[i].color + ';"></span>' + params[i].seriesName + ' : ' + params[i].value)
                } else {
                  htmlTip += (that.htmlspan + params[i].color + ';"></span>' + params[i].seriesName + ' : ' + params[i].value + '<br />')
                }
              }
              return htmlTip
            }
          },
          legend: {
            type: 'scroll',
            x: 'center',
            y: 'bottom',
            textStyle: {
              fontSize: '12'
            },
            data: this.legends
          },
          toolbox: {
            show: true,
            right: 20,
            top: 10,
            feature: {
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: true,
            data: dateArr,
            axisLabel: {
              textStyle: {
                fontSize: '12'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                fontSize: '12'
              }
            }
          },
          grid: {
            left: '4%'
          },
          series: this.series
        }, true)
      }).catch((r) => {
        console.error(r)
        that.$message.error('获取首页信息失败')
      })
    }
  },
  created () {
    this.getRepos()
    this.loadRepo = 1
  },
  mounted () {
    this.initMap('areas')
    this.welcomeMessage = this.welcome()
    this.myChart = this.$echarts.init(document.getElementById('countChart'))
    this.getHotelInfo()
    this.runningProject()
  }
}
</script>
<style lang="less">
  #areas {
    width: 100%;
    height: 300px;
    padding: 0;
    margin: 0;
  }
  .home-page {
    .head-info {
      margin-bottom: .5rem;
      .head-info-card {
        padding: .5rem;
        border-color: #f1f1f1;
        .head-info-avatar {
          display: inline-block;
          float: left;
          margin-right: 1rem;
          img {
            width: 5rem;
            border-radius: 2px;
          }
        }
        .head-info-count {
          display: inline-block;
          float: left;
          .head-info-welcome {
            font-size: 1.05rem;
            margin-bottom: .1rem;
          }
          .head-info-desc {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
            p {
              margin-bottom: 0;
            }
          }
          .head-info-time {
            color: rgba(0, 0, 0, 0.45);
            font-size: .8rem;
            padding: .2rem 0;
          }
        }
      }
    }
    .count-info {
      .visit-count-wrapper {
        padding-left: 0 !important;
        .visit-count {
          padding: .5rem;
          border-color: #f1f1f1;
          .ant-card-body {
            padding: .5rem 1rem !important;
          }
        }
      }
      .project-wrapper {
        padding-right: 0 !important;
        .project-card {
          border: none !important;
          .ant-card-head {
            border-left: 1px solid #f1f1f1 !important;
            border-top: 1px solid #f1f1f1 !important;
            border-right: 1px solid #f1f1f1 !important;
          }
          .ant-card-body {
            padding: 0 !important;
            table {
              width: 100%;
              td {
                width: 50%;
                border: 1px solid #f1f1f1;
                padding: .6rem;
                .project-avatar-wrapper {
                  display:inline-block;
                  float:left;
                  margin-right:.7rem;
                  .project-avatar {
                    color: #42b983;
                    background-color: #d6f8b8;
                  }
                }
              }
            }
          }
          .project-detail {
            display:inline-block;
            float:left;
            text-align:left;
            width: 78%;
            .project-name {
              font-size:.9rem;
              margin-top:-2px;
              font-weight:600;
            }
            .project-desc {
              color:rgba(0, 0, 0, 0.45);
              p {
                margin-bottom:0;
                font-size:.6rem;
                white-space:nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }
            }
          }
        }
      }
    }
  }
</style>
