// COMMON VALIDATION FUNCTIONS

// Convert input text to uppercase

function toUpperCase(input) {

    input.value = input.value.toUpperCase();

}

// Allow letters, spaces, dots and hyphens

function lettersOnly(input) {

    input.value = input.value.replace(
        /[^A-Za-z\s.'-]/g,
        ""
    );

}

// Allow numbers only

function numbersOnly(input) {

    input.value = input.value.replace(
        /[^0-9]/g,
        ""
    );

}

// Validate email

function isValidEmail(value) {

    const emailPattern =
        /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    return emailPattern.test(value.trim());

}

// Validate mobile number

function isValidMobile(value) {

    return /^[0-9]{10}$/.test(
        value.trim()
    );

}

// Set validation error

function setError(
    input,
    errorElement,
    message
) {

    if (input) {

        input.classList.add(
            "input-invalid"
        );

        input.classList.remove(
            "input-valid"
        );

    }

    if (errorElement) {

        errorElement.textContent = message;

        errorElement.style.display = "block";

    }

}

// Clear validation error

function clearError(
    input,
    errorElement
) {

    if (input) {

        input.classList.remove(
            "input-invalid"
        );

        input.classList.add(
            "input-valid"
        );

    }

    if (errorElement) {

        errorElement.textContent = "";

        errorElement.style.display = "none";

    }

}

// Toggle password visibility

function togglePassword(
    password,
    confirmPassword,
    checkbox
) {

    const type = checkbox.checked
        ? "text"
        : "password";

    if (password) {

        password.type = type;

    }

    if (confirmPassword) {

        confirmPassword.type = type;

    }

}

// Validate uploaded image

function validateImage(file) {

    if (!file) {

        return false;

    }

    const allowedTypes = [
        "image/jpeg",
        "image/png"
    ];

    return allowedTypes.includes(
        file.type
    );

}

// Preview selected image

function previewSelectedImage(
    file,
    previewImage
) {

    if (!file || !previewImage) {

        return;

    }

    const reader = new FileReader();

    reader.onload = function (event) {

        previewImage.src =
            event.target.result;

    };

    reader.readAsDataURL(file);

}

// Open modal

function openModal(modal) {

    if (!modal) {

        return;

    }

    modal.style.display = "flex";

}

// Close modal

function closeModal(modal) {

    if (!modal) {

        return;

    }

    modal.style.display = "none";

}

// Clear all validation

function clearValidation(form) {

    if (!form) {

        return;

    }

    const invalidInputs =
        form.querySelectorAll(
            ".input-invalid"
        );

    invalidInputs.forEach(input => {

        input.classList.remove(
            "input-invalid"
        );

    });

    const validInputs =
        form.querySelectorAll(
            ".input-valid"
        );

    validInputs.forEach(input => {

        input.classList.remove(
            "input-valid"
        );

    });

    const errorMessages =
        form.querySelectorAll(
            ".error-message"
        );

    errorMessages.forEach(error => {

        error.textContent = "";

        error.style.display = "none";

    });

}