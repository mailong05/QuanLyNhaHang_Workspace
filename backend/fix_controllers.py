import os
import re

controller_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo\controller"
skip_files = ["AuthController.java", "WebBookingController.java", "StaffOperationController.java"]

for filename in os.listdir(controller_dir):
    if not filename.endswith("Controller.java"): continue
    if filename in skip_files: continue
    
    filepath = os.path.join(controller_dir, filename)
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()
        
    # Replace return new ResponseEntity<>(XXX, HttpStatus.CREATED);
    # This matches new ResponseEntity<>(something, HttpStatus.CREATED);
    content = re.sub(r"return new ResponseEntity<>\((.*?), HttpStatus\.CREATED\);",
                     r"return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(\1));", content)
    
    # Fix the weird utf-8 issue with "Xóa thành công" that became "XA3a thAnh cA'ng"
    content = content.replace('ApiResponse.success("XA3a thAnh cA\'ng", null)', 'ApiResponse.success("Xóa thành công", null)')
    content = content.replace('ApiResponse.success("X\u00c3\u00b3a th\u00c3\u00a0nh c\u00c3\u00b4ng", null)', 'ApiResponse.success("Xóa thành công", null)')

    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)

print("Fixed controllers.")
