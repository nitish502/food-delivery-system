package dao;

import model.FoodItem;

public class TestDAO {

    public static void main(String[] args) {

        FoodItem foodItem = new FoodItem();

        foodItem.setRestaurantId(1);
        foodItem.setFoodName("Paneer Pizza");
        foodItem.setCategory("Pizza");
        foodItem.setPrice(249.00);
        foodItem.setDescription("Delicious paneer pizza");

        FoodItemDAO foodItemDAO = new FoodItemDAO();

        boolean result = foodItemDAO.addFoodItem(foodItem);

        if (result) {
            System.out.println("Food Item Added Successfully!");
        } else {
            System.out.println("Food Item Addition Failed!");
        }
    }
}