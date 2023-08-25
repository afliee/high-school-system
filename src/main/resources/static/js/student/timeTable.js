$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const STUDENT_ID = $("#id").val();
    const SCHEDULE_ID = $("#schedule_id").val();

    const calendarContainer = $(".calendar");

    if (SCHEDULE_ID === 'null') {
        calendarContainer.hide();
        $(".no-schedule").show();
        return;
    }


    const calendar = new FullCalendar.Calendar(calendarContainer[0], {
        initialView: 'dayGridMonth',
        initialDate: Date.now(),
        rerenderDelay: 500,
        allDaySlot: false,
        contentsHeight: 'auto',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek'
        },
        selectable: true,
        mirrorSelect: true,
        eventClick: function ({event}) {
            console.log("eventClick", event.extendedProps);

            const {id , subject, shift, absent, startDate, endDate} = event.extendedProps;
            const {name, department, teacher, color } = subject;
            // const startTime = new Date(startDate[0], startDate[1] - 1, startDate[2], startDate[3], startDate[4]).toISOString();
            // const endTime = new Date(endDate[0], endDate[1] - 1, endDate[2], endDate[3], endDate[4]).toISOString();
            const startTime = moment(startDate).format("DD-MM-YYYY HH:mm");
            const endTime = moment(endDate).format("DD-MM-YYYY HH:mm");

            const modal = $("#lessonDetail");

            modal.find(".modal-title").text(subject.name);
            modal.find(".modal-header").css("background-color", color);
            // modal.find(".modal-body .subject-name").text(subjectName);
            modal.find(".modal-body .department-name").text(department);
            modal.find((`.modal-body .teacher-name`)).html(`${teacher.fullName} :: ${teacher?.departmentName ? `<span class="text-primary">${teacher.departmentName}</span> department` : ''}`);
            modal.find((`.modal-body .shift-name`)).text(shift.name);
            modal.find((`.modal-body .start-date`)).text(startTime);
            modal.find((`.modal-body .end-date`)).text(endTime);

            //     show modal
            modal.modal("show");
        }
    });

    calendar.render();
    $.ajax({
        url: `/api/v1/student/schedule/${SCHEDULE_ID}`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${TOKEN}`
        },
        success: function (response) {
            console.log(response);
            const {lessons} = response;
            lessons.forEach(lesson => {
                const {startDate, endDate} = lesson;
                const startTime = new Date(startDate[0], startDate[1] - 1, startDate[2], startDate[3], startDate[4]).toISOString();
                const endTime = new Date(endDate[0], endDate[1] - 1, endDate[2], endDate[3], endDate[4]).toISOString();

                calendar.addEvent({
                    title: lesson.subject.name,
                    start: startTime,
                    end: endTime,
                    id: lesson.id,
                    backgroundColor: lesson.subject.color,
                    allDay: false,
                    extendedProps: {
                        ...lesson
                    }
                })
            })


        },
        error: function (err) {
            console.log(err.responseText)
        }
    })


})