import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TopologyGUI extends JFrame {

    private JTextArea outputArea;
    private DrawPanel drawPanel;

    public TopologyGUI() {
        setTitle("Network Topology Visualizer");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Title
        JLabel title = new JLabel("Network Topology Visualizer", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(30, 30, 150));

        // Output Area
        outputArea = new JTextArea(6, 30);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Draw Panel
        drawPanel = new DrawPanel();

        // Scan Button
        JButton scanButton = new JButton("Scan Network");
        scanButton.setFont(new Font("Arial", Font.BOLD, 14));
        scanButton.addActionListener(e -> scanNetwork());

        add(title, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        add(scanButton, BorderLayout.EAST);
    }

    private void scanNetwork() {
        outputArea.setText("");

        try {
            // Placeholder IPs for GitHub safe posting
            String localIP = "This PC";
            String routerIP = "Router";
            String subnet = "192.168.1.";

            outputArea.append("Local PC: " + localIP + "\n");
            outputArea.append("Router: " + routerIP + "\n");

            // Simulated devices
            List<String> devices = new ArrayList<>();
            devices.add(subnet + "10");
            devices.add(subnet + "15");
            devices.add(subnet + "20");
            devices.add(subnet + "25");
            devices.add(subnet + "30");

            drawPanel.setNetworkData(routerIP, localIP, devices);
            outputArea.append("\nScan completed successfully.\n");

        } catch (Exception e) {
            outputArea.append("Error: " + e.getMessage());
        }
    }

    class DrawPanel extends JPanel {
        private String routerIP = "";
        private String localIP = "";
        private List<String> devices = new ArrayList<>();
        private String hoverText = "";
        private Rectangle routerRect = new Rectangle();
        private List<Rectangle> deviceRects = new ArrayList<>();
        private Rectangle pcRect = new Rectangle();

        void setNetworkData(String rIP, String lIP, List<String> devs) {
            routerIP = rIP;
            localIP = lIP;
            devices = devs;
            repaint();
        }

        public DrawPanel() {
            setToolTipText("");
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    hoverText = getHoverText(e.getX(), e.getY());
                    setToolTipText(hoverText);
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkClick(e.getX(), e.getY());
                }
            });
        }

        private void checkClick(int x, int y) {
            if (routerRect.contains(x, y)) {
                outputArea.append("Clicked Router\n");
            }
            for (int i = 0; i < deviceRects.size(); i++) {
                if (deviceRects.get(i).contains(x, y)) {
                    outputArea.append("Clicked Device: " + devices.get(i) + "\n");
                }
            }
            if (pcRect.contains(x, y)) {
                outputArea.append("Clicked This PC\n");
            }
        }

        private String getHoverText(int x, int y) {
            if (routerRect.contains(x, y)) return "Router";
            for (int i = 0; i < deviceRects.size(); i++)
                if (deviceRects.get(i).contains(x, y)) return "Device: " + devices.get(i);
            if (pcRect.contains(x, y)) return "This PC";
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            Color bg1 = new Color(230, 240, 255);
            Color bg2 = new Color(200, 220, 255);
            g2.setPaint(new GradientPaint(0, 0, bg1, 0, getHeight(), bg2));
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (routerIP.isEmpty()) {
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("Click 'Scan Network' to visualize topology", getWidth() / 2 - 150, getHeight() / 2);
                return;
            }

            int width = getWidth();
            int height = getHeight();

            // Router at top-center
            int routerW = 80, routerH = 80;
            int routerX = width / 2 - routerW / 2;
            int routerY = 50;
            g2.setColor(new Color(34, 139, 34));
            g2.fillOval(routerX, routerY, routerW, routerH);
            g2.setColor(Color.BLACK);
            g2.drawOval(routerX, routerY, routerW, routerH);
            g2.drawString(routerIP, routerX + 15, routerY + 95);
            routerRect.setBounds(routerX, routerY, routerW, routerH);

            // Devices in semicircle
            deviceRects.clear();
            int deviceW = 100, deviceH = 60;
            int centerX = width / 2;
            int centerY = routerY + 200;
            int radius = 200;
            int n = devices.size();
            for (int i = 0; i < n; i++) {
                double angle = Math.PI * (i / (double) (n - 1)); // 0 to 180 degrees
                int dx = centerX + (int) (radius * Math.cos(angle)) - deviceW / 2;
                int dy = centerY + (int) (radius * Math.sin(angle)) - deviceH / 2;
                g2.setColor(new Color(0, 191, 255));
                g2.fillRoundRect(dx, dy, deviceW, deviceH, 15, 15);
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(dx, dy, deviceW, deviceH, 15, 15);
                g2.drawString("Device", dx + 25, dy + 25);
                g2.drawString(devices.get(i), dx + 10, dy + 45);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(routerX + routerW / 2, routerY + routerH, dx + deviceW / 2, dy);
                deviceRects.add(new Rectangle(dx, dy, deviceW, deviceH));
            }

            // PC at bottom-right
            int pcW = 100, pcH = 60;
            int pcX = width - 180;
            int pcY = centerY + 100;
            g2.setColor(new Color(148, 0, 211));
            g2.fillRoundRect(pcX, pcY, pcW, pcH, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(pcX, pcY, pcW, pcH, 15, 15);
            g2.drawString("This PC", pcX + 15, pcY + 25);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(routerX + routerW / 2, routerY + routerH, pcX + pcW / 2, pcY);
            pcRect.setBounds(pcX, pcY, pcW, pcH);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TopologyGUI().setVisible(true));
    }
}
