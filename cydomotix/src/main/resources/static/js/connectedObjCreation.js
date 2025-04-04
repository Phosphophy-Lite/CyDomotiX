function loadAttributes(objectTypeId) {
    let attributeTableBody = document.querySelector("#attributesTable tbody");
    attributeTableBody.innerHTML = ""; // Vider les lignes actuelles pour reload

    fetch(`/admin/connected-object/attributes?typeId=` + objectTypeId) // envoie une requête HTTP GET à cette url
        .then(response => response.json()) // récupère la réponse de la requête en JSON
        .then(data => {
            data.forEach((attribute, index) => {
                let row = document.createElement("tr");
                let inputField = ConditionalInputField(attribute.valueType, index);

                row.innerHTML = `
                    <td>${attribute.name}</td>
                    <td>${inputField}</td>
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

function ConditionalInputField(valueType, index){
    let inputField = "";
    switch(valueType){
        case "STRING":
            inputField = `<input type="text" name="attributeValueList[${index}].string_value" required>`;
            break;
        case "INTEGER":
            inputField = `<input type="number" name="attributeValueList[${index}].int_value" required>`;
            break;
        case "DOUBLE":
            inputField = `<input type="number" step="0.01" name="attributeValueList[${index}].double_value" required>`;
            break;
        case "TEMPERATURE":
            inputField = `<input type="number" min="-100" max="100" name="attributeValueList[${index}].int_value" required><span> °C</span>`;
            break;
        case "HOURS":
            inputField = `<input type="number" min="0" name="attributeValueList[${index}].int_value" required><span> h</span>`;
            break;
        case "MINUTES":
            inputField = `<input type="number" min="0" name="attributeValueList[${index}].int_value" required><span> min</span>`;
            break;
        case "SECONDS":
            inputField = `<input type="number" min="0" name="attributeValueList[${index}].int_value" required><span> s</span>`;
            break;
        case "PERCENTAGE":
            inputField = `<input type="number" min="0" max="100" name="attributeValueList[${index}].int_value" required><span> %</span>`;
            break;
    }

    return inputField;
}

document.addEventListener("DOMContentLoaded", function() {
    // Sélectionner tous les boutons "Activer/Désactiver"
    const powerBtn = document.querySelectorAll('.power-btn');

    powerBtn.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault(); // Empêche le comportement par défaut (le lien qui se déclenche)

            const connectedObjId = this.getAttribute('data-id'); // Récupère l'ID de l'objet connecté
            window.location.href = `connected-object/status/${connectedObjId}`; // Changer son status
        });
    });
});