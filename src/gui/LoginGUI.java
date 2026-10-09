package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;

import dao.UserDAO;
import model.User;
import util.UserSession;

public class LoginGUI extends JFrame {

    // ---------- Design tokens ----------

    private static final Color CHILI        = new Color(0xC8382B);
    private static final Color CHILI_DARK   = new Color(0xA32A20);
    private static final Color CHILI_PLATE  = new Color(0xD4493B);
    private static final Color SAFFRON      = new Color(0xF2A413);
    private static final Color PAPER        = new Color(0xFFFFFF);
    private static final Color INK          = new Color(0x1F2933);
    private static final Color INK_MUTED    = new Color(0x6B7785);
    private static final Color FIELD_FILL   = new Color(0xF6F4F1);
    private static final Color FIELD_LINE   = new Color(0xDAD5CE);
    private static final Color HINT         = new Color(0xA39E97);
    private static final Color ERROR_COLOR  = new Color(0xB3261E);

    private static final String FONT_NAME = "Segoe UI";

    private static Font font(int style, int size) {
        return new Font(FONT_NAME, style, size);
    }

    // ---------- Components ----------

    private JTextField emailField;
    private JPasswordField passwordField;

    private InputBox emailBox;
    private InputBox passwordBox;

    private RoundedButton loginButton;
    private JLabel errorLabel;

    private boolean busy = false;


    public LoginGUI() {

        setTitle("Food Delivery System - Login");
        setSize(860, 520);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(new BrandPanel());
        root.add(buildFormPanel());

        setContentPane(root);

        // Enter key = Log in
        getRootPane().setDefaultButton(loginButton);

        setVisible(true);

        emailField.requestFocusInWindow();
    }


    // =====================================================
    //  Right side: login form
    // =====================================================

    private JPanel buildFormPanel() {

        JPanel form = new JPanel();
        form.setBackground(PAPER);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(40, 56, 40, 56));

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(font(Font.BOLD, 30));
        heading.setForeground(INK);

        JLabel subHeading = new JLabel(
                "Log in to order from your favourite restaurants."
        );
        subHeading.setFont(font(Font.PLAIN, 14));
        subHeading.setForeground(INK_MUTED);

        // Email
        JLabel emailLabel = fieldLabel("Email");

        emailField = new JTextField();
        emailBox = new InputBox(
                emailField,
                "you@example.com",
                null
        );

        // Password
        JLabel passwordLabel = fieldLabel("Password");

        passwordField = new JPasswordField();
        final char hiddenEcho = passwordField.getEchoChar();

        JButton toggleButton = textButton("Show", INK_MUTED);
        toggleButton.setFont(font(Font.PLAIN, 13));
        toggleButton.addActionListener(e -> {

            boolean showing =
                    passwordField.getEchoChar() == (char) 0;

            if (showing) {
                passwordField.setEchoChar(hiddenEcho);
                toggleButton.setText("Show");
            } else {
                passwordField.setEchoChar((char) 0);
                toggleButton.setText("Hide");
            }
        });

        passwordBox = new InputBox(
                passwordField,
                "Enter your password",
                toggleButton
        );

        // Inline error message
        errorLabel = new JLabel(" ");
        errorLabel.setFont(font(Font.PLAIN, 13));
        errorLabel.setForeground(ERROR_COLOR);

        // Login button
        loginButton = new RoundedButton("Log in");
        loginButton.addActionListener(e -> loginUser());

        // Register row
        JPanel registerRow = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        registerRow.setOpaque(false);

        JLabel newHere = new JLabel("New here?  ");
        newHere.setFont(font(Font.PLAIN, 14));
        newHere.setForeground(INK_MUTED);

        JButton registerLink =
                textButton("Create an account", CHILI);
        registerLink.setFont(font(Font.BOLD, 14));
        registerLink.addActionListener(e -> {

            new RegisterGUI();

            dispose();
        });

        registerRow.add(newHere);
        registerRow.add(registerLink);

