package model;

import java.util.Observable;
import java.util.Observer;

import android.scrabble.GameBoardActivity;
import android.util.Log;

import controller.NetworkController;

import model.data.UserData;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;

public class GameSettingsModel extends Observable implements IModel, Observer{
	
	private NetworkController cc;
	
	public GameSettingsModel(){
		cc = NetworkController.getInstance("");
		cc.addObserver(this);
	}
	
	/*
	 * Sends a request to the server to get the list of people online
	 */
	public void getPeopleOnline(){
		SendObject object = new SendObject(SendableAction.PLAYERS_ONLINE, null);
		cc.send(object);
	}
	
	/*
	 * Sends a request to the server to get all available dictionaries
	 */
	public void getDictionaries(){
		SendObject object = new SendObject(SendableAction.GET_DICTIONARIES, null);
		cc.send(object);
	}
	
	public void sendInviteRequest(String opponent){
		SendObject object = new SendObject(SendableAction.INVITE_GAME, new String[]{UserData.getInstance().getUsername(), opponent});
		cc.send(object);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (((ResponseObject) data).getAction() == SendableAction.GET_DICTIONARIES
				|| ((ResponseObject) data).getAction() == SendableAction.PLAYERS_ONLINE) {
			setChanged();
			notifyObservers((ResponseObject)data);
		}
	}
}