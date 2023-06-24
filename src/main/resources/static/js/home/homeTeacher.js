$(document).ready(function () {
//     check token valid if invalid redirect to login
    const token = localStorage.getItem('token');
    const refreshToken = localStorage.getItem('refreshToken');
    const tokenType = localStorage.getItem('tokenType');

    if (!token || !refreshToken || !tokenType) {
        window.location.href = '/login?with=teacher';
    }

//     call api with token if token invalid redirect to login or refresh token and call api again if refresh token invalid redirect to login
//     if token valid call api get data and render to html
    $.ajax({
        url: '/api/v1/teacher',
        type: 'GET',
        headers: {
            'Authorization': `${tokenType}${token}`
        },
        success: function (response) {
            console.log(response)
        },
        error: function (err) {
            console.log(err)
            retryWithRefreshToken(refreshToken, tokenType)
        }
    })

    function retryWithRefreshToken(refreshToken, tokenType) {
        $.ajax({
            url: '/api/v1/auth/refresh-token',
            type: 'POST',
            headers: {
                Authorization: `${tokenType}${refreshToken}`
            },
            success: function (response) {
                console.log(response);
                localStorage.setItem('token', response.token);
                localStorage.setItem('refreshToken', response.refreshToken);
                localStorage.setItem('tokenType', response.tokenType);
                // call api again

            },
            error: function (err) {
                console.log(err);
                window.location.href = '/login?with=teacher';
            }
        })
    }
})