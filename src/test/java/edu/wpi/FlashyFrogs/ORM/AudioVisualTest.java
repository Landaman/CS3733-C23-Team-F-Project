package edu.wpi.FlashyFrogs.ORM;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.FlashyFrogs.DBConnection;
import java.util.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

// Creates iteration of Sanitation
public class AudioVisualTest {
  /** Sets up the data base before all tests run */
  @BeforeAll
  public static void setupDBConnection() {
    DBConnection.CONNECTION.connect(); // Connect
  }

  /** Tears down the database, meant to be used after all tests finish */
  @AfterAll
  public static void disconnectDBConnection() {
    DBConnection.CONNECTION.disconnect(); // Disconnect
  }

  /** Cleans up the user table. Runs after each test */
  @AfterEach
  public void teardownTable() {
    // If the prior test is open
    try {
      Session priorSession = DBConnection.CONNECTION.getSessionFactory().getCurrentSession();
      if (priorSession != null && priorSession.isOpen()) {

        // If the transaction is still active
        if (priorSession.getTransaction().isActive()) {
          priorSession.getTransaction().rollback(); // Roll it back
        }

        priorSession.close(); // Close it, so we can create new ones
      }
    } catch (HibernateException ignored) {
    }

    // Use a closure to manage the session to use
    try (Session connection = DBConnection.CONNECTION.getSessionFactory().openSession()) {
      Transaction cleanupTransaction = connection.beginTransaction(); // Begin a cleanup transaction
      connection.createMutationQuery("DELETE FROM AudioVisual").executeUpdate(); // Do the drop
      connection.createMutationQuery("DELETE FROM ServiceRequest").executeUpdate();
      connection.createMutationQuery("DELETE FROM LocationName").executeUpdate();
      connection.createMutationQuery("DELETE FROM HospitalUser").executeUpdate();
      connection.createMutationQuery("DELETE FROM Department").executeUpdate();
      cleanupTransaction.commit(); // Commit the cleanup
    }
  }

  private final Department sourceDept = new Department("a", "b");
  private final Department endDept = new Department("c", "d");
  private final HospitalUser emp =
      new HospitalUser("Wilson", "Softeng", "Wong", HospitalUser.EmployeeType.MEDICAL, sourceDept);
  private final HospitalUser assignedEmp =
      new HospitalUser("Jonathan", "Elias", "Golden", HospitalUser.EmployeeType.MEDICAL, endDept);
  AudioVisual testAV =
      new AudioVisual(
          emp,
          new Date(2023 - 1 - 31),
          new Date(2023 - 2 - 1),
          ServiceRequest.Urgency.MODERATELY_URGENT,
          "Headphones",
          "Connors Fault",
          "Connor destroyed my headphones :(",
          new LocationName("Name", LocationName.LocationType.EXIT, "name"));

  /** Reset testSan after each test */
  @BeforeEach
  @AfterEach
  public void resetTestSanitation() {
    emp.setFirstName("Wilson");
    emp.setMiddleName("Softeng");
    emp.setLastName("Wong");
    emp.setDepartment(sourceDept);
    assignedEmp.setFirstName("Jonathan");
    assignedEmp.setMiddleName("Elias");
    assignedEmp.setLastName("Golden");
    assignedEmp.setDepartment(endDept);
    emp.setEmployeeType(HospitalUser.EmployeeType.MEDICAL);
    assignedEmp.setEmployeeType(HospitalUser.EmployeeType.MEDICAL);
    testAV.setAssignedEmp(assignedEmp);
    testAV.setDate(new Date(2023 - 1 - 31));
    testAV.setDateOfSubmission(new Date(2023 - 2 - 1));
    testAV.setUrgency(ServiceRequest.Urgency.MODERATELY_URGENT);
    testAV.setDeviceType("Headphones");
    testAV.setReason("Connors Fault");
    testAV.setDescription("Connor destroyed my headphones :(");
    testAV.setLocation(new LocationName("Name", LocationName.LocationType.EXIT, "name"));
  }

