// PASSWORD PAGES

document.addEventListener("DOMContentLoaded", () => {

    // FORGOT PASSWORD

    const forgotForm =
        document.getElementById("forgotPasswordForm");

    if (forgotForm) {

        const email =
            document.getElementById("email");

        const emailError =
            document.getElementById("emailError");

        email.addEventListener("input", () => {

            const value =
                email.value.trim();

            if (value === "") {

                setError(
                    email,
                    emailError,
                    "Email address is required."
                );

                return;

            }

            if (!isValidEmail(value)) {

                setError(
                    email,
                    emailError,
                    "Enter a valid email address."
                );

                return;

            }

            clearError(
                email,
                emailError
            );

        });

        forgotForm.addEventListener(
            "submit",
            (event) => {

                const value =
                    email.value.trim();

                if (value === "") {

                    event.preventDefault();

                    setError(
                        email,
                        emailError,
                        "Email address is required."
                    );

                    return;

                }

                if (!isValidEmail(value)) {

                    event.preventDefault();

                    setError(
                        email,
                        emailError,
                        "Enter a valid email address."
                    );

                }

            }
        );

        forgotForm.addEventListener(
            "reset",
            () => {

                setTimeout(() => {

                    clearValidation(
                        forgotForm
                    );

                }, 0);

            }
        );

    }

    // UPDATE PASSWORD

    const updateForm =
        document.getElementById("updatePasswordForm");

    if (updateForm) {

        const password =
            document.getElementById("password");

        const confirmPassword =
            document.getElementById("confirmPassword");

        const showPassword =
            document.getElementById("showPassword");

        const passwordError =
            document.getElementById("passwordError");

        const confirmPasswordError =
            document.getElementById("confirmPasswordError");

        // PASSWORD

        password.addEventListener("input", () => {

            if (password.value.length === 0) {

                setError(
                    password,
                    passwordError,
                    "Password is required."
                );

                return;

            }

            if (password.value.length < 8) {

                setError(
                    password,
                    passwordError,
                    "Password must contain at least 8 characters."
                );

                return;

            }

            clearError(
                password,
                passwordError
            );

            if (
                confirmPassword.value.length > 0
                && confirmPassword.value !== password.value
            ) {

                setError(
                    confirmPassword,
                    confirmPasswordError,
                    "Passwords do not match."
                );

            } else if (
                confirmPassword.value.length > 0
            ) {

                clearError(
                    confirmPassword,
                    confirmPasswordError
                );

            }

        });

        // CONFIRM PASSWORD

        confirmPassword.addEventListener(
            "input",
            () => {

                if (
                    confirmPassword.value.length === 0
                ) {

                    setError(
                        confirmPassword,
                        confirmPasswordError,
                        "Confirm Password is required."
                    );

                    return;

                }

                if (
                    confirmPassword.value
                    !== password.value
                ) {

                    setError(
                        confirmPassword,
                        confirmPasswordError,
                        "Passwords do not match."
                    );

                    return;

                }

                clearError(
                    confirmPassword,
                    confirmPasswordError
                );

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

        // FORM SUBMISSION

        updateForm.addEventListener(
            "submit",
            (event) => {

                let valid = true;

                if (
                    password.value.length === 0
                ) {

                    setError(
                        password,
                        passwordError,
                        "Password is required."
                    );

                    valid = false;

                } else if (
                    password.value.length < 8
                ) {

                    setError(
                        password,
                        passwordError,
                        "Password must contain at least 8 characters."
                    );

                    valid = false;

                }

                if (
                    confirmPassword.value.length === 0
                ) {

                    setError(
                        confirmPassword,
                        confirmPasswordError,
                        "Confirm Password is required."
                    );

                    valid = false;

                } else if (
                    confirmPassword.value
                    !== password.value
                ) {

                    setError(
                        confirmPassword,
                        confirmPasswordError,
                        "Passwords do not match."
                    );

                    valid = false;

                }

                if (!valid) {

                    event.preventDefault();

                }

            }
        );

        // RESET FORM

        updateForm.addEventListener(
            "reset",
            () => {

                setTimeout(() => {

                    clearValidation(
                        updateForm
                    );

                    password.type =
                        "password";

                    confirmPassword.type =
                        "password";

                    if (showPassword) {

                        showPassword.checked =
                            false;

                    }

                }, 0);

            }
        );

    }

});