import os, re

base_path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo'

# 1. BanAn
repo = f'{base_path}/repository/BanAnRepository.java'
with open(repo, 'r', encoding='utf-8') as f: c = f.read()
if 'searchBanAn' not in c:
    c = c.replace('Page<BanAn> findAllWithRelations(Pageable pageable);', 
'''Page<BanAn> findAllWithRelations(Pageable pageable);

    @Query(value = "SELECT b FROM BanAn b LEFT JOIN FETCH b.khuVuc " +
                   "WHERE (:keyword IS NULL OR LOWER(b.maBan) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR b.trangThai = :trangThai)",
           countQuery = "SELECT COUNT(b) FROM BanAn b " +
                   "WHERE (:keyword IS NULL OR LOWER(b.maBan) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR b.trangThai = :trangThai)")
    Page<BanAn> searchBanAn(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn trangThai, Pageable pageable);''')
    with open(repo, 'w', encoding='utf-8') as f: f.write(c)

svc = f'{base_path}/service/BanAnService.java'
with open(svc, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'Page<BanAnResponseDTO> getAllBanAn\(Pageable pageable\);', 'Page<BanAnResponseDTO> getAllBanAn(String keyword, String trangThai, Pageable pageable);', c)
with open(svc, 'w', encoding='utf-8') as f: f.write(c)

svc_impl = f'{base_path}/service/impl/BanAnServiceImpl.java'
with open(svc_impl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public Page<BanAnResponseDTO> getAllBanAn\(Pageable pageable\) \{\s*return banAnRepository\.findAllWithRelations\(pageable\)\.map\(this::convertToResponseDTO\);\s*\}', 
'''public Page<BanAnResponseDTO> getAllBanAn(String keyword, String trangThai, Pageable pageable) {
        com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn enumTrangThai = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try { enumTrangThai = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiBanAn.valueOf(trangThai); } catch(Exception e) {}
        }
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return banAnRepository.searchBanAn(kw, enumTrangThai, pageable).map(this::convertToResponseDTO);
    }''', c)
with open(svc_impl, 'w', encoding='utf-8') as f: f.write(c)

ctrl = f'{base_path}/controller/BanAnController.java'
with open(ctrl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public ResponseEntity<ApiResponse<Page<BanAnResponseDTO>>> getAllBanAn\(Pageable pageable\) \{\s*return ResponseEntity\.ok\(ApiResponse\.success\(banAnService\.getAllBanAn\(pageable\)\)\);\s*\}',
'''public ResponseEntity<ApiResponse<Page<BanAnResponseDTO>>> getAllBanAn(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(banAnService.getAllBanAn(keyword, trangThai, pageable)));
    }''', c)
with open(ctrl, 'w', encoding='utf-8') as f: f.write(c)

# 2. MonAn
repo = f'{base_path}/repository/MonAnRepository.java'
with open(repo, 'r', encoding='utf-8') as f: c = f.read()
if 'searchMonAn' not in c:
    c = c.replace('Page<MonAn> findByTenMonContainingIgnoreCase(String tenMon, Pageable pageable);', 
'''Page<MonAn> findByTenMonContainingIgnoreCase(String tenMon, Pageable pageable);

    @Query(value = "SELECT m FROM MonAn m " +
                   "WHERE (:keyword IS NULL OR LOWER(m.tenMon) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR m.trangThai = :trangThai)",
           countQuery = "SELECT COUNT(m) FROM MonAn m " +
                   "WHERE (:keyword IS NULL OR LOWER(m.tenMon) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR m.trangThai = :trangThai)")
    Page<MonAn> searchMonAn(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn trangThai, Pageable pageable);''')
    with open(repo, 'w', encoding='utf-8') as f: f.write(c)

svc = f'{base_path}/service/MonAnService.java'
with open(svc, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'Page<MonAnResponseDTO> getAllMonAn\(Pageable pageable\);', 'Page<MonAnResponseDTO> getAllMonAn(String keyword, String trangThai, Pageable pageable);', c)
with open(svc, 'w', encoding='utf-8') as f: f.write(c)

