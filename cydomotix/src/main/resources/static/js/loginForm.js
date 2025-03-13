document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const show = urlParams.get("show");

    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const main = document.getElementById("main");

    if (show === "register") {
        signupForm.classList.remove("hidden");
        loginForm.classList.add("hidden");
    } else {
        loginForm.classList.remove("hidden");
        signupForm.classList.add("hidden");
    }

    document.getElementById("pChangeForm").addEventListener("click", function () {
        loginForm.classList.add("hidden");
        signupForm.classList.remove("hidden");
        main.classList.toggle("inscription-mode");
    });

    document.getElementById("pChangeToLogin").addEventListener("click", function () {
        signupForm.classList.add("hidden");
        loginForm.classList.remove("hidden");
        main.classList.remove("inscription-mode");
    });
});

