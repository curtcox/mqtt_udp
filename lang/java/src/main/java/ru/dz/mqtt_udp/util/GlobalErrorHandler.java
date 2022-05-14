package ru.dz.mqtt_udp.util;

import java.util.function.BiConsumer;


/**
 * Singleton for MQTT/UDP error callback.
 */
public final class GlobalErrorHandler {

	private static volatile GlobalErrorHandler instance;
	
	private BiConsumer<ErrorType, String> handler = null;

	/**
	 * Singleton for MQTT/UDP error callback.
	 * @return handler instance
	 */
	public static GlobalErrorHandler get() {
		GlobalErrorHandler localInstance = instance;
		if (localInstance == null) {
			synchronized (GlobalErrorHandler.class) {
				localInstance = instance;
				if (localInstance == null) {
					instance = localInstance = new GlobalErrorHandler();
				}
			}
		}
		return localInstance;
	}


	public static BiConsumer<ErrorType, String> getHandler()
	{
		return get().handler;
	}

	public static void setHandler(BiConsumer<ErrorType, String> h)
	{
		get().handler = h;
	}

	
	public static void handleError(ErrorType type, Throwable th)
	{
		handleError(type, th.toString());
	}
	
	public static void handleError(ErrorType type, String description) {
		BiConsumer<ErrorType, String> h = get().handler;
		if( h != null ) {
			h.accept(type, description);
			return;
		}
		
		// TODO logger
		//System.out.println(String.format( "MQTT/UDP %s error: %s", type.toString(), description ));
		System.err.println(String.format( "MQTT/UDP %s error: %s", type.toString(), description ));
	}

}
