
export const measureColumns0 = [
  { field: 'a0', title: 'A' },
  { field: 'b0', title: 'B' },
  { field: 'g0', title: 'G' },
  { field: 'a1', title: 'A' },
  { field: 'b1', title: 'B' },
  { field: 'p0', title: 'P' },
  { field: 'm0', title: 'M', bgClassName: 'bg-table-color0' },
  { field: 'x0', title: 'X', bgClassName: 'bg-table-color0' },
  { field: 'i0', title: 'I', bgClassName: 'bg-table-color0' },
  { field: 'a2', title: 'A', bgClassName: 'bg-table-color1' },
  { field: 'b2', title: 'B', bgClassName: 'bg-table-color1' },
  { field: 'p1', title: 'P', bgClassName: 'bg-table-color1' },
  { field: 'a3', title: 'A', bgClassName: 'bg-table-color1' }
]
export const measureColumns1 = [
  { field: 'a4', title: 'A', bgClassName: 'bg-table-color1' },
  { field: 'b3', title: 'B', bgClassName: 'bg-table-color1' },
  { field: 'p2', title: 'P', bgClassName: 'bg-table-color1' },
  { field: 'a5', title: 'A' }
]
export const defaultRow = {
  type: 'o',
  version: '',
  operation: '',
  key: null,
  a0: null,
  b0: null,
  g0: null,
  a1: null,
  b1: null,
  p0: null,
  m0: null,
  x0: null,
  i0: null,
  tool: '*0',
  a2: null,
  b2: null,
  p1: null,
  a3: null,
  frequency: null
}
export const measureFields = [ ...measureColumns0.map(c => c.field), 'tool', ...measureColumns1.map(c => c.field) ]
export const defaultFields = Object.keys(defaultRow)
export const commonMeasureFields = measureColumns0.slice(3, 6).map(c => c.field)
export const limitMeasureFields = measureColumns0.slice(6, 9).map(c => c.field)
export const toolMeasureFields = [ ...measureColumns0.slice(9, 13).map(c => c.field), 'tool', ...measureColumns1.slice(0, 3).map(c => c.field) ]
export const modeMeasureFields = [ ...commonMeasureFields, ...limitMeasureFields, ...toolMeasureFields ]
export const measureMode = []
export const jumpFields = [ 'operation', ...measureFields, 'frequency' ]
export const modeFields = {
  c: commonMeasureFields,
  l: limitMeasureFields,
  t: toolMeasureFields
}
export const modeCheckZeroFields = {
  c: commonMeasureFields,
  l: limitMeasureFields,
  t: toolMeasureFields.slice(0, 4)
}
export const modeSetZeroFields = {
  c: commonMeasureFields,
  l: limitMeasureFields,
  t: [ ...toolMeasureFields.slice(0, 4), ...toolMeasureFields.slice(5, 8) ]
}
export const allNumericMeasureField = [...measureColumns0, ...measureColumns1].map(c => c.field)
commonMeasureFields.forEach(f => { measureMode[f] = 'c' })
limitMeasureFields.forEach(f => { measureMode[f] = 'l' })
toolMeasureFields.forEach(f => { measureMode[f] = 't' })

export default {
  measureColumns0,
  measureColumns1,
  measureFields,
  defaultRow,
  defaultFields,
  commonMeasureFields,
  limitMeasureFields,
  toolMeasureFields,
  modeMeasureFields,
  measureMode,
  jumpFields,
  modeFields,
  modeCheckZeroFields,
  modeSetZeroFields,
  allNumericMeasureField
}
