/* Librarian Requests */

document.addEventListener("DOMContentLoaded", function () {

    const detailsModal =
        document.getElementById("requestDetailsModal");

    const rejectModal =
        document.getElementById("rejectRequestModal");

    const closeDetailsButton =
        document.getElementById("closeRequestDetailsModal");

    const closeRejectButton =
        document.getElementById("closeRejectRequestModal");

    const cancelRejectButton =
        document.getElementById("cancelRejectButton");

    const rejectForm =
        document.getElementById("rejectRequestForm");

    const rejectionReason =
        document.getElementById("rejectionReason");

    const detailsStudentName =
        document.getElementById("detailsStudentName");

    const detailsRollNumber =
        document.getElementById("detailsRollNumber");

    const detailsRequestDate =
        document.getElementById("detailsRequestDate");

    const detailsRequestStatus =
        document.getElementById("detailsRequestStatus");

    const detailsRequestType =
        document.getElementById("detailsRequestType");

    const detailsBooksContainer =
        document.getElementById("detailsBooksContainer");

    let currentRejectType = "NORMAL";
    let currentRejectRequestId = null;


    /* Initialize Book Selection */

    initializeBookSelection();


    /* View Details */

    document.querySelectorAll(".view-details-button")
        .forEach(function (button) {

            button.addEventListener("click", function () {

                const requestId =
                    this.getAttribute("data-request-id");

                if (!requestId) {
                    return;
                }

                loadRequestDetails(requestId);
            });

        });


    /* Normal Issue Request Rejection */

    document.querySelectorAll(".reject-request-button")
        .forEach(function (button) {

            button.addEventListener("click", function () {

                const requestId =
                    this.getAttribute("data-request-id");

                if (!requestId) {
                    return;
                }

                openRejectModal(requestId);
            });

        });


    /* Approve Re-Issue */

    document.querySelectorAll(".approve-reissue-button")
        .forEach(function (button) {

            button.addEventListener("click", function () {

                const requestId =
                    this.getAttribute("data-request-id");

                if (!requestId) {
                    return;
                }

                confirmReissueApproval(requestId);
            });

        });


    /* Reject Re-Issue */

    document.querySelectorAll(".reject-reissue-button")
        .forEach(function (button) {

            button.addEventListener("click", function () {

                const requestId =
                    this.getAttribute("data-request-id");

                if (!requestId) {
                    return;
                }

                openReissueRejectModal(requestId);
            });

        });


    /*
     * Handle rejection form for both normal
     * issue requests and re-issue requests.
     */
    if (rejectForm) {

        rejectForm.addEventListener("submit", function (event) {

            event.preventDefault();

            const reason =
                rejectionReason
                    ? rejectionReason.value.trim()
                    : "";

            if (reason.length > 500) {

                showWarning(
                    "Rejection reason cannot exceed 500 characters."
                );

                return;
            }


            /*
             * Close the rejection modal BEFORE
             * displaying SweetAlert.
             */
            closeModal(rejectModal);


            /*
             * Normal Issue Rejection
             */
            if (currentRejectType === "NORMAL") {

                Swal.fire({
                    icon: "warning",
                    title: "Reject Request?",
                    text:
                        reason
                            ? "Are you sure you want to reject this issue request?"
                            : "Are you sure you want to reject this issue request without a reason?",
                    showCancelButton: true,
                    confirmButtonText: "Yes, Reject",
                    cancelButtonText: "Cancel",
                    confirmButtonColor: "#dc3545",
                    cancelButtonColor: "#6c757d"
                }).then(function (result) {

                    if (result.isConfirmed) {

                        submitRejection();
                    }

                });

                return;
            }


            /*
             * Re-Issue Rejection
             */
            if (currentRejectType === "REISSUE") {

                Swal.fire({
                    icon: "warning",
                    title: "Reject Re-Issue?",
                    text:
                        reason
                            ? "Are you sure you want to reject this re-issue request?"
                            : "Are you sure you want to reject this re-issue request without a reason?",
                    showCancelButton: true,
                    confirmButtonText: "Yes, Reject",
                    cancelButtonText: "Cancel",
                    confirmButtonColor: "#dc3545",
                    cancelButtonColor: "#6c757d"
                }).then(function (result) {

                    if (result.isConfirmed) {

                        submitRejection();
                    }

                });

            }

        });

    }


    /*
     * Submit rejection to backend.
     */
    function submitRejection() {

        const formData =
            new FormData(rejectForm);

        fetch(rejectForm.action, {
            method: "POST",
            body: formData
        })
            .then(function (response) {

                if (!response.ok) {

                    throw new Error(
                        "Rejection request failed."
                    );
                }

                return response;

            })
            .then(function () {

                Swal.fire({
                    icon: "success",
                    title: "Request Rejected",
                    text:
                        currentRejectType === "REISSUE"
                            ? "The re-issue request has been rejected successfully."
                            : "The issue request has been rejected successfully.",
                    confirmButtonColor: "#244f87"
                }).then(function () {

                    window.location.reload();

                });

            })
            .catch(function (error) {

                console.error(
                    "Rejection error:",
                    error
                );

                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text:
                        "Unable to reject the request. Please try again.",
                    confirmButtonColor: "#244f87"
                });

            });

    }


    /* Open Normal Issue Reject Modal */

    function openRejectModal(requestId) {

        if (!rejectModal ||
            !rejectForm) {

            showWarning(
                "Rejection form was not found."
            );

            return;
        }


        currentRejectType = "NORMAL";

        currentRejectRequestId =
            requestId;


        rejectForm.action =
            "/librarian/requests/" +
            encodeURIComponent(requestId) +
            "/reject";


        if (rejectionReason) {

            rejectionReason.value = "";

            rejectionReason.placeholder =
                "Enter a reason for rejecting the request...";
        }


        const modalTitle =
            rejectModal.querySelector("h2");

        if (modalTitle) {

            modalTitle.textContent =
                "Reject Request";
        }


        const modalDescription =
            rejectModal.querySelector(
                ".reject-modal-description"
            );

        if (modalDescription) {

            modalDescription.textContent =
                "Any remaining pending books in this request will be rejected.";
        }


        const confirmButton =
            document.getElementById(
                "confirmRejectButton"
            ) ||
            rejectModal.querySelector(
                ".confirm-reject-button"
            );


        if (confirmButton) {

            confirmButton.textContent =
                "Reject Request";
        }


        openModal(rejectModal);

    }


    /* Open Re-Issue Reject Modal */

    function openReissueRejectModal(requestId) {

        if (!rejectModal ||
            !rejectForm) {

            showWarning(
                "Rejection form was not found."
            );

            return;
        }


        currentRejectType = "REISSUE";

        currentRejectRequestId =
            requestId;


        rejectForm.action =
            "/librarian/requests/" +
            encodeURIComponent(requestId) +
            "/reject-reissue";


        if (rejectionReason) {

            rejectionReason.value = "";

            rejectionReason.placeholder =
                "Enter a reason for rejecting the re-issue request...";
        }


        const modalTitle =
            rejectModal.querySelector("h2");

        if (modalTitle) {

            modalTitle.textContent =
                "Reject Re-Issue Request";
        }


        const modalDescription =
            rejectModal.querySelector(
                ".reject-modal-description"
            );

        if (modalDescription) {

            modalDescription.textContent =
                "Provide an optional reason for rejecting this re-issue request.";
        }


        const confirmButton =
            document.getElementById(
                "confirmRejectButton"
            ) ||
            rejectModal.querySelector(
                ".confirm-reject-button"
            );


        if (confirmButton) {

            confirmButton.textContent =
                "Reject Re-Issue";
        }


        openModal(rejectModal);

    }


    /* Confirm Re-Issue Approval */

    function confirmReissueApproval(requestId) {

        const action =
            "/librarian/requests/" +
            encodeURIComponent(requestId) +
            "/approve-reissue";


        Swal.fire({
            icon: "question",
            title: "Approve Re-Issue?",
            text:
                "The student's current due date will be extended according to the requested duration.",
            showCancelButton: true,
            confirmButtonText:
                "Approve Re-Issue",
            cancelButtonText:
                "Cancel",
            confirmButtonColor:
                "#168c55",
            cancelButtonColor:
                "#6c757d"

        }).then(function (result) {

            if (result.isConfirmed) {

                submitPostRequest(action);
            }

        });

    }


    /* Submit POST Request */

    function submitPostRequest(action) {

        fetch(action, {
            method: "POST"
        })
            .then(function (response) {

                if (!response.ok) {

                    throw new Error(
                        "Request failed."
                    );
                }

                return response;

            })
            .then(function () {

                Swal.fire({
                    icon: "success",
                    title: "Request Approved",
                    text:
                        "The re-issue request has been approved successfully.",
                    confirmButtonColor:
                        "#244f87"
                }).then(function () {

                    window.location.reload();

                });

            })
            .catch(function (error) {

                console.error(
                    "Request error:",
                    error
                );

                Swal.fire({
                    icon: "error",
                    title: "Error",
                    text:
                        "Unable to process the request. Please try again.",
                    confirmButtonColor:
                        "#244f87"
                });

            });

    }


    /* Initialize Book Selection */

    function initializeBookSelection() {

        const selectedBooksForms =
            document.querySelectorAll(
                ".selected-books-form"
            );


        selectedBooksForms.forEach(function (form) {

            const requestId =
                form.getAttribute(
                    "data-request-id"
                );


            const checkboxes =
                form.querySelectorAll(
                    ".book-selection-checkbox"
                );


            const selectAllCheckbox =
                document.querySelector(
                    '.select-all-checkbox[data-request-id="' +
                    requestId +
                    '"]'
                );


            const approveSelectedButton =
                form.querySelector(
                    ".approve-selected-button"
                );


            const selectedCount =
                form.querySelector(
                    ".selected-books-count"
                );


            /* Individual Book Selection */

            checkboxes.forEach(function (checkbox) {

                checkbox.addEventListener(
                    "change",
                    function () {

                        updateSelectionState(
                            form,
                            selectAllCheckbox,
                            approveSelectedButton,
                            selectedCount
                        );

                    }
                );

            });


            /* Select All Pending */

            if (selectAllCheckbox) {

                selectAllCheckbox.addEventListener(
                    "change",
                    function () {

                        checkboxes.forEach(
                            function (checkbox) {

                                checkbox.checked =
                                    selectAllCheckbox.checked;

                            }
                        );


                        updateSelectionState(
                            form,
                            selectAllCheckbox,
                            approveSelectedButton,
                            selectedCount
                        );

                    }
                );

            }


            /* Approve Selected */

            if (approveSelectedButton) {

                form.addEventListener(
                    "submit",
                    function (event) {

                        const selectedCheckboxes =
                            form.querySelectorAll(
                                ".book-selection-checkbox:checked"
                            );


                        if (
                            selectedCheckboxes.length === 0
                        ) {

                            event.preventDefault();

                            showWarning(
                                "Please select at least one book to approve."
                            );

                            return;
                        }


                        event.preventDefault();


                        confirmSelectedApproval(
                            form,
                            selectedCheckboxes.length
                        );

                    }
                );

            }


            /* Initial State */

            updateSelectionState(
                form,
                selectAllCheckbox,
                approveSelectedButton,
                selectedCount
            );

        });

    }


    /* Update Selection State */

    function updateSelectionState(
        form,
        selectAllCheckbox,
        approveSelectedButton,
        selectedCount
    ) {

        const checkboxes =
            form.querySelectorAll(
                ".book-selection-checkbox"
            );


        const selectedCheckboxes =
            form.querySelectorAll(
                ".book-selection-checkbox:checked"
            );


        const totalBooks =
            checkboxes.length;


        const selectedBooks =
            selectedCheckboxes.length;


        if (selectedCount) {

            if (selectedBooks === 1) {

                selectedCount.textContent =
                    "1 book selected";

            } else {

                selectedCount.textContent =
                    selectedBooks +
                    " books selected";
            }

        }


        if (approveSelectedButton) {

            approveSelectedButton.disabled =
                selectedBooks === 0;
        }


        if (selectAllCheckbox) {

            if (totalBooks === 0) {

                selectAllCheckbox.checked =
                    false;

                selectAllCheckbox.indeterminate =
                    false;

            } else if (
                selectedBooks === totalBooks
            ) {

                selectAllCheckbox.checked =
                    true;

                selectAllCheckbox.indeterminate =
                    false;

            } else if (
                selectedBooks > 0
            ) {

                selectAllCheckbox.checked =
                    false;

                selectAllCheckbox.indeterminate =
                    true;

            } else {

                selectAllCheckbox.checked =
                    false;

                selectAllCheckbox.indeterminate =
                    false;
            }

        }

    }


    /* Confirm Selected Approval */

    function confirmSelectedApproval(
        form,
        selectedCount
    ) {

        Swal.fire({
            icon: "question",
            title: "Approve Selected Books?",
            text:
                "You are about to approve " +
                selectedCount +
                (
                    selectedCount === 1
                        ? " selected book."
                        : " selected books."
                ) +
                " Only the selected books will be approved.",
            showCancelButton: true,
            confirmButtonText:
                "Approve Selected",
            cancelButtonText:
                "Cancel",
            confirmButtonColor:
                "#168c55",
            cancelButtonColor:
                "#6c757d"

        }).then(function (result) {

            if (result.isConfirmed) {

                form.submit();
            }

        });

    }


    /* Load Request Details */

    function loadRequestDetails(requestId) {

        if (detailsBooksContainer) {

            detailsBooksContainer.innerHTML = `
                <div class="details-loading">
                    Loading request details...
                </div>
            `;
        }


        openModal(detailsModal);


        fetch(
            "/librarian/requests/" +
            encodeURIComponent(requestId)
        )
            .then(function (response) {

                if (!response.ok) {

                    throw new Error(
                        "Unable to load request details."
                    );
                }

                return response.json();

            })
            .then(function (request) {

                displayRequestDetails(request);

            })
            .catch(function (error) {

                console.error(
                    "Error loading request details:",
                    error
                );


                if (detailsBooksContainer) {

                    detailsBooksContainer.innerHTML = `
                        <div class="details-error">
                            Unable to load request details.
                            Please try again.
                        </div>
                    `;
                }

            });

    }


    /* Display Request Details */

    function displayRequestDetails(request) {

        if (detailsStudentName) {

            detailsStudentName.textContent =
                request.studentName || "-";
        }


        if (detailsRollNumber) {

            detailsRollNumber.textContent =
                request.rollNumber || "-";
        }


        if (detailsRequestDate) {

            detailsRequestDate.textContent =
                formatDate(
                    request.requestDate
                );
        }


        if (detailsRequestType) {

            detailsRequestType.textContent =
                request.requestType === "REISSUE"
                    ? "RE-ISSUE"
                    : "NEW ISSUE";
        }


        if (detailsRequestStatus) {

            setStatus(
                detailsRequestStatus,
                request.status
            );
        }


        if (!detailsBooksContainer) {
            return;
        }


        detailsBooksContainer.innerHTML = "";


        if (
            !request.items ||
            request.items.length === 0
        ) {

            detailsBooksContainer.innerHTML = `
                <div class="details-empty">
                    No books found for this request.
                </div>
            `;

            return;
        }


        request.items.forEach(function (item) {

            const bookElement =
                document.createElement("div");


            bookElement.className =
                "details-book-item";


            let rejectionReason = "";


            if (item.rejectionReason) {

                rejectionReason = `
                    <div class="details-rejection-reason">
                        <strong>
                            Rejection Reason:
                        </strong>
                        ${escapeHtml(
                            item.rejectionReason
                        )}
                    </div>
                `;
            }


            let approvedDate = "";


            if (item.approvedAt) {

                approvedDate = `
                    <span>
                        Approved:
                        ${formatDate(
                            item.approvedAt
                        )}
                    </span>
                `;
            }


            let issuedDate = "";


            if (item.issuedAt) {

                issuedDate = `
                    <span>
                        Issued:
                        ${formatDate(
                            item.issuedAt
                        )}
                    </span>
                `;
            }


            let dueDate = "";


            if (item.dueDate) {

                dueDate = `
                    <span>
                        Due:
                        ${formatDate(
                            item.dueDate
                        )}
                    </span>
                `;
            }


            let returnedDate = "";


            if (item.returnedAt) {

                returnedDate = `
                    <span>
                        Returned:
                        ${formatDate(
                            item.returnedAt
                        )}
                    </span>
                `;
            }


            let currentDueDate = "";


            if (
                request.requestType === "REISSUE" &&
                item.currentDueDate
            ) {

                currentDueDate = `
                    <span>
                        Current Due:
                        ${formatDate(
                            item.currentDueDate
                        )}
                    </span>
                `;
            }


            bookElement.innerHTML = `

                <div class="details-book-title">
                    ${escapeHtml(
                        item.bookTitle ||
                        "Unknown Book"
                    )}
                </div>

                <div class="details-book-author">
                    Author:
                    ${escapeHtml(
                        item.author || "-"
                    )}
                </div>

                <div class="details-book-isbn">
                    ISBN:
                    ${escapeHtml(
                        item.isbn || "-"
                    )}
                </div>

                <div class="details-book-meta">

                    <span>
                        Duration:
                        <strong>
                            ${
                                item.durationDays != null
                                    ? item.durationDays
                                    : "-"
                            }
                            days
                        </strong>
                    </span>

                    <span>
                        Status:
                        <span
                            class="item-status ${
                                getStatusClass(
                                    item.status
                                )
                            }">

                            ${
                                escapeHtml(
                                    formatStatus(
                                        item.status
                                    )
                                )
                            }

                        </span>
                    </span>

                </div>

                ${
                    currentDueDate ||
                    approvedDate ||
                    issuedDate ||
                    dueDate ||
                    returnedDate
                        ? `
                            <div class="details-book-meta">
                                ${currentDueDate}
                                ${approvedDate}
                                ${issuedDate}
                                ${dueDate}
                                ${returnedDate}
                            </div>
                        `
                        : ""
                }

                ${rejectionReason}

            `;


            detailsBooksContainer.appendChild(
                bookElement
            );

        });

    }


    /* Open Modal */

    function openModal(modal) {

        if (!modal) {
            return;
        }


        modal.classList.add("active");


        modal.setAttribute(
            "aria-hidden",
            "false"
        );


        document.body.style.overflow =
            "hidden";

    }


    /* Close Modal */

    function closeModal(modal) {

        if (!modal) {
            return;
        }


        modal.classList.remove("active");


        modal.setAttribute(
            "aria-hidden",
            "true"
        );


        const detailsOpen =
            detailsModal &&
            detailsModal.classList.contains(
                "active"
            );


        const rejectOpen =
            rejectModal &&
            rejectModal.classList.contains(
                "active"
            );


        if (!detailsOpen &&
            !rejectOpen) {

            document.body.style.overflow =
                "";
        }

    }


    /* Close Details Modal */

    if (closeDetailsButton) {

        closeDetailsButton.addEventListener(
            "click",
            function () {

                closeModal(detailsModal);

            }
        );

    }


    /* Close Reject Modal */

    if (closeRejectButton) {

        closeRejectButton.addEventListener(
            "click",
            function () {

                closeModal(rejectModal);

            }
        );

    }


    /* Cancel Reject */

    if (cancelRejectButton) {

        cancelRejectButton.addEventListener(
            "click",
            function () {

                closeModal(rejectModal);

            }
        );

    }


    /* Close Modal From Overlay */

    document.querySelectorAll(
        ".librarian-request-modal-overlay"
    ).forEach(function (overlay) {

        overlay.addEventListener(
            "click",
            function (event) {

                if (event.target === overlay) {

                    const modal =
                        overlay.closest(
                            ".librarian-request-modal"
                        );

                    closeModal(modal);
                }

            }
        );

    });


    /* Close Modal With Escape */

    document.addEventListener(
        "keydown",
        function (event) {

            if (event.key === "Escape") {

                closeModal(detailsModal);

                closeModal(rejectModal);
            }

        }
    );


    /* Status Class */

    function getStatusClass(status) {

        if (!status) {
            return "";
        }


        return "status-" +
            status
                .toLowerCase()
                .replace(/_/g, "-");

    }


    /* Format Status */

    function formatStatus(status) {

        if (!status) {
            return "-";
        }


        return status.replace(
            /_/g,
            " "
        );

    }


    /* Set Status */

    function setStatus(
        element,
        status
    ) {

        if (!element) {
            return;
        }


        element.className =
            "request-status " +
            getStatusClass(status);


        element.textContent =
            formatStatus(status);

    }


    /* Format Date */

    function formatDate(dateValue) {

        if (!dateValue) {
            return "-";
        }


        const date =
            new Date(dateValue);


        if (isNaN(date.getTime())) {

            return dateValue;
        }


        return date.toLocaleString(
            "en-IN",
            {
                day: "2-digit",
                month: "short",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        );

    }


    /* Escape HTML */

    function escapeHtml(value) {

        if (
            value === null ||
            value === undefined
        ) {

            return "";
        }


        const div =
            document.createElement("div");


        div.textContent =
            value;


        return div.innerHTML;

    }


    /* Show Warning */

    function showWarning(message) {

        if (typeof Swal !== "undefined") {

            Swal.fire({
                icon: "warning",
                title: "Warning",
                text: message,
                confirmButtonColor:
                    "#244f87"
            });

        } else {

            window.alert(message);
        }

    }

});