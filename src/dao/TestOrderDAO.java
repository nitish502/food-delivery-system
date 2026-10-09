package dao;

import model.Order;

public class TestOrderDAO {

    public static void main(String[] args) {

        Order order = new Order();

        order.setUserId(1);
        order.setRestaurantId(1);
        order.setTotalAmount(249.00);
        order.setStatus("Pending");
        order.setPaymentMethod("Cash");

        OrderDAO orderDAO = new OrderDAO();

        boolean result = orderDAO.placeOrder(order);

        if (result) {
            System.out.println("Order Placed Successfully!");
        } else {
            System.out.println("Order Placement Failed!");
        }
    }
}