package com.hellozjf.demo.websocket.vo;

// 该类是发往浏览器的消息实体
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