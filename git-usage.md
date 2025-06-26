# Git 本地仓库初始化与远程关联说明

1. 初始化本地仓库
```bash
git init
```

2. 添加远程仓库
```bash
git remote add origin https://github.com/Idealistok/Learning-Assistant-Platform.git
```

3. 添加所有文件到暂存区
```bash
git add .
```

4. 首次提交
```bash
git commit -m "init: 项目初始化，添加需求文档与UML"
```

5. 设置主分支为 main
```bash
git branch -M main
```

6. 推送到远程仓库
```bash
git push -u origin main
```

> 如遇推送权限问题，请先在GitHub生成并配置SSH Key或使用账号密码/Token。 