// pages/minePage/minePage.js
var app = getApp();
var isLoginSuccess = false;
let http = require('../../utils/request')

Page({

     /**
      * 页面的初始数据
      */
     data: {
          userTitle: '点击登录',
          userHead: '../../res/images/ic_mine_normal.png',
          userInfo: {},
          hasUserInfo: false,
     },

     /**
      * 生命周期函数--监听页面加载
      */
     onLoad: function (options) {
          this.loginTap()
     },

     previewHead: function () {
          wx.previewImage({
               current: this.data.userHead,
               urls: [this.data.userHead]
          })
     },

     loginTap: function () {
          wx.getStorage({
               key: 'userInfo',
               success: (res) => {
                    app.globalData.userInfo = res.data
                    this.initLoginMsg()
               },
               fail: res => {
                    var that = this;
                    if (!isLoginSuccess) {
                         wx.showLoading({
                              title: '正在登录...',
                         })
                         // 获取用户信息
                         wx.getSetting({
                              success: res => {
                                   wx.hideLoading();
                                   if (res.authSetting['scope.userInfo']) {
                                        // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
                                        wx.getUserProfile({
                                             desc: '获取用户相关信息',
                                             success: res => {
                                                  // 可以将 res 发送给后台解码出 unionId
                                                  app.globalData.userInfo = res.userInfo
                                                  http.post('userAdd', {
                                                       username: res.userInfo.nickName
                                                  }).then((res) => {
                                                       that.setData({
                                                            hotel: res.data
                                                       })
                                                  })
                                                  wx.setStorage({
                                                       key: 'userInfo',
                                                       data: res.userInfo,
                                                       success: function () {
                                                            console.log("成功！")
                                                       }, fail: function () {
                                                            console.log("失败！")
                                                       }
                                                  })
                                                  // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
                                                  // 所以此处加入 callback 以防止这种情况
                                                  if (app.userInfoReadyCallback) {
                                                       app.userInfoReadyCallback(res)
                                                  }
                                                  that.initLoginMsg();
                                             },
                                             fail: res => {
                                                  wx.hideLoading();
                                                  isLoginSuccess = false;
                                                  that.setData({
                                                       userTitle: '点击登录',
                                                       userHead: '../../res/images/ic_mine_normal.png'
                                                  })
                                             }
                                        })
                                   }
                              }
                         })
                    }
               }
          })
     },

     initLoginMsg: function () {
          if (app.globalData.userInfo) {
               isLoginSuccess = true;
               this.setData({
                    userHead: app.globalData.userInfo.avatarUrl,
                    userTitle: '尊贵的，' + app.globalData.userInfo.nickName
               })
          } else if (this.data.canIUse) {
               // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
               // 所以此处加入 callback 以防止这种情况
               isLoginSuccess = true;
               app.userInfoReadyCallback = res => {
                    this.setData({
                         userHead: res.userInfo.avatarUrl,
                         userTitle: '尊贵的，' + res.userInfo.nickName
                    })
               }
          } else {
               // 在没有 open-type=getUserInfo 版本的兼容处理
               wx.getUserInfo({
                    success: res => {
                         isLoginSuccess = true;
                         app.globalData.userInfo = res.userInfo
                         this.setData({
                              userHead: res.userInfo.avatarUrl,
                              userTitle: '尊贵的，' + res.userInfo.nickName
                         })
                    },
                    fail() {
                         isLoginSuccess = false;
                         this.setData({
                              userTitle: '点击登录',
                              userHead: '../../res/images/ic_mine_normal.png'
                         })
                    }
               })
          }
     },

     allOrderTap: function () {
          wx.getStorage({
               key: 'userInfo',
               success: (res) => {
                    wx.navigateTo({
                         url: '../orderList/orderList?type=all&name='+res.data.nickName,
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

     hotelTap: function () {
          wx.navigateTo({
               url: '../hotel/hotel',
          })
     },

     todoOrderTap: function () {
          wx.getStorage({
               key: 'userInfo',
               success: (res) => {
                    wx.navigateTo({
                         url: '../orderList/orderList?type=todo&name='+res.data.nickName,
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