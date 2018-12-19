package de.demmtop.context;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EjbContext
{
	public static Context getEjbContext() throws NamingException
	{
		Properties props = new Properties();
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
		props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		return new InitialContext(props);
	}
}
