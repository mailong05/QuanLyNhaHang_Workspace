import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/controller/PosOperationController.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

old = """    @PostMapping("/thanh-toan/{hoaDonId}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> thanhToan(
            @PathVariable Long hoaDonId,
            @Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.PosThanhToanRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.thanhToanHoaDon(hoaDonId, request)));
    }
}"""

new = """    @PostMapping("/thanh-toan/{hoaDonId}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> thanhToan(
            @PathVariable Long hoaDonId,
            @Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.PosThanhToanRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.thanhToanHoaDon(hoaDonId, request)));
    }

    @PostMapping("/gop-ban")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> gopBan(@Valid @RequestBody com.QuanLyDatBanNhaHang.demo.dto.request.PosGopBanRequestDTO request) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.gopBan(request)));
    }
}"""

c = c.replace(old, new)
with open(path, 'w', encoding='utf-8') as f: f.write(c)
