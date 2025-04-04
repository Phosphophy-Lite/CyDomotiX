document.addEventListener('DOMContentLoaded', function() {
    // Créer l'élément pour la couche d'étoiles supplémentaire
    const starsLayer = document.createElement('div');
    starsLayer.id = 'stars-layer';
    document.body.appendChild(starsLayer);

    // Fonction pour créer une étoile filante
    function createShootingStar() {
        const star = document.createElement('div');
        star.className = 'shooting-star';

        // Position aléatoire sur le côté gauche ou supérieur de l'écran
        const topPos = Math.random() * window.innerHeight * 0.7;
        const leftPos = Math.random() * window.innerWidth * 0.3;

        star.style.top = `${topPos}px`;
        star.style.left = `${leftPos}px`;

        // Angle aléatoire
        const rotation = Math.random() * 45; // 0 à 45 degrés
        star.style.transform = `rotate(${rotation}deg)`;

        document.body.appendChild(star);

        // Nettoyer après l'animation
        setTimeout(() => {
            star.remove();
        }, 3000);
    }

    // Créer une étoile filante toutes les 5-15 secondes
    function scheduleShootingStar() {
        const delay = 5000 + Math.random() * 10000; // Entre 5 et 15 secondes
        setTimeout(() => {
            createShootingStar();
            scheduleShootingStar(); // Planifier la prochaine
        }, delay);
    }

    scheduleShootingStar();

    // Ajouter des étoiles scintillantes statiques
    function addTwinklingStars() {
        for (let i = 0; i < 50; i++) {
            const star = document.createElement('div');
            star.className = 'twinkle-star';

            // Position aléatoire sur l'écran
            star.style.top = `${Math.random() * 100}vh`;
            star.style.left = `${Math.random() * 100}vw`;

            // Taille aléatoire
            const size = 1 + Math.random() * 2;
            star.style.width = `${size}px`;
            star.style.height = `${size}px`;

            // Délai d'animation aléatoire
            star.style.animationDelay = `${Math.random() * 5}s`;
            star.style.animationDuration = `${3 + Math.random() * 7}s`;

            document.body.appendChild(star);
        }
    }

    addTwinklingStars();
});