$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');

    const historyItems = $('.history-item');

    historyItems.on('click', function () {
        const historyItem = $(this);

        const attendanceId = historyItem.data('id');

        $.ajax({
            url: `/api/v1/attendance/${attendanceId}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                console.log(data)
            },
            error: function (error) {
                console.log(error.responseText)
            }
        })
    })
})