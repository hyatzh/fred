/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.clients.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import freenet.support.io.Closer;

class WebSocketConnection implements WebSocketSender {

	private final WebSocketHandler wsh;
	private final InputStream in;
	private final OutputStream out;

	WebSocketConnection(WebSocketHandler webSocketHandler, InputStream inputStream, OutputStream outputStream) {
		wsh = webSocketHandler;
		in = inputStream;
		out = outputStream;
	}

	void service() throws IOException {
		System.out.println("Begin crochet service: "+this);
		wsh.onBeginService(this);

		int i;

		StringBuffer sb = null;
		boolean record = false;
		while ((i = in.read()) > -1) {
			if (i == 0) {
				sb = new StringBuffer();
				record = true;
			} else if (i == 255) {
				record = false;
				wsh.onMessage(sb.toString());
			} else {
				if (record)
					sb.append((char) i);
			}
		}
		System.out.println("End crochet service: "+this);
	}

	void close(boolean notify) {
		Closer.close(in);
		Closer.close(out);
		if (notify)
			wsh.onClose();
	}

	public void close() {
		close(false);
	}

	public void sendMessage(String message) throws IOException {
		out.write(0x00);
		out.write(message.getBytes("UTF-8"));
		out.write(0xFF);
		out.flush();
	}

}
