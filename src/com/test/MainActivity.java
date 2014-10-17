package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.udpconnect.R;

public class MainActivity extends Activity {
	EditText msg_et = null;
	Button send_bt = null;
	TextView info_tv = null;
	private TextView button1;
	private UDPClient client;
	private UDPServer server;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		msg_et = (EditText) findViewById(R.id.msg_et);
		send_bt = (Button) findViewById(R.id.send_bt);
		info_tv = (TextView) findViewById(R.id.info_tv);
		button1 = (TextView) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (client.getdSocket() != null) {
					client.getdSocket().close();
				}

			}
		});
		// 开启服务器
		ExecutorService exec = Executors.newCachedThreadPool();
		server = new UDPServer();
		exec.execute(server);

		// 发送消息
		send_bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				client = new UDPClient(msg_et.getText().toString().trim());
				info_tv.setText(client.send());
			}
		});

	}

	private String getIp() {
		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 检查Wifi状态
		if (!wm.isWifiEnabled())
			wm.setWifiEnabled(true);
		WifiInfo wi = wm.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAdd = wi.getIpAddress();
		// 把整型地址转换成“*.*.*.*”地址
		String ip = intToIp(ipAdd);
		return ip;
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
}
