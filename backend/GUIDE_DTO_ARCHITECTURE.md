# 🍲 Luồng Dữ Liệu An Toàn: Repository -> Service -> DTO -> HTTP JSON

---

## 📋 Tổng Quan

Đây là hướng dẫn hoàn chỉnh để **tránh StackOverflowError** (do vòng lặp recursion) 
và **xử lý N+1 query** trong hệ thống Quản Lý Đặt Bàn Nhà Hàng.

Luồng an toàn:
```
1️⃣  Repository (tầng persistence)
    └─→ Dùng JOIN FETCH query tránh N+1
    └─→ Trả Entity (DỮ LIỆU THỨC từ DB)
    
2️⃣  Service (tầng logic)
    └─→ Nhận Entity từ Repository
    └─→ Chuyển đổi Entity → DTO ("nồi lẩu" → "khay thức ăn")
    └─→ DTO chỉ chứa scalar fields (String, Integer, Double, ...) - KHÔNG nested object
    
3️⃣  Controller (tầng HTTP)
    └─→ Nhận DTO từ Service
    └─→ Trả DTO dạng JSON (gọi Jackson serialize)
    └─→ JSON an toàn, không recursion, không N+1 query
```

---

## 🗂️ Cấu Trúc Thư Mục

```
src/main/java/com/QuanLyDatBanNhaHang/demo/
├── controller/
│   └── PhieuDatBanController.java          ← HTTP endpoint
├── service/
│   └── PhieuDatBanService.java             ← Business logic + Entity->DTO conversion
├── repository/
│   └── PhieuDatBanRepository.java          ← JOIN FETCH query
├── entity/
│   ├── PhieuDatBan.java                    ← Entity (from DB)
│   ├── KhachHang.java
│   ├── NhanVien.java
│   └── ... (14+ entity files)
└── dto/response/
    └── PhieuDatBanDTO.java                 ← DTO (safe, no recursion)
```

---

## 🔧 Chi Tiết Từng Tầng

### 1️⃣ Tầng Repository - JOIN FETCH Query

**File:** `repository/PhieuDatBanRepository.java`

```java
@Repository
public interface PhieuDatBanRepository extends JpaRepository<PhieuDatBan, String> {
    
    /**
     * ✅ TRÁNH N+1 QUERY
     * Thay vì: 1 query SELECT phiếu + N query SELECT khachHang + N query SELECT nhanVien
     * Dùng:     1 query SELECT với JOIN FETCH (tất cả trong 1 result set)
     * 
     * SQL tương đương:
     * SELECT DISTINCT p.* FROM PhieuDatBan p
     * JOIN KhachHang k ON p.maKH = k.maKH
     * JOIN NhanVien n ON p.maNV = n.maNV
     */
    @Query("SELECT DISTINCT p FROM PhieuDatBan p " +
           "JOIN FETCH p.khachHang k " +
           "JOIN FETCH p.nhanVien n")
    List<PhieuDatBan> findAllWithRelations();
}
```

**Lợi ích:**
- ✅ Chỉ 1 query (không N+1)
- ✅ Dữ liệu đã eager-load, tránh lazy exception
- ✅ Hiệu năng cao

---

### 2️⃣ Tầng Service - Entity → DTO Conversion

**File:** `service/PhieuDatBanService.java`

```java
@Service
@RequiredArgsConstructor
public class PhieuDatBanService {
    
    private final PhieuDatBanRepository phieuDatBanRepository;
    
    /**
     * Bên ngoài (Client/API) KHÔNG bao giờ nhận Entity trực tiếp
     * Thay vào đó: nhận DTO (chỉ có fields cần thiết)
     */
    public List<PhieuDatBanDTO> getAllPhieuDatBan() {
        // 1️⃣ Repository trả Entity (từ DB, kèm JOIN FETCH)
        List<PhieuDatBan> phieus = phieuDatBanRepository.findAllWithRelations();
        
        // 2️⃣ Chuyển đổi Entity → DTO (bốc fields cần thiết)
        return phieus.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Chuyển đổi Entity PhieuDatBan sang DTO
     * ⭐ ĐÂY LÀ [KHAY] (DTO) - chỉ có dữ liệu cần trả cho client
     * ⭐ KHÔNG bao gồm toàn bộ object KhachHang/NhanVien (tránh recursion)
     */
    private PhieuDatBanDTO convertToDTO(PhieuDatBan phieu) {
        return PhieuDatBanDTO.builder()
                .maPhieuDat(phieu.getMaPhieuDat())
                .thoiGianDen(phieu.getThoiGianDen())
                .soLuongNguoi(phieu.getSoLuongNguoi())
                .trangThai(phieu.getTrangThai())
                // 🎯 Bốc SCALAR FIELDS từ quan hệ (không nested object)
                .tenKH(phieu.getKhachHang().getHoTen())    // String, safe
                .sdtKH(phieu.getKhachHang().getSdt())        // String, safe
                .hoTenNV(phieu.getNhanVien().getHoTen())     // String, safe
                // ❌ KHÔNG bao gồm: .khachHang(phieu.getKhachHang()) 
                //                   vì sẽ gây recursion khi JSON serialize
                .build();
    }
}
```

