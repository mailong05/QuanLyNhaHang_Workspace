import os
import re

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo"

# 1. ChiTietPhieuDatBanRepository
repo_ctpdb = os.path.join(base_dir, "repository", "ChiTietPhieuDatBanRepository.java")
with open(repo_ctpdb, "r", encoding="utf-8") as f: content = f.read()
if "findConflictingBookings" not in content:
    if "import org.springframework.data.repository.query.Param;" not in content:
        content = content.replace("import org.springframework.stereotype.Repository;", "import org.springframework.stereotype.Repository;\nimport org.springframework.data.repository.query.Param;\nimport org.springframework.data.jpa.repository.Query;\nimport java.time.LocalDateTime;\nimport java.util.List;")
    
    query = """
    @Query("SELECT c FROM ChiTietPhieuDatBan c WHERE c.banAn.maBan = :maBan " +
           "AND c.phieuDatBan.thoiGianDen BETWEEN :startTime AND :endTime " +
           "AND c.phieuDatBan.trangThai != 'DA_HUY' " +
           "AND (:excludePhieuId IS NULL OR c.phieuDatBan.id != :excludePhieuId)")
    List<ChiTietPhieuDatBan> findConflictingBookings(@Param("maBan") String maBan, 
                                                     @Param("startTime") LocalDateTime startTime, 
                                                     @Param("endTime") LocalDateTime endTime,
                                                     @Param("excludePhieuId") Long excludePhieuId);
"""
    content = content.replace("}", query + "\n}")
    with open(repo_ctpdb, "w", encoding="utf-8") as f: f.write(content)

# 2. PhieuDatBanServiceImpl
impl_pdb = os.path.join(base_dir, "service", "impl", "PhieuDatBanServiceImpl.java")
with open(impl_pdb, "r", encoding="utf-8") as f: content = f.read()

if "import java.time.LocalDateTime;" not in content:
    content = content.replace("import java.util.List;", "import java.util.List;\nimport java.time.LocalDateTime;")
if "ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;" not in content:
    content = content.replace("private final PhieuDatBanRepository phieuDatBanRepository;", "private final PhieuDatBanRepository phieuDatBanRepository;\n    private final com.QuanLyDatBanNhaHang.demo.repository.ChiTietPhieuDatBanRepository chiTietPhieuDatBanRepository;")

create_validation = """
        if (requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }

        if (requestDTO.getChiTiets() != null) {
            LocalDateTime start = requestDTO.getThoiGianDen().minusHours(2);
            LocalDateTime end = requestDTO.getThoiGianDen().plusHours(2);
            for (var ct : requestDTO.getChiTiets()) {
                if (!chiTietPhieuDatBanRepository.findConflictingBookings(ct.getMaBan(), start, end, null).isEmpty()) {
                    throw new IllegalArgumentException("Bàn " + ct.getMaBan() + " đã được đặt trong khoảng thời gian này.");
                }
            }
        }
"""
update_validation = """
        if (requestDTO.getThoiGianDen().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalArgumentException("Thời gian đến phải lớn hơn thời gian hiện tại ít nhất 30 phút.");
        }

        if (requestDTO.getChiTiets() != null) {
            LocalDateTime start = requestDTO.getThoiGianDen().minusHours(2);
            LocalDateTime end = requestDTO.getThoiGianDen().plusHours(2);
            for (var ct : requestDTO.getChiTiets()) {
                if (!chiTietPhieuDatBanRepository.findConflictingBookings(ct.getMaBan(), start, end, pdb.getId()).isEmpty()) {
                    throw new IllegalArgumentException("Bàn " + ct.getMaBan() + " đã được đặt trong khoảng thời gian này.");
                }
            }
        }
"""
# inject to create
if "Thời gian đến phải lớn hơn thời gian hiện tại" not in content:
    content = re.sub(r'(public PhieuDatBanResponseDTO createPhieuDatBan.*?\{)', r'\1\n' + create_validation, content, flags=re.DOTALL)
    content = re.sub(r'(public PhieuDatBanResponseDTO updatePhieuDatBan.*?\{[^{]*?PhieuDatBan pdb = [^;]+;)', r'\1\n' + update_validation, content, flags=re.DOTALL)
with open(impl_pdb, "w", encoding="utf-8") as f: f.write(content)

# 3. HoaDonServiceImpl
impl_hd = os.path.join(base_dir, "service", "impl", "HoaDonServiceImpl.java")
with open(impl_hd, "r", encoding="utf-8") as f: content = f.read()

if "import java.time.LocalDate;" not in content:
    content = content.replace("import java.util.List;", "import java.util.List;\nimport java.time.LocalDate;")

km_validation_create = """
        if (km != null) {
            LocalDate ngayTao = LocalDate.now();
            if (ngayTao.isBefore(km.getNgayBatDau()) || ngayTao.isAfter(km.getNgayKetThuc())) {
                throw new IllegalArgumentException("Khuyến mãi không nằm trong thời gian áp dụng.");
            }
            if (tongTienGoc < km.getDieuKienToiThieu()) {
                throw new IllegalArgumentException("Chưa đạt điều kiện tối thiểu để áp dụng khuyến mãi.");
            }
        }
"""
# inject right before hd = HoaDon.builder()
if "Khuyến mãi không nằm trong thời gian áp dụng" not in content:
    content = content.replace("HoaDon hd = HoaDon.builder()", km_validation_create + "\n        HoaDon hd = HoaDon.builder()")
    content = content.replace("hd.setKhuyenMai(km);", km_validation_create + "\n        hd.setKhuyenMai(km);")
with open(impl_hd, "w", encoding="utf-8") as f: f.write(content)

# 4. CaLamViecServiceImpl
impl_ca = os.path.join(base_dir, "service", "impl", "CaLamViecServiceImpl.java")
with open(impl_ca, "r", encoding="utf-8") as f: content = f.read()

ca_validation = """
        if (requestDTO.getGioKetThuc().isBefore(requestDTO.getGioBatDau())) {
            throw new IllegalArgumentException("Giờ kết thúc phải lớn hơn giờ bắt đầu.");
        }
"""
if "Giờ kết thúc phải lớn hơn giờ bắt đầu" not in content:
    content = re.sub(r'(public CaLamViecResponseDTO createCaLamViec.*?\{)', r'\1\n' + ca_validation, content, flags=re.DOTALL)
    content = re.sub(r'(public CaLamViecResponseDTO updateCaLamViec.*?\{)', r'\1\n' + ca_validation, content, flags=re.DOTALL)
with open(impl_ca, "w", encoding="utf-8") as f: f.write(content)

print("Phase 2 completed.")
