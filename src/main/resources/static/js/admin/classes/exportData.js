$(document).ready(function () {
    const TOKEN = getCookie('token') || localStorage.getItem('token');
    const CLASS_ID = $("#classId").val();

    const URL = `/api/v1/class/download/${CLASS_ID}`;

    const btnExport = $('.btn-export');

    let swal = null;

    btnExport.on('click', function (e) {
        e.preventDefault();
        $.ajax({
            url: URL,
            method: 'GET',
            headers : {
                'Authorization': `Bearer ${TOKEN}`
            },
            beforeSend: function () {
                btnExport.attr('disabled', true);
                swal = Swal.fire({
                    title: 'Data exporting...',
                    html: 'Please wait a second',
                    didOpen: () => {
                        Swal.showLoading()
                    }
                });
            },
            xhrFields:{
                responseType: 'blob'
            },
            success: function (res, status, xhr) {
                swal.close();
                btnExport.attr('disabled', false);
                let filename = "";
                let disposition = xhr.getResponseHeader('Content-Disposition');

                if (disposition) {
                    let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    let matches = filenameRegex.exec(disposition);
                    if (matches !== null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                }
                
                const downloadUrl = window.URL.createObjectURL(res);
                const a = document.createElement('a');
                a.href = downloadUrl;
                a.download = filename;
                document.body.appendChild(a);
                a.click();
            },
            error: function (err) {
                console.log(err.responseJSON.message);
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: err.responseJSON.message
                });
            }
        })

    })
})