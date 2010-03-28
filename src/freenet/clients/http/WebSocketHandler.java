/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.clients.http;

public interface WebSocketHandler {

	/**
	 * notify if the connection is closed.
	 * Do not close the connection again in this handler.
	 */
	void onClose();

	void onMessage(String string);

	void onBeginService(WebSocketSender sender);

}
