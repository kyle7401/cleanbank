function formatNumber (num) {
    if(!num) return num;
    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
}

function toKorDateStr(value) {
    //                    return new Date(value).toDateString();
//                    https://msdn.microsoft.com/library/ff743760(v=vs.94).aspx
    var date = new Date(value);
    /*                    var options = {
     weekday: "long", year: "numeric", month: "short",
     day: "numeric", hour: "2-digit", minute: "2-digit"
     };
     return date.toLocaleTimeString("ko-KR", options);*/
    var dateString = date.toLocaleDateString("ko-KR");
    dateString = dateString.replace( /. /g, '-');
    dateString = dateString.replace('.', '');

    return dateString;
}

function makeDateTimepicker(objDiv) {
    $(objDiv).datetimepicker({
        locale: 'ko',
        format: 'YYYY-MM-DD HH:mm',
        sideBySide: true
    });
}

function makeDatepicker(objInput) {
    $(objInput).datepicker({
        format: 'yyyy-mm-dd',
        //startDate: '-0d',
        language: 'ko',
        autoclose: true,
        todayHighlight: true,
        immediateUpdates: true,
        orientation: 'top'
    });
}

function makeDatepicker2(objInput) {
    $(objInput).datepicker({
        format: 'yyyy-mm-dd',
        //startDate: '-0d',
        language: 'ko',
        autoclose: true,
        todayHighlight: true,
        immediateUpdates: true
    });
}