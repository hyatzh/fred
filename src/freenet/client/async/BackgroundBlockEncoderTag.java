package freenet.client.async;

// WARNING: THIS CLASS IS STORED IN DB4O -- THINK TWICE BEFORE ADD/REMOVE/RENAME FIELDS
class BackgroundBlockEncoderTag {
	final Encodeable inserter;
	final long nodeDBHandle;
	/** For implementing FIFO ordering */
	final long addedTime;
	/** For implementing priority ordering */
	final short priority;

	/**
	 * zero arg c'tor for db4o on jamvm
	 */
	@SuppressWarnings("unused")
	private BackgroundBlockEncoderTag() {
		priority = 0;
		nodeDBHandle = 0;
		inserter = null;
		addedTime = 0;
	}

	BackgroundBlockEncoderTag(Encodeable inserter, short prio, ClientContext context) {
		this.inserter = inserter;
		this.nodeDBHandle = context.nodeDBHandle;
		this.addedTime = System.currentTimeMillis();
		this.priority = prio;
	}
}
