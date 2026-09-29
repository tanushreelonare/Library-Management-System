document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("addBookForm");

    if (!form) {
        return;
    }

    const isbn = document.getElementById("isbn");
    const title = document.getElementById("title");
    const author = document.getElementById("author");
    const category = document.getElementById("category");
    const publisher = document.getElementById("publisher");
    const publicationYear = document.getElementById("publicationYear");
    const language = document.getElementById("language");
    const totalCopies = document.getElementById("totalCopies");
    const availableCopies = document.getElementById("availableCopies");
    const description = document.getElementById("description");

    const addBookButton = form.querySelector(".add-book-btn");
    const resetButton = form.querySelector(".reset-btn");

    // Clear validation errors
    function clearErrors() {

        document.querySelectorAll(".field-error").forEach(function (error) {
            error.textContent = "";
        });

        document.querySelectorAll(".form-group input, .form-group textarea").forEach(function (field) {
            field.classList.remove("input-error");
        });
    }

    // Display field validation error
    function showError(field, message) {

        const errorElement = document.getElementById(field.id + "Error");

        if (errorElement) {
            errorElement.textContent = message;
        }

        field.classList.add("input-error");
    }

    // Validate form data
    function validateForm() {

        clearErrors();

        let isValid = true;

        if (isbn.value.trim() === "") {
            showError(isbn, "ISBN is required.");
            isValid = false;
        }

        if (title.value.trim() === "") {
            showError(title, "Book title is required.");
            isValid = false;
        }

        if (author.value.trim() === "") {
            showError(author, "Author is required.");
            isValid = false;
        }

        if (category.value.trim() === "") {
            showError(category, "Category is required.");
            isValid = false;
        }

        if (publisher.value.trim() === "") {
            showError(publisher, "Publisher is required.");
            isValid = false;
        }

        const year = Number(publicationYear.value);

        if (
            publicationYear.value === "" ||
            !Number.isInteger(year) ||
            year < 1000 ||
            year > 2100
        ) {
            showError(
                publicationYear,
                "Enter a valid publication year."
            );
            isValid = false;
        }

        if (language.value.trim() === "") {
            showError(language, "Language is required.");
            isValid = false;
        }

        const total = Number(totalCopies.value);
        const available = Number(availableCopies.value);

        if (
            totalCopies.value === "" ||
            !Number.isInteger(total) ||
            total < 0
        ) {
            showError(
                totalCopies,
                "Total copies cannot be negative."
            );
            isValid = false;
        }

        if (
            availableCopies.value === "" ||
            !Number.isInteger(available) ||
            available < 0
        ) {
            showError(
                availableCopies,
                "Available copies cannot be negative."
            );
            isValid = false;
        }

        if (
            Number.isInteger(total) &&
            Number.isInteger(available) &&
            total >= 0 &&
            available >= 0 &&
            available > total
        ) {
            showError(
                availableCopies,
                "Available copies cannot be greater than total copies."
            );
            isValid = false;
        }

        return isValid;
    }

    // Submit book
    form.addEventListener("submit", async function (event) {

        event.preventDefault();

        if (!validateForm()) {
            return;
        }

        const bookData = {
            isbn: isbn.value.trim(),
            title: title.value.trim(),
            author: author.value.trim(),
            category: category.value.trim(),
            publisher: publisher.value.trim(),
            publicationYear: Number(publicationYear.value),
            language: language.value.trim(),
            totalCopies: Number(totalCopies.value),
            availableCopies: Number(availableCopies.value),
            description: description.value.trim()
        };

        if (addBookButton) {
            addBookButton.disabled = true;
            addBookButton.textContent = "Adding...";
        }

        try {

            const response = await fetch(form.action, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(bookData)
            });

            const responseData = await response.json().catch(function () {
                return null;
            });

            if (!response.ok) {

                let errorMessage = "Unable to add the book.";

                if (Array.isArray(responseData)) {
                    errorMessage = responseData.join("<br>");
                } else if (typeof responseData === "string") {
                    errorMessage = responseData;
                } else if (responseData && responseData.message) {
                    errorMessage = responseData.message;
                }

                throw new Error(errorMessage);
            }

            await Swal.fire({
                icon: "success",
                title: "Book Added",
                text: "The book has been added successfully.",
                confirmButtonText: "View Books"
            });

            window.location.href = "/librarian/books";

        } catch (error) {

            Swal.fire({
                icon: "error",
                title: "Unable to Add Book",
                html: error.message || "Something went wrong while adding the book.",
                confirmButtonText: "OK"
            });

        } finally {

            if (addBookButton) {
                addBookButton.disabled = false;
                addBookButton.textContent = "Add Book";
            }

        }

    });

    // Reset form and validation messages
    if (resetButton) {

        resetButton.addEventListener("click", function () {

            setTimeout(function () {
                clearErrors();
            }, 0);

        });

    }

});