package edu.wpi.FlashyFrogs.ORM;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "MedicineDelivery")
@PrimaryKeyJoinColumn(
    name = "service_request_id",
    foreignKey = @ForeignKey(name = "service_request_id_fk"))
public class MedicineDelivery extends ServiceRequest {
  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  private String patientID;

  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  private String reason;

  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  private String medicine;

  @Basic
  @Column(nullable = false)
  @Getter
  @Setter
  private double dosage;

  /** Creates a new medicine object with a generated id */
  public MedicineDelivery() {
    super.setStatus(Status.BLANK);
    super.setRequestType("Religion");
  }

  /**
   * Creates a new Religion with a generated id and the specified fields
   *
   * @param patientID the ID of the patient
   * @param theLocation the LocationName to use in the location field
   * @param reason the reason for the medicine request
   * @param medicine the medicine to be delivered
   * @param dosage the dosage fo the medicine
   * @param emp the User to use in the emp field
   * @param datePreference the Date to use in the dateOfIncident field
   * @param dateOfSubmission the Date to use in the dateOfSubmission field
   * @param urgency the Urgency to use in the urgency field
   */
  public MedicineDelivery(
      @NonNull String patientID,
      LocationName theLocation,
      @NonNull String reason,
      @NonNull String medicine,
      double dosage,
      @NonNull Urgency urgency,
      @NonNull Date datePreference,
      @NonNull Date dateOfSubmission,
      HospitalUser emp) {
    this.patientID = patientID;
    super.setLocation(theLocation);
    super.setEmp(emp);
    super.setDate(datePreference);
    super.setDateOfSubmission(dateOfSubmission);
    super.setStatus(Status.BLANK);
    super.setUrgency(urgency);
    super.setRequestType("MedicineDelivery");
    this.reason = reason;
    this.medicine = medicine;
    this.dosage = dosage;
  }

  public MedicineDelivery(
      String text,
      LocationName value,
      String text1,
      String text2,
      String text3,
      Urgency value1,
      Date date,
      Date from,
      HospitalUser currentUser) {
    super();
  }
}
