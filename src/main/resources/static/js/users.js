
var $editRow = null;
$(".edit").click(function () {
    $editRow = $(this).closest("tr");
    $(".userId").val($editRow.find(".uid").text());
    $(".userName").val($editRow.find(".uname").text());
    $(".userLastName").val($editRow.find(".ulast").text());
    $(".userAge").val($editRow.find(".uage").text());
    $(".userEmail").val($editRow.find(".uemail").text());
    var select = $('#selected');
    for (var i = 0; i < roles.length; i++) {
        if ($('#select option').length !== roles.length) {
            var o = new Option(roles[i].name, roles[i].id);
            select.append(o);
        }
    }
    $("#editUserModal").modal('show');
});
$(".del").click(function (e) {
    $editRow = $(this).closest("tr");
    $(".userId").val($editRow.find(".uid").text());
    $(".userName").val($editRow.find(".uname").text());
    $(".userLastName").val($editRow.find(".ulast").text());
    $(".userAge").val($editRow.find(".uage").text());
    $(".userEmail").val($editRow.find(".uemail").text());
    var select = $('#select');
    for (var i = 0; i < roles.length; i++) {
        if ($('#select option').length !== roles.length) {
            var o = new Option(roles[i].name, roles[i].id);
            select.append(o);
        }
    }
    $("#delUserModal").modal('show');
});