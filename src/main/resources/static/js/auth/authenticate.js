function authenticate({username, password, role, errorLocation}) {
    $.ajax({
        url: '/api/v1/auth/authenticate',
        type: 'POST',
        data: JSON.stringify({
            username: username,
            password: password,
            role: role
        }),
        contentType: 'application/json',
        dataType: 'json',
        success: function (response) {
            console.log(response);
            const {token , refreshToken} = response;
            localStorage.setItem('token', token);
            localStorage.setItem('refreshToken', refreshToken);
            document.cookie = `token=${token}`;
            document.cookie = `refreshToken=${refreshToken}`;
        },
        error: function (error) {
            console.log(error);
            document.location = errorLocation;
        }
    })
}