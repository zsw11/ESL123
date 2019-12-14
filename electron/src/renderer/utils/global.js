
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
  tool: '',
  a2: null,
  b2: null,
  p1: null,
  a3: null,
  frequency: null
}
export const measureFields = measureColumns0.map(c => c.field).concat(measureColumns1.map(c => c.field))
export const defaultFields = Object.keys(defaultRow)

export default {
  measureColumns0,
  measureColumns1,
  measureFields,
  defaultRow,
  defaultFields
}
