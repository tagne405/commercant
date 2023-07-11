$(document).ready(function (){
    $('table #editButton').on('click', function (event){
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function (category, status){
            $('#idEdit').val(category.id);
            $('#nameEdit').val(category.nom);
        });
        $('#editModal').modal();
    });
});

// $(document).ready(function (){
//     $('#editButton').on('click', function (event){
//         event.preventDefault();
//         var href = $(this).attr('href');
//         $.get(href, function (category, status){
//             if(category && category.id && category.nom) {
//                 $('#idEdit').val(category.id);
//                 $('#nameEdit').val(category.nom);
//                 $('#editModal').modal();
//             }
//         });
//     });
// });

document.addEventListener('DOMContentLoaded', function() {
    var editButtons = document.querySelectorAll('table #editButton');

    for (var i = 0; i < editButtons.length; i++) {
        editButtons[i].addEventListener('click', function(event) {
            event.preventDefault();

            var href = this.getAttribute('href');
            var xhr = new XMLHttpRequest();

            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var category = JSON.parse(xhr.responseText);

                    document.querySelector('#idEdit').value = category.id;
                    document.querySelector('#nameEdit').value = category.nom;

                    document.querySelector('#editModal').classList.add('modal');
                }
            };

            xhr.open('GET', href);
            xhr.send();
        });
    }
});