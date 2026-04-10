import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Калькулятор");
        frame.setSize(400, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        field1.setFont(new Font("Arial", Font.PLAIN, 16));
        field2.setFont(new Font("Arial", Font.PLAIN, 16));
        field1.setBackground(new Color(40, 40, 60));
        field2.setBackground(new Color(40, 40, 60));
        field1.setForeground(Color.WHITE);
        field2.setForeground(Color.WHITE);
        field1.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field2.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field1.setToolTipText("Введите первое число");
        field2.setToolTipText("Введите второе число");

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.add(field1);
        topPanel.add(field2);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        String[][] ops = {{"+", "−", "×"}, {"÷", "C", "="}};
        String[][] tips = {{"Сложение", "Вычитание", "Умножение"}, {"Деление", "Очистить", "Результат"}};
        JLabel resultLabel = new JLabel("Результат: ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setToolTipText("Здесь появится результат вычисления");

        ActionListener calc = e -> {
            try {
                String cmd = ((JButton) e.getSource()).getText();
                if ("C".equals(cmd)) { field1.setText(""); field2.setText(""); resultLabel.setText("Результат: "); return; }
                if ("=".equals(cmd)) { resultLabel.setText("Результат: введите операцию"); return; }
                double a = Double.parseDouble(field1.getText());
                double b = Double.parseDouble(field2.getText());
                double res = switch (cmd) {
                    case "+" -> a + b;
                    case "−" -> a - b;
                    case "×" -> a * b;
                    case "÷" -> b == 0 ? Double.NaN : a / b;
                    default -> 0;
                };
                resultLabel.setText(Double.isNaN(res) ? "Ошибка: деление на 0" : "Результат: " + res);
            } catch (NumberFormatException ex) { resultLabel.setText("Ошибка: введите числа"); }
        };

        int rowIdx = 0, colIdx = 0;
        for (String[] row : ops) {
            colIdx = 0;
            for (String op : row) {
                JButton btn = new JButton(op);
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.setForeground(Color.WHITE);
                btn.setBackground(new Color(80, 50, 120));
                btn.setFocusPainted(false);
                btn.setBorderPainted(false);
                btn.setToolTipText(tips[rowIdx][colIdx]);
                btn.addActionListener(calc);
                btnPanel.add(btn);
                colIdx++;
            }
            rowIdx++;
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            private Image marble;
            { setOpaque(false); }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (marble == null || marble.getWidth(null) != getWidth() || marble.getHeight(null) != getHeight()) {
                    marble = createMarble(getWidth(), getHeight());
                }
                g.drawImage(marble, 0, 0, null);
            }
            private Image createMarble(int w, int h) {
                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img.createGraphics();
                g2.setPaint(new GradientPaint(0, 0, new Color(220, 215, 210), w, h, new Color(160, 150, 145)));
                g2.fillRect(0, 0, w, h);
                Random r = new Random(42);
                for (int i = 0; i < 60; i++) {
                    g2.setColor(new Color(200 + r.nextInt(55), 195 + r.nextInt(50), 190 + r.nextInt(45), 40 + r.nextInt(30)));
                    g2.setStroke(new BasicStroke(r.nextInt(4) + 1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int x = r.nextInt(w), y = r.nextInt(h), rad = r.nextInt(80) + 20;
                    g2.drawOval(x - rad, y - rad, rad * 2, rad * 2);
                }
                for (int i = 0; i < 25; i++) {
                    g2.setColor(new Color(180 + r.nextInt(40), 170 + r.nextInt(40), 165 + r.nextInt(35), 25));
                    g2.setStroke(new BasicStroke(r.nextInt(3) + 1));
                    int x1 = r.nextInt(w), y1 = r.nextInt(h);
                    g2.drawArc(x1 - 50, y1 - 50, r.nextInt(120) + 40, r.nextInt(120) + 40, r.nextInt(360), r.nextInt(180));
                }
                g2.dispose();
                return img;
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.CENTER);
        mainPanel.add(resultLabel, BorderLayout.SOUTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
