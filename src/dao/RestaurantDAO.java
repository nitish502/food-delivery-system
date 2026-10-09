package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Restaurant;
import util.DatabaseConnection;

public class RestaurantDAO {

    public boolean addRestaurant(Restaurant restaurant) {

        String sql = "INSERT INTO restaurants (restaurant_name, address, phone) VALUES (?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, restaurant.getRestaurantName());
            statement.setString(2, restaurant.getAddress());
            statement.setString(3, restaurant.getPhone());

            int result = statement.executeUpdate();

            if (result > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurants";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setRestaurantId(resultSet.getInt("restaurant_id"));
                restaurant.setRestaurantName(resultSet.getString("restaurant_name"));
                restaurant.setAddress(resultSet.getString("address"));
                restaurant.setPhone(resultSet.getString("phone"));

                restaurants.add(restaurant);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }

    public Restaurant getRestaurantById(int restaurantId) {

        String sql =
                "SELECT * FROM restaurants WHERE restaurant_id = ?";

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, restaurantId);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                Restaurant restaurant =
                        new Restaurant();

                restaurant.setRestaurantId(
                        resultSet.getInt("restaurant_id")
                );

                restaurant.setRestaurantName(
                        resultSet.getString("restaurant_name")
                );

                restaurant.setAddress(
                        resultSet.getString("address")
                );

                restaurant.setPhone(
                        resultSet.getString("phone")
                );

                return restaurant;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }
}