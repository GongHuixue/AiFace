package android.com.aiface.ui.adapter;

public class FragListItem {
    private int imageId;
    private String itemName;

    public FragListItem(String name, int image) {
        this.itemName = name;
        this.imageId = image;
    }

    public int getImageId() {
        return imageId;
    }

    public String getItemName() {
        return itemName;
    }
}
