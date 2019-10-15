import day from 'dayjs'

export function format (date, fm) {
  return date ? day(date).format(fm) : undefined
}

export function jsonStringData (jsonString, key) {
  return JSON.parse(jsonString)[key]
}
