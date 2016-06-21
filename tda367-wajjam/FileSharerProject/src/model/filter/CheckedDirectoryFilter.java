package model.filter;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.PathChecker;

import java.io.File;
import java.io.FileFilter;


public class CheckedDirectoryFilter extends DirectoryFilter{

	private PathChecker checker;
	
	public CheckedDirectoryFilter(PathChecker c){
		checker = c;
	}
	
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
		return super.accept(file) && !checker.isInRemove(file);
	}
}