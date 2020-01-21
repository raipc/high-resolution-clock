package com.axibase.clock;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

class HighResolutionClockLoader {
	private static final Logger logger = Logger.getLogger(HighResolutionClockLoader.class.getName());

	static boolean loadLibrary() {
		String libraryName = resolveLibraryName();
		File destination = null;
		try (InputStream lib = Thread.currentThread().getContextClassLoader().getResourceAsStream("native/" + libraryName)) {
			if (lib == null) {
				logger.log(Level.SEVERE, "Could not load {0} library", libraryName);
			} else {
				File tmpDir = new File(System.getProperty("java.io.tmpdir"));
				destination = new File(tmpDir, libraryName);
				copyToFile(lib, destination);
				System.load(destination.getAbsolutePath());
				return true;
			}
		} catch (Throwable e) {
			logger.log(Level.SEVERE, "Error while loading clock_gettime native library: {0}", e.getMessage());
		} finally {
			deleteQuietly(destination);
		}
		return false;
	}

	private static String resolveLibraryName() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("mac") || osName.contains("darwin")) {
			return "libclock_gettime.jnilib";
		}
		return "libclock_gettime.so";
	}

	private static void copyToFile(InputStream lib, File file) throws IOException {
		final FileOutputStream output = new FileOutputStream(file);
		try {
			copy(lib, output);
		} finally {
			closeIgnoreErrors(output);
			closeIgnoreErrors(lib);
		}
	}

	private static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		int n;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

	private static void closeIgnoreErrors(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	private static void deleteQuietly(File file) {
		if (file != null) {
			try {
				file.delete();
			} catch (Exception e) {
				// ignore
			}
		}
	}
}
