package com.hellozjf.demo.websocket.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hellozjf.demo.websocket.vo.CarouselVO;

@Component
public class CarouselSchedule {
    
    private static final Logger LOG = LoggerFactory.getLogger(CarouselSchedule.class);
    private int id = 0;

    @Autowired
    private SimpMessagingTemplate template;

    @Scheduled(fixedDelay=5000)
    public void publishUpdates(){
        CarouselVO carouselVO = new CarouselVO();
        carouselVO.setId(id);
        template.convertAndSend("/topic/carousel", carouselVO);
        LOG.info("id:{}", id);
        id = (id + 1) % 3;
    }
    
    public int getId() {
        return id;
    }
}
