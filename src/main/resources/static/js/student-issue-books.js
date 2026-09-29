document.addEventListener("DOMContentLoaded", function () {

    const durationSelect =
        document.getElementById("duration");

    const selectedDuration =
        document.getElementById("selectedDuration");

    if (durationSelect && selectedDuration) {

        durationSelect.addEventListener(
            "change",
            function () {

                selectedDuration.textContent =
                    durationSelect.value;

            }
        );

    }

    const maximumLimitReached =
        document.body.dataset.maximumLimitReached === "true";

    if (maximumLimitReached) {

        if (typeof Swal !== "undefined") {

            Swal.fire({
                icon: "warning",
                title: "Maximum Book Limit",
                text: "You can not issue more than six books at once",
                confirmButtonText: "OK"
            });

        }

    }

});