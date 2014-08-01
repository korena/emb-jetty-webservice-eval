package com.app.binding;

import com.app.rest.TheService;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author korena
 * @Comment this is done within the application, because jersey is initialized at the
 * web application's level, other containers (like glassfish) would transparently do it
 * at the server level, this is a wild guess.
 */
public class AppJerseyBinder extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TheService.class);
        return classes;
    }
}
