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

package com.cloudbees.upload;

import com.cloudbees.utils.ZipHelper;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 */
public class ArchiveUtils {

    public static int ENTRY_DELETED = 1;
    public static int ENTRY_UPDATED = 2;
    public static int ENTRY_ADDED = 3;
    private static final String META_INF = "META-INF";
    private static final String DELTA_FILE = "CB-DELTA.xml";

    public static File createDeltaWarFile(Map<String, Long> existingArchiveCheckSums, File warFile, String tmp) throws IOException {
        Map<String, Integer> deltas = getDeltas(warFile.getAbsolutePath(), existingArchiveCheckSums);

        String tmpDir = makeTmpDir(warFile, tmp);
        ZipFile zipFile = new ZipFile(warFile.getAbsolutePath());
        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = e.nextElement();
            Integer delta = deltas.get(entry.getName());
            if (delta != null && delta.intValue() != ArchiveUtils.ENTRY_DELETED) {
                // Copy the entry to tmp directory
                unArchiveZipEntry(tmpDir, zipFile, entry);
            }
        }

        // Add a file to the archive with the deltas
        File metaInfDir = new File(tmpDir + META_INF);
        metaInfDir.mkdirs();

        File deltaFile = new File(metaInfDir, DELTA_FILE);
        XStream xstream = new XStream();
        FileOutputStream fos = new FileOutputStream(deltaFile);
        xstream.toXML(deltas, fos);
        fos.close();

        // Archive the deltas
        String deltaDir = warFile.getParent() == null ? "." :  warFile.getParent();
        String deltaArchiveFile = deltaDir + "/DELTA-" + warFile.getName();
        archiveDirectory(tmpDir, deltaArchiveFile);

        deleteAll(new File(tmpDir));

        return new File(deltaArchiveFile);
    }


    public static Map<String, Integer> getDeltas(String archiveFile, Map<String, Long> oldCheckSums) throws IOException {
        Map<String, Integer> deltas =  new HashMap<String, Integer>();

        Map<String, Long> newCheckSums = getCheckSums(archiveFile);

        ConcurrentHashMap<String, Long> checkSumsTmp = new ConcurrentHashMap<String, Long>(oldCheckSums);
        for (Map.Entry<String, Long> entry : newCheckSums.entrySet()) {
            String key = entry.getKey();
            if (checkSumsTmp.get(key) == null) {
                deltas.put(key, ENTRY_ADDED);
            } else if (entry.getValue().longValue() != checkSumsTmp.get(key).longValue()) {
                deltas.put(key, ENTRY_UPDATED);
                checkSumsTmp.remove(key);
            } else {
                checkSumsTmp.remove(key);
            }
        }
        for (String key : checkSumsTmp.keySet()) {
            deltas.put(key, ENTRY_DELETED);
        }

        return deltas;
    }

    public static Map<String, Long> getCheckSums(String archiveFile) throws IOException {
        Map<String, Long> checkSums = new HashMap<String, Long>();

        ZipFile zipFile = new ZipFile(archiveFile);
        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = e.nextElement();
            checkSums.put(entry.getName(), entry.getCrc());
        }

        return checkSums;
    }

    private static void unArchiveZipEntry(String destinationDirectory, ZipFile zipfile, ZipEntry entry) throws IOException {
        File file = ZipHelper.unzipEntryToFolder(entry, zipfile.getInputStream(entry), new File(destinationDirectory));
        if (entry.getTime() > -1)
            file.setLastModified(entry.getTime());
    }


    private static void archiveDirectory(String directory, String archiveFile) throws IOException {
        File archive = new File(archiveFile);
        archive.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(archive);
        ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(fileOutputStream));
        ZipHelper.addDirectoryToZip(new File(directory), new File(directory), null, zipOutputStream);
        zipOutputStream.close();
    }

    private static String makeTmpDir(File file, String tmp) {
        if (tmp == null)
            tmp = ".";
        String fileName = file.getName();
        int idx = fileName.lastIndexOf('.');
        if (idx > -1) {
            fileName = fileName.substring(0,idx);
        }
        if (!tmp.endsWith(File.separator))
            tmp += File.separator;

        tmp = tmp + "tmp" + fileName + File.separator;
        File dir = new File(tmp);
        deleteAll(dir);
        dir.mkdirs();
        return tmp;
    }

    private static void deleteAll(File dir)
    {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for(File f : files)
                    {
                        if(f.isDirectory())
                            deleteAll(f);
                        else
                            f.delete();
                    }
                }
            }
            dir.delete();
        }
    }
}
