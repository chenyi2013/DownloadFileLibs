package com.kevin.downloadfile;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Build;

/**
 * 
 * @author Kevin
 * @data 2015/3/30
 * 
 */
public class DownloadFileHelper {

	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private Downloader mDownloader;

	public DownloadFileHelper() {

		if (Build.VERSION.SDK_INT > 9) {
			mDownloader = new UrlDownloader();
		} else {
			mDownloader = new HttpClientDownloader();
		}

	}

	public void download(final int method, final String url,
			final HashMap<String, String> header,
			final HashMap<String, String> param, final String dir,
			final String fileName, final OnDownloadListener listener) {

		cachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				mDownloader.performDownload(method, url, header, param, dir,
						fileName, listener);
			}
		});

	}
}
