const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
});

function filter() {
    let form = $("#filter");
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: form.serialize()
    }).done(function (data) {
        datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    })
}

function callDateTimePicker() {
    $( "#dateTime" ).datetimepicker({
        format: 'Y-m-d H:i:s'
    });
}

function callDatePicker() {
    $( "#startDate, #endDate" ).datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });
}

function callTimePicker() {
    $( "#startTime, #endTime" ).datetimepicker({
        datepicker:false,
        format:'H:i'
    });
}
function clearFilter() {
    updateTable();
}