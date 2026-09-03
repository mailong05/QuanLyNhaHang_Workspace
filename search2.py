import os
import re

for root, dirs, files in os.walk(r'C:\QuanLyNhaHang_Workspace\backend\src\main\java'):
    for f in files:
        if not f.endswith('.java'): continue
        path = os.path.join(root, f)
        with open(path, 'r', encoding='utf-8') as file:
            content = file.read()
        
        # very crude check: does it have '+' or '-' and 'BigDecimal'?
        # We need to specifically search for variables that are bigdecimal being used with + or -
        # Let's just output lines that contain + or - and some keywords like 'tongTienGoc', 'tienThue', 'thanhTien'
        lines = content.split('\n')
        for i, line in enumerate(lines):
            if 'import ' in line or '/*' in line or '*/' in line or ' * ' in line or '"' in line: continue
            if line.strip().startswith('//'): continue
            if '+' in line or '-' in line or '/' in line or '>' in line or '<' in line:
                if 'tongTienGoc' in line or 'tienThue' in line or 'thanhTien' in line or 'tongThanhToan' in line or 'tienGiamGia' in line or 'giaTriGiam' in line:
                    if 'List' in line or 'builder' in line or 'import' in line: continue
                    print(f'{path}:{i+1}:{line.strip()}')
