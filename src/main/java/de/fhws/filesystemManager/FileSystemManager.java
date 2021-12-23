package de.fhws.filesystemManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileSystemManager {

	/**
	 * Private constructor so the class can't be instantiated.
	 *
	 */
	private FileSystemManager() {
	}

	/**
	 * Writes a Java-Object to a file with a generated filename.
	 * 
	 * @param <T>        is the type of the object
	 * 
	 * @param fname      the name of the file that gets used to generate the full
	 *                   filename
	 * @param dir        the directory of the file.
	 * @param counting   determines whether the filename should be appended with a
	 *                   counter if files with this name already exist in the
	 *                   directory. If counting == false, no numbers get appended to
	 *                   the filename. (e.g. filename<b>(1)</b>.txt)
	 * @param fileEnding is the ending that gets appended if no ending is given
	 *                   in @param fname (e.g. ".txt"). If a file ending is already
	 *                   given in @param fname this attribute gets ignored.
	 * @param override   True means the generated File location gets overwritten if
	 *                   its filename already existed. False means the object gets
	 *                   appended to the generated File location. @param counting ==
	 *                   true prevents a collision of a existing file and the
	 *                   generated file location. So @param counting == true result
	 *                   in @param override having no impact on the method.
	 * 
	 * @return true if the object got successfully written to the file or false if
	 *         an Exception occurred.
	 * 
	 */
	public static <T extends Serializable> boolean writeObjectToAGeneratedFileLocation(T object, String fname, String dir, boolean counting,
			String fileEnding, boolean override) {
		createSubDirIfNotExist(dir);
		String generatedFileName = generateFullFilename(fname, dir+"/", counting, fileEnding);
		return writeObjectToFile(object, generatedFileName, override);
	}

	/**
	 * Writes a Java-Object to a given file. If the given file can't be found it
	 * will be created.
	 * 
	 * @param <T>      is the type of the object
	 * @param object   is the written element
	 * @param fname    is the filename (including path) of the destination
	 * @param override determines whether the object should overwrite the file or
	 *                 append it
	 * @return true if the object got successfully written to the file or false if
	 *         an Exception occurred.
	 */
	public static <T  extends Serializable> boolean writeObjectToFile(T object, String fname, boolean override) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fname, !override))) {
			oos.writeObject(object);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get the first Object from a File
	 * 
	 * @param fname is the filename (including path) of the destination
	 * 
	 * @return the read object or null if an Exception occurred.
	 */
	public static Object getFirstObjectFromFile(String fname) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fname));) {
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generates a Filename
	 * 
	 * @param fname      is the name of the file
	 * @param counting   determines whether the filename should be appended with a
	 *                   counter if files with this name already exist in the
	 *                   directory. If counting == false, no numbers get appended to
	 *                   the filename.
	 * @param dir        is the name of the directory for the "full filename"
	 * @param fileEnding is the ending that gets appended if no ending is given
	 *                   in @param fname (e.g. ".txt"). If a file ending is already
	 *                   given in @param fname this attribute gets ignored.
	 * @return the generated filename
	 */
	private static String generateFullFilename(String fname, String dir, boolean counting, String fileEnding) {
		String newfname = dir + fname;
		File tempFile = new File(newfname);
		int counter = 0;
		String[] splitted = splitUpFilename(fname);

		boolean endingExists = true;
		if (splitted[1] == null)
			endingExists = false;

		do {
			if (endingExists)
				newfname = dir + splitted[0] + getCounterFileEnding(counter) + splitted[1];
			else
				newfname = dir + fname + getCounterFileEnding(counter) + fileEnding;

			tempFile = new File(newfname);
			counter++;
		} while (tempFile.exists() && !counting);
		return newfname;
	}

	/**
	 * creates every subdirectory of given directory recursively if not exists
	 * @param dir
	 */
	private static void createSubDirIfNotExist(String dir) {
		if(dir.indexOf("/") == -1) {
			createDirIfNotExist(dir);
			return;
		}
		String subDirChain = "";
		for(String subDir : dir.split("/")) {
			subDirChain += subDir +"/";
			createDirIfNotExist(subDirChain);
		}
		
	}
	
	
	/**
	 * Searches the directory <b> dir </b> and if not found creates it.
	 * 
	 * @param dir is the path of the directory
	 * @return void
	 */

	private static void createDirIfNotExist(String dir) {
		Path tempDirectory = new File(dir).toPath();
		if (!Files.exists(tempDirectory)) {
			tempDirectory.toFile().mkdir();
		}
	}

	/**
	 * Creates a String that can be appended to a filename
	 * 
	 * @param counter shows how many files already existed with fitting names
	 * @return a String that can be appended to the filename to make the filenames
	 *         unique
	 */
	private static String getCounterFileEnding(int counter) {
		return (counter == 0 ? "" : "(" + counter + ")");
	}

	/**
	 * splits up a Filename to its name and ending
	 * 
	 * @param fname is the name of the file
	 * @return returns an Array of Strings where the first element is the filename
	 *         and the second is the file ending. if no file ending exists, the
	 *         second element is null
	 */
	private static String[] splitUpFilename(String fname) {
		int index = fname.lastIndexOf(".");
		if (index == -1)
			return new String[] { fname, null };
		return new String[] { fname.substring(0, index), fname.substring(index, fname.length()) };
	}


}