        // Left-align everything and stack
        JComponent[] stack = {
                heading, subHeading,
                emailLabel, emailBox,
                passwordLabel, passwordBox,
                errorLabel, loginButton, registerRow
        };

        for (JComponent c : stack) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        registerRow.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 28)
        );

        form.add(Box.createVerticalGlue());
        form.add(heading);
        form.add(Box.createVerticalStrut(6));
        form.add(subHeading);
        form.add(Box.createVerticalStrut(32));
        form.add(emailLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(emailBox);
        form.add(Box.createVerticalStrut(18));
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(passwordBox);
        form.add(Box.createVerticalStrut(10));
        form.add(errorLabel);
        form.add(Box.createVerticalStrut(12));
        form.add(loginButton);
        form.add(Box.createVerticalStrut(22));
        form.add(registerRow);
        form.add(Box.createVerticalGlue());

        return form;
    }


    private JLabel fieldLabel(String text) {

        JLabel label = new JLabel(text);
        label.setFont(font(Font.BOLD, 13));
        label.setForeground(INK);

        return label;
    }


    private JButton textButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setForeground(color);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return button;
    }


    // =====================================================
    //  Login logic
    // =====================================================

    private void loginUser() {

        if (busy) {
            return;
        }

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                ).trim();

        clearError();

        // Empty field validation

        if (email.isEmpty() || password.isEmpty()) {

            showError("Enter your email and password.");

            if (email.isEmpty()) {
                emailBox.setInvalid(true);
            }

            if (password.isEmpty()) {
                passwordBox.setInvalid(true);
            }

            if (email.isEmpty()) {
                emailField.requestFocusInWindow();
            } else {
                passwordField.requestFocusInWindow();
            }

            return;
        }

        // Email validation

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            showError("Enter a valid email address.");

            emailBox.setInvalid(true);
            emailField.requestFocusInWindow();

            return;
        }

        // Login from database (off the UI thread so the window never freezes)

        setBusy(true);

        new SwingWorker<User, Void>() {

            @Override
            protected User doInBackground() {

                UserDAO userDAO = new UserDAO();

                return userDAO.getUserByEmailAndPassword(
                        email,
                        password
                );
            }

            @Override
            protected void done() {

                setBusy(false);

                try {

                    User user = get();

                    if (user != null) {

                        UserSession.setCurrentUser(user);

                        new HomeGUI();

                        dispose();

                    } else {

                        passwordField.setText("");

                        showError("Email or password is incorrect.");

                        emailBox.setInvalid(true);
                        passwordBox.setInvalid(true);

                        passwordField.requestFocusInWindow();
                    }

                } catch (Exception ex) {

                    ex.printStackTrace();

                    showError(
                            "Could not reach the database. Try again."
                    );
                }
            }

        }.execute();
    }


    private void setBusy(boolean value) {

        busy = value;

        loginButton.setText(
                value ? "Logging in..." : "Log in"
        );

        setCursor(
                value
                        ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                        : Cursor.getDefaultCursor()
        );
    }


    private void showError(String message) {

        errorLabel.setText(message);
    }


    private void clearError() {

        errorLabel.setText(" ");

        emailBox.setInvalid(false);
        passwordBox.setInvalid(false);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new LoginGUI());
    }


    // =====================================================
    //  Left side: brand panel (plate motif)
    // =====================================================

    private static class BrandPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // Solid chili background
            g2.setColor(CHILI);
            g2.fillRect(0, 0, w, h);

            // The plate: concentric rings seen from above,
            // sitting half off the panel's bottom-right corner
            int cx = w - 30;
            int cy = h - 20;

            g2.setColor(CHILI_PLATE);
            fillCircle(g2, cx, cy, 250);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 70));
            drawCircle(g2, cx, cy, 250);
            drawCircle(g2, cx, cy, 205);

            g2.setColor(CHILI_DARK);
            fillCircle(g2, cx, cy, 160);

            // Saffron dot on the plate
            g2.setColor(SAFFRON);
            fillCircle(g2, cx - 78, cy - 78, 26);

            // Heading
            g2.setColor(Color.WHITE);
            g2.setFont(font(Font.BOLD, 40));
            g2.drawString("Food Delivery", 56, 150);
            g2.drawString("System", 56, 198);

            // Tagline
            g2.setColor(new Color(255, 255, 255, 220));
            g2.setFont(font(Font.PLAIN, 17));
            g2.drawString("Hot, fresh and on its way", 56, 240);
            g2.drawString("from the kitchen to your door.", 56, 266);

            // Small project tag at the top
            g2.setColor(new Color(255, 255, 255, 170));
            g2.setFont(font(Font.PLAIN, 12));
            g2.drawString("ARNA 4.0 project", 56, 56);

            g2.dispose();
        }

        private void fillCircle(Graphics2D g2, int cx, int cy, int r) {
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
        }

        private void drawCircle(Graphics2D g2, int cx, int cy, int r) {
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        }
    }


    // =====================================================
    //  Rounded input box with hint text, focus ring and
    //  invalid state
    // =====================================================

    private static class InputBox extends JPanel {

        private final JTextComponent field;
        private final String hint;

        private boolean focused = false;
        private boolean invalid = false;

        InputBox(JTextComponent field,
                 String hint,
                 JComponent trailing) {

            super(new BorderLayout());

            this.field = field;
            this.hint = hint;

            setOpaque(false);
            setBorder(new EmptyBorder(0, 14, 0, 12));

            field.setOpaque(false);
            field.setBorder(new EmptyBorder(0, 0, 0, 0));
            field.setFont(font(Font.PLAIN, 15));
            field.setForeground(INK);
            field.setCaretColor(CHILI);

            add(field, BorderLayout.CENTER);

            if (trailing != null) {
                add(trailing, BorderLayout.EAST);
            }

            field.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    focused = true;
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    repaint();
                }
            });

            // Typing clears the red "invalid" state
            field.getDocument().addDocumentListener(
                    new DocumentListener() {

                        public void insertUpdate(DocumentEvent e) {
                            clearInvalid();
                        }

                        public void removeUpdate(DocumentEvent e) {
                            clearInvalid();
                        }

                        public void changedUpdate(DocumentEvent e) {
                            clearInvalid();
                        }
                    }
            );

            setPreferredSize(new Dimension(300, 46));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        }

        void setInvalid(boolean value) {
            invalid = value;
            repaint();
        }

        private void clearInvalid() {
            if (invalid) {
                invalid = false;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            g2.setColor(FIELD_FILL);
            g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);

            if (invalid) {
                g2.setColor(ERROR_COLOR);
                g2.setStroke(new BasicStroke(1.6f));
            } else if (focused) {
                g2.setColor(CHILI);
                g2.setStroke(new BasicStroke(1.8f));
            } else {
                g2.setColor(FIELD_LINE);
                g2.setStroke(new BasicStroke(1.2f));
            }

            g2.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);

            g2.dispose();
        }

        @Override
        protected void paintChildren(Graphics g) {

            super.paintChildren(g);

            if (field.getDocument().getLength() == 0) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                );

                g2.setFont(field.getFont());
                g2.setColor(HINT);

                FontMetrics fm = g2.getFontMetrics();

                int x = field.getX() + field.getInsets().left;

                int y = field.getY()
                        + (field.getHeight() - fm.getHeight()) / 2
                        + fm.getAscent();

                g2.drawString(hint, x, y);

                g2.dispose();
            }
        }
    }


    // =====================================================
    //  Rounded button with hover / pressed colours
    // =====================================================

    private static class RoundedButton extends JButton {

        private boolean hover = false;
        private boolean pressed = false;

        RoundedButton(String text) {

            super(text);

            setFont(font(Font.BOLD, 15));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(
                    Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            );

            setPreferredSize(new Dimension(300, 46));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (pressed) {
                g2.setColor(CHILI_DARK.darker());
            } else if (hover) {
                g2.setColor(CHILI_DARK);
            } else {
                g2.setColor(CHILI);
            }

            g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 12, 12
            );

            g2.dispose();

            // Draws the button text on top
            super.paintComponent(g);
        }
    }
}