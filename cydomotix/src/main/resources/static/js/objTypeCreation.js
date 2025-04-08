// Charger les types de valeurs au chargement de la page
let valueTypes = [];

fetch('/gestion/object-type/valueTypes')
    .then(response => response.json())
    .then(data => {
        valueTypes = data;
    })
    .catch(error => console.error('Erreur lors du chargement des ValueTypes:', error));

function addRow() {
    var table = document.getElementById("attributesTable").getElementsByTagName('tbody')[0];
    var newRow = table.insertRow(table.rows.length);

    var cell1 = newRow.insertCell(0);
    var cell2 = newRow.insertCell(1);
    var cell3 = newRow.insertCell(2);

    var rowIndex = table.rows.length - 1;

    cell1.innerHTML = '<input type="text" name="attributes[' + rowIndex + '].name" required>';

    // Création dynamique du <select> des ValueTypes
    let select = document.createElement("select");
    select.name = `attributes[${rowIndex}].valueType`;

    valueTypes.forEach(valueType => {
        let option = document.createElement("option");
        option.value = valueType.name; // La valeur (renseignée dans le champ name du json) qui sera envoyée au serveur avec le formulaire
        option.textContent = valueType.displayName; // Le texte affiché dans le select côté utilisateur
        select.appendChild(option);
    });

    cell2.appendChild(select);

    cell3.innerHTML = '<button type="button" onclick="removeRow(this)">Supprimer</button>';
}

function removeRow(button) {
    var row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
}

document.addEventListener("DOMContentLoaded", function () {
    // Sélectionner tous les boutons "Supprimer"
    const deleteButtons = document.querySelectorAll('.delete-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Empêche le comportement par défaut (le lien qui se déclenche)

            const objTypeId = this.getAttribute('data-id'); // Récupère l'ID du Type d'objet

            // Confirmation avant de supprimer
            const confirmation = confirm("Supprimer ce Type d'objet ?");
            if (confirmation) {
                // Si confirmé, redirige vers l'URL de suppression
                window.location.href = `object-type/delete/${objTypeId}`;
            }
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

            const objTypeId = this.getAttribute('data-id');
            const reason = prompt("Veuillez indiquer la raison de la demande de suppression :");

            if (reason !== null && reason.trim() !== "") {
                fetch(`/gestion/object-type/${objTypeId}/request-deletion`, {
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