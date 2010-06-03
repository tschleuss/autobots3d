package org.furb.cg.util;

import java.io.InputStream;

public class ResourceUtil {

	public ResourceUtil() {
		super();
	}
	
	/**
	 * Procura a imagem de todas as formas possiveis, inclusive
	 * se o programa for executado de dentro de um JAR e a imagem
	 * estiver dentro do JAR tambem.
	 * @param resourceName
	 * @param caller
	 * @return
	 */
	public static InputStream getResource(final String resourceName, final Class<?> caller) 
	{
		final String resource;
		if (resourceName.startsWith("/")) 
		{
			resource = resourceName.substring(1);
		} 
		else {
			
			final Package callerPackage = caller.getPackage();
			
			if (callerPackage != null) 
			{
				resource = callerPackage.getName().replace('.', '/') + '/'+ resourceName;
			}
			else {
				resource = resourceName;
			}
		}
		
		final ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();
		
		if (threadClassLoader != null) 
		{
			final InputStream is = threadClassLoader.getResourceAsStream(resource);
			
			if (is != null) {
				return is;
			}
		}

		final ClassLoader classLoader = caller.getClassLoader();
		
		if (classLoader != null) 
		{
			final InputStream is = classLoader.getResourceAsStream(resource);
			
			if (is != null) {
				return is;
			}
		}

		return ClassLoader.getSystemResourceAsStream(resource);
	}
}
