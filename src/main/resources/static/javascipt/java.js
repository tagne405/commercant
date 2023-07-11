const header =document.querySelector("header");

window.addEventListener("scroll",function(){
    header.classList.toggle("sticky",this.window.scrollY >= 0);
})

// let menu=document.querySelector('#menu-icon');
// let navmenu=document.querySelector('.navmenu');

// menu.onclick=()=>{
//     menu.classList.toggle('fa-x');
//     navmenu.classList.toggle('open');

// }

// let dropdowns = document.querySelectorAll('.dropdown');
//
// dropdowns.forEach(function(dropdown) {
//     let menu = dropdown.querySelector('.dropdown-menu');
//     let toggle = dropdown.querySelector('.dropdown-toggle');
//
//     toggle.addEventListener('click', function(e) {
//         e.preventDefault();
//         menu.classList.toggle('show');
//     })
// })

// Récupère tous les éléments avec la classe 'dropdown-toggle'
var dropdownToggles = document.querySelectorAll('.dropdown-toggle');

// Ajoute un gestionnaire d'événements click à chaque élément
dropdownToggles.forEach(function(toggle) {
    toggle.addEventListener('click', function() {
// Récupère l'élément 'dropdown-menu' frère de l'élément cliqué et le toggle
        var menu = toggle.nextElementSibling;
        menu.style.display = (menu.style.display === 'block') ? '' : 'block';
    });
});

// Ajoute un gestionnaire d'événements click sur la page entière pour fermer le dropdown lorsque l'utilisateur clique ailleurs
document.addEventListener('click', function(event) {
// Vérifie si l'élément cliqué n'est pas un des toggles ou un enfant des toggles
    if (!event.target.matches('.dropdown-toggle') && !event.target.closest('.dropdown-toggle')) {
// Masque tous les éléments avec la classe 'dropdown-menu'
        document.querySelectorAll('.dropdown-menu').forEach(function(menu) {
            menu.style.display = '';
        });
    }
});