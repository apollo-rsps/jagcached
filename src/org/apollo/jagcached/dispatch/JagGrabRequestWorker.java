package org.apollo.jagcached.dispatch;

import java.io.IOException;
import java.nio.ByteBuffer;


import org.apollo.jagcached.fs.IndexedFileSystem;
import org.apollo.jagcached.net.jaggrab.JagGrabRequest;
import org.apollo.jagcached.net.jaggrab.JagGrabResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * A worker which services JAGGRAB requests.
 * @author Graham
 */
public final class JagGrabRequestWorker extends RequestWorker<JagGrabRequest> {

	/**
	 * Creates the JAGGRAB request worker.
	 * @param fs The file system.
	 */
	public JagGrabRequestWorker(IndexedFileSystem fs) {
		super(fs);
	}

	@Override
	protected ChannelRequest<JagGrabRequest> nextRequest() throws InterruptedException {
		return RequestDispatcher.nextJagGrabRequest();
	}

	@Override
	protected void service(IndexedFileSystem fs, Channel channel, JagGrabRequest request) throws IOException {
		String path = request.getFilePath();
		ByteBuffer buf = VirtualResourceMapper.getVirtualResource(fs, path);
		if (buf == null) {
			channel.close();
		} else {
			ChannelBuffer wrapped = ChannelBuffers.wrappedBuffer(buf);
			channel.write(new JagGrabResponse(wrapped)).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
