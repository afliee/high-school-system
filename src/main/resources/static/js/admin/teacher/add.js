$(document).ready(function () {
    const btnAdd = $(".btn-add");
    const btnUpdate = $(".btn-update");

    const filter = $("#filter").val();
    const page = $("#page").val();
    const token = localStorage.getItem('token');
    const content = $(".content");
    const MAX_PAGE = 10;
    fetchAll();

    // add teacher to system
    const btnAddTeacher = $("#btn-add-teacher");
    btnAddTeacher.on('click', function (e) {
        e.preventDefault();
        const username = $("#username").val();
        const fullName = $("#fullName").val();
        const email = $("#email").val();
        const password = $("#password").val();
        console.log($("#email"))
        const data = {
            username, fullName, email, password
        }

        console.log("data", data)

        $.ajax({
            headers: {
                'Authorization': `Bearer ${token}`
            },
            url: '/api/v1/admin/teacher',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data) {
                console.log(data);
                fetchAll();
                Toastify({
                    text: "Add teacher successfully",
                    backgroundColor: "#0DB473",
                    className: "info",
                    duration: 3000
                }).showToast();
            },
            error: function (error) {
                console.log(error);
            }
        })
    })

    btnUpdate.on('click', function (e) {
        e.preventDefault();
        const cardBody = $(".teacher-info");
        const id = cardBody.find("#__STATEMENT").val();
        const name = cardBody.find(".username").text();
        const fullName = cardBody.find(".fullname").text();
        const phone = cardBody.find("#phone").val();
        const cardId = cardBody.find("#cardId").val();
        const birthday = cardBody.find("#birthday").val();
        const salary = cardBody.find("#salary").val();
        const email = cardBody.find("#mail").val();
        const address = cardBody.find("#address").val();

        const data = {
            id, name, fullName, phone, cardId, birthday, salary, email, address
        };

        console.log(data)
        $.ajax({
            url: `/api/v1/teacher/${id}`,
            type: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (data) {
                console.log(data);
                Toastify({
                    text: "Update teacher successfully",
                    backgroundColor: "#0DB473",
                    className: "info",
                    duration: 3000
                }).showToast();
                fetchAll();
            },
            error: function (error) {
                console.log(error);
            }
        })

    })

    // get all teacher in system
    function fetchAll() {
        $.ajax({
            headers: {
                'Authorization': `Bearer ${token}`
            },
            url: `/api/v1/admin/members?filter=${filter}&page=${page}`,
            type: 'GET',
            success: function (data) {
                console.log(data)
                if (!data.content.length) {
                    content.text('No data');
                }
                const firstTeacher = data.content[0];
                getTeacherById(firstTeacher.id);
                let html = '';

                data.content.forEach((item, index) => {
                    // format date
                    const dayConverted = new Date(item.createdDate).toLocaleDateString('en-GB');
                    html += `
                        <tr class='teacher' data-id='${item.id}'>
                            <td>${item.name}</td>
                            <td>${item.fullName}</td>
                            <td>${item.email}</td>
                            <td>${dayConverted}</td>
                            <td>
                                <button class="btn btn-danger btn-delete" data-id="${item.id}">Delete</button>
                            </td>
                        </tr>
                    `
                })
                // create pagination
                let pagination = '';
                const totalPages = data.totalPages;
                const currentPage = data.number + 1;
                const prevPage = currentPage - 1;
                const nextPage = currentPage + 1;

                pagination += `
                    <li class="page-item">
                        <a class="page-link" href="/auth/admin/members?page=${prevPage}&filter=${filter}">&lt;</a>
                    </li>
                `;
                console.log({
                    totalPages,
                    currentPage,
                    prevPage,
                    nextPage
                })
                for (let i = 0; i < MAX_PAGE && i < totalPages; i++) {
                    pagination += `
                        <li class="page-item ${currentPage === i + 1 ? 'active' : ''}">
                            <a class="page-link" href="/auth/admin/members?page=${i + 1}&filter=${filter}">${i + 1}</a>
                        </li>
                    `
                }

                pagination += `
                    <li class="page-item">
                        <a class="page-link" href="/auth/admin/members?page=${nextPage}&filter=${filter}">&gt;</a>
                    </li>
                `;
                $(".pagination").html(pagination);
                content.html(html);

                //     add event to each row
                const teachers = $(".teacher");
                teachers.each(function (index, teacher) {
                    $(teacher).on('click', function (e) {
                        e.preventDefault();
                        const id = $(this).data('id');
                        getTeacherById(id);
                    })
                })

                const btnDeletes = $(".btn-delete");
                btnDeletes.each(function (index, btnDelete) {
                    $(btnDelete).on('click', function (e) {
                        e.preventDefault();
                        const id = $(this).data('id');
                        deleteTeacher(id);
                    })
                });
            },
            error: function (data) {
                console.log(data)
            }
        })
    }

    // register perfect scrollbar
    if (typeof PerfectScrollbar === 'function') {
        new PerfectScrollbar('.table-responsive', {
            wheelPropagation: false
        });
    }

    function getTeacherById(id) {
        $.ajax({
            url: `/api/v1/teacher/${id}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            success: function (teacher) {
                const cardBody = $(".teacher-info");
                const fullName = cardBody.find(".fullname");
                const username = cardBody.find(".username");
                const avatar = cardBody.find(".avatar");
                const phone = cardBody.find("#phone");
                const cardId = cardBody.find("#cardId");
                const birthday = cardBody.find("#birthday");
                const salary = cardBody.find("#salary");
                const email = cardBody.find("#mail");
                const address = cardBody.find("#address");

                const __STATEMENT = cardBody.find("#__STATEMENT");

                fullName.text(teacher.fullName);
                username.text(`#${teacher.name}`);
                avatar.attr('src', teacher.avatar || 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2-bg.webp');
                phone.val(teacher.phone || '');
                cardId.val(teacher.cardId || '');
                birthday.val(new Date(teacher.birthday).toLocaleDateString('en-GB') || '');
                salary.val(teacher.salary || '');
                email.val(teacher.email || '');
                address.val(teacher.address || '');
                __STATEMENT.val(id);
                console.log(teacher);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    function deleteTeacher(id) {
        $.ajax({
            url: `/api/v1/teacher/${id}`,
            type: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            success: function (data) {
                Toastify({
                    text: data,
                    duration: 3000,
                    backgroundColor: "#0DB473",
                    className: "info"
                }).showToast();
                fetchAll();
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
})