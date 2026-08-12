# Changelog

## [1.2.2](https://github.com/baiyibs/jwxt/compare/v1.2.1...v1.2.2) (2026-08-12)


### 👷 Continuous Integration

* 修复无法发布附件的问题 ([fb32113](https://github.com/baiyibs/jwxt/commit/fb3211329542ffb8bc8e53e2c306026d646539db))

## [1.2.1](https://github.com/baiyibs/jwxt/compare/v1.2.0...v1.2.1) (2026-08-12)


### ♻️ Code Refactoring

* 更改配置文件中版本号的更新方式 ([2f610b5](https://github.com/baiyibs/jwxt/commit/2f610b587c52efc288b05975f2ecfa966c7b3601))
* 统一使用 App.CONFIG 替代 ConfigManager.getInstance() ([3c0ee20](https://github.com/baiyibs/jwxt/commit/3c0ee20f91dd24b33054a9bf4dabad0a0bfcba46))


### 👷 Continuous Integration

* 修改构建配置 ([2f13210](https://github.com/baiyibs/jwxt/commit/2f1321023ab7512b2b0a4853b85d38bd7fc7b0a1))

## [1.2.0](https://github.com/baiyibs/jwxt/compare/v1.1.0...v1.2.0) (2026-08-11)


### ✨ Features

* 在配置文件中同步更新版本号 ([c74d2e6](https://github.com/baiyibs/jwxt/commit/c74d2e69f94796adc1e7da1b918d17ad568338d6))


### 🐛 Bug Fixes

* 修复异步任务中线程中断失败的问题 ([f069e38](https://github.com/baiyibs/jwxt/commit/f069e3833f4b115f55fb266e8461aea03177f953))


### ♻️ Code Refactoring

* 移除 Kotlin 支持，恢复为纯 Java 项目 ([473737b](https://github.com/baiyibs/jwxt/commit/473737b6be6cb0bb0c0beeb75f35025779f1b9ea))

## [1.1.0](https://github.com/baiyibs/jwxt/compare/v1.0.2...v1.1.0) (2026-08-10)


### ✨ Features

* **config:** 配置文件从TOML改为YAML ([c071918](https://github.com/baiyibs/jwxt/commit/c07191871bf77320f78ed2ee7c41dbbb968657d0))
* **core:** 修改日志格式 ([1897a37](https://github.com/baiyibs/jwxt/commit/1897a3751b507eb87df09812d6dcf879d7a18ad8))
* **core:** 支持验证码识别失败后进行手动输入 ([14474fa](https://github.com/baiyibs/jwxt/commit/14474fa2353508c8e669664eb5c4731f92961ef4))
* **core:** 新增 ImageViewer , 支持在终端中显示验证码 ([ba85a2b](https://github.com/baiyibs/jwxt/commit/ba85a2bb8bc01e4a0437e052ac26834d93277d7b))
* **core:** 更改包名 ([2379182](https://github.com/baiyibs/jwxt/commit/237918256ebe972f6c30e39e1bebedb99da19693))
* **Course:** 添加成绩解析功能 ([196c672](https://github.com/baiyibs/jwxt/commit/196c67279937c5b188b5541f2a945059ec65c0b2))
* 为 PlaywrightManager 添加 newPage 和 getPage 方法，为多账号运行做准备 ([6fa73dd](https://github.com/baiyibs/jwxt/commit/6fa73dd8d49f1a120fd9a99fa8879491c725bdf4))
* 修改 PlaywrightManager 的 close 逻辑 ([eae229a](https://github.com/baiyibs/jwxt/commit/eae229a766b0c8a1854d3a1906c882d2da266d7e))
* 修改 StudentParser 的实现方法，使用 StrSplitter 分割数据 ([7ebf6f7](https://github.com/baiyibs/jwxt/commit/7ebf6f7cf3d2d9269164d33b56c9826925422983))
* 删除slowMo参数 ([67b32ad](https://github.com/baiyibs/jwxt/commit/67b32ad30432d1fb9ddd40735af20f4bd552be5a))
* 完善登录流程 ([010470e](https://github.com/baiyibs/jwxt/commit/010470e25e49a5fe1003c33fc6d55671e4476fae))
* 对 PlaywrightManager 添加多线程支持 ([7a2d0a7](https://github.com/baiyibs/jwxt/commit/7a2d0a7eebea18755d8a9073120931b03b025106))
* 将 CaptchaService 重构为可实例化的对象 ([36715ac](https://github.com/baiyibs/jwxt/commit/36715ac8ad4df22e6b995e45cfd9970e6ae876f3))
* 将 OkHttp3 更换为 hutool , 修复手动输入验证码时会少读取一位的问题 ([b5e2699](https://github.com/baiyibs/jwxt/commit/b5e26996ed0a8f8e8ee67ae3e9c126b16a3e8bde))
* 新增 CaptchaHelper , 将 CaptchaService 中的错误处理剥离到Helper中 ([1c604cb](https://github.com/baiyibs/jwxt/commit/1c604cbf0314e009cb51568c8a4fe5dcd96fe0b9))
* 更改验证码识别接口，添加关于多账号的代码 ([362fd56](https://github.com/baiyibs/jwxt/commit/362fd56a72e198b06146e505bef8ca58cc307905))
* 添加 kotlin 配置 ([280f9c1](https://github.com/baiyibs/jwxt/commit/280f9c108a2fb5d7ec5575555495d6fab6715948))
* 添加 Student 类，支持获取学生信息 ([c67e3da](https://github.com/baiyibs/jwxt/commit/c67e3da3832049b053ab77f88588b9d1a1919e0c))
* 添加 Transcript 类 ([999694a](https://github.com/baiyibs/jwxt/commit/999694ab5349cb59a2bac265a5524ee983946ef9))
* 重构 AuthService 的 set 方法 ([96d5bf6](https://github.com/baiyibs/jwxt/commit/96d5bf65ea04e47150d9994ff2c819b931c5902e))
* 重构验证码识别模块，使用ddddocr-fastapi ([29bdb2b](https://github.com/baiyibs/jwxt/commit/29bdb2b23e9b6fbe81a32e8dfc8b929b4f9b54d5))


### 🐛 Bug Fixes

* **build:** 修复 Playwright 驱动加载问题 ([2cb2249](https://github.com/baiyibs/jwxt/commit/2cb2249e9c587b44b1b155d4f120c4263d35449b))
* 修复 release.yml 无法触发的问题, 将触发分支从 main 更改为 master ([0224af5](https://github.com/baiyibs/jwxt/commit/0224af5352b4d890675f1f90f09e4de7ddd0a13a))
* 删除 CaptchaService 内无用的构造函数 ([8e2c0f8](https://github.com/baiyibs/jwxt/commit/8e2c0f815705bf30124154c984debe9a8cdc23ef))
* 删除多余引用 ([92bed19](https://github.com/baiyibs/jwxt/commit/92bed19ea9d3466f440c6232f2c2cfec897ef097))
* 删除无用代码 ([dc30984](https://github.com/baiyibs/jwxt/commit/dc30984c83414eefd4c3d3b396865883fdb71da3))


### ♻️ Code Refactoring

* 删除手动输入验证码的方法 ([bbc2b08](https://github.com/baiyibs/jwxt/commit/bbc2b08d1635bc9b3eb0097d5bb987ddec486533))
* 删除调试代码 ([fb42cb0](https://github.com/baiyibs/jwxt/commit/fb42cb0d4d357c1c1aaae6ac96076c8ac906e686))
* 将登录逻辑提取到 AuthService ([8b35201](https://github.com/baiyibs/jwxt/commit/8b3520196339d827907d29544dd60e3ee2c935f8))
* 引入 PlaywrightManager 并重构认证服务 ([ab9b268](https://github.com/baiyibs/jwxt/commit/ab9b26806945c4075fdbed99304b6af8a7326537))


### 👷 Continuous Integration

* 将 CodeQL 的 Java/Kotlin 构建模式从 none 改为 autobuild ([f40f88d](https://github.com/baiyibs/jwxt/commit/f40f88dc93ed96516c650bc5a1232039e8693101))
