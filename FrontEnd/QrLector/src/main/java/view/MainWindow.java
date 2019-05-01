package view;

import com.github.sarxos.webcam.WebcamPanel;
import controller.MainController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainController controller;
    private JComboBox<String> devices;
    private WebcamPanel panel;

    public MainWindow(){

        controller = new MainController(this);
        setLayout(new BorderLayout());
        setSize(600,400);
        add(panel,BorderLayout.CENTER);
        setVisible(true);
    }

    public JComboBox<String> getDevices() {
        return devices;
    }

    public void setDevices(JComboBox devices) {
        this.devices = devices;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(WebcamPanel panel) {
        this.panel = panel;
    }
}
