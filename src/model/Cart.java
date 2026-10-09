package model;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addItem(FoodItem foodItem, int quantity) {

        for (CartItem item : cartItems) {

            if (item.getFoodItem().getFoodId()
                    == foodItem.getFoodId()) {

                item.setQuantity(
                        item.getQuantity() + quantity
                );

                return;
            }
        }

        CartItem cartItem =
                new CartItem(foodItem, quantity);

        cartItems.add(cartItem);
    }

    public void removeItem(int foodId) {

        cartItems.removeIf(
                item -> item.getFoodItem()
                        .getFoodId() == foodId
        );
    }

    public void increaseQuantity(int foodId) {

        for (CartItem item : cartItems) {

            if (item.getFoodItem().getFoodId()
                    == foodId) {

                item.setQuantity(
                        item.getQuantity() + 1
                );

                return;
            }
        }
    }

    public void decreaseQuantity(int foodId) {

        for (CartItem item : cartItems) {

            if (item.getFoodItem().getFoodId()
                    == foodId) {

                if (item.getQuantity() > 1) {

                    item.setQuantity(
                            item.getQuantity() - 1
                    );

                } else {

                    removeItem(foodId);
                }

                return;
            }
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalAmount() {

        double total = 0;

        for (CartItem item : cartItems) {

            total += item.getTotalPrice();
        }

        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}