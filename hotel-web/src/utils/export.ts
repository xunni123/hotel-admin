import * as XLSX from 'xlsx'

export const exportToExcel = (
  data: any[],
  columns: { prop: string; label: string }[],
  filename: string = 'export'
) => {
  if (!data || data.length === 0) {
    return
  }

  // 将数据转换为工作表格式
  const wsData = data.map(row => {
    const newRow: any = {}
    columns.forEach(col => {
      newRow[col.label] = row[col.prop] ?? ''
    })
    return newRow
  })

  // 创建工作簿
  const wb = XLSX.utils.book_new()
  const ws = XLSX.utils.json_to_sheet(wsData)

  // 设置列宽
  const colWidths = columns.map(col => ({
    wch: Math.max(col.label.length * 2, 15)
  }))
  ws['!cols'] = colWidths

  // 添加工作表
  XLSX.utils.book_append_sheet(wb, ws, 'Sheet1')

  // 生成文件名（包含时间戳）
  const timestamp = new Date().toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  }).replace(/\//g, '-').replace(/,/g, '').replace(/:/g, '-').replace(/ /g, '_')
  
  const exportFilename = `${filename}_${timestamp}.xlsx`

  // 导出文件
  XLSX.writeFile(wb, exportFilename)
}
