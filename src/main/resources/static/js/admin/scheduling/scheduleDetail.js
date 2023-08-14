$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const CLASS_ID = $("#classId").val();
    const SEMESTER_ID = $("#semesterId").val();

    const subjectsAvailable = $(".subject__available");
    const calendarContainer= $(".calendar__container");

    getSubjectAvailable();

    const calendar = new FullCalendar.Calendar(calendarContainer[0], {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        contentsHeight: "auto",
        contentHeight: "auto",
        events: function (fetchInfo, successCallback, failureCallback) {
            const start = moment(fetchInfo.startStr).format("yyyy-MM-DD");
            const end = moment(fetchInfo.endStr).format("yyyy-MM-DD");
            $.ajax({
                url: `/api/v1/schedule/detail?classId=${CLASS_ID}&semesterId=${SEMESTER_ID}&start=${start}&end=${end}`,
                type: "GET",
                headers: {
                    'Authorization': `Bearer ${TOKEN}`
                },
                success: function (data) {
                    console.log(data);
                    const events = data.map(function (item) {
                        // item.start is array = [2023, 8, 15, 10, 30] => [year, month, day, hour, minute]
                        // convert to string = "2023-08-15T10:30:00"
                        const startDate = new Date(item.startDate[0], item.startDate[1] - 1, item.startDate[2], item.startDate[3], item.startDate[4]).toISOString();
                        const endDate = new Date(item.endDate[0], item.endDate[1] - 1, item.endDate[2], item.endDate[3], item.endDate[4]).toISOString();
                        return {
                            title: item.subject.name,
                            start: startDate,
                            end: endDate,
                            color: item.subject.color
                        }
                    })
                    successCallback(events);
                },
                error: function (error) {
                    console.log(error.responseText);
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong!',
                        timer: 2000
                    })
                    failureCallback(error);
                }
            })
        }
    });

    calendar.render();
    function getSubjectAvailable() {
        $.ajax({
            url: `/api/v1/schedule/subject-available?classId=${CLASS_ID}`,
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                subjectsAvailable.html(`
                    <div class="d-flex justify-content-center w-100">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
                `);
            },
            success: function (data) {
                console.log(data);
                subjectsAvailable.html("");
                if (data.length > 0) {
                    data.forEach(function (subject) {
                        subjectsAvailable.append(`
                            <div class="subject__item subject col-12 col-md-3 col-sm-4 p-2 border-end shadow rounded-2 me-2" style="border-right-color: ${subject.color}" data-id="${subject.id}">
                                <div class="subject__name text-title-small">${subject.name}</div>
                                <div class="py-2">
                                    <div class="subject__teacher py-2">
                                        <span><i><strong>Teach by</strong></i></span>
                                            : ${subject.teacher.name}
                                    </div>
                                    <div class="subject__department"><i><strong>Belong to</strong></i> : ${subject.department}</div>
                                </div>
                            </div>`);
                    })
                }
            },
            error: function (error) {
                console.log(error.responseText);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                    timer: 2000
                })
            }
        })
    }

})