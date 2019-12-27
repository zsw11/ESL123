export default {
  namespaced: true,
  state: {
    currentWorkbook: null,
    workbookHistory: [],
    currentOffset: 0
  },
  mutations: {
    SetCurrentWorkbook (state, workbook) {
      state.currentWorkbook = workbook
      state.workbookHistory = []
      console.log(state)
    },
    PushHistory (state, data) {
      // Undo状态
      if (state.currentOffset < state.workbookHistory.length - 1) {
        state.workbookHistory.splice(state.currentOffset + 1)
      } else if (state.workbookHistory.length >= 10) {
        state.workbookHistory.shift()
      }
      state.workbookHistory.push(data)
      state.currentOffset = state.workbookHistory.length - 1
      console.log(state.workbookHistory.length, state.currentOffset)
    },
    Undo (state) {
      if (state.currentOffset >= 0) state.currentOffset--
    },
    Redo (state) {
      if (state.currentOffset <= state.workbookHistory.length - 1) state.currentOffset++
    }
  },
  actions: {
    setCurrentWorkbook ({ commit }, workbook) {
      commit('SetCurrentWorkbook', workbook)
    },
    pushHistory ({ commit }, data) {
      commit('PushHistory', data)
    },
    undo ({ commit }) {
      commit('Undo')
    },
    redo ({ commit }) {
      commit('Redo')
    },
    cache ({ commit }, data) {

    }
  }
}
