package com.digitalemu.gui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class Monitor extends JFrame {

	private static final long serialVersionUID = 1L;
	private String name;
	private JPanel panel = new JPanel();
	private JTextArea area = new JTextArea();
	private DefaultCaret caret = (DefaultCaret)area.getCaret();
	private JScrollPane scrollPane = new JScrollPane(area);
	private int winColumns = 80;
	private int winRows = 20;


	public Monitor(String tname) {
		name = tname;
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	initUi();
            }
		});
    }
	
	private void initUi(){
		area.setColumns(winColumns);
		area.setRows(winRows);
		area.setAutoscrolls(true);
		area.setLineWrap(true);
	    area.setWrapStyleWord(true);
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  // autoscroll
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setAutoscrolls(true);
		panel.add(scrollPane);
		add(panel);
		pack();						// Set window size to fit components
		setTitle(name+" @ "+Thread.currentThread().toString());
		setLocationRelativeTo(null);

		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
	}
	
	public void setSize(int col, int row){
		winColumns=col;
		winRows=row;
		area.setColumns(winColumns);
		area.setRows(winRows);
		pack();
	}
	
	public void close(){
		setVisible(false);
        dispose();
	}
	
	public void clear(){
		area.setText("");
	}
	

	public void println(String mss){
		area.append(mss+"\n");		
	}
	
	public void print(String mss){
		area.append(mss);		
	}

}