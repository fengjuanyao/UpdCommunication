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
	 * ������Ϣ��������
	 */
	public String send() {
		final StringBuilder sb = new StringBuilder();
		InetAddress local = null;
		String str = null;
		ByteArrayOutputStream ostream = null;
		try {
			local = InetAddress.getByName(SERVERIP); // ��������
			sb.append("���ҵ�������,������...").append("/n");
		} catch (UnknownHostException e) {
			sb.append("δ�ҵ�������.").append("/n");
			e.printStackTrace();
		}
		try {
			if (dSocket == null) {
				dSocket = new DatagramSocket();
			}// ע��˴�Ҫ���������ļ�������Ȩ��,�������Ȩ�޲�����쳣
			sb.append("�������ӷ�����...").append("/n");
		} catch (SocketException e) {
			e.printStackTrace();
			sb.append("����������ʧ��.").append("/n");
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
					sb.append("�ɹ����ӷ�����...").append("/n");
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

	// ����Handler����
	private Handler handler = new Handler() {
		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// ����UI
			switch (msg.what) {
			case 1:
				Log.i("TAG", "�ɹ�����");
				dSocket.close();
				break;
			case 0:
				Log.i("TAG", "����ʧ��");
				break;
			default:
				break;
			}
		}
	};

}
