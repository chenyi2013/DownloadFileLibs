package com.kevin.downloadfile;

import java.util.HashMap;
import java.util.Set;

/**
 * 
 * @author Kevin
 * @data 2015/3/30
 */
public class DownloaderUtil {

	public static String formatRequestParam(HashMap<String, String> param) {

		StringBuffer sb = null;

		if (param != null && param.size() > 0) {

			sb = new StringBuffer();
			Set<String> set = param.keySet();
			for (String key : set) {
				sb.append(key).append("=").append(param.get(key)).append("&");

			}
			sb.deleteCharAt(sb.length() - 1);

			return sb.toString();

		}

		return null;

	}

}
