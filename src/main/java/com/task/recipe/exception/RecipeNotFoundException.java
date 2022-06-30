package com.task.recipe.exception;


public class RecipeNotFoundException extends RuntimeException {

    private final String errorCode;

    public RecipeNotFoundException(String errorCode){
        super(errorCode);
        this.errorCode=errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "RecipeNotFoundException{" +
                "errorCode='" + errorCode + '\'' +
                '}';
    }
}
