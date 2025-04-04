// Fonction pour créer dynamiquement les étoiles scintillantes et filantes
document.addEventListener('DOMContentLoaded', function() {
    // Points fictifs de l'utilisateur (à remplacer par les vraies données)
    const userPoints = 853; // Augmenté pour permettre l'achat des deux modules

    // Coût des modules
    const moduleCosts = {
        'gestion': 300,
        'administration': 800
    };

    // Référence aux boutons des modules verrouillés
    const gestionButton = document.querySelector('.gestion-button');
    const adminButton = document.querySelector('.admin-button');

    // Créer le popup une seule fois et le réutiliser
    const popup = createPopup();
    document.body.appendChild(popup);

    // Ajouter les écouteurs d'événements aux boutons
    gestionButton.addEventListener('click', () => showPopup('gestion'));
    adminButton.addEventListener('click', () => showPopup('administration'));

    // Pour déboguer - vérifier que les éléments existent bien
    console.log("Élément gestion:", document.getElementById('gestion'));
    console.log("Élément administration:", document.getElementById('administration'));

    /**
     * Crée l'élément popup
     */
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

        // Ajouter le style du popup
        const style = document.createElement('style');
        style.textContent = `
            .module-popup {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
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
                margin-top: 0;
                font-size: 2.5em;
                background: linear-gradient(0.25turn, #4290fd, #e30af3);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-bottom: 20px;
            }

            .popup-content p {
                font-size: 1.5em;
                margin-bottom: 30px;
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

            .popup-button:hover {
                transform: scale(1.1);
                box-shadow: 0 0 15px rgba(255, 255, 255, 0.5);
            }

            /* Style pour les modules débloqués */
            .unlocked::before, .unlocked::after {
                display: none !important;
            }
        `;

        document.head.appendChild(style);

        // Ajouter l'écouteur pour le bouton d'annulation
        popupContainer.querySelector('#cancel-button').addEventListener('click', hidePopup);

        return popupContainer;
    }

    /**
     * Affiche le popup pour le module spécifié
     */
    function showPopup(moduleType) {
        const cost = moduleCosts[moduleType];
        const moduleName = moduleType.charAt(0).toUpperCase() + moduleType.slice(1);

        // Stocker le type de module actuel pour l'utiliser lors de l'achat
        popup.dataset.currentModule = moduleType;

        // Mettre à jour le message du popup
        const message = `Vous avez <span style="color: #4290fd; font-weight: bold;">${userPoints} pts</span> et le module ${moduleName} coûte <span style="color: #e30af3; font-weight: bold;">${cost} pts</span>.<br><br>Voulez-vous l'acheter ?`;

        document.getElementById('popup-message').innerHTML = message;

        // Activer/désactiver le bouton d'achat en fonction des points disponibles
        const buyButton = document.getElementById('buy-button');
        if (userPoints >= cost) {
            buyButton.disabled = false;
            buyButton.style.opacity = 1;
            // Utiliser le dataset pour stocker le type
            buyButton.onclick = function() {
                purchaseModule(moduleType);
            };
        } else {
            buyButton.disabled = true;
            buyButton.style.opacity = 0.5;
            buyButton.onclick = null;
        }

        // Afficher le popup
        popup.style.display = 'flex';
    }

    /**
     * Cache le popup
     */
    function hidePopup() {
        popup.style.display = 'none';
    }

    /**
     * Gère l'achat d'un module
     */
    function purchaseModule(moduleType) {
        console.log(`Achat du module: ${moduleType}`);

        // Sélectionner l'élément du module
        const moduleElement = document.getElementById(moduleType);

        if (!moduleElement) {
            console.error(`Élément avec ID "${moduleType}" non trouvé!`);
            return;
        }

        // Supprimer l'overlay du cadenas
        const lockOverlay = moduleElement.querySelector('.lock-overlay');
        if (lockOverlay) {
            lockOverlay.remove();
        } else {
            console.log("Aucun lockOverlay trouvé");
        }

        // Supprimer les chaînes en ajoutant une classe
        moduleElement.classList.add('unlocked');

        // Changer le style du bouton du module pour refléter qu'il est déverrouillé
        const moduleButton = moduleElement.querySelector(`.${moduleType}-button`);
        if (moduleButton) {
            moduleButton.style.zIndex = "1";
            moduleButton.style.background = "rgba(255, 255, 255, 0.3)";
            moduleButton.style.boxShadow = "0 0 20px rgba(255, 255, 255, 0.6)";
        } else {
            console.log(`Bouton .${moduleType}-button non trouvé`);
        }

        // Ajouter une bordure brillante
        moduleElement.style.border = "2px solid rgba(255, 255, 255, 0.5)";

        // Cacher le popup APRÈS avoir appliqué toutes les modifications
        hidePopup();

        // Animation de succès
        createUnlockAnimation(moduleElement);
    }

    /**
     * Crée une animation lors du déverrouillage d'un module
     */
    function createUnlockAnimation(element) {
        // Créer des particules d'étoiles autour de l'élément déverrouillé
        const rect = element.getBoundingClientRect();
        const centerX = rect.left + rect.width / 2;
        const centerY = rect.top + rect.height / 2;

        // Créer 20 particules d'étoiles
        for (let i = 0; i < 20; i++) {
            const particle = document.createElement('div');
            particle.classList.add('unlock-particle');

            // Position aléatoire autour du centre
            const angle = Math.random() * Math.PI * 2;
            const distance = (50 + Math.random() * 100);
            const x = centerX + Math.cos(angle) * distance;
            const y = centerY + Math.sin(angle) * distance;

            // Configurer la particule
            particle.style.position = 'fixed';
            particle.style.left = `${x}px`;
            particle.style.top = `${y}px`;
            particle.style.width = `${5 + Math.random() * 5}px`;
            particle.style.height = particle.style.width;
            particle.style.backgroundColor = Math.random() > 0.5 ? '#4290fd' : '#e30af3';
            particle.style.borderRadius = '50%';
            particle.style.boxShadow = '0 0 10px 2px white';
            particle.style.opacity = '0';
            particle.style.zIndex = '100';

            // Animation
            particle.style.animation = `unlockParticle ${1 + Math.random() * 2}s ease-out forwards`;

            // Ajouter au body
            document.body.appendChild(particle);

            // Supprimer après l'animation
            setTimeout(() => {
                particle.remove();
            }, 3000);
        }

        // Ajouter le style d'animation pour les particules
        if (!document.getElementById('unlock-animation-style')) {
            const style = document.createElement('style');
            style.id = 'unlock-animation-style';
            style.textContent = `
                @keyframes unlockParticle {
                    0% { transform: scale(0); opacity: 0; }
                    20% { opacity: 1; }
                    100% { transform: scale(2) translateY(-50px); opacity: 0; }
                }
            `;
            document.head.appendChild(style);
        }
    }
});