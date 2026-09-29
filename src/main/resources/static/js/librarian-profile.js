document.addEventListener("DOMContentLoaded", function () {

    const editProfileButton =
        document.getElementById("editProfileButton");

    const saveProfileButton =
        document.getElementById("saveProfileButton");

    const cancelEditButton =
        document.getElementById("cancelEditButton");

    const profileForm =
        document.getElementById("librarianProfileForm");

    const librarianImage =
        document.getElementById("librarianImage");

    const profileImagePreview =
        document.getElementById("profileImagePreview");

    const defaultProfileImage =
        document.getElementById("defaultProfileImage");

    const editableFields =
        document.querySelectorAll(".editable-field");

    let editMode = false;

    // Store original profile values

    const originalValues = {};

    editableFields.forEach(function (field) {
        originalValues[field.id] = field.value;
    });

    // Enable Edit Mode

    editProfileButton.addEventListener("click", function () {

        editMode = true;

        editableFields.forEach(function (field) {
            field.removeAttribute("readonly");
        });

        librarianImage.disabled = false;

        editProfileButton.style.display = "none";
        saveProfileButton.style.display = "inline-flex";
        cancelEditButton.style.display = "inline-flex";

    });

    // Cancel Edit Mode

    cancelEditButton.addEventListener("click", function () {

        editableFields.forEach(function (field) {
            field.value = originalValues[field.id];
            field.setAttribute("readonly", "readonly");
        });

        librarianImage.value = "";

        editMode = false;

        saveProfileButton.style.display = "none";
        cancelEditButton.style.display = "none";
        editProfileButton.style.display = "inline-flex";

        clearValidation();

    });

    // Profile Image Preview

    librarianImage.addEventListener("change", function () {

        const file = librarianImage.files[0];

        if (!file) {
            return;
        }

        if (!file.type.startsWith("image/")) {

            setError(
                librarianImage,
                "Please select a valid image file."
            );

            librarianImage.value = "";

            return;
        }

        const reader = new FileReader();

        reader.onload = function (event) {

            if (profileImagePreview) {

                profileImagePreview.src =
                    event.target.result;

                profileImagePreview.style.display =
                    "block";
            }

            if (defaultProfileImage) {

                defaultProfileImage.style.display =
                    "none";
            }
        };

        reader.readAsDataURL(file);

    });

    // Form Validation

    profileForm.addEventListener("submit", function (event) {

        if (!editMode) {

            event.preventDefault();

            return;
        }

        clearValidation();

        let isValid = true;

        const librarianName =
            document.getElementById("librarianName");

        const mobile =
            document.getElementById("mobile");

        const designation =
            document.getElementById("designation");

        const qualification =
            document.getElementById("qualification");

        const experience =
            document.getElementById("experience");

        const address =
            document.getElementById("address");

        // Librarian Name

        if (librarianName.value.trim() === "") {

            setError(
                librarianName,
                "Librarian Name is required."
            );

            isValid = false;

        } else if (
            librarianName.value.trim().length < 4
        ) {

            setError(
                librarianName,
                "Name must be at least 4  characters."
            );

            isValid = false;
        }

        // Mobile Number

        if (mobile.value.trim() === "") {

            setError(
                mobile,
                "Mobile Number is required."
            );

            isValid = false;

        } else if (
            !/^[0-9]{10}$/.test(mobile.value.trim())
        ) {

            setError(
                mobile,
                "Mobile Number must contain exactly 10 digits."
            );

            isValid = false;
        }

        // Designation

        if (designation.value.trim() === "") {

            setError(
                designation,
                "Designation is required."
            );

            isValid = false;
        }

        // Qualification

        if (qualification.value.trim() === "") {

            setError(
                qualification,
                "Qualification is required."
            );

            isValid = false;
        }

        // Experience

        const experienceValue =
            experience.value.trim();

        if (experienceValue === "") {

            setError(
                experience,
                "Experience is required."
            );

            isValid = false;

        } else {

            const experienceNumber =
                Number(experienceValue);

            if (
                experienceNumber < 0 ||
                experienceNumber > 50
            ) {

                setError(
                    experience,
                    "Experience must be between 0 and 50 years."
                );

                isValid = false;
            }
        }

        // Address

        if (address.value.trim() === "") {

            setError(
                address,
                "Address is required."
            );

            isValid = false;
        }

        if (!isValid) {

            event.preventDefault();

            return;
        }

        saveProfileButton.disabled = true;
        saveProfileButton.textContent = "Saving...";

    });

});