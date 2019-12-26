import { getToken, setToken, getDisplay, setDisplay } from '@/utils/auth'
import { login, fetchUserDetail } from '@/api/passport'
import md5 from 'blueimp-md5'

export default {
  namespaced: true,
  state: {
    id: null,
    name: '',
    department: '',
    token: getToken(),
    member: {},
    unreadCount: 0,
    displaySetting: {}
  },
  mutations: {
    SetId (state, id) {
      state.id = id
    },
    SetName (state, name) {
      state.name = name
    },
    SetToken (state, token) {
      state.token = token
      setToken(token)
    },
    SetMember (state, member) {
      state.member = member
    },
    SetUnreadCount (state, unreadCount) {
      state.unreadCount = unreadCount
    },
    SetDisplay (state, display) {
      state.displaySetting = display
    },
    SetADisplay (state, payload) {
      state.displaySetting[payload.page] = payload.display
      setDisplay(state.member.id, state.displaySetting)
    },
    SetDepartment (state, department) {
      state.department = department
    }
  },
  actions: {
    login ({ commit, state }, loginInfo) {
      return new Promise((resolve, reject) => {
        login({
          username: loginInfo.username.trim(),
          password: loginInfo.apo ? loginInfo.password.trim() : md5('security' + loginInfo.password.trim()),
          apo: loginInfo.apo
        }).then(response => {
          const data = response.page
          commit('SetId', data.id)
          commit('SetName', data.username)
          commit('SetToken', data.token)
          commit('SetMember', data.member)
          commit('SetDisplay', getDisplay(data.id))
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },
    updateUserInfo ({ commit }) {
      fetchUserDetail().then(res => {
        const data = res.page
        commit('SetId', data.id)
        commit('SetName', data.username)
        commit('SetDepartment', data.deptName)
      })
    },
    SetUnreadCount ({ commit }, unreadCount) {
      commit('SetUnreadCount', unreadCount)
    },
    SetADisplay ({ commit }, payload) {
      commit('setADisplay', payload)
    }
  }
}
