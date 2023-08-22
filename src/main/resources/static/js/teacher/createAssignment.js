$(document).ready(function (e) {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const TEACHER_ID = $("#teacher_id").val();
    const SUBJECT_ID = $("#subject_id").val();

    $("#no_due").change(function () {
        if (this.checked) {
            $("#due").attr("disabled", true);
        } else {
            $("#due").attr("disabled", false);
        }
    });

    const btnReview = $(".review_btn");
    btnReview.on('click', function (e) {
        e.preventDefault();

        //     show all content in step 1 and 2
        const tab1 = $("#step_1 .col-12:not(.buttons)");
        const tab2 = $("#step_2 .col-12:not(.buttons)");

        const tab3 = $("#step_3");

        tab3.empty();
        //     tab1 and tab2 is array content
        tab1.each((index, element) => tab3.append($(element).clone()));
        tab2.each((index, element) => tab3.append($(element).clone()));

        tab3.append(`
                <div class="col-12 d-flex justify-content-end gap-3 buttons">
                    <button
                            class="btn btn-outline-secondary"
                            id="prev_btn"
                            type="button"
                            onclick="nextPrev(1)"
                    >
                        Previous
                    </button>

                    <button
                            class="btn btn-primary submit_btn"
                            type="submit"
                    >
                        Submit
                    </button>
                </div>
            `);

        registerSubmit();
    });

    const registerSubmit = () => {
        const btnSubmit = $(".submit_btn");
        btnSubmit.on('click', function (e) {
            e.preventDefault();

            let swal = null;

            Swal.fire({
                title: 'Are you sure?',
                text: 'You will not be able to edit this assignment after submitting',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, submit it!',
                cancelButtonText: 'No, cancel!',
            }).then(result => {
                if (result.isConfirmed) {
                    const formData = new FormData();
                    const attachment = $("#attachment")[0].files[0];
                    const title = $("#title").val();
                    const description = $("#description").val();
                    const due = $("#due").val();
                    const noDue = $("#no_due").is(":checked");
                    const points = $("#points").val();
                    const dueDate = noDue ? null : due;

                    formData.append("title", title);
                    formData.append("description", description);
                    formData.append("points", points);

                    if (attachment) {
                        formData.append("attachment", attachment);
                    }

                    formData.append("isDue", noDue ? "false" : "true");
                    // statedData to format yyyy-MM-dd HH:mm:ss
                    formData.append("startedDate", new Date().toISOString().slice(0, 19).replace('T', ' '));
                    if (!noDue) {
                        formData.append("closedDate", `${dueDate.replace("T", " ")}:00`);
                    }
                    formData.append("subjectId", SUBJECT_ID);
                    formData.append("teacherId", TEACHER_ID);

                    console.log(formData)
                    $.ajax({
                        url: '/api/v1/assignment/create',
                        type: 'POST',
                        headers: {
                            "Authorization": `Bearer ${TOKEN}`
                        },
                        data: formData,
                        processData: false,
                        contentType: false,
                        enctype: 'multipart/form-data',
                        beforeSend: function () {
                            swal = Swal.fire({
                                title: 'Please wait',
                                html: 'Creating assignment',
                                didOpen: () => {
                                    Swal.showLoading();
                                }
                            });
                        },
                        success: function (data) {
                            swal.close();
                            Swal.fire({
                                title: 'Success',
                                text: 'Assignment created successfully',
                                icon: 'success',
                                showCancelButton: false,
                                confirmButtonText: 'OK',
                            }).then(result => {
                                if (result.isConfirmed) {
                                    window.location.href = `/teacher/enroll/${SUBJECT_ID}`;
                                }
                            });
                        },
                        error: function (err) {
                            console.log(err.responseText)
                            swal.close();
                            Swal.fire({
                                title: 'Error',
                                text: 'Something went wrong',
                                icon: 'error',
                                showCancelButton: false,
                                confirmButtonText: 'OK',
                            });
                        }
                    })
                }
            })
        });
    }
})