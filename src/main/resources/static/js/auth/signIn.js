$(document).ready(function () {
    const formSignIn = $('#formSignIn');
    const btnSignIn = $('.btn-sign');
    const errorEle = $('#error');
    const alertEle = $('#alertError');
    btnSignIn.on('click', function (e) {
        e.preventDefault();
        const username = $('#username').val();
        const password = $('#password').val();
        const role = $('#role').val();
        const payload = JSON.stringify({
            username: username,
            password: password,
            role: role
        })
        $.ajax({
            url: '/api/v1/auth/authenticate',
            type: 'POST',
            data: payload,
            contentType: 'application/json',
            dataType: 'json',
            success: function (response) {
                console.log(response);
                const {token , refreshToken} = response;
                localStorage.setItem('token', token);
                localStorage.setItem('refreshToken', refreshToken);
                location.href = '/' + role + '/home';
            },
            error: function (error) {
                console.log(error);
                errorEle.text(error.responseJSON.message);
                alertEle.removeClass('d-none');
            }
        })
    })
})