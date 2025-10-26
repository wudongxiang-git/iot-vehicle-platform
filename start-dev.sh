#!/bin/bash
# IoT Vehicle Platform - 开发环境启动脚本
# Author: dongxiang.wu

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Docker 是否安装
check_docker() {
    print_info "检查 Docker 环境..."
    
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker Desktop"
        print_info "macOS 安装: brew install --cask docker"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        print_error "Docker 未运行，请启动 Docker Desktop"
        exit 1
    fi
    
    print_info "Docker 环境正常 ✓"
}

# 检查 docker-compose 是否安装
check_docker_compose() {
    if ! command -v docker-compose &> /dev/null; then
        print_error "docker-compose 未安装"
        exit 1
    fi
    print_info "docker-compose 已安装 ✓"
}

# 启动服务
start_services() {
    print_info "启动 Docker 服务..."
    
    # 检查是否需要启动管理工具
    if [ "$1" == "--with-tools" ]; then
        print_info "启动核心服务 + 管理工具..."
        docker-compose --profile tools up -d
    else
        print_info "启动核心服务（PostgreSQL + Redis + EMQ X）..."
        docker-compose up -d
    fi
    
    if [ $? -eq 0 ]; then
        print_info "服务启动成功 ✓"
    else
        print_error "服务启动失败"
        exit 1
    fi
}

# 等待服务就绪
wait_for_services() {
    print_info "等待服务启动..."
    
    # 等待 PostgreSQL
    print_info "等待 PostgreSQL 就绪..."
    for i in {1..30}; do
        if docker exec iot-vehicle-postgres pg_isready -U postgres &> /dev/null; then
            print_info "PostgreSQL 就绪 ✓"
            break
        fi
        if [ $i -eq 30 ]; then
            print_warn "PostgreSQL 启动超时，请检查日志"
        fi
        sleep 1
    done
    
    # 等待 Redis
    print_info "等待 Redis 就绪..."
    for i in {1..30}; do
        if docker exec iot-vehicle-redis redis-cli ping &> /dev/null; then
            print_info "Redis 就绪 ✓"
            break
        fi
        if [ $i -eq 30 ]; then
            print_warn "Redis 启动超时，请检查日志"
        fi
        sleep 1
    done
    
    # 等待 EMQ X
    print_info "等待 EMQ X 就绪..."
    for i in {1..60}; do
        if docker exec iot-vehicle-emqx emqx ping &> /dev/null; then
            print_info "EMQ X 就绪 ✓"
            break
        fi
        if [ $i -eq 60 ]; then
            print_warn "EMQ X 启动超时，请检查日志"
        fi
        sleep 2
    done
}

# 显示服务信息
show_services_info() {
    echo ""
    echo "======================================"
    echo "IoT Vehicle Platform 开发环境已启动"
    echo "======================================"
    echo ""
    echo "📦 服务状态:"
    docker-compose ps
    echo ""
    echo "🔗 连接信息:"
    echo ""
    echo "  PostgreSQL:"
    echo "    地址: localhost:5432"
    echo "    数据库: iot_vehicle"
    echo "    用户名: postgres"
    echo "    密码: postgres"
    echo ""
    echo "  Redis:"
    echo "    地址: localhost:6379"
    echo "    密码: (无)"
    echo ""
    echo "  EMQ X MQTT:"
    echo "    MQTT: tcp://localhost:1883"
    echo "    WebSocket: ws://localhost:8083/mqtt"
    echo "    Dashboard: http://localhost:18083"
    echo "    用户名: admin"
    echo "    密码: admin123"
    echo ""
    
    # 检查是否启动了管理工具
    if docker ps | grep -q "iot-vehicle-redis-commander"; then
        echo "  Redis Commander: http://localhost:8081"
    fi
    
    if docker ps | grep -q "iot-vehicle-pgadmin"; then
        echo "  pgAdmin: http://localhost:5050"
        echo "    邮箱: admin@iotvehicle.com"
        echo "    密码: admin123"
    fi
    
    echo ""
    echo "📝 常用命令:"
    echo "  查看日志: docker-compose logs -f"
    echo "  停止服务: docker-compose stop"
    echo "  重启服务: docker-compose restart"
    echo "  查看状态: docker-compose ps"
    echo ""
    echo "📚 详细文档: docs/DOCKER_DEV_SETUP.md"
    echo ""
}

# 主函数
main() {
    echo ""
    echo "🚀 IoT Vehicle Platform - 开发环境启动"
    echo "   Author: dongxiang.wu"
    echo ""
    
    # 检查环境
    check_docker
    check_docker_compose
    
    # 启动服务
    start_services "$1"
    
    # 等待服务就绪
    wait_for_services
    
    # 显示服务信息
    show_services_info
}

# 执行主函数
main "$@"

