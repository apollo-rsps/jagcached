package org.apollo.jagcached.dispatch;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apollo.jagcached.fs.IndexedFileSystem;


/**
 * A class which maps 'virtual resources' to files in the
 * {@link IndexedFileSystem}.
 * @author Graham
 */
public final class VirtualResourceMapper {

	/**
	 * Gets a virtual resource.
	 * @param fs The file system.
	 * @param path The path.
	 * @return The resource, or {@code null} if it could not be found.
	 * @throws IOException if an I/O error occurs.
	 */
	public static ByteBuffer getVirtualResource(IndexedFileSystem fs, String path) throws IOException {
		if (path.startsWith("/crc")) {
			return fs.getCrcTable();
		} else if (path.startsWith("/title")) {
			return fs.getFile(0, 1);
		} else if (path.startsWith("/config")) {
			return fs.getFile(0, 2);
		} else if (path.startsWith("/interface")) {
			return fs.getFile(0, 3);
		} else if (path.startsWith("/media")) {
			return fs.getFile(0, 4);
		} else if (path.startsWith("/versionlist")) {
			return fs.getFile(0, 5);
		} else if (path.startsWith("/textures")) {
			return fs.getFile(0, 6);
		} else if (path.startsWith("/wordenc")) {
			return fs.getFile(0, 7);
		} else if (path.startsWith("/sounds")) {
			return fs.getFile(0, 8);
		}
		return null;
	}
	
	/**
	 * Default private constructor to prevent instantiation.
	 */
	private VirtualResourceMapper() {
		
	}

}
