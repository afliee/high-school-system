$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");
    const classes = $(".classes");

    const semesterSelection = $("#select_semester");
    const levelSelection = $("#select_level");

    fetchAllSemester(TOKEN, semesterSelection);
    fetchAllLevel(TOKEN, levelSelection);

    semesterSelection.change(function () {
        const semesterId = $(this).val();
        const levelId = levelSelection.val();
        if (semesterId && levelId) {
            fetchClasses({
                token: TOKEN,
                levelId,
                semesterId,
                size: 12
            }, generateClasses)
        }
    })

    levelSelection.change(function () {
        const semesterId = semesterSelection.val();
        const levelId = $(this).val();
        if (semesterId && levelId) {
            fetchClasses({
                token: TOKEN,
                levelId,
                semesterId,
                size: 12
            }, generateClasses)
        }
    })

    function generateClasses(response) {
        console.log(response);
        if (response.content.length === 0) {
            classes.html(`
            <div class="col-12 d-flex flex-column justify-content-center align-items-center">
                    <h5 class="text-title-small">Select <strong class="text-dark">Semester</strong> and <strong class="text-dark">Level</strong> for get all classes</h5>
                    <img src="/images/nodata.gif"
                         class="img-fluid|img-thumbnail|rounded|rounded-circle|rounded-top|rounded-right|rounded-bottom|rounded-left"
                         alt="">
                </div>
            `);
            pagination.empty();
            return;
        }
        classes.empty();
        const {number, totalPages} = response;

        const paginationLinks = generatePaginationOptions(number + 1, totalPages, 10);
        const pagination = classes.parent().find(".pagination");
        pagination.empty();
        pagination.append(paginationLinks);
        registerPaginationClick(pagination)
        response.content.forEach((item, index) => {
            const classItem = $(`
                        <div class="col-12 col-md-3 pe-2 mt-2 ">
                            <div class="class-item class border-end rounded-2 p-2 shadow ${!item.hasSchedule ? ' opacity-50 disable' : ''}" data-id="${item.id}">
                                <h5 class="text-title-small">${item.name}</h5>
                                <i><strong>Teach by: </strong></i>
                                <span>${item.chairman ? item.chairman : `No Range`}</span>
                            </div>
                        </div>
                    `);
            classes.append(classItem);
        })

        registerClassClick();
    }

    function registerPaginationClick(paginationElement) {
        const paginationItems = paginationElement.find(".pagination-item");
        paginationItems.on("click", function () {
            if ($(this).hasClass("active")) {
                return;
            }
            paginationItems.removeClass("active");
            $(this).addClass("active");
            const page = $(this).data("page");
            const levelId = levelSelection.val();
            const semesterId = semesterSelection.val();

            fetchClasses({
                levelId,
                semesterId,
                page
            }, generateClasses);
        })
    }

    function registerClassClick() {
        const classItems = $(".class-item");
        const semesterId = semesterSelection.val();
        classItems.on("click", function () {
            if ($(this).hasClass("disable")) {
                return;
            }
            const classId = $(this).data("id");
            window.location.href = `/auth/admin/review/${classId}?semesterId=${semesterId}`;
        })
    }
})