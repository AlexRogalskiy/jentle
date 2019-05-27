package com.wildbeeslabs.jentle.algorithms.retry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
 
/**
 * @author Crunchify.com
 */
 
public class CrunchifyRetryNTimes {
 
	public static void main(String[] args) {
 
		// Execute this Program as it is once to get correct response.
 
		// Change URL to
		// https://crunchify.com/wp-content/uploads/code/json.sample2.txt to
		// see retry logic in action..
		String myURL = "https://cdn.crunchify.com/wp-content/uploads/code/json.sample.txt";
		CrunchifyRetryOnExceptionStrategy retry = new CrunchifyRetryOnExceptionStrategy();
		while (retry.shouldRetry()) {
			try {
				System.out.println("Requested URL:" + myURL);
				StringBuilder sb = new StringBuilder();
				URLConnection urlConn = null;
				InputStreamReader in = null;
				URL url = new URL(myURL);
				urlConn = url.openConnection();
				if (urlConn != null)
					urlConn.setReadTimeout(60 * 1000);
				if (urlConn != null && urlConn.getInputStream() != null) {
					in = new InputStreamReader(urlConn.getInputStream(),
							Charset.defaultCharset());
					BufferedReader bufferedReader = new BufferedReader(in);
					if (bufferedReader != null) {
						int cp;
						while ((cp = bufferedReader.read()) != -1) {
							sb.append((char) cp);
						}
						bufferedReader.close();
					}
				}
				System.out.println(sb.toString());
				in.close();
				break;
 
			} catch (Exception e) {
				try {
					System.out.println("in catch.....");
					retry.errorOccured();
				} catch (RuntimeException e1) {
					throw new RuntimeException("Exception while calling URL:"
							+ myURL, e);
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
			}
		}
	}
 
	static class CrunchifyRetryOnExceptionStrategy {
		public static final int DEFAULT_RETRIES = 3;
		public static final long DEFAULT_WAIT_TIME_IN_MILLI = 2000;
 
		private int numberOfRetries;
		private int numberOfTriesLeft;
		private long timeToWait;
 
		public CrunchifyRetryOnExceptionStrategy() {
			this(DEFAULT_RETRIES, DEFAULT_WAIT_TIME_IN_MILLI);
		}
 
		public CrunchifyRetryOnExceptionStrategy(int numberOfRetries,
				long timeToWait) {
			this.numberOfRetries = numberOfRetries;
			numberOfTriesLeft = numberOfRetries;
			this.timeToWait = timeToWait;
		}
 
		/**
		 * @return true if there are tries left
		 */
		public boolean shouldRetry() {
			return numberOfTriesLeft > 0;
		}
 
		public void errorOccured() throws Exception {
			numberOfTriesLeft--;
			if (!shouldRetry()) {
				throw new Exception("Retry Failed: Total " + numberOfRetries
						+ " attempts made at interval " + getTimeToWait()
						+ "ms");
			}
			waitUntilNextTry();
		}
 
		public long getTimeToWait() {
			return timeToWait;
		}
 
		private void waitUntilNextTry() {
			try {
				Thread.sleep(getTimeToWait());
			} catch (InterruptedException ignored) {
			}
		}
	}
}
