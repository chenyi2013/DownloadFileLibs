package com.kevin.downloadfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * 
 * @author Kevin
 * @data 2015/3/30
 * 
 */
public class UrlDownloader implements Downloader {

	@Override
	public void performDownload(int method, String path,
			HashMap<String, String> header, HashMap<String, String> param,
			String dir, String fileName, OnDownloadListener listener,
			OnDownloderSuccess successListener, OnDownloadError error) {

		File dirFile = new File(dir);

		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		File file = new File(dir + File.separator + fileName);

		OutputStream out = null;
		InputStream in = null;

		int totalLength = 0;
		int currentLength = 0;
		double progress;

		HttpURLConnection conn = null;

		try {

			URL url = null;

			String sp = DownloaderUtil.formatRequestParam(param);

			if (sp != null && method == GET) {
				url = new URL(path + "?" + sp);
			} else {
				url = new URL(path);
			}

			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			byte[] data = null;

			if (method == POST) {
				conn.setRequestMethod("POST");

				if (sp != null) {
					data = sp.getBytes();
				}
				conn.setRequestProperty("Content-Length",
						String.valueOf(data.length));
			} else {
				conn.setRequestMethod("GET");
			}

			if (header != null) {
				Set<String> set = header.keySet();
				for (String key : set) {
					conn.setRequestProperty(key, header.get(key));
				}
			}

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

				if (method == POST) {
					OutputStream outputStream = conn.getOutputStream();
					outputStream.write(data);
					outputStream.close();
				}

				in = conn.getInputStream();
				totalLength = conn.getContentLength();
				out = new FileOutputStream(file);

				int len = -1;
				byte b[] = new byte[1024];

				while ((len = in.read(b)) > 0) {

					out.write(b, 0, len);

					if (listener != null) {
						currentLength = len + currentLength;
						progress = currentLength / (totalLength * 1.0);
						listener.progress(progress, path);
					}

				}
				successListener.completed(path);

			} else {
				error.Error(path, "Õ¯¬Á¡¨Ω” ß∞‹");
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
			if (error != null) {
				error.Error(path, e.getMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (error != null) {
				error.Error(path, e.getMessage());
			}
		} finally {

			try {
				if (in != null) {
					in.close();
				}

				if (out != null) {
					out.close();
				}

				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
				error.Error(path, e.getMessage());

			}

		}

	}
}
