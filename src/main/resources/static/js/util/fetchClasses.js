function fetchClasses(
    {
        token,
        levelId,
        semesterId,
        page = 0,
        size = 8
    },
    callback
) {
    $.ajax({
        url: `/api/v1/class/get?levelId=${levelId}&semesterId=${semesterId}&page=${page}&size=8`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function (response) {
            callback(response)
        },
        error: function (error) {
            console.log(error.responseText);
        }
    })
}