document.addEventListener("DOMContentLoaded", function() {

    const deleteBtn = document.querySelector('.delete-btn');
    let username = deleteBtn.getAttribute("data-id");

    if(deleteBtn){ // Si le bouton existe (n'existe pas si l'utilisateur n'est pas admin)
        deleteBtn.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche le comportement par défaut (le lien qui se déclenche)

            // Confirmation avant de supprimer
            const confirmation = confirm("Supprimer cet utilisateur ?");
            if (confirmation) {
                // Si confirmé, redirige vers l'URL de suppression
                window.location.href = `/profile/${username}/delete`;
            }
        });
    }
});