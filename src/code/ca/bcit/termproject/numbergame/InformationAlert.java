package ca.bcit.termproject.numbergame;

import javafx.scene.control.Alert;

/**
 * A custom alert for displaying informational messages to the user.
 * Extends PopupAlert and uses Alert.AlertType.INFORMATION for non-critical messages.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public class InformationAlert
        extends PopupAlert
{
    /**
     * Constructs an InformationAlert with the specified title.
     *
     * @param title the title of the alert dialog
     */
    public InformationAlert(final String title)
    {
        PopupAlert.validateTitle(title);
        
        super(Alert.AlertType.INFORMATION, title);
    }
}