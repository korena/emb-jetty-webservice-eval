package com.app.injectedIn;

import com.app.injectees.Injectable;

import javax.inject.Inject;

/**
 * Created by korena on 7/25/14.
 */
public class RecieveInjection {

    @Inject
    Injectable injectable;

    public void useInjected(){

        injectable.method();
    }

}
