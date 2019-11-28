const getters = {
  userId: state => state.user.id,
  username: state => state.user.name,
  token: state => state.user.token,
  member: state => state.user.member,
  displaySetting: state => state.user.displaySetting,
  unreadCount: state => state.user.unreadCount,
  mainTabs: state => state.common.mainTabs,
  copyContent: state => state.workbook.copyContent
}
export default getters
