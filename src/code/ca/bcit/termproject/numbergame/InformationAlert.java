package ca.bcit.termproject.numbergame;

import javafx.scene.control.Alert;

/**
 * A custom alert for displaying informational messages to the user.
 * Extends PopupAlert and uses Alert.AlertType.INFORMATION for non-critical messages.
 *
 * This class provides an easy-to-use alert dialog that can be used to show
 * informational messages to the user within the NumberGame. It extends the
 * PopupAlert class, ensuring that the alert inherits the common behavior
 * from PopupAlert while specifically setting the alert type to
 * {@link Alert.AlertType#INFORMATION} for non-critical messages.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public class InformationAlert
        extends PopupAlert
{
    /**
     * Constructs an InformationAlert with the specified title.
     * <p>
     * This constructor validates the provided title and passes it along
     * with the alert type to the superclass constructor. The alert type
     * is set to {@link Alert.AlertType#INFORMATION} to indicate the non-critical
     * nature of the message being displayed.
     *
     * @param title the title of the alert dialog
     */
    public InformationAlert(final String title)
    {
        // Validate the title to ensure it's a valid, non-empty string
        PopupAlert.validateTitle(title);

        // Call the superclass constructor with the specific alert type and title
        super(Alert.AlertType.INFORMATION, title);
    }
}
