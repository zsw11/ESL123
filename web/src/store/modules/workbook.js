export default {
  namespaced: true,
  state: {
    copyContent: null
  },
  mutations: {
    SetCopyContent (state, content) {
      state.copyContent = content
    }
  },
  actions: {
    copy ({ commit }, content) {
      commit('SetCopyContent', content)
    }
  }
}
