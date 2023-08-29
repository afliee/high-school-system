$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const CLASS_ID = $("#class_id").val();

    const btnSubmit = $("#submit");

    btnSubmit.click(function () {
        const studentList = $('input[name="attendance"]:checked');
        const studentIds = [];
        studentList.each(function () {
            studentIds.push($(this).val());
        });

        const data = {
            classId: CLASS_ID,
            studentIds: studentIds
        };

        $.ajax({
            url: `/api/v1/attendance/submit`,
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                btnSubmit.prop('disabled', true);
            //     add loading icon
                btnSubmit.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`);
            },
            data: JSON.stringify(data),
            success: function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text:   'Submit attendance successfully!',
                    timer: 2000,
                });
                setTimeout(function () {
                    location.href = "/attendance";
                }, 2000);

                // location.href = "/attendance";
            },
            error: function (response) {
                btnSubmit.prop('disabled', false);
                btnSubmit.html(`Submit`);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text:   'Something went wrong!',
                    timer: 2000,
                })
            }
        })
    })
})