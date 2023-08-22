$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const calendarContainer = $("#calendar");
    const levelSelection = $("#select_level");
    const semesterSelection = $("#select_semester");
    const classes = $(".classes");
    const subject = $(".subjects");
    const btnSave = $(".btn-save");
    const lessons = {};

    let currentLessonActive = [];
    let subjectItems = [];

    fetchAllLevel(TOKEN, levelSelection);
    fetchAllSemester(TOKEN, semesterSelection);

    const calendar = new FullCalendar.Calendar(calendarContainer[0], {
        initialView: 'timeGridWeek',
        initialDate: Date.now(),
        rerenderDelay: 500,
        contentHeight: 'auto',
        contentsHeight: 'auto',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek'
        }
    })

    calendar.render();

    semesterSelection.on('change', function () {
        const semesterId = $(this).val();
        const levelId = levelSelection.val();

        if (levelSelection.val() === "") {
            classes.empty();
            classes.html(`
                <div class="alert alert-danger" role="alert">
                    Please select <strong>level</strong> to get classes!
                </div>`)
            return;
        }

        fetchClasses({
            levelId,
            semesterId
        });
    })

    levelSelection.on('change', function () {
        const levelId = $(this).val();
        const semesterId = semesterSelection.val();

        if (semesterSelection.val() === "") {
            classes.empty();
            classes.html(`
                <div class="alert alert-danger" role="alert">
                    Please select <strong>semester</strong> to get classes!
                </div>`)
            return;
        }

        btnSave.on("click", function () {
            const classActive = $(".class-item.active:not(.disable)");
            if (!classActive.length) {
                Swal.fire({
                    icon: "warning",
                    title: "Please select class to save schedule!",
                    text: "You must select class to save schedule!",
                    showConfirmButton: false,
                    timer: 1500
                })
                return;
            }

            const classId = classActive.data("id");
            const semesterId = semesterSelection.val();
            const levelId = levelSelection.val();

            if (!currentLessonActive.length) {
                Swal.fire({
                    icon: "warning",
                    title: "Please select lesson to save schedule!",
                    text: "You must select lesson to save schedule!",
                    showConfirmButton: false,
                    timer: 1500
                })
                return;
            }

            if (!semesterId || !levelId) {
                Swal.fire({
                    icon: "warning",
                    title: "Please select semester and level to save schedule!",
                    text: "You must select semester and level to save schedule!",
                    showConfirmButton: false,
                    timer: 1500
                })
                return;
            }

            const data = {
                classId,
                semesterId,
                levelId,
                lessonIds: currentLessonActive,
                subjectIds: subjectItems
            }

            console.log("data", data);
            let swal = null;
            Swal.fire({
                title: 'Are you sure?',
                text: "You want to save this schedule?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, save it!',
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33'
            }).then(result => {
                if (result.isConfirmed) {
                    $.ajax({
                        url: "/api/v1/schedule/create",
                        type: "POST",
                        headers: {
                            'Authorization': `Bearer ${TOKEN}`
                        },
                        data: JSON.stringify(data),
                        contentType: "application/json",
                        beforeSend: function () {
                            swal = Swal.fire({
                                title: 'Saving schedule...',
                                text: 'Please wait a few seconds.',
                                showConfirmButton: false,
                                willOpen: () => {
                                    Swal.showLoading()
                                }
                            })
                        },
                        success: function (response) {
                            console.log(response);
                            swal.close();
                            Swal.fire({
                                icon: "success",
                                title: "Save schedule successfully!",
                                showConfirmButton: false,
                                timer: 1500
                            })
                        },
                        error: function (error) {
                            swal.close();
                            console.log(error.responseText);
                            Swal.fire({
                                icon: "error",
                                title: "Save schedule failed!",
                                showConfirmButton: false,
                                timer: 1500
                            })
                        }
                    })
                }
            })
        });


        fetchAllSubjectByLevel({
            token: TOKEN,
            levelId
        }, renderSubjects)
        fetchClasses({
            levelId,
            semesterId
        });
    })

    function fetchClasses(
        {
            levelId,
            semesterId,
            page = 0
        }
    ) {
        $.ajax({
            url: `/api/v1/class/get?levelId=${levelId}&semesterId=${semesterId}&page=${page}&size=8`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (response) {
                console.log(response);
                classes.empty();
                const {number, totalPages} = response;

                const paginationLinks = generatePaginationOptions(number + 1, totalPages, 10);
                const pagination = classes.parent().find(".pagination");
                pagination.empty();
                pagination.append(paginationLinks);
                registerPaginationClick(pagination)
                response.content.forEach((item, index) => {
                    const classItem = $(`
                        <div class="col-12 col-md-3 pe-2 mt-2 ">
                            <div class="class-item class border-end rounded-2 p-2 shadow ${item.hasSchedule ? 'active disable' : ''}" data-id="${item.id}">
                                <h5>${item.name}</h5>
                                <i><strong>Teached by: </strong></i>
                                <span>${item.chairman ? item.chairman : `No Range`}</span>
                            </div>
                        </div>
                    `);

                    classes.append(classItem);
                })
                activeClassItemClick();
            },
            error: function (error) {
                console.log(error.responseText);
            }
        })
    }

    function renderSubjects(subjects) {
        subject.empty();
        if (!Object.keys(subjects).length) {
            const emptyTitle = $(`
                <div>
                    <h5 class="text-primary fw-semibold">No subjects for this level</h5>
                </div>
            `);

            subject.append(emptyTitle);
        }

        Object.keys(subjects).forEach(key => {
            const data = subjects[key];
            data.subjects.forEach(subject => {
                lessons[subject.id] = subject.lessons;
            })
            const subjectGroupedBy = $(`
                <div class="col-12 col-md-6 department-item" data-id="${key}">
                    <h5 class="text-center"><i><strong>Department</strong></i>: ${data.name}</h5>
                    <div class="content row">
                        ${generateSubjectItem(data.subjects)}
                    </div>
                </div>
            `)

            subject.append(subjectGroupedBy);
        })

        activeSubjectItemClick();
    }

    function activeClassItemClick() {
        const classItems = $(".class-item:not(.disable)");

        classItems.on("click", function () {
            if ($(this).hasClass("active")) {
                $(this).removeClass("active");
            } else {
                classItems.removeClass("active");
                $(this).addClass("active");
            }
        })
    }

    function activeSubjectItemClick() {
        const departmentItems = $(".department-item");

        departmentItems.each(function (index, departmentItem) {
            const subjectItems = $(departmentItem).find(".subject-item");

            //     group subject item by subjectName into array
            const subjectItemsGroupedBySubjectName = subjectItems.toArray().reduce((acc, subjectItem) => {
                const subjectName = $(subjectItem).find(".subject__name").text();
                const subjectId = $(subjectItem).data("id");
                const subjectTeachedBy = $(subjectItem).find("span").text();

                if (!acc[subjectName]) {
                    acc[subjectName] = {
                        'this': [subjectItem],
                    }
                } else {
                    acc[subjectName] = {
                        'this': [...acc[subjectName].this, subjectItem]
                    }
                }

                return acc;
            }, {});

            console.log(subjectItemsGroupedBySubjectName);

            //     active subject item click
            Object.keys(subjectItemsGroupedBySubjectName).forEach(key => {
                const subjectItem = subjectItemsGroupedBySubjectName[key];

                if (subjectItem.this.length > 1) {
                    subjectItem.this.forEach((item, index) => {
                        $(item).on("click", function () {
                            if ($(this).hasClass("active")) {
                                $(this).removeClass("active");
                                removeCalendarLesson(lessons[$(this).data("id")]);
                            } else {
                                subjectItem.this.forEach((item, index) => {
                                    $(item).removeClass("active");
                                })
                                $(this).addClass("active");
                                showCalendarLesson(lessons[$(this).data("id")], $(this).find(".subject__name").text());
                            }
                        })
                    })
                } else {
                    $(subjectItem.this[0]).on("click", function () {
                        if ($(this).hasClass("active")) {
                            $(this).removeClass("active");
                            removeCalendarLesson(lessons[$(this).data("id")], $(this).data("id"));
                        } else {
                            $(this).addClass("active");
                            showCalendarLesson(lessons[$(this).data("id")], $(this).find(".subject__name").text(), $(this).data("id"));
                        }
                    })
                }
            })
        })
    }

    function generateSubjectItem(subjects) {
        let html = '';
        subjects.forEach((subjectItem, index) => {
            html += `
                <div class="col-12 col-md-6 pe-2">
                    <div class="subject-item subject border-end rounded-2  m-2 p-2 shadow" data-id="${subjectItem.id}">
                        <h5 class="text-title subject__name">${subjectItem.name}</h5>
                        <i><strong>Teached by: </strong></i><span>${subjectItem.teacher.name ? subjectItem.teacher.name : `No Range`}</span>
                    </div>
                </div>
            `
        })
        return html;
    }

    function showCalendarLesson(lessons, subjectName, subjectId) {
        if (!lessons.length) {
            return;
        }
        //     render event calendar
        const events = lessons.map(lesson => {
            currentLessonActive.push(lesson.id);
            // [2023, 8, 11, 11, 30] to 2023-08-11T11:30:00
            const {startDate, endDate} = lesson;
            const startTime = new Date(startDate[0], startDate[1] - 1, startDate[2], startDate[3], startDate[4]).toISOString();
            const endTime = new Date(endDate[0], endDate[1] - 1, endDate[2], endDate[3], endDate[4]).toISOString();
            return {
                title: subjectName,
                start: startTime,
                end: endTime,
                id: lesson.id
            }
        })

        console.log("events", events);
        subjectItems.push(subjectId);
        calendar.addEventSource(events);

        calendar.refetchEvents()
    }

    function removeCalendarLesson(lessons, subjectId) {
        if (!lessons.length) {
            return;
        }

        lessons.forEach(lesson => {
            currentLessonActive = currentLessonActive.filter(item => item !== lesson.id);
            calendar.getEventById(lesson.id).remove();
        })

        if (subjectId) {
            subjectItems = subjectItems.filter(item => item !== subjectId);
        }
        calendar.refetchEvents()
    }

    function registerPaginationClick(paginationElement) {
        const paginationItems = paginationElement.find(".pagination-item");
        paginationItems.on("click", function () {
            if ($(this).hasClass("active")) {
                return;
            }
            paginationItems.removeClass("active");
            $(this).addClass("active");
            const page = $(this).data("page");
            const levelId = levelSelection.val();
            const semesterId = semesterSelection.val();

            fetchClasses({
                levelId,
                semesterId,
                page
            });
        })
    }
})