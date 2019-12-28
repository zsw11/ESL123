const TokenKey = 'Security-Token'
const DisplayKeyPrefix = 'Security-Display'
const CacheKey = 'Cache-Key'

export function getToken () {
  return localStorage.getItem(TokenKey)
}
export function setToken (token) {
  return localStorage.setItem(TokenKey, token)
}
export function removeToken () {
  return localStorage.removeItem(TokenKey)
}

export function getDisplay (memberId) {
  return JSON.parse(localStorage.getItem(`${DisplayKeyPrefix}-${memberId}`) || '{}')
}
export function setDisplay (memberId, displaySetting) {
  return localStorage.setItem(`${DisplayKeyPrefix}-${memberId}`, JSON.stringify(displaySetting))
}

export function getCache () {
  return JSON.parse(localStorage.getItem(CacheKey) || '{}')
}
export function setCache (cache) {
  return localStorage.setItem(CacheKey, JSON.stringify(cache))
}
export function removeCache () {
  return localStorage.removeItem(CacheKey)
}
