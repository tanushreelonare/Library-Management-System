/* Library Statistics Charts */

document.addEventListener("DOMContentLoaded", function () {

    const statistics = window.libraryStatistics;

    if (!statistics) {
        return;
    }

    createMonthlyCirculationChart(
        statistics.monthlyCirculation || []
    );

    createRequestStatisticsChart(
        statistics.requestStatistics || []
    );

    createCategoryStatisticsChart(
        statistics.categoryStatistics || []
    );

    createMonthlyFineChart(
        statistics.monthlyFineStatistics || []
    );
});


/* Monthly Circulation */

function createMonthlyCirculationChart(data) {

    const canvas =
        document.getElementById("monthlyCirculationChart");

    const emptyState =
        document.getElementById("monthlyCirculationEmpty");

    if (!canvas) {
        return;
    }

    if (!data.length) {

        canvas.style.display = "none";

        if (emptyState) {
            emptyState.style.display = "flex";
        }

        return;
    }

    if (emptyState) {
        emptyState.style.display = "none";
    }

    const labels = data.map(item =>
        formatMonth(item.month)
    );

    const issued = data.map(item =>
        Number(item.issued || 0)
    );

    const returned = data.map(item =>
        Number(item.returned || 0)
    );

    new Chart(canvas, {

        type: "bar",

        data: {

            labels: labels,

            datasets: [

                {
                    label: "Issued",
                    data: issued,
                    backgroundColor: "#244f87",
                    borderRadius: 5,
                    maxBarThickness: 38
                },

                {
                    label: "Returned",
                    data: returned,
                    backgroundColor: "#5b8def",
                    borderRadius: 5,
                    maxBarThickness: 38
                }

            ]

        },

        options: {

            responsive: true,

            maintainAspectRatio: false,

            interaction: {
                mode: "index",
                intersect: false
            },

            plugins: {

                legend: {
                    position: "top",
                    align: "end"
                },

                tooltip: {
                    callbacks: {

                        label: function (context) {

                            return (
                                " " +
                                context.dataset.label +
                                ": " +
                                context.parsed.y
                            );

                        }

                    }

                }

            },

            scales: {

                x: {
                    grid: {
                        display: false
                    },

                    ticks: {
                        color: "#64748b"
                    }
                },

                y: {

                    beginAtZero: true,

                    ticks: {
                        precision: 0,
                        color: "#64748b"
                    },

                    grid: {
                        color: "#e9edf3"
                    }

                }

            }

        }

    });
}


/* Request Statistics */

function createRequestStatisticsChart(data) {

    const canvas =
        document.getElementById("requestStatisticsChart");

    const emptyState =
        document.getElementById("requestStatisticsEmpty");

    if (!canvas) {
        return;
    }

    if (!data.length) {

        canvas.style.display = "none";

        if (emptyState) {
            emptyState.style.display = "flex";
        }

        return;
    }

    if (emptyState) {
        emptyState.style.display = "none";
    }

    const labels = data.map(item =>
        formatRequestStatus(item.status)
    );

    const values = data.map(item =>
        Number(item.count || 0)
    );

    new Chart(canvas, {

        type: "doughnut",

        data: {

            labels: labels,

            datasets: [

                {
                    data: values,

                    backgroundColor: [
                        "#244f87",
                        "#5b8def",
                        "#f59e0b",
                        "#dc3545",
                        "#22a06b",
                        "#7c5cff",
                        "#64748b"
                    ],

                    borderColor: "#ffffff",

                    borderWidth: 3
                }

            ]

        },

        options: {

            responsive: true,

            maintainAspectRatio: false,

            cutout: "62%",

            plugins: {

                legend: {

                    position: "bottom",

                    labels: {
                        padding: 18,
                        usePointStyle: true,
                        pointStyle: "circle"
                    }

                },

                tooltip: {

                    callbacks: {

                        label: function (context) {

                            const total =
                                context.dataset.data.reduce(
                                    (sum, value) =>
                                        sum + Number(value),
                                    0
                                );

                            const value =
                                Number(context.raw || 0);

                            const percentage =
                                total > 0
                                    ? ((value / total) * 100)
                                        .toFixed(1)
                                    : 0;

                            return (
                                " " +
                                context.label +
                                ": " +
                                value +
                                " (" +
                                percentage +
                                "%)"
                            );

                        }

                    }

                }

            }

        }

    });
}


/* Category Statistics */

