package com.example.library.dto;

import java.util.ArrayList;
import java.util.List;

public class LibraryStatisticsDTO {

    /* Main Statistics */

    private long totalBooks;

    private long totalCopies;

    private long availableCopies;

    private long currentlyIssued;

    private long overdueBooks;

    private long totalStudents;

    private double totalFineAmount;

    private long totalOverdueDays;


    /* Chart Data */

    private List<MonthlyCirculationDTO> monthlyCirculation =
            new ArrayList<>();

    private List<CategoryStatisticsDTO> categoryStatistics =
            new ArrayList<>();

    private List<RequestStatisticsDTO> requestStatistics =
            new ArrayList<>();

    private List<ActiveStudentStatisticsDTO> activeStudents =
            new ArrayList<>();

    private List<MonthlyFineStatisticsDTO> monthlyFineStatistics =
            new ArrayList<>();


    /* Getters and Setters */

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(long totalCopies) {
        this.totalCopies = totalCopies;
    }

    public long getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(long availableCopies) {
        this.availableCopies = availableCopies;
    }

    public long getCurrentlyIssued() {
        return currentlyIssued;
    }

    public void setCurrentlyIssued(long currentlyIssued) {
        this.currentlyIssued = currentlyIssued;
    }

    public long getOverdueBooks() {
        return overdueBooks;
    }

    public void setOverdueBooks(long overdueBooks) {
        this.overdueBooks = overdueBooks;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public double getTotalFineAmount() {
        return totalFineAmount;
    }

    public void setTotalFineAmount(double totalFineAmount) {
        this.totalFineAmount = totalFineAmount;
    }

    public long getTotalOverdueDays() {
        return totalOverdueDays;
    }

    public void setTotalOverdueDays(long totalOverdueDays) {
        this.totalOverdueDays = totalOverdueDays;
    }

    public List<MonthlyCirculationDTO> getMonthlyCirculation() {
        return monthlyCirculation;
    }

    public void setMonthlyCirculation(
            List<MonthlyCirculationDTO> monthlyCirculation) {

        this.monthlyCirculation = monthlyCirculation;
    }

    public List<CategoryStatisticsDTO> getCategoryStatistics() {
        return categoryStatistics;
    }

    public void setCategoryStatistics(
            List<CategoryStatisticsDTO> categoryStatistics) {

        this.categoryStatistics = categoryStatistics;
    }

    public List<RequestStatisticsDTO> getRequestStatistics() {
        return requestStatistics;
    }

    public void setRequestStatistics(
            List<RequestStatisticsDTO> requestStatistics) {

        this.requestStatistics = requestStatistics;
    }

    public List<ActiveStudentStatisticsDTO> getActiveStudents() {
        return activeStudents;
    }

    public void setActiveStudents(
            List<ActiveStudentStatisticsDTO> activeStudents) {

        this.activeStudents = activeStudents;
    }

    public List<MonthlyFineStatisticsDTO> getMonthlyFineStatistics() {
        return monthlyFineStatistics;
    }

    public void setMonthlyFineStatistics(
            List<MonthlyFineStatisticsDTO> monthlyFineStatistics) {

        this.monthlyFineStatistics = monthlyFineStatistics;
    }


    /* Monthly Circulation */

    public static class MonthlyCirculationDTO {

        private String month;

        private long issued;

        private long returned;


        public MonthlyCirculationDTO() {
        }

        public MonthlyCirculationDTO(
                String month,
                long issued,
                long returned) {

            this.month = month;
            this.issued = issued;
            this.returned = returned;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public long getIssued() {
            return issued;
        }

        public void setIssued(long issued) {
            this.issued = issued;
        }

        public long getReturned() {
            return returned;
        }

        public void setReturned(long returned) {
            this.returned = returned;
        }
    }


    /* Category Statistics */

    public static class CategoryStatisticsDTO {

        private String category;

        private long totalCopies;

        private long availableCopies;

        private long issuedCopies;


        public CategoryStatisticsDTO() {
        }

        public CategoryStatisticsDTO(
                String category,
                long totalCopies,
                long availableCopies,
                long issuedCopies) {

            this.category = category;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.issuedCopies = issuedCopies;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public long getTotalCopies() {
            return totalCopies;
        }

        public void setTotalCopies(long totalCopies) {
            this.totalCopies = totalCopies;
        }

        public long getAvailableCopies() {
            return availableCopies;
        }

        public void setAvailableCopies(long availableCopies) {
            this.availableCopies = availableCopies;
        }

        public long getIssuedCopies() {
            return issuedCopies;
        }

        public void setIssuedCopies(long issuedCopies) {
            this.issuedCopies = issuedCopies;
        }
    }


    /* Request Statistics */

    public static class RequestStatisticsDTO {

        private String status;

        private long count;


        public RequestStatisticsDTO() {
        }

        public RequestStatisticsDTO(
                String status,
                long count) {

            this.status = status;
            this.count = count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }


    /* Active Student Statistics */

    public static class ActiveStudentStatisticsDTO {

        private String studentName;

        private String rollNumber;

        private long issuedCount;

        private long returnedCount;

        private long currentlyIssued;


        public ActiveStudentStatisticsDTO() {
        }

        public ActiveStudentStatisticsDTO(
                String studentName,
                String rollNumber,
                long issuedCount,
                long returnedCount,
                long currentlyIssued) {

            this.studentName = studentName;
            this.rollNumber = rollNumber;
            this.issuedCount = issuedCount;
            this.returnedCount = returnedCount;
            this.currentlyIssued = currentlyIssued;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getRollNumber() {
            return rollNumber;
        }

        public void setRollNumber(String rollNumber) {
            this.rollNumber = rollNumber;
        }

        public long getIssuedCount() {
            return issuedCount;
        }

        public void setIssuedCount(long issuedCount) {
            this.issuedCount = issuedCount;
        }

        public long getReturnedCount() {
            return returnedCount;
        }

        public void setReturnedCount(long returnedCount) {
            this.returnedCount = returnedCount;
        }

        public long getCurrentlyIssued() {
            return currentlyIssued;
        }

        public void setCurrentlyIssued(long currentlyIssued) {
            this.currentlyIssued = currentlyIssued;
        }
    }


    /* Monthly Fine Statistics */

    public static class MonthlyFineStatisticsDTO {

        private String month;

        private double fineAmount;

        private long overdueRecords;

        private long overdueDays;


        public MonthlyFineStatisticsDTO() {
        }

        public MonthlyFineStatisticsDTO(
                String month,
                double fineAmount,
                long overdueRecords,
                long overdueDays) {

            this.month = month;
            this.fineAmount = fineAmount;
            this.overdueRecords = overdueRecords;
            this.overdueDays = overdueDays;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public double getFineAmount() {
            return fineAmount;
        }

        public void setFineAmount(double fineAmount) {
            this.fineAmount = fineAmount;
        }

        public long getOverdueRecords() {
            return overdueRecords;
        }

        public void setOverdueRecords(long overdueRecords) {
            this.overdueRecords = overdueRecords;
        }

        public long getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(long overdueDays) {
            this.overdueDays = overdueDays;
        }
    }
}