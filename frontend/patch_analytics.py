import os

path = 'C:/QuanLyNhaHang_Workspace/frontend/src/pages/admin/Analytics.jsx'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

# Add recharts import
if "import { BarChart" not in c:
    c = c.replace("import { FilterOutlined, DownloadOutlined } from '@ant-design/icons';", "import { FilterOutlined, DownloadOutlined } from '@ant-design/icons';\nimport { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';")

# Update state and fetch logic
old_fetch = """  const [topItems, setTopItems] = useState([]);
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const fetchAnalytics = async () => {
      try {
        const token = sessionStorage.getItem('accessToken');
        const headers = token ? { Authorization: `Bearer ${token}` } : {};

        const [itemsRes, chartRes] = await Promise.all([
          axios.get('http://localhost:8080/api/v1/reports/top-items', { headers }),
          axios.get('http://localhost:8080/api/v1/reports/revenue-chart', { headers })
        ]);

        setTopItems(itemsRes.data.data);
        setChartData(chartRes.data.data);
      } catch (error) {
        console.error('Lỗi khi tải dữ liệu analytics', error);
      }
    };
    fetchAnalytics();
  }, []);"""

new_fetch = """  const [topItems, setTopItems] = useState([]);
  const [chartData, setChartData] = useState([]);
  const [dateRange, setDateRange] = useState(null);

  const fetchAnalytics = async (dates) => {
    try {
      const token = sessionStorage.getItem('accessToken');
      const headers = token ? { Authorization: `Bearer ${token}` } : {};
      
      let query = '';
      if (dates && dates.length === 2) {
        query = `?startDate=${dates[0].format('YYYY-MM-DD')}&endDate=${dates[1].format('YYYY-MM-DD')}`;
      }

      const [itemsRes, chartRes] = await Promise.all([
        axios.get(`http://localhost:8080/api/v1/reports/top-items${query}`, { headers }),
        axios.get(`http://localhost:8080/api/v1/reports/revenue-chart${query}`, { headers })
      ]);

      setTopItems(itemsRes.data.data);
      setChartData(chartRes.data.data);
    } catch (error) {
      console.error('Lỗi khi tải dữ liệu analytics', error);
    }
  };

  useEffect(() => {
    fetchAnalytics(dateRange);
  }, []);

  const handleFilter = () => {
    fetchAnalytics(dateRange);
  };

  const handleExport = () => {
    if (!chartData || chartData.length === 0) return;
    
    const csvRows = [];
    csvRows.push('Ngay,Doanh Thu');
    chartData.forEach(row => {
      csvRows.push(`${row.date},${row.doanhThu}`);
    });
    
    const csvString = csvRows.join('\\n');
    const blob = new Blob([csvString], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.setAttribute('hidden', '');
    a.setAttribute('href', url);
    a.setAttribute('download', 'BaoCaoDoanhThu.csv');
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };"""

if old_fetch in c:
    c = c.replace(old_fetch, new_fetch)

# Update buttons
old_buttons = """          <Space>
            <RangePicker style={{ width: '300px' }} />
            <Button type="primary" icon={<FilterOutlined />}>Lọc Dữ Liệu</Button>
            <Button icon={<DownloadOutlined />}>Xuất Báo Cáo</Button>
          </Space>"""

new_buttons = """          <Space>
            <RangePicker style={{ width: '300px' }} value={dateRange} onChange={setDateRange} />
            <Button type="primary" icon={<FilterOutlined />} onClick={handleFilter}>Lọc Dữ Liệu</Button>
            <Button icon={<DownloadOutlined />} onClick={handleExport}>Xuất Báo Cáo CSV</Button>
          </Space>"""

if old_buttons in c:
    c = c.replace(old_buttons, new_buttons)

# Update chart
old_chart = """            {/* Giả lập biểu đồ - trong thực tế sẽ dùng Recharts hoặc Chart.js */}
            <div style={{ height: '300px', display: 'flex', alignItems: 'flex-end', justifyContent: 'space-between', padding: '20px 0' }}>
              {chartData.map((data, index) => {
                const maxDoanhThu = Math.max(...chartData.map(d => d.doanhThu || 0));
                const heightPercent = maxDoanhThu === 0 ? 0 : ((data.doanhThu || 0) / maxDoanhThu) * 100;
                return (
                  <div key={index} style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', width: '10%' }}>
                    <div style={{ 
                      height: `${heightPercent}%`, 
                      minHeight: '4px',
                      width: '30px', 
                      backgroundColor: '#1890ff', 
                      borderRadius: '4px 4px 0 0',
                      transition: 'height 0.5s'
                    }}></div>
                    <Text style={{ fontSize: '12px', marginTop: '8px' }}>{data.date.split('-').slice(1).join('/')}</Text>
                  </div>
                );
              })}
            </div>"""

new_chart = """            <div style={{ height: '300px' }}>
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={chartData} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} />
                  <XAxis dataKey="date" tickFormatter={(tick) => tick.split('-').slice(1).join('/')} />
                  <YAxis tickFormatter={(value) => `${(value / 1000).toLocaleString('vi-VN')}k`} />
                  <Tooltip formatter={(value) => `${value.toLocaleString('vi-VN')} đ`} labelFormatter={(label) => `Ngày: ${label}`} />
                  <Bar dataKey="doanhThu" fill="#1890ff" radius={[4, 4, 0, 0]} barSize={40} />
                </BarChart>
              </ResponsiveContainer>
            </div>"""

if old_chart in c:
    c = c.replace(old_chart, new_chart)

with open(path, 'w', encoding='utf-8') as f: f.write(c)
