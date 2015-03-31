package com.example.downloaddemo;

import com.example.downloaddemo.DownloadService.LocalServiceBinder;
import com.kevin.downloadfile.DownloaderObserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

public class SecActivity extends Activity implements DownloaderObserver {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sec);
		Intent intent = new Intent(this, DownloadService.class);
		bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
		mTV = (TextView) findViewById(R.id.textView);
	}

	private TextView mTV;
	private Button mButton;

	private DownloadService mDownloadService;
	private boolean isBinder = false;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mDownloadService = null;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if (service != null) {
				mDownloadService = ((LocalServiceBinder) service)
						.getDownloadService();
				isBinder = true;

				Intent intent = getIntent();
				if (intent != null) {

					String str = intent.getStringExtra(MainActivity.URL);
					int tag = intent.getIntExtra(MainActivity.TAG, -1);
					mDownloadService.down(SecActivity.this, str, tag);
				}

			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				mTV.setText(msg.arg1 + "%");
				break;

			default:
				break;
			}

		};

	};

	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
	};

	@Override
	public void update(int tag, int progress) {
		Message msg = mHandler.obtainMessage();
		msg.arg1 = progress;
		msg.what = 1;
		msg.sendToTarget();

	}
}
