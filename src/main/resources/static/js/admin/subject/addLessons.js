$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');

    const SUBJECT_ID = $('#subjectId').val();

    const shifts = $('.shift');
    const btnSave = $('.btn-save');

    const days = [];
    const shiftsArray = [];
    shifts.on('click', function (e) {
        e.preventDefault();
        const shift = $(this);

        toggleActive(shift);

        const shiftId = shift.data('id');
        const dayId = shift.parent().data('id');

        if (shift.hasClass('active')) {
            shiftsArray.push(shiftId);
            dayId && days.push(dayId);
        } else {
            shiftsArray.splice(shiftsArray.indexOf(shiftId), 1);
            dayId && days.splice(days.indexOf(dayId), 1);
        }

        console.log(shiftsArray);
        console.log(days);
    })

    btnSave.on('click', function (e) {
        Swal.fire({
            title: 'Are you sure to save?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: "hsl(141, 53%, 53%)",
            cancelButtonColor: "hsl(348, 100%, 61%)",
            confirmButtonText: 'Yes, save it!'
        }).then((result) => {
            if (result.isConfirmed) {
                saveLessons({
                    days,
                    semesterId: $('#semesterId').val(),
                    subjectId: SUBJECT_ID,
                    shifts: shiftsArray
                });
            }
        })
    })


    function saveLessons(
        {
            days,
            semesterId,
            subjectId,
            shifts
        }
    ) {
        console.log({
            dayIds: days,
            semesterId: semesterId,
            subjectId: subjectId,
            shiftIds: shifts
        })
        let swal = null;
        $.ajax({
            url: '/api/v1/lessons/create',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            data: JSON.stringify({
                dayIds: days,
                semesterId: semesterId,
                subjectId: subjectId,
                shiftIds: shifts
            }),
            beforeSend: function () {
                swal = Swal.fire({
                    title: 'Saving...',
                    showConfirmButton: false,
                    willOpen: () => {
                        Swal.showLoading()
                    },
                })
            },
            success: function (data) {
                console.log(data);
                swal.close();
                Swal.fire({
                    icon: 'success',
                    title: 'Save successfully!',
                    showConfirmButton: false,
                    timer: 1500
                })
                setTimeout(function () {
                    window.location.href = `/auth/admin/subjects/${SUBJECT_ID}/lessons/add?semesterId=${semesterId}`;
                }, 1500)
            },
            error: function (error) {
                console.log(error.responseJSON.message);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Something went wrong! Cause by: ' + error.responseJSON.message
                })
            }
        })
    }

    function toggleActive(shift) {
        if (shift.hasClass('active')) {
            shift.removeClass('active');
        } else {
            shift.addClass('active');
        }
    }
})