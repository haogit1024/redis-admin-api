# HTW TO PACKAGE

开发/测试环境

```bash
mvn clean package -Pdev -Dmaven.test.skip=true
```
正式环境
```bash
mvn clean package -Pprod -Dmaven.test.skip=true
```

# 分支说明
1. master: 正式版分支
1. dev: 开发版分支
1. test: 测试版分支

# 开发
## 架构
spring-boot + maven 多模块架构
## api设计
1. admin restful