**Lợi ích:**
- ✅ Entity (nồi lẩu) không bao giờ expose ra ngoài
- ✅ DTO chỉ chứa fields cần thiết (kích thước nhỏ, bảo mật tốt)
- ✅ Tránh recursion vì không có nested object

---

### 3️⃣ DTO - An Toàn (Chỉ Scalar Fields)

**File:** `dto/response/PhieuDatBanDTO.java`

```java
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhieuDatBanDTO {
    // ✅ Scalar fields - SAFE để serialize JSON
    private String maPhieuDat;
    private LocalDateTime thoiGianDen;
    private Integer soLuongNguoi;
    private String trangThai;
    
    // ✅ Bốc fields từ quan hệ (scalar, không nested Entity)
    private String tenKH;       // String từ KhachHang.hoTen
    private String sdtKH;       // String từ KhachHang.sdt
    private String hoTenNV;     // String từ NhanVien.hoTen
    
    // ❌ KHÔNG có: private KhachHang khachHang;  (nested object → recursion!)
    // ❌ KHÔNG có: private NhanVien nhanVien;    (nested object → recursion!)
}
```

**Cấu trúc DTO khi JSON serialize:**
```json
{
  "maPhieuDat": "PDB001",
  "thoiGianDen": "2026-06-12T14:30:00",
  "soLuongNguoi": 4,
  "trangThai": "DANG_CHO",
  "tenKH": "Nguyễn Văn A",
  "sdtKH": "0901234567",
  "hoTenNV": "Trần Thị B"
}
```

✅ **Hoàn toàn planar** (không nested) → tránh recursion!

---

### 4️⃣ Controller - Trả HTTP JSON

**File:** `controller/PhieuDatBanController.java`

```java
@RestController
@RequestMapping("/api/v1/phieu-dat-ban")
@RequiredArgsConstructor
public class PhieuDatBanController {
    
    private final PhieuDatBanService phieuDatBanService;
    
    /**
     * GET /api/v1/phieu-dat-ban
     * 
     * 1️⃣  Controller gọi service.getAllPhieuDatBan()
     * 2️⃣  Service gọi repository.findAllWithRelations() (JOIN FETCH - 1 query)
     * 3️⃣  Service chuyển Entity → DTO
     * 4️⃣  @RestController tự động serialize DTO → JSON
     * 
     * ✅ Kết quả: JSON an toàn, planar, không recursion
     */
    @GetMapping
    public ResponseEntity<List<PhieuDatBanDTO>> getAllPhieuDatBan() {
        List<PhieuDatBanDTO> phieus = phieuDatBanService.getAllPhieuDatBan();
        return ResponseEntity.ok(phieus);  // ← Spring/Jackson serialize DTO → JSON
    }
}
```

---

## 🎯 So Sánh: ❌ Cách Sai vs ✅ Cách Đúng

### ❌ CÁCH SAI (dễ gặp lỗi):

```java
// ❌ KHÔNG NÊN: trả Entity trực tiếp
@GetMapping
public ResponseEntity<List<PhieuDatBan>> getAllPhieuDatBan() {
    List<PhieuDatBan> phieus = repository.findAll();  // Mặc định EAGER (N+1)
    return ResponseEntity.ok(phieus);  // Jackson serialize Entity
    // ⚠️ Khi Jackson traverse Entity.khachHang.phieuDatBans[0].khachHang...
    // ⚠️ → StackOverflowError (recursion vô hạn)
}

// ❌ KHÔNG NÊN: @Data (tự sinh toString/equals trên mọi field)
@Data  // ← Sẽ sinh code khiến StackOverflow khi ví dụ: System.out.println(phieu)
@Entity
public class PhieuDatBan {
    @ManyToOne
    private KhachHang khachHang;  // ← toString() sẽ call khachHang.toString()
                                  //   → phieuDatBans[0].toString()
                                  //   → khachHang.toString() again... vòng lặp!
}
```

### ✅ CÁCH ĐÚNG:

```java
// ✅ NÊN: dùng DTO
@GetMapping
public ResponseEntity<List<PhieuDatBanDTO>> getAllPhieuDatBan() {
    List<PhieuDatBanDTO> phieus = service.getAllPhieuDatBan();
    return ResponseEntity.ok(phieus);  // ✅ DTO planar, asn toàn
}

// ✅ NÊN: @Getter/@Setter (không sinh toString/equals tự động)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class PhieuDatBan {
    @ManyToOne(fetch = FetchType.LAZY)
    private KhachHang khachHang;  // ← LAZY, và không sinh toString/equals
}

// ✅ NÊN: DTO chỉ có scalar
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PhieuDatBanDTO {
    private String tenKH;  // ← string, an toàn
    private String hoTenNV;  // ← string, an toàn
    // ❌ KHÔNG: private KhachHang khachHang;
}
```

