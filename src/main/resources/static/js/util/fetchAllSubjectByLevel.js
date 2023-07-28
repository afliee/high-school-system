function fetchAllSubjectByLevel(
    {
        token,
        levelId
    },
    callback
) {
    $.ajax({
        url: `/api/v1/schedule/subjects?level=${levelId}`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function (response) {
            console.log("fetchAllSubjectByLevel.js", response);
            callback(response);
        },
        error: function (error) {
            console.log(error);
        }
    })
}