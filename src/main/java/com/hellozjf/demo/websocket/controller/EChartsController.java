package com.hellozjf.demo.websocket.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hellozjf.demo.websocket.vo.EChartOption;

@RestController
@RequestMapping(value="/echarts", produces="application/json", consumes="application/json")
public class EChartsController {

    @RequestMapping(value="/getAppleOption")
    public EChartOption getAppleOption() {
        EChartOption option = EChartOption.instance();
        option.color.set(0, "#FF0000");
        String[] xAxisData = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
        option.xAxis.get(0).data = Arrays.asList(xAxisData);
        Integer[] seriesData = {10, 100, 20, 200, 30, 300, 40, 400, 50, 500};
        option.series.get(0).data = Arrays.asList(seriesData);
        return option;
    }
    
    @RequestMapping(value="/getBananaOption")
    public EChartOption getBananaOption() {
        EChartOption option = EChartOption.instance();
        option.color.set(0, "#FFFF00");
        String[] xAxisData = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
        option.xAxis.get(0).data = Arrays.asList(xAxisData);
        Integer[] seriesData = {500, 50, 400, 40, 300, 30, 200, 20, 100, 10};
        option.series.get(0).data = Arrays.asList(seriesData);
        return option;
    }
    
    @RequestMapping(value="/getOrangeOption")
    public EChartOption getOrangeOption() {
        EChartOption option = EChartOption.instance();
        option.color.set(0, "#FF7F00");
        String[] xAxisData = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
        option.xAxis.get(0).data = Arrays.asList(xAxisData);
        Integer[] seriesData = {500, 300, 100, 40, 20, 10, 30, 50, 200, 400};
        option.series.get(0).data = Arrays.asList(seriesData);
        return option;
    }
}
