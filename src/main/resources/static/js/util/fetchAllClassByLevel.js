function fetchAllClassByLevelAndSemester(
    {
        token,
        levelId,
        semesterId,
        page = 0,
    },
    callback,
    beforeSendCallback = () => {},
) {
    $.ajax({
        url: `/api/v1/class/get?levelId=${levelId}&semesterId=${semesterId}&page=${page}`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        beforeSend: beforeSendCallback,
        success: function (data) {
            callback(data);
        },
        error: function (data) {
            console.log(data.responseJSON.message)
        }
    })
}