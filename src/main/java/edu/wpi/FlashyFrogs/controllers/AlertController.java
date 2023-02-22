package edu.wpi.FlashyFrogs.controllers;

import edu.wpi.FlashyFrogs.ORM.Announcement;
import edu.wpi.FlashyFrogs.ORM.Department;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;
import org.w3c.dom.Text;

public class AlertController {
    @FXML private TextField summaryField;
    @FXML private TextField departmentField;
    @FXML private TextField severityField;
    @FXML private TextArea descriptionField;
    @FXML private TextField authorField;
    @FXML private TextField dateField;


    public void initialize() {
    }

    public void insertAnnouncement(Announcement announcement) {
        summaryField.setText(announcement.getAnnouncement());
//        for(int i = 0; i < announcement.getSeverity())
        dateField.setText(announcement.getCreationDate().toString());
        authorField.setText(announcement.getAuthor().getFirstName() + " " + announcement.getAuthor().getLastName());
        departmentField.setText(announcement.getDepartment().getLongName());
//        descriptionField.setText(announcement.getDescription());
    }
}
