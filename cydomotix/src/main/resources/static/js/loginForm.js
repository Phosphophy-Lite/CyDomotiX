document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const show = urlParams.get("show");

    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const main = document.getElementById("main");

    function switchForm(formToShow, formToHide, addClass) {
        formToHide.classList.add("fade-out"); // Applique l'effet de disparition
        setTimeout(() => {
            formToHide.classList.add("hidden");
            formToHide.classList.remove("fade-out");

            formToShow.classList.remove("hidden");
            formToShow.classList.add("fade-in");

            setTimeout(() => {
                formToShow.classList.remove("fade-in"); // Retire la classe après l'animation
            }, 500);
        }, 250); // Délai avant que l'autre formulaire apparaisse

        if (addClass) {
            main.classList.add("inscription-mode");
        } else {
            main.classList.remove("inscription-mode");
        }
    }

    if (show === "register") {
        switchForm(signupForm, loginForm, true);
    } else {
        switchForm(loginForm, signupForm, false);
    }

    document.getElementById("pChangeForm").addEventListener("click", function () {
        switchForm(signupForm, loginForm, true);
    });

    document.getElementById("pChangeToLogin").addEventListener("click", function () {
        switchForm(loginForm, signupForm, false);
    });
});
