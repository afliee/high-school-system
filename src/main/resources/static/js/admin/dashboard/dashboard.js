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

function generatePaginationOptions(currentPage, totalPages, MAX_PAGE = 5) {
    let pagination = '';

    //  if totalPage > MAX_PAGE => show MAX_PAGE
    // else the distance between startPage and endPage is MAX_PAGE
    let startPage = 1;
    let endPage = totalPages;

    if (totalPages > MAX_PAGE) {
        const distance = Math.floor(MAX_PAGE / 2);
        startPage = currentPage - distance;
        endPage = currentPage + distance;

        if (startPage < 1) {
            endPage += Math.abs(startPage) + 1;
            startPage = 1;
        }

        if (endPage > totalPages) {
            startPage -= endPage - totalPages;
            endPage = totalPages;
        }
    }

    console.log("startPage", startPage);
    console.log("endPage", endPage);

    for (let i = startPage; i <= endPage; i++) {
        pagination += `
                <li class="page-item ${currentPage === i ? 'active' : ''}">
                    <span class="page-link">${i}</span>
                </li>
            `
    }

    return pagination;
}
