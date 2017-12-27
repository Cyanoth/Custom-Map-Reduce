public abstract class AbstractDetails {
    abstract Object getValueByName(Keys keyname);
    abstract void performPatternValidation();

    private boolean hasError;
    public boolean isValid() { //if it has an error then this object CANNOT BE CONSIDERED!
        return !hasError;
    }

    final void handleError(String errorDescription, ErrorType errorType) {
        ErrorManager.generateError(errorDescription, errorType, ErrorKind.Parsing);
        hasError = true;
    }


}
