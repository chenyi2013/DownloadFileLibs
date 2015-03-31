package com.kevin.downloadfile;

import java.util.HashMap;

/**
 * 
 * @author Kevin
 * @data 2015/3/30
 * 
 */
public interface Downloader {

	public static final int GET = 1;
	public static final int POST = 2;

	public void performDownload(int method, String url,
			HashMap<String, String> header, HashMap<String, String> param,
			String dir, String fileName, OnDownloadListener list,
			OnDownloderSuccess successListener, OnDownloadError error);
}