  /** Tests setter for emp */
  @Test
  public void changeEmpTest() {
    HospitalUser newEmp =
        new HospitalUser("Bob", "Bobby", "Jones", HospitalUser.EmployeeType.ADMIN, endDept);
    testAV.setEmp(newEmp);
    assertEquals(newEmp, testAV.getEmp());
  }

  /** Tests that the department clears (something -> null) correctly */
  @Test
  public void clearEmpTest() {
    testAV.setEmp(null);
    assertNull(testAV.getEmp());
  }

  /** Starts the location as null, then sets it to be something */
  @Test
  public void setEmpTest() {
    AudioVisual test =
        new AudioVisual(
            null, new Date(), new Date(), ServiceRequest.Urgency.NOT_URGENT, "a", "b", "c", null);
    test.setEmp(new HospitalUser("a", "b", "c", HospitalUser.EmployeeType.MEDICAL, null));

    // Assert that the location is correct
    assertEquals(
        new HospitalUser("a", "b", "c", HospitalUser.EmployeeType.MEDICAL, null), test.getEmp());
  }

  /** Starts the location name as null and sets it to null */
  @Test
  public void nullToNullEmployeeTest() {
    AudioVisual test =
        new AudioVisual(
            null, new Date(), new Date(), ServiceRequest.Urgency.NOT_URGENT, "A", "B", "C", null);
    test.setEmp(null);

    // Assert that the location is correct
    assertNull(test.getEmp());
  }

  /** Test setter for Assigned emp */
  @Test
  public void changeAssignedEmpTest() {
    HospitalUser newEmp =
        new HospitalUser("Bob", "Bobby", "Jones", HospitalUser.EmployeeType.ADMIN, sourceDept);
    testAV.setAssignedEmp(newEmp);
    assertEquals(newEmp, testAV.getAssignedEmp());
  }

  /** Tests that the department clears (something -> null) correctly */
  @Test
  public void clearAssignedEmpTest() {
    testAV.setAssignedEmp(emp);
    testAV.setAssignedEmp(null);
    assertNull(testAV.getAssignedEmp());
  }

  /** Starts the location as null, then sets it to be something */
  @Test
  public void setAssignedEmpTest() {
    AudioVisual test =
        new AudioVisual(
            null, new Date(), new Date(), ServiceRequest.Urgency.NOT_URGENT, "L", "a", "b", null);
    test.setAssignedEmp(new HospitalUser("a", "b", "c", HospitalUser.EmployeeType.MEDICAL, null));

    // Assert that the location is correct
    assertEquals(
        new HospitalUser("a", "b", "c", HospitalUser.EmployeeType.MEDICAL, null),
        test.getAssignedEmp());
  }

  /** Starts the location name as null and sets it to null */
  @Test
  public void nullToNullAssignedEmployeeTest() {
    AudioVisual test =
        new AudioVisual(
            null,
            new Date(),
            new Date(),
            ServiceRequest.Urgency.NOT_URGENT,
            "b",
            "as",
            "qwer",
            null);
    test.setAssignedEmp(null);
    test.setAssignedEmp(null);

    // Assert that the location is correct
    assertNull(test.getAssignedEmp());
  }

  /** Tests setter for dateOfIncident */
  @Test
  public void setDateTest() {
    Date newDOI = new Date(2002 - 1 - 17);
    testAV.setDate(newDOI);
    assertEquals(newDOI, newDOI);
  }

  /** Tests setter for dateOfSubmission */
  @Test
  public void setDateOfSubmissionTest() {
    Date newDOS = new Date(2002 - 1 - 17);
    testAV.setDateOfSubmission(newDOS);
    assertEquals(newDOS, testAV.getDateOfSubmission());
  }

  /** Tests setter for urgency */
  @Test
  public void setUrgencyTest() {
    testAV.setUrgency(ServiceRequest.Urgency.NOT_URGENT);
    assertEquals(ServiceRequest.Urgency.NOT_URGENT, testAV.getUrgency());
  }

