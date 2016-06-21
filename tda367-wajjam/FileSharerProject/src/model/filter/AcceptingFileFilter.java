package model.filter;

import java.io.File;

public class AcceptingFileFilter implements java.io.FileFilter{

	@Override
	public boolean accept(File file) {
		return file.isFile() && !file.isHidden();
	}
}
