/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    /**
     * Recursively inserts all files in a directory into a zipstream.
     *
     * @param directory
     *            the source directory
     * @param base
     *            optional parent directory that should serve as the entry root.
     *            Only path info after this base will be included as part of the
     *            entry name. By default, the directory parameter serves as
     *            root.
     * @param dirPrefix
     *            optional directory prefix to prepend onto each entry name.
     * @param zos
     *            the zip stream to add the files to.
     * @throws java.io.IOException
     */
    public static final void addDirectoryToZip(File directory, File base,
            String dirPrefix, ZipOutputStream zos) throws IOException {
        if (base == null)
            base = directory;
        if (dirPrefix == null)
            dirPrefix = "";

        // add an entry for the directory itself
        if (!base.equals(directory)) {
            String dirEntryPath = dirPrefix
                    + directory.getPath()
                            .substring(base.getPath().length() + 1).replace(
                                    '\\', '/');
            ZipEntry dirEntry = new ZipEntry(
                    dirEntryPath.endsWith("/") ? dirEntryPath : dirEntryPath
                            + "/");
            zos.putNextEntry(dirEntry);
        }

        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            // if (!files[i].isHidden()) {
            if (files[i].isDirectory()) {
                addDirectoryToZip(files[i], base, dirPrefix, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(dirPrefix
                        + files[i].getPath().substring(
                                base.getPath().length() + 1).replace('\\', '/'));
                entry.setTime(files[i].lastModified());
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
            // }
        }
    }

    public static void unzipFile(InputStream fis, ZipEntryHandler zipHandler,
            boolean closeStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            zipHandler.unzip(entry, new NoCloseInputStream(zis));
        }
        if (closeStream)
            zis.close();
    }

    public interface ZipEntryHandler {
        public void unzip(ZipEntry entry, InputStream zis)
                throws IOException;
    }

    private static int BUFFER = 2048;

    public static File unzipEntryToFolder(ZipEntry entry, InputStream zis, File destFolder) throws IOException {
        BufferedOutputStream dest;
        if (entry.isDirectory()) {
            File destFile = new File(destFolder, entry.getName());
            destFile.mkdirs();
            return destFile;
        } else {
            int count;
            byte data[] = new byte[BUFFER];
            // write the files to the disk
            File destFile = new File(destFolder, entry.getName());
            File parentFolder = destFile.getParentFile();
            if (!parentFolder.exists())
                parentFolder.mkdirs();
            FileOutputStream fos = new FileOutputStream(destFile);
            dest = new BufferedOutputStream(fos, BUFFER);
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();

            return destFile;
        }
    }
}

class NoCloseInputStream extends InputStream
{
	private InputStream in;
	NoCloseInputStream(InputStream in)
	{
		this.in = in;
	}
	public int available() throws IOException {
		return in.available();
	}
	public void close() throws IOException {
	}
	public boolean equals(Object obj) {
		return in.equals(obj);
	}
	public int hashCode() {
		return in.hashCode();
	}
	public void mark(int arg0) {
		in.mark(arg0);
	}
	public boolean markSupported() {
		return in.markSupported();
	}
	public int read() throws IOException {
		return in.read();
	}
	public int read(byte[] arg0, int arg1, int arg2) throws IOException {
		return in.read(arg0, arg1, arg2);
	}
	public int read(byte[] arg0) throws IOException {
		return in.read(arg0);
	}
	public void reset() throws IOException {
		in.reset();
	}
	public long skip(long arg0) throws IOException {
		return in.skip(arg0);
	}
	public String toString() {
		return in.toString();
	}


}
