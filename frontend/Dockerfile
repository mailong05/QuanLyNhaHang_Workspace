# ==========================================
# 🐳 STAGE 1: BUILD STAGE
# ==========================================
FROM node:20-alpine AS builder

WORKDIR /app

# Copy dependency files first for layer caching
COPY package*.json ./
RUN npm install

# Copy source code and build project
COPY . .
RUN npm run build

# ==========================================
# 🐳 STAGE 2: PRODUCTION STAGE (NGINX)
# ==========================================
FROM nginx:alpine

# Copy built distribution files from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
