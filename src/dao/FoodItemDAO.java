package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.FoodItem;
import util.DatabaseConnection;

public class FoodItemDAO {

    public boolean addFoodItem(FoodItem foodItem) {

        String sql = "INSERT INTO food_items " +
                     "(restaurant_id, food_name, category, price, description) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, foodItem.getRestaurantId());
            statement.setString(2, foodItem.getFoodName());
            statement.setString(3, foodItem.getCategory());
            statement.setDouble(4, foodItem.getPrice());
            statement.setString(5, foodItem.getDescription());

            int result = statement.executeUpdate();

            if (result > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<FoodItem> getAllFoodItems() {

        List<FoodItem> foodItems = new ArrayList<>();

        String sql = "SELECT * FROM food_items";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                FoodItem foodItem = new FoodItem();

                foodItem.setFoodId(resultSet.getInt("food_id"));
                foodItem.setRestaurantId(resultSet.getInt("restaurant_id"));
                foodItem.setFoodName(resultSet.getString("food_name"));
                foodItem.setCategory(resultSet.getString("category"));
                foodItem.setPrice(resultSet.getDouble("price"));
                foodItem.setDescription(resultSet.getString("description"));

                foodItems.add(foodItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return foodItems;
    }
}