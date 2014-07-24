package com.app.injectees;

import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by korena on 7/25/14.
 */
public class Injectable implements Serializable {

    final static org.slf4j.Logger logger = LoggerFactory.getLogger(Injectable.class);

    private String string = "I am not initialized yet.";

    public Injectable(){
        this.string = "I am initialized";
    }

    public void method(){
        logger.info("\nThe injectable object says:"+string+"\n");
    }

}
