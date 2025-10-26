# Docker 配置文件

本目录包含 Docker 开发环境的配置文件和初始化脚本。

## 目录结构

```
docker/
├── README.md                    # 本说明文件
└── postgres/
    └── init/
        └── 01-init.sql          # PostgreSQL 数据库初始化脚本
```

## 说明

### postgres/init/

PostgreSQL 数据库初始化脚本目录。

- 容器首次启动时，会自动执行此目录下的 `.sql` 和 `.sh` 文件
- 文件按字母顺序执行，建议使用数字前缀（如 `01-`, `02-`）来控制执行顺序
- 初始化脚本只在数据卷为空时执行一次

**当前脚本**:
- `01-init.sql`: 创建测试表，验证数据库连接

**后续规划**:
- `02-create-tables.sql`: 创建业务表结构（用户、设备等）
- `03-init-data.sql`: 插入初始化数据
- `04-create-indexes.sql`: 创建索引

## 使用方法

### 添加新的初始化脚本

1. 在 `postgres/init/` 目录下创建新的 SQL 文件
2. 使用数字前缀确保执行顺序（如 `02-xxx.sql`）
3. 删除现有数据卷并重新启动容器：

```bash
# 停止并删除容器和数据卷
docker-compose down -v

# 重新启动（会执行所有初始化脚本）
docker-compose up -d postgres
```

### 手动执行 SQL 脚本

如果数据库已经初始化，可以手动执行脚本：

```bash
# 方式1: 在容器内执行
docker exec -i iot-vehicle-postgres psql -U postgres -d iot_vehicle < docker/postgres/init/01-init.sql

# 方式2: 通过主机连接执行
psql -h localhost -p 5432 -U postgres -d iot_vehicle -f docker/postgres/init/01-init.sql
```

## 注意事项

1. **初始化脚本只执行一次**: 如果需要重新执行，必须删除数据卷
2. **脚本幂等性**: 建议使用 `CREATE TABLE IF NOT EXISTS` 等幂等语句
3. **备份重要数据**: 删除数据卷会清空所有数据，请先备份
4. **权限问题**: 确保脚本文件有可读权限

## 相关文档

- [Docker 开发环境搭建指南](../docs/DOCKER_DEV_SETUP.md)（个人文档，不提交git）
- [PostgreSQL 初始化文档](https://hub.docker.com/_/postgres)

---

**作者**: dongxiang.wu  
**更新时间**: 2025-10-26

