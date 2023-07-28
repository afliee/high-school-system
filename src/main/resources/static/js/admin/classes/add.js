const TOKEN = getCookie('token') || localStorage.getItem('token');

$(document).ready(function () {
    const classes = $('#classes');
    const semesters = $('#semesters');

    fetchClass('', addClass);

    semesters.on('change', function (e) {
    //     get value of selected option
        let semesterId = $(this).val();
        fetchClass(semesterId, addClass);
    })

    function addClass(res) {
        console.log(res)
        let html = '';
        if (res.content.length === 0) {
            html = `
                <div class="d-flex justify-content-center align-items-center col-12">
                    <img src="/images/nodata.gif" class="img-fluid|img-thumbnail|rounded|rounded-circle|rounded-top|rounded-right|rounded-bottom|rounded-left" alt="">
                </div>
            `;
            classes.html(html);
            return;
        }
        res.content.forEach(function (classItem) {
            let r = Math.floor(Math.random() * 255);
            let g = Math.floor(Math.random() * 255);
            let b = Math.floor(Math.random() * 255);
            html += `
                <div class="col-md-4 col-sm-6 col-xs-12 my-3" >
                    <div class="card class" style="border-right-color: rgb(${r}, ${g}, ${b})" data-id="${classItem.id}">
                        <div class="card-body">
                            <h3 class="name">${classItem.name}</h3>
                            <h5 class="chairman text-truncate" data-bs-toggle="tooltip" title="${classItem.chairman}">${classItem.chairman}</h5>
                            <div class="info d-flex justify-content-between align-items-center my-3">
                                <span class="create-at">${new Date(classItem.createdDate).toLocaleDateString('en-GB')}</span>
                                <span class="present">${classItem.present} Students</span>
                            </div>
                            <div class="students">  
                                ${classItem.students.length ? classItem.students.map(function (student) {
                return `
                                        <div class="student-avatar avatar">
                                            <img src="${student.avatar || '/images/1.jpg'}" alt="${student.name}">
                                        </div>
                                    `
            }).join('') : `
                                    <div class="student-avatar avatar bg-primary">
                                        <span class="avatar-content"><i class="bi bi-three-dots"></i></span>
                                    </div>
                                `}
                                
                                ${classItem.present > 3 ? `
                                    <div class="student-avatar avatar bg-dark">
                                        <span class="avatar-content"><i class="bi bi-three-dots"></i></span>
                                    </div>
                                ` : ``}
                            </div>
                        </div>
                    </div>
                </div>
            `;
        })

        // classes.html(html);
    //     smooth animation render html
        classes.fadeOut(300, function () {
            classes.html(html);
            registerEvent();
            classes.fadeIn(150);
        })
    }


    function fetchClass(semesterId ,callback) {
        $.ajax({
            url: '/api/v1/class/get' + (semesterId ? `?semesterId=${semesterId}` : ``),
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (res) {
                callback && callback(res);
            },
            error: function (err) {
                console.log(err);
            }
        })
    }

    function registerEvent() {
        const classItem = $('.class');
        classItem.on('click', function (e) {
            let classId = $(this).closest('.class').data('id');
            window.location.href = `/auth/admin/classes/${classId}`;
        })
    }
})
