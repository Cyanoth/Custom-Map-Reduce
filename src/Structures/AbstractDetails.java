/**
 * An abstract class, which all Data Details objects inherit from and override.
 * This class has an error handler which all Data Details objects can call to generate an error.
 */
public abstract class AbstractDetails {
    int fromLineNumber; //Track which line number from the data file this object is from (For Error-Reporting)

    /**
     * Depending on the DataDetail object, returns a key applicable to it.
     * @param keyname Get the value of a Key (see Enum: Keys)
     * @return Value from the keyname.
     */
    abstract Object getValueByName(Keys keyname);

    /**
     *  Performs the validation on the object. If any errors occur it will call the handleError
     *  function to mark this object has having an error.
     */
    abstract void performPatternValidation(); //

    private boolean hasError; //Marks the data object as having an error (validation)

    /**
     * @return Boolean for if this object has a validation error or not.
     */
    public boolean isValid() {
        return !hasError;  //Note: If this object/line has an error then it SHOULD NOT BE CONSIDERED!
    }

    /**
     * Error Handler for all Data Details object to call the Error Manager. If a validation error occurs then this function will be called
     * @param errorDescription A string to describe what the error is.
     * @param errorType whether the error is a Warning (just skip this object) or Fatal (Abort Program)
     */
    final void handleError(String errorDescription, ErrorType errorType) {
        ErrorManager.generateError(errorDescription, errorType, ErrorKind.Parsing);
        hasError = true;
    }


}
