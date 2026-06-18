import os
import re

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb"
schema_file = os.path.join(base_dir, "01_schema.sql")

with open(schema_file, "r", encoding="utf-8") as f:
    content = f.read()

if "email VARCHAR(100)" not in content:
    content = content.replace("sdt VARCHAR(15),", "sdt VARCHAR(15),\n    email VARCHAR(100),")
    content = content.replace("sdt VARCHAR(15) NOT NULL,", "sdt VARCHAR(15) NOT NULL,\n    email VARCHAR(100),")
    with open(schema_file, "w", encoding="utf-8") as f:
        f.write(content)

print("Updated 01_schema.sql")
