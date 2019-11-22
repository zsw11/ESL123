
export const measureColumns0 = [
  { field: 'a0', title: 'A' },
  { field: 'b0', title: 'B' },
  { field: 'g0', title: 'G' },
  { field: 'a1', title: 'A' },
  { field: 'b1', title: 'B' },
  { field: 'p0', title: 'P' },
  { field: 'm0', title: 'M', bgClassName: 'bg-light-orange' },
  { field: 'x0', title: 'X', bgClassName: 'bg-light-orange' },
  { field: 'i0', title: 'I', bgClassName: 'bg-light-orange' },
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
export const measureFields = measureColumns0.map(c => c.field).concat(measureColumns1.map(c => c.field))

export default {
  measureColumns0,
  measureColumns1,
  measureFields
}
