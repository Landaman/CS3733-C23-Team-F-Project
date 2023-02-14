package edu.wpi.FlashyFrogs.Accounts;

import static edu.wpi.FlashyFrogs.DBConnection.CONNECTION;

import edu.wpi.FlashyFrogs.GeneratedExclusion;
import edu.wpi.FlashyFrogs.ORM.User;
import edu.wpi.FlashyFrogs.ORM.UserLogin;
import edu.wpi.FlashyFrogs.controllers.IController;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.PopOver;
import org.hibernate.Session;
import org.hibernate.Transaction;

@GeneratedExclusion
public class NewUserController implements IController {

  private PopOver popOver;
  private LoginAdministratorController loginAdministratorController;
  @FXML private MFXTextField username;
  @FXML private MFXPasswordField pass1;
  @FXML private MFXPasswordField pass2;
  @FXML private Label errorMessage;

  public void setPopOver(PopOver thePopOver) {
    this.popOver = thePopOver;
  }

  public void setLoginAdminController(LoginAdministratorController adminController) {
    this.loginAdministratorController = adminController;
  }

  public void initialize() {}

  public void handleNewUser(ActionEvent actionEvent) throws IOException {
    if (username.getText().equals("") || pass1.getText().equals("") || pass2.getText().equals("")) {
      // One of the values is left null
      errorMessage.setText("Please fill out all fields!");
      errorMessage.setVisible(true);
    } else if (!pass1.getText().equals(pass2.getText())) {
      // Passwords do not match
      errorMessage.setText("Passwords do not match!");
      errorMessage.setVisible(true);
    } else {
      // Save Username and Password to db
      errorMessage.setVisible(false);
      User userFK =
          new User("test", "DELETE ME", "test", User.EmployeeType.ADMIN, null); // update department
      UserLogin newUser = new UserLogin(userFK, username.getText(), pass1.getText());
      Session ses = CONNECTION.getSessionFactory().openSession();
      Transaction transaction = ses.beginTransaction();
      try {
        ses.persist(userFK);
        ses.persist(newUser);
        transaction.commit();
        ses.close();
        loginAdministratorController.initialize();
        popOver.hide();
      } catch (Exception e) {
        errorMessage.setText("That username is already taken.");
        errorMessage.setVisible(true);
        transaction.rollback();
        ses.close();
      }
    }
  }

  public void onClose() {}
}
