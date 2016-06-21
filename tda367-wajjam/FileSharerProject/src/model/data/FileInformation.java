package model.data;

import java.io.File;
import java.io.Serializable;

/**
 * Class representing information about a file.
 * The information is filename, size and mimetype of the file.
 * The data is produced locally so sending the file over a network won't change it,
 * which means that you can save data to display anywhere else.
 * 
 * @author Magnus
 */

public class FileInformation implements Serializable{

	private static final long serialVersionUID = 8298586758863529772L;
	private String name = null,
				   filename = null,
				   mimeType = null;
	private double filesize = 0.0;
	
	/**
	 * Creates a new FileInformation from the specific filename.
	 * 
	 * @param filename the name of the file you want to retrieve the data from.
	 */
	public FileInformation(String filename){
		this.filename = filename;
		
		File file = new File(filename);
		name = file.getName();
		filesize = file.length();
	}

	/**
	 * Gets the name of the file.
	 * 
	 * @return the name of the file.
	 */
	public String getFilename() {
		return filename;
	}
	
	public String getName(){
		return name;
	}

	/**
	 * Gets the mimetype of the file. This can be "image/png" for png images or "audio/mp3" for mp3-files.
	 *
	 * @return the mimetype of the file.
	 */
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mt){
		this.mimeType = mt;
	}
	
	/**
	 * Gets the size of the file in kB.
	 * 
	 * @return the size of the file in kB.
	 */
	public double getSize(){
		return filesize;
	}
}