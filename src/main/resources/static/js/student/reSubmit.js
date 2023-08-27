$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const STUDENT_ID = $("#id").val();
    const SUBJECT_ID = $("#subject_id").val();

    const btnSubmit = $(".btn-re-submit");

    btnSubmit.on('click', function () {
        const id = $(this).data("id");

        const formData = new FormData();
        const file = $(`#file_${id}`)[0].files[0];

        formData.append("file", file);
        formData.append("studentId", STUDENT_ID);

        $.ajax({
            url: `/api/v1/assignment/re-submit/${id}`,
            type: "POST",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function (xhr) {
                //     add loading spinner
                btnSubmit.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`);
            },
            success: function (response) {
                btnSubmit.html(`Re-Submit`);
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Submit assignment successfully',
                    showConfirmButton: false,
                    timer: 1500
                })

                setTimeout(function () {
                    window.location.href = `/student/subject/${SUBJECT_ID}`;
                });
            },
            error: function (err) {
                console.log(err.responseText);
                btnSubmit.html(`Re-Submit`);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Submit assignment failed',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    })
})