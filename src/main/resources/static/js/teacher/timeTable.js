$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');

    const SUBJECT_ID = $("#subject_id").val();

    const container = $("#time-table");

    const calender = new FullCalendar.Calendar(container[0], {
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
            modal.find((`.modal-body .teacher-name`)).html(`${teacher.fullName} :: <span class="text-primary">${teacher.departmentName}</span> department`);
            modal.find((`.modal-body .shift-name`)).text(shift.name);
            modal.find((`.modal-body .start-date`)).text(startTime);
            modal.find((`.modal-body .end-date`)).text(endTime);

            //     show modal
            modal.modal("show");
        }
    })

    // get all lesson by subject
    $.ajax({
        url: `/api/v1/lessons/get?subjectId=${SUBJECT_ID}`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${TOKEN}`
        },
        success: function (data) {
            data.forEach(lesson => {
                const {startDate, endDate} = lesson;
                const startTime = new Date(startDate[0], startDate[1] - 1, startDate[2], startDate[3], startDate[4]).toISOString();
                const endTime = new Date(endDate[0], endDate[1] - 1, endDate[2], endDate[3], endDate[4]).toISOString();

                calender.addEvent({
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
        error: function (error) {
            console.log(error);
        }
    })
    calender.render();
})