package demo;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import testutil.ConfigLoaderTestHelper;
import testutil.ImplLoader;

import java.io.File;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

public class DemoAppBase {
	
	static final String YOUR_GMAIL_ADDRESS = "yourname@gmail.com";
	
	// if you have 2-factor login turned on, you need to generate a once-per app password
	// https://security.google.com/settings/security/apppasswords
	private static final String YOUR_GMAIL_PASSWORD = "<your password>";
	
	static final Mailer mailerSMTP = buildMailer("smtp.gmail.com", 25, YOUR_GMAIL_ADDRESS, YOUR_GMAIL_PASSWORD, TransportStrategy.SMTP);
	static final Mailer mailerTLS = buildMailer("smtp.gmail.com", 587, YOUR_GMAIL_ADDRESS, YOUR_GMAIL_PASSWORD, TransportStrategy.SMTP_TLS);
	static final Mailer mailerSSL = buildMailer("smtp.gmail.com", 465, YOUR_GMAIL_ADDRESS, YOUR_GMAIL_PASSWORD, TransportStrategy.SMTPS);
	
	/**
	 * If you just want to see what email is being sent, just set this to true. It won't actually connect to an SMTP server then.
	 */
	private static final boolean LOGGING_MODE = false;
	
	static {
		// make Simple Java Mail ignore the properties file completely: that's there for the junit tests, not this demo.
		ConfigLoaderTestHelper.clearConfigProperties();
		
		//noinspection ConstantConditions
		if (YOUR_GMAIL_ADDRESS.equals("your_gmail_user@gmail.com")) {
			throw new AssertionError("For these demo's to work, please provide your Gnail credentials in DemoAppBase.java first (or change the SMTP config)");
		}
	}
	
	static String determineResourceFolder(@SuppressWarnings("SameParameterValue") final String module) {
		// the following is needed bacause this is a project with submodules
		if (new File("src/test/resources/log4j2.xml").exists()) {
			return "src";
		} else if (new File("modules/" + module + "/src/test/resources/log4j2.xml").exists()) {
			return "modules/" + module + "/src";
		} else {
			throw new AssertionError("Was unable to locate resources folder");
		}
	}
	
	@SuppressWarnings("SameParameterValue")
	private static Mailer buildMailer(String host, int port, String gMailAddress, String gMailPassword, TransportStrategy strategy) {
		return ImplLoader.loadMailerBuilder()
				.withSMTPServer(host, port, gMailAddress, gMailPassword)
				.withTransportStrategy(strategy)
				.withTransportModeLoggingOnly(LOGGING_MODE)
				.clearProxy()
				.buildMailer();
	}
	
	static byte[] produceThumbsUpImage() {
		String base64String = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAABeElEQVRYw2NgoAAYGxu3GxkZ7TY1NZVloDcA" +
				"Wq4MxH+B+D8Qv3FwcOCgtwM6oJaDMTAUXOhmuYqKCjvQ0pdoDrCnmwNMTEwakC0H4u8GBgYC9Ap6DSD+iewAoIPm0ctyLqBlp9F" +
				"8/x+YE4zpYT8T0LL16JYD8U26+B7oyz4sloPwenpYno3DchCeROsUbwa05A8eB3wB4kqgIxOAuArIng7EW4H4EhC/B+JXQLwDaI4" +
				"ryZaDSjeg5mt4LCcFXyIn1fdSyXJQVt1OtMWGhoai0OD8T0W8GohZifE1PxD/o7LlsPLiFNAKRrwOABWptLAcqc6QGDAHQEOAYaA" +
				"c8BNotsJAOgAUAosG1AFA/AtUoY3YEFhKMAvS2AE7iC1+WaG1H6gY3gzE36hUFJ8mqzbU1dUVBBqQBzTgIDQRkWo5qCZdpaenJ0Z" +
				"x1aytrc0DDB0foIG1oAYKqC0IZK8D4n1AfA6IzwPxXpCFoGoZVEUDaRGGUTAKRgEeAAA2eGJC+ETCiAAAAABJRU5ErkJggg==";
		return parseBase64Binary(base64String);
	}
}