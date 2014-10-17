package com.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UDPClient {
	private static final int SERVER_PORT = 6000;
	public static final String SERVERIP = "127.0.0.1";
	private DatagramSocket dSocket = null;

	private String msg;

	public DatagramSocket getdSocket() {
		return dSocket;
	}

	/**
	 * @param msg
	 */
	public UDPClient(String msg) {
		super();
		this.msg = msg;
	}

	/**
	 * 发送信息到服务器
	 */
	public String send() {
		final StringBuilder sb = new StringBuilder();
		InetAddress local = null;
		String str = null;
		ByteArrayOutputStream ostream = null;
		try {
			local = InetAddress.getByName(SERVERIP); // 本机测试
			sb.append("已找到服务器,连接中...").append("/n");
		} catch (UnknownHostException e) {
			sb.append("未找到服务器.").append("/n");
			e.printStackTrace();
		}
		try {
			if (dSocket == null) {
				dSocket = new DatagramSocket();
			}// 注意此处要先在配置文件里设置权限,否则会抛权限不足的异常
			sb.append("正在连接服务器...").append("/n");
		} catch (SocketException e) {
			e.printStackTrace();
			sb.append("服务器连接失败.").append("/n");
		}
		try {
			ostream= new ByteArrayOutputStream();
			DataOutputStream dataStream = new DataOutputStream(ostream);
			dataStream.writeUTF(msg);
			dataStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		final DatagramPacket dPacket = new DatagramPacket(ostream.toByteArray(),
				ostream.size(), local, SERVER_PORT);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sb.append("成功连接服务器...").append("/n");
					dSocket.send(dPacket);
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				}
			}
		}).start();

		return sb.toString();
	}

	// 定义Handler对象
	private Handler handler = new Handler() {
		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 处理UI
			switch (msg.what) {
			case 1:
				Log.i("TAG", "成功发送");
				dSocket.close();
				break;
			case 0:
				Log.i("TAG", "发送失败");
				break;
			default:
				break;
			}
		}
	};

}
