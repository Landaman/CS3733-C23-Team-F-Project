package edu.wpi.FlashyFrogs.controllers;

import edu.wpi.FlashyFrogs.ORM.LocationName;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.concurrent.atomic.AtomicReference;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import lombok.NonNull;
import org.hibernate.Session;

public class LocationNameInfoController {
  @FXML private MFXTextField locationNameField;
  @FXML private MFXComboBox<LocationName.LocationType> locationTypeField;
  @FXML private MFXTextField shortNameField;

  /**
   * Sets the location name and updates the associated
   *
   * @param locationName the location name to set everything to use
   */
  public void setLocationName(@NonNull LocationName locationName, @NonNull Session session) {
    // Create an atomic version of the location, so we can update it
    AtomicReference<LocationName> location = new AtomicReference<>(locationName);

    locationNameField.setText(location.get().getLongName()); // Set the long name

    // Add a listener to the long name field
    locationNameField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              // Create the new location (on update cascade)
              session
                  .createMutationQuery(
                      "UPDATE LocationName SET longName=:newValue "
                          + "WHERE longName=:originalLocation")
                  .setParameter("longName", newValue)
                  .setParameter("originalLocation", location.get().getLongName())
                  .executeUpdate();

              // Re-fetch the location
              location.set(
                  session
                      .createQuery("FROM LocationName WHERE longName=:newValue", LocationName.class)
                      .setParameter("newValue", newValue)
                      .uniqueResult());
            });

    // Set the items for the combobox
    locationTypeField.setItems(
        FXCollections.observableArrayList(LocationName.LocationType.values()));
    locationTypeField.setText(location.get().getLocationType().name()); // Set the location type

    // Add a listener to the location type field
    locationTypeField
        .valueProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              location.get().setLocationType(newValue);
              session.merge(location.get()); // Save the changes
            });

    // Set the short name text
    shortNameField.setText(location.get().getShortName());

    // Add a listener to the short name field
    shortNameField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              location.get().setShortName(newValue);
              session.merge(location.get()); // Save teh changes
            });
  }
}
