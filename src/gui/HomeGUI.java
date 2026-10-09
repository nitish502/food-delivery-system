package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import dao.RestaurantDAO;
import dao.FoodItemDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;

import model.Restaurant;
import model.FoodItem;
import model.Order;
import model.OrderItem;
import model.User;
import model.Cart;
import model.CartItem;

import util.UserSession;

public class HomeGUI extends JFrame {

    private Cart cart;

    private JLabel cartCountLabel;

    private final Color BLUE = new Color(35, 83, 200);
    private final Color LIGHT_BLUE = new Color(235, 242, 255);
    private final Color BACKGROUND = new Color(245, 247, 252);
    private final Color DARK = new Color(35, 40, 55);
    private final Color GREEN = new Color(30, 150, 85);
    private final Color RED = new Color(210, 60, 60);

    

    public HomeGUI() {

        cart = new Cart();

        setTitle("ARNA 4.0 FOOD DELIVERY");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createHomeScreen();

        setVisible(true);
    }

    // =====================================================
    // HOME SCREEN
    // =====================================================

    private void createHomeScreen() {

        GradientPanel mainPanel =
                new GradientPanel();

        mainPanel.setLayout(
                new BorderLayout()
        );

        // ---------------- HEADER ----------------

        JPanel header =
                new JPanel();

        header.setOpaque(false);

        header.setLayout(
                new BoxLayout(
                        header,
                        BoxLayout.Y_AXIS
                )
        );

        header.setBorder(
                new EmptyBorder(
                        30, 30, 25, 30
                )
        );

        JLabel title =
                new JLabel(
                        "ARNA 4.0 FOOD DELIVERY"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(
                Color.WHITE
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel welcome =
                new JLabel(
                        getWelcomeText()
                );

        welcome.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        17
                )
        );

        welcome.setForeground(
                new Color(235, 240, 255)
        );

        welcome.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        header.add(title);

        header.add(
                Box.createVerticalStrut(8)
        );

        header.add(welcome);

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // ---------------- MENU ----------------

        JPanel menu =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                22,
                                22
                        )
                );

        menu.setOpaque(false);

        menu.setBorder(
                new EmptyBorder(
                        20, 55, 25, 55
                )
        );

        JButton restaurantButton =
                createMenuButton(
                        "🍴  Restaurants"
                );

        JButton foodButton =
                createMenuButton(
                        "🍔  Food Items"
                );

        JButton ordersButton =
                createMenuButton(
                        "📦  My Orders"
                );

        JButton cartButton =
                createMenuButton(
                        "🛒  My Cart"
                );

        menu.add(restaurantButton);
        menu.add(foodButton);
        menu.add(ordersButton);
        menu.add(cartButton);

        mainPanel.add(
                menu,
                BorderLayout.CENTER
        );

        // ---------------- FOOTER ----------------

        JPanel footer =
                new JPanel();

        footer.setOpaque(false);

        footer.setBorder(
                new EmptyBorder(
                        5, 20, 25, 20
                )
        );

        JPanel cartInfo =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                8,
                                0
                        )
                );

        cartInfo.setOpaque(false);

        JLabel cartText =
                new JLabel(
                        "Cart Items:"
                );

        cartText.setForeground(
                Color.WHITE
        );

        cartText.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        cartCountLabel =
                new JLabel(
                        "0"
                );

        cartCountLabel.setForeground(
                Color.WHITE
        );

        cartCountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        cartInfo.add(cartText);
        cartInfo.add(cartCountLabel);

        RoundedButton logoutButton =
                new RoundedButton(
                        "Logout"
                );

        logoutButton.setPreferredSize(
                new Dimension(
                        120,
                        38
                )
        );

        logoutButton.setNormalColor(
                RED
        );

        logoutButton.setHoverColor(
                new Color(180, 40, 40)
        );

        logoutButton.addActionListener(
                e -> logout()
        );

        footer.add(cartInfo);

        footer.add(
                Box.createHorizontalStrut(30)
        );

        footer.add(logoutButton);

        mainPanel.add(
                footer,
                BorderLayout.SOUTH
        );

        // ---------------- ACTIONS ----------------

        restaurantButton.addActionListener(
                e -> showRestaurants()
        );

        foodButton.addActionListener(
                e -> showFoodItems()
        );

        ordersButton.addActionListener(
                e -> showOrders()
        );

        cartButton.addActionListener(
                e -> showCart()
        );

        add(mainPanel);

        updateCartCount();
    }

    private String getWelcomeText() {

        User user =
                UserSession.getCurrentUser();

        if (user != null) {

            return "Welcome, "
                    + user.getName()
                    + " 👋";
        }

        return "Welcome to Our Food Delivery";
    }

    // =====================================================
    // MENU BUTTON
    // =====================================================

    private JButton createMenuButton(
            String text
    ) {

        RoundedButton button =
                new RoundedButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        button.setNormalColor(
                Color.WHITE
        );

        button.setHoverColor(
                LIGHT_BLUE
        );

        button.setForeground(
                DARK
        );

        return button;
    }

    // =====================================================
    // RESTAURANTS
    // =====================================================

    private void showRestaurants() {

        RestaurantDAO dao =
                new RestaurantDAO();

        List<Restaurant> restaurants =
                dao.getAllRestaurants();

        if (restaurants.isEmpty()) {

            showError(
                    "No restaurants found!"
            );

            return;
        }

        JPanel panel =
                createScrollPanel();

        for (Restaurant restaurant :
                restaurants) {

            JPanel card =
                    createCard();

            card.setLayout(
                    new BoxLayout(
                            card,
                            BoxLayout.Y_AXIS
                    )
            );

            JLabel name =
                    new JLabel(
                            restaurant
                                    .getRestaurantName()
                    );

            name.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            20
                    )
            );

            name.setForeground(
                    BLUE
            );

            JLabel address =
                    new JLabel(
                            "📍 "
                            + restaurant.getAddress()
                    );

            JLabel phone =
                    new JLabel(
                            "☎ "
                            + restaurant.getPhone()
                    );

            card.add(name);

            card.add(
                    Box.createVerticalStrut(8)
            );

            card.add(address);
            card.add(phone);

            panel.add(card);

            panel.add(
                    Box.createVerticalStrut(12)
            );
        }

        showPanelDialog(
                panel,
                "Available Restaurants",
                560,
                400
        );
    }

    // =====================================================
    // FOOD
    // =====================================================

    private void showFoodItems() {

        FoodItemDAO dao =
                new FoodItemDAO();

        List<FoodItem> foods =
                dao.getAllFoodItems();

        if (foods.isEmpty()) {

            showError(
                    "No food items found!"
            );

            return;
        }

        JPanel panel =
                createScrollPanel();

        for (FoodItem food :
                foods) {

            JPanel card =
                    createCard();

            card.setLayout(
                    new BorderLayout(
                            15,
                            5
                    )
            );

            JPanel details =
                    new JPanel();

            details.setOpaque(false);

            details.setLayout(
                    new BoxLayout(
                            details,
                            BoxLayout.Y_AXIS
                    )
            );

            JLabel name =
                    new JLabel(
                            food.getFoodName()
                    );

            name.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            19
                    )
            );

            name.setForeground(
                    DARK
            );

            JLabel category =
                    new JLabel(
                            "Category: "
                            + food.getCategory()
                    );

            JLabel price =
                    new JLabel(
                            "₹"
                            + food.getPrice()
                    );

            price.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            18
                    )
            );

            price.setForeground(
                    GREEN
            );

            JLabel description =
                    new JLabel(
                            food.getDescription()
                    );

            description.setForeground(
                    Color.GRAY
            );

            details.add(name);

            details.add(
                    Box.createVerticalStrut(5)
            );

            details.add(category);
            details.add(price);

            details.add(
                    Box.createVerticalStrut(5)
            );

            details.add(description);

            RoundedButton addButton =
                    new RoundedButton(
                            "Add to Cart"
                    );

            addButton.setNormalColor(
                    BLUE
            );

            addButton.setHoverColor(
                    new Color(
                            20,
                            65,
                            175
                    )
            );

            addButton.setForeground(
                    Color.WHITE
            );

            addButton.setPreferredSize(
                    new Dimension(
                            130,
                            42
                    )
            );

            addButton.addActionListener(
                    e -> addFoodToCart(food)
            );

            card.add(
                    details,
                    BorderLayout.CENTER
            );

            card.add(
                    addButton,
                    BorderLayout.EAST
            );

            panel.add(card);

            panel.add(
                    Box.createVerticalStrut(12)
            );
        }

        showPanelDialog(
                panel,
                "Available Food Items",
                680,
                480
        );
    }

    private void addFoodToCart(
            FoodItem food
    ) {

        if (!cart.getCartItems().isEmpty()) {

            int currentRestaurant =
                    cart.getCartItems()
                            .get(0)
                            .getFoodItem()
                            .getRestaurantId();

            if (currentRestaurant
                    != food.getRestaurantId()) {

                showError(
                        "You can order food from only one "
                        + "restaurant at a time!"
                );

                return;
            }
        }

        cart.addItem(
                food,
                1
        );

        updateCartCount();

        showSuccess(
                food.getFoodName()
                + " added to cart!"
        );
    }

    // =====================================================
    // CART
    // =====================================================

    private void showCart() {

        if (cart.getCartItems().isEmpty()) {

            showError(
                    "Your cart is empty!"
            );

            return;
        }

        JPanel panel =
                createScrollPanel();

        for (CartItem item :
                cart.getCartItems()) {

            JPanel card =
                    createCard();

            card.setLayout(
                    new BorderLayout(
                            10,
                            5
                    )
            );

            JLabel name =
                    new JLabel(
                            item.getFoodItem()
                                    .getFoodName()
                    );

            name.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            17
                    )
            );

            JLabel price =
                    new JLabel(
                            "₹"
                            + item.getFoodItem()
                                    .getPrice()
                    );

            JLabel quantity =
                    new JLabel(
                            "Qty: "
                            + item.getQuantity()
                    );

            JButton minus =
                    new JButton("-");

            JButton plus =
                    new JButton("+");

            JButton remove =
                    new JButton("Remove");

            minus.addActionListener(
                    e -> {

                        cart.decreaseQuantity(
                                item.getFoodItem()
                                        .getFoodId()
                        );

                        showCart();
                    }
            );

            plus.addActionListener(
                    e -> {

                        cart.increaseQuantity(
                                item.getFoodItem()
                                        .getFoodId()
                        );

                        showCart();
                    }
            );

            remove.addActionListener(
                    e -> {

                        cart.removeItem(
                                item.getFoodItem()
                                        .getFoodId()
                        );

                        updateCartCount();

                        showCart();
                    }
            );

            JPanel info =
                    new JPanel();

            info.setOpaque(false);

            info.setLayout(
                    new BoxLayout(
                            info,
                            BoxLayout.Y_AXIS
                    )
            );

            info.add(name);
            info.add(price);
            info.add(quantity);

            JPanel actions =
                    new JPanel(
                            new FlowLayout()
                    );

            actions.setOpaque(false);

            actions.add(minus);
            actions.add(plus);
            actions.add(remove);

            card.add(
                    info,
                    BorderLayout.CENTER
            );

            card.add(
                    actions,
                    BorderLayout.SOUTH
            );

            panel.add(card);

            panel.add(
                    Box.createVerticalStrut(10)
            );
        }

        JLabel total =
                new JLabel(
                        "Cart Total: ₹"
                        + cart.getTotalAmount()
                );

        total.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        total.setForeground(
                GREEN
        );

        total.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(total);

        panel.add(
                Box.createVerticalStrut(15)
        );

        RoundedButton orderButton =
                new RoundedButton(
                        "PLACE ORDER"
                );

        orderButton.setNormalColor(
                GREEN
        );

        orderButton.setHoverColor(
                new Color(
                        20,
                        120,
                        65
                )
        );

        orderButton.setForeground(
                Color.WHITE
        );

        orderButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        orderButton.setPreferredSize(
                new Dimension(
                        180,
                        45
                )
        );

        orderButton.addActionListener(
                e -> placeOrder()
        );

        panel.add(orderButton);

        showPanelDialog(
                panel,
                "🛒 My Cart",
                680,
                450
        );
    }

    // =====================================================
    // PLACE ORDER
    // =====================================================

    private void placeOrder() {

        User user =
                UserSession.getCurrentUser();

        if (user == null) {

            showError(
                    "Please login first!"
            );

            return;
        }

        if (cart.getCartItems().isEmpty()) {

            showError(
                    "Your cart is empty!"
            );

            return;
        }

        String[] paymentMethods = {
                "Cash",
                "UPI",
                "Card"
        };

        String payment =
                (String)
                JOptionPane.showInputDialog(
                        this,
                        "Select Payment Method:",
                        "Payment",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        paymentMethods,
                        paymentMethods[0]
                );

        if (payment == null) {
            return;
        }

        int restaurantId =
                cart.getCartItems()
                        .get(0)
                        .getFoodItem()
                        .getRestaurantId();

        Order order =
                new Order();

        order.setUserId(
                user.getUserId()
        );

        order.setRestaurantId(
                restaurantId
        );

        order.setTotalAmount(
                cart.getTotalAmount()
        );

        order.setStatus(
                "Pending"
        );

        order.setPaymentMethod(
                payment
        );

        OrderDAO orderDAO =
                new OrderDAO();

        int orderId =
                orderDAO.placeOrderAndGetId(
                        order
                );

        if (orderId == -1) {

            showError(
                    "Order placement failed!"
            );

            return;
        }

        OrderItemDAO itemDAO =
                new OrderItemDAO();

        boolean success = true;

        for (CartItem item :
                cart.getCartItems()) {

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrderId(
                    orderId
            );

            orderItem.setFoodId(
                    item.getFoodItem()
                            .getFoodId()
            );

            orderItem.setQuantity(
                    item.getQuantity()
            );

            orderItem.setPrice(
                    item.getFoodItem()
                            .getPrice()
            );

            if (!itemDAO.addOrderItem(
                    orderItem
            )) {

                success = false;
            }
        }

        if (success) {

            double total =
                    cart.getTotalAmount();

            cart.clearCart();

            updateCartCount();

            showSuccess(
                    "Order Placed Successfully!\n\n"
                    + "Order ID: "
                    + orderId
                    + "\nPayment: "
                    + payment
                    + "\nTotal: ₹"
                    + total
            );

        } else {

            showError(
                    "Order created, but some items "
                    + "could not be saved."
            );
        }
    }

    // =====================================================
    // ORDERS
    // =====================================================

    private void showOrders() {

        User user =
                UserSession.getCurrentUser();

        if (user == null) {

            showError(
                    "Please login first!"
            );

            return;
        }

        OrderDAO orderDAO =
                new OrderDAO();

        List<Order> orders =
                orderDAO.getOrdersByUserId(
                        user.getUserId()
                );

        if (orders.isEmpty()) {

            showError(
                    "No orders found!"
            );

            return;
        }

        RestaurantDAO restaurantDAO =
                new RestaurantDAO();

        OrderItemDAO itemDAO =
                new OrderItemDAO();

        JTextArea area =
                new JTextArea();

        area.setEditable(false);

        area.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        StringBuilder text =
                new StringBuilder();

        for (Order order :
                orders) {

            text.append(
                    "====================================\n"
            );

            text.append(
                    "ORDER ID: "
            ).append(
                    order.getOrderId()
            ).append("\n");

            text.append(
                    "------------------------------------\n"
            );

            List<String> items =
                    itemDAO
                            .getOrderItemDetailsByOrderId(
                                    order.getOrderId()
                            );

            text.append(
                    "ITEMS:\n"
            );

            for (String item :
                    items) {

                text.append(
                        "  "
                ).append(
                        item
                ).append("\n");
            }

            Restaurant restaurant =
                    restaurantDAO
                            .getRestaurantById(
                                    order.getRestaurantId()
                            );

            text.append(
                    "\nRestaurant: "
            );

            if (restaurant != null) {

                text.append(
                        restaurant
                                .getRestaurantName()
                );

            } else {

                text.append(
                        "Unknown"
                );
            }

            text.append("\n");

            text.append(
                    "Total Amount: ₹"
            ).append(
                    order.getTotalAmount()
            ).append("\n");

            text.append(
                    "Order Date: "
            ).append(
                    order.getOrderDate()
            ).append("\n");

            text.append(
                    "Status: "
            ).append(
                    order.getStatus()
            ).append("\n");

            text.append(
                    "Payment: "
            ).append(
                    order.getPaymentMethod()
            ).append("\n\n");
        }

        area.setText(
                text.toString()
        );

        JScrollPane scroll =
                new JScrollPane(area);

        scroll.setPreferredSize(
                new Dimension(
                        650,
                        450
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                "📦 My Orders",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice ==
                JOptionPane.YES_OPTION) {

            UserSession.clearSession();

            new LoginGUI();

            dispose();
        }
    }

    // =====================================================
    // UI HELPERS
    // =====================================================

    private JPanel createCard() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        228,
                                        235
                                )
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        return panel;
    }

    private JPanel createScrollPanel() {

        JPanel panel =
                new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(
                BACKGROUND
        );

        panel.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        return panel;
    }

    private void showPanelDialog(
            JPanel panel,
            String title,
            int width,
            int height
    ) {

        JScrollPane scroll =
                new JScrollPane(
                        panel
                );

        scroll.setBorder(null);

        scroll.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                title,
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private void updateCartCount() {

        if (cartCountLabel != null) {

            int count = 0;

            for (CartItem item :
                    cart.getCartItems()) {

                count += item.getQuantity();
            }

            cartCountLabel.setText(
                    String.valueOf(count)
            );
        }
    }

    private void showSuccess(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Message",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =====================================================
    // CUSTOM GRADIENT PANEL
    // =====================================================

    private static class GradientPanel
            extends JPanel {

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g;

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            new Color(
                                    20,
                                    65,
                                    150
                            ),
                            0,
                            getHeight(),
                            new Color(
                                    75,
                                    125,
                                    230
                            )
                    );

            g2.setPaint(
                    gradient
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );
        }
    }

    // =====================================================
    // ROUNDED BUTTON
    // =====================================================

    private static class RoundedButton
            extends JButton {

        private Color normalColor =
                Color.WHITE;

        private Color hoverColor =
                new Color(
                        235,
                        242,
                        255
                );

        public RoundedButton(
                String text
        ) {

            super(text);

            setFocusPainted(false);

            setBorderPainted(false);

            setContentAreaFilled(false);

            setOpaque(false);

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );

            addMouseListener(
                    new java.awt.event.MouseAdapter() {

                        @Override
                        public void mouseEntered(
                                java.awt.event.MouseEvent e
                        ) {

                            setBackground(
                                    hoverColor
                            );

                            repaint();
                        }

                        @Override
                        public void mouseExited(
                                java.awt.event.MouseEvent e
                        ) {

                            setBackground(
                                    normalColor
                            );

                            repaint();
                        }
                    }
            );
        }

        public void setNormalColor(
                Color color
        ) {

            normalColor = color;

            setBackground(
                    color
            );

            repaint();
        }

        public void setHoverColor(
                Color color
        ) {

            hoverColor = color;
        }

        @Override
        protected void paintComponent(
                Graphics g
        ) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    getBackground()
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    20,
                    20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                HomeGUI::new
        );
    }
}