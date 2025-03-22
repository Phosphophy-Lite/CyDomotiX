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
                    <td><input type="text" th:field="*{attributeValueList[${index}].value" required></td>
                `;

                console.log(`${attribute.name}`);

                attributeTableBody.appendChild(row);
            });

        });

}