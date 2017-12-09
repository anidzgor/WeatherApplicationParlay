/*
 * **************************************************************************
 * *                                                                        *
 * * Ericsson hereby grants to the user a royalty-free, irrevocable,        *
 * * worldwide, nonexclusive, paid-up license to copy, display, perform,    *
 * * prepare and have prepared derivative works based upon the source code  *
 * * in this sample application, and distribute the sample source code and  *
 * * derivative works thereof and to grant others the foregoing rights.     *
 * *                                                                        *
 * * ERICSSON DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE,        *
 * * INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS.       *
 * * IN NO EVENT SHALL ERICSSON BE LIABLE FOR ANY SPECIAL, INDIRECT OR      *
 * * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS    *
 * * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE  *
 * * OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE *
 * * OR PERFORMANCE OF THIS SOFTWARE.                                       *
 * *                                                                        *
 * **************************************************************************
 */

package com.ericsson.nrgsdk.examples.tools.configuration;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.ImageIcon;

/*******************************************************************************
 * A specialization of {@link Properties}that has the capability of loading
 * property files recursively. If a line consists of the pragma
 * <code>"#include &lt;filename&gt;"</code>, the file denoted
 * <code>by &lt;filename&gt;</code> is processed recursively.
 * <code>&lt;filename&gt;</code> should be relative to the file containing the
 * pragma.
 */
public class NestedProperties extends Properties {

	private static final String INCLUDEPRAGMA = "#include";
	boolean runOnce = false;

	/**
	 * Recursively loads a properties file
	 * 
	 * @deprecated
	 * @see NestedProperties#load(Object) instead
	 * @param aFileName
	 *            The name of the file containing (nested) properties
	 * @throws IOException
	 *             when source file is not found
	 */
	public void load(String aFileName) throws IOException {
		load(new File(aFileName), new StringBuffer());
	}

	/**
	 * Recursively loads a properties file
	 * 
	 * @param aMemberOfTheApplicationPackage
	 *            the object who's package will be parsed to determine the
	 *            application name. NOTE! This will not work if object is of type <code>java.lang.String</code>
	 * A typical call of this method looks like: <code>myConfig.load(this);</code> This will assume that the
	 * config file name is the same as the package name and located under ./resources
	 * 
	 * @throws IOException
	 *             when source file is not found
	 */
	public void load(Object aMemberOfTheApplicationPackage) throws IOException {
		load(new File(getConfigFilePath(aMemberOfTheApplicationPackage)),
				new StringBuffer());
	}

