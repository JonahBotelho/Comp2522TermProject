package ca.bcit.termproject.numbergame;

/**
 * A custom alert for displaying warning messages to the user.
 * Extends PopupAlert and uses Alert.AlertType.WARNING for warning messages.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public class WarningAlert
        extends PopupAlert
{
    /**
     * Constructs a WarningAlert with the specified title.
     *
     * @param title the title of the warning alert dialog
     */
    public WarningAlert(final String title)
    {
        PopupAlert.validateTitle(title);
        
        super(AlertType.WARNING, title);
    }
}
