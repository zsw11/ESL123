/**
 * 邮箱
 * @param {*} s
 */
export function isEmail (s) {
  return /^([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-]+){0,3}@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]+){2,3})$/.test(s)
}

/**
 * 手机号码
 * @param {*} s
 */
export function isMobile (s) {
  return /^1[0-9]{10}$/.test(s)
}

/**
 * URL地址
 * @param {*} s
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

export function GetRequest (url) {
  var theRequest = {}
  if (url.indexOf('?') !== -1) {
    var str = url.substr(url.indexOf('?') + 1, url.length)
    var strs = str.split('&')
    for (var i = 0; i < strs.length; i++) {
      theRequest[strs[i].split('=')[0]] = unescape(strs[i].split('=')[1])
    }
  }
  return theRequest
}

export function validateCode (rule, value, callback) {
  if (!/^[a-zA-Z]\w+$/.test(value)) {
    callback(new Error('请输入由字母和数字组成的编码'))
  } else {
    callback()
  }
}

export function validateInteger (rule, value, callback) {
  if (!/^[1-9]\d+$/.test(value)) {
    callback(new Error('请输入整数'))
  } else {
    callback()
  }
}

export function validateUsername (rule, value, callback) {
  if (value.length < 4 || value.length > 202) {
    callback(new Error('长度在 4 到 20 个字符'))
  } else if (!/^\w+$/.test(value)) {
    callback(new Error('请输入由字母和数字组成的正确用户名'))
  } else {
    callback()
  }
}

export function validatePassword (rule, value, callback) {
  if (value.length < 6 || value.length > 32) {
    callback(new Error('长度在 6 到 32 个字符'))
  } else {
    callback()
  }
}

export function validateMobile (rule, value, callback) {
  if (value && !/^1[345789]\d{9}$/.test(value)) {
    callback(new Error('请输入11位手机号码'))
  } else {
    callback()
  }
}

export function validateEmail (rule, value, callback) {
  if (value && !isEmail(value)) {
    callback(new Error('请输入有效的邮箱地址'))
  } else {
    callback()
  }
}

export function validatePhone (rule, value, callback) {
  if (!/^(?:(?:0\d{2,3}-?)?\d{7,8}|1[345789]\d{9})$/.test(value)) {
    callback(new Error('请输入电话号码，格式为：11位手机号，区号-电话，电话'))
  } else {
    callback()
  }
}