  /** Tests setter for accoommodationType */
  @Test
  public void setDeviceTypeTest() {
    testAV.setDeviceType("bbb");
    assertEquals("bbb", testAV.getDeviceType());
  }

  /** Tests setter for patientFirstName */
  @Test
  public void setReasonTest() {
    testAV.setReason("Steve");
    assertEquals("Steve", testAV.getReason());
  }

  /** Tests setter for patientMiddleName */
  @Test
  public void setDescriptionTest() {
    testAV.setDescription("description");
    assertEquals("description", testAV.getDescription());
  }

  /** Tests setter for location */
  @Test
  public void updateLocationTest() {
    testAV.setLocation(new LocationName("Hello", LocationName.LocationType.CONF, "Hello"));
    assertEquals(
        new LocationName("Hello", LocationName.LocationType.CONF, "Hello"), testAV.getLocation());
  }

  /** Tests that the department clears (something -> null) correctly */
  @Test
  public void clearLocationTest() {
    testAV.setLocation(null);
    assertNull(testAV.getLocation());
  }

  /** Starts the location as null, then sets it to be something */
  @Test
  public void setLocationTest() {
    AudioVisual test =
        new AudioVisual(
            assignedEmp,
            new Date(),
            new Date(),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "b",
            "c",
            null);
    test.setLocation(new LocationName("a", LocationName.LocationType.INFO, "B"));

    // Assert that the location is correct
    assertEquals(new LocationName("a", LocationName.LocationType.INFO, "B"), test.getLocation());
  }

  /** Starts the location name as null and sets it to null */
  @Test
  public void nullToNullLocationTest() {
    AudioVisual test =
        new AudioVisual(
            assignedEmp,
            new Date(),
            new Date(),
            ServiceRequest.Urgency.NOT_URGENT,
            "b",
            "as",
            "qwer",
            null);
    test.setLocation(null);

    // Assert that the location is correct
    assertNull(test.getLocation());
  }

  /** Checks to see if toString makes a string in the same format specified in Sanitation.java */
  @Test
  public void toStringTest() {
    String sanToString = testAV.toString();
    assertEquals(sanToString, testAV.getClass().getSimpleName() + "_" + testAV.getId());
  }

  /**
   * Tests the equals and hash code methods for the AudioVisual class, ensures that fetched objects
   * are equal
   */
  @Test
  public void testEqualsAndHashCode() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser(
            "Wilson", "Softeng", "Wong", HospitalUser.EmployeeType.MEDICAL, sourceDept);
    LocationName location = new LocationName("Name", LocationName.LocationType.EXIT, "name");

    session.persist(sourceDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(2023 - 1 - 31),
            new Date(2023 - 2 - 1),
            ServiceRequest.Urgency.MODERATELY_URGENT,
            "Emre",
            "Rusen",
            "Sabaz",
            location);
    session.persist(av);

    // Assert that the one thing in the database matches this
    assertEquals(av, session.createQuery("FROM AudioVisual", AudioVisual.class).getSingleResult());
    assertEquals(
        av.hashCode(),
        session.createQuery("FROM AudioVisual ", AudioVisual.class).getSingleResult().hashCode());

    // Identical av request that should have a different ID
    AudioVisual av2 =
        new AudioVisual(
            emp,
            new Date(2023 - 1 - 31),
            new Date(2023 - 2 - 1),
            ServiceRequest.Urgency.MODERATELY_URGENT,
            "Emre",
            "Rusen",
            "Sabaz",
            location);
    session.persist(av2); // Load av2 into the DB, set its ID

    assertNotEquals(av, av2); // Assert av and av2 aren't equal
    assertNotEquals(av.hashCode(), av2.hashCode()); // Assert their has hash codes are different

