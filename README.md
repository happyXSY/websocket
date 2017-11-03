# websocket

学习websocket，并实现多屏同时轮播的效果

# 使用WebSocket来构建一个交互的web应用程序\[[译](https://spring.io/guides/gs/messaging-stomp-websocket/)\]

本指南将引导您完成创建“hello world”应用程序的过程，该应用程序在浏览器和服务器之间来回发送消息。 WebSocket是TCP之上的一个非常轻薄的层。 它使得使用“子协议”嵌入消息非常合适。 在本指南中，我们将深入Spring并使用STOMP消息传递来创建交互式Web应用程序。

## 你将会构建

您将构建一个服务器，它将接受携带用户名的消息。 作为响应，它会将问候语推入客户订阅的队列中。

## 使用Maven构建

首先你建立一个基本的构建脚本。 在使用Spring构建应用程序时，您可以使用任何构建系统，但是需要使用Maven的代码包含在这里。 如果您不熟悉Maven，请参阅使用Maven构建Java项目。

创建目录结构

在您选择的项目目录中，创建以下子目录结构; 例如，使用`mkdir -p src/main/java/hello` 在*nix系统上：

```
└── src
    └── main
        └── java
            └── hello
```

`pom.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.springframework</groupId>
    <artifactId>gs-messaging-stomp-websocket</artifactId>
    <version>0.1.0</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.8.RELEASE</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>

        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>webjars-locator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>sockjs-client</artifactId>
            <version>1.0.2</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>stomp-websocket</artifactId>
            <version>2.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>bootstrap</artifactId>
            <version>3.3.7</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.1.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

Spring Boot Maven插件提供了许多方便的功能：

* 它收集类路径上的所有jar，并构建一个可运行的“über-jar”，这使得执行和传输服务更加方便。
* 它搜索`public static void main()`方法标记为可运行类。
* 它提供了一个内置的依赖解析器来设置版本号以匹配Spring Boot的依赖关系。 你可以覆盖任何你想要的版本，但是它会默认使用Boot的选择版本。

## 创建一个资源表示类

现在你已经建立了项目和构建系统，你可以创建你的STOMP消息服务。

考虑服务交互，开始这个过程。

该服务将接受包含STOMP消息中的名称的消息，该消息的主体是JSON对象。 如果给出的名字是“Fred”，那么消息可能看起来像这样：

```
{
    "name": "Fred"
}
```

为了建模带有名称的消息，可以使用`name`属性和相应的`getName()`方法创建一个普通的旧Java对象：

`src/main/java/hello/HelloMessage.java`

```
package hello;

public class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

在收到消息并提取名称后，服务将通过创建问候语并在客户端订阅的单独队列上发布该问候语来处理该问候语。 问候也将是一个JSON对象，可能看起来像这样：

```
{
    "content": "Hello, Fred!"
}
```

为了建模问候表示，需要添加另一个具有`content`属性和相应`getContent()`方法的普通旧Java对象：

`src/main/java/hello/Greeting.java`

```
package hello;

public class Greeting {

    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
```

Spring将使用Jackson JSON库自动将类型为`Greeting`的实例编组为JSON。

接下来，您将创建一个控制器来接收hello消息并发送一个问候消息。

## 创建一个消息处理控制器

在Spring使用STOMP消息传递的方法中，STOMP消息可以被路由到`@Controller`类。 例如，`GreetingController`被映射为处理消息到目的地"/hello"。

`src/main/java/hello/GreetingController.java`

```
package hello;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
```

这个控制器简洁明了，但还有很多。 让我们一步一步地打破它。

`@MessageMapping`注释可以确保如果消息被发送到目的地"/hello"，那么`greeting()`方法被调用。

消息的有效内容绑定到一个`HelloMessage`对象，该对象被传递到`greeting()`。

在内部，该方法的实现通过使线程睡眠1秒来模拟处理延迟。 这是为了说明在客户端发送消息之后，服务器可以采取与需要异步处理消息一样长的时间。 客户可以继续其需要做的任何工作，而不必等待响应。

在1秒延迟之后，`greeting()`方法创建一个`Greeting`对象并返回它。 返回值被广播给所有订阅者，如`@SendTo`注释中指定的"/topic/greetings"。

## 为STOMP消息传递配置Spring

既然创建了服务的基本组件，您可以配置Spring以启用WebSocket和STOMP消息传递。

创建一个名为`WebSocketConfig`的Java类，如下所示：

`src/main/java/hello/WebSocketConfig.java`

```
package hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

}
```

`WebSocketConfig`用`@Configuration`注释来表明它是一个Spring配置类。 它也被注释`@EnableWebSocketMessageBroker`。 顾名思义，`@EnableWebSocketMessageBroker`启用WebSocket消息处理，由消息代理支持。

`configureMessageBroker()`方法重写`WebSocketMessageBrokerConfigurer`中的默认方法来配置消息代理。 它首先调用`enableSimpleBroker()`来启用一个简单的基于内存的消息代理，将问候消息带回以"/topic"为前缀的客户端。 它还为绑定为`@MessageMapping`注释方法的邮件指定"/app"前缀。 该前缀将用于定义所有消息映射; 例如，“/app/hello”是`GreetingController.greeting()`方法映射来处理的端点。

