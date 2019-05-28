package config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import services.UsersResources;

@ApplicationPath("gym")
public class ApplicationConfig extends Application{

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> recursos = new HashSet<Class<?>>();
		recursos.add(UsersResources.class);
		return recursos;
	}

	
	
}
