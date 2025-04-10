function formatDateTime(dateString) {
    const date = new Date(dateString);

    return date.toLocaleString("fr-FR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit"
    });
}

function loadConsumptionIntervals() {
    const selectedId = document.getElementById("objectSelect").value;
    const url = `/gestion/history/${selectedId}/intervals`;

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const consumptList = document.getElementById("consumpt-list");
            consumptList.innerHTML = ""; // effacer les entrées

            // Si aucune données
            if (data.length === 0) {
                const noDataMessage = document.createElement("div");
                noDataMessage.setAttribute("class", "no-data-message");
                noDataMessage.textContent = "Aucune donnée disponible pour cet objet.";
                consumptList.appendChild(noDataMessage);
                return;
            }

            data.forEach(interval => {
                const entry = document.createElement("div");
                const icon = document.createElement("i");
                entry.appendChild(icon);

                const description = document.createElement("div");
                description.setAttribute("class", "description");

                if(interval.status){
                    entry.setAttribute("class", "consumption-entry active");
                    icon.setAttribute("class", "fas fa-power-off icon");
                    description.textContent = "L'objet a été allumé.";
                } else{
                    entry.setAttribute("class", "consumption-entry inactive");
                    icon.setAttribute("class", "fas fa-ban icon");
                    description.textContent = "L'objet a été éteint.";
                }

                const content = document.createElement("div");
                content.setAttribute("class", "content");

                const date = document.createElement("div");
                date.setAttribute("class", "date");
                const startDate = formatDateTime(interval.start.replace("T", " "));
                date.textContent = "Du : " + startDate;
                const endDate = interval.end ? formatDateTime(interval.end.replace("T", " ")) : "-";
                date.textContent += " | Au : " + endDate;
                content.appendChild(date);

                content.append(description);

                const details = document.createElement("div");
                details.setAttribute("class", "details");

                const time = document.createElement("span");
                time.setAttribute("class", "time");
                time.textContent = interval.durationMinutes + " min";
                details.appendChild(time);

                const consumption = document.createElement("strong");
                consumption.setAttribute("class", "consumption");

                if (interval.energyWh >= 1000) { // Formatter en kWh
                    const kWh = (interval.energyWh / 1000).toFixed(2);
                    consumption.textContent = `${kWh} kWh`;
                } else { // Afficher en Wh
                    consumption.textContent = `${interval.energyWh.toFixed(2)} Wh`;
                }

                details.appendChild(consumption);

                content.append(details);
                entry.appendChild(content);
                consumptList.appendChild(entry);
            });
        });
}