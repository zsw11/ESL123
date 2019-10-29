## Vueadmin

# 安装

> - 安装 python 2.6 ~ 2.7 配置 系统环境 Path
> - 解压 vueadmin-cli.zip 并进入文件夹
> - npm install -g 进行全局安装

# 命令

## 通用

> 1. 帮助 ：`vueadmin -h`
> 2. 版本 ：`vueadmin -v`

## 操作命令

> 1. 初始化 ： `vueadmin init (项目名称) (项目模板)`
>    - 项目模板：security
> 2. 新增 api ：`vueadmin addapi (api 名称) -m (egg 模型路径)`
>    - 创建位置 : (项目名)/src/api
>    - egg 模型路径 : 绝对路径
>    - 资源名称对应后台资源名称
>    - api 前缀, 如 api/v1/
> 3. 新增页面 : `vueadmin addpage (类型) (文件名) -m (egg 模型路径)`
>    - 文件名 : test/test
>    - 创建位置 ：(项目名)/src/views/modules
>    - egg 模型路径 : 绝对路径
>    - 类型
>      - list : 列表页面
>      - maintain : 创建/修改页面
> 4. 生成路由 ： `vueadmin addroute`
>    - 生成完成后将代码复制到 项目名/src/router/index.js 中，即可 (注意，确定路由的路径是否正确，设置备注)
