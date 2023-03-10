package edu.wpi.FlashyFrogs.Accounts;

import static edu.wpi.FlashyFrogs.DBConnection.CONNECTION;

import edu.wpi.FlashyFrogs.Fapp;
import edu.wpi.FlashyFrogs.GeneratedExclusion;
import edu.wpi.FlashyFrogs.ORM.Department;
import edu.wpi.FlashyFrogs.ORM.HospitalUser;
import edu.wpi.FlashyFrogs.ORM.UserLogin;
import edu.wpi.FlashyFrogs.controllers.IController;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;
import org.hibernate.Session;

@GeneratedExclusion
public class LoginAdministratorController implements IController {
  @FXML private Label errorMessage;
  @FXML private TableView<UserLogin> userLoginTable;
  @FXML private TableColumn<UserLogin, Number> idCol;

  @FXML private TableColumn<UserLogin, String> rfidCol;
  @FXML private TableColumn<UserLogin, String> userNameCol;
  @FXML private TableColumn<UserLogin, String> nameCol;
  @FXML private TableColumn<UserLogin, HospitalUser.EmployeeType> empTypeCol;
  @FXML private TableColumn<UserLogin, Department> deptCol;
  @FXML private Button addNewUser;

  @FXML Text h1;
  @FXML Text h2;
  boolean hDone = false;

  public void handleBack(ActionEvent actionEvent) throws IOException {
    Fapp.handleBack();
  }

  public void handleNewUser(ActionEvent actionEvent) throws IOException {
    FXMLLoader newLoad = new FXMLLoader(getClass().getResource("NewUser.fxml"));
    PopOver popOver = new PopOver(newLoad.load());
    NewUserController newUser = newLoad.getController();
    newUser.setPopOver(popOver);
    newUser.setLoginAdminController(this);
    popOver.detach();
    Node node = (Node) actionEvent.getSource();
    popOver.show(node.getScene().getWindow());
    addNewUser.setDisable(true);
    popOver
        .showingProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue) {
                addNewUser.setDisable(false);
              }
            });
  }

  public void initialize() throws Exception {
    errorMessage.setVisible(false);
    h1.setVisible(false);
    h2.setVisible(false);

    // Clear old table before init
    userLoginTable.getItems().clear();
    // set columns userlogin

    idCol.setCellValueFactory(
        data -> {
          HospitalUser user = data.getValue().getUser();
          return new SimpleLongProperty(user.getId());
        });
    rfidCol.setCellValueFactory(
        data -> {
          String rfid = data.getValue().getRFIDBadge();
          return new SimpleStringProperty(rfid != null ? "Yes" : "");
        });
    userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
    nameCol.setCellValueFactory(
        data -> {
          HospitalUser user = data.getValue().getUser();
          return new SimpleStringProperty(
              user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
        });
    empTypeCol.setCellValueFactory(
        data -> {
          HospitalUser user = data.getValue().getUser();
          return new SimpleObjectProperty<>(user.getEmployeeType());
        });
    deptCol.setCellValueFactory(
        data -> {
          HospitalUser user = data.getValue().getUser();
          return new SimpleObjectProperty<>(user.getDepartment());
        });

    // create logIn table
    // open session
    ObservableList<UserLogin> userLoginObservableList = null; // convert list to ObservableList
    Session ses = CONNECTION.getSessionFactory().openSession();
    try {
      List<UserLogin> userLoginObjects =
          ses.createQuery("SELECT s FROM UserLogin s", UserLogin.class)
              .getResultList(); // select everything from userLogin table and add to list
      userLoginObservableList = FXCollections.observableList(userLoginObjects);
      ses.close();
      userLoginTable
          .getItems()
          .addAll(userLoginObservableList); // add every item in observable list to moveTable
    } catch (Exception e) {
      ses.close();
      throw e;
    }
    userLoginTable.setOnMouseClicked(
        event -> {
          // Make sure the user clicked on a populated item
          if (userLoginTable.getSelectionModel().getSelectedItem() != null) {
            UserLogin selectedUserLogin = userLoginTable.getSelectionModel().getSelectedItem();
            FXMLLoader newLoad = new FXMLLoader(getClass().getResource("EditUser.fxml"));
            PopOver popOver = null;
            try {
              popOver = new PopOver(newLoad.load());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
            EditUserController editUser = newLoad.getController();
            editUser.setPopOver(popOver);
            editUser.setLoginAdminController(this);
            editUser.initialize(selectedUserLogin);
            popOver.detach();
            Node node = (Node) event.getSource();
            popOver.show(node.getScene().getWindow());
            addNewUser.setDisable(true);
            popOver
                .showingProperty()
                .addListener(
                    (observable, oldValue, newValue) -> {
                      if (!newValue) {
                        addNewUser.setDisable(false);
                      }
                    });
          }
          userLoginTable.getSelectionModel().clearSelection();
        });
  }

  public void onClose() {}

  @Override
  public void help() {
    if (!hDone) {
      h1.setVisible(true);
      h2.setVisible(true);
      hDone = true;
    } else if (hDone) {
      h1.setVisible(false);
      h2.setVisible(false);
      hDone = false;
    }
  }
}