---

## 🧪 Kiểm Tra: Tests Đã Pass

**Service Test:** `src/test/java/.../PhieuDatBanServiceTest.java`

4 test case đã pass ✅:
1. `testGetAllPhieuDatBan` - Lấy tất cả, kiểm tra DTO an toàn
2. `testGetPhieuDatBanById` - Lấy 1 phiếu theo ID
3. `testNoN1Query` - Verify repository gọi 1 lần (JOIN FETCH tránh N+1)
4. `testDTOSafeData` - Verify DTO không expose field nhạy cảm

```bash
# Chạy test:
cd C:\QuanLyDatBanNhaHang
.\mvnw.cmd test -Dtest=PhieuDatBanServiceTest

# Kết quả:
# Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
```

---

## 📝 Cách Tạo Repository Query Mới (Template)

Nếu cần tạo query mới cho entity khác (ví dụ: HoaDon, ChiTietHoaDon, ...):

```java
@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    
    /**
     * Lấy tất cả hóa đơn kèm chi tiết (PhieuDatBan, NhanVien, Thue)
     * ⭐ Dùng JOIN FETCH để tránh N+1
     */
    @Query("SELECT DISTINCT h FROM HoaDon h " +
           "JOIN FETCH h.phieuDatBan p " +
           "JOIN FETCH h.nhanVien n " +
           "JOIN FETCH h.thue t")
    List<HoaDon> findAllWithRelations();
}
```

Sau đó trong Service:
```java
@Service
public class HoaDonService {
    
    public List<HoaDonDTO> getAllHoaDon() {
        List<HoaDon> hoaDons = repository.findAllWithRelations();
        return hoaDons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private HoaDonDTO convertToDTO(HoaDon hoaDon) {
        return HoaDonDTO.builder()
                .maHD(hoaDon.getMaHD())
                .tongThanhToan(hoaDon.getTongThanhToan())
                .maPDB(hoaDon.getPhieuDatBan().getMaPhieuDat())  // ← scalar
                .tenNV(hoaDon.getNhanVien().getHoTen())           // ← scalar
                // ❌ KHÔNG: .phieuDatBan(hoaDon.getPhieuDatBan())
                .build();
    }
}
```

---

## 🚀 Chạy Ứng Dụng

```bash
# 1. Compile
cd C:\QuanLyDatBanNhaHang
.\mvnw.cmd clean compile

# 2. Chạy ứng dụng (nếu setup DB đúng)
.\mvnw.cmd spring-boot:run

# 3. Test endpoint (bằng cURL hoặc Postman)
curl http://localhost:8080/api/v1/phieu-dat-ban

# Output: JSON list các phiếu (an toàn, không recursion)
# [
#   {
#     "maPhieuDat": "PDB001",
#     "thoiGianDen": "2026-06-12T14:30:00",
#     "soLuongNguoi": 4,
#     "trangThai": "DANG_CHO",
#     "tenKH": "Nguyễn Văn A",
#     "hoTenNV": "Trần Thị B"
#   },
#   ...
# ]
```

---

## 💡 Lưu Ý Quan Trọng

1. **Luôn dùng DTO cho API response** - không bao giờ trả Entity trực tiếp
2. **JOIN FETCH trong Repository** - tránh N+1 query
3. **@Getter/@Setter thay @Data** - tránh sinh toString/equals gây recursion
4. **Tất cả @ManyToOne/@OneToOne = FetchType.LAZY** - tránh eager load quá nhiều
5. **DTO chỉ scalar fields** - không nested object

---

## 📚 Tham Khảo Thêm

- **Entity:** `src/main/java/.../entity/` (15 files)
- **Repository:** `src/main/java/.../repository/PhieuDatBanRepository.java`
- **Service:** `src/main/java/.../service/PhieuDatBanService.java`
- **DTO:** `src/main/java/.../dto/response/PhieuDatBanDTO.java`
- **Controller:** `src/main/java/.../controller/PhieuDatBanController.java`
- **Test:** `src/test/java/.../PhieuDatBanServiceTest.java` (4 tests ✅)

---

## 🎓 Kết Luận

Luồng Repository → Service → DTO → JSON này:
✅ **Tránh StackOverflowError** (không recursion)
✅ **Tránh N+1 query** (dùng JOIN FETCH)
✅ **An toàn dữ liệu** (DTO chỉ expose fields cần thiết)
✅ **Hiệu năng cao** (1 query, dữ liệu planar)

**Đây là best practice trong Spring Boot + JPA production!**

