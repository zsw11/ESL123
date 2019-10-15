const TokenKey = 'Security-Token'
const DisplayKeyPrefix = 'Security-Display'

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
