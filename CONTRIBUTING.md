# 贡献指南

感谢你对 IoT Vehicle Platform 项目的关注！

**作者**: dongxiang.wu

---

## 🤝 如何贡献

我们欢迎任何形式的贡献，包括但不限于：

- 🐛 报告 Bug
- 💡 提出新功能建议
- 📝 改进文档
- 🔧 提交代码修复
- ✨ 实现新功能

---

## 📋 提交流程

### 1. Fork 项目

点击页面右上角的 Fork 按钮，将项目 Fork 到你的 GitHub 账号下。

### 2. 克隆项目

```bash
git clone https://github.com/你的用户名/iot-vehicle-platform.git
cd iot-vehicle-platform
```

### 3. 创建分支

```bash
git checkout -b feature/your-feature-name
# 或
git checkout -b fix/your-bug-fix
```

### 4. 开发和测试

- 编写代码
- 添加测试
- 确保所有测试通过
- 遵循代码规范

### 5. 提交代码

遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```bash
git add .
git commit -m "feat: 添加用户登录功能"
```

### 6. 推送到 GitHub

```bash
git push origin feature/your-feature-name
```

### 7. 创建 Pull Request

在 GitHub 上创建 Pull Request，描述你的修改内容。

---

## 📝 Git 提交规范

### 提交消息格式

```
<类型>: <简短描述>

<详细描述>（可选）

<关联Issue>（可选）
```

### 类型说明

| 类型 | 说明 | 示例 |
|------|------|------|
| **feat** | 新功能 | `feat: 添加设备批量导入功能` |
| **fix** | 修复Bug | `fix: 修复分页查询参数错误` |
| **docs** | 文档更新 | `docs: 更新API接口文档` |
| **style** | 代码格式调整 | `style: 格式化设备管理代码` |
| **refactor** | 重构 | `refactor: 重构用户认证逻辑` |
| **perf** | 性能优化 | `perf: 优化数据库查询性能` |
| **test** | 测试相关 | `test: 添加设备管理单元测试` |
| **chore** | 构建/工具变动 | `chore: 更新Maven依赖版本` |
| **revert** | 回退提交 | `revert: 回退commit abc123` |

### 提交示例

**好的提交消息** ✅

```bash
feat: 添加用户登录功能

- 实现JWT令牌生成
- 添加登录接口
- 添加用户认证拦截器

Closes #123
```

```bash
fix: 修复分页查询Bug

修复当页码为0时查询失败的问题
```

```bash
docs: 更新README安装说明
```

**不好的提交消息** ❌

```bash
update     # 太模糊
修复bug    # 没说明是什么bug
完成功能开发  # 没说明是什么功能
```

### 使用提交模板

项目已配置了 Git 提交消息模板（`.gitmessage`），使用方法：

```bash
# 配置模板（项目已配置，无需重复执行）
git config commit.template .gitmessage

# 提交时会自动打开编辑器，显示模板
git commit
```

---

## 💻 代码规范

### Java 代码规范

遵循阿里巴巴 Java 开发手册：

- 类名使用大驼峰（UpperCamelCase）
- 方法名、参数名、成员变量使用小驼峰（lowerCamelCase）
- 常量名全部大写，单词间用下划线隔开
- 每个类、方法都要有注释
- 使用 4 个空格缩进（不使用 Tab）

### 示例

```java
/**
 * 用户服务接口
 *
 * @author dongxiang.wu
 */
public interface UserService {
    
    /**
     * 根据用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);
}
```

---

## ✅ 代码检查

### 提交前检查清单

- [ ] 代码编译通过
- [ ] 单元测试通过
- [ ] 代码格式符合规范
- [ ] 添加了必要的注释
- [ ] 更新了相关文档
- [ ] 遵循了提交消息规范

### 运行测试

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 检查代码覆盖率
mvn clean verify
```

---

## 📚 开发环境

### 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 15+
- Redis 7.0+
- Docker（推荐）

### 快速开始

```bash
# 1. 启动Docker环境
docker-compose up -d

# 2. 编译项目
mvn clean install

# 3. 启动应用
cd iot-vehicle-web
mvn spring-boot:run
```

详见 [README.md](README.md)

---

## 🐛 报告 Bug

### 提交 Issue

在 [GitHub Issues](https://github.com/wudongxiang-git/iot-vehicle-platform/issues) 提交 Bug 报告时，请包含：

1. **Bug 描述**：简洁清晰的描述
2. **复现步骤**：详细的复现步骤
3. **期望行为**：应该发生什么
4. **实际行为**：实际发生了什么
5. **环境信息**：
   - 操作系统
   - JDK 版本
   - Maven 版本
6. **错误日志**：相关的错误日志
7. **截图**：如果有的话

### Bug 报告模板

```markdown
## Bug 描述
简洁清晰的描述 Bug

## 复现步骤
1. 执行 xxx
2. 点击 xxx
3. 看到错误

## 期望行为
应该显示 xxx

## 实际行为
显示了 xxx 错误

## 环境信息
- OS: macOS 14.0
- JDK: 17.0.8
- Maven: 3.9.5

## 错误日志
```java
粘贴错误日志
```

## 截图
如果有的话
```

---

## 💡 功能建议

在 [GitHub Issues](https://github.com/wudongxiang-git/iot-vehicle-platform/issues) 提交功能建议时，请描述：

1. **功能描述**：你希望添加什么功能
2. **使用场景**：为什么需要这个功能
3. **解决方案**：你认为应该如何实现（可选）
4. **替代方案**：是否考虑过其他方案（可选）

---

## 📄 许可证

提交代码即表示你同意将代码按照 [Apache License 2.0](LICENSE) 开源。

---

## 🙏 致谢

感谢所有为项目做出贡献的开发者！

---

## 📞 联系方式

- GitHub Issues: https://github.com/wudongxiang-git/iot-vehicle-platform/issues
- 作者: dongxiang.wu

---

**Happy Coding!** 🎉

