function openPurchasePopup(module, cost) {
    document.getElementById('popupModuleName').textContent = module;
    document.getElementById('popupModuleCost').textContent = cost;
    document.getElementById('popupAccessType').value = module;

    // Optionally check if user has enough points before enabling confirm button
    const userPoints = parseInt(document.getElementById('popupUserPoints').textContent);
    document.getElementById('confirmBtn').disabled = (userPoints < cost);

    document.getElementById('module-popup').style.display = 'flex';
}

function closePopup() {
    document.getElementById('module-popup').style.display = 'none';
}