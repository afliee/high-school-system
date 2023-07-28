$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const departments = $("#departments");
    const modal = $("#addDepartment");

    fetchAllDepartments();


// ========= Event Listeners =========
    modal.find('.btn-save').on('click', function () {
        const name = modal.find('input[name="name"]').val();
        addDepartment(name);
    })


//     ========= Call API =========
    function addDepartment(name) {
        $.ajax({
            url: '/api/v1/department/create',
            type: 'POST',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            contentType: 'application/json',
            data: JSON.stringify({
                name: name,
                description: ''
            }),
            success: function (data) {
                modal.animate({
                    scrollTop: 0
                }, 500);

                modal.modal('hide');

                Toastify({
                    text: "Add department successfully",
                    backgroundColor: "#0dbd7d",
                    className: "toastify-success",
                    duration: 3000,
                    gravity: "top",
                    position: "right"
                }).showToast();

                fetchAllDepartments()
            },
            error: function (data) {
                console.log(data);
                Toastify({
                    text: "Add department failed",
                    backgroundColor: "#dc3545",
                    className: "toastify-error",
                    duration: 3000,
                    gravity: "top",
                    position: "right"
                }).showToast();
            }
        })
    }

// ========= Event Handlers =========
    function fetchAllDepartments(page = 0, size = 9, sort = "name") {
        $.ajax({
            url: `/api/v1/department/all?page=${page}&size=${size}&sortBy=${sort}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                renderDepartment(data);
            }
        })
    }

    function renderDepartment(data) {
        const {totalPages, content, number} = data;

        let departmentsHTML = '';
        content.forEach(department => {
            if (department.subjectColors.length === 0) {
                department.subjectColors.push({
                    name: 'No Subject',
                    color: ''
                })
            }
            // display limit 3 subjects
            if (department.subjectColors.length > 3) {
                department.subjectColors = department.subjectColors.slice(0, 3);
                //     add more subject
                department.subjectColors.push({
                    name: '...',
                    color: ''
                })
            }
            departmentsHTML += `
                <div class="col-md-4 col-sm-6 col-xs-12 my-3" >
                    <div class="card department" data-id="${department.id}">
                        <div class="card-body">
                            <h3 class="name text-title text-truncate">${department.name}</h3>
                            <div class="info d-flex justify-content-between align-items-center my-3">
                                <span class="create-at">Fouded: ${new Date(department.foundedDate).toLocaleDateString('en-GB')}</span>
                            </div>
<!--                            display subjects 1 line-->
                            <div class="subjects d-flex flex-shrink-1">
                                ${department.subjectColors.length ? department.subjectColors.map(function (subject) {
                                const colorString = subject?.color;
                                return `
                                        <label class="subject badge text-truncate" data-bs-toggle="tooltip" title="${subject.name}" style="border: 1px solid ${colorString || 'black'}; color: ${colorString || 'black'}">${subject.name}</label>
                                    `
                                }).join('') : `
                                        <label class="subject badge bg-primary">No Subject</label>
                                `}
                            </div>
                        </div>
                    </div>
                </div>
            `
        });

        departments.html(departmentsHTML);
        renderPagination(number + 1, totalPages);
        registerShowDepartmentEvent();
    }

    function renderPagination(currentPage, totalPage) {
        let pagination = '';
        const MAX_PAGE = 12;

        const start = Math.max(currentPage - 2, 1);
        const end = Math.min(currentPage + MAX_PAGE - 1, totalPage);

        if (currentPage > 1) {
            pagination += `
                <li class="page-item">
                    <a class="page-link" href="#">&lt;</a>
                </li>`
        }

        for (let i = start; i <= end; i++) {
            pagination += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#">${i}</a>
                </li>
            `;
        }

        if (currentPage < totalPage) {
            pagination += `
                <li class="page-item">
                    <a class="page-link" href="#">&gt;</a>
                </li>`
        }

        $(".pagination").html(pagination);
        registerPaginationEvent();
    }

    function registerPaginationEvent() {
        $(".pagination .page-link").click(function (e) {
            e.preventDefault();
            const page = $(this).text();
            if (page === '<') {
                const currentPage = $(".pagination .active .page-link").text();
                console.log(currentPage)
                fetchAllDepartments(Math.max(currentPage - 2, 0));
                return;
            } else if (page === '>') {
                const currentPage = $(".pagination .active .page-link").text();
                console.log(currentPage)
                fetchAllDepartments(Math.min(currentPage, 10));
                return;
            }
            fetchAllDepartments(page - 1);
        })
    }

    function registerShowDepartmentEvent() {
        $(".department").click(function () {
            const id = $(this).data('id');
            window.location.href = `/auth/admin/department/${id}`;
        })
    }
})