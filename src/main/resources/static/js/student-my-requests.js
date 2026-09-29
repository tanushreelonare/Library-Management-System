document.addEventListener("DOMContentLoaded", function () {

    const detailButtons =
        document.querySelectorAll(".view-details-button");

    const modal =
        document.getElementById("requestDetailsModal");

    const modalContent =
        document.getElementById("requestDetailsContent");

    const closeModalButton =
        document.getElementById("closeRequestModal");

    const modalOverlay =
        document.querySelector(".request-modal-overlay");

    detailButtons.forEach(function (button) {

        button.addEventListener("click", function () {

            const requestId =
                button.getAttribute("data-request-id");

            loadRequestDetails(requestId);

        });

    });

    function loadRequestDetails(requestId) {

        fetch("/student/requests/" + requestId)

            .then(function (response) {

                if (!response.ok) {
                    throw new Error(
                        "Unable to load request details."
                    );
                }

                return response.json();

            })

            .then(function (request) {

                renderRequestDetails(request);

                modal.style.display = "block";

                document.body.style.overflow = "hidden";

            })

            .catch(function (error) {

                modalContent.innerHTML =
                    "<p>Unable to load request details.</p>";

                modal.style.display = "block";

                document.body.style.overflow = "hidden";

                console.error(error);

            });

    }

    function renderRequestDetails(request) {

        let html = "";

        html +=
            "<div class='modal-request-summary'>";

        html +=
            "<p><strong>Request ID:</strong> #" +
            request.id +
            "</p>";

        html +=
            "<p><strong>Status:</strong> " +
            request.status +
            "</p>";

        if (request.requestDate) {

            html +=
                "<p><strong>Requested On:</strong> " +
                formatDate(request.requestDate) +
                "</p>";

        }

        html +=
            "</div>";

        html +=
            "<div class='modal-request-books'>";

        html +=
            "<h3>Requested Books</h3>";

        request.items.forEach(function (item) {

            html +=
                "<div class='modal-book-item'>";

            html +=
                "<h4>" +
                escapeHtml(item.title) +
                "</h4>";

            html +=
                "<p>Author: " +
                escapeHtml(item.author || "Not Available") +
                "</p>";

            html +=
                "<p>ISBN: " +
                escapeHtml(item.isbn || "Not Available") +
                "</p>";

            html +=
                "<p>Duration: " +
                item.durationDays +
                " Days</p>";

            html +=
                "<p>Status: " +
                escapeHtml(item.status) +
                "</p>";

            if (item.rejectionReason) {

                html +=
                    "<p>Rejection Reason: " +
                    escapeHtml(item.rejectionReason) +
                    "</p>";

            }

            if (item.issuedAt) {

                html +=
                    "<p>Issued On: " +
                    formatDate(item.issuedAt) +
                    "</p>";

            }

            if (item.dueDate) {

                html +=
                    "<p>Due Date: " +
                    formatDate(item.dueDate) +
                    "</p>";

            }

            if (item.returnedAt) {

                html +=
                    "<p>Returned On: " +
                    formatDate(item.returnedAt) +
                    "</p>";

            }

            html +=
                "</div>";

        });

        html +=
            "</div>";

        modalContent.innerHTML = html;
    }

    function closeModal() {

        modal.style.display = "none";

        document.body.style.overflow = "";

    }

    if (closeModalButton) {

        closeModalButton.addEventListener(
            "click",
            closeModal
        );

    }

    if (modalOverlay) {

        modalOverlay.addEventListener(
            "click",
            closeModal
        );

    }

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape") {
                closeModal();
            }

        }
    );

    function formatDate(value) {

        const date =
            new Date(value);

        return date.toLocaleString(
            "en-IN",
            {
                day: "2-digit",
                month: "long",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        );

    }

    function escapeHtml(value) {

        const div =
            document.createElement("div");

        div.textContent =
            value;

        return div.innerHTML;

    }

});