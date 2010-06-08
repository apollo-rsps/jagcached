package org.apollo.jagcached.dispatch;

import java.io.IOException;


import org.apollo.jagcached.fs.IndexedFileSystem;
import org.jboss.netty.channel.Channel;

/**
 * The base class for request workers.
 * @author Graham
 * @param <T> The type of request.
 */
public abstract class RequestWorker<T> implements Runnable {
	
	/**
	 * The file system.
	 */
	private final IndexedFileSystem fs;
	
	/**
	 * An object used for locking checks to see if the worker is running.
	 */
	private final Object lock = new Object();
	
	/**
	 * A flag indicating if the worker should be running.
	 */
	private boolean running = true;
	
	/**
	 * Creates the request worker with the specified file system.
	 * @param fs The file system.
	 */
	public RequestWorker(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Stops this worker. The worker's thread may need to be interrupted.
	 */
	public final void stop() {
		synchronized (lock) {
			running = false;
		}
	}
		
	@Override
	public final void run() {
		try {
			while (true) {
				synchronized (lock) {
					if (!running) {
						break;
					}
				}
				
				ChannelRequest<T> request;
				try {
					request = nextRequest();
				} catch (InterruptedException e) {
					continue;
				}
				
				Channel channel = request.getChannel();
				
				try {
					service(fs, channel, request.getRequest());
				} catch (IOException e) {
					e.printStackTrace();
					channel.close();
				}
			}
		} finally {
			try {
				fs.close();
			} catch (IOException ex) {
				/* ignore */
			}
		}
	}

	/**
	 * Gets the next request.
	 * @return The next request.
	 * @throws InterruptedException if the thread is interrupted.
	 */
	protected abstract ChannelRequest<T> nextRequest() throws InterruptedException;
	
	/**
	 * Services a request.
	 * @param fs The file system.
	 * @param channel The channel.
	 * @param request The request to service.
	 * @throws IOException if an I/O error occurs.
	 */
	protected abstract void service(IndexedFileSystem fs, Channel channel, T request) throws IOException;

}
