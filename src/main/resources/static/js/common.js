//---------- 링크 이동 ---------
let link = (link) => {
    let csrfHeader = $("#_csrf_header").attr('content');
    let csrfToken = $("#_csrf").attr('content');

    let callback =
        $.ajax({
            type       : "post",
            url        : link,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        })
    callback.success( data => {
        $("#mainContents").html(data);
    });
    callback.fail( (xhr, status, error) => {
        let errMsg = JSON.parse(xhr.responseText).message;
        if(xhr.status == '401') // 비로그인
            location.href = "/login";
        if(xhr.status == '403') // 접근 권한 없는 사용자
            location.href = "/api/denied?exception=" + errMsg;
    })
}


// ---------admin 링크이동 --------
function tabChange(tabNum){
    if(tabNum == '1'){
        location.href='/admin/accounts';
    }else if(tabNum == '2'){
        location.href='/admin/roles';
    }else if(tabNum == '3'){
        location.href='/admin/resources';
    }

}