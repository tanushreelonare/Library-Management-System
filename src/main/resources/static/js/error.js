// ERROR PAGES 403 & 404 

document.addEventListener("DOMContentLoaded", () => {

    // ELEMENTS

    const errorCard = document.querySelector(".error-card");
    const buttons = document.querySelectorAll(".button-group .btn");

    // PRIMARY BUTTON FOCUS

    if (buttons.length > 0) {

        buttons[0].focus();

    }

    // ESCAPE KEY → GO BACK

    document.addEventListener("keydown", (event) => {

        if (event.key === "Escape") {

            // Only go back if browser history exists.

            if (window.history.length > 1) {

                window.history.back();

            } else {

                window.location.href = "/";

            }

        }

    });

    // ALT + H → HOME

    document.addEventListener("keydown", (event) => {

        if (event.altKey && event.key.toLowerCase() === "h") {

            event.preventDefault();

            window.location.href = "/";

        }

    });

    // ALT + L → LOGIN

    document.addEventListener("keydown", (event) => {

        if (event.altKey && event.key.toLowerCase() === "l") {

            event.preventDefault();

            window.location.href = "/login";

        }

    });

    // BUTTON CLICK PROTECTION

    buttons.forEach((button) => {

        button.addEventListener("click", () => {

            button.classList.add("button-clicked");

        });

    });


    // ERROR CARD ACCESSIBILITY

    if (errorCard) {

        errorCard.setAttribute("role", "alert");

        errorCard.setAttribute(
            "aria-live",
            "polite"
        );

    }

});