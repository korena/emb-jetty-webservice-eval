package com.app.injectedIn;

import com.app.injectees.Injectable;

import javax.inject.Inject;

/**
 * Created by korena on 7/25/14.
 */
public class RecieveInjection {

    @Inject
    Injectable injectable;

    @Inject
    public RecieveInjection() {
    }

    public String useInjected(){

       return injectable.method();
    }

}
