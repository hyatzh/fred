/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.clients.http;

import java.io.IOException;
import java.net.URI;

import freenet.client.HighLevelSimpleClient;
import freenet.l10n.NodeL10n;
import freenet.support.HTMLNode;
import freenet.support.Logger;
import freenet.support.api.HTTPRequest;

/**
 * WebSocket Test Toadlet
 * 
 * A really simple websock app to test browser ability.
 * 
 */
public class WebSocketToadlet extends Toadlet implements WebSocketAcceptor {

	public WebSocketToadlet(HighLevelSimpleClient client) {
		super(client);
	}

	public void handleMethodGET(URI uri, HTTPRequest request, ToadletContext ctx)
	throws ToadletContextClosedException, IOException, RedirectException {
		String path = uri.getPath();
		String foundkey = null;
		String foundtarget = null;

		PageNode page = ctx.getPageMaker().getPageNode("Websocket test", true, ctx);
		HTMLNode pageNode = page.outer;
		HTMLNode contentNode = page.content;

		if (!ctx.getContainer().isFProxyJavascriptEnabled()) {
			// no java script? go away with your web 1.0 settings,
			// websockets is web 3.0.
			HTMLNode errorbox = ctx.getPageMaker().getInfobox("infobox-error", "WebSockets Error: Javascript is disabled", contentNode, "websocket-error", true);
			errorbox.addChild("#", "Websockets requires Javascript to function, but Javascript is disabled in your configuration.");
			errorbox.addChild("br");
			errorbox.addChild("#", "Please enable Javascript or stay 'Web 1.0'.");
			writeHTMLReply(ctx, 200, "OK", pageNode.generate());
			return;
		}

		HTMLNode statusbox = ctx.getPageMaker().getInfobox("infobox-information", "WebSocket status", contentNode, "websocket-status", true);
		statusbox.addChild("#", "Websockets: ");
		if (ctx.getContainer().isWebSocketEnabled())
			statusbox.addChild("#", "enabled.");
		else
			statusbox.addChild("#", "disabled.");

		HTMLNode headNode = page.headNode;
		HTMLNode scriptNode = headNode.addChild("script","//abc");
		scriptNode.addAttribute("type", "text/javascript");
		scriptNode.addAttribute("src", "/static/js/websockettest.js");

		HTMLNode testbox = ctx.getPageMaker().getInfobox("infobox-information", "WebSocket Test", contentNode, "websocket-test", true);
		testbox.addChild("a", "href", "javascript:WebSocketTest()", "Run WebSocket test");

		writeHTMLReply(ctx, 200, "OK", pageNode.generate());
	}

	@Override
	public String path() {
		return "/websocket/";
	}

	private static String l10n(String key) {
		return NodeL10n.getBase().getString("WebSocketToadlet." + key);
	}

	public static class WebSocketEcho implements WebSocketHandler {

		private final String _host;
		private final String _origin;
		private final String _protocol;
		private WebSocketSender webSocketSender;

		public WebSocketEcho(String host, String origin, String protocol) {
			_host = host;
			_origin = origin;
			_protocol = protocol;
		}

		public void onBeginService(WebSocketSender sender) {
			webSocketSender = sender;
		}

		public void onMessage(String message) {
			// a simple echo
			try {
				webSocketSender.sendMessage(message);
			} catch (IOException e) {
				Logger.error(this, "Unexpected error while WebSockets test:", e);
			}
			webSocketSender.close();
		}

		public void onClose() {
			Logger.error(this, "Connection got closed unexpectly while WebSockets test.");
		}

	}

	public WebSocketHandler acceptUpgrade(String host, String origin, String protocol) {
		// accept any and do a simple echo.
		return new WebSocketEcho(host, origin, protocol);
	}

}
