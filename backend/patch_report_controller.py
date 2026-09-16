import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/controller/ReportController.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old1 = """    @GetMapping("/top-items")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<TopItemProjection>>> getTopItems() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getTopItems()));
    }"""
new1 = """    @GetMapping("/top-items")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<TopItemProjection>>> getTopItems(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getTopItems(startDate, endDate)));
    }"""

old2 = """    @GetMapping("/revenue-chart")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRevenueChart() {
        return ResponseEntity.ok(ApiResponse.success(reportService.getRevenueChart()));
    }"""
new2 = """    @GetMapping("/revenue-chart")
    @PreAuthorize("hasAnyRole('ADMIN', 'NHAN_VIEN')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRevenueChart(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getRevenueChart(startDate, endDate)));
    }"""

c = c.replace(old1, new1).replace(old2, new2)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
