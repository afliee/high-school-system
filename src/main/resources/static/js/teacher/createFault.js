$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const ID = $('#id').val();
    const SUBJECT_ID = $('#subject_id').val();

    const btnSave = $('.btn-save');

    btnSave.on('click', function () {
        const faultSelect = $('#select_fault');

        const faultSelectValue = faultSelect.val();

        if (!faultSelectValue) {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Please select a fault',
            })
            return;
        }

        if (faultSelectValue.includes('OTHER')) {
            const faultInput = $('#other_fault');
            const faultInputValue = faultInput.val();
            if (!faultInputValue) {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Please input a fault',
                })
                return;
            }
        }

        const data = {
            studentId: ID,
            subjectId: SUBJECT_ID,
            faults: faultSelectValue,
            otherFault: $('#other_fault').val()
        }

        $.ajax({
            url: `/api/v1/fault/create`,
            type: 'POST',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify(data),
            contentType: 'application/json',
            beforeSend: function () {
                btnSave.prop('disabled', true);
            //     load spinner
                btnSave.html(`
                    <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    Loading...
                `)
            },
            success: function (data) {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'Create fault success',
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = `/teacher/enroll/${SUBJECT_ID}`;
                    }
                })
            },
            error: function (err) {
                btnSave.prop('disabled', false);
                btnSave.html('Save');
                console.log(err.responseText)
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Create fault fail',
                    timer: 2000
                })
            }
        })
    })
})