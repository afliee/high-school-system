$(document).ready(function () {
    const TOKEN = localStorage.getItem("token");
    let stompClient = null;

    if (TOKEN == null) {
        window.location.href = "/auth/admin/login";
    }

    const navigators = $(".navigators");
    fetchAllNavigator({page: 0, size: 10})

    function fetchAllNavigator(
        {
            page = 0,
            size = 10,
        }
    ) {
        $.ajax({
            url: `/api/navigator/get?page=${page}&size=${size}`,
            type: "GET",
            headers: {
                'Authorization': `Bearer ${TOKEN}`
            },
            success: function (data) {
                navigators.empty();

                const {number, totalPages} = data;
                const pagination = $(".pagination");
                pagination.empty();
                const paginationOptions = generatePaginationOptions(number + 1, totalPages);
                pagination.append(paginationOptions);
                registerPaginationClick();

                if (data.content.length === 0) {
                    navigators.append(`
                        <tr>
                            <td colspan="6" class="text-center">No data</td>
                        </tr>
                    `)
                    pagination.hide();
                    return;
                }

                data.content.forEach(navigator => {
                    const {student, status} = navigator;
                    navigators.append(`
                        <tr class="navigator" data-id="${navigator.id}" data-user-id="${student.id}">
                            <td class="text-center">${student.name}</td>
                            <td class="text-center">${student.fullName}</td>
                            <td class="text-center">${student.email}</td>
                            <td class="text-center">
                                <img src="${student.avatar ? student.avatar : '/images/1.jpg'}" alt="${student.name} avatar" title="${student.name} avatar"
                                         class="rounded-circle img-fluid avatar" style="width: 50px;"/>
                            </td>
                            <td class="text-center">${generateBadgeStatus(status)}</td>
                            <td>
                                <div class="btn-group">
                                    ${generateAction(status)}
                                </div>
                            </td>
                        </tr>
                    `)
                });

                registerActionClick();
            },
            error: function (e) {
                console.log(e.responseJSON);
            }
        })
    }

    function registerPaginationClick() {
        const paginationItems = $(".page-item");
        paginationItems.click(function () {
            const page = $(this).attr("page");
            fetchAllNavigator({page: page - 1, size: 10});
        })
    }

    function generateBadgeStatus(navigatorStatus){
        switch (navigatorStatus) {
            case 0: {
                return `<span class="badge badge-warning">Submitted</span>`;
            }
            case 1: {
                //     approved
                return `<span class="badge badge-success">Approved</span>`;
            }
            case 2: {
                //     rejected
                return `<span class="badge badge-danger">Rejected</span>`;
            }
            case 5: {
                // expired
                return `<span class="badge badge-secondary">Expired</span>`;
            }
        }
    }

    function generateAction(status) {
        switch (status) {
            case 0: {
                return `
                    <a href="#" class="btn btn-success btn-action" data-action="1">Approve</a>
                    <a href="#" class="btn btn-danger btn-action" data-action="2">Reject</a>
                `;
            }
            case 1: {
                return `
                    <a href="#" class="btn btn-danger btn-action" data-action="2">Reject</a>
                `;
            }
            case 2: {
                return `
                    <a href="#" class="btn btn-success btn-action" data-action="1">Approve</a>
                `;
            }
            case 5: {
                return `
                    <a href="#" class="btn btn-success btn-action" data-action="1">Approve</a>
                    <a href="#" class="btn btn-danger btn-action" data-action="2">Reject</a>
                `;
            }
        }
    }

    function registerActionClick() {
        const btnActions = $(".btn-action");

        btnActions.click(function () {
            const btnAction = $(this);
            if (stompClient != null && stompClient.connected) {
                const action = btnAction.data("action")

                const navigatorId = btnAction.closest(".navigator").data("id");
                const userId = btnAction.closest(".navigator").data("user-id");
                stompClient.send(`/app/navigator/do-check/${userId}`, {}, JSON.stringify({
                    id: navigatorId,
                    status: action
                }))
                const pageItem = $(".page-item.active");
                fetchAllNavigator({page: parseInt(pageItem.data("page")) - 1, size: 10})
            }
        })
    }

    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/register', function (data) {
                console.log(data);
                fetchAllNavigator({page: 0, size: 10})
            })
        })
    }



    connect();
})