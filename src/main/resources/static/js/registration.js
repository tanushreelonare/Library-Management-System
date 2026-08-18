// STUDENT REGISTRATION

document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("studentForm");

    if (!form) return;

    // INPUT FIELDS

    const rollNumber = document.getElementById("rollNumber");
    const studentName = document.getElementById("studentName");
    const email = document.getElementById("email");
    const mobile = document.getElementById("mobile");
    const branch = document.getElementById("branch");
    const semester = document.getElementById("semester");
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmPassword");
    const address = document.getElementById("address");

    const studentImage = document.getElementById("studentImage");

    // BUTTONS

    const previewButton = document.getElementById("previewButton");
    const showPassword = document.getElementById("showPassword");

    // MODAL

    const imageModal = document.getElementById("imageModal");
    const previewImage = document.getElementById("previewImage");

    const closeModalButton = document.getElementById("closeModal");
    const closePreviewButton = document.getElementById("closePreview");

    // ERROR LABELS

    const errors = {

        rollNumber: document.getElementById("rollNumberError"),
        studentName: document.getElementById("studentNameError"),
        email: document.getElementById("emailError"),
        mobile: document.getElementById("mobileError"),
        branch: document.getElementById("branchError"),
        semester: document.getElementById("semesterError"),
        password: document.getElementById("passwordError"),
        confirmPassword: document.getElementById("confirmPasswordError"),
        address: document.getElementById("addressError"),
        studentImage: document.getElementById("studentImageError")

    };

    // ROLL NUMBER

    rollNumber.addEventListener("input", () => {

        toUpperCase(rollNumber);

        if (rollNumber.value.trim().length < 1) {

            setError(
                rollNumber,
                errors.rollNumber,
                "Roll Number is required."
            );

        } else {

            clearError(
                rollNumber,
                errors.rollNumber
            );

        }

    });

    // STUDENT NAME

    studentName.addEventListener("input", () => {

        lettersOnly(studentName);

        if (studentName.value.trim().length < 3) {

            setError(
                studentName,
                errors.studentName,
                "Minimum 3 characters required."
            );

        } else {

            clearError(
                studentName,
                errors.studentName
            );

        }

    });

    // EMAIL

    email.addEventListener("input", () => {

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

    });

    // MOBILE

    mobile.addEventListener("input", () => {

        numbersOnly(mobile);

        if (!isValidMobile(mobile.value)) {

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

    });

    // BRANCH

    branch.addEventListener("change", () => {

        if (branch.value === "") {

            setError(
                branch,
                errors.branch,
                "Please select a branch."
            );

        } else {

            clearError(
                branch,
                errors.branch
            );

        }

    });

    // SEMESTER

    semester.addEventListener("change", () => {

        if (semester.value === "") {

            setError(
                semester,
                errors.semester,
                "Please select a semester."
            );

        } else {

            clearError(
                semester,
                errors.semester
            );

        }

    });

    // PASSWORD

    password.addEventListener("input", () => {

        if (password.value.length < 8) {

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

    });

    // CONFIRM PASSWORD

    confirmPassword.addEventListener("input", () => {

        if (confirmPassword.value !== password.value) {

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

    });

    // SHOW PASSWORD

    showPassword.addEventListener("change", () => {

        togglePassword(
            password,
            confirmPassword,
            showPassword
        );

    });

    // IMAGE

    studentImage.addEventListener("change", () => {

        const file = studentImage.files[0];

        if (!file) {

            previewButton.disabled = true;

            previewImage.src = "";

            return;

        }

        if (!validateImage(file)) {

            setError(
                studentImage,
                errors.studentImage,
                "Only JPG and PNG images are allowed."
            );

            studentImage.value = "";

            previewButton.disabled = true;

            previewImage.src = "";

            return;

        }

        clearError(
            studentImage,
            errors.studentImage
        );

        previewSelectedImage(
            file,
            previewImage
        );

        previewButton.disabled = false;

    });

    // IMAGE MODAL

    previewButton.addEventListener("click", () => {

        openModal(imageModal);

    });

    closeModalButton.addEventListener("click", () => {

        closeModal(imageModal);

    });

    closePreviewButton.addEventListener("click", () => {

        closeModal(imageModal);

    });

    window.addEventListener("click", (event) => {

        if (event.target === imageModal) {

            closeModal(imageModal);

        }

    });

    // FORM SUBMIT

    form.addEventListener("submit", (event) => {

        const invalidFields = form.querySelectorAll(".input-invalid");

        if (invalidFields.length > 0) {

            event.preventDefault();

        }

    });

    // FORM RESET

    form.addEventListener("reset", () => {

        setTimeout(() => {

            clearValidation(form);

            previewImage.src = "";

            previewButton.disabled = true;

            closeModal(imageModal);

        }, 0);

    });

});