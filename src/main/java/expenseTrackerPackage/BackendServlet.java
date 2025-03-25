package expenseTrackerPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.*;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class BackendServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String FILE_PATH = ""; // Enter the text file location where you need to save the expenses.
    private static ArrayList<Expense> expenseList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        loadExpensesFromFile(); 
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filterType = request.getParameter("filterType");
        LocalDate today = LocalDate.now();
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        double totalToday = 0, totalWeek = 0, totalMonth = 0;

        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);
        YearMonth currentMonth = YearMonth.from(today);

        for (Expense exp : expenseList) {
            LocalDate expenseDate = LocalDate.parse(exp.getExpenseDate());
            double amount = Double.parseDouble(exp.getExpenseAmount());

            if (expenseDate.isEqual(today)) totalToday += amount;
            if (!expenseDate.isBefore(weekStart) && !expenseDate.isAfter(weekEnd)) totalWeek += amount;
            if (YearMonth.from(expenseDate).equals(currentMonth)) totalMonth += amount;

            if (filterType == null || filterType.equals("all")) {
                filteredExpenses = expenseList;
            } else {
                switch (filterType) {
                    case "today":
                        if (expenseDate.isEqual(today)) filteredExpenses.add(exp);
                        break;
                    case "week":
                        if (!expenseDate.isBefore(weekStart) && !expenseDate.isAfter(weekEnd)) filteredExpenses.add(exp);
                        break;
                    case "month":
                        if (YearMonth.from(expenseDate).equals(currentMonth)) filteredExpenses.add(exp);
                        break;
                }
            }
        }

        request.setAttribute("expenses", filteredExpenses);
        request.setAttribute("selectedFilter", filterType);
        request.setAttribute("totalToday", totalToday);
        request.setAttribute("totalWeek", totalWeek);
        request.setAttribute("totalMonth", totalMonth);

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String expenseAmount = request.getParameter("expenseAmount");
        String expenseCategory = request.getParameter("expenseCategory");
        if (expenseCategory == null) expenseCategory = "";
        String expenseDate = request.getParameter("expenseDate");
        String expenseDescription = request.getParameter("expenseDescription");

        LocalDate enteredDate = LocalDate.parse(expenseDate);
        LocalDate today = LocalDate.now();

        if (enteredDate.isAfter(today)) {
            request.setAttribute("errorMessage", "Future dates are not allowed!");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        Expense newExpense = new Expense(expenseAmount, expenseCategory, expenseDate, expenseDescription);
        expenseList.add(newExpense);

        saveExpenseToFile(newExpense);
        response.sendRedirect("BackendServlet");
    }

    private void saveExpenseToFile(Expense expense) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(expense.getExpenseAmount() + "," + expense.getExpenseCategory() + "," +
                    expense.getExpenseDate() + "," + expense.getExpenseDescription());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadExpensesFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 4);
                if (data.length == 4) {
                    Expense expense = new Expense(data[0], data[1], data[2], data[3]);
                    expenseList.add(expense);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
