$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const DEPARTMENT_ID = $("#departmentId").val();

    const container = $("#editor")
    const editor = CKEDITOR.replace(container[0]);

    const btnAddSubject = $("#addSubject");


    getAllTeacher();


//     ========== EVENT ==========
    editor.on('change', function (e) {
        const data = e.editor.getData();
        $(".description").html(data);
    })

    btnAddSubject.click(function (e) {
        const subjectName = $("#name").val();
        const description = $(".description").html();
        const teacherId = $("#teacher").val();

        const data = {
            name: subjectName,
            description: description,
            teacherId: teacherId,
            departmentId: DEPARTMENT_ID
        }

        addSubject(data);
    });

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
                    const option = $(`<option value="${teacher.id}">${teacher.fullName}</option>`);
                    select.append(option);
                })
            },
            error: function (err) {
                console.log(err);
            }
        });
    }

    function addSubject(data) {
        $.ajax({
            url: `/api/v1/subject/create`,
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (res) {
                console.log(res);
                window.location.href = `/auth/admin/department/${DEPARTMENT_ID}`;
            },
            error: function (err) {
                console.log(err);
                Toastify({
                    text: "Add Subject Failed",
                    backgroundColor: "#FFA500",
                    className: "toastify-error",
                    gravity: "top",
                    position: "right"
                }).showToast();
            }
        })
    }
})