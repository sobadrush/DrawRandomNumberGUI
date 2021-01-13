package com.ctbc.DrawRandomNumberGUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.ctbc.utils.MyUtils;
import javax.swing.JProgressBar;
import java.awt.List;

public class MySwingForm {

	private JFrame frame;
	private JTextField textField_min;
	private JTextField textField_max;

	private Object lock = new Object(); // lock
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MySwingForm window = new MySwingForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MySwingForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 672, 288);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label_min = new JLabel("請輸入最小值");
		label_min.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		label_min.setHorizontalAlignment(SwingConstants.CENTER);
		label_min.setBounds(22, 22, 145, 33);
		frame.getContentPane().add(label_min);

		JLabel label_min_1 = new JLabel("請輸入最大值");
		label_min_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_min_1.setFont(new Font("微軟正黑體", Font.BOLD, 18));
		label_min_1.setBounds(22, 78, 145, 33);
		frame.getContentPane().add(label_min_1);

		textField_min = new JTextField();
		textField_min.setText("1");
		textField_min.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
		textField_min.setHorizontalAlignment(SwingConstants.CENTER);
		textField_min.setBounds(177, 22, 110, 33);
		frame.getContentPane().add(textField_min);
		textField_min.setColumns(10);

		textField_max = new JTextField();
		textField_max.setText("12");
		textField_max.setHorizontalAlignment(SwingConstants.CENTER);
		textField_max.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
		textField_max.setColumns(10);
		textField_max.setBounds(177, 78, 110, 33);
		frame.getContentPane().add(textField_max);
		
		java.awt.List awtList = new List();
		awtList.setFont(new Font("Dialog", Font.BOLD, 22));
		awtList.setBounds(307, 22, 314, 176);
		frame.getContentPane().add(awtList);		
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(new Color(64, 224, 208));
		progressBar.setBounds(41, 208, 580, 25);
		frame.getContentPane().add(progressBar);
		
		JButton btnStartDraw = new JButton("開始抽籤");
		btnStartDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (StringUtils.isBlank(textField_min.getText())) {
					JOptionPane.showMessageDialog(null, "請輸入最小值！", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (StringUtils.isBlank(textField_max.getText())) {
					JOptionPane.showMessageDialog(null, "請輸入最大值！", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// ------------------------------------------------------------------
				int partOfProgressBar = MyUtils.getRandomNumberUsingNextInt(3, 4);
				System.out.println("partOfProgressBar >>> " + partOfProgressBar);
				int increaseUnit = progressBar.getMaximum() / partOfProgressBar;
				Thread progressBarThread = new Thread(() -> {
					
					for (int i = 1; i <= partOfProgressBar; i++) {
						
						if (i == partOfProgressBar && progressBar.getValue() != progressBar.getMaximum()) {
							progressBar.setValue(progressBar.getMaximum());
							
							progressBar.update(progressBar.getGraphics()); // progressBar 更新值畫面不更新處理 ( http://hk.uwenku.com/question/p-gmqmyyqe-bkq.html )
							
							synchronized (lock) {
								lock.notify();
							}
							
							break;
						}
						
						progressBar.setValue(increaseUnit * i);
						progressBar.update(progressBar.getGraphics());
						
						try {
							TimeUnit.MILLISECONDS.sleep(600);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				});
				progressBarThread.start();
				// ------------------------------------------------------------------
				
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				int min = Integer.parseInt(textField_min.getText());
				int max = Integer.parseInt(textField_max.getText());
				
				int randNum = MyUtils.getRandomNumberUsingNextInt(min, max);
				System.out.println("Random number is : " + randNum);
				
				// awt.list 中文亂碼解決 ( https://www.itdaan.com/tw/f43e93d7358f )
				awtList.add(String.format("產生的亂數為：%d", randNum));
				
			}
		});
		btnStartDraw.setFont(new Font("標楷體", Font.BOLD, 23));
		btnStartDraw.setBounds(41, 121, 246, 77);
		frame.getContentPane().add(btnStartDraw);
		
	}
}