svc_impl = f'{base_path}/service/impl/MonAnServiceImpl.java'
with open(svc_impl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public Page<MonAnResponseDTO> getAllMonAn\(Pageable pageable\) \{\s*return monAnRepository\.findAll\(pageable\)\.map\(this::convertToResponseDTO\);\s*\}', 
'''public Page<MonAnResponseDTO> getAllMonAn(String keyword, String trangThai, Pageable pageable) {
        com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn enumTrangThai = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try { enumTrangThai = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiMonAn.valueOf(trangThai); } catch(Exception e) {}
        }
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return monAnRepository.searchMonAn(kw, enumTrangThai, pageable).map(this::convertToResponseDTO);
    }''', c)
with open(svc_impl, 'w', encoding='utf-8') as f: f.write(c)

ctrl = f'{base_path}/controller/MonAnController.java'
with open(ctrl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public ResponseEntity<ApiResponse<Page<MonAnResponseDTO>>> getAllMonAn\(Pageable pageable\) \{\s*return ResponseEntity\.ok\(ApiResponse\.success\(monAnService\.getAllMonAn\(pageable\)\)\);\s*\}',
'''public ResponseEntity<ApiResponse<Page<MonAnResponseDTO>>> getAllMonAn(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(monAnService.getAllMonAn(keyword, trangThai, pageable)));
    }''', c)
with open(ctrl, 'w', encoding='utf-8') as f: f.write(c)

# 3. KhuyenMai
repo = f'{base_path}/repository/KhuyenMaiRepository.java'
with open(repo, 'r', encoding='utf-8') as f: c = f.read()
if 'searchKhuyenMai' not in c:
    c = c.replace('Page<KhuyenMai> findByTenKhuyenMaiContainingIgnoreCase(String tenKhuyenMai, Pageable pageable);', 
'''Page<KhuyenMai> findByTenKhuyenMaiContainingIgnoreCase(String tenKhuyenMai, Pageable pageable);

    @Query(value = "SELECT k FROM KhuyenMai k " +
                   "WHERE (:keyword IS NULL OR LOWER(k.tenKhuyenMai) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR k.trangThai = :trangThai)",
           countQuery = "SELECT COUNT(k) FROM KhuyenMai k " +
                   "WHERE (:keyword IS NULL OR LOWER(k.tenKhuyenMai) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR k.trangThai = :trangThai)")
    Page<KhuyenMai> searchKhuyenMai(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai trangThai, Pageable pageable);''')
    with open(repo, 'w', encoding='utf-8') as f: f.write(c)

svc = f'{base_path}/service/KhuyenMaiService.java'
with open(svc, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'Page<KhuyenMaiResponseDTO> getAllKhuyenMai\(Pageable pageable\);', 'Page<KhuyenMaiResponseDTO> getAllKhuyenMai(String keyword, String trangThai, Pageable pageable);', c)
with open(svc, 'w', encoding='utf-8') as f: f.write(c)

svc_impl = f'{base_path}/service/impl/KhuyenMaiServiceImpl.java'
with open(svc_impl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public Page<KhuyenMaiResponseDTO> getAllKhuyenMai\(Pageable pageable\) \{\s*return khuyenMaiRepository\.findAll\(pageable\)\.map\(this::convertToResponseDTO\);\s*\}', 
'''public Page<KhuyenMaiResponseDTO> getAllKhuyenMai(String keyword, String trangThai, Pageable pageable) {
        com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai enumTrangThai = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try { enumTrangThai = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiKhuyenMai.valueOf(trangThai); } catch(Exception e) {}
        }
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return khuyenMaiRepository.searchKhuyenMai(kw, enumTrangThai, pageable).map(this::convertToResponseDTO);
    }''', c)
with open(svc_impl, 'w', encoding='utf-8') as f: f.write(c)

