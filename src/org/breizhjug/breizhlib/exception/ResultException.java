package org.breizhjug.breizhlib.exception;


import org.breizhjug.breizhlib.remote.Result;

public class ResultException extends Exception{

    public Result result;

    public ResultException( Result result) {
        super(result.msg);
        this.result = result;
    }
}
