// PASSWORD PAGES

document.addEventListener("DOMContentLoaded", () => {

    // FORGOT PASSWORD

    const forgotForm = document.getElementById("forgotPasswordForm");

    if (forgotForm) {

        const usernameOrEmail =
            document.getElementById("usernameOrEmail");

        const error =
            document.getElementById("usernameOrEmailError");

        usernameOrEmail.addEventListener("input", () => {

            const value = usernameOrEmail.value.trim();

            if (value.length < 3) {

                setError(
                    usernameOrEmail,
                    error,
                    "Enter your Username or Registered Email."
                );

                return;

            }

            clearError(
                usernameOrEmail,
                error
            );

        });

        forgotForm.addEventListener("submit", (event) => {

            if (usernameOrEmail.value.trim().length < 3) {

                event.preventDefault();

                setError(
                    usernameOrEmail,
                    error,
                    "Enter your Username or Registered Email."
                );

            }

        });

    }

    // UPDATE PASSWORD

    const updateForm =
        document.getElementById("updatePasswordForm");

    if (updateForm) {

        const newPassword =
            document.getElementById("newPassword");

        const confirmPassword =
            document.getElementById("confirmPassword");

        const showPassword =
            document.getElementById("showPassword");

        const passwordError =
            document.getElementById("newPasswordError");

        const confirmError =
            document.getElementById("confirmPasswordError");

        newPassword.addEventListener("input", () => {

            if (newPassword.value.length < 8) {

                setError(
                    newPassword,
                    passwordError,
                    "Password must contain at least 8 characters."
                );

                return;

            }

            clearError(
                newPassword,
                passwordError
            );

        });

        confirmPassword.addEventListener("input", () => {

            if (confirmPassword.value !== newPassword.value) {

                setError(
                    confirmPassword,
                    confirmError,
                    "Passwords do not match."
                );

                return;

            }

            clearError(
                confirmPassword,
                confirmError
            );

        });

        showPassword.addEventListener("change", () => {

            const type =
                showPassword.checked
                    ? "text"
                    : "password";

            newPassword.type = type;
            confirmPassword.type = type;

        });

        updateForm.addEventListener("submit", (event) => {

            let valid = true;

            if (newPassword.value.length < 8) {

                setError(
                    newPassword,
                    passwordError,
                    "Password must contain at least 8 characters."
                );

                valid = false;

            }

            if (confirmPassword.value !== newPassword.value) {

                setError(
                    confirmPassword,
                    confirmError,
                    "Passwords do not match."
                );

                valid = false;

            }

            if (!valid) {

                event.preventDefault();

            }

        });

        updateForm.addEventListener("reset", () => {

            setTimeout(() => {

                clearValidation(updateForm);

            }, 0);

        });

    }

});