	private void load(File aRoot, StringBuffer flatProperties)
			throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(aRoot));
		load(aRoot.getParent(), reader, flatProperties);
		reader.close();
		InputStream flatStream = new ByteArrayInputStream(flatProperties
				.toString().getBytes());
		super.load(flatStream);
		flatStream.close();
	}

	private void load(String aRoot, BufferedReader f, StringBuffer aDestination)
			throws IOException {
		String s;
		while ((s = f.readLine()) != null) 
		{
			s = s.trim();
			if (s.startsWith(INCLUDEPRAGMA)) 
			{
				String myFileName = s.substring(INCLUDEPRAGMA.length(),s.length()).trim();
				File orb = new File(aRoot+myFileName);
				if(!orb.exists())
				{
				    orb = orb.getParentFile().getParentFile();
				}
				//System.out.println("orb" + orb);
				File subFile = new File(orb.getAbsolutePath()+File.separator+myFileName);
				load(subFile, aDestination);
			} 
			else 
			{
				aDestination.append(s);
				aDestination.append("\n");
			}
		}
	}

	/**
	 * @return true if (and only if) at least one property name exists that
	 *         starts with a given prefix.
	 * 
	 * @param aPrefix
	 *            Prefix used in the search criterion
	 */
	public boolean prefixExists(String aPrefix) {
		aPrefix = aPrefix.toLowerCase();
		for (Enumeration i = propertyNames(); i.hasMoreElements();) {
			String key = (String) i.nextElement();
			if (key.startsWith(aPrefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Puts a value pair in the configuration
	 * 
	 * @param key
	 *            the name of the value pair
	 * @param value
	 *            the content of the name
	 * @return the previous value of the key or null if none
	 */
	public Object put(Object key, Object value) {
		if (key instanceof String) {
			key = ((String) key).toLowerCase();
		}
		return super.put(key, value);
	}

	/**
	 * @param key
	 *            the name of the property
	 * @return the value associated with the key
	 */
	public String getProperty(String key) {
		return super.getProperty(key.toLowerCase());
	}

	/**
	 * @param anObject
	 *            a member of the package who's package name will be parsed.
	 *            NOTE! if sent object is of type <code>String</code> this
	 *            will be interpreted as the file path itself.
	 * @return the default path to the .ini file that will be used as
	 *         configuration based on the package name. A config file for a package 
	 * named myexample will be presumed a name as myexample.ini in the /resources directory.
	 */
	private String getConfigFilePath(Object anObject) {
		String path = "";
		String realPath="";
		if (anObject instanceof String) 
		{
			path = (String) anObject;
		} 
		else 
		{
			String anApplicationName = anObject.getClass().getPackage()
					.getName();
			anApplicationName = anApplicationName.substring(anApplicationName
					.lastIndexOf(".") + 1);
			File tempFile = new File("checker");
			realPath = tempFile.getAbsolutePath();
			path = tempFile.getAbsolutePath().substring(0,
					tempFile.getAbsolutePath().lastIndexOf(File.separator))
					+ File.separator + "resources" + File.separator + anApplicationName + ".ini";
			tempFile.delete();
		}
		//System.out.println("Trying to find: " + path);
		File configPath = new File(path);
		if (!configPath.exists()) {
			if(!runOnce)
			{
			    //System.err.print("Cannot find: " + path + " Trying to resolve... ");
			    System.err.println("Cannot find: " + path + " Trying to recover... ");
			}
		    String iniFileName = path.substring(path
					.lastIndexOf(File.separator) + 1, path.length());
			String newPath = null;
			while (configPath.getParentFile() != null
					&& configPath.getParentFile().exists() && newPath == null) 
			{
				configPath = configPath.getParentFile();
				//System.out.println("Searching folder: " +configPath.getName()); 
				newPath = resolveFileInDir(configPath, iniFileName);
			}
			if(!runOnce && newPath == null)
			{
			    runOnce = true;
			    //System.err.print("\nMissing path completly. Deepsearching... ");
			    if(realPath!="")
			    {
			        //System.out.println("real:" +realPath);
			        newPath = getConfigFilePath(realPath.substring(0,realPath.lastIndexOf(File.separator)) + File.separator + iniFileName);
			    }
			    else
			    {
			        String pathPrefix = configPath.getAbsolutePath().substring(0,configPath.getAbsolutePath().indexOf(File.separator+"resources"));
			        //System.out.println("fixed:" +pathPrefix+File.separator+iniFileName);
			        newPath = getConfigFilePath(pathPrefix+File.separator+iniFileName);
			    }
			}
			if (newPath != null) {
				//System.out.println("found. Now using: " + newPath);
				path = newPath;
			}
			//else
			//{
			    //System.err.println("failed");
			    //runOnce = false;
			//}
			//runOnce = false;
		}
		runOnce = false;
		return path;
	}
	
	/**
	 * Method to find if a specific file exists in a directory.
	 * 
	 * @param aDirName
	 *            the directory to evaluate if the file is in
	 * @param fileName
	 *            the file to look for
	 * @return the absolute path of the found file or <code>null</code> if
	 *         it's not found.
	 */
	private String resolveFileInDir(File aDirName, String fileName) {
		File[] files = aDirName.listFiles();
		for (int i = 0; (i < files.length); i++) {
			//System.err.println("\t\t" +files[i].getName());
			if (files[i].getName().equalsIgnoreCase(fileName)) {
				//System.err.println("found");
				return files[i].getAbsolutePath();
			}
		}
		String result = null;
		for (int i = 0; (i < files.length); i++) {
			if (files[i].isDirectory()) {
				if ((result = resolveFileInDir(files[i], fileName)) != null) {
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * @param anObject
	 *            the package who's package name will be parsed.
	 * @return the name of the object's package with captial letter
	 */
	public String getApplicationNameFromPackage(Object anObject) {
		String anApplicationName = anObject.getClass().getPackage().getName();
		anApplicationName = anApplicationName.substring(anApplicationName
				.lastIndexOf(".") + 1);
		return anApplicationName.substring(0, 1).toUpperCase()
				+ anApplicationName.substring(1, anApplicationName.length());
	}

	/**
	 * Searches for images under /resources/images/<imageFilename>
	 * 
	 * @param anImageFileName the name of the image resource (e.g. myImage.gif)
	 * @return the image resource
	 */
	public ImageIcon getImage(String anImageFileName) {
		File buff = new File("buffer");
		String path = buff.getAbsolutePath().substring(0,buff.getAbsolutePath().lastIndexOf(File.separator));
		path = getConfigFilePath(path+File.separator+"resources"+File.separator+"images"+File.separator+anImageFileName);
		buff.delete();
		return new ImageIcon(path);
	}
}
