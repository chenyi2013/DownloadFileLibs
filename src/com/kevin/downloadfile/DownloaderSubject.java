package com.kevin.downloadfile;

/**
 * 
 * @author Kevin
 * @data 2015/3/31
 * 
 */
public interface DownloaderSubject {

	/**
	 * 调用这个方法登记一个新的观察者对象
	 * 
	 * @param observer
	 */
	public void attach(DownloaderObserver observer);

	/**
	 * 调用这个方法删除一个已经登记过的观察者对象
	 * 
	 * @param observer
	 */
	public void detach(DownloaderObserver observer);

	/**
	 * 调用这个方法通知所有登记过的观察者对象
	 */
	public void notifyObservers();

}
