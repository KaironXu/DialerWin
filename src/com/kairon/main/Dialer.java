package com.kairon.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.CRC32;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.kairon.util.MD5Util;
import com.kairon.util.Rpm;
import com.kairon.util.XmlIO;

public class Dialer extends JFrame {

	/**
	 * version 1.0
	 */
	private static final long serialVersionUID = 1L;
	final int FAIL_DIAL = 0100;
	final int SUCCESS_DIAL = 0101;
	final int FAIL_DISCONNECT = 1010;
	final int SUCCESS_DISCONNECT = 1011;

	private JTextField userID = new JTextField(13);
	private JPasswordField password = new JPasswordField(13);
	private JTextField IPRouter = new JTextField(13);
	private JTextField userIdRouter = new JTextField(13);
	private JPasswordField passwordRouter = new JPasswordField(13);
	private JButton connect = new JButton();
	private JButton disconnect = new JButton();

	public static void main(String[] args) {
		new Dialer();
	}

	public Dialer() {
		super();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 关闭按钮处理事件
		Toolkit tool = Toolkit.getDefaultToolkit();
		Dimension screenSize = tool.getScreenSize();
		setSize(300, 250);
		setVisible(true);
		setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
		setTitle("校园拨号器");

		// 添加主面板
		JPanel mainPanel = new JPanel();
		final GridLayout gridLayout = new GridLayout(0, 2);
		gridLayout.setVgap(5);// 设置组件之间垂直距离
		gridLayout.setHgap(5);// 设置组件之间水平距离
		mainPanel.setLayout(gridLayout);
		getContentPane().add(mainPanel, BorderLayout.CENTER);

		JLabel userIdLable = new JLabel("宽带账号");
		userIdLable.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(userIdLable);
		mainPanel.add(userID);

		JLabel passwordLable = new JLabel("宽带密码");
		passwordLable.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(passwordLable);
		mainPanel.add(password);

		JLabel IPRouterLabel = new JLabel("路由IP");
		IPRouterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(IPRouterLabel);
		mainPanel.add(IPRouter);

		JLabel userIdRouterLabel = new JLabel("路由账号");
		userIdRouterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(userIdRouterLabel);
		mainPanel.add(userIdRouter);

		JLabel passwordRouterLabel = new JLabel("路由密码");
		passwordRouterLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(passwordRouterLabel);
		mainPanel.add(passwordRouter);

		connect.setText("拨号");
		connect.setHorizontalAlignment(SwingConstants.CENTER);
		connect.addActionListener(new ConnectListener());
		disconnect.setText("断网");
		disconnect.setHorizontalAlignment(SwingConstants.CENTER);
		disconnect.addActionListener(new DisconnectListener());
		mainPanel.add(disconnect);
		mainPanel.add(connect);

		// 用XML中的值初始化文本框
		Map<String, Map<String, String>> mapInfo = XmlIO.read("UserInfo.xml");
		if (!mapInfo.isEmpty()) {
			Map<String, String> mapUser = mapInfo.get("user");
			Map<String, String> mapRouter = mapInfo.get("router");
			userID.setText(mapUser.get("ID"));
			password.setText(mapUser.get("password"));
			IPRouter.setText(mapRouter.get("IP"));
			userIdRouter.setText(mapRouter.get("username"));
			passwordRouter.setText(mapRouter.get("password"));
		}
	}

	//拨号监听器类
	private class ConnectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ExecutorService exec = Executors.newCachedThreadPool();
			Future<Integer> result = exec.submit(new DialingThread());
			/*
			 * try { JOptionPane.showMessageDialog(null, result.get()); } catch
			 * (HeadlessException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (ExecutionException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */

		}
	}

	//断网监听器类
	private class DisconnectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ExecutorService exec = Executors.newCachedThreadPool();
			Future<Integer> result = exec.submit(new DisconnectThread());
			/*
			 * try { JOptionPane.showMessageDialog(null, result.get()); } catch
			 * (HeadlessException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (InterruptedException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (ExecutionException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
		}

	}

	//拨号子线程类
	private class DialingThread implements Callable<Integer> {

		public Integer call() {

			final String userIdStr = userID.getText().trim();
			final String passwordStr = password.getText().trim();
			final String userIdRouterStr = userIdRouter.getText().trim();
			final String passwordRouterStr = passwordRouter.getText().trim();
			final String IPStr = IPRouter.getText().trim();
			
			//保存用户数据
			XmlIO.write(userIdStr, passwordStr, IPStr, userIdRouterStr, passwordRouterStr);
			
			String Md5CodeStr;
			(new Date()).getTime();
			CRC32 crc32 = new CRC32();
			crc32.update((String.valueOf((new Date()).getTime()) + String
					.valueOf((new Random()).nextLong())).getBytes());
			String time = Long.toHexString(crc32.getValue());
			String temp = "jepyid" + userIdStr + time + passwordStr;
			Md5CodeStr = MD5Util.string2MD5(temp).substring(0, 20);

			String result = "~ghca" + time + "2007" + Md5CodeStr + userIdStr;

			String url = null;
			try {
				url = MessageFormat
						.format("http://192.168.1.1/userRpm/PPPoECfgRpm.htm?wan=0&wantype=2&acc={0}&psw={1}&confirm={1}&specialDial=100&SecType=0&sta_ip=0.0.0.0&sta_mask=0.0.0.0&linktype=1&waittime=0&Connect=%C1%AC+%BD%D3",
								(java.net.URLEncoder.encode(result, "UTF-8")),
								passwordStr);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Rpm.on(url, IPStr, userIdRouterStr, passwordRouterStr);

			return SUCCESS_DIAL;
		}

	}

	//断网子线程类
	private class DisconnectThread implements Callable<Integer> {

		@Override
		public Integer call() throws Exception {
			final String userIdRouterStr = userIdRouter.getText().trim();
			final String passwordRouterStr = passwordRouter.getText().trim();
			final String IPStr = IPRouter.getText().trim();
			Rpm.off(IPStr, userIdRouterStr, passwordRouterStr);
			return SUCCESS_DISCONNECT;
		}

	}
}
