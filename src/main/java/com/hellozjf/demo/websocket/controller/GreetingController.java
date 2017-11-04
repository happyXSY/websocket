package com.hellozjf.demo.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.hellozjf.demo.websocket.schedule.CarouselSchedule;
import com.hellozjf.demo.websocket.vo.CarouselVO;
import com.hellozjf.demo.websocket.vo.Greeting;
import com.hellozjf.demo.websocket.vo.HelloMessage;

@Controller
public class GreetingController {
    
    private static final Logger LOG = LoggerFactory.getLogger(GreetingController.class);
    
    @Autowired
    private CarouselSchedule carouselSchedule;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @MessageMapping("/getCarousel")
    @SendTo("/topic/carousel")
    public CarouselVO getCarousel() throws Exception {
        LOG.info("getCarousel");
        CarouselVO carouselVO = new CarouselVO();
        carouselVO.setId(carouselSchedule.getId());
        return carouselVO;
    }
}