$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const btnDeletes = $(".btn-delete");

    btnDeletes.on("click", function () {
        const btnDelete = $(this);
        const id = $(this).data("id");

        let swal = null;

        Swal.fire({
            icon: 'warning',
            title: 'Are your sure delete this fault detail?',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            cancelButtonText: 'No',
            confirmButtonColor: '#3085d6',
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `/api/v1/fault/delete/${id}`,
                    method: "DELETE",
                    headers: {
                        "Authorization": `Bearer ${TOKEN}`
                    },
                    beforeSend: function () {
                        swal = Swal.fire({
                            title: "Please wait",
                            didOpen: () => {
                                Swal.showLoading();
                            },
                        });
                    },
                    success: function (data) {
                        swal.close();
                        Swal.fire({
                            icon: 'success',
                            title: 'Delete fault detail successfully',
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            btnDelete.closest(".faultDetail").remove();
                            if (btnDelete.closest(".faultDetails").find(".faultDetail").length === 0) {
                                btnDelete.closest(".faultDetails").remove();
                            }

                            if ($(".faultDetails").length === 0) {
                                location.reload();
                            }
                        })
                    },
                    error: function (error) {
                        swal.close();
                        Swal.fire({
                            icon: 'error',
                            title: 'Delete fault detail fail',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    }
                })
            }
        })
    })
})