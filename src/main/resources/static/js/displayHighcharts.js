//var i = 0;

$(function() {
    var types = [ 'Apple', 'Banana', 'Orange' ];

    // 连接websocket server
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/carousel', function(carousel) {
            console.log(carousel);
            var id = JSON.parse(carousel.body).id;
            // 收到消息更新highcharts
            $.ajax({
                url : '/highcharts/get' + types[id] + 'Data',
                dataType : 'json',
                contentType : 'application/json',
                success : function(data) {
                    // 使用刚指定的配置项和数据显示图表。
                    $('#container').highcharts(data);
                }
            });
        });
        // 初始化
        stompClient.send("/app/getCarousel", {}, "");
    });

    // setInterval(() => {
    // // 指定图表的配置项和数据
    // $.ajax({
    // url : '/highcharts/get' + types[i % 3] + 'Data',
    // dataType : 'json',
    // contentType : 'application/json',
    // success : function(data) {
    // // 使用刚指定的配置项和数据显示图表。
    // $('#container').highcharts(data);
    // i++;
    // console.log(i);
    // }
    // });
    // }, 5000);

});