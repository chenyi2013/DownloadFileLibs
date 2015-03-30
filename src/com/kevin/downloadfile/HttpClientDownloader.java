package com.kevin.downloadfile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

/**
 * 
 * @author Kevin
 * @data 2015/3/30
 */
public class HttpClientDownloader implements Downloader {

    @Override
    public void performDownload(int method, String path,
            HashMap<String, String> header, HashMap<String, String> param,
            String dir, String fileName, OnDownloadListener listener,
            OnDownloadError error) {

        OutputStream out = null;
        InputStream in = null;

        HttpClient httpClient = null;
        HttpResponse response = null;

        long totalLength = 0;
        int currentLength = 0;
        double progress;

        try {

            File dirFile = new File(dir);

            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            File file = new File(dir + File.separator + fileName);

            httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, 5000);

            if (method == GET) {

                String sp = DownloaderUtil.formatRequestParam(param);

                if (sp != null) {
                    path = path + "?" + sp;
                }

                HttpGet get = new HttpGet(path);

                if (header != null) {

                    Set<String> set = header.keySet();
                    for (String key : set) {
                        get.addHeader(key, header.get(key));
                    }

                }
                response = httpClient.execute(get);
            } else {

                HttpPost post = new HttpPost(path);

                if (header != null) {

                    Set<String> set = header.keySet();
                    for (String key : set) {
                        post.addHeader(key, header.get(key));
                    }

                }

                if (param != null) {

                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();

                    Set<String> set = param.keySet();

                    for (String key : set) {
                        pairs.add(new BasicNameValuePair(key, param.get(key)));
                    }

                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                            pairs);

                    post.setEntity(formEntity);
                }

                response = httpClient.execute(post);

            }

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                HttpEntity entity = response.getEntity();

                in = entity.getContent();
                totalLength = entity.getContentLength();
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
            }

        } catch (ClientProtocolException e) {
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

                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }

        }

    }

}
