import os
import re

# 1. Update DTOs
base_dir = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/dto/request/'
dtos = [
    'NhanVienCreateRequestDTO.java',
    'NhanVienUpdateRequestDTO.java',
    'KhachHangCreateRequestDTO.java',
    'KhachHangUpdateRequestDTO.java'
]
for dto in dtos:
    path = os.path.join(base_dir, dto)
    with open(path, 'r', encoding='utf-8') as f: c = f.read()
    if 'private String password;' not in c:
        c = c.replace('private String username;', 'private String username;\n    private String password;')
        with open(path, 'w', encoding='utf-8') as f: f.write(c)

# 2. Update NhanVienServiceImpl
path_nv = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/NhanVienServiceImpl.java'
with open(path_nv, 'r', encoding='utf-8') as f: c = f.read()

if 'PasswordEncoder' not in c:
    c = c.replace('import org.springframework.stereotype.Service;', 'import org.springframework.stereotype.Service;\nimport org.springframework.security.crypto.password.PasswordEncoder;\nimport com.QuanLyDatBanNhaHang.demo.enums.QuyenHanTaiKhoan;')
if 'private final PasswordEncoder passwordEncoder;' not in c:
    c = c.replace('private final TaiKhoanRepository taiKhoanRepository;', 'private final TaiKhoanRepository taiKhoanRepository;\n    private final PasswordEncoder passwordEncoder;')

c = re.sub(
r'TaiKhoan tk = null;\s*if \(requestDTO\.getUsername\(\) != null && !requestDTO\.getUsername\(\)\.isBlank\(\)\) \{\s*tk = taiKhoanRepository\.findByUsernameIgnoreCase\(requestDTO\.getUsername\(\)\)\s*\.orElseThrow\(\(\) -> new ResourceNotFoundException\([^)]+\)\);\s*\}',
r'''TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
            }
            if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản");
            }
            tk = new TaiKhoan();
            tk.setUsername(requestDTO.getUsername());
            tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            tk.setQuyenHan(QuyenHanTaiKhoan.NHAN_VIEN);
            tk.setTrangThai(true);
            tk = taiKhoanRepository.save(tk);
        }''', c, count=1)

c = re.sub(
r'TaiKhoan tk = null;\s*if \(requestDTO\.getUsername\(\) != null && !requestDTO\.getUsername\(\)\.isBlank\(\)\) \{\s*tk = taiKhoanRepository\.findByUsernameIgnoreCase\(requestDTO\.getUsername\(\)\)\s*\.orElseThrow\(\(\) -> new ResourceNotFoundException\([^)]+\)\);\s*\}',
r'''TaiKhoan tk = nv.getTaiKhoan();
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            if (tk != null) {
                tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                taiKhoanRepository.save(tk);
            } else {
                // If there's no account yet, maybe they want to create one during update? 
                if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
                    if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                        throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
                    }
                    tk = new TaiKhoan();
                    tk.setUsername(requestDTO.getUsername());
                    tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                    tk.setQuyenHan(QuyenHanTaiKhoan.NHAN_VIEN);
                    tk.setTrangThai(true);
                    tk = taiKhoanRepository.save(tk);
                }
            }
        }''', c, count=1)

with open(path_nv, 'w', encoding='utf-8') as f: f.write(c)

# 3. Update KhachHangServiceImpl
path_kh = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/KhachHangServiceImpl.java'
with open(path_kh, 'r', encoding='utf-8') as f: c = f.read()

if 'PasswordEncoder' not in c:
    c = c.replace('import org.springframework.stereotype.Service;', 'import org.springframework.stereotype.Service;\nimport org.springframework.security.crypto.password.PasswordEncoder;\nimport com.QuanLyDatBanNhaHang.demo.enums.QuyenHanTaiKhoan;')
if 'private final PasswordEncoder passwordEncoder;' not in c:
    c = c.replace('private final TaiKhoanRepository taiKhoanRepository;', 'private final TaiKhoanRepository taiKhoanRepository;\n    private final PasswordEncoder passwordEncoder;')

c = re.sub(
r'TaiKhoan tk = null;\s*if \(requestDTO\.getUsername\(\) != null && !requestDTO\.getUsername\(\)\.isBlank\(\)\) \{\s*tk = taiKhoanRepository\.findByUsernameIgnoreCase\(requestDTO\.getUsername\(\)\)\s*\.orElseThrow\(\(\) -> new ResourceNotFoundException\([^)]+\)\);\s*\}',
r'''TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
            }
            if (requestDTO.getPassword() == null || requestDTO.getPassword().isBlank()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo tài khoản");
            }
            tk = new TaiKhoan();
            tk.setUsername(requestDTO.getUsername());
            tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            tk.setQuyenHan(QuyenHanTaiKhoan.KHACH_HANG);
            tk.setTrangThai(true);
            tk = taiKhoanRepository.save(tk);
        }''', c, count=1)

c = re.sub(
r'TaiKhoan tk = null;\s*if \(requestDTO\.getUsername\(\) != null && !requestDTO\.getUsername\(\)\.isBlank\(\)\) \{\s*tk = taiKhoanRepository\.findByUsernameIgnoreCase\(requestDTO\.getUsername\(\)\)\s*\.orElseThrow\(\(\) -> new ResourceNotFoundException\([^)]+\)\);\s*\}',
r'''TaiKhoan tk = kh.getTaiKhoan();
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            if (tk != null) {
                tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                taiKhoanRepository.save(tk);
            } else {
                if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
                    if (taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).isPresent()) {
                        throw new DuplicateResourceException("Tên đăng nhập đã tồn tại trong hệ thống");
                    }
                    tk = new TaiKhoan();
                    tk.setUsername(requestDTO.getUsername());
                    tk.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
                    tk.setQuyenHan(QuyenHanTaiKhoan.KHACH_HANG);
                    tk.setTrangThai(true);
                    tk = taiKhoanRepository.save(tk);
                }
            }
        }''', c, count=1)

with open(path_kh, 'w', encoding='utf-8') as f: f.write(c)

print("Backend patched successfully")
