package ca.bcit.termproject.numbergame;

import javafx.scene.control.Alert.AlertType;

/**
 * A custom alert for displaying warning messages to the user.
 * Extends PopupAlert and uses Alert.AlertType.WARNING for non-critical warning messages.
 * <p>
 * This class provides an easy-to-use alert dialog that can be used to show
 * warning messages to the user within the NumberGame. It extends the
 * PopupAlert class, ensuring that the alert inherits the common behavior
 * from PopupAlert while specifically setting the alert type to
 * {@link AlertType#WARNING} for non-critical warning messages.
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
     * This constructor validates the provided title and passes it along
     * with the alert type to the superclass constructor. The alert type
     * is set to {@link AlertType#WARNING} to indicate the non-critical
     * nature of the warning message being displayed.
     *
     * @param title the title of the warning alert dialog
     */
    public WarningAlert(final String title)
    {
        // Validate the title to ensure it's a valid, non-empty string
        PopupAlert.validateTitle(title);

        // Call the superclass constructor with the specific alert type and title
        super(AlertType.WARNING, title);
    }
}
