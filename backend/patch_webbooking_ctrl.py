import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/controller/WebBookingController.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if 'getAvailableTables' not in c:
    old = """    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBooking(@Valid @RequestBody WebBookingRequestDTO request) {"""
    
    new = """    @GetMapping("/available-tables")
    public ResponseEntity<ApiResponse<java.util.List<java.util.Map<String, Object>>>> getAvailableTables(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime thoiGianDen) {
        return ResponseEntity.ok(ApiResponse.success(webBookingService.getAvailableTables(thoiGianDen)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBooking(@Valid @RequestBody WebBookingRequestDTO request) {"""
    
    c = c.replace(old, new)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
