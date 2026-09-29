// LOGIN PAGE

document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("loginForm");

    if (!form) return;

    // INPUTS

    const email = document.getElementById("email");
    const password = document.getElementById("password");

    const showPassword = document.getElementById("showPassword");
    const rememberMe = document.getElementById("rememberMe");

    // ERROR LABELS

    const emailError = document.getElementById("emailError");
    const passwordError = document.getElementById("passwordError");

    // EMAIL VALIDATION

    function validateEmail() {

        const value = email.value.trim();

        const emailPattern =
                /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

        if (value === "") {

            setError(
                    email,
                    emailError,
                    "Email address is required."
            );

            return false;
        }

        if (!emailPattern.test(value)) {

            setError(
                    email,
                    emailError,
                    "Enter a valid email address."
            );

            return false;
        }

        clearError(
                email,
                emailError
        );

        return true;
    }

    // PASSWORD VALIDATION

    function validatePassword() {

        if (password.value.length === 0) {

            setError(
                    password,
                    passwordError,
                    "Password is required."
            );

            return false;
        }

        if (password.value.length < 8) {

            setError(
                    password,
                    passwordError,
                    "Password must contain at least 8 characters."
            );

            return false;
        }

        clearError(
                password,
                passwordError
        );

        return true;
    }

    // EMAIL INPUT

    email.addEventListener("input", () => {

        validateEmail();

    });

    email.addEventListener("blur", () => {

        validateEmail();

    });

    // PASSWORD INPUT

    password.addEventListener("input", () => {

        validatePassword();

    });

    password.addEventListener("blur", () => {

        validatePassword();

    });

    // SHOW PASSWORD

    showPassword.addEventListener("change", () => {

        password.type =
                showPassword.checked
                        ? "text"
                        : "password";

    });

    // REMEMBER ME

    rememberMe.addEventListener("change", () => {

        console.log(
                "Remember Me:",
                rememberMe.checked
        );

    });

    // FORM SUBMIT

    form.addEventListener("submit", (event) => {

        const emailValid = validateEmail();

        const passwordValid = validatePassword();

        if (!emailValid || !passwordValid) {

            event.preventDefault();

            return;

        }

        console.log("Login Form Submitted");

    });

    // RESET

    form.addEventListener("reset", () => {

        clearValidation(form);

    });

});