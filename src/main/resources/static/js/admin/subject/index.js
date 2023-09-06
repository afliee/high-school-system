$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const MAX_SUBJECTS_PER_PAGE = 9;

    const subjectsTemplate = $("#subjects");
    const paginationTemplate = $("#pagination");
    const selectDepartment = $(".select_department");
    const searchModal = $("#advancedSearch");

//     ========== CALL FUNCTION ==========
    getSubjects({});
    getAllOptionDepartment({});
    registerSearchEvent();

//     ========== FUNCTION ==========
    function getSubjects(
        {
            page = 0,
            size = 9,
            sort = "ASC",
            sortBy = "id",
            filter = ""
        }
    ) {
        // ?page=0&size=10&sort=ASC&sortBy=id&filter=name:chemical;teacherName:HuynhTruong;departmentId:0
        $.ajax({
            url: `/api/v1/subject/all?page=${page}&size=${size}&sort=${sort}&sortBy=${sortBy}&filter=${filter}`,
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                renderSubjects(data);
            },
            error: function (e) {
                console.log(e.responseJSON.message);
                Toastify({
                    text: "Error get subjects",
                    color: "#FF0000",
                    backgroundColor: "linear-gradient(to right, #FF0000, #FF5A5A)",
                    position: "right",
                    gravity: "top",
                }).showToast();
            }
        })
    }

    function getAllOptionDepartment(
        {
            page = 0,
            size = 5,
            sort = "name"
        }
    ) {
        $.ajax({
            url: `/api/v1/department/all?page=${page}&size=${size}&sortBy=${sort}`,
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                $(".card-loaders").removeClass('d-none');
            },
            success: function (data) {
                renderDepartmentOptions(data);
            },
            error: function (e) {
                console.log(e.responseJSON.message);
                Toastify({
                    text: "Error get departments",
                    color: "#FF0000",
                    backgroundColor: "linear-gradient(to right, #FF0000, #FF5A5A)",
                    position: "right",
                    gravity: "top",
                }).showToast();
            }
        })
    }

    function renderSubjects(data) {
        const {content: subjects, totalPages, number: currentPage} = data;
        if (subjects.length === 0) {
            let html = `
                <div class="d-flex justify-content-center align-items-center">
                    <img src="/images/nodata.gif" class="img-fluid|img-thumbnail|rounded|rounded-circle|rounded-top|rounded-right|rounded-bottom|rounded-left" alt="">
                </div>
            `;

            subjectsTemplate.html(html);
            paginationTemplate.hide();
            return;
        }
        let html = subjects.map(function (subject) {
            // convert date timestamp to date string dd-MM-yyyy
            const createdAt = moment(subject.createdAt).format("DD-MM-yyyy");

            return `
                <div class="col-md-4 col-sm-6 col-xs-12 my-3" >
                    <div class="card subject" style="border-right-color: ${subject.color}" data-id="${subject.id}">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <h3 class="name text-title text-truncate" data-bs-toggle="tooltip" title="${subject.name}">${subject.name}</h3>
                                <button 
                                    class="btn btn-danger btn-sm btn-delete d-inline-block"
                                    data-id="${subject.id}"
                                >&times;</button>
                            </div>
                             <div class="info my-3 d-flex flex-column gap-2">
                                <span class="text-truncate" data-bs-toggle="tooltip" title="${subject.teacher.name}"><i><span class="fw-semibold">Teached By</span>: ${subject.teacher.fullName}</i></span>
                                <span><i><span class="fw-semibold">Created at</span></i>: ${createdAt}</span>
                            </div>
                            <i><h5 class="text-truncate text-muted" data-bs-toggle="tooltip" title="${subject.department}"><span class="fw-semibold">Belong to</span>: ${subject.department} Department</h5></i>
                        </div>
                    </div>
                </div>
            `;
        })

        $('.card-loaders').fadeOut(300, function () {
            $(this).addClass('d-none');
        });
        subjectsTemplate.fadeOut(300, function () {
            subjectsTemplate.html(html);
            registerDeleteSubjectEvent();
            registerSubjectClicked();
            subjectsTemplate.fadeIn(150);
        });

        paginationTemplate.fadeOut(300, function () {
            const paginationHtml = generatePaginationOptions(currentPage + 1, totalPages, MAX_SUBJECTS_PER_PAGE);
            paginationTemplate.html(paginationHtml);
            registerPaginationEvent();
            paginationTemplate.fadeIn(150);
        })

    }

    function renderDepartmentOptions({content: departments, totalPages, number: currentPage}) {
        // clear all option except default option
        selectDepartment.find("option").not(":first").remove();
        departments.forEach(function (department) {
            const option = $(`<option value="${department.id}">${department.name}</option>`);
            selectDepartment.append(option);
        });

        //     if currentPage < totalPages - 1 then append next Option
        if (currentPage < totalPages - 1) {
            const nextOption = $(`<option class="bg-primary btn-next" value="${'next:' + (currentPage + 1)}">Next</option>`);
            selectDepartment.append(nextOption);
        }

        //     if currentPage > 0 then append previous Option
        if (currentPage > 0) {
            const previousOption = $(`<option value="${'prev:' + (currentPage - 1)}" class="btn-pre">Previous</option>`);
            selectDepartment.append(previousOption);
        }
        registerSelectDepartmentEvent();
    }

