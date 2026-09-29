/* Librarian Manage Books */

document.addEventListener("DOMContentLoaded", function () {

    const searchForm =
        document.getElementById("bookSearchForm");

    const searchInput =
        document.getElementById("bookSearchInput");

    const statusFilter =
        document.getElementById("statusFilter");

    const availabilityFilter =
        document.getElementById("availabilityFilter");

    const clearFiltersButton =
        document.getElementById("clearFiltersButton");

    const bookRows =
        Array.from(
            document.querySelectorAll(".book-row")
        );

    const bookCount =
        document.getElementById("bookCount");

    const emptyStateRow =
        document.getElementById("emptyStateRow");

    const paginationSummary =
        document.getElementById("paginationSummary");

    const paginationPages =
        document.getElementById("paginationPages");

    const previousPageButton =
        document.getElementById("previousPageButton");

    const nextPageButton =
        document.getElementById("nextPageButton");


    /* Pagination */

    const booksPerPage = 10;

    let currentPage = 1;

    let filteredRows = [...bookRows];


    /* Book Modal */

    const bookModal =
        document.getElementById("bookModal");

    const closeBookModal =
        document.getElementById("closeBookModal");

    const closeViewButton =
        document.getElementById("closeViewButton");

    const editBookButton =
        document.getElementById("editBookButton");

    const cancelEditButton =
        document.getElementById("cancelEditButton");

    const saveBookButton =
        document.getElementById("saveBookButton");

    const bookViewMode =
        document.getElementById("bookViewMode");

    const editBookForm =
        document.getElementById("editBookForm");

    const viewModeActions =
        document.getElementById("viewModeActions");

    const editModeActions =
        document.getElementById("editModeActions");

    const bookModalTitle =
        document.getElementById("bookModalTitle");


    let currentBook = null;

    let isSavingBook = false;


    /* Initialize */

    updateBookList();


    /* Search */

    if (searchForm) {

        searchForm.addEventListener(
            "submit",
            function (event) {

                event.preventDefault();

                currentPage = 1;

                updateBookList();

            }
        );

    }


    /* Search Input */

    if (searchInput) {

        searchInput.addEventListener(
            "input",
            function () {

                currentPage = 1;

                updateBookList();

            }
        );

    }


    /* Status Filter */

    if (statusFilter) {

        statusFilter.addEventListener(
            "change",
            function () {

                currentPage = 1;

                updateBookList();

            }
        );

    }


    /* Availability Filter */

    if (availabilityFilter) {

        availabilityFilter.addEventListener(
            "change",
            function () {

                currentPage = 1;

                updateBookList();

            }
        );

    }


    /* Clear Filters */

    if (clearFiltersButton) {

        clearFiltersButton.addEventListener(
            "click",
            function () {

                if (searchInput) {
                    searchInput.value = "";
                }

                if (statusFilter) {
                    statusFilter.value = "ALL";
                }

                if (availabilityFilter) {
                    availabilityFilter.value = "ALL";
                }

                currentPage = 1;

                updateBookList();

            }
        );

    }


    /* View / Edit Buttons */

    document.querySelectorAll(
        ".view-edit-button"
    ).forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const bookId =
                    this.getAttribute(
                        "data-book-id"
                    );

                if (!bookId) {
                    return;
                }

                openBookDetails(bookId);

            }
        );

    });


    /* Toggle Status Buttons */

    document.querySelectorAll(
        ".toggle-status-button"
    ).forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const bookId =
                    this.getAttribute(
                        "data-book-id"
                    );

                const active =
                    this.getAttribute(
                        "data-active"
                    ) === "true";

                if (!bookId) {
                    return;
                }

                confirmStatusToggle(
                    bookId,
                    active
                );

            }
        );

    });


    /* Delete Buttons */

    document.querySelectorAll(
        ".delete-book-button"
    ).forEach(function (button) {

        button.addEventListener(
            "click",
            function () {

                const bookId =
                    this.getAttribute(
                        "data-book-id"
                    );

                if (!bookId) {
                    return;
                }

                confirmBookDelete(
                    bookId
                );

            }
        );

    });


    /* Close Modal */

    if (closeBookModal) {

        closeBookModal.addEventListener(
            "click",
            function () {

                closeBookDetailsModal();

            }
        );

    }


    if (closeViewButton) {

        closeViewButton.addEventListener(
            "click",
            function () {

                closeBookDetailsModal();

            }
        );

    }


    /* Enter Edit Mode */

    if (editBookButton) {

        editBookButton.addEventListener(
            "click",
            function () {

                enterEditMode();

            }
        );

    }


    /* Cancel Edit */

    if (cancelEditButton) {

        cancelEditButton.addEventListener(
            "click",
            function () {

                exitEditMode();

            }
        );

    }


    /* Save Book */

    if (saveBookButton) {

        saveBookButton.addEventListener(
            "click",
            function () {

                saveBook();

            }
        );

    }


    /* Close Modal On Overlay */

    if (bookModal) {

        const overlay =
            bookModal.querySelector(
                ".book-modal-overlay"
            );

        if (overlay) {

            overlay.addEventListener(
                "click",
                function (event) {

                    if (
                        event.target === this &&
                        !isSavingBook
                    ) {

                        closeBookDetailsModal();

                    }

                }
            );

        }

    }


    /* Escape Key */

    document.addEventListener(
        "keydown",
        function (event) {

            if (
                event.key === "Escape" &&
                bookModal &&
                bookModal.classList.contains(
                    "active"
                ) &&
                !isSavingBook
            ) {

                closeBookDetailsModal();

            }

        }
    );


    /* Update Book List */

    function updateBookList() {

        const searchTerm =
            searchInput
                ? searchInput.value
                    .trim()
                    .toLowerCase()
                : "";

        const selectedStatus =
            statusFilter
                ? statusFilter.value
                : "ALL";

        const selectedAvailability =
            availabilityFilter
                ? availabilityFilter.value
                : "ALL";


        filteredRows =
            bookRows.filter(function (row) {

                const isbn =
                    getRowValue(
                        row,
                        "data-isbn"
                    );

                const title =
                    getRowValue(
                        row,
                        "data-title"
                    );

                const author =
                    getRowValue(
                        row,
                        "data-author"
                    );

                const category =
                    getRowValue(
                        row,
                        "data-category"
                    );

                const active =
                    getRowValue(
                        row,
                        "data-active"
                    ) === "true";

                const availableCopies =
                    parseInt(
                        getRowValue(
                            row,
                            "data-available-copies"
                        ),
                        10
                    ) || 0;


                const searchableText =
                    (
                        isbn +
                        " " +
                        title +
                        " " +
                        author +
                        " " +
                        category
                    ).toLowerCase();


                const matchesSearch =
                    searchTerm === "" ||
                    searchableText.includes(
                        searchTerm
                    );


                const matchesStatus =
                    selectedStatus === "ALL" ||
                    (
                        selectedStatus === "ACTIVE" &&
                        active
                    ) ||
                    (
                        selectedStatus === "INACTIVE" &&
                        !active
                    );


                const matchesAvailability =
                    selectedAvailability === "ALL" ||
                    (
                        selectedAvailability === "AVAILABLE" &&
                        availableCopies > 0
                    ) ||
                    (
                        selectedAvailability === "OUT_OF_STOCK" &&
                        availableCopies === 0
                    );


                return (
                    matchesSearch &&
                    matchesStatus &&
                    matchesAvailability
                );

            });


        renderCurrentPage();

    }


    /* Render Current Page */

    function renderCurrentPage() {

        const totalBooks =
            filteredRows.length;


        const totalPages =
            Math.max(
                1,
                Math.ceil(
                    totalBooks /
                    booksPerPage
                )
            );


        if (
            currentPage >
            totalPages
        ) {

            currentPage =
                totalPages;

        }


        const startIndex =
            (
                currentPage - 1
            ) *
            booksPerPage;


        const endIndex =
            Math.min(
                startIndex +
                booksPerPage,
                totalBooks
            );


        const visibleRows =
            new Set(
                filteredRows.slice(
                    startIndex,
                    endIndex
                )
            );


        bookRows.forEach(function (row) {

            row.style.display =
                visibleRows.has(row)
                    ? ""
                    : "none";

        });


        if (emptyStateRow) {

            emptyStateRow.style.display =
                totalBooks === 0
                    ? ""
                    : "none";

        }


        if (bookCount) {

            bookCount.textContent =
                totalBooks;

        }


        updatePagination(
            totalBooks,
            totalPages,
            startIndex,
            endIndex
        );

    }


    /* Pagination */

    function updatePagination(
        totalBooks,
        totalPages,
        startIndex,
        endIndex
    ) {

        if (paginationSummary) {

            paginationSummary.textContent =
                totalBooks === 0
                    ? "Showing 0–0 of 0 books"
                    : "Showing " +
                      (startIndex + 1) +
                      "–" +
                      endIndex +
                      " of " +
                      totalBooks +
                      " books";

        }


        if (previousPageButton) {

            previousPageButton.disabled =
                currentPage <= 1;

        }


        if (nextPageButton) {

            nextPageButton.disabled =
                currentPage >= totalPages;

        }


        renderPageButtons(
            totalPages
        );

    }


    /* Page Buttons */

    function renderPageButtons(
        totalPages
    ) {

        if (!paginationPages) {
            return;
        }


        paginationPages.innerHTML = "";


        if (totalPages <= 1) {
            return;
        }


        const maxVisiblePages = 5;


        let startPage =
            Math.max(
                1,
                currentPage -
                Math.floor(
                    maxVisiblePages / 2
                )
            );


        let endPage =
            Math.min(
                totalPages,
                startPage +
                maxVisiblePages -
                1
            );


        if (
            endPage -
            startPage +
            1 <
            maxVisiblePages
        ) {

            startPage =
                Math.max(
                    1,
                    endPage -
                    maxVisiblePages +
                    1
                );

        }


        for (
            let page = startPage;
            page <= endPage;
            page++
        ) {

            const button =
                document.createElement(
                    "button"
                );


            button.type =
                "button";


            button.className =
                "pagination-page-button";


            if (
                page === currentPage
            ) {

                button.classList.add(
                    "active"
                );

            }


            button.textContent =
                page;


            button.addEventListener(
                "click",
                function () {

                    currentPage =
                        page;

                    renderCurrentPage();

                    window.scrollTo({
                        top: 0,
                        behavior: "smooth"
                    });

                }
            );


            paginationPages.appendChild(
                button
            );

        }

    }


    /* Previous Page */

    if (previousPageButton) {

        previousPageButton.addEventListener(
            "click",
            function () {

                if (currentPage > 1) {

                    currentPage--;

                    renderCurrentPage();

                }

            }
        );

    }


    /* Next Page */

    if (nextPageButton) {

        nextPageButton.addEventListener(
            "click",
            function () {

                const totalPages =
                    Math.max(
                        1,
                        Math.ceil(
                            filteredRows.length /
                            booksPerPage
                        )
                    );


                if (
                    currentPage <
                    totalPages
                ) {

                    currentPage++;

                    renderCurrentPage();

                }

            }
        );

    }


    /* Get Row Value */

    function getRowValue(
        row,
        attribute
    ) {

        return (
            row.getAttribute(
                attribute
            ) || ""
        );

    }


    /* Open Book Details */

    function openBookDetails(
        bookId
    ) {

        fetch(
            "/librarian/books/" +
            encodeURIComponent(
                bookId
            )
        )
            .then(function (response) {

                if (!response.ok) {

                    throw new Error(
                        "Unable to load book details."
                    );

                }

                return response.json();

            })
            .then(function (book) {

                currentBook =
                    book;

                populateViewMode(
                    book
                );

                exitEditMode();

                openBookDetailsModal();

            })
            .catch(function (error) {

                console.error(
                    "Book details error:",
                    error
                );

                showError(
                    "Unable to load book details. Please try again."
                );

            });

    }


    /* Populate View Mode */

    function populateViewMode(
        book
    ) {

        setText(
            "viewIsbn",
            book.isbn
        );

        setText(
            "viewTitle",
            book.title
        );

        setText(
            "viewAuthor",
            book.author
        );

        setText(
            "viewCategory",
            book.category
        );

        setText(
            "viewPublisher",
            book.publisher
        );

        setText(
            "viewPublicationYear",
            book.publicationYear
        );

        setText(
            "viewLanguage",
            book.language
        );

        setText(
            "viewTotalCopies",
            book.totalCopies
        );

        setText(
            "viewAvailableCopies",
            book.availableCopies
        );


        const issuedCopies =
            Math.max(
                0,
                Number(book.totalCopies) -
                Number(book.availableCopies)
            );


        setText(
            "viewIssuedCopies",
            issuedCopies
        );


        setText(
            "viewStatus",
            book.active
                ? "Active"
                : "Inactive"
        );


        setText(
            "viewDescription",
            book.description ||
            "No description available."
        );

    }


    /* Set Text */

    function setText(
        elementId,
        value
    ) {

        const element =
            document.getElementById(
                elementId
            );


        if (element) {

            element.textContent =
                value !== null &&
                value !== undefined
                    ? value
                    : "-";

        }

    }


    /* Open Modal */

    function openBookDetailsModal() {

        if (!bookModal) {
            return;
        }


        bookModal.classList.add(
            "active"
        );


        bookModal.setAttribute(
            "aria-hidden",
            "false"
        );


        document.body.style.overflow =
            "hidden";

    }


    /* Close Modal */

    function closeBookDetailsModal() {

        if (!bookModal) {
            return;
        }


        bookModal.classList.remove(
            "active"
        );


        bookModal.setAttribute(
            "aria-hidden",
            "true"
        );


        document.body.style.overflow =
            "";

    }


    /* Enter Edit Mode */

    function enterEditMode() {

        if (!currentBook) {
            return;
        }


        bookModalTitle.textContent =
            "Edit Book";


        bookViewMode.style.display =
            "none";


        editBookForm.style.display =
            "block";


        viewModeActions.style.display =
            "none";


        editModeActions.style.display =
            "flex";


        populateEditForm(
            currentBook
        );

    }


    /* Exit Edit Mode */

    function exitEditMode() {

        if (!bookViewMode ||
            !editBookForm) {

            return;
        }


        bookModalTitle.textContent =
            "Book Details";


        bookViewMode.style.display =
            "block";


        editBookForm.style.display =
            "none";


        viewModeActions.style.display =
            "flex";


        editModeActions.style.display =
            "none";

    }


    /* Populate Edit Form */

    function populateEditForm(
        book
    ) {

        setInputValue(
            "editBookId",
            book.id
        );

        setInputValue(
            "editIsbn",
            book.isbn
        );

        setInputValue(
            "editTitle",
            book.title
        );

        setInputValue(
            "editAuthor",
            book.author
        );

        setInputValue(
            "editCategory",
            book.category
        );

        setInputValue(
            "editPublisher",
            book.publisher
        );

        setInputValue(
            "editPublicationYear",
            book.publicationYear
        );

        setInputValue(
            "editLanguage",
            book.language
        );

        setInputValue(
            "editTotalCopies",
            book.totalCopies
        );

        setInputValue(
            "editAvailableCopies",
            book.availableCopies
        );


        const description =
            document.getElementById(
                "editDescription"
            );


        if (description) {

            description.value =
                book.description ||
                "";

        }

    }


    /* Set Input Value */

    function setInputValue(
        elementId,
        value
    ) {

        const element =
            document.getElementById(
                elementId
            );


        if (element) {

            element.value =
                value !== null &&
                value !== undefined
                    ? value
                    : "";

        }

    }


    /* Save Book */

    function saveBook() {

        if (isSavingBook) {
            return;
        }


        if (
            !editBookForm.checkValidity()
        ) {

            editBookForm.reportValidity();

            return;

        }


        const bookId =
            document.getElementById(
                "editBookId"
            ).value;


        const totalCopies =
            parseInt(
                document.getElementById(
                    "editTotalCopies"
                ).value,
                10
            );


        const availableCopies =
            parseInt(
                document.getElementById(
                    "editAvailableCopies"
                ).value,
                10
            );


        if (
            isNaN(totalCopies) ||
            totalCopies < 0
        ) {

            showWarning(
                "Total copies must be zero or greater."
            );

            return;

        }


        if (
            totalCopies <
            availableCopies
        ) {

            showWarning(
                "Total copies cannot be less than available copies."
            );

            return;

        }


        const updatedBook = {

            isbn:
                document.getElementById(
                    "editIsbn"
                ).value.trim(),

            title:
                document.getElementById(
                    "editTitle"
                ).value.trim(),

            author:
                document.getElementById(
                    "editAuthor"
                ).value.trim(),

            category:
                document.getElementById(
                    "editCategory"
                ).value.trim(),

            publisher:
                document.getElementById(
                    "editPublisher"
                ).value.trim(),

            publicationYear:
                parseInt(
                    document.getElementById(
                        "editPublicationYear"
                    ).value,
                    10
                ),

            language:
                document.getElementById(
                    "editLanguage"
                ).value.trim(),

            totalCopies:
                totalCopies,

            availableCopies:
                availableCopies,

            description:
                document.getElementById(
                    "editDescription"
                ).value.trim(),

            active:
                currentBook
                    ? currentBook.active
                    : true

        };


        Swal.fire({

            icon: "question",

            title: "Save Changes?",

            text:
                "Are you sure you want to update this book?",

            showCancelButton: true,

            confirmButtonText:
                "Yes, Save Changes",

            cancelButtonText:
                "Cancel",

            confirmButtonColor:
                "#244f87",

            cancelButtonColor:
                "#6c757d"

        }).then(function (result) {

            if (
                result.isConfirmed
            ) {

                performBookUpdate(
                    bookId,
                    updatedBook
                );

            }

        });

    }


    /* Perform Book Update */

    function performBookUpdate(
        bookId,
        updatedBook
    ) {

        isSavingBook =
            true;


        setSaveButtonLoading(
            true
        );


        /*
         * Close the application modal before
         * showing any success or error alert.
         */
        closeBookDetailsModal();


        fetch(
            "/librarian/books/" +
            encodeURIComponent(
                bookId
            ),
            {
                method: "PUT",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(
                        updatedBook
                    )
            }
        )
            .then(function (response) {

                if (!response.ok) {

                    return response
                        .text()
                        .then(function (message) {

                            throw new Error(
                                message ||
                                "Unable to update the book."
                            );

                        });

                }

                return response.json();

            })
            .then(function (book) {

                currentBook =
                    book;


                updateTableRow(
                    book
                );


                isSavingBook =
                    false;


                setSaveButtonLoading(
                    false
                );


                Swal.fire({

                    icon: "success",

                    title:
                        "Book Updated",

                    text:
                        "The book has been updated successfully.",

                    confirmButtonColor:
                        "#244f87",

                    timer:
                        1800,

                    timerProgressBar:
                        true,

                    showConfirmButton:
                        false

                });

            })
            .catch(function (error) {

                console.error(
                    "Book update error:",
                    error
                );


                isSavingBook =
                    false;


                setSaveButtonLoading(
                    false
                );


                /*
                 * Restore the edit modal so the
                 * librarian can correct the data.
                 */
                if (currentBook) {

                    populateEditForm(
                        currentBook
                    );

                    enterEditMode();

                    openBookDetailsModal();

                }


                showError(
                    error.message ||
                    "Unable to update the book. Please try again."
                );

            });

    }


    /* Save Button Loading State */

    function setSaveButtonLoading(
        loading
    ) {

        if (!saveBookButton) {
            return;
        }


        if (loading) {

            saveBookButton.disabled =
                true;

            saveBookButton.dataset.originalText =
                saveBookButton.textContent;

            saveBookButton.textContent =
                "Saving...";

        } else {

            saveBookButton.disabled =
                false;

            saveBookButton.textContent =
                saveBookButton.dataset.originalText ||
                "Save Changes";

        }

    }


    /* Update Table Row */

    function updateTableRow(
        book
    ) {

        const row =
            document.querySelector(
                '.book-row[data-book-id="' +
                book.id +
                '"]'
            );


        if (!row) {
            return;
        }


        row.setAttribute(
            "data-isbn",
            book.isbn
        );

        row.setAttribute(
            "data-title",
            book.title
        );

        row.setAttribute(
            "data-author",
            book.author
        );

        row.setAttribute(
            "data-category",
            book.category
        );

        row.setAttribute(
            "data-publisher",
            book.publisher
        );

        row.setAttribute(
            "data-publication-year",
            book.publicationYear
        );

        row.setAttribute(
            "data-language",
            book.language
        );

        row.setAttribute(
            "data-total-copies",
            book.totalCopies
        );

        row.setAttribute(
            "data-available-copies",
            book.availableCopies
        );

        row.setAttribute(
            "data-active",
            book.active
        );


        const cells =
            row.querySelectorAll(
                "td"
            );


        if (
            cells.length <
            10
        ) {

            return;

        }


        cells[0].textContent =
            book.isbn;

        cells[1].textContent =
            book.title;

        cells[2].textContent =
            book.author;

        cells[3].textContent =
            book.category;

        cells[4].textContent =
            book.publisher;

        cells[5].textContent =
            book.publicationYear;

        cells[6].textContent =
            book.language;


        const availabilityBadge =
            cells[7].querySelector(
                ".availability-badge"
            );


        if (availabilityBadge) {

            availabilityBadge.textContent =
                book.availableCopies +
                " / " +
                book.totalCopies;


            availabilityBadge.classList.toggle(
                "available",
                book.availableCopies > 0
            );


            availabilityBadge.classList.toggle(
                "unavailable",
                book.availableCopies <= 0
            );

        }


        const statusBadge =
            cells[8].querySelector(
                ".status-badge"
            );


        if (statusBadge) {

            statusBadge.textContent =
                book.active
                    ? "Active"
                    : "Inactive";


            statusBadge.classList.toggle(
                "active",
                book.active
            );


            statusBadge.classList.toggle(
                "inactive",
                !book.active
            );

        }


        const toggleButton =
            row.querySelector(
                ".toggle-status-button"
            );


        if (toggleButton) {

            toggleButton.setAttribute(
                "data-active",
                book.active
            );


            toggleButton.classList.toggle(
                "activate-button",
                !book.active
            );


            toggleButton.classList.toggle(
                "deactivate-button",
                book.active
            );


            const toggleText =
                toggleButton.querySelector(
                    "span"
                );


            if (toggleText) {

                toggleText.textContent =
                    book.active
                        ? "Deactivate"
                        : "Activate";

            }

        }

    }


    /* Confirm Status Toggle */

    function confirmStatusToggle(
        bookId,
        currentlyActive
    ) {

        const actionTitle =
            currentlyActive
                ? "Deactivate Book?"
                : "Activate Book?";


        const actionButtonText =
            currentlyActive
                ? "Yes, Deactivate"
                : "Yes, Activate";


        const successTitle =
            currentlyActive
                ? "Book Deactivated"
                : "Book Activated";


        Swal.fire({

            icon: "warning",

            title:
                actionTitle,

            text:
                currentlyActive
                    ? "This book will no longer be available for normal library operations."
                    : "This book will become active for normal library operations again.",

            showCancelButton: true,

            confirmButtonText:
                actionButtonText,

            cancelButtonText:
                "Cancel",

            confirmButtonColor:
                currentlyActive
                    ? "#d97706"
                    : "#198754",

            cancelButtonColor:
                "#6c757d"

        }).then(function (result) {

            if (
                result.isConfirmed
            ) {

                toggleBookStatus(
                    bookId,
                    successTitle
                );

            }

        });

    }


    /* Toggle Book Status */

    function toggleBookStatus(
        bookId,
        successTitle
    ) {

        fetch(
            "/librarian/books/" +
            encodeURIComponent(
                bookId
            ) +
            "/toggle-status",
            {
                method: "PATCH"
            }
        )
            .then(function (response) {

                if (!response.ok) {

                    return response
                        .text()
                        .then(function (message) {

                            throw new Error(
                                message ||
                                "Unable to change book status."
                            );

                        });

                }

                return response.json();

            })
            .then(function (book) {

                updateTableRow(
                    book
                );

                currentBook =
                    book;


                if (
                    bookModal &&
                    bookModal.classList.contains(
                        "active"
                    )
                ) {

                    populateViewMode(
                        book
                    );

                }


                Swal.fire({

                    icon: "success",

                    title:
                        successTitle,

                    text:
                        "The book status has been updated successfully.",

                    confirmButtonColor:
                        "#244f87",

                    timer:
                        1800,

                    timerProgressBar:
                        true,

                    showConfirmButton:
                        false

                });

            })
            .catch(function (error) {

                console.error(
                    "Status update error:",
                    error
                );

                showError(
                    error.message ||
                    "Unable to update the book status."
                );

            });

    }


    /* Confirm Delete */

    function confirmBookDelete(
        bookId
    ) {

        Swal.fire({

            icon: "warning",

            title:
                "Delete Book?",

            text:
                "This will permanently delete the book from the database. This action cannot be undone.",

            showCancelButton: true,

            confirmButtonText:
                "Yes, Delete",

            cancelButtonText:
                "Cancel",

            confirmButtonColor:
                "#dc3545",

            cancelButtonColor:
                "#6c757d"

        }).then(function (result) {

            if (
                result.isConfirmed
            ) {

                deleteBook(
                    bookId
                );

            }

        });

    }


    /* Delete Book */

    function deleteBook(
        bookId
    ) {

        fetch(
            "/librarian/books/" +
            encodeURIComponent(
                bookId
            ),
            {
                method: "DELETE"
            }
        )
            .then(function (response) {

                if (!response.ok) {

                    return response
                        .text()
                        .then(function (message) {

                            throw new Error(
                                message ||
                                "Unable to delete the book."
                            );

                        });

                }

                return response;

            })
            .then(function () {

                const row =
                    document.querySelector(
                        '.book-row[data-book-id="' +
                        bookId +
                        '"]'
                    );


                if (row) {

                    const rowIndex =
                        bookRows.indexOf(
                            row
                        );


                    if (
                        rowIndex !== -1
                    ) {

                        bookRows.splice(
                            rowIndex,
                            1
                        );

                    }


                    const filteredIndex =
                        filteredRows.indexOf(
                            row
                        );


                    if (
                        filteredIndex !== -1
                    ) {

                        filteredRows.splice(
                            filteredIndex,
                            1
                        );

                    }


                    row.remove();

                }


                updateBookList();


                if (
                    currentBook &&
                    String(currentBook.id) ===
                    String(bookId)
                ) {

                    closeBookDetailsModal();

                }


                Swal.fire({

                    icon: "success",

                    title:
                        "Book Deleted",

                    text:
                        "The book has been permanently deleted.",

                    confirmButtonColor:
                        "#244f87",

                    timer:
                        1800,

                    timerProgressBar:
                        true,

                    showConfirmButton:
                        false

                });

            })
            .catch(function (error) {

                console.error(
                    "Delete error:",
                    error
                );

                showError(
                    error.message ||
                    "Unable to delete the book. Please try again."
                );

            });

    }


    /* Warning */

    function showWarning(
        message
    ) {

        if (
            typeof Swal !==
            "undefined"
        ) {

            Swal.fire({

                icon: "warning",

                title:
                    "Warning",

                text:
                    message,

                confirmButtonColor:
                    "#244f87"

            });

        } else {

            window.alert(
                message
            );

        }

    }


    /* Error */

    function showError(
        message
    ) {

        if (
            typeof Swal !==
            "undefined"
        ) {

            Swal.fire({

                icon: "error",

                title:
                    "Update Failed",

                text:
                    message,

                confirmButtonColor:
                    "#244f87"

            });

        } else {

            window.alert(
                message
            );

        }

    }

});