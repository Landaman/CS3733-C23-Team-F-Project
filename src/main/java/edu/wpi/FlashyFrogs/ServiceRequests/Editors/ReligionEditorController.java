package edu.wpi.FlashyFrogs.ServiceRequests.Editors;

import static edu.wpi.FlashyFrogs.DBConnection.CONNECTION;

import edu.wpi.FlashyFrogs.GeneratedExclusion;
import edu.wpi.FlashyFrogs.ORM.*;
import edu.wpi.FlashyFrogs.ServiceRequests.ServiceRequestController;
import edu.wpi.FlashyFrogs.Sound;
import edu.wpi.FlashyFrogs.controllers.IController;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.persistence.RollbackException;
import java.io.IOException;
import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javafx.animation.FillTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;
import org.hibernate.Session;
import org.hibernate.Transaction;

@GeneratedExclusion
public class ReligionEditorController extends ServiceRequestController implements IController {

  @FXML Pane errtoast;
  @FXML Rectangle errcheck2;
  @FXML Rectangle errcheck1;
  @FXML Rectangle check2;
  @FXML Rectangle check1;
  @FXML Pane toast;

  @FXML MFXButton clear;
  @FXML MFXButton submit;
  @FXML Label errorMessage;
  @FXML SearchableComboBox<HospitalUser> assignedBox;
  @FXML SearchableComboBox<ServiceRequest.Status> statusBox;
  @FXML TextField patient;
  @FXML SearchableComboBox<LocationName> locationofPatient;
  @FXML TextField religion;
  @FXML TextField requestDescription;
  @FXML SearchableComboBox<ServiceRequest.Urgency> urgency;
  @FXML DatePicker serviceDate;

  boolean hDone = false;
  private Connection connection = null;
  private Religion relReq = new Religion();
  PopOver popOver;

  public void initialize() {

    Session session = CONNECTION.getSessionFactory().openSession();
    List<LocationName> locations =
        session.createQuery("FROM LocationName", LocationName.class).getResultList();

    locations.sort(Comparator.comparing(LocationName::getShortName));

    List<HospitalUser> users =
        session.createQuery("FROM HospitalUser", HospitalUser.class).getResultList();

    users.sort(Comparator.comparing(HospitalUser::getFirstName));

    locationofPatient.setItems(FXCollections.observableArrayList(locations));
    assignedBox.setItems(FXCollections.observableArrayList(users));
    statusBox.setItems(FXCollections.observableArrayList(ServiceRequest.Status.values()));
    urgency.setItems(FXCollections.observableArrayList(ServiceRequest.Urgency.values()));
    session.close();

    assignedBox.setButtonCell(
        new ListCell<HospitalUser>() {
          @Override
          protected void updateItem(HospitalUser item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Assigned User");
            } else {
              setText(item.toString());
            }
          }
        });

