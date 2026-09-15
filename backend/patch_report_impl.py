import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/ReportServiceImpl.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old_items = """    @Override
    public List<TopItemProjection> getTopItems() {
        LocalDateTime startOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), LocalTime.MAX);
        List<TopItemProjection> topItems = chiTietHoaDonRepository.getTopItems(startOfMonth, endOfMonth);
        return topItems.stream().limit(5).collect(Collectors.toList());
    }"""

new_items = """    @Override
    public List<TopItemProjection> getTopItems(String startDateStr, String endDateStr) {
        LocalDateTime start;
        LocalDateTime end;
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            start = LocalDate.parse(startDateStr).atStartOfDay();
            end = LocalDate.parse(endDateStr).atTime(LocalTime.MAX);
        } else {
            start = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
            end = LocalDateTime.of(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), LocalTime.MAX);
        }
        List<TopItemProjection> topItems = chiTietHoaDonRepository.getTopItems(start, end);
        return topItems.stream().limit(5).collect(Collectors.toList());
    }"""

old_chart = """    @Override
    public List<Map<String, Object>> getRevenueChart() {
        List<Map<String, Object>> chart = new java.util.ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
            BigDecimal doanhThu = hoaDonRepository.sumDoanhThuByDateRange(startOfDay, endOfDay);
            if (doanhThu == null) doanhThu = BigDecimal.ZERO;
            
            chart.add(Map.of(
                "date", date.toString(),
                "doanhThu", doanhThu
            ));
        }
        return chart;
    }"""

new_chart = """    @Override
    public List<Map<String, Object>> getRevenueChart(String startDateStr, String endDateStr) {
        List<Map<String, Object>> chart = new java.util.ArrayList<>();
        LocalDate start, end;
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            start = LocalDate.parse(startDateStr);
            end = LocalDate.parse(endDateStr);
        } else {
            end = LocalDate.now();
            start = end.minusDays(6);
        }
        
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);
            BigDecimal doanhThu = hoaDonRepository.sumDoanhThuByDateRange(startOfDay, endOfDay);
            if (doanhThu == null) doanhThu = BigDecimal.ZERO;
            
            chart.add(Map.of(
                "date", date.toString(),
                "doanhThu", doanhThu
            ));
        }
        return chart;
    }"""

c = c.replace(old_items, new_items).replace(old_chart, new_chart)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
