package config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("gym")
public class ApplicationConfig extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> recursos = new HashSet<Class<?>>();
		recursos.add(services.UsersResources.class);
		return recursos;
	}

	
	
}
