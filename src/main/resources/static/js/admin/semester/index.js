$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const btnSave = $(".btn-save");
    const semesters = $(".semesters")

    const txtSemesterName = $("#semesterName");
    const startDate = $("#startDate");
    const endDate = $("#endDate");

    btnSave.on('click', function () {
        const startDateValue = startDate.val();
        const endDateValue = endDate.val();
        const semesterName  = txtSemesterName.val();

        const data = {
            name: semesterName,
            startDate: startDateValue,
            endDate: endDateValue
        }
        console.log("data", data)
        let swal = Swal.mixin({
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000
        })

        $.ajax({
            url: '/api/v1/semester/add',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify(data),
            beforeSend: function () {
                btnSave.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`);
            //     open alert loading
                swal = Swal.fire({
                    title: 'Loading...',
                    html: 'Please wait a second',
                    didOpen: () => {
                        Swal.showLoading()
                    }
                })
            },
            success: function (data) {
                btnSave.html(`Save`);
                swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Add semester successfully!',
                    text: 'You can add more semester or close this dialog',
                    showConfirmButton: false,
                    timer: 1500
                })
                setTimeout(() => {
                    window.location.href = "/auth/admin/semester";
                }, 1500);
            },
            error: function (data) {
                console.log(data.responseJSON.message)
                btnSave.html(`Save`);
                swal.fire({
                    icon: 'error',
                    title: 'Add semester failed!',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    })
    fetchAllSemester();
    function fetchAllSemester() {
        $.ajax({
            url: '/api/v1/semester',
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                console.log("data", data)
                if (data.length === 0) {
                    semesters.html(`
                        <div class="col-12 d-flex justify-content-center align-items-center">
                            <img src="/images/nodata.gif" class="img-fluid|img-thumbnail|rounded|rounded-circle|rounded-top|rounded-right|rounded-bottom|rounded-left" alt="">
                        </div>
                    `)
                    return;
                }
                data.forEach(semester => {
                    console.log("semester", semester)
                    const startDate = moment(new Date(semester.startDate).toLocaleDateString()).format("DD/MM/YYYY");
                    const endDate = moment(new Date(semester.endDate).toLocaleDateString()).format("DD/MM/YYYY");
                    const template = $(`
                        <div class="semsester-item pe-2 col-md-4 text-black" data-id="${semester.id}">
                            <div class="card-tag rounded border-end mt-3 shadow p-2">
                                <div class="d-flex justify-content-between">
                                <h4 class="text-title text-truncate" data-bs-toggle="tooltip" title="${semester.name}">${semester.name}</h4>
                                    <div class="dropdown">
                                        <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
                                            <i class="fas fa-ellipsis-v"></i>
                                        </button>
                                        
                                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                                            <li><a class="dropdown-item" href="/auth/admin/semester/update/${semester.id}">Update</a></li>
                                            <li><span class="dropdown-item text-danger btn-delete" data-id="${semester.id}">Delete</span></li>
                                        </ul>
                                    </div>
                                </div>
                                <p><i><strong>Start date</strong></i>: ${startDate}</p>
                                <p><i><strong>End date</strong></i>: ${endDate}</p>
                            </div>
                        </div>
                    `);
                    semesters.append(template);
                });

                registerSemesterClick();

            },
            error: function (data) {
                console.log(data.responseJSON.message)
            }
        })
    }

    function registerSemesterClick() {
        const btnDeletes = $('.btn-delete');

        btnDeletes.on('click', function () {
            console.log("click")
            const btnDelete = $(this);

            const id = btnDelete.data('id');
            let swal = null;

            $.ajax({
                url: `/api/v1/semester/delete/${id}`,
                type: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                beforeSend: function () {
                    swal = Swal.fire({
                        title: 'Loading...',
                        html: 'Please wait a second',
                        didOpen: () => {
                            Swal.showLoading()
                        }
                    })
                },
                success: function (data) {
                    swal.close();
                    Swal.fire({
                        icon: 'success',
                        title: 'Delete semester successfully!',
                        text: 'You can add more semester or close this dialog',
                        showConfirmButton: false,
                        timer: 1500
                    })
                    setTimeout(() => {
                        window.location.href = "/auth/admin/semester";
                    }, 1500);
                },
                error: function (err) {
                    console.log(err.responseText)
                    swal.close();

                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong!',
                        showConfirmButton: true,
                        confirmButtonText: 'Try again'
                    })
                }
            })
        })
    }
})
