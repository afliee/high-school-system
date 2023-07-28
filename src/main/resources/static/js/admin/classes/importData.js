$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

//     call API
    const selectChairman = $("#select_chairman");
    const selectSemester = $("#select_semester");
    const selectLevel = $("#select_level");
    const selectFile = $("#students");
    const btnImport = $(".btn-import");
    const className = $("#name");
    const modal = $("#addClass");
    function fetchAllChairman() {
        $.ajax({
            url: '/api/v1/teacher/all',
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                console.log(res);

                const defaultOption = $('<option value="" selected disabled hidden>Choose Chairman</option>');
                selectChairman.append(defaultOption);

                res.forEach(teacher => {
                    const option = $(`<option value="${teacher.id}">${teacher.fullName}</option>`);
                    selectChairman.append(option);
                });
            },
            error: function (err) {
                console.log(err.responseText);
            }
        })
    }

    function fetchAllLevel() {
        $.ajax({
            url: `/api/v1/level/all?isDetail=true`,
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                console.log(res);

                const defaultOption = $('<option value="" selected disabled hidden>Choose Level</option>');
                selectLevel.append(defaultOption);

                res.forEach(level => {
                    const option = $(`<option value="${level.id}">Level <span class="text-primary">${level.name}</span></option>`);
                    selectLevel.append(option);
                });
            },
            error: function (err) {
                console.log(err.responseJSON);
                console.log(err.responseJSON)
            }
        })
    }

    function fetchAllSemester() {
        $.ajax({
            url: '/api/v1/semester',
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                console.log(res);

                const defaultOption = $('<option value="" selected disabled hidden>Choose Semester</option>');
                selectSemester.append(defaultOption);

                res.forEach(semester => {
                    const option = $(`<option value="${semester.id}">Semester <span class="text-primary">${semester.name}</span></option>`);
                    selectSemester.append(option);
                });
            },
            error: function (err) {
                console.log(err.responseText);
            }
        })
    }

    fetchAllLevel();
    fetchAllChairman();
    fetchAllSemester();

    btnImport.on('click', function () {
        const formData = new FormData();
        formData.append("students", selectFile[0].files[0]);
        formData.append("chairman", selectChairman.val());
        formData.append("semesterId", selectSemester.val());
        formData.append("levelId", selectLevel.val());
        formData.append("name", className.val());

        $.ajax({
            url: '/api/v1/class/add',
            method: 'POST',
            mimeType: "multipart/form-data",
            contentType: false,
            processData: false,
            data: formData,
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                btnImport.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`);
            },
            success: function (res) {
                console.log(res);
                modal.modal('hide');
                Swal.fire({
                    icon: 'success',
                    title: 'Add class success',
                    text: 'Add class success',
                });
                btnImport.html(`Import`);

                setTimeout(function () {
                    location.reload();
                }, 2000);
            },
            error: function (err) {
                console.log(err.responseText);
                Swal.fire({
                    icon: 'error',
                    title: 'Add class error',
                    text: err.responseText,
                });
                btnImport.html(`Import`);
            }
        })
    })
})