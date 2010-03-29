function WebSocketTest()
{
	if (!("WebSocket" in window))
	{
		alert("WebSocket not supported here!\r\n\r\nBrowser: " + navigator.appName + " " + navigator.appVersion + "\r\n\r\nTry a recent browser with HTML5/WebSockets support");
		return;
	}
	RealTest()
}

function RealTest() {
	// replace the leading 'http' with 'ws'
	var ws = new WebSocket("ws"+document.location.href.substr(4), "websocktest");

	ws.onopen = function(evt) { 
		ws.send("Hello Server!");
	};
	ws.onmessage = function(evt) {
		alert("Got a sock from server: " + evt.data);
	};
	ws.onclose = function(evt) {
		alert("Conn closed");
	};
	// spec says something about, but goggling for
	// 'websocket onerror' shows only the spec, no samples :(
	// ws.onerror = fiction(evt) {
	// alert(errmsg);
	// };

}
