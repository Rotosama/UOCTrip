/**
 * Module of the program (based on Java 9's modules)
 */
open module edu.uoc.trip {
    requires javafx.fxml;
    requires transitive javafx.controls;

    exports edu.uoc.trip.view.gui to javafx.controls, javafx.fxml;
}
