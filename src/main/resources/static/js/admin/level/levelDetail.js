$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const LEVEL_ID = $("#levelId").val();

    const semesterSelection = $("#select_semester");
    const subjects = $(".subjects");
    const classes = $(".classes");
    console.log("semesterSelection", semesterSelection.val());

    if (semesterSelection.val() === null) {
        $(".loader").hide();
    }


    fetchAllSemester(TOKEN, semesterSelection);

    fetchAllSubjectByLevel({
        token: TOKEN,
        levelId: LEVEL_ID
    }, function (subjects_data) {
        if (Object.keys(subjects_data).length === 0) {
            subjects.append(`
                <div class="col-12">
                    <div class="alert alert-warning" role="alert">
                        No subject found!
                    </div>
                </div>
            `)
            return;
        }
        Object.keys(subjects_data).forEach(semesterId => {
            const subjectsAvailable = subjects_data[semesterId].subjects;
            subjectsAvailable.forEach(subject => {
                const template = $(`
                <div class="content col-md-4 col-12 pe-2" data-id="${subject.id}">
                    <div class="card-tag rounded border-end mt-3 shadow p-2">
                        <h5 class="text-title text-truncate" data-bs-toggle="tooltip" title="${subject.name}">${subject.name}</h5>
                       <i><strong>Teached By: </strong></i> <span>${subject.teacher.name ? subject.teacher.name : 'No Range'}</span>
                    </div>
                </div>
            `);

                subjects.append(template);
            })
        })
    })

    semesterSelection.on('change', function () {
        classes.empty();
        classes.html(`<h5 class="fw-normal">Classes</h5>`);
        const semesterId = $(this).val();

        fetchAllClassByLevelAndSemester({
            token: TOKEN,
            levelId: LEVEL_ID,
            semesterId: semesterId
        }, generateClasses, beforeSend)
    })

    function generateClasses(classes_data) {
        $(".loader").hide();
        if (classes_data.content.length === 0) {
            $(".pagination").empty();
            classes.append(`
                    <div class="col-12">
                        <div class="alert alert-warning" role="alert">
                            No class found!
                        </div>
                    </div>
                `)
            return;
        }
        const { number : currentPage, totalPages} = classes_data;
        console.log("currentPage", currentPage);
        console.log("totalPages", totalPages);
        const pageOptions = generatePaginationOptions(currentPage + 1, totalPages, 6);
        const pagination = $(".pagination")

        pagination.empty();
        pagination.append(pageOptions);
        console.log("classes_data", classes_data)
        classes_data.content.forEach(class_data => {
            const template = $(`
                    <div class="class-item content col-md-4 col-12 pe-2" data-id="${class_data.id}">
                        <div class="card-tag rounded border-end mt-3 shadow p-2">
                            <h5 class="text-title text-truncate" data-bs-toggle="tooltip" title="${class_data.name}">${class_data.name}</h5>
                            <p><i><strong>Teached by</strong></i>: <span>${class_data.chairman}</span></p>
                            <p><i><strong>Present</strong></i>: <span>${class_data.present}</span></p>
                        </div>
                    </div>
                `);

            classes.append(template);
        });

        registerPaginationItemClick();
        registerClassItemClick();
    }

    function registerPaginationItemClick() {
        const paginationItems = $(".pagination li");
        paginationItems.on('click', function () {
            const page = $(this).text();
            const semesterId = semesterSelection.val();

            fetchAllClassByLevelAndSemester({
                token: TOKEN,
                levelId: LEVEL_ID,
                semesterId: semesterId,
                page: page - 1
            }, generateClasses, beforeSend);
        })
    }

    function registerClassItemClick() {
        const classItems = $(".class-item");
        classItems.on('click', function () {
           const id = $(this).data('id');
           window.location.href = `/auth/admin/classes/${id}`;
        });
    }

    function beforeSend() {
        $(".loader").show();
    }
})