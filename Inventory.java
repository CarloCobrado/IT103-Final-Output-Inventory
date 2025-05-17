import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Inventory {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HashMap<Integer, ItemData> Inventory = new HashMap<>();
        ItemData test = new ItemData("test"); //Test Data
        Inventory.put(0, test);
        System.out.println("Welcome User!");
        boolean startup = true;
        while (startup) {
            System.out.println("\nInventory Management System"); 
            System.out.println("1. Add Item"); 
            System.out.println("2. Edit Item Data"); 
            System.out.println("3. Display Inventory");
            System.out.println("4. Save & Exit"); 
            System.out.print("Enter your Choice: ");

            try {
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1: //Adds an inventory item & unique number code
                        AddItem(input, Inventory);
                        break;

                    case 2: //Data Editor
                        EditItemData(input, Inventory);
                        break;

                    case 3: //View Inventory

                    case 4: //Save & Exit
                        System.out.println("Thank you for using the program!");
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
        input.close();
    }
    public static void AddItem(Scanner input, HashMap<Integer, ItemData> Inventory) {
        System.out.println("Adding new item...");
        System.out.println("To return to menu, type'quit'");
        System.out.println("Please enter Item Name: ");
        String ItemName = input.nextLine();
        if (ItemName.equalsIgnoreCase("quit")) {
            return;
        }

        System.out.println("Please enter Item Unique Number: ");
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
        System.out.println("Would you like to view all existing Items? Type 'Y'. Enter anything otherwise. ");
        String DataEditSearch = input.nextLine();
        if (DataEditSearch.equalsIgnoreCase("Y")) {
            for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
                System.out.println("Code: " + entry.getKey() + " - " + entry.getValue().getName());
            }
        }
        
        System.out.println("Please enter Item Name to edit: ");
        int ItemCode = -1;
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

        ItemData ItemName = Inventory.get(ItemCode);
        Boolean Editing = true;
        while (Editing) {
            System.out.println("\nEditing Item: " + ItemName);
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
            int DataEditChoice = 0;
            try {
                DataEditChoice = input.nextInt();
                input.nextLine();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid option. Please try again.");
                input.nextLine();
                continue;
            }

            switch (DataEditChoice) { 
                case 1:
                    System.out.print("Enter new item name: ");
                    String NewName = input.nextLine();
                    ItemName.setName(NewName);
                    System.out.println("The name of the item with the code " + ItemCode + " is replaced by " + NewName);
                    break;

                case 2:
                    System.out.println("Enter new item description: ");
                    String NewDesc = input.nextLine();
                    ItemName.setDescription(NewDesc);
                    System.out.println(ItemName + "'s description is replaced by " + NewDesc);
                    break;
                
                case 3:
                    System.out.println("Enter new item price: ");
                    try {
                        Float NewPrice = input.nextFloat();
                        input.nextLine();
                        ItemName.setPrice(NewPrice);
                        System.out.println(ItemName + "'s cost is now " + NewPrice);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid price. Please try again.");
                        input.nextLine();
                    }
                    break;
                
                case 4:
                    System.out.println("Enter new item quantity: ");
                    try {
                        int NewQuantity = input.nextInt();
                        input.nextLine();
                        ItemName.setQuantity(NewQuantity);
                        System.out.println("There are " + NewQuantity + " " + ItemName + " in currently in stock.");
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid quantity. Please try again.");
                        input.nextLine();
                    }
                    break;

                case 5:
                    System.out.println("Is there a reorder? true or false?");
                    String NewReorder = input.nextLine();
                    ItemName.setReorder(Boolean.parseBoolean(NewReorder));
                    System.out.println("Reorder of " + ItemName + " is " + NewReorder);
                    break;

                case 6:
                    System.out.println("Quantity of items reordered: ");
                    try {
                        int NewReorderQuantity = input.nextInt();
                        input.nextLine();
                        ItemName.setReorderQuantity(NewReorderQuantity);
                        System.out.println("The next shipment will have " + NewReorderQuantity + " of " + ItemName);
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Invalid quantity. Please try again.");
                        input.nextLine();
                    }
                    break;

                case 7:
                    System.out.println("Date of item reorder: ");
                    String NewReorderDate = input.nextLine();
                    ItemName.setReorderDate(NewReorderDate);
                    System.out.println(ItemName + "'s next shipment will be at " + NewReorderDate);
                    break;

                case 8:
                    System.out.println("Is the item discontinued? true or false?");
                    String NewDiscontinued = input.nextLine();
                    ItemName.setDiscontinued(Boolean.parseBoolean(NewDiscontinued));
                    System.out.println(ItemName + "'s discontiunation status is " + NewDiscontinued);
                    break;

                case 9: 
                    System.out.println("Stopped editing.");
                    Editing = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    public static void ViewInventory(HashMap <Integer, ItemData> Inventory){
        if (Inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        System.out.println("Current Inventory:");
        for (Map.Entry<Integer, ItemData> entry : Inventory.entrySet()) {
            System.out.println("Code: " + entry.getKey() + " - " + entry.getValue());
        }
    }
}
