/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.support.db4o;

import java.io.File;

import com.db4o.ObjectContainer;
import com.db4o.config.ObjectConstructor;

import freenet.support.Logger;

/**
 * Simple file translater for db4o.
 * Stores the Absolute path instead of the file object.
 * 
 * @author saces
 */
public class FileTranslator implements ObjectConstructor {

	private static volatile boolean logDEBUG;

	static {
		Logger.registerClass(FileTranslator.class);
	}

	public Object onInstantiate(ObjectContainer container, Object storedObject) {
		if (logDEBUG) Logger.debug(this, "onInstantiate for "+storedObject);
		return new File((String)storedObject);
	}

	public void onActivate(ObjectContainer container, Object applicationObject, Object storedObject) {
		if (logDEBUG) Logger.debug(this, "onActivate for "+applicationObject+" / "+storedObject);
	}

	public Object onStore(ObjectContainer container, Object applicationObject) {
		if (logDEBUG) Logger.debug(this, "onStore for "+applicationObject);
		return ((File)applicationObject).getAbsolutePath();
	}

	public Class storedClass() {
		return String.class;
	}

}
