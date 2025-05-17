//Collection of Valid Item Data
public class ItemData {
    private String name;
    private String description;
    private float price;
    private int quantity;
    private boolean reorder;
    private int reorderQuantity;
    private String reorderDate;
    private boolean discontinued;

    public ItemData(String name) {
        this.name = name;
        this.description = "";
        this.price = 0f;
        this.quantity = 0;
        this.reorder = false;
        this.reorderQuantity = 0;
        this.reorderDate = "";
        this.discontinued = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isReorder() { return reorder; }
    public void setReorder(boolean reorder) { this.reorder = reorder; }

    public int getReorderQuantity() { return reorderQuantity; }
    public void setReorderQuantity(int reorderQuantity) { this.reorderQuantity = reorderQuantity; }

    public String getReorderDate() { return reorderDate; }
    public void setReorderDate(String reorderDate) { this.reorderDate = reorderDate; }

    public boolean isDiscontinued() { return discontinued; }
    public void setDiscontinued(boolean discontinued) { this.discontinued = discontinued; }

    @Override //Sets format for viewing inventory
    public String toString() {
        return "Name: " + name + 
               ", Description: " + description +
               ", Price: $" + price + 
               ", Quantity: " + quantity +
               ", Reorder: " + reorder +
               ", Reorder Quantity: " + reorderQuantity +
               ", Reorder Date: " + reorderDate +
               ", Discontinued: " + discontinued;
    }
}
