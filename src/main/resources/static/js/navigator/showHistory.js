$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');

    const historyItems = $('.history-item');

    historyItems.on('click', function () {
        const historyItem = $(this);

        const attendanceId = historyItem.data('id');
        console.log(attendanceId)
        $.ajax({
            url: `/api/v1/attendance/${attendanceId}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            timeout: 10000,
            success: function (data) {
                console.log(data)
                const collapseContent = historyItem.parent().find('.collapse-content');
                collapseContent.empty();
                const {students, classEntity} = data;
                const studentsTemplate = students.map(student => {
                    return `
                        <tr>
                            <td class="text-center" style="vertical-align: center">
                                <label>
                                    <input type="checkbox" name="attendance_history" data-id="${student.id}" value="${student.id}" ${!student.absent ? 'checked' : ''}>
                                </label>
                            </td>
                            <td class="text-center" style="vertical-align: center" >${student.name}</td>
                            <td class="text-center" style="vertical-align: center" >${student.fullName}</td>
                            <td style="vertical-align: center" class="text-center  d-flex justify-content-center">
                                <img src="${student?.avatar ? student.avatar : '/images/1.jpg'}" alt="avatar"
                                     class="avatar avatar-lg" width="50"/>
                            </td>
                            <td class="text-center" style="vertical-align: center" >${student.email}</td>
                            <td class="text-center" style="vertical-align: center">${student?.phone ? student.phone : ''}</td>
                        </tr>
                    `
                });

                collapseContent.html(`
                    <div class="table-responsive mt-3">
                        <table class="table table-striped table-hover table-bordered">
                            <thead class="">
                                <tr>
                                    <th></th>
                                    <th class="text-center">Nickname</th>
                                    <th class="text-center">Student</th>
                                    <th class="text-center">Avatar</th>
                                    <th class="text-center">Email</th>
                                    <th class="text-center">Phone</th>
                                </tr>
                            </thead>
                            <tbody class="">
                                ${studentsTemplate}
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colspan="6" class="text-center">
                                        <button class="btn btn-primary btn-sm" id="save-attendance" data-id="${attendanceId}" data-class-id="${classEntity.id}">Save Changes</button>
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                `)

                // add animation
                collapseContent.collapse('toggle');

                // add event listener
                const saveAttendanceBtn = $('#save-attendance');
                saveAttendanceBtn.on('click', function () {
                    updateAttendance(saveAttendanceBtn.data('id'), saveAttendanceBtn.data('class-id'), historyItem.parent());
                });
            },
            error: function (error) {
                console.log(error)
            }
        })
    })

    function updateAttendance(attendanceId, classId, parent) {
        const studentList = parent.find('input[name="attendance_history"]:checked');
        const studentIds = [];
        studentList.each(function () {
            studentIds.push($(this).val());
        });

        const data = {
            classId: classId,
            studentIds: studentIds
        };

        console.log( "data", data)
        $.ajax({
            url: `/api/v1/attendance/${attendanceId}`,
            type: 'PUT',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            timeout: 30000,
            data: JSON.stringify(data),
            contentType: 'application/json',
            success: function (data) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text:   'Update attendance successfully!',
                    timer: 2000,
                });
                setTimeout(function () {
                    location.href = "/attendance";
                }, 2000);
            },
            error: function (error) {
                console.log(error)
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text:   'Something went wrong!',
                    timer: 2000,
                })
            }
        })
    }
})