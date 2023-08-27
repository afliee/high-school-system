$(document).ready(function () {
    const TOKEN = getCookie("token") || localStorage.getItem("token");

    const btnSignOut = $(".dropdown-item.logout");
    console.log("btnSignOut", btnSignOut)
    btnSignOut.click(function () {
        console.log("btnSignOut clicked");
        $.ajax({
            url: "/api/v1/auth/logout",
            type: "POST",
            headers: {
                'Authorization': 'Bearer ' + TOKEN
            },
            success: function (data) {
                console.log("data", data);
                localStorage.removeItem("token");
                localStorage.removeItem("refreshToken");
                window.location.href = "/auth/admin/login";
            },
            error: function (error) {
                console.log("error", error);
            }
        })
    })
})