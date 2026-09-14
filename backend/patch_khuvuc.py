import os

path = 'C:/QuanLyNhaHang_Workspace/backend/src/main/java/com/QuanLyDatBanNhaHang/demo/entity/KhuVuc.java'
with open(path, 'r', encoding='utf-8') as f:
    c = f.read()

if 'SQLDelete' not in c:
    c = c.replace('import jakarta.persistence.Enumerated;', 'import jakarta.persistence.Enumerated;\nimport org.hibernate.annotations.SQLDelete;\nimport org.hibernate.annotations.SQLRestriction;')
    c = c.replace('@Entity\n@Table(name = "KhuVuc")', '@Entity\n@Table(name = "KhuVuc")\n@SQLDelete(sql = "UPDATE KhuVuc SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")\n@SQLRestriction("deleted_at IS NULL")')
    with open(path, 'w', encoding='utf-8') as f: f.write(c)
    print('Patched KhuVuc.java with Soft Delete')
else:
    print('KhuVuc already has Soft Delete')