    // Completely different av request
    AudioVisual av3 =
        new AudioVisual(
            emp,
            new Date(2024 - 2 - 20),
            new Date(2024 - 3 - 21),
            ServiceRequest.Urgency.VERY_URGENT,
            "Owen",
            "Matthew",
            "Krause",
            location);
    session.persist(av3); // Load av3 into the DB, set its ID

    assertNotEquals(av, av3); // Assert av and av3 aren't equal
    assertNotEquals(av.hashCode(), av3.hashCode()); // Assert their hash codes are different

    transaction.rollback();
    session.close();
  }

  /** Tests that deleting the emp this is associated to with a query sets it to null */
  @Test
  public void locationDeleteCascadeQueryTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("b", "a", "d", HospitalUser.EmployeeType.MEDICAL, sourceDept);
    LocationName location = new LocationName("q", LocationName.LocationType.EXIT, "name");

    session.persist(sourceDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(2014 - 2 - 14),
            new Date(2026 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "ab",
            "cd",
            "gjh",
            location);
    session.persist(av);

    // Remove the location
    session.createMutationQuery("DELETE FROM LocationName").executeUpdate();

    session.flush();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertNull(session.createQuery("FROM LocationName", LocationName.class).uniqueResult());
    assertNull(av.getLocation()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Test that updating the location cascades */
  @Test
  public void locationUpdateCascadeTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("jhj", "aew", "hgfd", HospitalUser.EmployeeType.ADMIN, endDept);
    LocationName location = new LocationName("b", LocationName.LocationType.EXIT, "a");

    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(2014 - 2 - 14),
            new Date(2026 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "aasfdfb",
            "cghd",
            "gwerjh",
            location);
    session.persist(av);

    // Change the location
    session.createMutationQuery("UPDATE LocationName SET longName = 'newName'").executeUpdate();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertEquals(
        new LocationName("newName", LocationName.LocationType.EXIT, "name"),
        session.find(LocationName.class, "newName"));
    assertEquals(
        new LocationName("newName", LocationName.LocationType.EXIT, "name"),
        av.getLocation()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that deleting the emp this is referenced to sets it to null */
  @Test
  public void empDeleteCascadeTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    session.persist(av);

    session.flush();

    // Change the emp
    session.remove(emp);

    session.flush();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertNull(session.find(HospitalUser.class, emp.getId()));
    assertNull(av.getEmp()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that deleting the emp this is associated to with a query sets it to null */
  @Test
  public void empDeleteCascadeQueryTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    session.persist(av);

    // Change the enp
    session
        .createMutationQuery("DELETE FROM HospitalUser WHERE id = :id")
        .setParameter("id", emp.getId())
        .executeUpdate();

    session.flush();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertNull(
        session
            .createQuery("FROM HospitalUser WHERE id = :id", HospitalUser.class)
            .setParameter("id", emp.getId())
            .uniqueResult());
    assertNull(av.getEmp()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that updating the employee results in a cascade update failure */
  @Test
  public void empUpdateCascadeTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    session.persist(av);

    // Commit stuff so we can access it later (it's persisted)
    transaction.commit();
    transaction = session.beginTransaction();

    // Change the enp
    assertThrows(
        Exception.class,
        () ->
            session
                .createMutationQuery("UPDATE HospitalUser SET id = 999 WHERE id = :id")
                .setParameter("id", emp.getId())
                .executeUpdate());

    transaction.rollback(); // This transaction is trash due to the SQL error
    transaction = session.beginTransaction(); // Create a new transaction

    // Update the request
    av = session.createQuery("FROM AudioVisual", AudioVisual.class).getSingleResult();

    // Assert the location is not actually gone
    assertEquals(emp, session.find(HospitalUser.class, emp.getId()));
    assertEquals(emp, av.getEmp()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that deleting the emp this is associated to with a query sets it to null */
  @Test
  public void assignedEmpDeleteCascadeQueryTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(assignedEmp);
    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            assignedEmp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    av.setAssignedEmp(emp);
    session.persist(av);

    // Change the enp
    session
        .createMutationQuery("DELETE FROM HospitalUser WHERE id = :id")
        .setParameter("id", emp.getId())
        .executeUpdate();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertNull(
        session
            .createQuery("FROM HospitalUser WHERE id = :id", HospitalUser.class)
            .setParameter("id", emp.getId())
            .uniqueResult());
    assertNull(av.getAssignedEmp()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that updating the employee results in a cascade update failure */
  @Test
  public void assignedEmpUpdateCascadeTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(endDept);
    session.persist(assignedEmp);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            assignedEmp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    av.setAssignedEmp(emp);
    session.persist(av);

    transaction.commit(); // Commit what we have, so that we can get it after the failure

    transaction = session.beginTransaction(); // Open a new transaction
    // Change the enp
    assertThrows(
        Exception.class,
        () ->
            session
                .createMutationQuery("UPDATE HospitalUser SET id = 999 WHERE id = :id")
                .setParameter("id", emp.getId())
                .executeUpdate());

    session.flush();

    transaction.rollback(); // End that transaction

    transaction = session.beginTransaction();

    // Update the request
    session.refresh(av);

    // Assert the location is not actually gone
    assertEquals(emp, session.find(HospitalUser.class, emp.getId()));
    assertEquals(emp, av.getAssignedEmp()); // Assert the location is null

    transaction.rollback();
    session.close();
  }

  /** Tests that deleting the emp this is associated to with a query sets it to null */
  @Test
  public void bothEmpDeleteCascadeQueryTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(assignedEmp);
    session.persist(endDept);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    av.setAssignedEmp(emp);
    session.persist(av);

    // Change the enp
    session
        .createMutationQuery("DELETE FROM HospitalUser WHERE id = :id")
        .setParameter("id", emp.getId())
        .executeUpdate();

    // Update the request
    session.refresh(av);

    // Assert the location is actually gone
    assertNull(
        session
            .createQuery("FROM HospitalUser WHERE id = :id", HospitalUser.class)
            .setParameter("id", emp.getId())
            .uniqueResult());
    assertNull(av.getAssignedEmp()); // Assert the location is null
    assertNull(av.getEmp());

    transaction.rollback();
    session.close();
  }

  /** Tests that updating the employee results in a cascade update failure */
  @Test
  public void bothEmpUpdateCascadeTest() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession(); // Open a session
    Transaction transaction = session.beginTransaction(); // Begin a transaction

    HospitalUser emp =
        new HospitalUser("basdf", "axcvb", "dxcbv", HospitalUser.EmployeeType.STAFF, endDept);
    LocationName location = new LocationName("qwq", LocationName.LocationType.EXIT, "zx");

    session.persist(endDept);
    session.persist(assignedEmp);
    session.persist(emp);
    session.persist(location);
    // Create the av request we will use
    AudioVisual av =
        new AudioVisual(
            emp,
            new Date(201674 - 2 - 14),
            new Date(20126 - 1 - 12),
            ServiceRequest.Urgency.NOT_URGENT,
            "a",
            "d",
            "jh",
            location);
    av.setAssignedEmp(emp);
    session.persist(av);

    transaction.commit(); // Commit what we have, so that we can get it after the failure

    transaction = session.beginTransaction(); // Open a new transaction
    // Change the enp
    assertThrows(
        Exception.class,
        () ->
            session
                .createMutationQuery("UPDATE HospitalUser SET id = 999 WHERE id = :id")
                .setParameter("id", emp.getId())
                .executeUpdate());

    session.flush();

    transaction.rollback(); // End that transaction

    transaction = session.beginTransaction();

    // Update the request
    session.refresh(av);

    // Assert the location is not actually gone
    assertEquals(emp, session.find(HospitalUser.class, emp.getId()));
    assertEquals(emp, av.getAssignedEmp()); // Assert the location is null
    assertEquals(emp, av.getEmp());

    transaction.rollback();
    session.close();
  }
}
