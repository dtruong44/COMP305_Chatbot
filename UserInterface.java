import java.awt.*;
import javax.swing.*;

public final class UserInterface {

    private final JFrame window;
    private final GridBagConstraints gbc;
    private JTextArea chat;
    private String currentMessage;

    Color darkBlue = new Color(10, 25, 74);
    Color lightBlue = new Color(173, 216, 230);
    
    public UserInterface() {
        this.window = new JFrame("Chatbot");
        window.setSize(400, 500);
        window.setLayout(new GridBagLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(darkBlue);
        this.gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        window.add(createWelcomeLabel(), gbc);

        gbc.gridy = 1;
        window.add(createStartButton(), gbc);

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    public JLabel createWelcomeLabel() {
        JLabel welcome_label = new JLabel("Welcome to the Chatbot");
        welcome_label.setFont(new Font("Serif", Font.BOLD, 20));
        welcome_label.setForeground(lightBlue);
        return welcome_label;
    }

    public JButton createStartButton() {
        JButton start_button = new JButton("Start Chatting");
        start_button.addActionListener(e -> setChat());
        return start_button;
    }

    public void setChat() {
        window.getContentPane().removeAll();

        gbc.gridx = 0;
        gbc.gridy = 0;

        window.add(createChatArea());

        gbc.gridy = 1;
        window.add(createUserInputArea(), gbc);
        
        window.revalidate();
        window.repaint();
    }

    public JPanel createChatArea() {
        JPanel chat_area = new JPanel();
        chat_area.setLayout(new BoxLayout(chat_area, BoxLayout.X_AXIS));
        chat_area.add(createButtons());
        chat_area.setBackground(darkBlue);
        chat_area.setBackground(darkBlue);
        JTextArea text_area = new JTextArea(20, 30);
        this.chat = text_area;
        text_area.setLineWrap(true);
        text_area.setWrapStyleWord(true);
        text_area.setEditable(false);
        text_area.setBackground(lightBlue);
        text_area.setForeground(darkBlue);
        text_area.setCaretColor(Color.WHITE);
        text_area.append("BOT: Hello, welcome to the chat\n");

        JScrollPane scroll_pane = new JScrollPane(text_area);
        scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        chat_area.add(scroll_pane);
        
        return chat_area;
    }

    public JPanel createButtons() {
        JPanel button_panel = new JPanel();
        button_panel.setLayout(new BoxLayout(button_panel, BoxLayout.Y_AXIS));
        JButton clear_button = new JButton("Clear Chat");
        JButton save_button = new JButton("Save Chat");
        JButton open_chat_button = new JButton("Open Chat");
        JButton summary_button = new JButton("Summarize");
        JButton quit_button = new JButton("Quit");
        quit_button.addActionListener(e -> System.exit(0));
        button_panel.add(quit_button);

        button_panel.setBackground(lightBlue);
        button_panel.add(clear_button);
        button_panel.add(save_button);
        button_panel.add(open_chat_button);
        button_panel.add(summary_button);

        return button_panel;
    }

    public JPanel createUserInputArea() {
        JPanel user_input_panel = new JPanel();
        user_input_panel.setBackground(darkBlue);
        JTextArea user_text_area = new JTextArea(3, 20);
        user_text_area.setLineWrap(true);
        user_text_area.setWrapStyleWord(true);
        user_text_area.setBackground(lightBlue);
        user_text_area.setForeground(Color.BLACK);

        JButton send_button = new JButton("Send");
        send_button.addActionListener(e -> {sendMessage(chat, user_text_area);
            try {
                sendChatBotMessage(chat,user_text_area);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        user_input_panel.add(user_text_area);
        user_input_panel.add(send_button);

        JLabel charCounter = createCharCounter(user_text_area);
        user_input_panel.add(charCounter);


        return user_input_panel;
    }
    public JLabel createCharCounter(JTextArea userTextArea) {
        JLabel charCountLabel = new JLabel("Characters: 0");

        charCountLabel.setForeground(Color.WHITE);
        userTextArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateCount();
            }
    
            private void updateCount() {
                charCountLabel.setText("Characters: " + userTextArea.getText().length());
            }
        });
    
        return charCountLabel;
    }
    

    public void sendMessage(JTextArea chat, JTextArea user_message) {
        String message = user_message.getText();
        if (!(message.equals(""))) {
            currentMessage = message;
            chat.append("USER: " + message + "\n");
            user_message.setText("");
        }
    }

    public void sendChatBotMessage(JTextArea chat, JTextArea user_message) throws Exception {
        String message = ChatbotEngine.createResponse(currentMessage);
        chat.append("BOT: " + message + "\n");
    }
}