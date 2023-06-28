$(document).ready(function () {
    const btnAdd = $(".btn-add");
    const filter = $("#filter").val();
    const page = $("#page").val();
    const token = localStorage.getItem('token');
    $.ajax({
        headers: {
            'Authorization': `Bearer ${token}`
        },
        url: `/api/v1/admin/members?filter=${filter}&page=${page}`,
        type: 'GET',
        success: function (data) {
            console.log(data)
        },
        error: function (data) {
            console.log(data)
        }
    })
    function registerEvent(element, event, func) {
        element.unbind(event);
        element.bind(event, func);
    }
})