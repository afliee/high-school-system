$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const btnSubmit = $('#submit');

    btnSubmit.click(function () {
        if ($(this).is(':disabled')) {
            return;
        }

        const username = $('#username').val();
        const newPassword = $('#newPassword').val();

        $(this).html(
            `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>`
        );

        $.ajax({
            url: '/api/v1/auth/reset-password',
            type: 'POST',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify({
                username,
                newPassword
            }),
            contentType: 'application/json',
            success: function (data) {
                btnSubmit.html('Reset Now');
                Swal.fire({
                    icon: 'success',
                    title: 'Reset password successfully',
                    showConfirmButton: true,
                    showCancelButton: false,
                    confirmButtonText: 'OK',
                    timer: 1500
                }).then(result => {
                    if (result.isConfirmed) {
                        logout();
                    }
                })

                setTimeout(() => {
                    logout();
                }, 1500);
            },
            error: function (error) {
                btnSubmit.html('Reset Now');
                Swal.fire({
                    icon: 'error',
                    title: 'Reset password failed',
                    text: 'Please try again',
                    showConfirmButton: true,
                    showCancelButton: false,
                    confirmButtonText: 'OK',
                    timer: 1500
                })
            }
        })

    });

    function logout() {
        $.ajax({
            url: "/api/v1/auth/logout",
            type: "POST",
            headers: {
                'Authorization': 'Bearer ' + TOKEN
            },
            success: function (data) {
                console.log("data", data);
                localStorage.removeItem("token");
                localStorage.removeItem("refreshToken");
                window.location.href = "/?component=chooseLogin";
            },
            error: function (error) {
                console.log("error", error);
            }
        })
    }
})