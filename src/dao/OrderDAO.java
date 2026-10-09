package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Order;
import util.DatabaseConnection;

public class OrderDAO {

    public int placeOrderAndGetId(Order order) {

        String sql = "INSERT INTO orders " +
                     "(user_id, restaurant_id, total_amount, status, payment_method) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {

            Connection connection =
                    DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(
                            sql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            statement.setInt(
                    1,
                    order.getUserId()
            );

            statement.setInt(
                    2,
                    order.getRestaurantId()
            );

            statement.setDouble(
                    3,
                    order.getTotalAmount()
            );

            statement.setString(
                    4,
                    order.getStatus()
            );

            statement.setString(
                    5,
                    order.getPaymentMethod()
            );

            int result =
                    statement.executeUpdate();

            if (result > 0) {

                ResultSet generatedKeys =
                        statement.getGeneratedKeys();

                if (generatedKeys.next()) {

                    return generatedKeys.getInt(1);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }

    public boolean placeOrder(Order order) {

        int orderId = placeOrderAndGetId(order);

        return orderId != -1;
    }

    public List<Order> getAllOrders() {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Order order = new Order();

                order.setOrderId(
                        resultSet.getInt("order_id")
                );

                order.setUserId(
                        resultSet.getInt("user_id")
                );

                order.setRestaurantId(
                        resultSet.getInt("restaurant_id")
                );

                order.setTotalAmount(
                        resultSet.getDouble("total_amount")
                );

                order.setOrderDate(
                        resultSet.getString("order_date")
                );

                order.setStatus(
                        resultSet.getString("status")
                );

                order.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<Order> getOrdersByUserId(int userId) {

        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders WHERE user_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Order order = new Order();

                order.setOrderId(
                        resultSet.getInt("order_id")
                );

                order.setUserId(
                        resultSet.getInt("user_id")
                );

                order.setRestaurantId(
                        resultSet.getInt("restaurant_id")
                );

                order.setTotalAmount(
                        resultSet.getDouble("total_amount")
                );

                order.setOrderDate(
                        resultSet.getString("order_date")
                );

                order.setStatus(
                        resultSet.getString("status")
                );

                order.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }
}