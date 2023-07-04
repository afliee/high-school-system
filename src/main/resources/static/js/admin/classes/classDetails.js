$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const CLASS_ID = $("#classId").val();

    fetchClassDetails(CLASS_ID, 0 , renderClassDetails);


    function fetchClassDetails(classId, page,  callback) {
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
                                <div class="col-6">
                                    <p>${res.id}</p>
                                    <p>${res.chairman}</p>
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

        const students = $('.students');
        html = '';


        res.studentPage.content.forEach(function (student) {
            html += `
                <tr>
                    <td>${student.id}</td>
                    <td>${student.name}</td>
                    <td>${student.fullName}</td>
                    <td>${student.email}</td>
                    <td>
                        <button type="button" class="btn btn-primary btn-show" data-id="${student.id}" >View</button>
                    </td>
                    <td>
                        <button class="btn btn-danger btn-delete" data-id="${student.id}">Delete</button>
                    </td>
                </tr>
            `
        })
        students.html(html);
        registerShowStudentEvent();


        let pagination = '';
        const { totalPages, pageable } = res.studentPage;
        const currentPage = pageable.pageNumber;
        const prevPage = currentPage - 1 < 0 ? 0 : currentPage - 1;
        const nextPage = currentPage + 1 > totalPages ? totalPages : currentPage + 1;
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

                modal.modal('show')

                const btnUpdate = modal.find('.btn-update');

                btnUpdate.click(function () {
                    const phoneValue = phone.val();
                    const cardIdValue = cardId.val();
                    const emailValue = email.val();
                    const addressValue = address.val();

                //     get image file from input file
                    const imageFile = avatarInput[0].files[0];
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
})