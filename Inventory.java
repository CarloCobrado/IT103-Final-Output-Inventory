import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Inventory {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HashMap<Integer, ItemData> Inventory = new HashMap<>();
        System.out.println("Welcome User!");

        boolean startup = true;
        while (startup) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Load File"); 
            System.out.println("2. Add Item"); 
            System.out.println("3. Edit Item Data"); 
            System.out.println("4. Display Inventory");
            System.out.println("5. Make Report");
            System.out.println("6. Save & Exit"); 
            System.out.print("Enter your Choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1: //File Loader
                        LoadInventory(input, Inventory);
                        break;
                    
                    case 2: //Adds an inventory item & unique number code
                        AddItem(input, Inventory);
                        break;

                    case 3: //Data Editor
                        EditItemData(input, Inventory);
                        break;

                    case 4: //View Inventory
                        ViewInventory(input, Inventory);
                        break;

                    case 5: //Report Generator
                        System.out.println("Making Report...");
                        System.out.println("Please Enter the Report Type: ");
                        System.out.println("1. Inventory Current Stock");
                        System.out.println("2. Inventory Reorder Information");
                        System.out.println("3. Quit");
                        System.out.print("Please enter choice: ");
                        try {
                            int ReportChoice = input.nextInt();
                            input.nextLine();
                            if (ReportChoice==1) {
                                InventoryReportStock(input, Inventory);
                                break;
                            }

                            else if (ReportChoice==2) {
                                InventoryReportReorder(input, Inventory);
                                break;
                            }

                            else if (ReportChoice==3) {
                                break;
                            }

                            else{
                                System.out.println("Invalid option. Please try again.");    
                            }
                        }
                        catch (InputMismatchException e) {
                            System.out.println("Invalid option. Please try again.");
                            input.nextLine();
                        }

                    case 6: //Save & Exit
                        SaveInventory(input, Inventory);
                        System.out.println("File Saved.");
                        System.out.println("Thank you for using the program!");
                        input.close();
                        startup = false;
                        break;

                    default: //If inputed number is not on the cases
                    System.out.println("Invalid option. Please try again.");
                    break;
                }
            }
            catch (InputMismatchException e) { //Catches if input is not a number
            System.out.println("Invalid option. Please try again.");
            input.nextLine();
            }
        }
    }

    public static Map<Integer, ItemData> LoadInventory(Scanner input, HashMap<Integer, ItemData> Inventory) {
        System.out.println("Enter File Name to Load: (example.csv) ");
        String FileName = input.nextLine();

        try (BufferedReader Loader = new BufferedReader(new FileReader(FileName))) {
            String line = Loader.readLine();

            while ((line = Loader.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                int ItemCode = Integer.parseInt(parts[0].trim());
                String name = CsvtoJava(parts[1].trim());
                String description = CsvtoJava(parts[2].trim());
                float price = Float.parseFloat(parts[3].trim());
                int quantity = Integer.parseInt(parts[4].trim());
                boolean reorder = Boolean.parseBoolean(parts[5].trim());
                int reorderQuantity = Integer.parseInt(parts[6].trim());
                String reorderDate = parts[7].trim();
                boolean discontinued = Boolean.parseBoolean(parts[8].trim());

                ItemData ItemDataLoader = new ItemData(name);
                ItemDataLoader.setDescription(description);
                ItemDataLoader.setPrice(price);
                ItemDataLoader.setQuantity(quantity);
                ItemDataLoader.setReorder(reorder);
                ItemDataLoader.setReorderQuantity(reorderQuantity);
                ItemDataLoader.setReorderDate(reorderDate);
                ItemDataLoader.setDiscontinued(discontinued);
                Inventory.put(ItemCode, ItemDataLoader);
            }
                System.out.println("Inventory loaded from " + FileName);
        
        }
        catch (IOException e) {
            System.out.println("File not found or cannot be read.");
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error parsing CSV: " + e.getMessage());
        }
        return Inventory;
    }

    private static String CsvtoJava(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1).replace("\"\"", "\"");
        }
        return input;
    }

    public static void AddItem(Scanner input, HashMap<Integer, ItemData> Inventory) {
        System.out.println("Adding new item...");
        System.out.println("To return to menu, type'quit'");
        System.out.print("Please enter Item Name: ");
        String ItemName = input.nextLine();
        if (ItemName.equalsIgnoreCase("quit")) {
            return;
        }

        System.out.print("Please enter Item Unique Number: ");
        try {
            int ItemCode = input.nextInt();
            input.nextLine();
            if (Inventory.containsKey(ItemCode)){
                System.out.println("Sorry, this code was already used.");
            }
            else {
                ItemData newItem = new ItemData(ItemName);
                Inventory.put(ItemCode, newItem);
                System.out.println("Item Successfully Added.");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid item code.");
            input.nextLine();
        }
    }

    public static void EditItemData(Scanner input, HashMap<Integer, ItemData> Inventory) {
        if (Inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        } //Checks if Inventory is empty

        System.out.println("Editing Data...");
        System.out.print("Would you like to view all existing Items? Type 'Y'. Enter anything otherwise. ");
        String DataEditSearch = input.nextLine();
        if (DataEditSearch.equalsIgnoreCase("Y")) {
            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue().getName());
            }
        }
        
        System.out.print("Please enter Item Code to edit: ");
        int ItemCode;
        try {
            ItemCode = input.nextInt();
            input.nextLine();
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid item code.");
            input.nextLine();
            return;
        }

        if (!Inventory.containsKey(ItemCode)) {
            System.out.println("Item does not exist in records.");
            return;
        }

        ItemData CurrentItemData = Inventory.get(ItemCode);
        Boolean Editing = true;
        while (Editing) {
            System.out.println("\nEditing Item- " + CurrentItemData);
            System.out.println("Please select what fields you want to edit:");
            System.out.println("1. Item Name");
            System.out.println("2. Item Description");
            System.out.println("3. Item Price");
            System.out.println("4. Item Quantity");
            System.out.println("5. Reorder?");
            System.out.println("6. Reorder Quantity");
            System.out.println("7. Reorder Date");
            System.out.println("8. Discontinued?");
            System.out.println("9. Quit");
            System.out.print("Enter your Choice: ");
            int DataEditChoice;

            try {
                DataEditChoice = input.nextInt();
                input.nextLine();

                if (DataEditChoice==1) { 
                    System.out.print("Enter new item name: ");
                    String NewName = input.nextLine();
                    CurrentItemData.setName(NewName);
                    System.out.println("The name of the item with the code " + ItemCode + " is replaced by " + CurrentItemData.getName());
                }
                
                else if (DataEditChoice==2) { 
                    System.out.print("Enter new item description: ");
                    String NewDesc = input.nextLine();
                    CurrentItemData.setDescription(NewDesc);
                    System.out.println(CurrentItemData.getName() + "'s description is replaced by " + NewDesc);
                }

                else if (DataEditChoice==3) {
                        System.out.print("Enter new item price: ");
                        try {
                            Float NewPrice = input.nextFloat();
                            input.nextLine();
                            CurrentItemData.setPrice(NewPrice);
                            System.out.println(CurrentItemData.getName() + "'s cost is now $" + NewPrice);
                        }
                        catch (InputMismatchException e) {
                            System.out.println("Invalid price. Please try again.");
                            input.nextLine();
                        }
                    }
                    
                else if (DataEditChoice==4) {
                    System.out.print("Enter new item quantity: ");
                    try {
                        int NewQuantity = input.nextInt();
                        input.nextLine();
                        CurrentItemData.setQuantity(NewQuantity);
                        System.out.println("There are " + NewQuantity + " " + CurrentItemData.getName() + " currently in stock.");
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid quantity. Please try again.");
                        input.nextLine();
                    }
                }

                else if (DataEditChoice==5) {
                    System.out.print("Is there a reorder? true or false? ");
                    String NewReorder = input.nextLine().toLowerCase();
                    if (NewReorder.equals("true") || NewReorder.equals("false")) {
                        CurrentItemData.setReorder(Boolean.parseBoolean(NewReorder));
                        System.out.println("Reorder of " + CurrentItemData.getName() + " is set to " + NewReorder);
                    }
                    else {
                        System.out.println("Invalid input. Please enter 'true' or 'false'.");
                    }
                }

                else if (DataEditChoice==6) {
                    System.out.print("Enter Quantity of items reordered: ");
                    try {
                        int NewReorderQuantity = input.nextInt();
                        input.nextLine();
                        CurrentItemData.setReorderQuantity(NewReorderQuantity);
                        System.out.println("The next shipment will have " + NewReorderQuantity + " of " + CurrentItemData.getName());
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid quantity. Please try again.");
                        input.nextLine();
                    }
                }

                else if (DataEditChoice==7) {
                    System.out.print("Enter Date of item reorder (YYYY-MM-DD) or leave blank to skip: ");
                    String NewReorderDate = input.nextLine();
                    if (!NewReorderDate.trim().isEmpty()) {
                        try{
                            DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate ParsedDate = LocalDate.parse(NewReorderDate, DateFormat);
                            CurrentItemData.setReorderDate(ParsedDate.toString());
                            System.out.println(CurrentItemData.getName() + "'s next shipment will be at " + NewReorderDate);
                        }
                        catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please enter as YYYY-MM-DD.");
                        }
                    }
                    else {
                        CurrentItemData.setReorderDate("N/A");
                    }
                }

                else if (DataEditChoice==8) {
                    System.out.print("Is the item discontinued? true or false? ");
                    String NewDiscontinued = input.nextLine().toLowerCase();
                    if (NewDiscontinued.equals("true") || NewDiscontinued.equals("false")) {
                        CurrentItemData.setDiscontinued(Boolean.parseBoolean(NewDiscontinued));
                        System.out.println("Discontinuation of " + CurrentItemData.getName() + " is set to " + NewDiscontinued);
                    }

                    else {
                        System.out.println("Invalid input. Please enter 'true' or 'false'.");
                    }
                }
                    
                else if (DataEditChoice==9) { 
                    System.out.println("Stopped editing.");
                    Editing = false;
                }

                else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid option. Please try again.");
                input.nextLine();
            }
        }
    }

    public static void ViewInventory(Scanner input, HashMap <Integer, ItemData> Inventory){

        if (Inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        Boolean ViewInventory = true;

        while (ViewInventory){
            System.out.println("Current Inventory:");
            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
            }
            System.out.println("Please select option:");
            System.out.println("1. Filter and search:");
            System.out.println("2. Quit:");
            System.out.print("Enter your Choice: ");

            int ViewInventoryChoice;
            try {
                ViewInventoryChoice = input.nextInt();
                input.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid option. Please try again.");
                input.nextLine();
                continue;
            }

            Boolean Filter = true;
            while (Filter) {
                if (ViewInventoryChoice==1) {
                    System.out.println("Which category would you like to filter through: ");
                    System.out.println("1. Code:");
                    System.out.println("2. Name:");
                    System.out.println("3. Description:");
                    System.out.println("4. Price:");
                    System.out.println("5. Quantity:");
                    System.out.println("6. Value:");
                    System.out.println("7. Reorder?:");
                    System.out.println("8. Reorder quantity:");
                    System.out.println("9. Reorder date:");
                    System.out.println("10. Discontinued?");
                    System.out.println("11. Exit filter?");
                    System.out.print("Enter your Choice: ");

                    int ViewInventoryFilter;

                    try {
                        ViewInventoryFilter = input.nextInt();
                        input.nextLine();
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid option. Please try again.");
                        input.nextLine();
                        continue;
                    }

                    if (ViewInventoryFilter==1) {
                        System.out.print("Enter item code to search: ");
                        String ItemSearchCode = input.nextLine().toLowerCase();
                        boolean found = false;

                        for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                            String ItemCode = String.valueOf(entry.getKey()).toLowerCase();
                            if (ItemCode.contains(ItemSearchCode)) {
                                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==2) {
                        System.out.print("Enter item name to search: ");
                        String ItemSearchName = input.nextLine().toLowerCase();
                        boolean found = false;

                        for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                            String ItemName = entry.getValue().getName().toLowerCase();
                            if (ItemName.contains(ItemSearchName)) {
                                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==3) {
                        System.out.print("Enter item description to search: ");
                        String ItemSearchDescription = input.nextLine().toLowerCase();
                        boolean found = false;

                        for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                            String ItemDescription = entry.getValue().getDescription().toLowerCase();
                            if (ItemDescription.contains(ItemSearchDescription)) {
                                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==4) {
                        System.out.println("Enter item price range to search.");
                        System.out.println("Type 'higher', 'lower', 'equal', or 'between: ");
                        String condition = input.nextLine().toLowerCase();
                        boolean found = false;

                        if (condition.equals("higher")) {
                            try{
                                System.out.print("Enter minimum price: ");
                                float MinPrice = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getPrice() > MinPrice) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("lower")) {
                            try{
                                System.out.print("Enter maximum price: ");
                                float MaxPrice = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getPrice() < MaxPrice) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("equal")) {
                            try {
                                System.out.print("Enter exact price: ");
                                float EqualPrice = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getPrice() == EqualPrice) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("between")) {
                            try {
                                System.out.print("Enter lower price range: ");
                                float PriceRangeLow = input.nextFloat();
                                System.out.print("Enter upper price range: ");
                                float PriceRangeUp = input.nextFloat();
                                input.nextLine();

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    float price = entry.getValue().getPrice();
                                    if (price >= PriceRangeLow && price <= PriceRangeUp) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else {
                            System.out.println("Invalid condition entered.");
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==5) {
                        System.out.println("Enter item quantity range to search.");
                        System.out.println("Type 'higher', 'lower', 'equal', or 'between: ");
                        String condition = input.nextLine().toLowerCase();
                        boolean found = false;

                        if (condition.equals("higher")) {
                            try {
                                System.out.print("Enter minimum quantity: ");
                                int MinQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getQuantity() > MinQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("lower")) {
                            try {
                                System.out.print("Enter maximum quantity: ");
                                int MaxQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getQuantity() < MaxQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("equal")) {
                            try{
                                System.out.print("Enter exact quantity: ");
                                int EqualQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getQuantity() == EqualQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("between")) {
                            try {
                                System.out.print("Enter lower quantity range: ");
                                int QuantRangeLow = input.nextInt();
                                System.out.print("Enter upper quantity range: ");
                                int QuantRangeUp = input.nextInt();
                                input.nextLine();

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    float price = entry.getValue().getQuantity();
                                    if (price >= QuantRangeLow && price <= QuantRangeUp) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else {
                            System.out.println("Invalid condition entered.");
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==6) {
                        System.out.println("Enter item value range to search.");
                        System.out.println("Type 'higher', 'lower', 'equal', or 'between: ");
                        String condition = input.nextLine().toLowerCase();
                        boolean found = false;

                        if (condition.equals("higher")) {
                            try{
                                System.out.print("Enter minimum value: ");
                                float MinValue = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getItemValue() > MinValue) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("lower")) {
                            try{
                                System.out.print("Enter maximum value: ");
                                float MaxValue = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getItemValue() < MaxValue) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("equal")) {
                            try {
                                System.out.print("Enter exact value: ");
                                float EqualValue = input.nextFloat();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    if (entry.getValue().getItemValue() == EqualValue) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("between")) {
                            try {
                                System.out.print("Enter lower value range: ");
                                float ValueRangeLow = input.nextFloat();
                                System.out.print("Enter upper value range: ");
                                float ValueRangeUp = input.nextFloat();
                                input.nextLine();

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    float price = entry.getValue().getItemValue();
                                    if (price >= ValueRangeLow && price <= ValueRangeUp) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else {
                            System.out.println("Invalid condition entered.");
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==7) {
                        System.out.print("Search for items with reorder = true or false? ");
                        String ItemReorder = input.nextLine().toLowerCase();

                        if (ItemReorder.equals("true") || ItemReorder.equals("false")) {
                            boolean searchReorder = Boolean.parseBoolean(ItemReorder);
                            boolean found = false;
                            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                if (entry.getValue().isReorder() == searchReorder) {
                                    System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                    found = true;
                                }
                            }

                            if (!found) {
                                System.out.println("No items found with reorder = " + searchReorder);
                            }
                        }

                        else{
                            System.out.println("Invalid input. Please enter 'true' or 'false'.");
                        }
                        
                    }

                    else if (ViewInventoryFilter==8) {
                        System.out.println("Enter item reorder quantity range to search.");
                        System.out.println("Type 'higher', 'lower', 'equal', or 'between: ");
                        String condition = input.nextLine().toLowerCase();
                        boolean found = false;

                        if (condition.equals("higher")) {
                            try{
                                System.out.print("Enter minimum quantity: ");
                                int MinReorderQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    ItemData ItemDataFilter = entry.getValue();
                                    if (ItemDataFilter.isReorder() && ItemDataFilter.getReorderQuantity() > MinReorderQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("lower")) {
                            try{
                                System.out.print("Enter maximum quantity: ");
                                int MaxReorderQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    ItemData ItemDataFilter = entry.getValue();
                                    if (ItemDataFilter.isReorder() && ItemDataFilter.getReorderQuantity() < MaxReorderQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("equal")) {
                            try {
                                System.out.print("Enter exact quantity: ");
                                int EqualReorderQuant = input.nextInt();
                                input.nextLine();
                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    ItemData ItemDataFilter = entry.getValue();
                                    if (ItemDataFilter.isReorder() && ItemDataFilter.getReorderQuantity() == EqualReorderQuant) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("between")) {
                            try {
                                System.out.print("Enter lower quantity range: ");
                                int ReorderRangeLow = input.nextInt();
                                System.out.print("Enter upper quantity range: ");
                                int ReorderRangeUp = input.nextInt();
                                input.nextLine();

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    ItemData ItemDataFilter = entry.getValue();
                                    if (ItemDataFilter.isReorder() && ItemDataFilter.getReorderQuantity()>= ReorderRangeLow &&
                                        ItemDataFilter.getReorderQuantity() <= ReorderRangeUp) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                    }
                                }
                            }
                            catch (InputMismatchException e) {
                                System.out.println("Invalid input. Please try again.");
                                input.nextLine();
                            }
                        }

                        else {
                            System.out.println("Invalid condition entered.");
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }
                    
                    else if (ViewInventoryFilter==9) {
                        DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        System.out.println("Enter reorder date range to search.");
                        System.out.println("Type 'after', 'before', 'during', or 'between: ");
                        String condition = input.nextLine().toLowerCase();
                        boolean found = false;

                        if (condition.equals("after")) {
                            try{
                                System.out.print("Enter date (YYYY-MM-DD): ");
                                String DateAfter = input.nextLine();
                                LocalDate ParsedDateAfter = LocalDate.parse(DateAfter, DateFormat);

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    String ReorderDateString = entry.getValue().getReorderDate();
                                    
                                    if (ReorderDateString != null && !ReorderDateString.equalsIgnoreCase("N/A")) {
                                        try {
                                            LocalDate ReorderDate = LocalDate.parse(ReorderDateString, DateFormat);
                                            if (ReorderDate.isAfter(ParsedDateAfter)) {
                                                System.out.println("Code: " + entry.getKey() + " -> " + entry.getValue());
                                                found = true;
                                            }
                                        }
                                        catch (DateTimeParseException e) {
                                            System.out.println("Invalid reorder date format for item with code " + entry.getKey());
                                        }
                                    }
                                }
                            }
                            catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("before")) {
                            try{
                                System.out.print("Enter date (YYYY-MM-DD): ");
                                String DateBefore = input.nextLine();
                                LocalDate ParsedDateBefore = LocalDate.parse(DateBefore, DateFormat);

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    String ReorderDateString = entry.getValue().getReorderDate();
                                    
                                    if (ReorderDateString != null && !ReorderDateString.equalsIgnoreCase("N/A")) {
                                        try {
                                            LocalDate ReorderDate = LocalDate.parse(ReorderDateString, DateFormat);
                                            if (ReorderDate.isBefore(ParsedDateBefore)) {
                                                System.out.println("Code: " + entry.getKey() + " -> " + entry.getValue());
                                                found = true;
                                            }
                                        }
                                        catch (DateTimeParseException e) {
                                            System.out.println("Invalid reorder date format for item with code " + entry.getKey());
                                        }
                                    }
                                }
                            }
                            catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("during")) {
                            try{
                                System.out.print("Enter date (YYYY-MM-DD): ");
                                String DateEqual = input.nextLine();
                                LocalDate ParsedDateEqual = LocalDate.parse(DateEqual, DateFormat);

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    String ReorderDateString = entry.getValue().getReorderDate();
                                    
                                    if (ReorderDateString != null && !ReorderDateString.equalsIgnoreCase("N/A")) {
                                        try {
                                            LocalDate ReorderDate = LocalDate.parse(ReorderDateString, DateFormat);
                                            if (ReorderDate.isEqual(ParsedDateEqual)) {
                                                System.out.println("Code: " + entry.getKey() + " -> " + entry.getValue());
                                                found = true;
                                            }
                                        }
                                        catch (DateTimeParseException e) {
                                            System.out.println("Invalid reorder date format for item with code " + entry.getKey());
                                        }
                                    }
                                }
                            }
                            catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please try again.");
                                input.nextLine();
                            }
                        }

                        else if (condition.equals("between")) {
                            try {
                                System.out.print("Enter lower date range (YYYY-MM-DD): ");
                                String DateRangeLower = input.nextLine();
                                LocalDate ParsedDateLower = LocalDate.parse(DateRangeLower, DateFormat);
                                System.out.print("Enter upper date range (YYYY-MM-DD): ");
                                String DateRangeUpper = input.nextLine();
                                LocalDate ParsedDateUpper = LocalDate.parse(DateRangeUpper, DateFormat);

                                for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                    String ReorderDateString = entry.getValue().getReorderDate();

                                    if (ReorderDateString != null && !ReorderDateString.equalsIgnoreCase("N/A")) {
                                        try {
                                            LocalDate ReorderDate = LocalDate.parse(ReorderDateString, DateFormat);
                                            if ((ReorderDate.isEqual(ParsedDateLower) || ReorderDate.isAfter(ParsedDateLower)) && 
                                                (ReorderDate.isEqual(ParsedDateUpper) || ReorderDate.isBefore(ParsedDateUpper)))
                                             {
                                                System.out.println("Code: " + entry.getKey() + " -> " + entry.getValue());
                                                found = true;
                                            }
                                        }
                                        catch (DateTimeParseException e) {
                                            System.out.println("Invalid reorder date format for item with code " + entry.getKey());
                                        }
                                    }
                                }
                            }
                            catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please try again.");
                                input.nextLine();
                            }
                        }
                        else {
                            System.out.println("Invalid condition entered.");
                        }

                        if (!found) {
                            System.out.println("No items matched your search.");
                        }
                    }

                    else if (ViewInventoryFilter==10) {
                        System.out.print("Search for items if discontinued = true or false? ");
                        String ItemDiscontinued = input.nextLine().toLowerCase();

                        if (ItemDiscontinued.equals("true") || ItemDiscontinued.equals("false")) {
                            boolean searchDiscontinued = Boolean.parseBoolean(ItemDiscontinued);
                            boolean found = false;

                            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                                if (entry.getValue().isDiscontinued() == searchDiscontinued) {
                                        System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
                                        found = true;
                                }
                            }

                            if (!found) {
                                System.out.println("No items found with discontinued = " + searchDiscontinued);
                            }
                        }

                        else {
                            System.out.println("Invalid input. Please enter 'true' or 'false'.");
                        }
                    }

                    else if (ViewInventoryFilter==11) {
                        System.out.println("Exiting filter...");
                        Filter = false;
                    }
                    
                    else {
                        System.out.println("Invalid option. Please try again.");
                        input.nextLine();
                    }
                }

                else if (ViewInventoryChoice==2) {
                        System.out.println("Stopped viewing inventory.");
                        ViewInventory = false;
                        break;
                }

                else {
                    System.out.println("Invalid option. Please try again.");
                    input.nextLine();
                }
            }
        }
    }

    public static void InventoryReportStock(Scanner input, HashMap<Integer, ItemData> inventory) {
        System.out.print("Enter Report File Name: ");
        String CurrentDate = LocalDate.now().toString();
        String FileName = input.nextLine() + "_StockInfo-" + CurrentDate + ".txt";

        try (FileWriter ReportWriter = new FileWriter(FileName)) {
            ReportWriter.write(String.format("%-6s %-20s %-30s %-10s %-10s %-10s %-12s%n",
                    "Code", "Name", "Description", "Price", "Quantity", "Value", "Discontinued?"));
            ReportWriter.write("-----------------------------------------------------------------------------------------------------------\n");

            for (Map.Entry<Integer, ItemData> entry : inventory.entrySet()) {
                int ItemCode = entry.getKey();
                ItemData ItemReportStock = entry.getValue();
                float Price = ItemReportStock.getPrice();
                int Quant = ItemReportStock.getQuantity();
                float Value = Price * Quant;

                ReportWriter.write(String.format("%-6d %-20s %-30s %-10.2f %-10d %-10.2f %-12s%n",
                    ItemCode,
                    ItemReportStock.getName(),
                    ItemReportStock.getDescription(),
                    Price,
                    Quant,
                    Value,
                    ItemReportStock.isDiscontinued() ? "Yes" : "No"));
            }
            System.out.println("Report successfully saved to: " + FileName);
        }
        catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }

    public static void InventoryReportReorder(Scanner input, HashMap<Integer, ItemData> inventory) {
            System.out.print("Enter Report File Name: ");
            String CurrentDate = LocalDate.now().toString();
            String FileName = input.nextLine() + "_ReorderInfo-" +  CurrentDate + ".txt";

            try (FileWriter ReportWriter = new FileWriter(FileName)) {
                ReportWriter.write(String.format("%-6s %-20s %-30s %-15s %-12s%n",
                    "Code", "Name", "Description", "Reorder Date", "Reorder Quantity"));
                ReportWriter.write("---------------------------------------------------------------------------------------------\n");
                for (Map.Entry<Integer, ItemData> entry : inventory.entrySet()) {
                    ItemData ItemReportReorder = entry.getValue();
                    if (ItemReportReorder.isReorder()) {
                        int ItemCode = entry.getKey();
                        String ReorderDate = ItemReportReorder.getReorderDate();
                        int ReorderQuant = ItemReportReorder.getReorderQuantity();

                        ReportWriter.write(String.format("%-6d %-20s %-30s %-15s %-12d%n",
                            ItemCode,
                            ItemReportReorder.getName(),
                            ItemReportReorder.getDescription(),
                            ReorderDate,
                            ReorderQuant
                        ));
                    }
                }
                System.out.println("Report successfully saved to: " + FileName);
            }
            catch (IOException e) {
                System.out.println("Error writing report: " + e.getMessage());
            }
    }

    public static void SaveInventory(Scanner input, HashMap<Integer, ItemData> Inventory) {
        System.out.print("Enter File Name to Save: (example.csv) ");
        String FileName = input.nextLine();

        try (PrintWriter FileSaver = new PrintWriter(new FileWriter(FileName))) {
            FileSaver.println("Code,Name,Description,Price,Quantity,Reorder,ReorderQuantity,ReorderDate,Discontinued");
            
            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                int ItemCode = entry.getKey();
                ItemData ItemDavaSaver = entry.getValue();

                    FileSaver.printf(
                        "%d,\"%s\",\"%s\",%.2f,%d,%b,%d,%s,%b,%n",
                        ItemCode,
                        JavatoCsv(ItemDavaSaver.getName()),
                        JavatoCsv(ItemDavaSaver.getDescription()),
                        ItemDavaSaver.getPrice(),
                        ItemDavaSaver.getQuantity(),
                        ItemDavaSaver.isReorder(),
                        ItemDavaSaver.getReorderQuantity(),
                        ItemDavaSaver.getReorderDate(),
                        ItemDavaSaver.isDiscontinued()
                    );
            }
        System.out.println("Inventory saved to " + FileName);
        }
        catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static String JavatoCsv(String input) {
        if (input.contains(",") || input.contains("\"")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
    }
}
