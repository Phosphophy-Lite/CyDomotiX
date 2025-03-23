function loadAttributes(objectTypeId) {
    let attributeTableBody = document.querySelector("#attributesTable tbody");
    attributeTableBody.innerHTML = ""; // Vider les lignes actuelles pour reload

    fetch(`/admin/connected-object/attributes?typeId=` + objectTypeId) // envoie une requête HTTP GET à cette url
        .then(response => response.json()) // récupère la réponse de la requête en JSON
        .then(data => {
            data.forEach((attribute, index) => {
                let row = document.createElement("tr");
                row.innerHTML = `
                    <td>${attribute.name}</td>
                    <td><input type="text" name="attributeValueList[${index}].string_value" required></td>
                    <input type="hidden" name="attributeValueList[${index}].objectAttribute" value="${attribute.id}">
                `;

                attributeTableBody.appendChild(row);
            });

        });

}

document.addEventListener("DOMContentLoaded", function() {
    // Sélectionner tous les boutons "Supprimer"
    const deleteButtons = document.querySelectorAll('.delete-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche le comportement par défaut (le lien qui se déclenche)

            const connectedObjId = this.getAttribute('data-id'); // Récupère l'ID de l'objet connecté

            // Confirmation avant de supprimer
            const confirmation = confirm("Supprimer cet object connecté ?");
            if (confirmation) {
                // Si confirmé, redirige vers l'URL de suppression
                window.location.href = `connected-object/delete/${connectedObjId}`;
            }
        });
    });
});