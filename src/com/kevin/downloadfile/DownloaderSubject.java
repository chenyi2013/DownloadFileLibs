package com.kevin.downloadfile;

/**
 * 
 * @author Kevin
 * @data 2015/3/31
 * 
 */
public interface DownloaderSubject {

	/**
	 * ������������Ǽ�һ���µĹ۲��߶���
	 * 
	 * @param observer
	 */
	public void attach(DownloaderObserver observer);

	/**
	 * �����������ɾ��һ���Ѿ��Ǽǹ��Ĺ۲��߶���
	 * 
	 * @param observer
	 */
	public void detach(DownloaderObserver observer);

	/**
	 * �����������֪ͨ���еǼǹ��Ĺ۲��߶���
	 */
	public void notifyObservers();

}
