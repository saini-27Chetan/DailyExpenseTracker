package expenseTrackerPackage;

public class Expense {
	private String expenseAmount;
    private String expenseCategory;
    private String expenseDate;
    private String expenseDescription;

    public Expense(String amount, String category, String date, String description) {
        this.expenseAmount = amount;
        this.expenseCategory = category;
        this.expenseDate = date;
        this.expenseDescription = description;
    }

    // Getter methods
    public String getExpenseAmount() {
        return expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }
}
