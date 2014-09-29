package com.app.binders;



import com.app.injectees.Injectable;
import com.app.injectees.InjectableImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;


public class InternalsBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(InjectableImpl.class).to(Injectable.class);
    }
}