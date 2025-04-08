package ca.bcit.termproject.numbergame;

import javafx.scene.control.Alert;

/**
 * A base class for creating custom alert dialogs.
 * Extends {@link Alert} to simplify the creation of popup alerts with a title.
 *
 * The PopupAlert class serves as a base for creating custom alert dialogs with
 * a specific type and title. By extending the {@link Alert} class, it allows
 * easy customization of alerts for different purposes (e.g., informational,
 * warning). The class includes utility methods to validate the alert type and
 * title, ensuring they are properly provided before creating the alert dialog.
 *
 * This class is abstract, meaning it cannot be instantiated directly but
 * should be extended to create specific alert types such as {@link InformationAlert}
 * or {@link WarningAlert}.
 *
 * @author Jonah Botelho
 * @version 1.0s
 */
public abstract class PopupAlert
        extends Alert
{
    /**
     * Constructs a PopupAlert with the specified alert type and title.
     *
     * This constructor validates the provided alert type and title before
     * creating the alert dialog. It passes the alert type to the superclass
     * constructor and sets the alert's title. The alert type defines the
     * nature of the message (e.g., INFORMATION, WARNING), and the title is
     * displayed at the top of the dialog box.
     *
     * @param alertType the type of alert (e.g., {@link Alert.AlertType#INFORMATION},
     *                  {@link Alert.AlertType#WARNING})
     * @param title the title of the alert dialog
     */
    public PopupAlert(final AlertType alertType, final String title)
    {
        // Validate the alert type and title before proceeding
        validateAlertType(alertType);
        validateTitle(title);

        // Pass the alert type to the superclass constructor
        super(alertType);

        // Set the title of the alert dialog
        super.setTitle(title);
    }

    /**
     * Validates the provided alert type to ensure it is not {@code null}.
     *
     * @param alert the alert type to validate
     * @throws NullPointerException if the alert type is {@code null}
     */
    public static void validateAlertType(final AlertType alert)
    {
        if (alert == null)
        {
            throw new NullPointerException("alert cannot be null");
        }
    }

    /**
     * Validates the provided title to ensure it is neither {@code null} nor empty.
     *
     * @param title the title to validate
     * @throws IllegalArgumentException if the title is {@code null} or empty
     */
    public static void validateTitle(final String title)
    {
        if (title == null || title.isEmpty())
        {
            throw new IllegalArgumentException("title cannot be null or empty");
        }
    }
}
