document.addEventListener("DOMContentLoaded", function () {

    const editModal = document.getElementById("editStudentModal");
    const editForm = document.getElementById("editStudentForm");

    let currentStudentId = null;

    // Open edit modal
    window.openEditModal = async function (studentId) {

        currentStudentId = studentId;

        try {

            const response = await fetch(
                `/librarian/students/${studentId}`
            );

            if (!response.ok) {
                throw new Error("Unable to load student details.");
            }

            const student = await response.json();

            document.getElementById("editRollNumber").value =
                student.rollNumber || "";

            document.getElementById("editEmail").value =
                student.email || "";

            document.getElementById("editStudentName").value =
                student.studentName || "";

            document.getElementById("editMobile").value =
                student.mobile || "";

            document.getElementById("editBranch").value =
                student.branch || "";

            document.getElementById("editSemester").value =
                student.semester || "";

            document.getElementById("editAddress").value =
                student.address || "";

            clearEditErrors();

            editModal.classList.add("show");

            document.body.style.overflow = "hidden";

        } catch (error) {

            showAlert(
                "error",
                "Error",
                error.message
            );
        }
    };

    // Close edit modal
    window.closeEditModal = function () {

        editModal.classList.remove("show");

        document.body.style.overflow = "";

        currentStudentId = null;

        editForm.reset();

        clearEditErrors();
    };

    // Close modal when clicking outside
    editModal.addEventListener("click", function (event) {

        if (event.target === editModal) {
            closeEditModal();
        }
    });

    // Close modal with Escape key
    document.addEventListener("keydown", function (event) {

        if (event.key === "Escape"
                && editModal.classList.contains("show")) {

            closeEditModal();
        }
    });

    // Submit edit form
    editForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        clearEditErrors();

        if (!validateEditForm()) {
            return;
        }

        const studentData = {

            rollNumber:
                document.getElementById("editRollNumber").value,

            email:
                document.getElementById("editEmail").value,

            studentName:
                document.getElementById("editStudentName").value.trim(),

            mobile:
                document.getElementById("editMobile").value.trim(),

            branch:
                document.getElementById("editBranch").value.trim(),

            semester:
                Number(
                    document.getElementById("editSemester").value
                ),

            address:
                document.getElementById("editAddress").value.trim()
        };

        const saveButton =
            editForm.querySelector(".modal-save-btn");

        const originalText = saveButton.textContent;

        saveButton.disabled = true;
        saveButton.textContent = "Saving...";

        try {

            const response = await fetch(
                `/librarian/students/${currentStudentId}/update`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(studentData)
                }
            );

            const result = await response.text();

            if (!response.ok) {
                throw new Error(result || "Unable to update student.");
            }

            const updatedStudent = JSON.parse(result);

            updateStudentRow(updatedStudent);

            closeEditModal();

            showAlert(
                "success",
                "Updated",
                "Student details updated successfully."
            );

        } catch (error) {

            showAlert(
                "error",
                "Update Failed",
                error.message
            );

        } finally {

            saveButton.disabled = false;
            saveButton.textContent = originalText;
        }
    });

    // Validate edit form
    function validateEditForm() {

        let valid = true;

        const name =
            document.getElementById("editStudentName").value.trim();

        const mobile =
            document.getElementById("editMobile").value.trim();

        const branch =
            document.getElementById("editBranch").value.trim();

        const semester =
            document.getElementById("editSemester").value;

        const address =
            document.getElementById("editAddress").value.trim();

        if (name.length < 3) {

            showFieldError(
                "editStudentNameError",
                "Name must contain at least 3 characters."
            );

            valid = false;
        }

        if (!/^\d{10}$/.test(mobile)) {

            showFieldError(
                "editMobileError",
                "Mobile number must contain exactly 10 digits."
            );

            valid = false;
        }

        if (branch.length === 0) {

            showFieldError(
                "editBranchError",
                "Branch is required."
            );

            valid = false;
        }

        const semesterNumber = Number(semester);

        if (!semester
                || semesterNumber < 1
                || semesterNumber > 10) {

            showFieldError(
                "editSemesterError",
                "Select a valid semester."
            );

            valid = false;
        }

        if (address.length === 0) {

            showFieldError(
                "editAddressError",
                "Address is required."
            );

            valid = false;
        }

        return valid;
    }

    // Show field error
    function showFieldError(elementId, message) {

        const element =
            document.getElementById(elementId);

        if (element) {
            element.textContent = message;
        }
    }

    // Clear validation errors
    function clearEditErrors() {

        const errorElements =
            document.querySelectorAll(
                "#editStudentForm .field-error"
            );

        errorElements.forEach(function (element) {
            element.textContent = "";
        });
    }

    // Update table row after editing
    function updateStudentRow(student) {

        const row =
            document.getElementById(
                `student-row-${student.id}`
            );

        if (!row) {
            return;
        }

        const cells = row.querySelectorAll("td");

        cells[0].textContent =
            student.rollNumber || "";

        cells[1].textContent =
            student.studentName || "";

        cells[2].textContent =
            student.email || "";

        cells[3].textContent =
            student.mobile || "";

        cells[4].textContent =
            student.branch || "";

        cells[5].textContent =
            student.semester || "";

        updateStatusDisplay(row, student.enabled);

        const deleteButton =
            row.querySelector(".delete-btn");

        if (deleteButton) {
            deleteButton.dataset.name =
                student.studentName || "";
        }
    }

    // Activate or deactivate student
    window.toggleStudentStatus = async function (button) {

        const studentId = button.dataset.id;

        const isCurrentlyEnabled =
            button.dataset.enabled === "true";

        const action =
            isCurrentlyEnabled
                ? "deactivate"
                : "activate";

        const actionText =
            isCurrentlyEnabled
                ? "Deactivate"
                : "Activate";

        const confirmed = await confirmAction(
            actionText + " Student?",
            isCurrentlyEnabled
                ? "The student will no longer be able to log in."
                : "The student will be able to log in again.",
            actionText
        );

        if (!confirmed) {
            return;
        }

        button.disabled = true;

        try {

            const response = await fetch(
                `/librarian/students/${studentId}/toggle-status`,
                {
                    method: "POST"
                }
            );

            if (!response.ok) {

                const error =
                    await response.text();

                throw new Error(
                    error || "Unable to change student status."
                );
            }

            const student =
                await response.json();

            const row =
                document.getElementById(
                    `student-row-${student.id}`
                );

            updateStatusDisplay(
                row,
                student.enabled
            );

            showAlert(
                "success",
                "Status Updated",
                student.enabled
                    ? "Student account activated successfully."
                    : "Student account deactivated successfully."
            );

        } catch (error) {

            showAlert(
                "error",
                "Status Update Failed",
                error.message
            );

        } finally {

            button.disabled = false;
        }
    };

    // Update status badge and button
    function updateStatusDisplay(row, enabled) {

        if (!row) {
            return;
        }

        const statusBadge =
            row.querySelector(".status-badge");

        const statusButton =
            row.querySelector(".status-btn");

        if (statusBadge) {

            statusBadge.textContent =
                enabled ? "Active" : "Inactive";

            statusBadge.classList.remove(
                "status-active",
                "status-inactive"
            );

            statusBadge.classList.add(
                enabled
                    ? "status-active"
                    : "status-inactive"
            );
        }

        if (statusButton) {

            statusButton.dataset.enabled =
                enabled;

            statusButton.classList.remove(
                "activate-btn",
                "deactivate-btn"
            );

            statusButton.classList.add(
                enabled
                    ? "deactivate-btn"
                    : "activate-btn"
            );

            const text =
                statusButton.querySelector("span");

            if (text) {

                text.textContent =
                    enabled
                        ? "Deactivate"
                        : "Activate";
            }
        }
    }

    // Delete student
    window.deleteStudent = async function (button) {

        const studentId =
            button.dataset.id;

        const studentName =
            button.dataset.name || "this student";

        const confirmed = await confirmAction(
            "Delete Student?",
            `Are you sure you want to permanently delete ${studentName}? This action cannot be undone.`,
            "Delete"
        );

        if (!confirmed) {
            return;
        }

        button.disabled = true;

        try {

            const response = await fetch(
                `/librarian/students/${studentId}`,
                {
                    method: "DELETE"
                }
            );

            const result =
                await response.text();

            if (!response.ok) {
                throw new Error(
                    result || "Unable to delete student."
                );
            }

            const row =
                document.getElementById(
                    `student-row-${studentId}`
                );

            if (row) {
                row.remove();
            }

            updateStudentCount();

            showAlert(
                "success",
                "Student Deleted",
                "Student has been permanently deleted."
            );

        } catch (error) {

            showAlert(
                "error",
                "Delete Failed",
                error.message
            );

            button.disabled = false;
        }
    };

    // Update total student count
    function updateStudentCount() {

        const rows =
            document.querySelectorAll(
                ".student-table tbody tr[id^='student-row-']"
            );

        const count =
            document.querySelector(".report-summary strong");

        if (count) {
            count.textContent = rows.length;
        }
    }

    // Confirmation dialog
    async function confirmAction(
        title,
        message,
        confirmButtonText) {

        if (typeof Swal !== "undefined") {

            const result = await Swal.fire({

                title: title,

                text: message,

                icon: "warning",

                showCancelButton: true,

                confirmButtonText:
                    confirmButtonText,

                cancelButtonText:
                    "Cancel",

                reverseButtons: true

            });

            return result.isConfirmed;
        }

        return window.confirm(message);
    }

    // Alert
    function showAlert(
        icon,
        title,
        message) {

        if (typeof Swal !== "undefined") {

            Swal.fire({

                icon: icon,

                title: title,

                text: message,

                confirmButtonColor: "#244f87",

                timer: 2200,

                timerProgressBar: true

            });

        } else {

            alert(`${title}\n\n${message}`);
        }
    }

});