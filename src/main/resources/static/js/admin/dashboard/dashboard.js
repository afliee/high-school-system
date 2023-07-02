$(document).ready(function () {
    const token = localStorage.getItem('token') || getCookie('token');
    const refreshToken = localStorage.getItem('refreshToken') || getCookie('refreshToken');

    if(!token || !refreshToken) {
        window.location.href = "/auth/admin/login";
    }

    console.log(token);

})
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if(parts.length === 2) return parts.pop().split(';').shift();
}
