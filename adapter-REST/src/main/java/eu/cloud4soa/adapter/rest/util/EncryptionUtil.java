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


package eu.cloud4soa.adapter.rest.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import eu.cloud4soa.adapter.rest.auth.AuthenticationPropertyExclusionProvider;
import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.request.Request;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class EncryptionUtil {

	public static final String CIPHER = "SHA1";
	public static final int SEEDSIZE = 48;

	/**
	 * Creates an hash of all alphabetic ordered properties of the given request
	 * by passing by the credentials
	 * 
	 * @param request
	 *            the request plus the inquired hash
	 * @param credentials
	 *            the credentials to use to authenticate
	 * @return request the verified request
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static <T> Request<T> encipher(Request<T> request, Credentials credentials) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Field[] declaredFields = ClassUtil.getAllDeclaredFields(request.getClass(), AuthenticationPropertyExclusionProvider.EXCLUSIONS);
		
		Arrays.sort(declaredFields, 0, declaredFields.length, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});

		StringBuffer buffer = new StringBuffer();
		for (Field field : declaredFields) {
			String value = ClassUtil.getValueOf(field.getName(), request, request.getClass(), String.class);
			if (value != null) {
				buffer.append(value);
			}
		}
		buffer.insert(0, new String(credentials.getApiKey()));
		buffer.append(new String(credentials.getSecretKey()));

		request.setHash(toSHA1(buffer.toString()));
		request.setApiKey(credentials.getApiKey());
		return request;
	}

	/**
	 * Has to generate the key pair.
	 * 
	 * <strong>Note:</strong> this is only proof of concept.<br>
	 * The accounting & billing module has to generate the real credentials.
	 * 
	 * @return Credentials the credentials to use to access the adapter.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static Credentials generateCredentials() throws NoSuchAlgorithmException {
		CustomerCredentials toUse = new CustomerCredentials();

		toUse.setApiKey(convertToHex(randomSeed()));
		toUse.setSecretKey(convertToHex(randomSeed()));

		return toUse;
	}
	
	/**
	 * Generates random byte array
	 * 
	 * @return bytes random byte array
	 */
	private static byte[] randomSeed(){
		Random r = new Random();
		byte[] seed = new byte[SEEDSIZE];
		r.nextBytes(seed);
		return seed;
	}
	
	/**
	 * self-explanatory
	 * 
	 * @param unencrypted String
	 * @return encrypted string / hashed
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String toSHA1(String unencrypted) throws NoSuchAlgorithmException, UnsupportedEncodingException{ 
		MessageDigest md = MessageDigest.getInstance(CIPHER);
	    byte[] sha1hash = new byte[40];
	    md.update(unencrypted.getBytes("UTF-8"), 0, unencrypted.length());
	    sha1hash = md.digest();
	    return convertToHex(sha1hash);
	}
	
	/**
	 * Converts byte array to hex string
	 * 
	 * @param data byte array to encrypt
	 * @return hex string
	 */
	private static String convertToHex(byte[] data) { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
}
