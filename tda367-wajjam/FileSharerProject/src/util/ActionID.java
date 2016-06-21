package util;

/**
 * This enum is used to associate a certain action to a certain event.
 * 
 * <p>
 * The enum can also save an ID-number for use when there's multiple instances
 * of the same performer class, that needs to communicate with a specific instance 
 * from a multiple of supplier objects. In such a case, the classes should statically
 * save how many instances of itself is running, and give new objects ID-numbers corresponding
 * to the saved number. 
 * 
 * <p>
 * For classes where only one instance are running at any time, or where multiple performers
 * are supplied by one object, there is already a default value that means the class need not
 * bother with the ID-number.
 * 
 * <p>
 * At the moment, the number of suppliers must be either one, or the same as the number of performers.
 * If not, special solutions are required.
 * 
 * @author Albin Bramstång
 */

public enum ActionID {
	
	ACQUIRE_FILE,
	ADD_SERVER,
	CHANGE_PORT,
	CHANGE_USERNAME,
	CHAT_RECEIVED,
	CONNECT,
	DELETE_DOWNLOAD,
	DISCONNECT,
	DOWNLOAD_FOLDER,
	DOWNLOAD_NODE,
	FILE_ACQUIRED,
	FILE_RECEIVED,
	FILE_SELECTED,
	IS_CHATVIEW,
	MODEL_INITIATOR_FINISHED,
	NEW_PERFOMER_INSTANTIATED,
	NEW_SUPPLIER_INSTANTIATED,
	OPEN_FOLDER,
	PERFORMER_DELETED,
	RECEIVE_FILE,
	RECEIVE_TREE,
	REMOVE,
	SEND_CHAT,
	SEND_FILE,
	SEND_TREE,
	SERVER_UPDATED,
	SHOW_SETTINGS_WINDOW,
	SHARE,
	SHOW_SERVER_INPUT_WINDOW,
	START_DOWNLOAD,
	STATUS_UPDATED,
	STOP_DOWNLOAD,
	SUPPLIER_DELETED,	
	TREE_RECEIVED,
	UPDATE_DOWNLOAD_SPEED,
	UPDATE_SELECTED_SERVER_NAME,
	UPDATE_UPLOAD_SPEED,
	VIEW_CHAT,
	VIEW_SHARED_CONTENT;
}
