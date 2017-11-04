package com.hellozjf.demo.websocket.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellozjf.demo.websocket.vo.HighchartsData;

@RestController
@RequestMapping(value="/highcharts", produces="application/json", consumes="application/json")
public class HighchartsController {

    @RequestMapping(value="/getAppleData")
    public HighchartsData getAppleData() {
        HighchartsData data = HighchartsData.instance();
        data.colors.set(0, "#FF0000");
        data.title.text = "苹果";
        data.subtitle.text = "苹果";
        Double[] seriesData = {10.0, 100.0, 20.0, 200.0, 30.0, 300.0, 40.0, 400.0, 50.0, 500.0};
        data.series.get(0).name = "苹果";
        data.series.get(0).data = Arrays.asList(seriesData);
        return data;
    }
    
    @RequestMapping(value="/getBananaData")
    public HighchartsData getBananaData() {
        HighchartsData data = HighchartsData.instance();
        data.colors.set(0, "#FFFF00");
        data.title.text = "香蕉";
        data.subtitle.text = "香蕉";
        Double[] seriesData = {500.0, 50.0, 400.0, 40.0, 300.0, 30.0, 200.0, 20.0, 100.0, 10.0};
        data.series.get(0).name = "香蕉";
        data.series.get(0).data = Arrays.asList(seriesData);
        return data;
    }
    
    @RequestMapping(value="/getOrangeData")
    public HighchartsData getOrangeData() {
        HighchartsData data = HighchartsData.instance();
        data.colors.set(0, "#FF7F00");
        data.title.text = "橘子";
        data.subtitle.text = "橘子";
        Double[] seriesData = {500.0, 300.0, 100.0, 40.0, 20.0, 10.0, 30.0, 50.0, 200.0, 400.0};
        data.series.get(0).name = "橘子";
        data.series.get(0).data = Arrays.asList(seriesData);
        return data;
    }
}
