pChangeForm= document.getElementById('pChangeForm')

pChangeForm.setEventListener('click',function(){
    document.getE("id").style.visilib
})


function scrollToFeatures() {
        // Faire défiler jusqu'à la section des fonctionnalités
        document.getElementById('features').scrollIntoView({
            behavior: 'smooth'
        });
    }

    function scrollToFreeTour() {
        // Faire défiler jusqu'à la section Free Tour
        document.getElementById('free-tour').scrollIntoView({
            behavior: 'smooth'
        });
    }

    // Carousel du Free Tour
    document.addEventListener('DOMContentLoaded', function() {
        let currentSlide = 1;
        const totalSlides = 3;

        const nextButton = document.getElementById('next-button');
        const prevButton = document.getElementById('prev-button');
        const stepCounter = document.getElementById('current-step');
        const allSlides = document.querySelectorAll('.carousel-slide');
        const allDots = document.querySelectorAll('.step-dot');

        // Fonction pour changer de slide
        function goToSlide(slideNumber) {
            // Masquer tous les slides
            allSlides.forEach(slide => {
                slide.classList.remove('active');
            });

            // Afficher le slide actif
            document.querySelector(`.carousel-slide[data-slide="${slideNumber}"]`).classList.add('active');

            // Mettre à jour les dots
            allDots.forEach(dot => {
                dot.classList.remove('active');
            });
            document.querySelector(`.step-dot[data-step="${slideNumber}"]`).classList.add('active');

            // Mettre à jour le compteur
            stepCounter.textContent = slideNumber;

            // Activer/désactiver les boutons
            prevButton.disabled = slideNumber === 1;
            nextButton.disabled = slideNumber === totalSlides;

            // Réinitialiser les interactions pour le slide actuel
            resetInteractions(slideNumber);

            currentSlide = slideNumber;
        }

        // Fonctions de navigation
        nextButton.addEventListener('click', function() {
            if (currentSlide < totalSlides) {
                goToSlide(currentSlide + 1);
            }
        });

        prevButton.addEventListener('click', function() {
            if (currentSlide > 1) {
                goToSlide(currentSlide - 1);
            }
        });

        // Navigation par les dots
        allDots.forEach(dot => {
            dot.addEventListener('click', function() {
                const slideNumber = parseInt(this.getAttribute('data-step'));
                goToSlide(slideNumber);
            });
        });

        // Interactions de l'entrée
        const entranceScene = document.getElementById('entrance-scene');
        const activateCameraBtn = document.getElementById('activate-camera');
        const recognizeFaceBtn = document.getElementById('recognize-face');

        activateCameraBtn.addEventListener('click', function() {
            entranceScene.classList.add('camera-active');
            this.textContent = 'Caméra activée';
            this.disabled = true;
            recognizeFaceBtn.disabled = false;
        });

        recognizeFaceBtn.addEventListener('click', function() {
            entranceScene.classList.add('face-recognized');
            entranceScene.classList.add('door-open');
            this.textContent = 'Accès autorisé';
            this.disabled = true;
        });

        // Interactions du salon
        const livingRoomScene = document.getElementById('living-room-scene');
        const toggleLightsBtn = document.getElementById('toggle-lights');
        const playMusicBtn = document.getElementById('play-music');

        toggleLightsBtn.addEventListener('click', function() {
            if (livingRoomScene.classList.contains('lights-on')) {
                livingRoomScene.classList.remove('lights-on');
                this.textContent = 'Allumer les lumières';
            } else {
                livingRoomScene.classList.add('lights-on');
                this.textContent = 'Éteindre les lumières';
            }
        });

        playMusicBtn.addEventListener('click', function() {
            if (livingRoomScene.classList.contains('music-playing')) {
                livingRoomScene.classList.remove('music-playing');
                this.textContent = 'Lancer la musique';
            } else {
                livingRoomScene.classList.add('music-playing');
                this.textContent = 'Arrêter la musique';
            }
        });

        // Interactions de la sécurité
        const securityScene = document.getElementById('security-scene');
        const activateAlarmBtn = document.getElementById('activate-alarm');
        const sendNotificationBtn = document.getElementById('send-notification');

        activateAlarmBtn.addEventListener('click', function() {
            securityScene.classList.add('alarm-on');
            this.textContent = 'Alarme activée';
            this.disabled = true;
            sendNotificationBtn.disabled = false;
        });

        sendNotificationBtn.addEventListener('click', function() {
            securityScene.classList.add('notification-sent');
            this.textContent = 'Notification envoyée';
            this.disabled = true;
        });

        // Réinitialiser les interactions lors du changement de slide
        function resetInteractions(slideNumber) {
            switch(slideNumber) {
                case 1:
                    entranceScene.classList.remove('camera-active', 'face-recognized', 'door-open');
                    activateCameraBtn.textContent = 'Activer la caméra';
                    activateCameraBtn.disabled = false;
                    recognizeFaceBtn.textContent = 'Reconnaissance faciale';
                    recognizeFaceBtn.disabled = true;
                    break;

                case 2:
                    livingRoomScene.classList.remove('lights-on', 'music-playing');
                    toggleLightsBtn.textContent = 'Allumer les lumières';
                    playMusicBtn.textContent = 'Lancer la musique';
                    break;

                case 3:
                    securityScene.classList.remove('alarm-on', 'notification-sent');
                    activateAlarmBtn.textContent = "Activer l'alarme";
                    activateAlarmBtn.disabled = false;
                    sendNotificationBtn.textContent = 'Tester notification';
                    sendNotificationBtn.disabled = true;
                    break;
            }
        }
    });

