package com.QuanLyDatBanNhaHang.demo.security.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Lưu trữ token bucket cho từng IP theo mục đích
    private final Map<String, TokenBucket> bookingBuckets = new ConcurrentHashMap<>();
    private final Map<String, TokenBucket> loginBuckets = new ConcurrentHashMap<>();
    private final Map<String, TokenBucket> generalBuckets = new ConcurrentHashMap<>();

    private static class TokenBucket {
        private final long capacity;
        private final long refillTokensPerSecond;
        private double availableTokens;
        private long lastRefillTimestamp;

        public TokenBucket(long capacity, long refillIntervalSeconds) {
            this.capacity = capacity;
            this.availableTokens = capacity;
            this.refillTokensPerSecond = capacity / Math.max(1, refillIntervalSeconds);
            this.lastRefillTimestamp = System.nanoTime();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (availableTokens >= 1.0) {
                availableTokens -= 1.0;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            double secondsPassed = (now - lastRefillTimestamp) / 1_000_000_000.0;
            if (secondsPassed > 0) {
                availableTokens = Math.min(capacity, availableTokens + (secondsPassed * refillTokensPerSecond));
                lastRefillTimestamp = now;
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        String clientIp = getClientIp(request);

        // 1. Rate Limit cho đặt bàn web (chống spam đơn đặt bàn ảo: 5 req / 60s)
        if ("POST".equalsIgnoreCase(method) && path.startsWith("/api/web/booking")) {
            TokenBucket bucket = bookingBuckets.computeIfAbsent(clientIp, k -> new TokenBucket(5, 60));
            if (!bucket.tryConsume()) {
                sendRateLimitResponse(response, "Bạn đang gửi yêu cầu đặt bàn quá nhanh. Vui lòng thử lại sau 1 phút.");
                return;
            }
        }

        // 2. Rate Limit cho login (chống brute force dò pass: 5 attempts / 60s)
        if ("POST".equalsIgnoreCase(method) && path.startsWith("/api/auth/login")) {
            TokenBucket bucket = loginBuckets.computeIfAbsent(clientIp, k -> new TokenBucket(5, 60));
            if (!bucket.tryConsume()) {
                sendRateLimitResponse(response, "Bạn đã thử đăng nhập quá nhiều lần. Vui lòng đợi 1 phút trước khi thử lại.");
                return;
            }
        }

        // 3. Rate Limit chung cho toàn bộ API (chống DDoS: 100 req / 60s)
        if (path.startsWith("/api/")) {
            TokenBucket bucket = generalBuckets.computeIfAbsent(clientIp, k -> new TokenBucket(100, 60));
            if (!bucket.tryConsume()) {
                sendRateLimitResponse(response, "Hệ thống phát hiện lưu lượng bất thường. Vui lòng thử lại sau giây lát.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendRateLimitResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Retry-After", "60");

        Map<String, Object> errorBody = Map.of(
                "code", 429,
                "message", message,
                "data", null
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || "unknown".equalsIgnoreCase(xfHeader)) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}
