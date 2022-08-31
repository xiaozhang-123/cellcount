## 简介
活死细菌在线计算系统服务端负责按照业务相关的需求，实现对用户账号的管理功能，以及对用户上传的活死细菌图片的计算处理功能。服务端采用Spring boot 2.5框架进行开发，使用maven构建项目，数据库ORM框架使用JPA，Reids数据库实现临时数据快速存取以及访问，账号注册采用邮箱激活方式实现，用户图片在线访问采用Nginx 配置静态资源访问实现。

## 使用说明
自动生成文档命令
mvn asciidoctor:process-asciidoc
mvn generate-resources（可能要命令要多运行几遍）

使用连接访问（本地调试版
http://localhost:8080/swagger-ui.html
http://localhost:8080/v2/api-docs

nohup java -jar demo-0.0.1-SNAPSHOT.jar --spring.profiles.active=alitest> temp.txt &

