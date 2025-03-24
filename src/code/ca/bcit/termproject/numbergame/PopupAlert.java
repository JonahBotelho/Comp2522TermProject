package ca.bcit.termproject.numbergame;

import javafx.scene.control.Alert;

/**
 * A base class for creating custom alert dialogs.
 * Extends Alert to simplify the creation of popup alerts with a title.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public class PopupAlert extends Alert
{
    /**
     * Constructs a PopupAlert with the specified alert type and title.
     *
     * @param alertType the type of alert (e.g., INFORMATION, WARNING)
     * @param title the title of the alert dialog
     */
    public PopupAlert(final AlertType alertType, final String title)
    {
        super(alertType);
        super.setTitle(title);
    }
}