    statusBox.setButtonCell(
        new ListCell<ServiceRequest.Status>() {
          @Override
          protected void updateItem(ServiceRequest.Status item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Status");
            } else {
              setText(item.toString());
            }
          }
        });

    locationofPatient.setButtonCell(
        new ListCell<LocationName>() {
          @Override
          protected void updateItem(LocationName item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Location of Patient");
            } else {
              setText(item.toString());
            }
          }
        });

    urgency.setButtonCell(
        new ListCell<ServiceRequest.Urgency>() {
          @Override
          protected void updateItem(ServiceRequest.Urgency item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Urgency");
            } else {
              setText(item.toString());
            }
          }
        });
  }

  public void updateFields() {
    patient.setText(relReq.getPatientID());
    locationofPatient.setValue(relReq.getLocation());
    assignedBox.setValue(relReq.getAssignedEmp());
    urgency.setValue(relReq.getUrgency());
    serviceDate.setValue(
        Instant.ofEpochMilli(relReq.getDate().getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate());
    requestDescription.setText(relReq.getDescription());
    statusBox.setValue(relReq.getStatus());
    religion.setText(relReq.getReligion());
    if (relReq.getAssignedEmp() != null) {
      assignedBox.setValue(relReq.getAssignedEmp());
    }
  }

  @Override
  public void setPopOver(PopOver popOver) {
    this.popOver = popOver;
  }

  public void handleSubmit(ActionEvent actionEvent) throws IOException {
    Session session = CONNECTION.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();

    try {
      // check
      if (locationofPatient.getValue().toString().equals("")
          || patient.getText().equals("")
          || religion.getText().equals("")
          || serviceDate.getValue().toString().equals("")
          || requestDescription.getText().equals("")) {
        throw new NullPointerException();
      }

      Date dateOfRequest =
          Date.from(serviceDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

      relReq.setPatientID(patient.getText());
      relReq.setLocation(locationofPatient.getValue());
      relReq.setAssignedEmp(assignedBox.getValue());
      relReq.setDate(dateOfRequest);
      relReq.setStatus(statusBox.getValue());
      relReq.setReligion(religion.getText());
      relReq.setDescription(requestDescription.getText());
      relReq.setUrgency(urgency.getValue());

      try {
        session.merge(relReq);
        transaction.commit();
        session.close();
        handleClear(actionEvent);
        toastAnimation();
        Sound.SUBMITTED.play();
      } catch (RollbackException exception) {
        session.clear();
        errortoastAnimation();
        session.close();
        Sound.ERROR.play();
      }
    } catch (ArrayIndexOutOfBoundsException | NullPointerException exception) {
      session.clear();
      errortoastAnimation();
      session.close();
      Sound.ERROR.play();
    }
  }

  @Override
  public void setRequest(ServiceRequest request) {
    relReq = (Religion) request;
  }

  public void handleClear(ActionEvent actionEvent) throws IOException {
    locationofPatient.valueProperty().set(null);
    urgency.valueProperty().set(null);
    serviceDate.valueProperty().set(null);
    religion.setText("");
    patient.setText("");
    requestDescription.setText("");
    assignedBox.valueProperty().set(null);
    statusBox.valueProperty().set(null);
  }

  public void handleDelete(ActionEvent event) {
    Session session = CONNECTION.getSessionFactory().openSession();

    session.beginTransaction();
    session
        .createMutationQuery("DELETE FROM Religion WHERE id=:ID")
        .setParameter("ID", relReq.getId())
        .executeUpdate();
    session.getTransaction().commit();
    session.close();

    popOver.hide();
  }

  @Override
  protected void handleBack(ActionEvent event) throws IOException {}

  public void help() {
    if (!hDone) {

      hDone = true;
    } else if (hDone) {

      hDone = false;
    }
  }

  public void errortoastAnimation() {
    TranslateTransition translate1 = new TranslateTransition(Duration.seconds(0.5), errtoast);
    translate1.setByX(-280);
    translate1.setAutoReverse(true);
    errcheck1.setFill(Color.web("#012D5A"));
    errcheck2.setFill(Color.web("#012D5A"));
    // Create FillTransitions to fill the second and third rectangles in sequence
    FillTransition fill2 =
        new FillTransition(
            Duration.seconds(0.1), errcheck1, Color.web("#012D5A"), Color.web("#B6000B"));
    FillTransition fill3 =
        new FillTransition(
            Duration.seconds(0.1), errcheck2, Color.web("#012D5A"), Color.web("#B6000B"));
    SequentialTransition fillSequence = new SequentialTransition(fill2, fill3);

    // Create a TranslateTransition to move the first rectangle back to its original position
    TranslateTransition translateBack1 = new TranslateTransition(Duration.seconds(0.5), errtoast);
    translateBack1.setDelay(Duration.seconds(0.5));
    translateBack1.setByX(280.0);

    // Play the animations in sequence
    SequentialTransition sequence =
        new SequentialTransition(translate1, fillSequence, translateBack1);
    sequence.setCycleCount(1);
    sequence.setAutoReverse(false);
    sequence.jumpTo(Duration.ZERO);
    sequence.playFromStart();
    sequence.setOnFinished(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {

            submit.setDisable(false);
          }
        });
  }

  public void toastAnimation() {
    // Create a TranslateTransition to move the first rectangle to the left
    TranslateTransition translate1 = new TranslateTransition(Duration.seconds(0.5), toast);
    translate1.setByX(-280.0);
    translate1.setAutoReverse(true);
    check1.setFill(Color.web("#012D5A"));
    check2.setFill(Color.web("#012D5A"));
    // Create FillTransitions to fill the second and third rectangles in sequence
    FillTransition fill2 =
        new FillTransition(
            Duration.seconds(0.1), check1, Color.web("#012D5A"), Color.web("#F6BD38"));
    FillTransition fill3 =
        new FillTransition(
            Duration.seconds(0.1), check2, Color.web("#012D5A"), Color.web("#F6BD38"));
    SequentialTransition fillSequence = new SequentialTransition(fill2, fill3);

    // Create a TranslateTransition to move the first rectangle back to its original position
    TranslateTransition translateBack1 = new TranslateTransition(Duration.seconds(0.5), toast);
    translateBack1.setDelay(Duration.seconds(0.5));
    translateBack1.setByX(280.0);

    // Play the animations in sequence
    SequentialTransition sequence =
        new SequentialTransition(translate1, fillSequence, translateBack1);
    sequence.setCycleCount(1);
    sequence.setAutoReverse(false);
    sequence.play();
    sequence.setOnFinished(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {

            popOver.hide();
          }
        });
  }

  @Override
  public void onClose() {}
}
