let chartInstance;

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

// Récupérer la consommation totale sur une période sélectionnée + faire l'histogramme
function loadStats(period) {
    const { start, end } = getPeriodRange(period);

    // requête pour récupérer le tableau associatif des consommations totales de chaque objet sur une période donnée
    fetch(`/gestion/stats/range?start=${start}&end=${end}`)
        .then(res => res.json())
        .then(data => {
            data.sort((a, b) => b.totalWh - a.totalWh);

            const labels = data.map(stat => stat.name);
            const values = data.map(stat => (stat.totalWh / 1000).toFixed(2)); // en kWh

            const ctx = document.getElementById("consumptionChart").getContext("2d");

            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Consommation (kWh)',
                        data: values,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    indexAxis: 'x',
                    scales: {
                        y: {
                            type: 'logarithmic',
                            min: 0.1,
                            title: {
                                display: true,
                                text: 'Consommation (kWh)'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Objets connectés'
                            }
                        }
                    },
                    plugins: {
                        legend: { display: false },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return context.raw + ' kWh';
                                }
                            }
                        }
                    }
                }
            });
        });
}

// Chargement initial + option "tout le temps"
window.onload = () => {
    loadStats("all");
    document.getElementById("period").addEventListener("change", (e) => {
        loadStats(e.target.value);
    });
};