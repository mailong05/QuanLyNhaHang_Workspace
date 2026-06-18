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
        
    # Check if already imported
    if "import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;" not in content:
        content = content.replace("import org.springframework.http.ResponseEntity;", 
                                  "import org.springframework.http.ResponseEntity;\nimport com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;")
    
    # 1. Replace ResponseEntity<Page<...>> -> ResponseEntity<ApiResponse<Page<...>>>
    # Be careful not to replace if already replaced
    content = re.sub(r"public ResponseEntity<((?!ApiResponse).*?)> ([a-zA-Z0-9_]+)\(", 
                     r"public ResponseEntity<ApiResponse<\1>> \2(", content)
    
    # 2. Replace return ResponseEntity.ok(...)
    # Note: this might match multiple lines if we are not careful, so we use a non-greedy approach
    content = re.sub(r"return ResponseEntity\.ok\((.*?)\);",
                     r"return ResponseEntity.ok(ApiResponse.success(\1));", content)
                     
    # 3. Replace return new ResponseEntity<>(..., HttpStatus.CREATED);
    content = re.sub(r"return new ResponseEntity\w*\((.*?), HttpStatus\.CREATED\);",
                     r"return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(\1));", content)
                     
    # 4. Replace return ResponseEntity.noContent().build();
    content = re.sub(r"return ResponseEntity\.noContent\(\)\.build\(\);",
                     r'return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));', content)

    # Clean up double ApiResponse if any got messed up (e.g. ApiResponse<ApiResponse<...>>)
    content = content.replace("ApiResponse<ApiResponse<", "ApiResponse<")
    content = content.replace("ApiResponse.success(ApiResponse.success(", "ApiResponse.success(")
    
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)

print("Controllers refactored successfully.")
