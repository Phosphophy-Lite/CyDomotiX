document.addEventListener("DOMContentLoaded", function() {

    const deleteBtn = document.querySelector('.delete-btn');
    let connectedObjId = deleteBtn.getAttribute("data-id");

    if(deleteBtn){ // Si le bouton existe (n'existe pas si l'utilisateur n'est pas admin)
        deleteBtn.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche le comportement par défaut (le lien qui se déclenche)

            // Confirmation avant de supprimer
            const confirmation = confirm("Supprimer cet object connecté ?");
            if (confirmation) {
                // Si confirmé, redirige vers l'URL de suppression
                window.location.href = `/object/${connectedObjId}/delete`;
            }
        });
    }

});