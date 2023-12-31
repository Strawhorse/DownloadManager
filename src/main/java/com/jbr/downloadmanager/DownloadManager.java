package com.jbr.downloadmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class DownloadManager extends JFrame {

    private JTextField addText;
    private DownloadsTableModel tableModel;
    private JTable table;
    private JButton pauseButton, resumeButton, clearButton, cancelButton;
    private Download selectedDownload;

//    flag for whether selection is being cleared - might not work
    private boolean clearing;


    public DownloadManager(){
//        title and size
        setTitle("Download Manager");
        setMinimumSize(new Dimension(800,500));


//        add listener for when closing the program
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
//                System.exit(0);
            }
        });

//        set title menu on top left
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });

        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);


//        set up the Add panel and Add Download button
        JPanel addPanel = new JPanel();
        JTextField addTextField = new JTextField(30);
        addPanel.add(addTextField);

        JButton addButton = new JButton("Add Download");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        addPanel.add(addButton);


    }


}
