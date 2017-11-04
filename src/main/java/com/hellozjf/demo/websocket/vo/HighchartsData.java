package com.hellozjf.demo.websocket.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HighchartsData {
    
    public Chart chart;
    public List<String> colors;
    public Title title;
    public Subtitle subtitle;
    public XAxis xAxis;
    public YAxis yAxis;
    public PlotOptions plotOptions;
    public List<Series> series;

    public class Chart {
        public String type;
    }
    public class Title {
        public String text;
    }
    public class Subtitle {
        public String text;
    }
    public class XAxis {
        public List<String> categories;
    }
    public class YAxis {
        public Title title;
    }
    public class PlotOptions {
        public Line line;
        public class Line {
            public DataLabels dataLabels;
            public class DataLabels {
                public boolean enabled;
            }
            public boolean enableMouseTracking;
        }
    }
    public class Series {
        public String name;
        public List<Double> data;
    }
    
    public static HighchartsData instance() {
        HighchartsData data = new HighchartsData();
        data.chart = data.new Chart();
        data.chart.type = "line";
        data.colors = new ArrayList<String>();
        data.colors.add("#ff0000");
        data.title = data.new Title();
        data.title.text = "水果产量";
        data.subtitle = data.new Subtitle();
        data.subtitle.text = "苹果";
        data.xAxis = data.new XAxis();
        String[] categories = {"2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
        data.xAxis.categories = Arrays.asList(categories);
        data.yAxis = data.new YAxis();
        data.yAxis.title = data.new Title();
        data.yAxis.title.text = "销量";
        data.plotOptions = data.new PlotOptions();
        data.plotOptions.line = data.plotOptions.new Line();
        data.plotOptions.line.dataLabels = data.plotOptions.line.new DataLabels();
        data.plotOptions.line.dataLabels.enabled = true;
        data.plotOptions.line.enableMouseTracking = false;
        data.series = new ArrayList<>();
        Series series = data.new Series();
        series.name = "苹果";
        Double[] seriesData = {1.0, 10.0, 2.0, 20.0, 3.0, 30.0, 4.0, 40.0, 5.0, 50.0};
        series.data = Arrays.asList(seriesData);
        data.series.add(series);
        return data;
    }
    
    public static void main(String[] args) throws Exception {
        HighchartsData data = instance();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);
        System.out.println(json);
    }
}
