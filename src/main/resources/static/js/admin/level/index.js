$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const btnSave = $(".btn-save");
    const levelModal = $("#levelModal");
    console.log(btnSave)
    let swal = null;

    btnSave.on("click", function () {
        const levelName = $("#name").val();
        const levelNumber = $("#level_number").val();
        const smallError = $("#small-error");
        if (levelName === "" || levelNumber === "") {
            smallError.text("Please fill in all fields");
            return;
        }

        const data = {
            name: levelName,
            levelNumber: levelNumber
        }

        $.ajax({
            url: '/api/v1/level/create',
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify(data),
            contentType: 'application/json',
            beforeSend: function () {
                btnSave.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Saving...`)
            },
            success: function (res) {
                levelModal.modal('hide');
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Level created successfully',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    location.reload();
                })
            },
            error: function (err) {
                console.log(err.responseJSON.message)
                levelModal.modal('hide');
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Something went wrong',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    })
})