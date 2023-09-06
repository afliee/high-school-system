$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const DEPARTMENT_ID = $("#departmentId").val();
    const SUBJECT_ID = $("#subjectId").val();
    const TEACHER_ID = $("#teacher_id").val();
    const LEVEL_ID = $('#level_id').val();

    const container = $("#editor")
    const editor = CKEDITOR.replace(container[0]);


    const btnUpdate = $("#updateSubject");

    getAllTeacher();
    getAllLevel();

    $.ajax({
        url: `/api/v1/subject/get/${SUBJECT_ID}`,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${TOKEN}`
        },
        success: function (subject) {
            console.log(subject)
            $("#name").val(subject?.name);
            editor.setData(subject?.description);
            $(".description").html(subject?.description);
        }
    })


    btnUpdate.on('click', function () {
        const name = $("#name").val();
        const description = $(".description").html();
        const teacherId = $("#teacher").val();
        const levelId = $("#level").val();

        const data= {
            name,
            description,
            teacherId,
            levelId,
            departmentId: DEPARTMENT_ID
        }
        $.ajax({
            url: `/api/v1/subject/update/${SUBJECT_ID}`,
            type: 'PUT',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify(data),
            contentType: 'application/json',
            beforeSend: function () {
                btnUpdate.prop('disabled', true);
                btnUpdate.html(
                    `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>Updating...`
                );
            },
            success: function (res) {
                console.log(res);
                btnUpdate.prop('disabled', false);
                btnUpdate.html(`Update`);
                Swal.fire({
                    icon: 'success',
                    title: 'Update Subject Successfully',
                    showConfirmButton: false,
                    timer: 1500
                })

                setTimeout(() => {
                    location.href = `/auth/admin/subjects/${SUBJECT_ID}`;
                }, 1500)
            },
            error: function (err) {
                console.log(err.responseText);

                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong!',
                    timer: 1500
                });
            }
        })
    })
//     ========== EVENT ==========
    editor.on('change', function (e) {
        const data = e.editor.getData();
        $(".description").html(data);
    })



//  ========== CALL API ==========
    function getAllTeacher() {
        $.ajax({
            url: `/api/v1/teacher/all`,
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                console.log(res);
                const select = $("#teacher");
                const defaultOption = $('<option value="" selected disabled hidden>Choose Teacher</option>');

                select.append(defaultOption);
                res.forEach(teacher => {
                    const option = $(`<option ${teacher.id === TEACHER_ID ? 'selected' : ''} value="${teacher.id}">${teacher.fullName}</option>`);
                    select.append(option);
                })
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function getAllLevel() {
        $.ajax({
            url: `/api/v1/level/all?isDetail=true`,
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                console.log(res);

                const select = $("#level");

                const defaultOption = $('<option value="" selected disabled hidden>Choose Level</option>');
                select.append(defaultOption);

                res.forEach(level => {
                    const option = $(`<option ${level.id === LEVEL_ID ? 'selected': ''} value="${level.id}">Level <span class="text-primary">${level.name}</span></option>`);
                    select.append(option);
                });
            }
        })
    }
})