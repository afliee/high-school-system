$(document).ready(function () {
    const forgotForm = $("#forgotForm");
    const btnSubmit = $("#btnSubmitForgot");
    const alertError = $("#alertError");
    const alertSuccess = $("#alertSuccess");

    const code = $(".reset-code");
    const newPassword = $(".new-password");

    btnSubmit.on('click', function (e) {
        e.preventDefault();

        const username = $("#username").val();

        $.ajax({
            url: "/api/v1/auth/forgot-password",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                username: username
            }),
            success: function (response) {
                const successMessage = $("#success");
                successMessage.text("Please check your email to get code");
                alertSuccess.removeClass("d-none");
                code.removeClass("d-none");
                newPassword.removeClass("d-none");
                btnSubmit.text("Submit");
                btnSubmit.removeAttr("id");
                btnSubmit.attr("id", "btnConfirmForgot");
                addEventListenerConfirmForgot();
            },
            error: function (response) {
                console.log(response)
                const errorMessage = $("#error");
                errorMessage.text(response.responseJSON?.message);
                alertError.removeClass("d-none");
            }
        })
    })

    function addEventListenerConfirmForgot() {
        const btnConfirmForgot = $("#btnConfirmForgot");
        btnConfirmForgot.on('click', function (e) {
            e.preventDefault();
            const username = $("#username").val();
            const code = $("#code").val();
            const newPassword = $("#newPassword").val();

            $.ajax({
                url: "/api/v1/auth/forgot-password/confirm",
                type: "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    username: username,
                    code: code,
                    newPassword: newPassword
                }),
                success: function (response) {
                    const successMessage = $("#success");
                    successMessage.text("Change password successfully");
                    alertSuccess.removeClass("d-none");
                    setTimeout(function () {
                        window.location.href = "/";
                    }, 2000);
                },
                error: function (response) {
                    const errorMessage = $("#error");
                    errorMessage.text(response.responseJSON.message);
                    alertError.removeClass("d-none");
                }
            });
        });
    }
})