package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.OrderItem;
import util.DatabaseConnection;

public class OrderItemDAO {

    public boolean addOrderItem(OrderItem orderItem) {

        String sql = "INSERT INTO order_items " +
                     "(order_id, food_id, quantity, price) " +
                     "VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getFoodId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getPrice());

            int result = statement.executeUpdate();

            if (result > 0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {

    List<OrderItem> orderItems = new ArrayList<>();

    String sql =
            "SELECT * FROM order_items WHERE order_id = ?";

    try {

        Connection connection =
                DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setInt(1, orderId);

        ResultSet resultSet =
                statement.executeQuery();

        while (resultSet.next()) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrderItemId(
                    resultSet.getInt("order_item_id")
            );

            orderItem.setOrderId(
                    resultSet.getInt("order_id")
            );

            orderItem.setFoodId(
                    resultSet.getInt("food_id")
            );

            orderItem.setQuantity(
                    resultSet.getInt("quantity")
            );

            orderItem.setPrice(
                    resultSet.getDouble("price")
            );

            orderItems.add(orderItem);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return orderItems;
}

public List<String> getOrderItemDetailsByOrderId(int orderId) {

    List<String> itemDetails = new ArrayList<>();

    String sql =
            "SELECT f.food_name, oi.quantity, oi.price " +
            "FROM order_items oi " +
            "JOIN food_items f " +
            "ON oi.food_id = f.food_id " +
            "WHERE oi.order_id = ?";

    try {

        Connection connection =
                DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setInt(1, orderId);

        ResultSet resultSet =
                statement.executeQuery();

        while (resultSet.next()) {

            String foodName =
                    resultSet.getString("food_name");

            int quantity =
                    resultSet.getInt("quantity");

            double price =
                    resultSet.getDouble("price");

            String details =
                    foodName
                    + " | Quantity: "
                    + quantity
                    + " | Price: ₹"
                    + price;

            itemDetails.add(details);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return itemDetails;
}
}