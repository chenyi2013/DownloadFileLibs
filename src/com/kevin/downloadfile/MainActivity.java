package com.kevin.downloadfile;

import com.example.downloaddemo.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private TextView mTV;
	private Button mButton;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTV = (TextView) findViewById(R.id.text_view);
		mButton = (Button) findViewById(R.id.download);

		final DownloadFileHelper mDownloadFileHelper = new DownloadFileHelper();

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("click download btn");

				mDownloadFileHelper
						.download(
								Downloader.GET,
								"http://wiki.lbsyun.baidu.com/cms/BaiduLBS_AndroidSDK_Sample.zip",
								null, null,
								getDir("hello", Context.MODE_PRIVATE)
										.getAbsolutePath(), "hello",
								new OnDownloadListener() {

									@Override
									public void progress(double progress,
											String path) {
										int pro = (int) (progress * 100);
										Message msg = mHandler.obtainMessage();
										msg.arg1 = pro;
										msg.what = 1;
										msg.sendToTarget();

									}
								},null);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
