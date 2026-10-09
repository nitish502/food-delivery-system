package dao;

import model.OrderItem;

public class TestOrderItemDAO {

    public static void main(String[] args) {

        OrderItem orderItem = new OrderItem();

        orderItem.setOrderId(1);
        orderItem.setFoodId(1);
        orderItem.setQuantity(2);
        orderItem.setPrice(249.00);

        OrderItemDAO orderItemDAO = new OrderItemDAO();

        boolean result = orderItemDAO.addOrderItem(orderItem);

        if (result) {
            System.out.println("Order Item Added Successfully!");
        } else {
            System.out.println("Order Item Addition Failed!");
        }
    }
}