$(document).ready(function () {
    let newPassword = $('#newPassword');
    let newPasswordConfirm = $('#newPasswordConfirm');
    let error = $('.error');
    let submit = $('#submit');

    newPasswordConfirm.on('keyup', function () {
        if (newPassword.val() !== newPasswordConfirm.val()) {
            error.css('display', 'block');
            submit.prop('disabled', true);
            $('.icon-password-confirm').css('color', 'red');
            $('.bi-check2:last-child').hide();
        } else {
            error.css('display', 'none');
            submit.prop('disabled', false);
            $('.icon-password-confirm').hide();
            $('.bi-check2:last-child').show().css('color', 'green');

        }
    })

    let testResult;
    let regex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*_])(?=.{6,}$)");

    $('.error').hide();
    $('.bi-check2').hide();


    newPassword.on('change', function () {
        console.log($(this).val());
        testResult = regex.test($('.password-field').val());
        console.log(testResult);
        if (testResult) {
            $('.password-field').css('border-color', 'green');
            $('.error').hide();
            $('.bi-check2:first').show().css('color', 'green');
            $('.icon-password').hide()
        }
        else {
            $('.error').show().css('color', 'red');
            $('.password-field').css('border-color', 'red');
            $('.bi-check2:first').hide();
            $('.icon-password').show().css('color', 'red');
        }
    })
});