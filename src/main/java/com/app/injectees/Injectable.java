package com.app.injectees;


import javax.enterprise.context.SessionScoped;


/**
 * Created by korena on 7/25/14.
 */
@SessionScoped
public interface Injectable {
    public String method(String message);

}
