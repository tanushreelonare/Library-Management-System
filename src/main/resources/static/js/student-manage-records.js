document.addEventListener("DOMContentLoaded", function () {

    const reissueModal =
        document.getElementById("reissueModal");

    const reissueForm =
        document.getElementById("reissueForm");

    const reissueItemId =
        document.getElementById("reissueItemId");

    const reissueBookTitle =
        document.getElementById("reissueBookTitle");

    const currentDueDate =
        document.getElementById("currentDueDate");

    const reissueDuration =
        document.getElementById("reissueDuration");

    const closeReissueModal =
        document.getElementById("closeReissueModal");

    const cancelReissue =
        document.getElementById("cancelReissue");

    const reissueButtons =
        document.querySelectorAll(".reissue-button");

    const returnForms =
        document.querySelectorAll(".return-form");


    // Show return success message

    const returned =
        document.body.dataset.returned === "true";

    if (returned && typeof Swal !== "undefined") {

        Swal.fire({
            icon: "success",
            title: "Book Returned",
            text: "The book has been returned successfully.",
            confirmButtonText: "OK"
        });

        // Remove the query parameter after displaying the message.

        window.history.replaceState(
            {},
            document.title,
            window.location.pathname
        );
    }

    // Show re-issue request success message

    const reissueSubmitted =
        document.body.dataset.reissueSubmitted === "true";

    if (reissueSubmitted && typeof Swal !== "undefined") {

        Swal.fire({
            icon: "success",
            title: "Re-issue Request Submitted",
            text: "Your re-issue request has been sent to the librarian for approval.",
            confirmButtonText: "OK"
        });

        window.history.replaceState(
            {},
            document.title,
            window.location.pathname
        );
    }


    // Show duplicate re-issue request message

    const reissuePending =
        document.body.dataset.reissuePending === "true";

    if (reissuePending && typeof Swal !== "undefined") {

        Swal.fire({
            icon: "warning",
            title: "Re-issue Request Already Pending",
            text: "A re-issue request for this book is already pending.",
            confirmButtonText: "OK"
        });

        window.history.replaceState(
            {},
            document.title,
            window.location.pathname
        );
    }

    // Return Book Confirmation

    returnForms.forEach(function (form) {

        form.addEventListener(
            "submit",
            function (event) {

                event.preventDefault();

                if (typeof Swal === "undefined") {

                    form.submit();

                    return;
                }

                Swal.fire({
                    icon: "warning",
                    title: "Return Book?",
                    text: "Are you sure you want to return this book?",
                    showCancelButton: true,
                    confirmButtonText: "Return Book",
                    cancelButtonText: "Cancel"
                }).then(function (result) {

                    if (result.isConfirmed) {

                        form.submit();

                    }

                });

            }
        );

    });


    // Open Re-issue Modal

    reissueButtons.forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const itemId =
                    button.getAttribute(
                        "data-item-id"
                    );

                const bookTitle =
                    button.getAttribute(
                        "data-book-title"
                    );

                const dueDate =
                    button.getAttribute(
                        "data-current-due-date"
                    );

                const duration =
                    button.getAttribute(
                        "data-duration"
                    );


                reissueItemId.value =
                    itemId || "";

                reissueBookTitle.textContent =
                    bookTitle || "-";

                currentDueDate.textContent =
                    dueDate || "-";


                if (duration) {

                    const durationOption =
                        reissueDuration.querySelector(
                            'option[value="' + duration + '"]'
                        );

                    if (durationOption) {

                        reissueDuration.value =
                            duration;

                    } else {

                        reissueDuration.value =
                            "14";

                    }

                } else {

                    reissueDuration.value =
                        "14";

                }


                reissueModal.classList.add(
                    "active"
                );

                reissueModal.setAttribute(
                    "aria-hidden",
                    "false"
                );

                document.body.style.overflow =
                    "hidden";

            }
        );

    });


    // Close Re-issue Modal

    function closeModal() {

        reissueModal.classList.remove(
            "active"
        );

        reissueModal.setAttribute(
            "aria-hidden",
            "true"
        );

        document.body.style.overflow =
            "";

    }


    if (closeReissueModal) {

        closeReissueModal.addEventListener(
            "click",
            closeModal
        );

    }


    if (cancelReissue) {

        cancelReissue.addEventListener(
            "click",
            closeModal
        );

    }


    // Close Modal When Clicking Outside

    if (reissueModal) {

        reissueModal.addEventListener(
            "click",
            function (event) {

                if (event.target === reissueModal) {

                    closeModal();

                }

            }
        );

    }


    // Close Modal With Escape Key

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape"
                    && reissueModal
                    && reissueModal.classList.contains(
                        "active"
                    )) {

                closeModal();

            }

        }
    );


    // Re-issue Form

    if (reissueForm) {

        reissueForm.addEventListener(
            "submit",
            function () {

                const itemId =
                    reissueItemId.value;

                const duration =
                    reissueDuration.value;

                if (!itemId || !duration) {

                    return;

                }

            }
        );

    }

});