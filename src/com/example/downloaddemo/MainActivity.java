package com.example.downloaddemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.downloaddemo.DownloadService.LocalServiceBinder;
import com.kevin.downloadfile.DownloaderObserver;
import com.kevin.utils.SharePreferencesUtils;

;

public class MainActivity extends ActionBarActivity implements
		DownloaderObserver, OnItemClickListener {

	public static final String TAG = "tag";
	public static final String URL = "url";
	public static final String DOWNING = "download";

	private TextView mTV;
	private ListView mListView;
	private SharePreferencesUtils mPreferencesUtils;
	MyAdapter adapter;

	private DownloadService mDownloadService;
	private HashMap<Integer, ProgressBar> bars = new HashMap<>();

	ArrayList<String> lists;

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
			}
		}
	};

	private ArrayList<String> initData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("http://wiki.lbsyun.baidu.com/cms/BaiduLBS_AndroidSDK_Sample.zip");
		list.add("https://codeload.github.com/baoyongzhang/SwipeMenuListView/zip/master");
		list.add("https://codeload.github.com/chenyi2013/ScrollInsideScroll/zip/master");
		list.add("http://www.eoeandroid.com/forum.php?mod=attachment&aid=MTQ1MjY1fGU4NmFhYjdlfDE0MjMwMzkxNTV8ODAyNDA4fDU2MjQ0Mw%3D%3D");
		list.add("http://bs.baidu.com/baidulbs-android-1-5-1/BaiduLBS_Android_V1.5.1_503317088.zip");
		list.add("http://sharesdk.mob.com/index.php/Download/android_down");
		return list;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				mTV.setText(msg.arg1 + "%");
				if (msg.arg2 >= mListView.getFirstVisiblePosition()
						&& msg.arg2 <= mListView.getLastVisiblePosition()) {
					bars.get(msg.arg2).setProgress(msg.arg1);
				}

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
		Intent intent = new Intent(this, DownloadService.class);
		bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
		mPreferencesUtils = new SharePreferencesUtils(this);
		mListView = (ListView) findViewById(R.id.list_view);

		mTV = (TextView) findViewById(R.id.text_view);
		mTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this, SecActivity.class);
				startActivity(intent);

			}
		});

		lists = initData();
		adapter = new MyAdapter(this);
		adapter.bindData(lists);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

	}

	@Override
	public void update(int tag, int progress) {

		Message msg = mHandler.obtainMessage();
		msg.arg1 = progress;
		msg.arg2 = tag;
		msg.what = 1;
		msg.sendToTarget();

	}

	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
	};

	class MyAdapter extends BaseAdapter implements OnClickListener {

		private ArrayList<String> lists = new ArrayList<String>();
		LayoutInflater inflater = null;

		public MyAdapter(Context context) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		class ViewHolder {
			Button download;
			ProgressBar progressBar;
		}

		public void bindData(ArrayList<String> data) {
			if (data != null) {
				lists = data;
			}

		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.download = (Button) convertView
						.findViewById(R.id.download);
				viewHolder.download.setOnClickListener(this);
				viewHolder.progressBar = (ProgressBar) convertView
						.findViewById(R.id.progress_bar);
				convertView.setTag(viewHolder);

			}

			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.download.setTag(position);

			if (mPreferencesUtils.isSuccess(lists.get(position))) {
				viewHolder.progressBar.setProgress(100);
			} else {
				viewHolder.progressBar.setProgress(0);
			}

			return convertView;
		}

		@Override
		public void onClick(View v) {

			int position = (int) v.getTag();
			mDownloadService.down(MainActivity.this, lists.get(position),
					position);

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, SecActivity.class);
		intent.putExtra(TAG, position);
		intent.putExtra(URL, lists.get(position));
		intent.putExtra(DOWNING, true);
		startActivity(intent);

	}

}
