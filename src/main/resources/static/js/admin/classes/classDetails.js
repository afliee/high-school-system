$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const CLASS_ID = $("#classId").val();

    const btnDelete = $(".btn-delete");

    fetchClassDetails(CLASS_ID, 0, renderClassDetails);
    registerUpdateStudentEvent();

    btnDelete.on("click", function () {
        Swal.fire({
            title: 'Are you sure?',
            text: "You want to delete this class?\n All students in this class will be deleted!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#32CD32',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Yes, delete it!'
        }).then(result => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `/api/v1/class/delete?classId=${CLASS_ID}`,
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${TOKEN}`
                    },
                    success: function (res) {
                        Swal.fire({
                            title: 'Deleted!',
                            text: 'Your class has been deleted.',
                            icon: 'success',
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: 'OK',
                            timer: 1500
                        })

                        setTimeout(function () {
                            window.location.href = "/auth/admin/classes";
                        }, 1500)
                    }
                })
            }
        })
    })

    function fetchClassDetails(classId, page, callback) {
        $.ajax({
            url: `/api/v1/class/get/${classId}?page=${page}`,
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                callback && callback(res);
            },
            error: function (err) {
                console.log(err);
            }
        })
    }

    function renderClassDetails(res) {
        console.log(res);
        const info = $('.info');
        let html = `
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        ${res.name}
                    </div>
                    <div class="card-body">
                        <div class="card-text">
                            <div class="row">
                                <div class="col-6">
                                    <p class="fw-bold">Class Id</p>
                                    <p class="fw-bold">Chairman</p>
                                    <p class="fw-bold">Student Present</p>
                                    <p class="fw-bold">Created Date</p>
                                </div>
                                <div class="col-6" class="class-info" data-id="${res.id}">
                                    <p>${res.id}</p>
                                    <p>
                                        ${res.chairman}
                                         <span>
                                            <button class="btn btn-outline-primary btn-add-chairman" data-bs-toggle="tooltip" title="Change Chairman"><i class="bi bi-plus-lg"></i></button>
                                        </span>
                                    </p>
                                    <p>${res.students.length}</p>
                                    <p>${new Date(res.createdDate).toLocaleDateString('en-GB')}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        info.html(html);
        registerAddChairmanEvent();
        const students = $('.students');
        html = '';


        res.studentPage.content.forEach(function (student) {
            html += `
                <tr>
                    <td>${student.id.substring(0, 10)}...</td>
                    <td>${student.name}</td>
                    <td>${student.fullName}</td>
                    <td>${student.email}</td>
                    <td>
                        <button type="button" class="btn btn-primary btn-show" data-id="${student.id}" >View</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-delete-student" data-id="${student.id}" data-class-id="${res.id}">Delete</button>
                    </td>
                </tr>
            `
        })
        students.html(html);
        registerShowStudentEvent();
        registerDeleteStudentEvent();

        let pagination = '';
        const {totalPages, pageable} = res.studentPage;
        const currentPage = pageable.pageNumber;
        const prevPage = currentPage - 1 < 0 ? 0 : currentPage - 1;
        const nextPage = currentPage + 1 > totalPages ? totalPages : currentPage + 1;
        localStorage.setItem('page', currentPage);
        console.log("prevPage", prevPage)
        console.log("nextPage", nextPage)
        pagination += `
            <li class="page-item">
                <a class="page-link" href="#">&lt;</a>
            </li>    
        `

        for (let i = 0; i < totalPages; i++) {
            pagination += `
                <li class="page-item ${currentPage === i ? 'active' : ''} ">
                    <a class="page-link" href="#">${i + 1}</a>
                </li>
            `
        }

        pagination += `
            <li class="page-item">
                <a class="page-link" href="#">&gt;</a>
            </li>
        `

        $('.pagination').html(pagination);

        registerEventPagination(prevPage, nextPage);

    }

    function registerEventPagination(prevPage, nextPage) {
        console.log(prevPage, nextPage)
        const pageLinks = $('.page-link');
        pageLinks.click(function (e) {
            e.preventDefault();
            const page = $(this).text();

            if (page === '<') {
                fetchClassDetails(CLASS_ID, prevPage, renderClassDetails);
            } else if (page === '>') {
                fetchClassDetails(CLASS_ID, nextPage, renderClassDetails);
            } else {
                fetchClassDetails(CLASS_ID, page - 1, renderClassDetails);
            }
        })
    }

    function registerShowStudentEvent() {
        const btnShows = $('.btn-show');
        const modal = $('#studentDetail');
        console.log(modal)
        btnShows.click(function () {
            const id = $(this).data('id');
            console.log(id)

            fetchStudentDetails(id, function (res) {
                console.log("student", res)
                const avatar = modal.find('.avatar');
                const avatarInput = modal.find('#avatar');

                const phone = modal.find('#phone');
                const cardId = modal.find('#cardId');
                const email = modal.find('#mail')
                const enterDate = modal.find('#enterDate');
                const address = modal.find('#address');
                const statement = modal.find('#__STATEMENT');

                const fullName = modal.find('.fullname');
                const username = modal.find('.username');


                fullName.text(res.fullName);
                username.text(res.name);

                avatar.attr('src', res.avatar ? res.avatar : '/images/1.jpg');

                phone.val(res.phone);
                cardId.val(res.cardId);
                email.val(res.email);
                enterDate.val(new Date(res.enterDate).toLocaleDateString('en-GB'));
                address.val(res.location);
                statement.val(id);

                modal.modal('show')

                const btnUpdate = modal.find('.btn-update');
                console.log("button update", btnUpdate.length)

                avatarInput.change(function () {
                    const imageFile = avatarInput[0].files[0];
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        avatar.attr('src', e.target.result);
                    }
                    reader.readAsDataURL(imageFile);
                })
            })
        })
    }

    function registerUpdateStudentEvent() {
        const modal = $('#studentDetail');
        const btnUpdate = modal.find('.btn-update');
        btnUpdate.click(function () {
            const avatarInput = modal.find('#avatar');
            const phone = modal.find('#phone');
            const cardId = modal.find('#cardId');
            const email = modal.find('#mail')
            const address = modal.find('#address');
            const statement = modal.find('#__STATEMENT');

            const id = statement.val();
            const phoneValue = phone.val();
            const cardIdValue = cardId.val();
            const emailValue = email.val();
            const addressValue = address.val();
            console.log("avatar input: ", avatarInput[0].files[0])
            //     get image file from input file
            const imageFile = avatarInput[0].files[0];
            // clear data form input file


            const data = new FormData();
            data.append('phone', phoneValue);
            data.append('cardId', cardIdValue);
            data.append('email', emailValue);
            data.append('address', addressValue);
            data.append('avatar', imageFile);
            $.ajax({
                url: `/api/v1/student/${id}`,
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                enctype: 'multipart/form-data',
                data: data,
                cache: false,
                processData: false,
                contentType: false,
                success: function (res) {
                    console.log(res);
                    // animate hide modal
                    modal.fadeOut(500, function () {
                        modal.modal('hide');
                    })
                },
                error: function (err) {
                    console.log(err);
                }
            })
        })
    }

    function registerDeleteStudentEvent() {
        const btnDeletes = $('.btn-delete-student');
        btnDeletes.click(function () {
            const studentId = $(this).data('id');
            const classId = $(this).data('class-id');
            console.log("student Id Delete", studentId)


            $.ajax({
                url: `/api/v1/class/delete/${classId}?studentId=${studentId}`,
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                success: function (res) {
                    // get page form url
                    fetchClassDetails(CLASS_ID, localStorage.getItem('page') || 1, renderClassDetails);
                    Toastify({
                        text: "Delete student successfully",
                        duration: 3000,
                        gravity: "top",
                        position: 'right',
                        backgroundColor: "#32CD32",
                        stopOnFocus: true,
                    })

                    fetchClassDetails(CLASS_ID, localStorage.getItem('page') || 1, renderClassDetails);
                },
                error: function (err) {
                    console.log(err);
                }
            })
        });
    }


    function fetchStudentDetails(id, callback) {
        $.ajax({
            url: `/api/v1/student/${id}`,
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                callback && callback(res);
            },
            error: function (err) {
                console.log(err);
            }
        })
    }

    function registerAddChairmanEvent() {
        const btnAddChairman = $('.btn-add-chairman');
        const chairmanModal = $('#addChairman');

        btnAddChairman.click(function () {
            //     call get all student
            $.ajax({
                url: `/api/v1/teacher/all`,
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                success: function (res) {
                    console.log(res)
                    const chairmans = chairmanModal.find('.chairmans');

                    chairmans.empty();
                    const select = $('<select class="form-control" id="chairman"></select>');

                    const defaultOption = $(`<option value="">Select chairman</option>`);
                    select.append(defaultOption);

                    res.forEach(function (chairman) {
                        const option = $(`<option value="${chairman.id}">${chairman.fullName}</option>`);
                        select.append(option);
                    })

                    chairmans.append(select);
                    chairmanModal.modal('show');
                }
            });
        });

        const btnSubmit = chairmanModal.find('.btn-submit');
        const searchInput = chairmanModal.find('#search');
        searchInput.keyup(function () {
            const value = $(this).val();
            console.log("value", value)

            $.ajax({
                url: `/api/v1/teacher/search?q=${value}`,
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                success: function (res) {
                    console.log(res)
                    renderChairmans(chairmanModal, res)
                },
                error: function (err) {
                    console.log(err)
                }
            })
        });

        btnSubmit.click(function () {
            const optionSelected = chairmanModal.find('#chairman option:selected');
            const chairmanId = optionSelected.val();
            console.log("chairman id", chairmanId)

            $.ajax({
                url: '/api/v1/class/set-chairman',
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                data: {
                    classId: CLASS_ID,
                    teacherId: chairmanId
                },
                success: function (res) {
                    console.log(res)
                    chairmanModal.modal('hide');
                    Toastify({
                        text: "Add chairman successfully",
                        duration: 3000,
                        gravity: "top",
                        position: 'right',
                        backgroundColor: "#32CD32",
                        stopOnFocus: true,
                    }).showToast();
                    fetchClassDetails(CLASS_ID, localStorage.getItem('page') || 1, renderClassDetails);
                },
                error: function (err) {
                    console.log(err);
                    const message = chairmanModal.find('#message');
                    message.text(err.responseJSON.message);
                    message.addClass('text-danger');
                    Toastify({
                        text: err.responseJSON.message,
                        duration: 3000,
                        gravity: "top",
                        position: 'right',
                        backgroundColor: "#FF0000",
                        stopOnFocus: true,
                    }).showToast();
                }
            })
        });

        function renderChairmans(chairManModal, res) {
            const chairmans = chairmanModal.find('.chairmans');

            chairmans.empty();
            const select = $('<select class="form-control" id="chairman"></select>');

            const defaultOption = $(`<option value="">Select chairman</option>`);
            select.append(defaultOption);

            res.forEach(function (chairman) {
                const option = $(`<option value="${chairman.id}">${chairman.fullName}</option>`);
                select.append(option);
            })

            chairmans.append(select);
        }
    }
})