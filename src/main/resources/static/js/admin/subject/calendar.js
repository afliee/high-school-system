$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const semesterSelection = $("#select_semester");

    const subjectId = $("#subjectId").val();
    const semesterId = $("#semesterId").val();

    console.log("calendar.js")
    const calendarContainer = $("#calendar");

    const now = moment(Date.now()).format("YYYY-MM-DD");

    const calendar = new FullCalendar.Calendar(calendarContainer[0], {
        initialView: 'dayGridMonth',
        initialDate: now,
        rerenderDelay: 500,
        headerToolbar: {
            left: 'prev,next,today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek, prevYear,nextYear'
        },
        contentHeight: 'auto',
        editable: true,
        selectable: true,
        selectMirror: true,
        navLinks: true,
        eventChange: function (changeInfo) {
            //       call api update lesson
            const {event} = changeInfo;
            console.log("event", event);
        },
        events: function (fetchInfo, successCallback, failureCallback) {
            fetchLessons(fetchInfo, successCallback, failureCallback);
        }
    });

    const startTimeDefault = $("#semesterId").data("start-time");
    calendar.gotoDate(startTimeDefault);
    calendar.render();

    getALlSemester()

    function getALlSemester() {
        $.ajax({
            url: "/api/v1/semester",
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                console.log(data)
                generateSemesterSelection(data);
            },
            error: function (error) {
                console.log(error.responseJSON.message);
            }
        })
    }

    function generateSemesterSelection(data) {
        const semesterId = $("#semesterId").val();
        // remove first option
        semesterSelection.find("option:first").remove();
        data.forEach(semester => {
            // convert date from array [2024, 1, 1] => 2024-01-01
            const startDate = new Date(semester.startDate[0], semester.startDate[1] - 1, semester.startDate[2]).toISOString();
            const endDate = new Date(semester.endDate[0], semester.endDate[1] - 1, semester.endDate[2]).toISOString();

            const option = $(`<option value="${semester.id}" data-start-date="${startDate}" data-end-date="${endDate}" ${semester.id === semesterId ? 'selected' : 0}>${semester.name}</option>`);
            semesterSelection.append(option);
        })

        registerSemesterSelectionEvent();
    }

    function registerSemesterSelectionEvent() {
        semesterSelection.on('change', function () {
            $("#semesterId").val($(this).val());
            const startDate = $(this).find(":selected").data("start-date");
            const endDate = $(this).find(":selected").data("end-date");

            calendar.gotoDate(startDate);
        })
    }

    function fetchLessons(fetchInfo, successCallback, failureCallback) {
        const startStr = moment(fetchInfo.startStr).format("yyyy-MM-DD");
        const endStr = moment(fetchInfo.endStr).format("yyyy-MM-DD");

        console.log("startStr: " + startStr);
        console.log("endStr: " + endStr);
        // convert start to yyyy-MM-dd
        const semesterId = $("#semesterId").val();
        console.log("semesterId", semesterId);
        $.ajax({
            url: `/api/v1/lessons/get/${subjectId}?semesterId=${semesterId}&start=${startStr}&end=${endStr}`,
            type: "GET",
            headers: {
                "Authorization": `Bearer ${TOKEN}`
            },
            success: function (data) {
                console.log(data)
                const events = data.map(function (lesson) {
                    // "startDate": [
                    //             2021,
                    //             9,
                    //             18,
                    //             9,
                    //             50
                    //         ]
                    // convert startDate to ISOString
                    const {startDate, endDate} = lesson;
                    const startTime = new Date(startDate[0], startDate[1] - 1, startDate[2], startDate[3], startDate[4]).toISOString();
                    const endTime = new Date(endDate[0], endDate[1] - 1, endDate[2], endDate[3], endDate[4]).toISOString();
                    return {
                        id: lesson.id,
                        title: lesson.subject.name,
                        start: startTime,
                        end: endTime,
                        color: lesson.subject.color,
                        extendedProps: {
                            lessonId: lesson.id
                        }
                    }
                });
                successCallback(events);
            },
            error: function (error) {
                console.log(error.responseJSON.message);
                failureCallback(error);
            }
        })
    }


    $(".btn-add-lessons").on('click', function () {
        location.href = `/auth/admin/subjects/${subjectId}/lessons/add`
    })
})