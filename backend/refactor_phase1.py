import os
import re

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo"

entities_conf = [
    {"name": "KhachHang", "var": "k", "field": "maKH", "prefix": "KH", "digits": 4},
    {"name": "NhanVien", "var": "n", "field": "maNV", "prefix": "NV", "digits": 4},
    {"name": "HoaDon", "var": "h", "field": "maHD", "prefix": "HD", "digits": 6},
    {"name": "PhieuDatBan", "var": "p", "field": "maPhieuDat", "prefix": "PDB", "digits": 6},
    {"name": "BanAn", "var": "b", "field": "maBan", "prefix": "BA", "digits": 4},
    {"name": "CaLamViec", "var": "c", "field": "maCa", "prefix": "CA", "digits": 3},
    {"name": "KhuyenMai", "var": "km", "field": "maKM", "prefix": "KM", "digits": 4},
    {"name": "KhuVuc", "var": "kv", "field": "maKhuVuc", "prefix": "KV", "digits": 2},
    {"name": "MonAn", "var": "m", "field": "maMon", "prefix": "MA", "digits": 3},
    {"name": "Thue", "var": "t", "field": "maThue", "prefix": "TH", "digits": 2}
]

def update_repo(entity):
    repo_file = os.path.join(base_dir, "repository", f"{entity['name']}Repository.java")
    if not os.path.exists(repo_file): return
    with open(repo_file, "r", encoding="utf-8") as f: content = f.read()
    
    cap_field = entity["field"][0].upper() + entity["field"][1:]
    start_idx = len(entity["prefix"]) + 1 # JPQL is 1-indexed
    # CAST(SUBSTRING(e.ma, 3, 4) AS int)
    new_query = f'@Query("SELECT MAX(CAST(SUBSTRING({entity["var"]}.{entity["field"]}, {start_idx}, {entity["digits"]}) AS int)) FROM {entity["name"]} {entity["var"]}")\n    Integer findMax{cap_field}();'
    
    # replace old findMax
    content = re.sub(rf'@Query\("SELECT MAX\([^)]+\) FROM {entity["name"]} [^"]+"\)\s*String findMax{cap_field}\(\);', new_query, content)
    
    with open(repo_file, "w", encoding="utf-8") as f: f.write(content)

def update_service(entity):
    impl_file = os.path.join(base_dir, "service", "impl", f"{entity['name']}ServiceImpl.java")
    if not os.path.exists(impl_file): return
    with open(impl_file, "r", encoding="utf-8") as f: content = f.read()
    
    cap_field = entity["field"][0].upper() + entity["field"][1:]
    repo_var = entity['name'][:1].lower() + entity['name'][1:] + "Repository"
    
    # Replace the generateNext method
    old_method_regex = rf'private String generateNext{cap_field}\(\)\s*\{{.*?\n    \}}'
    
    new_method = f"""private String generateNext{cap_field}() {{
        Integer maxMa = {repo_var}.findMax{cap_field}();
        if (maxMa == null) {{
            return String.format("{entity['prefix']}%0{entity['digits']}d", 1);
        }}
        return String.format("{entity['prefix']}%0{entity['digits']}d", maxMa + 1);
    }}"""
    
    content = re.sub(old_method_regex, new_method, content, flags=re.DOTALL)
    with open(impl_file, "w", encoding="utf-8") as f: f.write(content)

for e in entities_conf:
    update_repo(e)
    update_service(e)

# Handle email logic
def add_email_to_entity(name):
    fpath = os.path.join(base_dir, "entity", f"{name}.java")
    with open(fpath, "r", encoding="utf-8") as f: content = f.read()
    if "String email;" not in content:
        content = re.sub(r'(private String sdt;)', r'\1\n\n    @Column(name = "email", length = 100)\n    private String email;', content)
        with open(fpath, "w", encoding="utf-8") as f: f.write(content)

def add_email_to_dto(name, dto_type):
    fpath = os.path.join(base_dir, "dto", "request" if "Request" in dto_type else "response", f"{name}{dto_type}.java")
    with open(fpath, "r", encoding="utf-8") as f: content = f.read()
    if "String email;" not in content:
        content = re.sub(r'(private String sdt;)', r'\1\n\n    private String email;', content)
        with open(fpath, "w", encoding="utf-8") as f: f.write(content)

for n in ["KhachHang", "NhanVien"]:
    add_email_to_entity(n)
    add_email_to_dto(n, "CreateRequestDTO")
    add_email_to_dto(n, "UpdateRequestDTO")
    add_email_to_dto(n, "ResponseDTO")
    
    # Update mapping in ServiceImpl
    impl_path = os.path.join(base_dir, "service", "impl", f"{n}ServiceImpl.java")
    with open(impl_path, "r", encoding="utf-8") as f: content = f.read()
    if ".email(" not in content:
        content = content.replace('.sdt(requestDTO.getSdt())', '.sdt(requestDTO.getSdt())\n                .email(requestDTO.getEmail())')
        content = content.replace('kh.setSdt(requestDTO.getSdt());', 'kh.setSdt(requestDTO.getSdt());\n        kh.setEmail(requestDTO.getEmail());')
        content = content.replace('nv.setSdt(requestDTO.getSdt());', 'nv.setSdt(requestDTO.getSdt());\n        nv.setEmail(requestDTO.getEmail());')
        content = content.replace('.sdt(kh.getSdt())', '.sdt(kh.getSdt())\n                .email(kh.getEmail())')
        content = content.replace('.sdt(nv.getSdt())', '.sdt(nv.getSdt())\n                .email(nv.getEmail())')
        with open(impl_path, "w", encoding="utf-8") as f: f.write(content)

print("Phase 1 completed.")
