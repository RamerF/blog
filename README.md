# admin_springdata
管理端框架,基于SpringBoot,SpringSecurity,SpringData,LayUi.

[演示1 ,旧版](http://ramer.tpddns.cn:8000/manage/index)

[演示2 ,新功能开发中](http://ramer.tpddns.cn:9000/manage/index)

超级管理员: `admin/admin`
观察员: `10000/12345678`

# 概览
![参数配置](http://ramer.tpddns.cn:9000/public/upload/public/2019/08/13/config.png)
![参数配置单选]('http://ramer.tpddns.cn:9000/public/upload/public/2019/08/13/config-single-select.png')
![管理员管理](http://ramer.tpddns.cn:9000/public/upload/public/2019/08/13/manager.png)
![角色管理](http://ramer.tpddns.cn:9000/public/upload/public/2019/08/13/role.png)
![权限管理](http://ramer.tpddns.cn:9000/public/upload/public/2019/08/13/privilege.png)

# 安装
1. 修改application.yml中数据库地址以及用户名密码
2. 启动项目
3. 访问: [http://localhost:8080/manage/index](http://localhost:8080/manage/index) 账号:` admin/admin`

# 生成增删改查功能代码
运行 `MainGenerator.java`,现在支持配置文件,参考config.ini
```
1. 请输入domain对象的包路径: 例如: org.ramer.admin.entity.domain.manage.Manager
2. 请输入domain对象的class目录: 例如: D:\workspace\admin-springdata\target\classes
    tip: 该路径是指编译输出路径,不包括包目录
    (上述Manager.java文件编译后目录为:D:\workspace\admin-springdata\target\classes\org\ramer\admin\entity\domain\manage\Manager.class)
3. 请输入对象的别名: 例如: manager
    tip: 权限,变量名以及请求路径使用该变量
4. 请输入对象的中文描述: 例如: 管理员
    tip: 用于生成swagger文档描述
5. 复制到指定路径 ?(y/n)
    tip: n: 生成代码文件到当前(项目|jar同级)目录下,y: 指定每个文件的目录
```
