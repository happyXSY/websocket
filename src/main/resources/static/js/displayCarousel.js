//var i = 0;

$(function() {

    // 暂停自动轮播
    $('#myCarousel').carousel('pause');

    // 连接websocket server
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/carousel', function(carousel) {
            console.log(carousel);
            var id = JSON.parse(carousel.body).id;
            $('#myCarousel').carousel(id);
        });
        console.log("stompClient.send /app/getCarousel");
        stompClient.send("/app/getCarousel", {}, "");
    });

    // $('#myCarousel').carousel('pause');
    // setInterval(() => {
    // console.log(i);
    // $('#myCarousel').carousel(i % 3);
    // i++;
    // }, 5000);
});