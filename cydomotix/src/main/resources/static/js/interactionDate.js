function formatDateTime(dateString) {
    const date = new Date(dateString);

    return date.toLocaleString("fr-FR", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit"
    });
}

const lastInteractionElements = document.querySelectorAll(".lastInteractionText");

lastInteractionElements.forEach(element => {
    let rawValue = element.textContent.trim(); // Récupérer le texte affiché
    let formattedDate = formatDateTime(rawValue); // Formatter la date
    element.textContent = formattedDate;
});

