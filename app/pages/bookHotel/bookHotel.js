// pages/bookHotel/bookHotel.js
let http = require('../../utils/request')

var roomPrice;
var hotelName;
var roomName;
var startDate;
var endDate;
var bed;
var remaker;

Page({

     /**
      * 页面的初始数据
      */
     data: {
          roomInfo: {},
          username: '',
          phone: '',
          content: '',
          isDiscount: false,
          roomPrice,
          hotelName,
          roomName,
          startDate,
          endDate,
          discount: '不选择优惠',
          bed,
          remaker,
     },

     /**
      * 生命周期函数--监听页面加载
      */
     onLoad: function (options) {
          console.log(options);
          roomPrice = options.price;
          hotelName = options.hotelName;
          roomName = options.roomName;
          startDate = options.startDate;
          endDate = options.endDate;
          bed = options.bed;
          remaker = options.remaker;
          let data = options
          this.setData({
               roomPrice: roomPrice,
               hotelName: hotelName,
               roomName: roomName,
               startDate: startDate,
               endDate: endDate,
               remaker: remaker,
               bed: bed,
               roomInfo: data
          });
     },

     orderAdd: function () {
          wx.getStorage({
               key: 'userInfo',
               success: (res) => {
                    let param = { roomId: this.data.roomInfo.type, money: this.data.roomInfo.price, type: 0, status: 0, person: this.data.username, phone: this.data.phone, content: this.data.content, startDate: this.data.roomInfo.startDate, endDate: this.data.roomInfo.endDate, username: res.data.nickName }
                    http.post('order', param).then((res) => {
                         wx.showToast({
                              title: '预订房间成功',
                              icon: 'none',
                              duration: 2000
                         })
                         setTimeout(() => {
                              wx.switchTab({
                                   url: '../homePage/homePage',
                              })
                         }, 1000)
                    })
               },
               fail: res => {
                    wx.showToast({
                         title: '请先进行登录',
                         icon: 'none',
                         duration: 2000
                    })
               }
          })
     },

     /**
      * 生命周期函数--监听页面初次渲染完成
      */
     onReady: function () {

     },

     /**
      * 生命周期函数--监听页面显示
      */
     onShow: function () {

     },

     /**
      * 生命周期函数--监听页面隐藏
      */
     onHide: function () {

     },

     /**
      * 生命周期函数--监听页面卸载
      */
     onUnload: function () {

     },

     /**
      * 页面相关事件处理函数--监听用户下拉动作
      */
     onPullDownRefresh: function () {

     },

     /**
      * 页面上拉触底事件的处理函数
      */
     onReachBottom: function () {

     },

     /**
      * 用户点击右上角分享
      */
     onShareAppMessage: function () {

     }
})