package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import dao.UserDAO;
import model.User;

public class RegisterGUI extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField addressField;

    private final Color BLUE = new Color(35, 83, 200);
    private final Color BACKGROUND = new Color(245, 247, 252);
    private final Color DARK = new Color(35, 40, 55);


    public RegisterGUI() {

        setTitle("ARNA 4.0 FOOD DELIVERY - Register");
        setSize(520, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createRegisterScreen();

        setVisible(true);
    }

    private void createRegisterScreen() {

        JPanel mainPanel = new JPanel();

        mainPanel.setBackground(BACKGROUND);

        mainPanel.setLayout(
                new BoxLayout(
                        mainPanel,
                        BoxLayout.Y_AXIS
                )
        );

        mainPanel.setBorder(
                new EmptyBorder(
                        30,
                        50,
                        30,
                        50
                )
        );

        JLabel title =
                new JLabel(
                        "Create Your Account"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(BLUE);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Join ARNA 4.0 Food Delivery"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(Color.GRAY);

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        mainPanel.add(title);

        mainPanel.add(
                Box.createVerticalStrut(5)
        );

        mainPanel.add(subtitle);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // Name
        mainPanel.add(
                createLabel("Name")
        );

        nameField =
                createTextField();

        mainPanel.add(nameField);

        mainPanel.add(
                Box.createVerticalStrut(12)
        );

        // Email
        mainPanel.add(
                createLabel("Email")
        );

        emailField =
                createTextField();

        mainPanel.add(emailField);

        mainPanel.add(
                Box.createVerticalStrut(12)
        );

        // Password
        mainPanel.add(
                createLabel("Password")
        );

        passwordField =
                new JPasswordField();

        styleField(passwordField);

        mainPanel.add(passwordField);

        mainPanel.add(
                Box.createVerticalStrut(12)
        );

        // Phone
        mainPanel.add(
                createLabel("Phone")
        );

        phoneField =
                createTextField();

        mainPanel.add(phoneField);

        mainPanel.add(
                Box.createVerticalStrut(12)
        );

        // Address
        mainPanel.add(
                createLabel("Address")
        );

        addressField =
                createTextField();

        mainPanel.add(addressField);

        mainPanel.add(
                Box.createVerticalStrut(25)
        );

        // Register button
        RoundedButton registerButton =
                new RoundedButton(
                        "Create Account"
                );

        registerButton.setNormalColor(BLUE);

        registerButton.setHoverColor(
                new Color(
                        20,
                        65,
                        175
                )
        );

        registerButton.setForeground(
                Color.WHITE
        );

        registerButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        registerButton.setPreferredSize(
                new Dimension(
                        250,
                        45
                )
        );

        registerButton.addActionListener(
                e -> registerUser()
        );

        mainPanel.add(registerButton);

        mainPanel.add(
                Box.createVerticalStrut(15)
        );

        // Back button
        JButton backButton =
                new JButton(
                        "Back to Login"
                );

        backButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        backButton.setForeground(BLUE);

        backButton.setBorderPainted(false);

        backButton.setContentAreaFilled(false);

        backButton.setFocusPainted(false);

        backButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        backButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        backButton.addActionListener(
                e -> {

                    new LoginGUI();

                    dispose();
                }
        );

        mainPanel.add(backButton);

        add(mainPanel);
    }

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(DARK);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        styleField(field);

        return field;
    }

    private void styleField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        210,
                                        214,
                                        220
                                )
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );
    }

    private void registerUser() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                ).trim();

        String phone =
                phoneField.getText().trim();

        String address =
                addressField.getText().trim();

        // Empty field validation
        if (name.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || phone.isEmpty()
                || address.isEmpty()) {

            showError(
                    "Please fill all fields!"
            );

            return;
        }

        // Name validation
        if (!name.matches(
                "[a-zA-Z ]+"
        )) {

            showError(
                    "Name should contain only letters."
            );

            return;
        }

        // Email validation
        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {

            showError(
                    "Please enter a valid email."
            );

            return;
        }

        // Password validation
        if (password.length() < 5) {

            showError(
                    "Password must contain at least 5 characters."
            );

            return;
        }

        // Phone validation
        if (!phone.matches(
                "\\d{10}"
        )) {

            showError(
                    "Phone number must contain exactly 10 digits."
            );

            return;
        }

        User user =
                new User();

        user.setName(name);

        user.setEmail(email);

        user.setPassword(password);

        user.setPhone(phone);

        user.setAddress(address);

        UserDAO userDAO =
                new UserDAO();

        boolean result =
                userDAO.registerUser(user);

        if (result) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration Successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            new LoginGUI();

            dispose();

        } else {

            showError(
                    "Registration failed.\n"
                    + "Email may already exist."
            );
        }
    }

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Registration Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private static class RoundedButton
            extends JButton {

        private Color normalColor =
                Color.BLUE;

        private Color hoverColor =
                Color.DARK_GRAY;

        public RoundedButton(
                String text
        ) {

            super(text);

            setFocusPainted(false);

            setBorderPainted(false);

            setContentAreaFilled(false);

            setOpaque(false);

            setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            15
                    )
            );

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

            setBackground(color);

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

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                RegisterGUI::new
        );
    }
}