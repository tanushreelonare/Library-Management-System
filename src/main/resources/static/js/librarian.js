// LIBRARIAN REGISTRATION

document.addEventListener("DOMContentLoaded", () => {

    const form =
        document.getElementById("librarianForm");

    if (!form) return;

    // INPUT FIELDS

    const employeeId =
        document.getElementById("employeeId");

    const librarianName =
        document.getElementById("librarianName");

    const email =
        document.getElementById("email");

    const mobile =
        document.getElementById("mobile");

    const designation =
        document.getElementById("designation");

    const qualification =
        document.getElementById("qualification");

    const experience =
        document.getElementById("experience");

    const password =
        document.getElementById("password");

    const confirmPassword =
        document.getElementById("confirmPassword");

    const address =
        document.getElementById("address");

    const librarianImage =
        document.getElementById("librarianImage");

    // BUTTONS

    const previewButton =
        document.getElementById("previewButton");

    const showPassword =
        document.getElementById("showPassword");

    // MODAL

    const imageModal =
        document.getElementById("imageModal");

    const previewImage =
        document.getElementById("previewImage");

    const closeModalButton =
        document.getElementById("closeModal");

    const closePreviewButton =
        document.getElementById("closePreview");

    // ERROR LABELS

    const errors = {

        employeeId:
            document.getElementById(
                "employeeIdError"
            ),

        librarianName:
            document.getElementById(
                "librarianNameError"
            ),

        email:
            document.getElementById(
                "emailError"
            ),

        mobile:
            document.getElementById(
                "mobileError"
            ),

        designation:
            document.getElementById(
                "designationError"
            ),

        qualification:
            document.getElementById(
                "qualificationError"
            ),

        experience:
            document.getElementById(
                "experienceError"
            ),

        password:
            document.getElementById(
                "passwordError"
            ),

        confirmPassword:
            document.getElementById(
                "confirmPasswordError"
            ),

        address:
            document.getElementById(
                "addressError"
            ),

        librarianImage:
            document.getElementById(
                "librarianImageError"
            )

    };

    // EMPLOYEE ID

    employeeId.addEventListener(
        "input",
        () => {

            toUpperCase(employeeId);

            if (
                employeeId.value.trim().length < 3
            ) {

                setError(
                    employeeId,
                    errors.employeeId,
                    "Employee ID is required."
                );

            } else {

                clearError(
                    employeeId,
                    errors.employeeId
                );

            }

        }
    );

    // LIBRARIAN NAME

    librarianName.addEventListener(
        "input",
        () => {

            lettersOnly(librarianName);

            if (
                librarianName.value.trim().length < 3
            ) {

                setError(
                    librarianName,
                    errors.librarianName,
                    "Minimum 3 characters required."
                );

            } else {

                clearError(
                    librarianName,
                    errors.librarianName
                );

            }

        }
    );

    // EMAIL

    email.addEventListener(
        "input",
        () => {

            if (!isValidEmail(email.value)) {

                setError(
                    email,
                    errors.email,
                    "Invalid Email Address."
                );

            } else {

                clearError(
                    email,
                    errors.email
                );

            }

        }
    );

    // MOBILE

    mobile.addEventListener(
        "input",
        () => {

            numbersOnly(mobile);

            if (
                !isValidMobile(mobile.value)
            ) {

                setError(
                    mobile,
                    errors.mobile,
                    "Enter a valid 10-digit mobile number."
                );

            } else {

                clearError(
                    mobile,
                    errors.mobile
                );

            }

        }
    );

    // DESIGNATION

    designation.addEventListener(
        "change",
        () => {

            if (designation.value === "") {

                setError(
                    designation,
                    errors.designation,
                    "Please select designation."
                );

            } else {

                clearError(
                    designation,
                    errors.designation
                );

            }

        }
    );

    // QUALIFICATION

    qualification.addEventListener(
        "change",
        () => {

            if (qualification.value === "") {

                setError(
                    qualification,
                    errors.qualification,
                    "Please select qualification."
                );

            } else {

                clearError(
                    qualification,
                    errors.qualification
                );

            }

        }
    );

    // EXPERIENCE

    experience.addEventListener(
        "input",
        () => {

            const value =
                parseInt(experience.value);

            if (isNaN(value)) {

                setError(
                    experience,
                    errors.experience,
                    "Experience is required."
                );

                return;

            }

            if (
                value < 0 ||
                value > 50
            ) {

                setError(
                    experience,
                    errors.experience,
                    "Experience must be between 0 and 50 years."
                );

            } else {

                clearError(
                    experience,
                    errors.experience
                );

            }

        }
    );

    // PASSWORD

    password.addEventListener(
        "input",
        () => {

            if (
                password.value.length < 8
            ) {

                setError(
                    password,
                    errors.password,
                    "Password must contain at least 8 characters."
                );

            } else {

                clearError(
                    password,
                    errors.password
                );

            }

            if (
                confirmPassword.value !== ""
                &&
                confirmPassword.value !==
                password.value
            ) {

                setError(
                    confirmPassword,
                    errors.confirmPassword,
                    "Passwords do not match."
                );

            } else if (
                confirmPassword.value !== ""
            ) {

                clearError(
                    confirmPassword,
                    errors.confirmPassword
                );

            }

        }
    );

    // CONFIRM PASSWORD

    confirmPassword.addEventListener(
        "input",
        () => {

            if (
                confirmPassword.value !==
                password.value
            ) {

                setError(
                    confirmPassword,
                    errors.confirmPassword,
                    "Passwords do not match."
                );

            } else {

                clearError(
                    confirmPassword,
                    errors.confirmPassword
                );

            }

        }
    );

    // SHOW PASSWORD

    showPassword.addEventListener(
        "change",
        () => {

            togglePassword(
                password,
                confirmPassword,
                showPassword
            );

        }
    );

    // ADDRESS

    address.addEventListener(
        "input",
        () => {

            if (
                address.value.trim().length < 5
            ) {

                setError(
                    address,
                    errors.address,
                    "Address is required."
                );

            } else {

                clearError(
                    address,
                    errors.address
                );

            }

        }
    );

    // LIBRARIAN IMAGE

    librarianImage.addEventListener(
        "change",
        () => {

            const file =
                librarianImage.files[0];

            if (!file) {

                previewButton.disabled = true;

                previewImage.src = "";

                clearError(
                    librarianImage,
                    errors.librarianImage
                );

                return;

            }

            if (!validateImage(file)) {

                setError(
                    librarianImage,
                    errors.librarianImage,
                    "Only JPG and PNG images are allowed."
                );

                librarianImage.value = "";

                previewButton.disabled = true;

                previewImage.src = "";

                return;

            }

            clearError(
                librarianImage,
                errors.librarianImage
            );

            previewSelectedImage(
                file,
                previewImage
            );

            previewButton.disabled = false;

        }
    );

    // IMAGE PREVIEW

    previewButton.addEventListener(
        "click",
        () => {

            openModal(imageModal);

        }
    );

    closeModalButton.addEventListener(
        "click",
        () => {

            closeModal(imageModal);

        }
    );

    closePreviewButton.addEventListener(
        "click",
        () => {

            closeModal(imageModal);

        }
    );

    window.addEventListener(
        "click",
        (event) => {

            if (
                event.target === imageModal
            ) {

                closeModal(imageModal);

            }

        }
    );

    // FORM SUBMIT

    form.addEventListener(
        "submit",
        (event) => {

            let valid = true;

            // Employee ID

            if (
                employeeId.value.trim().length < 3
            ) {

                setError(
                    employeeId,
                    errors.employeeId,
                    "Employee ID is required."
                );

                valid = false;

            }

            // Librarian Name

            if (
                librarianName.value.trim().length < 3
            ) {

                setError(
                    librarianName,
                    errors.librarianName,
                    "Minimum 3 characters required."
                );

                valid = false;

            }

            // Email

            if (
                !isValidEmail(email.value)
            ) {

                setError(
                    email,
                    errors.email,
                    "Invalid Email Address."
                );

                valid = false;

            }

            // Mobile

            if (
                !isValidMobile(mobile.value)
            ) {

                setError(
                    mobile,
                    errors.mobile,
                    "Enter a valid 10-digit mobile number."
                );

                valid = false;

            }

            // Designation

            if (designation.value === "") {

                setError(
                    designation,
                    errors.designation,
                    "Please select designation."
                );

                valid = false;

            }

            // Qualification

            if (qualification.value === "") {

                setError(
                    qualification,
                    errors.qualification,
                    "Please select qualification."
                );

                valid = false;

            }

            // Experience

            const experienceValue =
                parseInt(experience.value);

            if (
                isNaN(experienceValue)
                ||
                experienceValue < 0
                ||
                experienceValue > 50
            ) {

                setError(
                    experience,
                    errors.experience,
                    "Experience must be between 0 and 50 years."
                );

                valid = false;

            }

            // Password

            if (
                password.value.length < 8
            ) {

                setError(
                    password,
                    errors.password,
                    "Password must contain at least 8 characters."
                );

                valid = false;

            }

            // Confirm Password

            if (
                confirmPassword.value !==
                password.value
            ) {

                setError(
                    confirmPassword,
                    errors.confirmPassword,
                    "Passwords do not match."
                );

                valid = false;

            }

            // Address

            if (
                address.value.trim().length < 5
            ) {

                setError(
                    address,
                    errors.address,
                    "Address is required."
                );

                valid = false;

            }

            if (!valid) {

                event.preventDefault();

                return;

            }

        }
    );

    // FORM RESET

    form.addEventListener(
        "reset",
        () => {

            setTimeout(
                () => {

                    clearValidation(form);

                    previewImage.src = "";

                    previewButton.disabled = true;

                    closeModal(imageModal);

                },
                0
            );

        }
    );

});