// STUDENT PROFILE

document.addEventListener("DOMContentLoaded", () => {

    let editMode = false;

    const editButton =
        document.getElementById("editProfileButton");

    const cancelButton =
        document.getElementById("cancelEditButton");

    const profileForm =
        document.getElementById("studentProfileForm");

    const profileImageInput =
        document.getElementById("studentImage");

    const profilePreview =
        document.getElementById("profileImagePreview");

    const editableInputs =
        document.querySelectorAll(".editable-field");

    // EDIT PROFILE

    if (editButton) {

        editButton.addEventListener("click", () => {

            editMode = true;

            editableInputs.forEach(input => {

                input.removeAttribute("readonly");

                input.removeAttribute("disabled");

            });

            if (profileImageInput) {

                profileImageInput.disabled = false;

            }

            editButton.style.display = "none";

            if (cancelButton) {

                cancelButton.style.display = "inline-flex";

            }

            const saveButton =
                document.getElementById("saveProfileButton");

            if (saveButton) {

                saveButton.style.display = "inline-flex";

            }

        });

    }

    // CANCEL EDIT

    if (cancelButton) {

        cancelButton.addEventListener("click", () => {

            window.location.reload();

        });

    }

    // IMAGE PREVIEW

    if (profileImageInput && profilePreview) {

        profileImageInput.addEventListener(
            "change",
            () => {

                const file =
                    profileImageInput.files[0];

                if (!file) {

                    return;

                }

                if (!validateImage(file)) {

                    alert(
                        "Please select a valid JPG or PNG image."
                    );

                    profileImageInput.value = "";

                    return;

                }

                previewSelectedImage(
                    file,
                    profilePreview
                );

            }
        );

    }

    // FORM VALIDATION

    if (profileForm) {

        profileForm.addEventListener(
            "submit",
            event => {

                if (!editMode) {

                    event.preventDefault();

                    return;

                }

                let valid = true;

                const studentName =
                    document.getElementById(
                        "studentName"
                    );

                const mobile =
                    document.getElementById(
                        "mobile"
                    );

                const branch =
                    document.getElementById(
                        "branch"
                    );

                const semester =
                    document.getElementById(
                        "semester"
                    );

                const address =
                    document.getElementById(
                        "address"
                    );

                if (
                    studentName &&
                    studentName.value.trim().length < 3
                ) {

                    valid = false;

                    setError(
                        studentName,
                        document.getElementById(
                            "studentNameError"
                        ),
                        "Student name must contain at least 3 characters."
                    );

                } else if (studentName) {

                    clearError(
                        studentName,
                        document.getElementById(
                            "studentNameError"
                        )
                    );

                }

                if (
                    mobile &&
                    !isValidMobile(
                        mobile.value
                    )
                ) {

                    valid = false;

                    setError(
                        mobile,
                        document.getElementById(
                            "mobileError"
                        ),
                        "Enter a valid 10-digit mobile number."
                    );

                } else if (mobile) {

                    clearError(
                        mobile,
                        document.getElementById(
                            "mobileError"
                        )
                    );

                }

                if (
                    branch &&
                    branch.value.trim() === ""
                ) {

                    valid = false;

                    setError(
                        branch,
                        document.getElementById(
                            "branchError"
                        ),
                        "Branch is required."
                    );

                } else if (branch) {

                    clearError(
                        branch,
                        document.getElementById(
                            "branchError"
                        )
                    );

                }

                if (
                    semester &&
                    (
                        semester.value === "" ||
                        Number(semester.value) < 1 ||
                        Number(semester.value) > 10
                    )
                ) {

                    valid = false;

                    setError(
                        semester,
                        document.getElementById(
                            "semesterError"
                        ),
                        "Semester must be between 1 and 10."
                    );

                } else if (semester) {

                    clearError(
                        semester,
                        document.getElementById(
                            "semesterError"
                        )
                    );

                }

                if (
                    address &&
                    address.value.trim() === ""
                ) {

                    valid = false;

                    setError(
                        address,
                        document.getElementById(
                            "addressError"
                        ),
                        "Address is required."
                    );

                } else if (address) {

                    clearError(
                        address,
                        document.getElementById(
                            "addressError"
                        )
                    );

                }

                if (!valid) {

                    event.preventDefault();

                }

            }
        );

    }

});