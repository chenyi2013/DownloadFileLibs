package com.example.downloaddemo;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.kevin.downloadfile.ConcreteDownloaderSubject;
import com.kevin.downloadfile.DownloadFileHelper;
import com.kevin.downloadfile.Downloader;
import com.kevin.downloadfile.DownloaderObserver;
import com.kevin.downloadfile.OnDownloadError;
import com.kevin.downloadfile.OnDownloadListener;
import com.kevin.downloadfile.OnDownloderSuccess;
import com.kevin.utils.SharePreferencesUtils;

public class DownloadService extends Service {

	private LocalServiceBinder mBinder = new LocalServiceBinder();

	private HashSet<String> sets = new HashSet<String>();
	private HashMap<String, ConcreteDownloaderSubject> subjects = new HashMap<>();

	private SharePreferencesUtils mPreferencesUtils;

	public DownloadService() {
	}

	@Override
	public IBinder onBind(Intent intent) {

		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mPreferencesUtils = new SharePreferencesUtils(this);
	}

	public class LocalServiceBinder extends Binder {

		/**
		 * 客户端调用此方法获得DownloadService的实例
		 * */
		public DownloadService getDownloadService() {

			return DownloadService.this;
		}

	}

	public void down(final DownloaderObserver downloader, final String str,
			int tag) {

		if (!subjects.containsKey(str)) {
			subjects.put(str, new ConcreteDownloaderSubject());
		}

		final ConcreteDownloaderSubject subject = subjects.get(str);
		subject.attach(downloader);

		if (!sets.contains(str)) {
			sets.add(str);
			subject.setTag(tag);
			final DownloadFileHelper mDownloadFileHelper = new DownloadFileHelper();
			mDownloadFileHelper.download(Downloader.GET, str, null, null,
					getDir("hello", Context.MODE_PRIVATE).getAbsolutePath(),
					"hello" + tag, new OnDownloadListener() {

						@Override
						public void progress(double progress, String path) {
							int pro = (int) (progress * 100);
							subject.setProgress(pro);
							subject.notifyObservers();
						}
					}, new OnDownloderSuccess() {

						@Override
						public void completed(String url) {
							subject.detach(downloader);
							subjects.remove(str);
							sets.remove(str);
							mPreferencesUtils.save(str, true);

							System.out.println("completed");
						}
					}, new OnDownloadError() {

						@Override
						public void Error(String url, String error) {
							subject.detach(downloader);
							subjects.remove(str);
							sets.remove(str);

							System.out.println("adderror");
						}
					});
		}

	}
}
