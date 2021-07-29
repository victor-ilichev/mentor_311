$(function(){
    let $modal = $('#confirmModal'),
        $modalBody = $modal.find('.modal-body'),
        $modalLabel = $modal.find('#exampleModalLabel'),
        $confirmButton = $modal.find('.modal-footer .confirm');

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
        let dataId = $(event.target).closest('tr').data('id');
        let url = '';

        if ('edit' === jqueryEvent.relatedTarget.getAttribute('data-type')) {
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

        getFrom(url)
            .then(response => {
                let $response = $(response);
                $form = $response.find('form');

                if ('delete' === jqueryEvent.relatedTarget.getAttribute('data-type')) {
                    $form.find('input').attr('readonly', 'readonly');
                    $form.find('select').attr('disabled', 'disabled');
                }

                $(this).find('.modal-body').html($response)
            })
        ;
    })
    .on('hide.bs.modal', function() {
        $modalBody.empty();
        $form = null;
    });
});

async function getFrom(url) {
    let response = await fetch(url);

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        return await response.text();
    } else {
        alert("Ошибка HTTP: " + response.status);
    }
}