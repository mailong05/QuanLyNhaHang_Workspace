import os

base_dir = r"c:\QuanLyDatBanNhaHangVerWeb\src\main\java\com\QuanLyDatBanNhaHang\demo"
dto_resp_dir = os.path.join(base_dir, "dto", "response")
exception_dir = os.path.join(base_dir, "exception")
controller_dir = os.path.join(base_dir, "controller")

os.makedirs(dto_resp_dir, exist_ok=True)

# 1. CREATE ApiResponse.java
api_response_file = os.path.join(dto_resp_dir, "ApiResponse.java")
with open(api_response_file, "w", encoding="utf-8") as f:
    f.write("""package com.QuanLyDatBanNhaHang.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
""")

# 2. OVERWRITE GlobalExceptionHandler.java
global_ex_file = os.path.join(exception_dir, "GlobalExceptionHandler.java")
with open(global_ex_file, "w", encoding="utf-8") as f:
    f.write("""package com.QuanLyDatBanNhaHang.demo.exception;

import com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Validation Error", errors));
    }
    
    // Custom method to return errors within ApiResponse.data
    private ResponseEntity<ApiResponse<Map<String, String>>> createValidationErrorResponse(Map<String, String> errors) {
        ApiResponse<Map<String, String>> response = new ApiResponse<>(400, "Validation Error", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Bạn không có quyền truy cập tài nguyên này."));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Thông tin xác thực không chính xác."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Dữ liệu đầu vào không hợp lệ hoặc sai định dạng JSON/Enum."));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Tham số đường dẫn hoặc query không hợp lệ."));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Lỗi máy chủ nội bộ: " + ex.getMessage()));
    }
}
""")

# 3. REFACTOR AuthController
auth_file = os.path.join(controller_dir, "AuthController.java")
with open(auth_file, "r", encoding="utf-8") as f:
    auth_content = f.read()

auth_content = auth_content.replace(
    "import org.springframework.http.ResponseEntity;",
    "import org.springframework.http.ResponseEntity;\nimport com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;"
)

# Replace authenticateUser signature and body
auth_method_old = """    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(JwtAuthResponseDTO.builder()
                    .accessToken(jwt)
                    .username(userDetails.getUsername())
                    .role(userDetails.getTaiKhoan().getQuyenHan().name())
                    .build());
        } catch (org.springframework.security.core.AuthenticationException e) {
            // Trả về 401 Unauthorized thay vì để lỗi văng ra ngoài gây 403 Forbidden
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }"""
    
auth_method_new = """    public ResponseEntity<ApiResponse<JwtAuthResponseDTO>> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        JwtAuthResponseDTO responseDTO = JwtAuthResponseDTO.builder()
                .accessToken(jwt)
                .username(userDetails.getUsername())
                .role(userDetails.getTaiKhoan().getQuyenHan().name())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", responseDTO));
        // Lỗi AuthenticationException sẽ được GlobalExceptionHandler bắt và trả về 401 tự động.
    }"""

auth_content = auth_content.replace(auth_method_old, auth_method_new)
with open(auth_file, "w", encoding="utf-8") as f:
    f.write(auth_content)


# 4. REFACTOR WebBookingController
web_booking_file = os.path.join(controller_dir, "WebBookingController.java")
with open(web_booking_file, "r", encoding="utf-8") as f:
    wb_content = f.read()

wb_content = wb_content.replace(
    "import org.springframework.http.ResponseEntity;",
    "import org.springframework.http.ResponseEntity;\nimport com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;"
)
wb_content = wb_content.replace(
    "public ResponseEntity<?> createBooking",
    "public ResponseEntity<ApiResponse<String>> createBooking"
)
wb_content = wb_content.replace(
    "return ResponseEntity.ok(\"Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.\");",
    "return ResponseEntity.ok(ApiResponse.success(\"Đặt bàn thành công! Hệ thống đang xử lý và chờ xác nhận.\", null));"
)
with open(web_booking_file, "w", encoding="utf-8") as f:
    f.write(wb_content)


# 5. REFACTOR StaffOperationController
staff_file = os.path.join(controller_dir, "StaffOperationController.java")
with open(staff_file, "r", encoding="utf-8") as f:
    st_content = f.read()

st_content = st_content.replace(
    "import org.springframework.http.ResponseEntity;",
    "import org.springframework.http.ResponseEntity;\nimport com.QuanLyDatBanNhaHang.demo.dto.response.ApiResponse;"
)
st_content = st_content.replace(
    "public ResponseEntity<?> changeTable",
    "public ResponseEntity<ApiResponse<String>> changeTable"
)
st_content = st_content.replace(
    "return ResponseEntity.ok(\"Đổi bàn thành công!\");",
    "return ResponseEntity.ok(ApiResponse.success(\"Đổi bàn thành công!\", null));"
)

st_content = st_content.replace(
    "public ResponseEntity<?> mergeTables",
    "public ResponseEntity<ApiResponse<String>> mergeTables"
)
st_content = st_content.replace(
    "return ResponseEntity.ok(\"Gộp phiếu đặt bàn thành công!\");",
    "return ResponseEntity.ok(ApiResponse.success(\"Gộp phiếu đặt bàn thành công!\", null));"
)
with open(staff_file, "w", encoding="utf-8") as f:
    f.write(st_content)

print("Phase 4 completed.")
