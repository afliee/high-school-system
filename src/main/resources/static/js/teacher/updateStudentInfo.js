$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const btnUpdate = $('.btn-update');
    const avatarInput = $('#avatar');
    avatarInput.change(function (e) {
        const imageFile = avatarInput[0].files[0];
        const reader = new FileReader();
        reader.onload = function (e) {
            $(".avatar").attr('src', e.target.result);
        }
        reader.readAsDataURL(imageFile);
    });

    btnUpdate.click(function (e) {
        e.preventDefault();

        const id = $('.student').data('id');

        const name = $('#name').val();
        const email = $('#email').val();
        const phone = $('#phone').val();
        const cardId = $('#cardId').val();
        const location = $('#location').val();
        const birthday = $('#birthday').val();
        const gender = $('.gender').is(':checked') ? 0 : 1;
    //     avatar is input file
        let avatar = $('#avatar')[0].files[0];
        // check if img with class avatar has src then create temporary file to upload
        if ($('.avatar').attr('src').startsWith('/images/')) {
            avatar = null;
        }

        const formData = new FormData();
        formData.append('cardId', cardId);
        formData.append('name', name);
        formData.append('email', email);
        formData.append('phone', phone);
        formData.append('location', location);
        formData.append('birthday', birthday);
    //     append gender
        formData.append('gender', gender ? '1' : '0');
        avatar && formData.append('avatar', avatar);

        let swal = null;

        $.ajax({
            url: `/api/v1/student/${id}`,
            type: 'PUT',
            contentType: false,
            processData: false,
            enctype: 'multipart/form-data',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                swal = Swal.fire({
                    title: 'Saving...',
                    didOpen: () => {
                        Swal.showLoading()
                    },
                })
            },
            data: formData,
            success: function (data) {
                swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Update success',
                    showConfirmButton: false,
                    timer: 1500
                }).then(() => {
                    location.reload();
                })
            },
            error: function (err) {
                swal.close();
                Swal.fire({
                    icon: 'error',
                    title: 'Update fail',
                    showConfirmButton: false,
                    timer: 1500
                })
            }
        })
    });
})