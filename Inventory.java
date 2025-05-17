import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Inventory {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        HashMap<Integer, String> ItemName = new HashMap<Integer, String>();
        HashMap<Integer, String> Description = new HashMap<Integer, String>();
        HashMap<Integer, Float> ItemPrice = new HashMap<Integer, Float>();
        HashMap<Integer, Integer> itemQuantity = new HashMap<Integer, Integer>();
        HashMap<Integer, Float> ItemValue = new HashMap<Integer, Float>();
        HashMap<Integer, String> Reorder = new HashMap<Integer, String>();
        HashMap<Integer, Integer> ReorderQuantity = new HashMap<Integer, Integer>();
        HashMap<Integer, String> ReorderDate = new HashMap<Integer, String>();
        HashMap<Integer, String> Discontinued = new HashMap<Integer, String>();
        ItemName.put(0, "test");
        
        System.out.println("Welcome User!");
        boolean startup = true;
        while (startup) {
            System.out.println("Inventory Management System"); 
            System.out.println("1. Add Item"); 
            System.out.println("2. Edit Item Data"); 
            System.out.println("3. Display Inventory");
            System.out.println("4. Save & Exit"); 
            System.out.print("Enter your Choice: ");
            try{
                int choice = input.nextInt();
                input.nextLine();

                switch (choice){
                    case 1: //Adds an inventory item & unique number code
                        System.out.println("Adding new item...");
                        System.out.println("To return to menu, type'quit'");
                        System.out.println("Please enter Item Name: ");
                        String itemname = input.nextLine();
                        if (itemname.equalsIgnoreCase("quit")) {
                            break;
                        }
                        else {
                        }

                        System.out.println("Please enter Item Unique Number: ");
                        int itemcode = input.nextInt();

                        if (ItemName.containsKey(itemcode)){
                            System.out.println("Sorry, this code was already used.");
                            break;
                        }
                        else {
                            ItemName.put(itemcode, itemname);
                            System.out.println("Item Successfully Added.");
                            break;
                        }

                    case 2: //Data Editor
                        System.out.println("Editing Data...");
                        System.out.println("Would you like to view all existing Items? Type 'Y'. Enter anything otherwise. ");
                        
                        String DataEditSearch = input.nextLine();
                        if (DataEditSearch.equalsIgnoreCase("Y")) {
                            System.out.println(ItemName);
                        }
                        else {
                        }
                        
                        System.out.println("Please enter Item Name: ");
                        String DataEdit = input.nextLine();
                        if (ItemName.containsValue(DataEdit)) {
                            int ItemCode = -1;
                            for (Map.Entry<Integer, String> entry : ItemName.entrySet()) {
                                if (entry.getValue().equalsIgnoreCase(DataEdit)) {
                                    ItemCode = entry.getKey();
                                    break;
                                }
                            }
                            if(ItemName.containsValue(DataEdit)){
                                Boolean Editing = true;
                                while (Editing) {
                                    System.out.println("Please select what you want to edit:");
                                    System.out.println("1. Item Name");
                                    System.out.println("2. Item Description");
                                    System.out.println("3. Item Price");
                                    System.out.println("4. Item Quantity");
                                    System.out.println("5. Reorder?");
                                    System.out.println("6. Reorder Quantity");
                                    System.out.println("7. Reorder Date");
                                    System.out.println("8. Discontinued?");
                                    System.out.println("9. Quit");
                                    int DataEditChoice = input.nextInt();
                                    input.nextLine();

                                    switch (DataEditChoice){
                                        case 1:
                                        System.out.println("Enter new item name: ");
                                        String NewName = input.nextLine();
                                        ItemName.replace(ItemCode, NewName);
                                        System.out.println("The name of the item with the code " + ItemCode + " is replaced by " + DataEdit);
                                        break;

                                        case 2:
                                        System.out.println("Enter new item description: ");
                                        String NewDesc = input.nextLine();
                                        Description.replace(ItemCode, NewDesc);
                                        System.out.println(DataEdit + "'s description is replaced by " + NewDesc);
                                        break;
                                        
                                        case 3:
                                        System.out.println("Enter new item price: ");
                                        Float NewPrice = input.nextFloat();
                                        ItemPrice.replace(ItemCode, NewPrice);
                                        System.out.println(DataEdit + "'s price is replaced by " + NewPrice);
                                        break;
                                        
                                        case 4:
                                        System.out.println("Enter new item quantity: ");
                                        int NewQuant = input.nextInt();
                                        itemQuantity.replace(ItemCode, NewQuant);
                                        System.out.println(DataEdit + "'s quantity is replaced by " + NewQuant);
                                        break;

                                        case 5:
                                        System.out.println("Will there be a reorder?");
                                        String NewReorder = input.nextLine();
                                        Reorder.replace(ItemCode, NewReorder);
                                        System.out.println(DataEdit + "'s price is replaced by " + NewReorder);
                                        break;

                                        case 6:
                                        System.out.println("Quantity of items reordered: ");
                                        int NewReorderQuant = input.nextInt();
                                        ReorderQuantity.replace(ItemCode, NewReorderQuant);
                                        System.out.println(DataEdit + "'s price is replaced by " + NewReorderQuant);
                                        break;

                                        case 7:
                                        System.out.println("Date of item reorder: ");
                                        String NewReorderDate = input.nextLine();
                                        ReorderDate.replace(ItemCode, NewReorderDate);
                                        System.out.println(DataEdit + "'s price is replaced by " + NewReorderDate);
                                        break;

                                        case 8:
                                        System.out.println("Is the item discontinued?");
                                        String NewDiscontinued = input.nextLine();
                                        Discontinued.replace(ItemCode, NewDiscontinued);
                                        System.out.println(DataEdit + "'s price is replaced by " + NewDiscontinued);
                                        break;

                                        case 9: 
                                        Editing = false;
                                        break;
                                    }
                                }
                            }
                            else{
                                System.out.println("Item does not exist in records.");
                            }
                        }
                        else {
                            System.out.println("Item does not exist in records.");
                            break;
                        }
                    case 4:
                        System.out.println("Thank you for using the program!");
                        startup = false;
                    
                    case 9: //Troubleshooting
                    System.out.println(ItemName);
                }
            }
            catch (InputMismatchException e) {
            System.out.println("Invalid option. Please try again.");
            input.nextLine();
            }
        }
        input.close();
    }
}