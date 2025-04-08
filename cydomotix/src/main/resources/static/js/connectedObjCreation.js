function loadAttributes(objectTypeId) {
    let attributeTableBody = document.querySelector("#attributesTable tbody");
    attributeTableBody.innerHTML = ""; // Vider les lignes actuelles pour reload

    fetch(`/gestion/connected-object/attributes?typeId=` + objectTypeId) // envoie une requête HTTP GET à cette url
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

// Pour tous les boutons "Demander la suppression"
document.addEventListener("DOMContentLoaded", function () {
    const requestDeleteButtons = document.querySelectorAll('.request-delete-btn');

    // Récupération du token CSRF
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    requestDeleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault();

            const objId = this.getAttribute('data-id');
            const reason = prompt("Veuillez indiquer la raison de la demande de suppression :");

            if (reason !== null && reason.trim() !== "") {
                fetch(`/gestion/connected-object/${objId}/request-deletion`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        [csrfHeader]: csrfToken // Utilisation dynamique du nom de l'en-tête CSRF
                    },
                    body: new URLSearchParams({
                        reason: reason
                    })
                })
                    .then(response => {
                        if (response.ok) {
                            alert("Demande de suppression envoyée !");
                            location.reload();
                        } else {
                            alert("Erreur lors de l'envoi de la demande.");
                        }
                    })
                    .catch(error => {
                        console.error("Erreur réseau :", error);
                        alert("Erreur réseau !");
                    });
            } else {
                alert("La raison est obligatoire pour envoyer une demande.");
            }
        });
    });
});

