import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/controller/PosOperationController.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if 'xoaMon' not in c:
    old = """    @PostMapping("/them-mon")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> themMon(@Valid @RequestBody PosThemMonRequestDTO request) {"""
    
    new = """    @DeleteMapping("/hoa-don/{hoaDonId}/mon/{chiTietId}")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> xoaMon(
            @PathVariable Long hoaDonId,
            @PathVariable Long chiTietId) {
        return ResponseEntity.ok(ApiResponse.success(posOperationService.xoaMon(hoaDonId, chiTietId)));
    }

    @PostMapping("/them-mon")
    public ResponseEntity<ApiResponse<HoaDonResponseDTO>> themMon(@Valid @RequestBody PosThemMonRequestDTO request) {"""
    
    c = c.replace(old, new)
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
