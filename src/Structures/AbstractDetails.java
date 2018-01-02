public abstract class AbstractDetails {
    int fromLineNumber;

    abstract Object getValueByName(Keys keyname);
    abstract void performPatternValidation();

    private boolean hasError;
    public boolean isValid() { //if it has an error then this object/line CANNOT BE CONSIDERED!
        return !hasError;
    }

    final void handleError(String errorDescription, ErrorType errorType) {
        ErrorManager.generateError(errorDescription, errorType, ErrorKind.Parsing);
        hasError = true;
    }


}
