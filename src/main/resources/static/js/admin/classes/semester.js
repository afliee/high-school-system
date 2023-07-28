$(document).ready(function () {
    const semesters = $('#semesters');
    fetchAllSemesters(function (res) {
        let html = '';
        // get current semester have isCurrent = true
        let currentSemester = res.find(function (semester) {
            return semester.current;
        });
        // remove current semester from res
        res = res.filter(function (semester) {
            return !semester.current;
        });
        html += `
            <option value="${currentSemester.id}">${currentSemester.name}</option>
        `
        res.forEach(function (semester) {
            html += `
                <option value="${semester.id}">${semester.name}</option>
            `
        })
        semesters.html(html);
    });


    function fetchAllSemesters(callback) {
        $.ajax({
            url: '/api/v1/semester',
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
})