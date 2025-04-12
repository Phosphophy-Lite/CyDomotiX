// Renvoyer la période calculée en fonction de ce jour
function getPeriodRange(period) {
    const now = new Date();
    let start;

    switch (period) {
        case 'today':
            start = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            break;
        case 'week':
            start = new Date();
            start.setDate(now.getDate() - 7);
            break;
        case 'month':
            start = new Date(now.getFullYear(), now.getMonth(), 1);
            break;
        case 'all':
        default:
            start = new Date(1970, 0, 1);
    }

    return {
        start: start.toISOString(),
        end: now.toISOString()
    };
}

// Récupérer la consommation totale de la maison sur une période sélectionnée
function loadStats(period) {
    const { start, end } = getPeriodRange(period);

    // requête pour récupérer le tableau associatif des consommations totales de chaque objet sur une période donnée
    fetch(`/admin/stats/range?start=${start}&end=${end}`)
        .then(res => res.json())
        .then(data => {
            showRetrievedData(data);
        })
        .catch(error => {
            document.getElementById('stats-list').textContent = 'Aucune donnée disponible.';
    });
}

// Chargement initial + option "tout le temps"
window.onload = () => {
    loadStats("all");
    document.getElementById("period").addEventListener("change", (e) => {
        loadStats(e.target.value);
    });
};

function showRetrievedData(data){
    const statsList = document.getElementById("stats-list");
    statsList.innerHTML = ""; // effacer les entrées

    // Si aucune données
    if (Object.keys(data).length === 0) {
        const noDataMessage = document.createElement("div");
        noDataMessage.setAttribute("class", "no-data-message");
        noDataMessage.textContent = "Aucune donnée disponible.";
        statsList.appendChild(noDataMessage);
        return;
    }

    const consumptContainer =  document.createElement("div");
    const consumptSpan = document.createElement("span");

    const loginRateContainer =  document.createElement("div");
    const loginRateSpan = document.createElement("span");

    const consumption = data["totalHouseConsumption"];
    loginRateSpan.textContent = data["loginRate"] + "%";

    if (consumption >= 1000) { // Formatter en kWh
        const kWh = (consumption / 1000).toFixed(2);
        consumptSpan.textContent = `${kWh} kWh`;
    } else { // Afficher en Wh
        consumptSpan.textContent = `${consumption.toFixed(2)} Wh`;
    }

    consumptContainer.textContent = "Consommation totale de la maison : ";
    loginRateContainer.textContent = "Taux de connexion des utilisateurs : ";

    consumptContainer.appendChild(consumptSpan);
    loginRateContainer.appendChild(loginRateSpan);

    statsList.appendChild(consumptContainer);
    statsList.appendChild(loginRateContainer);

    if (Object.keys(data["durationStats"]).length !== 0) {
        const durationDiv = document.getElementById("duration-stat");

        // Clear
        const existingTitle = durationDiv.querySelector(".duration-title");
        if (existingTitle) existingTitle.remove();

        const title = document.createElement("div");
        title.setAttribute("class", "duration-title");
        title.textContent = "Les plus utilisés : ";
        durationDiv.insertBefore(title, durationDiv.firstChild);
        loadDurationStats(data["durationStats"]);
    }

}

let chartInstance;

// Récupérer la durée totale de fonctionnement sur une période sélectionnée + faire l'histogramme
function loadDurationStats(data) {
    data.sort((a, b) => b.durationMinutes - a.durationMinutes); // tri du plus grand en premier

    // Ne garder que les 10 premiers
    const top10 = data.slice(0, 10);

    const labels = top10.map(stat => stat.name);
    const firstKey = Object.keys(top10)[0];
    let unit = "";
    let values;

    if(top10[firstKey].durationMinutes > 1000){
        unit = "h";
        values = top10.map(stat => (stat.durationMinutes / 60).toFixed(2)); // conversion en h
    } else {
        unit = "min";
        values = top10.map(stat => stat.durationMinutes);
    }

    const ctx = document.getElementById("durationChart").getContext("2d");

    if (chartInstance) {
        chartInstance.destroy();
    }

    chartInstance = new Chart(ctx, {
        type: 'bar', data: {
            labels: labels, datasets: [
                {
                    label: `Durée de fonctionnement (${unit})`,
                    data: values,
                    backgroundColor: 'rgba(54, 162, 235, 0.6)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }
            ]
        }, options: {
            indexAxis: 'x', scales: {
                y: {
                    type: 'linear',
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: `Durée de fonctionnement (${unit})`
                    }
                }, x: {
                    title: {
                        display: true,
                        text: 'Objets connectés'
                    }
                }
            }, plugins: {
                legend: {display: false}, tooltip: {
                    callbacks: {
                        label: function (context) {
                            return context.raw + " " + unit;
                        }
                    }
                }
            }
        }
    });
}