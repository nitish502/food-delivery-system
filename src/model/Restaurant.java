package model;

public class Restaurant {

    private int restaurantId;
    private String restaurantName;
    private String address;
    private String phone;

    public Restaurant() {
    }

    public Restaurant(int restaurantId, String restaurantName,
                      String address, String phone) {

        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.phone = phone;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}