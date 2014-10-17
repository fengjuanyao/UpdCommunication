package com.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import android.util.Log;

public class UDPServer implements Runnable {
	private static final int PORT = 6000;

	private byte[] msg = new byte[1024];

	private boolean life = true;
	private DatagramSocket dSocket = null;

	public UDPServer() {
	}

	/**
	 * @return the life
	 */
	public boolean isLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(boolean life) {
		this.life = life;
	}

	@SuppressWarnings("resource")
	@Override
	public void run() {

		DatagramPacket dPacket = new DatagramPacket(msg, msg.length);
		try {
			if (dSocket == null) {
				dSocket = new DatagramSocket(PORT);
			}
			while (true) {
				try {
					dSocket.receive(dPacket);
					DataInputStream istream = new DataInputStream(
							new ByteArrayInputStream(dPacket.getData(),
									dPacket.getOffset(), dPacket.getLength()));
					Log.i("msg sever received", istream.readUTF());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
