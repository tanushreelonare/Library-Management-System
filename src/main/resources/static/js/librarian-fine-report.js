document.addEventListener("DOMContentLoaded", function () {

    const modal = document.getElementById("fineDetailsModal");
    const overlay = modal
        ? modal.querySelector(".fine-modal-overlay")
        : null;

    const closeModalButton =
        document.getElementById("closeFineModal");

    const closeModalFooterButton =
        document.getElementById("closeFineModalButton");

    const viewButtons =
        document.querySelectorAll(".view-fine-button");


    /* Modal Detail Elements */

    const detailsStudent =
        document.getElementById("detailsStudent");

    const detailsRollNumber =
        document.getElementById("detailsRollNumber");

    const detailsBook =
        document.getElementById("detailsBook");

    const detailsIsbn =
        document.getElementById("detailsIsbn");

    const detailsIssuedDate =
        document.getElementById("detailsIssuedDate");

    const detailsDueDate =
        document.getElementById("detailsDueDate");

    const detailsReturnedDate =
        document.getElementById("detailsReturnedDate");

    const detailsOverdueDays =
        document.getElementById("detailsOverdueDays");

    const detailsFineAmount =
        document.getElementById("detailsFineAmount");

    const detailsStatus =
        document.getElementById("detailsStatus");


    /* Format Fine Amount */

    function formatFineAmount(value) {

        if (value === null ||
            value === undefined ||
            value === "") {

            return "₹0";
        }

        const amount =
            Number(value);

        if (Number.isNaN(amount)) {
            return "₹0";
        }

        return "₹" + amount.toFixed(0);
    }


    /* Open Fine Details Modal */

    function openFineModal(button) {

        if (!modal || !button) {
            return;
        }

        detailsStudent.textContent =
            button.dataset.student || "-";

        detailsRollNumber.textContent =
            button.dataset.rollNumber || "-";

        detailsBook.textContent =
            button.dataset.book || "-";

        detailsIsbn.textContent =
            button.dataset.isbn || "Not Available";

        detailsIssuedDate.textContent =
            button.dataset.issuedDate || "Not Available";

        detailsDueDate.textContent =
            button.dataset.dueDate || "Not Available";

        detailsReturnedDate.textContent =
            button.dataset.returnedDate || "Not Returned";

        const overdueDays =
            Number(button.dataset.overdueDays);

        detailsOverdueDays.textContent =
            overdueDays === 1
                ? "1 day"
                : overdueDays + " days";

        detailsFineAmount.textContent =
            formatFineAmount(
                button.dataset.fine
            );

        detailsStatus.textContent =
            button.dataset.status || "-";


        /* Apply Status Styling */

        detailsStatus.classList.remove(
            "status-overdue",
            "status-returned-late"
        );

        if (button.dataset.status === "OVERDUE") {

            detailsStatus.classList.add(
                "status-overdue"
            );

        } else if (
            button.dataset.status === "RETURNED LATE"
        ) {

            detailsStatus.classList.add(
                "status-returned-late"
            );
        }


        modal.classList.add("is-open");

        modal.setAttribute(
            "aria-hidden",
            "false"
        );

        document.body.classList.add(
            "fine-modal-open"
        );

    }


    /* Close Fine Details Modal */

    function closeFineModal() {

        if (!modal) {
            return;
        }

        modal.classList.remove("is-open");

        modal.setAttribute(
            "aria-hidden",
            "true"
        );

        document.body.classList.remove(
            "fine-modal-open"
        );

    }


    /* View Details Buttons */

    viewButtons.forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                openFineModal(button);

            }
        );

    });


    /* Close Button */

    if (closeModalButton) {

        closeModalButton.addEventListener(
            "click",
            closeFineModal
        );

    }


    /* Footer Close Button */

    if (closeModalFooterButton) {

        closeModalFooterButton.addEventListener(
            "click",
            closeFineModal
        );

    }


    /* Overlay Click */

    if (overlay) {

        overlay.addEventListener(
            "click",
            closeFineModal
        );

    }


    /* Escape Key */

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape" &&
                modal &&
                modal.classList.contains("is-open")
            ) {

                closeFineModal();

            }

        }
    );


});