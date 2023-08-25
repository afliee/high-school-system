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
                const { id, student, totalScore } = data;

                submitInfo.empty();

                submitInfo.html(`
                    <div class="card-title d-flex justify-content-end m-2 gap-2">
                        <button class="btn btn-primary" data-id="${id}" ${data?.file ? '' : 'disabled'}>
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
                                ${data?.file ? `<a href="${data.file}" target="_blank" class="badge badge-primary">${data.file}</a>` : "No file"}
                            </span>
                            <span>
                                ${data?.isTurnedLate ? `<span class="badge badge-danger">Late</span>` : ""}
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
                                                   placeholder="Score" id="score">
                                            <div class="form-control-icon">
                                                <span>/${totalScore}</span>
                                            </div>
                                        </div>
                            </div>
                            <div class="form-group">
                                <textarea name="comment" id="commnent" rows="5" placeholder="Comment" class="w-100 form-control"></textarea>
                            </div>    
                        </div>
                    </div>
                `);
                // animetion for show
                submitInfo.show(300);
                registerCloseCollapseEvent();
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
})