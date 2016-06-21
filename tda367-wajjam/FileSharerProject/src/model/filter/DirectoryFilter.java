package model.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * FileFilter that sorts out the files, so that only visible directories are shown.
 * The filter is used to sort out files and hidden directories, leaving only
 * non-hidden directories left.
 *  
 * @author Magnus
 */
public class DirectoryFilter implements FileFilter {

	/**
	 * Evaluates if the file matches the filter-description and is accepted. 
	 * 
	 * @see FileFilter#accept(File)
	 * 
	 * @param file the file to evaluate.
	 * 
	 * @return true if the file matches the filter-description, otherwise false.
	 */
	public boolean accept(File file) {
		return file.isDirectory() && !file.isHidden();
	}
}