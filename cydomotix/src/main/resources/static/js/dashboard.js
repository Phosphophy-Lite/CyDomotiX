document.addEventListener('DOMContentLoaded', () => {
    let userPoints = 853;

    const moduleCosts = {
        gestion: 300,
        administration: 800
    };

    const popup = createPopup();
    document.body.appendChild(popup);

    const buyButton = document.getElementById('buy-button');
    const cancelButton = document.getElementById('cancel-button');
    const popupMessage = document.getElementById('popup-message');

    // Click listeners sur les modules
    document.querySelector('.gestion-button')?.addEventListener('click', () => showPopup('gestion'));
    document.querySelector('.admin-button')?.addEventListener('click', () => showPopup('administration'));

    // Cacher le popup
    cancelButton.onclick = () => popup.style.display = 'none';

    // Stocke temporairement le module courant
    let currentModule = null;

    buyButton.onclick = () => {
        if (currentModule) {
            purchaseModule(currentModule);
            popup.style.display = 'none';
        }
    };

    function showPopup(moduleType) {
        currentModule = moduleType;
        const cost = moduleCosts[moduleType];
        const moduleName = capitalize(moduleType);
        const isUnlocked = document.getElementById(moduleType)?.classList.contains('unlocked');

        if (isUnlocked) {
            console.log(`${moduleName} déjà déverrouillé.`);
            return;
        }

        popupMessage.innerHTML = `
            Vous avez <span style="color: #4290fd; font-weight: bold;">${userPoints} pts</span> 
            et le module <strong>${moduleName}</strong> coûte 
            <span style="color: #e30af3; font-weight: bold;">${cost} pts</span>.<br><br>
            Voulez-vous l'acheter ?
        `;

        buyButton.disabled = userPoints < cost;
        buyButton.style.opacity = userPoints < cost ? 0.5 : 1;

        popup.style.display = 'flex';
    }

    function purchaseModule(moduleType) {
        const cost = moduleCosts[moduleType];
        if (userPoints < cost) return;

        const moduleElement = document.getElementById(moduleType);
        if (!moduleElement || moduleElement.classList.contains('unlocked')) return;

        userPoints -= cost;
        moduleElement.classList.add('unlocked');

        // Supprimer l’overlay
        moduleElement.querySelector('.lock-overlay')?.remove();

        // Style visuel du bouton
        const moduleButton = moduleElement.querySelector(`.${moduleType}-button`);
        if (moduleButton) {
            moduleButton.style.zIndex = '1';
            moduleButton.style.background = 'rgba(255, 255, 255, 0.3)';
            moduleButton.style.boxShadow = '0 0 20px rgba(255, 255, 255, 0.6)';
        }

        moduleElement.style.border = '2px solid rgba(255, 255, 255, 0.5)';

        createUnlockAnimation(moduleElement);
        console.log(`Module ${moduleType} acheté. Points restants: ${userPoints}`);
    }

    function createPopup() {
        const popupContainer = document.createElement('div');
        popupContainer.classList.add('module-popup');
        popupContainer.style.display = 'none';
        popupContainer.innerHTML = `
            <div class="popup-content">
                <h2>Achat de module</h2>
                <p id="popup-message"></p>
                <div class="popup-buttons">
                    <button id="buy-button" class="popup-button buy">Acheter</button>
                    <button id="cancel-button" class="popup-button cancel">Annuler</button>
                </div>
            </div>
        `;

        // Styles du popup (ajouté une seule fois)
        if (!document.getElementById('module-style')) {
            const style = document.createElement('style');
            style.id = 'module-style';
            style.textContent = `
                .module-popup {
                    position: fixed;
                    top: 0; left: 0; width: 100%; height: 100%;
                    background-color: rgba(0, 0, 0, 0.7);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 1000;
                }

                .popup-content {
                    background: linear-gradient(135deg, #1e1e2f, #2d2d44);
                    border-radius: 20px;
                    padding: 30px;
                    text-align: center;
                    width: 400px;
                    box-shadow: 0 0 30px rgba(66, 144, 253, 0.6), 0 0 30px rgba(227, 10, 243, 0.6);
                    border: 2px solid rgba(255, 255, 255, 0.1);
                    color: white;
                    font-family: amongus, sans-serif;
                }

                .popup-content h2 {
                    font-size: 2.5em;
                    background: linear-gradient(0.25turn, #4290fd, #e30af3);
                    -webkit-background-clip: text;
                    -webkit-text-fill-color: transparent;
                }

                .popup-content p {
                    font-size: 1.5em;
                    margin: 20px 0;
                }

                .popup-buttons {
                    display: flex;
                    justify-content: space-around;
                }

                .popup-button {
                    padding: 10px 30px;
                    border-radius: 50px;
                    border: none;
                    font-family: amongus, sans-serif;
                    font-size: 1.5em;
                    cursor: pointer;
                    transition: all 0.3s ease;
                }

                .popup-button.buy {
                    background: linear-gradient(0.25turn, #4290fd, #e30af3);
                    color: white;
                }

                .popup-button.cancel {
                    background: rgba(255, 255, 255, 0.2);
                    color: white;
                }

                .popup-button:hover:not(:disabled) {
                    transform: scale(1.1);
                    box-shadow: 0 0 15px rgba(255, 255, 255, 0.5);
                }

                .unlocked::before, .unlocked::after {
                    display: none !important;
                }

                @keyframes unlockParticle {
                    0% { transform: scale(0); opacity: 0; }
                    20% { opacity: 1; }
                    100% { transform: scale(2) translateY(-50px); opacity: 0; }
                }
            `;
            document.head.appendChild(style);
        }

        return popupContainer;
    }

    function createUnlockAnimation(element) {
        const rect = element.getBoundingClientRect();
        const centerX = rect.left + rect.width / 2;
        const centerY = rect.top + rect.height / 2;

        for (let i = 0; i < 20; i++) {
            const particle = document.createElement('div');
            particle.classList.add('unlock-particle');

            const angle = Math.random() * 2 * Math.PI;
            const distance = 50 + Math.random() * 100;
            const x = centerX + Math.cos(angle) * distance;
            const y = centerY + Math.sin(angle) * distance;

            Object.assign(particle.style, {
                position: 'fixed',
                left: `${x}px`,
                top: `${y}px`,
                width: `${5 + Math.random() * 5}px`,
                height: `${5 + Math.random() * 5}px`,
                backgroundColor: Math.random() > 0.5 ? '#4290fd' : '#e30af3',
                borderRadius: '50%',
                boxShadow: '0 0 10px 2px white',
                opacity: 0,
                zIndex: 100,
                animation: `unlockParticle ${1 + Math.random() * 2}s ease-out forwards`
            });

            document.body.appendChild(particle);
            setTimeout(() => particle.remove(), 3000);
        }
    }

    function capitalize(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }
});