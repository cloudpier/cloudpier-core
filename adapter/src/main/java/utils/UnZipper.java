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


/**
 * $Id: UnZipper.java 10 2009-11-26 13:46:20Z oneyour $
 *
 * UnZipper.java
 * This is an accompanying program for the article
 * <a href="http://www.1your.com/drupal/unziparchivedorcompressedfilesinJava
" title="http://www.1your.com/drupal/unziparchivedorcompressedfilesinJava
">http://www.1your.com/drupal/unziparchivedorcompressedfilesinJava
</a> *
 * Copyright (c) 2009 - 2010 <a href="http://www.1your.com" title="www.1your.com">www.1your.com</a>.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of <a href="http://www.1your.com" title="www.1your.com">www.1your.com</a> nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A Java program to unzip a ZIP archive.
 * The ZIP archive file name is got from the user
 * through the System.in input stream
 */
public class UnZipper
{
    /**
     * Private Constructor as this is a utility class with only public
     * static methods
     */
    private UnZipper()
    {}


    /**
     * Request and read input from the command line (System.in)
     *
     * @param inputMessage
     *      The message to be displayed as a request to the user
     *
     * @return
     *      The input from the command line
     *
     */
    public static String readCommandLineInput(String inputMessage)
    {
        System.out.println(inputMessage);
        Scanner scanner = new Scanner(System.in);

        String inputLine = scanner.nextLine();
        return inputLine;
    }

    /**
     * The core Business Logic method that extracts a ZIP file maintaining
     * the folder structure
     *
     * @param zipFileName
     *      The name of the ZIP file to be extracted
     *
     * @throws IOException
     *      Problems while extacting the ZIP file
     */
    public static void unzip(String zipFileName)  throws IOException
    {
        ZipFile zipFile = null;
        InputStream inputStream = null;

        File inputFile = new File(zipFileName);
        try
        {
             // Wrap the input file with a ZipFile to iterate through
             // its contents
             zipFile = new ZipFile(inputFile);
             Enumeration<? extends ZipEntry> oEnum = zipFile.entries();
             while(oEnum.hasMoreElements())
             {


                 ZipEntry zipEntry = oEnum.nextElement();
                 File file = new File(zipEntry.getName());

                 if(zipEntry.isDirectory())
                 {
                     // If the entry in the ZIP file is a directory
                     // then create the directory

                     file.mkdirs();


                 }
                 else
                 {
                     // If the entry in the ZIP file is a file
                     // then write the file in the appropriate
                     // directory location (as it is in the ZIP file)
                     inputStream = zipFile.getInputStream(zipEntry);

                     //make dir from war name
                    // File war_folder= new File("WARFOLDER");
                  //   war_folder.mkdir();

                     write(inputStream, file);
                 }


             }
        }
        catch (IOException ioException)
        {
            throw ioException;
        }
        finally
        {
            // Clean up the I/O
            try
            {
                if (zipFile != null)
                {
                    zipFile.close();
                }
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch(IOException problemsDuringClose)
            {
                System.out.println("Problems during cleaning up the I/O.");
            }
        }
    }

    /**
     * Writes to the supplied file with the contents read from the supplied input stream.
     *
     * @param inputStream
     *      The Source input stream from where the contents will be read to write to the file.
     *
     * @param fileToWrite
     *      The file to which the contents from the input stream will be written to.
     *
     * @throws IOException
     *      Any problems while reading from the input stream or writing to the file.
     */
    public static void write(InputStream inputStream, File fileToWrite) throws IOException
    {
            BufferedInputStream buffInputStream = new BufferedInputStream( inputStream );
            FileOutputStream fos = new FileOutputStream(fileToWrite );
            BufferedOutputStream bos = new BufferedOutputStream( fos );

            // write bytes
            int byteData;
            while( ( byteData = buffInputStream.read() ) != -1 )
            {
                 bos.write( (byte) byteData);
            }

            // close all the open streams
            bos.close();
            fos.close();
            buffInputStream.close();
    }

}
