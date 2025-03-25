<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="expenseTrackerPackage.Expense" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Expense Tracker</title>
    <link rel="stylesheet" href="newStyle.css">
</head>

<body>
    <div class="container">
        <div class="header" style="font-size: 2rem; font-weight: bold;">Daily Expense Tracker</div>

        <% String errorMessage=(String) request.getAttribute("errorMessage"); if (errorMessage !=null) { %>
            <p style="color: red; font-weight: bold;">
                <%= errorMessage %>
            </p>
            <% } %>

                <div class="expense-info">
                    <form class="expense-form" action="BackendServlet" method="post">
                        <input type="number" id="amount" placeholder="Enter Amount" required name="expenseAmount">
                        <select id="category" name="expenseCategory">
                            <option value="" disabled selected>Select Category</option>
                            <option value="Food">Food</option>
                            <option value="Travel">Travel</option>
                            <option value="Shopping">Shopping</option>
                            <option value="Bills">Bills</option>
                            <option value="Others">Others</option>
                        </select>
                        <input type="date" id="date" placeholder="Enter Date" required name="expenseDate">
                        <input type="text" id="description" placeholder="Enter Description" name="expenseDescription">
                        <button>Add Expense</button>
                    </form>

                    <div class="summary-section">
                        <h2>Expense Summary</h2>
                        <table class="summary-table">
                            <tr>
                                <th>Category</th>
                                <th>Amount (₹)</th>
                            </tr>
                            <tr>
                                <td>Today's Expenses:</td>
                                <td>
                                    <%= request.getAttribute("totalToday") %>
                                </td>
                            </tr>
                            <tr>
                                <td>This Week's Expenses:</td>
                                <td>
                                    <%= request.getAttribute("totalWeek") %>
                                </td>
                            </tr>
                            <tr>
                                <td>This Month's Expenses:</td>
                                <td>
                                    <%= request.getAttribute("totalMonth") %>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="expense-display">
                    <div class="filter-section">
                        <label for="filter" class="labelHead">Filter by:</label>
                        <select id="filter" name="filterType" onchange="applyFilter()">
                            <option value="all" ${selectedFilter=='all' ? 'selected' : '' }>All</option>
                            <option value="today" ${selectedFilter=='today' ? 'selected' : '' }>Today
                            </option>
                            <option value="week" ${selectedFilter=='week' ? 'selected' : '' }>This Week
                            </option>
                            <option value="month" ${selectedFilter=='month' ? 'selected' : '' }>This Month
                            </option>
                        </select>
                    </div>
                    <table id="expense-table">
                        <thead>
                            <tr>
                                <th>Amount</th>
                                <th>Category</th>
                                <th>Description</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody id="expense-list">
                            <% @SuppressWarnings("unchecked") ArrayList<Expense> expenses = (ArrayList
                                <Expense>) request.getAttribute("expenses");
                                    if (expenses != null && !expenses.isEmpty()) {
                                    for (Expense exp : expenses) {
                                    %>
                                    <tr class="expense-row">
                                        <td class="expense-amount">
                                            <%= exp.getExpenseAmount() %>
                                        </td>
                                        <td class="expense-category">
                                            <%= exp.getExpenseCategory() %>
                                        </td>
                                        <td class="expense-description">
                                            <%= exp.getExpenseDescription() %>
                                        </td>
                                        <td class="expense-date">
                                            <%= exp.getExpenseDate() %>
                                        </td>
                                    </tr>
                                    <% } } else { %>
                                        <tr class="no-expenses">
                                            <td colspan="4">No expenses added yet.</td>
                                        </tr>
                                        <% } %>
                        </tbody>
                    </table>
                </div>
    </div>

    <script>
        function applyFilter() {
            let filterValue = document.getElementById("filter").value;
            window.location.href = "BackendServlet?filterType=" + filterValue;
        }
    </script>
</body>

</html>