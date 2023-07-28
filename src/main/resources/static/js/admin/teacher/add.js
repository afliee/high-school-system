$(document).ready(function () {
    const btnAdd = $(".btn-add");
    const btnUpdate = $(".btn-update");
    const paginationDropdown = $("#departments");

    const filter = $("#filter").val();
    const page = $("#page").val();
    const token = getCookie("token") || localStorage.getItem("token");
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
        const department = cardBody.find("#departments option:selected") || null;
        console.log(department)
        const departmentId = department ? department.val() : null;

        const data = {
            id, name, fullName, phone, cardId, birthday, salary, email, address, departmentId
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
                            <td class="text-truncate" data-bs-toggle="tooltip" title="${item.name}">${item.fullName}</td>
                            <td class="text-truncate" data-bs-toggle="tooltip" title="${item.email}">${item.email}</td>
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
                        Swal.fire({
                            title: 'Are you sure?',
                            text: "You won't be able to revert this!\nThis action will delete all data related to this teacher",
                            icon: 'warning',
                            confirmButtonColor: '#0DB473',
                            cancelButtonColor: '#d33',
                            confirmButtonText: 'Yes, delete it!',
                            showCancelButton: true,
                        }).then(result => {
                            if (result.isConfirmed) {
                                const id = $(this).data('id');
                                deleteTeacher(id);
                            }
                        });
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
                username.text(`${teacher.name}`);
                avatar.attr('src', teacher.avatar || 'https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava2-bg.webp');
                phone.val(teacher.phone || '');
                cardId.val(teacher.cardId || '');
                birthday.val(new Date(teacher.birthday).toLocaleDateString('en-GB') || '');
                salary.val(teacher.salary || '');
                email.val(teacher.email || '');
                address.val(teacher.address || '');
                if (!teacher?.departmentName) {
                    fetchAllDepartment(false, false);
                } else {
                    fetchAllDepartment(teacher.departmentName, teacher.departmentId)
                }
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

    /**
     * @param {string} departmentName - name of department
     * @param {string} departmentId - id of department
     * @param {DeparmentResponse[]} data - total item
     * @returns {string} - html of pagination
     */
    function generateDepartmentOption(departmentName, departmentId ,data) {
        let html = '';

        if (departmentName && departmentId) {
            html += `
                <option value="${departmentId}" class="fw-bold mt-3">${departmentName}</option>
            `
        } else {
            html += `
                <option value="">Choose department</option>
            `
        }

        data.forEach((item, index) => {
            if (item.id === departmentId) return;
            html += `
                <option value="${item.id}">${item.name}</option>
            `
        });
        return html;
    }

    // function generatePaginationOptions(currentPage, totalPages) {
    //     const MAX_PAGE = 5;
    //     let pagination = '';
    //
    //     //  if totalPage > MAX_PAGE => show MAX_PAGE
    //     // else the distance between startPage and endPage is MAX_PAGE
    //     let startPage = 1;
    //     let endPage = totalPages;
    //
    //     if (totalPages > MAX_PAGE) {
    //         const distance = Math.floor(MAX_PAGE / 2);
    //         startPage = currentPage - distance;
    //         endPage = currentPage + distance;
    //
    //         if (startPage < 1) {
    //             endPage += Math.abs(startPage) + 1;
    //             startPage = 1;
    //         }
    //
    //         if (endPage > totalPages) {
    //             startPage -= endPage - totalPages;
    //             endPage = totalPages;
    //         }
    //     }
    //
    //     console.log("startPage", startPage);
    //     console.log("endPage", endPage);
    //
    //     for (let i = startPage; i <= endPage; i++) {
    //         pagination += `
    //             <li class="page-item ${currentPage === i ? 'active' : ''}">
    //                 <span class="page-link">${i}</span>
    //             </li>
    //         `
    //     }
    //
    //     return pagination;
    // }

    function fetchAllDepartment(departmentName, departmentId , page = 0) {
        const MAX_SIZE = 5;
        $.ajax({
            url: `/api/v1/department/all?size=${MAX_SIZE}&page=${page}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            success: function (data) {
                const {totalPages, pageable, content, number : currentPage} = data;
                const pageSize = pageable.pageSize;
                console.log(data)
                const pagination = generatePaginationOptions(currentPage + 1, totalPages);
                const departmentOptions = generateDepartmentOption(departmentName, departmentId, content);

                paginationDropdown.html(departmentOptions);
                $('.department-pagination').html(pagination);
                registerPageDepartmentEvent();
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    function registerPageDepartmentEvent() {
        const pageItems = $(".department-pagination").find(".page-item");
        pageItems.each(function (index, pageItem) {
            $(pageItem).on('click', function (e) {
                console.log($(this))
                e.preventDefault();
                let page = $(this).find(".page-link").text();
                console.log(page)
                const currentSelected = $('#departments').find(":selected");
                const departmentId = currentSelected.val();
                const departmentName = currentSelected.text();
                if (page === '>') {
                    page = parseInt($(this).prev().find(".page-link").text()) + 1;
                }
                fetchAllDepartment(departmentName, departmentId, page - 1);
            })
        })
    }
})