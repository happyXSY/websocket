//var i = 0;

$(function() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));
    var types = [ 'Apple', 'Banana', 'Orange' ];

    // 连接websocket server
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/carousel', function(carousel) {
            console.log(carousel);
            var id = JSON.parse(carousel.body).id;
            // 收到消息更新echarts
            $.ajax({
                url : 'echarts/get' + types[id] + 'Option',
                dataType : 'json',
                contentType : 'application/json',
                success : function(option) {
                    myChart.setOption(option);
                }
            });
        });
        // 初始化
        stompClient.send("/app/getCarousel", {}, "");
    });

    // setInterval(() => {
    // // 指定图表的配置项和数据
    // $.ajax({
    // url : '/echarts/get' + types[i % 3] + 'Option',
    // dataType : 'json',
    // contentType : 'application/json',
    // success : function(option) {
    // // 使用刚指定的配置项和数据显示图表。
    // myChart.setOption(option);
    // i++;
    // console.log(i);
    // }
    // });
    // }, 5000);

});