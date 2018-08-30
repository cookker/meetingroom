const WEEKDAY = {"MONDAY":1, "TUESDAY":2, "WEDNESDAY":3, "THURSDAY":4, "FRIDAY":5};
const BGCOLOR   = ["#00FFFF","#7FFFD4","#0000FF","#8A2BE2","#A52A2A","#DEB887","#5F9EA0","#6495ED","#FF1493"];
const FONTCOLOR = ["#000000","#000000","#7FFFD4","#00FFFF","#00FFFF","#A52A2A","#000000","#7FFF00","#FFE4C4"];

/**
 * 예약하기 모달 띄우기
 */
$('#create-reservation-modal').on('shown.bs.modal', function () {
    $('#create-reservation').trigger('focus')
});

/**
 * 회의실 생성 모달 띄우기
 */
$('#create-meetingroom-modal').on('shown.bs.modal', function () {
    $('#create-meetingroom').trigger('focus')
});

/**
 * 회의실 생성버튼 클릭
 */
$("#cmr-save").on("click", function(){
    var roomName = $("#cmr-name").val();

    var postData ={
        roomName: roomName,
    };

    $.ajax({
        url:"/room",
        type:"POST",
        data:JSON.stringify(postData),
        contentType:"application/json; charset=utf-8",
        dataType:"json",
    }).always(function(response) {
        if(response && response.code == "0000"){
            drawMeetingRoom();
        }else{
            alert(response.responseJSON.message);
        }
    });
});

/**
 * 예약 생성버튼 클릭
 */
$("#cr-save").on("click", function(){
    var postData ={
        memberName:$("#cr-member").val(),
        roomName:$("#cr-room").val(),
        date: $("#cr-date").val(),
        time: $("#cr-time").val(),
        repeat: $("#cr-repeat").val(),
        comment: $("#cr-comment").val()
    };

    $.ajax({
        url:"/reservation",
        type:"POST",
        data:JSON.stringify(postData),
        contentType:"application/json; charset=utf-8",
        dataType:"json"
    }).always(function(response) {
        if(response && response.code == "0000"){
            clearTimeScheduleTable();
            drawTimeScheduleTable();
            getReservationsByRoomName(selectedRoom.roomName);
        }else{
            alert(response.responseJSON.message);
        }
    });
});

/**
 * 회의실 뷰 그리기
 */
function drawMeetingRoom(){
    $.get("/rooms", function(result){
        $("#meeting-room-list ul").empty();
        $.each(result, function(i, room){
            $('#meeting-room-list .list-group')
                .append("<li onclick='getReservationsByRoomName(this.innerHTML)' class='list-group-item'>" + room.name + "</li>");
        });
    });
}

/**
 * 예약 스케쥴 테이블 내용 삭제
 */
