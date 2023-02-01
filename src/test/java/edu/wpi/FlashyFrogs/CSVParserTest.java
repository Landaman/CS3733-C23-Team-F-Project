package edu.wpi.FlashyFrogs;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.FlashyFrogs.ORM.Node;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.junit.jupiter.api.*;

public class CSVParserTest {
  private static SessionFactory
      sessionFactory; // Session factory to be created before all tests have run
  private static StandardServiceRegistry
      serviceRegistry; // Service registry associated with the factory
  private static Session testSession; // Session to be used for each individual test

  static File nodeFile = new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/L1Nodes.csv");
  static File testNodeFile =
      new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/testNodes.csv");
  static File edgeFile = new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/L1Edges.csv");
  static File moveFile = new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/move.csv");
  static File locationFile =
      new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/locationName.csv");
  static File emptyFile = new File("src/test/resources/edu/wpi/FlashyFrogs/CSVFiles/emptyFile.csv");

  @BeforeEach
  public void setup() {
    testSession = Main.factory.openSession();
  }

  @AfterEach
  public void teardownSession() {
    Transaction deleteTransaction = testSession.beginTransaction(); // Clearing transaction

    testSession.createMutationQuery("DELETE FROM Edge").executeUpdate(); // Drop edge
    testSession.createMutationQuery("DELETE FROM Move").executeUpdate(); // Drop move
    testSession.createMutationQuery("DELETE FROM LocationName").executeUpdate(); // Drop location
    testSession.createMutationQuery("DELETE FROM Node").executeUpdate(); // Drop node

    deleteTransaction.commit(); // Commit the transaction

    testSession.close(); // Close the session
  }

  /** tests that files with the correct data does not fail */
  @Test
  public void readFilesTest() {
    try {
      assertDoesNotThrow(
          () -> {
            CSVParser.readFiles(nodeFile, edgeFile, moveFile, locationFile, Main.factory);
          });
    } catch (Exception e) {
      fail();
    }
  }

  /** tests that empty files will work */
  @Test
  public void readEmptyFilesTest() {
    try {
      assertDoesNotThrow(
          () -> {
            CSVParser.readFiles(emptyFile, emptyFile, emptyFile, emptyFile, Main.factory);
          });
    } catch (Exception e) {
      fail();
    }
  }

  /** tests to ensure that the correct objects are inserted into the database */
  @Test
  public void insertTest() {
    try {
      CSVParser.readFiles(testNodeFile, emptyFile, emptyFile, emptyFile, Main.factory);
    } catch (FileNotFoundException e) {
      fail();
    }

    try {
      Scanner nodeFileScanner = new Scanner(testNodeFile);
      nodeFileScanner.nextLine();
      String[] fields = nodeFileScanner.nextLine().split(",");
      Node node =
          new Node(
              fields[0],
              fields[4],
              Node.Floor.valueOf(fields[3]),
              Integer.parseInt(fields[1]),
              Integer.parseInt(fields[2]));
      assertEquals(node, testSession.find(Node.class, node.getId()));
    } catch (FileNotFoundException e) {
      fail();
    }
  }
}