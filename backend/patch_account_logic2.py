import os

# 1. Update NhanVienServiceImpl
path_nv = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/NhanVienServiceImpl.java'
with open(path_nv, 'r', encoding='utf-8') as f: c = f.read()

old_tk_block = """        TaiKhoan tk = null;
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            tk = taiKhoanRepository.findByUsernameIgnoreCase(requestDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("KhA'ng tAm thy TAi khon: " + requestDTO.getUsername()));
        }"""
# Wait, let me just find all blocks that look like this.
# Instead of hardcoding the non-ASCII chars, let me just find the lines.
lines = c.split('\n')
new_lines = []
in_tk = False
tk_type = '' # 'create' or 'update'
for line in lines:
    if 'public NhanVienResponseDTO createNhanVien' in line:
        tk_type = 'create'
    elif 'public NhanVienResponseDTO updateNhanVien' in line:
        tk_type = 'update'
    
    if 'TaiKhoan tk = null;' in line:
        in_tk = True
        if tk_type == 'create':
            new_lines.append("""        TaiKhoan tk = null;
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
        }""")
        elif tk_type == 'update':
            new_lines.append("""        TaiKhoan tk = nv.getTaiKhoan();
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
                    tk.setQuyenHan(QuyenHanTaiKhoan.NHAN_VIEN);
                    tk.setTrangThai(true);
                    tk = taiKhoanRepository.save(tk);
                }
            }
        }""")
        continue

    if in_tk:
        if '}' in line and 'orElseThrow' not in line:
            in_tk = False
        continue

    new_lines.append(line)

with open(path_nv, 'w', encoding='utf-8') as f: f.write('\n'.join(new_lines))


# 2. Update KhachHangServiceImpl
path_kh = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/service/impl/KhachHangServiceImpl.java'
with open(path_kh, 'r', encoding='utf-8') as f: c = f.read()

lines = c.split('\n')
new_lines = []
in_tk = False
tk_type = '' # 'create' or 'update'
for line in lines:
    if 'public KhachHangResponseDTO createKhachHang' in line:
        tk_type = 'create'
    elif 'public KhachHangResponseDTO updateKhachHang' in line:
        tk_type = 'update'
    
    if 'TaiKhoan tk = null;' in line:
        in_tk = True
        if tk_type == 'create':
            new_lines.append("""        TaiKhoan tk = null;
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
        }""")
        elif tk_type == 'update':
            new_lines.append("""        TaiKhoan tk = kh.getTaiKhoan();
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
        }""")
        continue

    if in_tk:
        if '}' in line and 'orElseThrow' not in line:
            in_tk = False
        continue

    new_lines.append(line)

with open(path_kh, 'w', encoding='utf-8') as f: f.write('\n'.join(new_lines))
