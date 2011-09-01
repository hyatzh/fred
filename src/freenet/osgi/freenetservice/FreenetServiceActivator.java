package freenet.osgi.freenetservice;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

import freenet.log.Logger;
import freenet.log.LoggerHook;
import freenet.log.OSGiLoggerHook;
import freenet.log.Logger.LogLevel;
import freenet.node.NodeStarter;

public class FreenetServiceActivator implements BundleActivator {

	private static BundleContext context;

	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Hallo, Freenet...");

		context = bundleContext;
		ServiceReference sr = context.getServiceReference(LogService.class.getName());
		LogService ls = null;
		if (sr != null) {
			ls = (LogService) context.getService(sr);
		}

		String threshold = context.getProperty("foe.log.threshold").toUpperCase();
		String detailedthreshold = context.getProperty("foe.log.detailedthreshold");

		LogLevel level = LogLevel.valueOf(threshold);
		LoggerHook hook;
		hook = new OSGiLoggerHook(ls,
				"(c, t, p): m", "MMM dd, yyyy HH:mm:ss:SSS", level);
		hook.setDetailedThresholds(detailedthreshold);

		Logger.globalAddHook(hook);

		// for compatiblity with old code also
		// init the old logger
		LoggerWrapper.migrationHack(hook);

		Logger.error(this, "Hello, Error");
		Logger.normal(this, "Hello, Normal");
		Logger.warning(this, "Hello, Warning");
		Logger.debug(this, "Hello, Debug");

		System.out.println("Welcome, Freenet!");

		NodeStarter.start_osgi(new String[] {});
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("bye, freenet");
		NodeStarter.stop_osgi(0);
		context = null;
	}

	public static void registerService(String clazz, Object service, Dictionary props) {
		context.registerService(clazz, service, props);
	}

}