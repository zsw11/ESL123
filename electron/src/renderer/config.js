import isDev from 'electron-is-dev'

export default {
  AutoSaveInterval: 1000 * 30,
  AutoCacheInterval: 1000 * 5,
  LockInterval: 1000 * 10,
  baseUrl: `${isDev ? 'http://117.48.230.8:8080/esl' : 'http://172.16.73.24:8080/esl'}`
}
