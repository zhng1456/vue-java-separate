# vue-java-separate
This is the source code of ![Easy Way to Separate Java Backend and Vue Frontend](https://ywzhang.hashnode.dev/easy-way-to-separate-java-backend-and-vue-frontend)
## How to Run

My complete project is on the github.

```bash
git clone git@github.com:zhng1456/vue-java-separate.git
```

Open the project in IDEA and run Application.java

![1.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458858301/poZh5lTEg.png align="left")


Access [http://localhost:9090](http://localhost:9090) in chrome
![2.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458810966/6o5aAVUyO.png align="left")


Explore the network tab in chrome dev tool,we find that [http://localhost:9090/api/users](http://localhost:9090/api/users) returns the data of the table.


![3.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458882916/_ikD1FfzB.png align="left")

The post will help you create the project from scratch and separate backend and frontend.

## Java Backend

First,create a maven project in you IDEA.

File→New → Project

create a module,New→module

pom.xml

```bash
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>vue-java-separate</artifactId>
        <groupId>org.example</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>java-backend</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

</project>
```

Create the first controller

```bash
package com.yw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author henry
 * @date 2021/8/3
 */
@RestController
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "admin";
    }
}
```

change port in application.yml

```bash
server:
  port: 9090
  context-path: /
```

Run the project and curl http://localhost:9090

```bash
$ curl "http://127.0.0.1:9090/"
admin%
```

## Vue project

### Init

use vue-cli create webpack project.

I create vue project in root of the maven project.

If you develop more complex project,you can put vue project in another git repository.

```bash
vue init webpack vue-frontend

cd vue-frontend

# run the application
npm run dev

# build
npm run build
```

After execute “npm run build”,the dist dictory is as follows

![4.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458910541/2MW2gfb76.png align="left")

### Simplify js and css files

Modify the webpack config files to make one js file and one css file in dist directory.


![5.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458929607/8b5UIGzyU.png align="left")

The detail is in this commit.

### Install Element UI

```bash
npm install element-ui -S

```

modify main.js

```bash
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>'
})
```

### Send HTTP Request

Add a controller in java backend

```bash
package com.yw.controller;

import com.yw.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("users")
    public List<User> selectAllUsers() {
        List<User> userList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(1);
            user.setName(String.format("name%s", i));
            user.setAddress(String.format("address%s", i));
            userList.add(user);
        }
        return userList;
    }
}
```

Call the controller in vue and show results with el-table

```bash
export async function getAllUsers() {
  const response = await fetch('/api/users');
  return await response.json();
}
```

```bash
<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <el-row>
      <el-button>default</el-button>
      <el-button type="primary">primary</el-button>
      <el-button type="success">success</el-button>
      <el-button type="info">info</el-button>
      <el-button type="warning">warning</el-button>
      <el-button type="danger">danger</el-button>
    </el-row>
    <el-table
      :data="userList"
      style="width: 100%">
      <el-table-column
        prop="id"
        label="id"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="name"
        width="180">
      </el-table-column>
      <el-table-column
        prop="address"
        label="address">
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getAllUsers } from '../services/user'
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      userList: []
    }
  },

  methods: {
    getAllUsers() {
      getAllUsers().then(response => {
        console.log(response)
        this.userList = response
      })
    }
  },
  mounted() {
    this.getAllUsers();
  }
}
</script>
```

### Run Locally

Before run the project,we need to config proxy in webpack config

```bash
devServer: {
    proxy: {
      '/api/*': {
        target: 'http://localhost:9090',
        changeOrigin: true,
        secure: false
      }
    }
  }
```

Run java backend and run vue project,access http://localhost:8080

![6.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663458953556/7o-lsn3k4.png align="left")

## Separate  Backend and Frontend

### How to Separate

The role of the front-end project is to provide the built js files and css files.If we can access the file by internet,all links are [fine.](http://fine.So)So the solution to separate backend and frontend is

- Put the root of pages,index.html to java backend and use FreeMarker to show it
- Upload files to Google Cloud Storage
- Import js files and css files in index.html

### Add FreeMarker

Add Maven dependency

```bash
<dependency>
	<groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

config FreeMarker in application.yaml

```bash
server:
  port: 9090
  context-path: /

spring:
  freemarker:
    suffix: .ftl
```

Create the directory templates in resources and create index.ftl

```
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>vue-frontend</title>
</head>
<body>
<div id="app"></div>
</body>
</html>

```

### Upload Files to Google Cloud Storage

We need to craete a bucket and the most important thing is to set **Public to internet.**

![11.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663459168922/CwHLOVqu5.png align="left")

Then upload js files and css files(you can find this files in your dist directory of vue project)

![google.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663459113824/qIf2mlibY.png align="left")

Click “Copy URL” and confirm that you can access it.

### Import cloud files in template

Import files in index.ftl

```bash
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <link href="https://storage.googleapis.com/webpack-frontend/app.css" rel="stylesheet">
    <title>vue-frontend</title>
</head>
<body>
<div id="app"></div>
<script type="text/javascript" src="https://storage.googleapis.com/webpack-frontend/app.js"></script>
</body>
</html>
```

Run java backend

```bash
/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -noverify -Dspring.output.ansi.enabled=always -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64768:/Applications/IntelliJ IDEA.app/Contents/bin -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/legacy8ujsse.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/openjsse.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/rt.jar:/Users/yunwang/workspace/vue-java-separate/java-backend/target/classes:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter-web/2.1.6.RELEASE/spring-boot-starter-web-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter/2.1.6.RELEASE/spring-boot-starter-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot/2.1.6.RELEASE/spring-boot-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.1.6.RELEASE/spring-boot-autoconfigure-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.1.6.RELEASE/spring-boot-starter-logging-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/yunwang/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/yunwang/.m2/repository/org/slf4j/slf4j-api/1.7.26/slf4j-api-1.7.26.jar:/Users/yunwang/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.11.2/log4j-to-slf4j-2.11.2.jar:/Users/yunwang/.m2/repository/org/apache/logging/log4j/log4j-api/2.11.2/log4j-api-2.11.2.jar:/Users/yunwang/.m2/repository/org/slf4j/jul-to-slf4j/1.7.26/jul-to-slf4j-1.7.26.jar:/Users/yunwang/.m2/repository/javax/annotation/javax.annotation-api/1.3.2/javax.annotation-api-1.3.2.jar:/Users/yunwang/.m2/repository/org/springframework/spring-core/5.1.8.RELEASE/spring-core-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-jcl/5.1.8.RELEASE/spring-jcl-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/yaml/snakeyaml/1.23/snakeyaml-1.23.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter-json/2.1.6.RELEASE/spring-boot-starter-json-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.9.9/jackson-databind-2.9.9.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.9.9/jackson-datatype-jdk8-2.9.9.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.9.9/jackson-datatype-jsr310-2.9.9.jar:/Users/yunwang/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.9.9/jackson-module-parameter-names-2.9.9.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/2.1.6.RELEASE/spring-boot-starter-tomcat-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/9.0.21/tomcat-embed-core-9.0.21.jar:/Users/yunwang/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/9.0.21/tomcat-embed-el-9.0.21.jar:/Users/yunwang/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/9.0.21/tomcat-embed-websocket-9.0.21.jar:/Users/yunwang/.m2/repository/org/hibernate/validator/hibernate-validator/6.0.17.Final/hibernate-validator-6.0.17.Final.jar:/Users/yunwang/.m2/repository/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar:/Users/yunwang/.m2/repository/org/jboss/logging/jboss-logging/3.3.2.Final/jboss-logging-3.3.2.Final.jar:/Users/yunwang/.m2/repository/com/fasterxml/classmate/1.4.0/classmate-1.4.0.jar:/Users/yunwang/.m2/repository/org/springframework/spring-web/5.1.8.RELEASE/spring-web-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-beans/5.1.8.RELEASE/spring-beans-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-webmvc/5.1.8.RELEASE/spring-webmvc-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-aop/5.1.8.RELEASE/spring-aop-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-context/5.1.8.RELEASE/spring-context-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/spring-expression/5.1.8.RELEASE/spring-expression-5.1.8.RELEASE.jar:/Users/yunwang/.m2/repository/org/springframework/boot/spring-boot-starter-freemarker/2.1.6.RELEASE/spring-boot-starter-freemarker-2.1.6.RELEASE.jar:/Users/yunwang/.m2/repository/org/freemarker/freemarker/2.3.28/freemarker-2.3.28.jar:/Users/yunwang/.m2/repository/org/springframework/spring-context-support/5.1.8.RELEASE/spring-context-support-5.1.8.RELEASE.jar com.yw.Application

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.6.RELEASE)

2022-09-18 06:21:04.801  INFO 3257 --- [           main] com.yw.Application                       : Starting Application on Zhangs-MacBook-Air.local with PID 3257 (/Users/yunwang/workspace/vue-java-separate/java-backend/target/classes started by yunwang in /Users/yunwang/workspace/vue-java-separate)
2022-09-18 06:21:04.806  INFO 3257 --- [           main] com.yw.Application                       : No active profile set, falling back to default profiles: default
2022-09-18 06:21:05.182  INFO 3257 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 9090 (http)
2022-09-18 06:21:05.191  INFO 3257 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-09-18 06:21:05.191  INFO 3257 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.21]
2022-09-18 06:21:05.229  INFO 3257 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-09-18 06:21:05.230  INFO 3257 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 369 ms
2022-09-18 06:21:05.307  INFO 3257 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2022-09-18 06:21:05.342  INFO 3257 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page template: index
2022-09-18 06:21:05.398  INFO 3257 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9090 (http) with context path ''
2022-09-18 06:21:05.403  INFO 3257 --- [           main] com.yw.Application                       : Started Application in 15.777 seconds (JVM running for 21.088)
```

Notice “Adding welcome page template: index” and it means your config is correct.

Access http://localhost:9090


![7.png](https://cdn.hashnode.com/res/hashnode/image/upload/v1663459049093/zAywqUxDV.png align="left")

### M**ore convenient Way to Upload files**

It is too troublesome to manually upload the file every time, is there a more convenient way?

Yes,we can upload files by node client of Google Cloud Storage.

I will detail this part in another blog post.


