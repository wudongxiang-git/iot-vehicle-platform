#!/bin/bash
# IoT Vehicle Platform - 自动化测试运行脚本
# Author: dongxiang.wu

set -e

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}IoT Vehicle Platform - 自动化测试${NC}"
echo -e "${GREEN}=====================================${NC}"
echo ""

# 检查Maven
echo -e "${YELLOW}[0/4] 检查Maven环境...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven未找到！${NC}"
    echo ""
    echo "请选择以下方式之一："
    echo "1. 使用IDE运行测试（推荐）"
    echo "   - 打开IntelliJ IDEA"
    echo "   - 右键点击测试类"
    echo "   - 选择 'Run Tests'"
    echo ""
    echo "2. 安装Maven"
    echo "   brew install maven"
    echo ""
    echo "3. 使用Maven Wrapper"
    echo "   ./mvnw test"
    exit 1
fi
echo -e "${GREEN}✓ Maven已安装${NC}"
echo ""

# 检查Docker服务
echo -e "${YELLOW}[1/4] 检查Docker服务...${NC}"
if ! docker-compose ps | grep -q "iot-vehicle-postgres.*Up"; then
    echo -e "${RED}PostgreSQL未运行，请先启动Docker服务：${NC}"
    echo "  docker-compose up -d"
    exit 1
fi
echo -e "${GREEN}✓ Docker服务正常${NC}"
echo ""

# 检查数据库连接
echo -e "${YELLOW}[2/4] 检查数据库连接...${NC}"
if docker exec iot-vehicle-postgres psql -U postgres -d iot_vehicle -c "SELECT 1" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ 数据库连接正常${NC}"
else
    echo -e "${RED}数据库连接失败${NC}"
    exit 1
fi
echo ""

# 检查测试表是否存在
echo -e "${YELLOW}[3/4] 检查数据库表...${NC}"
TABLE_COUNT=$(docker exec iot-vehicle-postgres psql -U postgres -d iot_vehicle -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public' AND table_name IN ('sys_user', 'sys_role', 'sys_permission')" 2>/dev/null | tr -d ' ')

if [ "$TABLE_COUNT" == "3" ]; then
    echo -e "${GREEN}✓ 数据库表已创建${NC}"
else
    echo -e "${YELLOW}数据库表未完全创建，尝试手动执行SQL脚本...${NC}"
    docker exec -i iot-vehicle-postgres psql -U postgres -d iot_vehicle < docker/postgres/init/02-create-tables.sql
    echo -e "${GREEN}✓ 数据库初始化完成${NC}"
fi
echo ""

# 运行测试
echo -e "${YELLOW}[4/4] 运行自动化测试...${NC}"
echo ""

# 运行所有测试
mvn test

TEST_RESULT=$?

echo ""
if [ $TEST_RESULT -eq 0 ]; then
    echo -e "${GREEN}=====================================${NC}"
    echo -e "${GREEN}✓ 所有测试通过！${NC}"
    echo -e "${GREEN}=====================================${NC}"
else
    echo -e "${RED}=====================================${NC}"
    echo -e "${RED}✗ 测试失败，请查看日志${NC}"
    echo -e "${RED}=====================================${NC}"
    exit 1
fi

echo ""
echo "测试报告位置: iot-vehicle-web/target/surefire-reports/"
echo "查看详细报告: open iot-vehicle-web/target/surefire-reports/index.html"

