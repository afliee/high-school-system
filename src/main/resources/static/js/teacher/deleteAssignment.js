$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');

    const btnDeletes = $('.btn-delete');

    btnDeletes.on('click', function () {
        const id = $(this).data('id');

        let swal = null;

        Swal.fire({
            icon: 'warning',
            title: 'Are you sure?',
            text: 'You will not be able to recover this assignment!',
            showCancelButton: true,
            confirmButtonText: 'Yes, delete it!',
        }).then(result  => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `/api/v1/assignment/delete/${id}`,
                    type: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${TOKEN}`
                    },
                    beforeSend: function () {
                        swal = Swal.fire({
                            title: 'Please wait ...',
                            didOpen: () => {
                                Swal.showLoading()
                            },
                        })
                    },
                    success: function (data) {
                        swal.close();
                        Swal.fire({
                            icon: 'success',
                            title: 'Deleted!',
                            text: 'Your assignment has been deleted.',
                            showConfirmButton: false,
                            timer: 1500
                        }).then(() => {
                            window.location.reload();
                        })
                    },
                    error: function (err) {
                        console.log(err.responseText)
                        swal.close();
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: 'Something went wrong!',
                            timer: 1500
                        })
                    }
                })
            }
        })
    })
})