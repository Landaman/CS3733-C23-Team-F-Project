package edu.wpi.FlashyFrogs.ORM;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "EquipmentTransport")
@PrimaryKeyJoinColumn(
    name = "service_request_id",
    foreignKey = @ForeignKey(name = "service_request_id_fk"))
public class EquipmentTransport extends ServiceRequest {
  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  private String equipment;

  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  private String description;

  @Getter
  @Setter
  @JoinColumn(
      name = "moveTo",
      foreignKey =
          @ForeignKey(
              name = "location_name1_fk",
              foreignKeyDefinition =
                  "FOREIGN KEY (moveTo) REFERENCES locationname(longName) "
                      + "ON UPDATE CASCADE ON DELETE SET NULL"))
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  @ManyToOne
  private LocationName moveTo; // Location the request is needed for (source)

  /** Creates a new AudioVisual with a generated id */
  public EquipmentTransport() {
    super.setStatus(Status.BLANK);
    super.setRequestType("EquipmentTransport");
  }

  /**
   * Creates a new AudioVisual with a generated id and the specified fields
   *
   * @param emp the User to use in the emp field
   * @param requestDate the Date to use in the requestDate field
   * @param dateOfSubmission the Date to use in the dateOfSubmission field
   * @param urgency the Urgency to use in the urgency field
   * @param location the LocationName to use in the location field
   * @param equipment the String to use in the equipment field
   * @param description the String to use in the description field
   * @param moveTo the LocationName to use in the moveTo field
   */
  public EquipmentTransport(
      HospitalUser emp,
      @NonNull Date requestDate,
      @NonNull Date dateOfSubmission,
      @NonNull Urgency urgency,
      @NonNull LocationName location,
      @NonNull LocationName moveTo,
      @NonNull String description,
      @NonNull String equipment) {
    super.setEmp(emp);
    super.setDate(requestDate);
    super.setDateOfSubmission(dateOfSubmission);
    super.setStatus(Status.BLANK);
    super.setUrgency(urgency);
    super.setRequestType("EquipmentTransport");
    super.setLocation(location);
    this.moveTo = moveTo;
    this.description = description;
    this.equipment = equipment;
  }
}