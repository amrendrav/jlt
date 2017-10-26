package com.getabby.ap.rest;

import java.io.Serializable;

import com.getabby.ap.exception.Transmittable;
import com.getabby.ap.util.TransmittalMessageLevel;

public class ApiResponse<T> implements Serializable {

	private static final long serialVersionUID = 5011111620028706233L;

	private String message;

	private String uniqueId;

	private boolean explicitDismissal;

	private TransmittalMessageLevel notificationType;

	private boolean userStale;

	private T payload;

	/**
	 * Generates a ApiReponse object with blank defaults for all members
	 */
	public ApiResponse() {
	}

	/**
	 * Generates a ApiReponse object with blank defaults for all members
	 * 
	 * @param payload
	 *            - Payload to be send back to client UI
	 */
	public ApiResponse(T payload) {
		this.payload = payload;
	}

	/**
	 * Generates a ApiReponse object with blank defaults for all members
	 * 
	 * @param transmittableObject
	 */
	public ApiResponse(Transmittable transmittableObject) {
		this.message = transmittableObject.getMessage();
		this.explicitDismissal = transmittableObject.getExcplicitDismissal();
		this.notificationType = transmittableObject.getNotificationType();
		this.uniqueId = "";
	}

	public ApiResponse(String message, boolean explicitDismissal, TransmittalMessageLevel notificationType) {
		this.message = message;
		this.explicitDismissal = explicitDismissal;
		this.notificationType = notificationType;
	}

	public ApiResponse(String message, boolean explicitDismissal, TransmittalMessageLevel notificationType, T payload) {
		this.message = message;
		this.explicitDismissal = explicitDismissal;
		this.notificationType = notificationType;
		this.payload = payload;
	}

	/**
	 * Generates a ApiReponse object with blank defaults for all members
	 * 
	 * @param message
	 *            - Message to be sent to client UI
	 * @param uniqueId
	 *            - Unique ID to be sent to the client UI
	 * @param explicitDismissal
	 *            - True requires the client user to explicitly dismiss the
	 *            notification, false otherwise
	 * @param notificationType
	 *            - Type of notification the client UI receives
	 */

	public ApiResponse(String message, String uniqueId, boolean explicitDismissal, TransmittalMessageLevel notificationType) {
		this.message = message;
		this.uniqueId = uniqueId;
		this.explicitDismissal = explicitDismissal;
		this.notificationType = notificationType;
	}

	/**
	 * Generates a ApiReponse object with blank defaults for all members
	 * 
	 * @param message
	 *            - Message to be sent to client UI
	 * @param uniqueId
	 *            - Unique ID to be sent to the client UI
	 * @param explicitDismissal
	 *            - True requires the client user to explicitly dismiss the
	 *            notification, false otherwise
	 * @param notificationType
	 *            - Type of notification the client UI receives
	 * @param payload
	 *            - Payload to be send back to client UI
	 */

	public ApiResponse(String message, String uniqueId, boolean explicitDismissal, TransmittalMessageLevel notificationType, T payload) {
		this(message, uniqueId, explicitDismissal, notificationType);
		this.payload = payload;
	}

	//----g/s---
	/**
	 * Returns the message
	 * 
	 * @return : Returns the message contained in the ApiRepsonse
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message of the ApiResponse
	 * 
	 * @param message
	 *            : Message to store in the ApiResponse
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the Unique Id of the ApiReponse
	 * 
	 * @return : Unique ID of the ApiResponse
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets the UniqueID of the ApiResponse
	 * 
	 * @param uniqueId
	 *            : returns the UniqueID of the ApiResponse
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Returns the explicitDismissal flag of the ApiResponse
	 * 
	 * @return : True implies that the end user must dismiss the client UI
	 */
	public boolean isExplicitDismissal() {
		return explicitDismissal;
	}

	/**
	 * Sets the explicitDismissal flag of the ApiResponse
	 * 
	 * @param explicitDismissal
	 *            - True requires the end user client to explicitly dismiss the
	 *            notification
	 */
	public void setExplicitDismissal(boolean explicitDismissal) {
		this.explicitDismissal = explicitDismissal;
	}

	/**
	 * Returns the notification type of the ApiResponse
	 * 
	 * @return : NotificationType of the ApiResponse
	 */

	public TransmittalMessageLevel getNotificationType() {
		return notificationType;
	}

	/**
	 * sets the notification type of the ApiResponse
	 * 
	 * @param notificationType
	 *            : Notification type of the ApiResponse as defined by
	 *            NotificatioType
	 */

	public void setNotificationType(TransmittalMessageLevel notificationType) {
		this.notificationType = notificationType;
	}

	/**
	 * Returns the payload of type T, and defined by the instantiation of this
	 * ApiResponse
	 * 
	 * @return : The payload of type T
	 */
	public T getPayload() {
		return payload;
	}

	/**
	 * Sets the payload of type T, as defined by the instantiation of this
	 * ApiResponse
	 * 
	 * @param payload
	 *            : Payload to set in the ApiResponse
	 */
	public void setPayload(T payload) {
		this.payload = payload;
	}

	public boolean isUserStale() {
		return userStale;
	}

	public void setUserStale(boolean userStale) {
		this.userStale = userStale;
	}

}
