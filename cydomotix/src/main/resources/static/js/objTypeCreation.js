function addRow() {
    var table = document.getElementById("attributesTable").getElementsByTagName('tbody')[0];
    var newRow = table.insertRow(table.rows.length);

    var cell1 = newRow.insertCell(0);
    var cell2 = newRow.insertCell(1);
    var cell3 = newRow.insertCell(2);

    var rowIndex = table.rows.length-1;

    cell1.innerHTML = '<input type="text" name="attributes[' + rowIndex + '].name" required>';
    cell2.innerHTML = '<input type="text" name="attributes[' + rowIndex + '].valueType" required>';
    cell3.innerHTML = '<button type="button" onclick="removeRow(this)">Supprimer</button>';
}

function removeRow(button) {
    var row = button.parentNode.parentNode;
    row.parentNode.removeChild(row);
}

document.addEventListener("DOMContentLoaded", function() {
    // Sélectionner tous les boutons "Supprimer"
    const deleteButtons = document.querySelectorAll('.delete-btn');

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
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