function clearTimeScheduleTable(){
    var table = document.getElementById("timetable");
    while(table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}

/**
 * 예약 스케쥴 테이블 그리기
 */
function drawTimeScheduleTable() {
    var table = document.getElementById("timetable");
    var head = table.insertRow(0);
    head.insertCell(0).innerHTML = '';
    head.insertCell(1).innerHTML = '월';
    head.insertCell(2).innerHTML = '화';
    head.insertCell(3).innerHTML = '수';
    head.insertCell(4).innerHTML = '목';
    head.insertCell(5).innerHTML = '금';

    var hour = 9;
    var min = 0;
    for(var i = 0; i < 19; i++){
        var row = table.insertRow(i + 1);
        if(i%2 === 0 && i > 0){
            hour++;
        }
        for(var j = 0 ; j < 6 ; j++){
            var cell = row.insertCell(j);
            min = i % 2 !== 0 ? 30 : 0;
            var strHour = hour < 10 ? '0' + hour : hour;
            var strMin = min < 10 ? '0' + min : min;
            if(j === 0){
                cell.innerText = strHour + ":" + strMin;
            }
            cell.setAttribute("id", strHour.toString() + strMin.toString() + j.toString());
            cell.width = 50;
        }
    }
}
/*
* 회의실에 예약된 방 조회.
* */
var selectedRoom = {};
function getReservationsByRoomName(name, li){
    document.querySelectorAll(".list-group-item").forEach(r =>{
        if(r.innerHTML == name){
            $(r).css("background-color", "#DAA520");
        }else{
            $(r).css("background-color", "#FFFFFF");
        }
    });

    selectedRoom.roomName = name;
    clearTimeScheduleTable();
    drawTimeScheduleTable();

    const today = $("#today").text();

    $.get("/reservations/"+name + "?date=" + today, function(result){
        var colorIndex = Math.floor(Math.random() * BGCOLOR.length);
        Array.from(result).forEach(r =>{
            var comment = r.comment;
            var weekOfDay = WEEKDAY[r.dayOfWeek];
            var from = r.reservedTimeFrom;
            var to   = r.reservedTimeTo;
            var user = r.member.name;
            var repeatSeq = r.repeatSeq;
            var repeatTotal = r.repeatTotal;
            var repeatText ="";
            if(repeatTotal > 1){
                repeatText = "<br/>[반복 " + (repeatSeq + 1) + "회 /" + repeatTotal +"]";
            }
            $("#"+from+weekOfDay).html(comment + "<br/>(" + user + ")" + repeatText);
            colorIndex = colorIndex % (BGCOLOR.length - 1);
            colorIndex++;

            for(let i = from ; i <= to ;){
                let id = "#" + i + weekOfDay;
                $(id).css("background-color", BGCOLOR[colorIndex]);
                $(id).css("color", FONTCOLOR[colorIndex]);
                $(id).css("font-size", "12px");
                $(id).css("text-align", "center");
                $(id).attr("data-from",from);
                $(id).attr("data-to",to);
                $(id).on("click", function(){
                    popupReservationDialog($(this).attr("id"))
                });

                let increase = parseInt(i) + 30;
                if((increase%100) === 60){
                    increase = increase - 60 + 100;
                }
                i =  parseInt(increase);
                i = i < 1000 ? '0' + i : i;
            }
        });
    });
}

/**
 * 예약된 방 수정을 위한 다이얼로그 띄우기
 */
function popupReservationDialog(tdId){
    //ex) tdId=12303 = 12시30분, 수(3)
    $('#update-reservation-modal').modal('show');

    var yyyyMMddMonday = $("#hidden-monday").text();
    var mon = new Date(yyyyMMddMonday.substring(0,4) + "-" + yyyyMMddMonday.substring(4,6) + "-" + yyyyMMddMonday.substring(6));
    var weekOfDay = tdId.substring(4);
    var targetDate = new Date();
    targetDate.setDate(mon.getDate() + (parseInt(weekOfDay) - 1));
    var fromTime = $("#"+tdId).data().from;
    var toTime = $("#"+tdId).data().to;

    $.get("/reservations/"+selectedRoom.roomName + "/" + targetDate.yyyymmdd() + "/" + fromTime +"/" + toTime, function(result){
        $("#hidden-reservation-id").text(result.reservationId);
        $("#ur-date").val(result.reservedDate);
        $("#ur-time").val(result.reservedTimeFrom + "~" + result.reservedTimeTo);
        $("#ur-comment").val(result.comment);
    });
}

/**
 * 현재 날짜를 기준으로 이전주 다음주 획득
 */
function getPreviousAndNextWeekday(){
    const today = $("#today").text();
    $.get("/dates/" + today, function(result){
        if(result && result.data){
            $("#today").text(result.data.today);
            weekday.previousWeekDay = result.data.previousWeekDay;
            weekday.nextWeekDay = result.data.nextWeekDay;
            $("#hidden-monday").text(result.data.monday);
        }
    });
}

/**
 * 예약된 방 삭제 버튼을 눌렀을 때.
 */
$("#ur-delete").on("click", function(){
    var reservationId = $("#hidden-reservation-id").text();

    $.ajax({
        url:"/reservation/" + reservationId,
        type:"DELETE",
        data:JSON.stringify(reservationId),
        contentType:"application/json; charset=utf-8",
        dataType:"json"
    }).always(function(response) {
        if(response && response.code == "0000"){
            clearTimeScheduleTable();
            drawTimeScheduleTable();
            getReservationsByRoomName(selectedRoom.roomName);
        }else{
            alert(response.responseJSON.message);
        }
    });
});

/**
 * 예약된 방 수정 버튼을 눌렀을 때
 */
$("#ur-save").on("click", function(){
    var reservationId = $("#hidden-reservation-id").text();
    var postData ={
        reservedDate: $("#ur-date").val(),
        reservedTime: $("#ur-time").val(),
        comment: $("#ur-comment").val()
    };

    $.ajax({
        url:"/reservation/" + reservationId,
        type:"PUT",
        data:JSON.stringify(postData),
        contentType:"application/json; charset=utf-8",
        dataType:"json"
    }).always(function(response) {
        if(response && response.code == "0000"){
            clearTimeScheduleTable();
            drawTimeScheduleTable();
            getReservationsByRoomName(selectedRoom.roomName);
        }else{
            alert(response.responseJSON.message);
        }
    });
});

/**
 * 화면 상단 날짜 그리기
 */
function drawToday(){
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1;
    var yyyy = today.getFullYear();
    mm = mm < 10 ? '0' + mm : mm;
    dd = dd < 10 ? '0' + dd : dd;

    today = yyyy + '-' + mm + '-' + dd;
    $("#today").text(today);

    getPreviousAndNextWeekday();
    return today;
}
var weekday = {};
/**
 * 이전 주 버튼 클릭 하였을 때
 */
$("#previous-week").on("click", function(){
    $("#today").text(weekday.previousWeekDay);
    getPreviousAndNextWeekday();
    drawTimeScheduleTable();
    getReservationsByRoomName(selectedRoom.roomName);
});

/**
 * 다음 주 버튼 클릭 하였을 때
 */
$("#next-week").on("click", function(){
    $("#today").text(weekday.nextWeekDay);
    getPreviousAndNextWeekday();
    drawTimeScheduleTable();
    getReservationsByRoomName(selectedRoom.roomName);
});


Date.prototype.yyyymmdd = function() {
    var mm = this.getMonth() + 1;
    var dd = this.getDate();

    return [this.getFullYear(),
        (mm > 9 ? '' : '0') + mm,
        (dd > 9 ? '' : '0') + dd
    ].join('');
};