//     ========== EVENT ==========

    function registerSelectDepartmentEvent() {
        const jsSelect2 = $(".js-select2.select_department");

        jsSelect2.off("change"); // clear previous event register

        jsSelect2.on("change", function () {
            const selected_val = $(this).val();
            if (selected_val.startsWith("next")) {
                console.log("next department");
                const page = selected_val.split(":")[1];
                getAllOptionDepartment({page: page});
            } else if (selected_val.startsWith("prev")) {
                console.log("prev department");
                const page = selected_val.split(":")[1];
                getAllOptionDepartment({page: page});
            } else {
                getSubjects({filter: `departmentId:${selected_val}`});
            }
        });
    }

    function registerSearchEvent() {
        const btnSearch = searchModal.find('.btn-search');
        btnSearch.on('click', function (e) {
            const subjectName = searchModal.find('#name');
            const teacherName = searchModal.find('#teacherName');

            let filter = '';
            if (subjectName.val() !== '') {
                filter += `name:${subjectName.val()};`
            }

            if (teacherName.val() !== '') {
                filter += `teacherName:${teacherName.val()};`
            }
            console.log("filter: " + filter);
            filter ? getSubjects({filter: filter}) : getSubjects({});
            searchModal.modal('hide');

            //     clear search value
            subjectName.val('');
            teacherName.val('');
        })
    }

    function registerSubjectClicked() {
        const subjects = subjectsTemplate.find('.subject');

        subjects.on('click', function (e) {
            const subjectId = $(this).data('id');
            e.preventDefault();
            e.stopPropagation();
            if ($(e.target).hasClass('btn-delete')) {
                return;
            }

            window.location.href = `/auth/admin/subjects/${subjectId}`;
        })
    }

    function registerPaginationEvent() {
        const pagination = paginationTemplate.find('.page-item');
        pagination.on('click', function (e) {
            const page = $(this).data('page');

            if (page) {
                getSubjects({page: page - 1});
            }
        })
    }

    function registerDeleteSubjectEvent() {
        const btnDelete = subjectsTemplate.find('.subject .btn-delete');

        btnDelete.on('click', function (e) {
            const id = $(this).data('id');
            let swal = null;
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton:true,
                showConfirmButton:true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then(result => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: `/api/v1/subject/delete/${id}`,
                        type: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${TOKEN}`
                        },
                        beforeSend: function () {
                            swal = Swal.fire({
                                title: 'Deleting...',
                                didOpen: () => {
                                    Swal.showLoading()
                                }
                            });
                        },
                        success: function (data) {
                            console.log(data);
                            swal.close();
                            Swal.fire({
                                title: 'Deleted!',
                                text: 'Subject has been deleted.',
                                icon: 'success',
                                showConfirmButton: false,
                                timer: 1500
                            }).then(() => {
                                window.location.reload();
                            })
                        },
                        error: function (e) {
                            swal.close();
                            console.log(e.responseText);
                            Swal.fire({
                                title: 'Error!',
                                text: 'Subject has not been deleted.',
                                icon: 'error',
                                showConfirmButton: false,
                                timer: 1500
                            })
                        }
                    })
                }
            })
        });
    }
})