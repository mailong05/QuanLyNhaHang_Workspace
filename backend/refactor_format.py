import os
import re

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo"

entities_conf = [
    {"name": "KhachHang", "var": "k", "field": "maKH", "prefix": "KH"},
    {"name": "NhanVien", "var": "n", "field": "maNV", "prefix": "NV"},
    {"name": "HoaDon", "var": "h", "field": "maHD", "prefix": "HD"},
    {"name": "PhieuDatBan", "var": "p", "field": "maPhieuDat", "prefix": "PDB"},
    {"name": "BanAn", "var": "b", "field": "maBan", "prefix": "BA"},
    {"name": "CaLamViec", "var": "c", "field": "maCa", "prefix": "CA"},
    {"name": "KhuyenMai", "var": "km", "field": "maKM", "prefix": "KM"},
    {"name": "KhuVuc", "var": "kv", "field": "maKhuVuc", "prefix": "KV"},
    {"name": "MonAn", "var": "m", "field": "maMon", "prefix": "MA"},
    {"name": "Thue", "var": "t", "field": "maThue", "prefix": "TH"}
]

def update_repo(entity):
    repo_file = os.path.join(base_dir, "repository", f"{entity['name']}Repository.java")
    if not os.path.exists(repo_file): return
    with open(repo_file, "r", encoding="utf-8") as f: content = f.read()
    
    cap_field = entity["field"][0].upper() + entity["field"][1:]
    start_idx = len(entity["prefix"]) + 1 # JPQL is 1-indexed
    # CAST(SUBSTRING(e.ma, 3, 6) AS int) -> ALWAYS 6
    new_query = f'@Query("SELECT MAX(CAST(SUBSTRING({entity["var"]}.{entity["field"]}, {start_idx}, 6) AS int)) FROM {entity["name"]} {entity["var"]}")\n    Integer findMax{cap_field}();'
    
    # replace old findMax query
    content = re.sub(rf'@Query\("SELECT MAX[^"]+"\)\s*Integer findMax{cap_field}\(\);', new_query, content)
    content = re.sub(rf'@Query\("SELECT MAX[^"]+"\)\s*String findMax{cap_field}\(\);', new_query, content)
    
    with open(repo_file, "w", encoding="utf-8") as f: f.write(content)

def update_service(entity):
    impl_file = os.path.join(base_dir, "service", "impl", f"{entity['name']}ServiceImpl.java")
    if not os.path.exists(impl_file): return
    with open(impl_file, "r", encoding="utf-8") as f: content = f.read()
    
    cap_field = entity["field"][0].upper() + entity["field"][1:]
    repo_var = entity['name'][:1].lower() + entity['name'][1:] + "Repository"
    
    old_method_regex = rf'private String generateNext{cap_field}\(\)\s*\{{.*?\n    \}}'
    
    new_method = f"""private String generateNext{cap_field}() {{
        Integer maxMa = {repo_var}.findMax{cap_field}();
        if (maxMa == null) {{
            return String.format("{entity['prefix']}%06d", 1);
        }}
        return String.format("{entity['prefix']}%06d", maxMa + 1);
    }}"""
    
    content = re.sub(old_method_regex, new_method, content, flags=re.DOTALL)
    
    # Also handle some legacy cases where String was returned instead of Integer
    content = content.replace(f"String maxMa = {repo_var}.findMax{cap_field}();", f"Integer maxMa = {repo_var}.findMax{cap_field}();")
    
    with open(impl_file, "w", encoding="utf-8") as f: f.write(content)

for e in entities_conf:
    update_repo(e)
    update_service(e)

print("Unified format to %06d for all entities.")
