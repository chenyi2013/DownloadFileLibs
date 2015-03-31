package com.kevin.downloadfile;

public interface DownloaderObserver {

	/**
	 * 调用这个方法更新自己
	 */
	public void update(int tag, int progress);

}
