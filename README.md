# 海洋生物文档管理系统 (Obwiki)

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-blue" alt="Java">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-green" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.2-blue" alt="Vue">
  <img src="https://img.shields.io/badge/Ant%20Design-4.2-blue" alt="Ant Design">
</p>

## 项目简介

基于 Spring Boot + Vue 3 的海洋生物知识文档管理系统，提供电子书分类、文档阅读、管理后台等功能。适合用于个人知识管理、企业文档中心等场景。

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 21 | JDK |
| Spring Boot | 3.5.7 | Web 框架 |
| MyBatis Plus | 3.5.14 | ORM 框架 |
| MySQL | 8.0+ | 关系数据库 |
| Redis | 7.0+ | 登录 Token 缓存 |
| Druid | 1.2.20 | 数据库连接池 |
| MD5 | - | 密码加密存储 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.2 | 渐进式前端框架 |
| TypeScript | 4.5 | 类型安全的 JavaScript |
| Ant Design Vue | 4.2.6 | UI 组件库 |
| WangEditor | 5.1.12 | 富文本编辑器 |
| Vuex | 4.0 | 状态管理 |
| Vue Router | 4.0 | 路由管理 |
| Axios | 1.13 | HTTP 客户端 |

## 环境要求

```
- JDK: 21 (推荐 Amazon Corretto 21 / OpenJDK 21)
- Maven: 3.9+
- MySQL: 8.0+
- Redis: 7.0+ (用于登录 Token 缓存，必需)
- Node.js: 18+
- npm: 9+
```

## 快速开始

### 1. 克隆项目

```bash
git clone <你的仓库地址>
cd web_demo
```

### 2. 配置数据库

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE wiki CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

# 导入数据
 SOURCE web_wiki.sql;
# 或
mysql -u root -p wiki < web_wiki.sql
```

### 3. 配置后端

复制并修改配置文件：

```bash
# 复制示例配置
cp wikidemo/src/main/resources/application-local.properties.example wikidemo/src/main/resources/application-local.properties
```

编辑 `application-local.properties`：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/wiki?characterEncoding=UTF8&autoReconnect=true&serverTimezone=Asia/Shanghai&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=你的MySQL密码

# Redis配置 (可选)
spring.data.redis.host=127.0.0.1
spring.data.redis.port=6379
```

> ⚠️ **注意**: 请务必修改默认密码，不要将包含真实密码的配置文件提交到仓库！

### 4. 启动后端

```bash
cd wikidemo
mvn spring-boot:run
# 后端启动成功: http://localhost:8088
```

### 5. 启动前端

```bash
cd obwiki-web
npm install
npm run serve
# 前端启动成功: http://localhost:8080
```

## 默认账号

| 角色 | 登录名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin |
| 测试用户 | test | test |

## 项目结构

```
web_demo/
│
├── obwiki-web/               # Vue3 前端项目
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   │   ├── admin/        # 管理后台
│   │   │   ├── HomeView.vue  # 首页
│   │   │   ├── DocView.vue   # 文档阅读
│   │   │   └── AboutView.vue # 关于
│   │   ├── components/       # 公共组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # Vuex 状态管理
│   │   └── utils/            # 工具函数
│   └── package.json
│
├── wikidemo/                 # Spring Boot 后端项目
│   └── src/main/
│       ├── java/com/gec/wikidemo/
│       │   ├── controller/   # 控制器
│       │   ├── service/      # 业务逻辑
│       │   ├── mapper/       # 数据访问
│       │   ├── entity/       # 实体类
│       │   ├── req/          # 请求参数
│       │   ├── resp/         # 响应结果
│       │   ├── config/       # 配置类
│       │   ├── interceptor/  # 拦截器
│       │   ├── aspect/       # AOP
│       │   └── exception/    # 异常处理
│       └── resources/
│           ├── mapper/       # MyBatis XML
│           ├── static/       # 静态资源
│           └── templates/    # 模板文件
│
├── web_wiki.sql              # MySQL 数据库脚本
└── README.md                 # 项目说明
```

## 功能列表

- [x] 电子书管理 (增删改查)
- [x] 文档分类管理 (树形结构)
- [x] 文档内容管理 (富文本编辑)
- [x] 用户管理
- [x] 电子书/文档点赞
- [x] 阅读量统计
- [x] 电子书快照 (预留)

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/user/login` | POST | 用户登录 |
| `/api/ebook/list` | GET | 电子书列表 |
| `/api/doc/list` | GET | 文档列表 |
| `/api/doc/content` | GET/POST | 文档内容 |
| `/api/category/list` | GET | 分类列表 |

## 注意事项

1. **密码安全**: 生产环境请使用环境变量或配置中心管理敏感信息
2. **图片存储**: 当前使用 Base64 存储，大文件会影响性能，建议后续接入对象存储 (OSS/MinIO)
3. **权限控制**: 项目使用简单拦截器验证，建议增加 Spring Security 做完整权限控制
4. **生产部署**: 当前无 Docker/Nginx 配置，生产环境需要手动配置

## 待完善功能

- [ ] 电子书快照统计功能 (`ebook_snapshot` 表)
- [ ] 接口权限控制 (Spring Security / Shiro)
- [ ] 文件上传到对象存储
- [ ] Docker 容器化部署
- [ ] Nginx 反向代理配置

## 许可证

MIT License