package planet.info.skyline.model;

import java.util.ArrayList;



/**
 * Created by Abhishek on 16/02/17.
 */

public class ItemType {

    public String id;
    public String itemType;

    public ItemType(String itemType, String id) {
        this.id = id;
        this.itemType = itemType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return itemType;
    }

    public static ArrayList<ItemType> fillItemTypes() {
        ArrayList<ItemType> al_itemType = new ArrayList<>();
        al_itemType.add(new ItemType("--Choose your Item Type--", "0"));
        al_itemType.add(new ItemType("Customer Owned", "1"));
        al_itemType.add(new ItemType("IDC Rental", "2"));
        al_itemType.add(new ItemType("Local Rental", "3"));
        al_itemType.add(new ItemType("New Product", "4"));
        return al_itemType;

    }

    public static String getItemTypeByPosition(int position){
        ArrayList<ItemType> al_itemType =fillItemTypes();
        return al_itemType.get(position).toString();
    }

}
