$(document).ready(function (){
    $('table #listOrderButton').on('click', function (event){
        event.preventDefault();
        let href = $(this).attr('href');
        $.get(href, function (p, status){
            $('#idEdit').val(p.id);
            $('#nameEdit').val(p.nom);
        });
        $('#listOrderModal').modal();
    });
});


// document.addEventListener('DOMContentLoaded', function() {
//     let editButtons = document.querySelectorAll('table #listOrderButton');
//
//     for (let i = 0; i < editButtons.length; i++) {
//         editButtons[i].addEventListener('click', function(event) {
//             event.preventDefault();
//
//             let href = this.getAttribute('href');
//             let xhr = new XMLHttpRequest();
//
//             xhr.onreadystatechange = function() {
//                 if (xhr.readyState === 4 && xhr.status === 200) {
//                     let category = JSON.parse(xhr.responseText);
//
//                     document.querySelector('idEdit').value = category.id;
//                     document.querySelector('nameEdit').value = category.nom;
//
//                     document.querySelector('listOrderModal').classList.add('modal');
//                 }
//             };
//
//             xhr.open('GET', href);
//             xhr.send();
//         });
//     }
// });