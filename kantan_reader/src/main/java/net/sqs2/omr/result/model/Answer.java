package net.sqs2.omr.result.model;

import java.io.Serializable;

public abstract class Answer implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public Answer(){}
    
    public abstract int size();
}
