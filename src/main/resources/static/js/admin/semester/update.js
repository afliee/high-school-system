$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const ID = $("#id").val();

    const btnSave = $(".btn-save");

    const txtSemesterName = $("#semesterName");
    const startDate = $("#startDate");
    const endDate = $("#endDate");

    btnSave.on('click', function () {
        const startDateValue = startDate.val();
        const endDateValue = endDate.val();
        const semesterName  = txtSemesterName.val();

        const data = {
            name: semesterName,
            startDate: startDateValue,
            endDate: endDateValue
        }
        console.log("data", data)
        let swal = Swal.mixin({
            toast: true,
            position: "center",
            showConfirmButton: false,
            timer: 2000
        })

        $.ajax({
            url: `/api/v1/semester/update/${ID}`,
            type: 'PUT',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify(data),
            beforeSend: function () {
                btnSave.html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Loading...`);
                //     open alert loading
                swal = Swal.fire({
                    title: 'Loading...',
                    html: 'Please wait a second',
                    didOpen: () => {
                        Swal.showLoading()
                    }
                })
            },
            success: function (data) {
                btnSave.html(`Save`);
                swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Add semester successfully!',
                    text: 'You can add more semester or close this dialog',
                    showConfirmButton: false,
                    timer: 1500
                })
                setTimeout(() => {
                    window.location.href = "/auth/admin/semester";
                }, 1500);
            },
            error: function (data) {
                console.log(data.responseText)
                btnSave.html(`Save`);
                swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Add semester failed!',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    })
})