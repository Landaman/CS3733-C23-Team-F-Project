package edu.wpi.FlashyFrogs.ORM;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "userlogin")
public class UserLogin {

  @Id
  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
  @Column(nullable = false)
  @NonNull
  @Getter
  @Setter
  String userName;

  @Basic
  @Column(nullable = false)
  @NonNull
  @Getter
  String hash;

  public void setPassword(String newPassword) {
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    hash = argon2.hash(2, 15 * 1024, 1, newPassword.toCharArray());
  }

  /** Creates a new UserLogin with empty fields */
  public UserLogin() {}

  public UserLogin(@NonNull String theUserName, @NonNull String thePassword) {
    this.userName = theUserName;

    // Encrypts password with salt
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    hash = argon2.hash(2, 15 * 1024, 1, thePassword.toCharArray());
  }

  /**
   * Overrides the default equals method with one that compares primary keys
   *
   * @param obj the node object to compare to
   * @return boolean whether the primary keys are the same or not
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (this.getClass() != obj.getClass()) return false;
    UserLogin other = (UserLogin) obj;
    return this.userName.equals(other.getUserName());
  }

  /**
   * Overrides the default hashCode method with one that uses the id, xcoord, and ycoord of the node
   * object
   *
   * @return the new hashcode
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.userName, this.hash);
  }

  /**
   * Overrides the default toString method with one that returns the id of the Node object
   *
   * @return the id of the UserLogin object
   */
  @Override
  @NonNull
  public String toString() {
    return this.userName;
  }

  /**
   * @param potentialPassword given by user to be checked against actual password
   * @return true if the password matches the current password, false otherwise
   */
  public boolean checkPasswordEqual(String potentialPassword) {
    Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 32, 64);
    return argon2.verify(hash, potentialPassword.toCharArray());
  }
}