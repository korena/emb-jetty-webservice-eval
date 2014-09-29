/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.injectees;

import java.io.Serializable;
import org.slf4j.LoggerFactory;

/**
 *
 * @author korena
 */
public class InjectableImpl implements Serializable, Injectable{

    static final org.slf4j.Logger logger = LoggerFactory.getLogger(InjectableImpl.class);
    String string = "saying something";
    
    @Override
    public String method(String message){
        logger.debug("\nThe injectable object says:"+string+"\n");
        return message != null? message: "I am injected in an object that is then injected into the servlet that sent you this text.";
    }
    
}
