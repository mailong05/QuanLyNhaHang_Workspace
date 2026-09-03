import os
import re

for root, dirs, files in os.walk(r'C:\QuanLyNhaHang_Workspace\backend\src\main\java'):
    for f in files:
        if not f.endswith('.java'): continue
        path = os.path.join(root, f)
        with open(path, 'r', encoding='utf-8') as file:
            content = file.read()
        
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if 'import ' in line or '/*' in line or '*/' in line or ' * ' in line or '"' in line: continue
            if line.strip().startswith('//'): continue
            if '<' in line or '>' in line:
                if 'tongTienGoc' in line or 'tienThue' in line or 'thanhTien' in line or 'tongThanhToan' in line or 'tienGiamGia' in line or 'giaTriGiam' in line or 'donGia' in line or 'dieuKienToiThieu' in line:
                    if 'List' in line or 'builder' in line or 'import' in line: continue
                    print(f'{path}:{i+1}:{line.strip()}')
