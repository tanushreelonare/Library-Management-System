document.addEventListener("DOMContentLoaded", function () {

    const searchForm = document.querySelector(".book-search-form");

    const searchInput = searchForm
        ? searchForm.querySelector('input[name="search"]')
        : null;

    if (searchForm && searchInput) {

        searchForm.addEventListener("submit", function (event) {

            const searchTerm = searchInput.value.trim();

            if (searchTerm.length === 0) {

                event.preventDefault();

                window.location.href = searchForm.action;

                return;

            }

            searchInput.value = searchTerm;

        });

    }

});