ctrl = f'{base_path}/controller/KhuyenMaiController.java'
with open(ctrl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public ResponseEntity<ApiResponse<Page<KhuyenMaiResponseDTO>>> getAllKhuyenMai\(Pageable pageable\) \{\s*return ResponseEntity\.ok\(ApiResponse\.success\(khuyenMaiService\.getAllKhuyenMai\(pageable\)\)\);\s*\}',
'''public ResponseEntity<ApiResponse<Page<KhuyenMaiResponseDTO>>> getAllKhuyenMai(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(khuyenMaiService.getAllKhuyenMai(keyword, trangThai, pageable)));
    }''', c)
with open(ctrl, 'w', encoding='utf-8') as f: f.write(c)

# 4. HoaDon
repo = f'{base_path}/repository/HoaDonRepository.java'
with open(repo, 'r', encoding='utf-8') as f: c = f.read()
if 'searchHoaDon' not in c:
    c = c.replace('Page<HoaDon> findAllWithRelations(Pageable pageable);', 
'''Page<HoaDon> findAllWithRelations(Pageable pageable);

    @Query(value = "SELECT h FROM HoaDon h " +
                   "LEFT JOIN FETCH h.khachHang " +
                   "LEFT JOIN FETCH h.nhanVien " +
                   "LEFT JOIN FETCH h.banAn " +
                   "LEFT JOIN FETCH h.phieuDatBan " +
                   "LEFT JOIN FETCH h.khuyenMai " +
                   "WHERE (:keyword IS NULL OR LOWER(h.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR h.trangThaiThanhToan = :trangThai)",
           countQuery = "SELECT COUNT(h) FROM HoaDon h " +
                   "WHERE (:keyword IS NULL OR LOWER(h.maHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                   "AND (:trangThai IS NULL OR h.trangThaiThanhToan = :trangThai)")
    Page<HoaDon> searchHoaDon(@Param("keyword") String keyword, @Param("trangThai") com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToan trangThai, Pageable pageable);''')
    with open(repo, 'w', encoding='utf-8') as f: f.write(c)

svc = f'{base_path}/service/HoaDonService.java'
with open(svc, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'Page<HoaDonResponseDTO> getAllHoaDon\(Pageable pageable\);', 'Page<HoaDonResponseDTO> getAllHoaDon(String keyword, String trangThai, Pageable pageable);', c)
with open(svc, 'w', encoding='utf-8') as f: f.write(c)

svc_impl = f'{base_path}/service/impl/HoaDonServiceImpl.java'
with open(svc_impl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public Page<HoaDonResponseDTO> getAllHoaDon\(Pageable pageable\) \{\s*return hoaDonRepository\.findAllWithRelations\(pageable\)\.map\(this::convertToResponseDTO\);\s*\}', 
'''public Page<HoaDonResponseDTO> getAllHoaDon(String keyword, String trangThai, Pageable pageable) {
        com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToan enumTrangThai = null;
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            try { enumTrangThai = com.QuanLyDatBanNhaHang.demo.enums.TrangThaiThanhToan.valueOf(trangThai); } catch(Exception e) {}
        }
        String kw = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return hoaDonRepository.searchHoaDon(kw, enumTrangThai, pageable).map(this::convertToResponseDTO);
    }''', c)
with open(svc_impl, 'w', encoding='utf-8') as f: f.write(c)

ctrl = f'{base_path}/controller/HoaDonController.java'
with open(ctrl, 'r', encoding='utf-8') as f: c = f.read()
c = re.sub(r'public ResponseEntity<ApiResponse<Page<HoaDonResponseDTO>>> getAllHoaDon\(Pageable pageable\) \{\s*return ResponseEntity\.ok\(ApiResponse\.success\(hoaDonService\.getAllHoaDon\(pageable\)\)\);\s*\}',
'''public ResponseEntity<ApiResponse<Page<HoaDonResponseDTO>>> getAllHoaDon(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(hoaDonService.getAllHoaDon(keyword, trangThai, pageable)));
    }''', c)
with open(ctrl, 'w', encoding='utf-8') as f: f.write(c)

print('Done backend injection')
