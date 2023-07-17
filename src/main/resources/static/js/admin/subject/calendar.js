$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
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
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
        },
        contentHeight: 'auto',
        editable: true,
        selectable: true,
        selectMirror: true,
        navLinks: true,
        eventSources: function (fetchInfo, successCallback, failureCallback) {
            const start = fetchInfo.startStr;
            const end = fetchInfo.endStr;

            console.log("start: " + start);
            console.log("end: " + end);

            $.ajax({
                url: `/api/v1/lessons/get/${subjectId}?semesterId=${semesterId}?start=${start}?end=${end}`,
                type: "GET",
                headers: {
                    "Authorization": `Bearer ${TOKEN}`
                },
                success: function (data) {
                    console.log(data);
                    const events = data.map(function (lesson) {
                        return {
                            id: lesson.id,
                            title: lesson.subject.name,
                            start: lesson.startTime,
                            end: lesson.endTime,
                            color: lesson.subject.color,
                            extendedProps: {
                                lessonId: lesson.id
                            }
                        }
                    });
                    successCallback(events);
                }
            })
        }
    });

    calendar.render();
})