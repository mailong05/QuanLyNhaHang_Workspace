import os
for root, dirs, files in os.walk(r'C:\QuanLyNhaHang_Workspace\backend\src\main\java'):
    for f in files:
        if not f.endswith('.java'): continue
        path = os.path.join(root, f)
        with open(path, 'r', encoding='utf-8') as file:
            lines = file.readlines()
        for i, line in enumerate(lines):
            if 'import ' in line or '/*' in line or '*/' in line or ' * ' in line or 'replaceAll' in line or '"*"' in line: continue
            if line.strip().startswith('*'): continue
            if '*' in line:
                print(f'{path}:{i+1}:{line.strip()}')
