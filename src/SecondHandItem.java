public class SecondHandItem {
    int itemID;
    String itemName;
    String itemDescription;
    double itemPrice;
    String itemCategory;
    int itemSeller;

    // Constructor with itemDescription and itemCategory
    public SecondHandItem(String itemName, String itemDescription, double itemPrice, String itemCategory, int itemSeller) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemSeller = itemSeller;
    }

    // Constructor with only itemDescription
    public SecondHandItem(String itemName, String itemDescription, double itemPrice, int itemSeller) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemSeller = itemSeller;
    }

    // Constructor with only itemCategory
    public SecondHandItem(String itemName, double itemPrice, String itemCategory, int itemSeller) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemSeller = itemSeller;
    }

    // Constructor with only required fields
    public SecondHandItem(String itemName, double itemPrice, int itemSeller) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemSeller = itemSeller;
    }
}
