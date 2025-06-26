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

### 1. 配置git代理

```bash
git config --global http.proxy http://127.0.0.1:7897
git config --global https.proxy http://127.0.0.1:7897
```

如果你的VPN只支持SOCKS5代理，也可以这样设置：

```bash
git config --global http.proxy socks5://127.0.0.1:7897
git config --global https.proxy socks5://127.0.0.1:7897
```

> **注意**：  
> - 如果你的VPN客户端明确标注"HTTP端口"，优先用http://开头。  
> - 如果标注"SOCKS5端口"，优先用socks5://开头。  
> - 你可以都试一下，哪个快用哪个。

### 2. 测试拉取/推送

设置完代理后，重新执行：

```bash
git pull origin main --rebase
```
或
```bash
git push -u origin main
```

### 3. 取消代理（如不用时）

如果以后不需要代理，可以用下面命令取消：

```bash
git config --global --unset http.proxy
git config --global --unset https.proxy
```

如还有问题，把报错信息发给我，我会继续帮你排查！ 