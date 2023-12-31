package com.jbr.downloadmanager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


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
            public void actionPerformed(ActionEvent e) {
                actionAdd();
            }
        });
        addPanel.add(addButton);


//        Downloads table, main section
        tableModel = new DownloadsTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableSelectionChanged();
            }
        });


//        only one row at a time can be selected in the interface
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



//        Progress bar as a renderer for the progress column
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
//        show progress text
        table.setDefaultRenderer(JProgressBar.class, renderer);


//        set table row height enough to fit JProgressBar
        table.setRowHeight((int) renderer.getPreferredSize().getHeight());


//        create the downloads panel
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);


//        set up buttons panel (at the bottom of the interface - later)
        JPanel buttonsPanel = new JPanel();

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener((e) -> {
            actionPause();
        });
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);

        resumeButton = new JButton("Resume");
        resumeButton.addActionListener((e)->{
            actionResume();
        });
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);

        cancelButton = new JButton("Resume");
        cancelButton.addActionListener((e)->{
            actionCancel();
        });
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);

        clearButton = new JButton("Resume");
        clearButton.addActionListener((e)->{
            actionClear();
        });
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);


//        add the created panels to the display
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);


    }

    //        exiting the program when finished method
    private void actionExit() {
        System.exit(0);
    }


//    method for adding a new download
    private void actionAdd(){
        URL verifiedUrl = verifyUrl(addTextField.getText());
        if(verifiedUrl!=null) {
            tableModel.addDownload(new Download(verifiedUrl));
            addTextField.setText("");
//            resets the add text field
        }
        else{
            JOptionPane.showMessageDialog(this, "URL is invalid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


//    method to verify the download and return a URL
    private URL verifyUrl(String url) throws MalformedURLException {
//        will only allow HTTP urls
        if(!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")){
            return null;
        }

//        verify the format of the URL
        URL verifiedUrl = new URL(url);

        if(verifiedUrl.getFile().length()<2) {
            return null;
        }

        return verifiedUrl;
    }


//    method for when table row selection changes
    private void tableSelectionChanged(){
        if(selectedDownload !=null){
            selectedDownload.deleteObserver(DownloadManager.this);
        }
//        to not get notifications for the download

        if(!clearing) {
            selectedDownload = tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(DownloadManager.this);
            updateButtons();
        }
//        receive notifications for the download
    }



//    method to pause the selected download
    private void actionPause(){
        selectedDownload.pause();
        updateButtons();
    }


//    method to resume the download
    private void actionResume(){
        selectedDownload.resume();
        updateButtons;
    }


//    method to cancel the selected download
    private void actionCancel(){
        selectedDownload.cancel();
        updateButtons();
    }


//    method to clear the selection
    private void actionClear(){
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }

}
