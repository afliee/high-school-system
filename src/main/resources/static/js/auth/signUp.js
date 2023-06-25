$(document).ready(function () {
    const signUpForm = $("#signUpForm");
    const btnSignUp = $("#btnSignUp");

    const errorEle = $('#error');
    const alertEle = $('#alertError');

    btnSignUp.on('click', function (e) {
        e.preventDefault();

        const username = $("#username").val();
        const email = $("#email").val();
        const password = $("#password").val();
        const fullName = $("#fullName").val();
        const role = $("#role").val();

        const payload = {
            username: username,
            email: email,
            password: password,
            fullName: fullName
        }

        $.ajax({
            url: `/api/v1/auth/register?role=${role}`,
            type: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(payload),
            success: function (response) {
                const { token, refreshToken, tokenType } = response;
                localStorage.setItem('token', token);
                localStorage.setItem('refreshToken', refreshToken);
                localStorage.setItem('tokenType', tokenType);
                // set header before redirect
            //     call api
                location.href = `${role}`
            },
            error: function (errorResponse) {
                console.log(errorResponse);
                errorEle.text(errorResponse.responseJSON.message);
                alertEle.removeClass('d-none');
            }
        })
    })
})