function createCategoryStatisticsChart(data) {

    const canvas =
        document.getElementById("categoryStatisticsChart");

    const emptyState =
        document.getElementById("categoryStatisticsEmpty");

    if (!canvas) {
        return;
    }

    if (!data.length) {

        canvas.style.display = "none";

        if (emptyState) {
            emptyState.style.display = "flex";
        }

        return;
    }

    if (emptyState) {
        emptyState.style.display = "none";
    }

    /*
     * Display the eight categories with the
     * highest number of total copies.
     */

    const topCategories = [...data]
        .sort(
            (a, b) =>
                Number(b.totalCopies || 0) -
                Number(a.totalCopies || 0)
        )
        .slice(0, 8);

    const labels = topCategories.map(item =>
        item.category
    );

    const totalCopies = topCategories.map(item =>
        Number(item.totalCopies || 0)
    );

    const availableCopies = topCategories.map(item =>
        Number(item.availableCopies || 0)
    );

    const issuedCopies = topCategories.map(item =>
        Number(item.issuedCopies || 0)
    );

    new Chart(canvas, {

        type: "bar",

        data: {

            labels: labels,

            datasets: [

                {
                    label: "Total Copies",
                    data: totalCopies,
                    backgroundColor: "#244f87",
                    borderRadius: 4,
                    barThickness: 18
                },

                {
                    label: "Available Copies",
                    data: availableCopies,
                    backgroundColor: "#5b8def",
                    borderRadius: 4,
                    barThickness: 18
                },

                {
                    label: "Issued Copies",
                    data: issuedCopies,
                    backgroundColor: "#f59e0b",
                    borderRadius: 4,
                    barThickness: 18
                }

            ]

        },

        options: {

            indexAxis: "y",

            responsive: true,

            maintainAspectRatio: false,

            interaction: {
                mode: "index",
                intersect: false
            },

            plugins: {

                legend: {

                    position: "top",

                    align: "end"
                },

                tooltip: {

                    callbacks: {

                        label: function (context) {

                            return (
                                " " +
                                context.dataset.label +
                                ": " +
                                context.parsed.x
                            );

                        }

                    }

                }

            },

            scales: {

                x: {

                    beginAtZero: true,

                    ticks: {
                        precision: 0,
                        color: "#64748b"
                    },

                    grid: {
                        color: "#e9edf3"
                    }

                },

                y: {

                    grid: {
                        display: false
                    },

                    ticks: {
                        color: "#475569"
                    }

                }

            }

        }

    });
}


/* Monthly Fine Statistics */

function createMonthlyFineChart(data) {

    const canvas =
        document.getElementById("monthlyFineChart");

    const emptyState =
        document.getElementById("monthlyFineEmpty");

    if (!canvas) {
        return;
    }

    if (!data.length) {

        canvas.style.display = "none";

        if (emptyState) {
            emptyState.style.display = "flex";
        }

        return;
    }

    if (emptyState) {
        emptyState.style.display = "none";
    }

    const labels = data.map(item =>
        formatMonth(item.month)
    );

    const fineAmounts = data.map(item =>
        Number(item.fineAmount || 0)
    );

    new Chart(canvas, {

        type: "line",

        data: {

            labels: labels,

            datasets: [

                {
                    label: "Fine Amount",

                    data: fineAmounts,

                    borderColor: "#244f87",

                    backgroundColor:
                        "rgba(36, 79, 135, 0.10)",

                    borderWidth: 2,

                    fill: true,

                    tension: 0.3,

                    pointRadius: 4,

                    pointHoverRadius: 6,

                    pointBackgroundColor: "#244f87"
                }

            ]

        },

        options: {

            responsive: true,

            maintainAspectRatio: false,

            plugins: {

                legend: {
                    position: "top",
                    align: "end"
                },

                tooltip: {

                    callbacks: {

                        label: function (context) {

                            return (
                                " Fine: ₹" +
                                Number(
                                    context.parsed.y
                                ).toFixed(2)
                            );

                        }

                    }

                }

            },

            scales: {

                x: {

                    grid: {
                        display: false
                    },

                    ticks: {
                        color: "#64748b"
                    }

                },

                y: {

                    beginAtZero: true,

                    ticks: {

                        color: "#64748b",

                        callback: function (value) {
                            return "₹" + value;
                        }

                    },

                    grid: {
                        color: "#e9edf3"
                    }

                }

            }

        }

    });
}


/* Month Formatting */

function formatMonth(value) {

    if (!value) {
        return "";
    }

    const parts = value.split("-");

    if (parts.length !== 2) {
        return value;
    }

    const year = Number(parts[0]);
    const month = Number(parts[1]);

    if (!year || !month) {
        return value;
    }

    const date =
        new Date(year, month - 1, 1);

    return date.toLocaleDateString(
        "en-US",
        {
            month: "short",
            year: "numeric"
        }
    );
}


/* Request Status Formatting */

function formatRequestStatus(status) {

    if (!status) {
        return "";
    }

    return status
        .toLowerCase()
        .split("_")
        .map(word =>
            word.charAt(0).toUpperCase() +
            word.slice(1)
        )
        .join(" ");
}