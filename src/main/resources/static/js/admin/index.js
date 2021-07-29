$(document).ready(function(){
    let $modal = $('#confirmModal'),
        $modalBody = $modal.find('.modal-body'),
        $modalLabel = $modal.find('#exampleModalLabel'),
        $confirmButton = $modal.find('.modal-footer .confirm'),
        $usersTableContainer = $('#js-users-table-container'),
        $newUserContainer = $('#js-new-user-container')
    ;

    freshUsers();

    var $form = null;

    $confirmButton.on('click', function() {
        if (null != $form) {
            $form.submit();
        }
    });

    $modal.modal({
        keyboard: true,
        backdrop: "static",
        show:false,

    })
    .on('show.bs.modal', function(jqueryEvent) {
        // console.log();
        let dataId = $(event.target).closest('tr').data('id'),
            url = '',
            isEdit = 'edit' === jqueryEvent.relatedTarget.getAttribute('data-type'),
            isDelete = 'delete' === jqueryEvent.relatedTarget.getAttribute('data-type')
        ;

        if (isEdit) {
            $modalLabel.text('Edit User');
            if ($confirmButton.hasClass('btn-danger')) {
                $confirmButton
                    .removeClass('btn-danger')
                    .addClass('btn-primary')
                    .text('Edit');
            }

            url = '/admin/' + dataId + '/edit';
        } else {
            $modalLabel.text('Delete User');
            if ($confirmButton.hasClass('btn-primary')) {
                $confirmButton
                    .removeClass('btn-primary')
                    .addClass('btn-danger')
                    .text('Delete');
            }

            url = '/admin/' + dataId + '/edit?disabled=1';
        }

        getHtmlFrom(url)
            .then(response => {
                let $response = $(response);
                $form = $response.find('form');

                $form.on('submit', function (event) {
                    event.preventDefault();

                    fetch('/admin/' + dataId, {
                        method: isEdit ? 'PATCH' : 'DELETE',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: isEdit ? $form.serialize() : {}
                    }).then(result => {
                        console.log(result);
                        if (result.status === 200) {
                            $modal.modal('hide');
                            freshUsers();
                        }

                        return false;
                    }).catch(error => {
                        console.log(error);
                    });


                    return false;
                });
                $(this).find('.modal-body').html($response)
            })
        ;
    })
    .on('hide.bs.modal', function() {
        $modalBody.empty();
        $form = null;
    });

    // $('#nav-tab a#nav-home-tab').on('click', function (event) {
    //     event.preventDefault()
    //     // $(this).tab('show')
    //     getHtmlFrom('/api/v1/users/list')
    //         .then(response => {
    //             $usersTableContainer.html($(response));
    //         });
    // })
    // show.bs.tab

    $('#nav-tab a#nav-home-tab').on('show.bs.tab', function (event) {
        // event.preventDefault()
        // $(this).tab('show')
        freshUsers();
    });

    $('#nav-tab a#nav-new-user-tab').on('show.bs.tab', function (event) {
        getHtmlFrom('/api/v1/users/new-user-form')
            .then(response => {
                let $response = $(response);
                $newUserContainer.html($response);

                $response.on('submit', function (event) {
                    event.preventDefault();

                    fetch('/admin', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: $response.serialize()
                    }).then(result => {
                        //console.log(result);
                        if (result.status === 200) {
                            $('#nav-home-tab').tab('show');
                        }

                        return false;
                    }).catch(error => {
                        console.log(error);
                    });


                    return false;
                });

            });
    });

    function freshUsers() {
        getHtmlFrom('/api/v1/users/list')
            .then(response => {
                $usersTableContainer.html($(response));
            });
    }
});

async function getHtmlFrom(url) {
    let response = await fetch(url);

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        return await response.text();
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
}

function getFormData($form){
    // var formData  = new FormData();

    // for(const name in data) {
    //     formData.append(name, data[name]);
    // }
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    // formData.append('foo', 'bar');

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
        // console.log(n['name'], n['value']);
        // formData.append(n['name'], n['value']);
    });

    // for(const name in unindexed_array) {
    //     formData.append(name, unindexed_array[name]);
    // }

    console.log(indexed_array);
    // console.log(Array.from(formData));
    return indexed_array;
}