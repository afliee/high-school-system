$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const submits = $(".submit");
    const submitInfo = $("#submit_info");

    submits.on('click', function (e) {
        const submit = $(this);

        const id = submit.data("id");
        console.log(id)
        submitInfo.empty();
        // add loading
        submitInfo.html(`
            <div class="d-flex justify-content-center align-items-center my-3">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>
        `);

        $.ajax({
            url: `/api/v1/submitting/${id}`,
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                console.log(data)
                const {id, student, totalScore} = data;
                let file = data?.file;
                if (file) {
                    file = file.substring(file.lastIndexOf("/") + 1);
                }
                submitInfo.empty();

                submitInfo.html(`
                    <div class="card-title d-flex justify-content-end m-2 gap-2">
                        <button class="btn btn-primary btn-return" data-id="${id}" ${data?.file ? '' : 'disabled'}>
                            <span>Return</span>
                        </button>
                        <button class="btn-close-collapse">
<!--                            close collapse -->
                            <i class="bi bi-caret-down-fill"></i>
                        </button>
                    </div>
                    <div class="card-body row">
                        <div class="content-left col-md-6 col-12">
                            <h5 class="student_fullName">${student.fullName}</h5>
                            <p class="student_name" class="text-primary">${student.name}</p>
                            <span>
                                ${data?.file ? `<a href="/uploads/assignments/${data.file}" target="_blank" class="badge badge-primary bg-primary text-white">${file}</a>` : "No file"}
                            </span>
                            <span>
                                ${data?.isTurnedLate ? `<span class="badge badge-danger ">Late</span>` : ""}
                            </span>
                        </div>
                        <div class="content-right col-md-6 col-12">
<!--                            score and comment-->
                            <div class="form-group has-icon-right">
                                        <label for="score">
                                            Score
                                        </label>
                                        <div class="position-relative mt-2 mb-3">
                                            <input type="number" class="form-control"
                                                   placeholder="Score" id="score" max="${totalScore}" min="0" required value="${data?.score ? data.score : ''}">
                                            <div class="form-control-icon">
                                                <span>/${totalScore}</span>
                                            </div>
                                        </div>
                            </div>
                            <div class="form-group">
                                <textarea name="comment" id="comment" rows="5" placeholder="Comment" class="w-100 form-control">${data?.comment ? data.comment : ''}</textarea>
                            </div>    
                        </div>
                    </div>
                `);
                // animetion for show
                submitInfo.show(300);
                registerCloseCollapseEvent();
                registerReturnEvent();
            },
            error: function (error) {
                console.log(error.responseText)
                submitInfo.empty();

                //     add alert error
                submitInfo.html(`
                    <div class="alert alert-danger" role="alert">
                        <h4 class="alert-heading">Error!</h4>
                        <p>Something went wrong!</p>
                    </div>
                `);
            }
        })
    })

    function registerCloseCollapseEvent() {
        const btnCloseCollapse = $(".btn-close-collapse");

        btnCloseCollapse.on('click', function (e) {
            submitInfo.hide(300);
        })
    }

    function registerReturnEvent() {
        const btnReturn = $(".btn-return");
        if (btnReturn.is(":disabled")) {
            return;
        }

        btnReturn.on('click', function (e) {
            const id = $(this).data("id");
            const data = {
                score: $("#score").val() * 1 || 0,
                comment: $("#comment").val()
            }
            console.log(data)
            $.ajax({
                url: `/api/v1/assignment/grade/${id}`,
                type: "POST",
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                data: JSON.stringify(data),
                contentType: "application/json",
                beforeSend: function () {
                    btnReturn.attr("disabled", true);
                //     add loading spinner

                },
                success: function (data) {
                    console.log(data)
                    btnReturn.attr("disabled", false);
                    Swal.fire({
                        icon: 'success',
                        title: 'Success',
                        text: 'Return successfully!',
                        showConfirmButton: false,
                        timer: 1500
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function (error) {
                    console.log(error.responseText)
                    btnReturn.attr("disabled", false);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Something went wrong!',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            })
        });
    }
})