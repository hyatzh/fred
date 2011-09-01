package freenet.osgi.freenetservice;

import freenet.support.Logger;
import freenet.support.Logger.LogLevel;
import freenet.support.LoggerHook;

public class LoggerWrapper {

	static class HookWrapper extends LoggerHook {
		final freenet.log.LoggerHook hook;
		HookWrapper(freenet.log.LoggerHook h) {
			super(fromNewApi(h.getThreshold()));
			hook = h;
		}
		@Override
		public void log(Object o, Class<?> source, String message, Throwable e,
				LogLevel priority) {
			hook.log(o, source, message, e, toNewApi(priority));
		}
	}

	static LogLevel fromNewApi(freenet.log.Logger.LogLevel level) {
		return LogLevel.fromOrdinal(level.ordinal());
	}

	static freenet.log.Logger.LogLevel toNewApi(LogLevel level) {
		return freenet.log.Logger.LogLevel.fromOrdinal(level.ordinal());
	}

	static void migrationHack(freenet.log.LoggerHook hook) {
		Logger.globalAddHook(new HookWrapper(hook));
	}

}
