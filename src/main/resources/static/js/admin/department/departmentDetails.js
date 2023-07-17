$(document).ready(function () {
    const token = getCookie('token') || localStorage.getItem('token');
    const DEPARTMENT_ID = $("#departmentId").val();

    const departmentEditor = $("#editor");
    const btnAddSubject = $(".btn-add");

    const editor = CKEDITOR.replace(departmentEditor[0], {
        height: 500,
    });

    const departmentName = $("#department-name");
    const departmentContent = $(".department-content");

//     ======== CALL FUNCTION ========
    getDepartmentDetails();


//     ======== EVENT ========
    editor.on('change', function (evt) {
        // getData() returns CKEditor's HTML content.
        departmentContent.html(evt.editor.getData());
    });

    
//  editor on save
    CKEDITOR.instances.editor.on('save', function (event) {
        console.log(event.editor.getData());
        saveDepartment(event.editor.getData());
        return false;
    })

    btnAddSubject.on('click', function () {
        window.location.href = `/auth/admin/subjects/add?departmentId=${DEPARTMENT_ID}`;
    })

//    ======== FUNCTION ========
    function getDepartmentDetails() {

        $.ajax({
            url: `/api/v1/department/${DEPARTMENT_ID}`,
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token}`
            },
            success: function (response) {
                console.log("render department details", response)
                $(".subjects").html(renderSubjectBadges(response.subjects));
                departmentName.val(response.name);
                departmentContent.html(response.description);
                editor.setData(response.description);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }

    function saveDepartment(data) {
        $.ajax({
            url: `/api/v1/department/update/${DEPARTMENT_ID}`,
            method: "PUT",
            headers: {
                'Authorization': `Bearer ${token}`
            },
            contentType: "application/json",
            data: JSON.stringify({
                name: departmentName.val(),
                description: data
            }),
            success: function (response) {
                Toastify({
                    text: "Update department successfully",
                    backgroundColor: "#0DB473",
                    className: "info",
                    gravity: "top",
                    position: "right",
                    duration: 3000
                }).showToast();
            },
            error: function (error) {
                console.log(error);
                Toastify({
                    text: "Update department failed",
                    backgroundColor: "#FF9800",
                    className: "info",
                    gravity: "top",
                    position: "right",
                    duration: 3000
                }).showToast();
            }
        })
    }

    function renderSubjectBadges(subjects) {
        let html = '';
        subjects.forEach(subject => {
            html += `<span class="badge badge-pill" data-bs-toggle="tooltip" data-bs-placement="top" title="${subject.teacher.fullName}" style="color : ${subject.color}; border: 1px solid ${subject.color}" data-id="${subject.id}">${subject.name}</span>`
        })
        return html;
    }
})