`registerStompEndpoints()`方法注册"/gs-guide-websocket"端点，启用SockJS后备选项，以便在WebSocket不可用时可以使用替代传输。 SockJS客户端将尝试连接到"/gs-guide-websocket"并使用可用的最佳传输（websocket，xhr-streaming，xhr-polling等）。

## 创建一个浏览器客户端

随着服务器端的部分，现在让我们把注意力转移到将发送消息和从服务器端接收消息的JavaScript客户端。

创建一个如下所示的index.html文件：

`src/main/resources/static/index.html`

```
<!DOCTYPE html>
<html>
<head>
    <title>Hello WebSocket</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/main.css" rel="stylesheet">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
    <script src="/app.js"></script>
</head>
<body>
<noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
    enabled. Please enable
    Javascript and reload this page!</h2></noscript>
<div id="main-content" class="container">
    <div class="row">
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <label for="connect">WebSocket connection:</label>
                    <button id="connect" class="btn btn-default" type="submit">Connect</button>
                    <button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Disconnect
                    </button>
                </div>
            </form>
        </div>
        <div class="col-md-6">
            <form class="form-inline">
                <div class="form-group">
                    <label for="name">What is your name?</label>
                    <input type="text" id="name" class="form-control" placeholder="Your name here...">
                </div>
                <button id="send" class="btn btn-default" type="submit">Send</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table id="conversation" class="table table-striped">
                <thead>
                <tr>
                    <th>Greetings</th>
                </tr>
                </thead>
                <tbody id="greetings">
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
```

这个HTML文件导入了`SockJS`和`STOMP` JavaScript库，这些库将用于在websocket上使用STOMP与我们的服务器进行通信。 我们还在这里导入一个包含我们的客户端应用程序逻辑的`app.js`。

我们来创建这个文件：

`src/main/resources/static/app.js`

```
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});
```

这个JavaScript文件要注意的主要部分是`connect()`和`sendName()`函数。

`connect()`函数使用**SockJS**和**stomp.js**打开连接到"/gs-guide-websocket"，这是我们的SockJS服务器正在等待连接的地方。 连接成功后，客户端将订阅"/topic/greetings"目标，服务器将在其中发布问候消息。 当在该目的地收到问候语时，它会在DOM上添加一个段落元素来显示问候消息。

`sendName()`函数检索用户输入的名称，并使用STOMP客户端将其发送到"/app/hello"目的地（`GreetingController.greeting()`将接收到）。

## 使应用程序可执行

尽管可以将此服务作为传统WAR文件打包以部署到外部应用程序服务器，但下面演示的更简单的方法创建了独立的应用程序。 你把所有东西都封装在一个单独的，可执行的JAR文件中，由一个好的旧的Java `main()`方法驱动。 一路上，您使用Spring的支持将Tomcat servlet容器嵌入到HTTP运行时，而不是部署到外部实例。

`src/main/java/hello/Application.java`

```
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

`@SpringBootApplication`是一个方便的注释，它添加以下所有内容：

* `@Configuration`将该类标记为应用程序上下文的bean定义的源。
* `@EnableAutoConfiguration`指示Spring Boot根据类路径设置，其他bean和各种属性设置开始添加bean。
* 通常，您将为Spring MVC应用程序添加`@EnableWebMvc`，但是当Spring类在类路径中看到**spring-webmvc**时，Spring Boot会自动添加它。 这将应用程序标记为Web应用程序，并激活诸如设置`DispatcherServlet`等关键行为。
* `@ComponentScan`告诉Spring在`hello`包中查找其他组件，配置和服务，以便找到控制器。

`main()`方法使用Spring Boot的`SpringApplication.run()`方法启动应用程序。 你注意到没有一行XML吗？ 没有web.xml文件。 此Web应用程序是100％纯Java，您无需处理配置任何管道或基础设施。

## 构建可执行的JAR

您可以从命令行运行应用程序与Gradle或Maven。 或者，您可以构建一个包含所有必需依赖项，类和资源的单个可执行JAR文件，并运行该文件。 这使得在整个开发生命周期中，跨不同的环境等运输，版本和部署服务成为一个应用程序。

如果您正在使用Maven，则可以使用`./mvnw spring-boot:run`来运行该应用程序。 或者你也可以使用`./mvnw clean package`建立JAR文件。 然后可以运行JAR文件：

```
java -jar target/gs-messaging-stomp-websocket-0.1.0.jar
```

> 上面的过程将创建一个可运行的JAR。 您也可以选择[构建一个经典的WAR文件](https://spring.io/guides/gs/convert-jar-to-war/)。

记录输出显示。 该服务应该在几秒钟内启动并运行。

## 测试服务

现在服务正在运行，请将浏览器指向[http://localhost:8080](http://localhost:8080)，然后单击"连接"按钮。

打开连接后，系统会询问您的姓名。 输入你的名字，然后点击"发送"。 您的名字通过STOMP作为JSON消息发送到服务器。 经过1秒的模拟延迟后，服务器会发送一条消息，并在页面上显示" Hello"问候语。 此时，您可以发送其他名称，也可以单击"断开"按钮关闭连接。