package com.hellozjf.demo.websocket.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EChartOption {
    
    public List<String> color;
    public Tooltip tooltip;
    public Grid grid;
    public List<XAxis> xAxis;
    public List<YAxis> yAxis;
    public List<Series> series;
    
    public class Tooltip {
        public String trigger;
        public AxisPointer axisPointer;

        public class AxisPointer {
            public String type;
        }
    }

    public class Grid {
        public String left;
        public String right;
        public String bottom;
        public boolean containLabel;
    }
    
    public class XAxis {
        public String type;
        public List<String> data;
        public AxisTick axisTick;
        
        public class AxisTick {
            public boolean alignWithLabel;
        }
    }
    
    public class YAxis {
        public String type;
    }
    
    public class Series {
        public String name;
        public String type;
        public String barWidth;
        public List<Integer> data;
    }
    
    public static EChartOption instance() {
        EChartOption option = new EChartOption();
        option.color = new ArrayList<String>();
        option.color.add("#3398DB");
        option.tooltip = option.new Tooltip();
        option.tooltip.trigger = "axis";
        option.tooltip.axisPointer = option.tooltip.new AxisPointer();
        option.tooltip.axisPointer.type = "shadow";
        option.grid = option.new Grid();
        option.grid.left = "3%";
        option.grid.right = "4%";
        option.grid.bottom = "3%";
        option.grid.containLabel = true;
        option.xAxis = new ArrayList<XAxis>();
        XAxis xAxis = option.new XAxis();
        xAxis.type = "category";
        xAxis.data = new ArrayList<String>();
        xAxis.data.add("Mon");
        xAxis.data.add("Tue");
        xAxis.data.add("Wed");
        xAxis.data.add("Thu");
        xAxis.data.add("Fri");
        xAxis.data.add("Sat");
        xAxis.data.add("Sun");
        xAxis.axisTick = xAxis.new AxisTick();
        xAxis.axisTick.alignWithLabel = true;
        option.xAxis.add(xAxis);
        option.yAxis = new ArrayList<YAxis>();
        YAxis yAxis = option.new YAxis();
        yAxis.type = "value";
        option.yAxis.add(yAxis);
        option.series = new ArrayList<Series>();
        Series series = option.new Series();
        series.name = "直接访问";
        series.type = "bar";
        series.barWidth = "60%";
        series.data = new ArrayList<Integer>();
        series.data.add(10);
        series.data.add(52);
        series.data.add(200);
        series.data.add(334);
        series.data.add(390);
        series.data.add(330);
        series.data.add(220);
        option.series.add(series);
        return option;
    }
    
    public static void main(String[] args) throws Exception {
        EChartOption option = instance();
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(option);
        System.out.println(json);
    }
}
