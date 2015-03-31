package com.kevin.downloadfile;

import java.util.HashSet;

/**
 * 
 * @author Kevin
 * @data 2015/3/31
 * 
 */
public class ConcreteDownloaderSubject implements DownloaderSubject {

	private HashSet<DownloaderObserver> observers = new HashSet<>();

	private int progress;
	private int tag;

	public int getProgress() {
		return progress;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	@Override
	public void attach(DownloaderObserver observer) {
		observers.add(observer);
	}

	@Override
	public void detach(DownloaderObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {

		for (DownloaderObserver observer : observers) {
			observer.update(tag, progress);
		}
	}

}
