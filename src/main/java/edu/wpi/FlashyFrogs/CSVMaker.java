package edu.wpi.FlashyFrogs;

import edu.wpi.FlashyFrogs.ORM.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.hibernate.Session;

public class CSVMaker {

  public static void makeCSVs() {
    Session session = DBConnection.CONNECTION.getSessionFactory().openSession();

    makeEdgesCSV(session);
    makeNodesCSV(session);
    makeMovesCSV(session);
    makeLocationsCSV(session);
    makeServiceRequestCSV(session);
    makeAudioVisualCSV(session);
    makeComputerServiceCSV(session);
    makeInternalTransportCSV(session);
    makeSanitationCSV(session);
    makeSecurityCSV(session);
    makeReligionCSV(session);
    makeEquipmentTransportCSV(session);
    makeMedicineDeliveryCSV(session);

    session.close();
  }

  private static void makeEdgesCSV(Session session) {
    List<Edge> edges = session.createQuery("SELECT e FROM Edge e", Edge.class).getResultList();
    File edgesFile = new File("edges.csv");
    try {
      FileWriter fileWriter = new FileWriter(edgesFile);
      fileWriter.write("startNode,endNode\n");

      for (Edge edge : edges) {

        fileWriter.write(edge.getNode1() + ",");
        fileWriter.write(edge.getNode2() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeNodesCSV(Session session) {
    List<Node> nodes = session.createQuery("SELECT e FROM Node e", Node.class).getResultList();
    File nodesFile = new File("nodes.csv");
    try {
      FileWriter fileWriter = new FileWriter(nodesFile);
      fileWriter.write("nodeID,xcoord,ycoord,floor,building\n");

      for (Node node : nodes) {

        fileWriter.write(node.getId() + ",");
        fileWriter.write(node.getXCoord() + ",");
        fileWriter.write(node.getYCoord() + ",");
        fileWriter.write(node.getFloor().floorNum + ",");
        fileWriter.write(node.getBuilding() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeMovesCSV(Session session) {
    List<Move> moves = session.createQuery("SELECT e FROM Move e", Move.class).getResultList();
    File movesFile = new File("moves.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      fileWriter.write("nodeID,longName,moveDate\n");

      for (Move move : moves) {

        fileWriter.write(move.getNode().getId() + ",");
        fileWriter.write(move.getLocation().getLongName() + ",");
        fileWriter.write(move.getMoveDate() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeLocationsCSV(Session session) {
    List<LocationName> locationNames =
        session.createQuery("SELECT e FROM LocationName e", LocationName.class).getResultList();
    File movesFile = new File("locations.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      fileWriter.write("nodeType,longName,shortName\n");

      for (LocationName locationName : locationNames) {

        fileWriter.write(locationName.getLocationType() + ",");
        fileWriter.write(locationName.getLongName() + ",");
        fileWriter.write(locationName.getShortName() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeServiceRequestCSV(Session session) {
    List<ServiceRequest> serviceRequests =
        session.createQuery("SELECT e FROM ServiceRequest e", ServiceRequest.class).getResultList();
    File movesFile = new File("serviceRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (ServiceRequest serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getId() + ",");
        fileWriter.write(serviceRequest.getDate() + ",");
        fileWriter.write(serviceRequest.getDateOfSubmission() + ",");
        fileWriter.write(serviceRequest.getRequestType() + ",");
        fileWriter.write(serviceRequest.getStatus() + ",");
        fileWriter.write(serviceRequest.getUrgency() + ",");
        fileWriter.write(serviceRequest.getAssignedEmp() + ",");
        fileWriter.write(serviceRequest.getEmp() + ",");
        fileWriter.write(serviceRequest.getLocation() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeAudioVisualCSV(Session session) {
    List<AudioVisual> serviceRequests =
        session.createQuery("SELECT e FROM AudioVisual e", AudioVisual.class).getResultList();
    File movesFile = new File("audioVisualRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);

      for (AudioVisual serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getDeviceType() + ",");
        fileWriter.write(serviceRequest.getReason() + ",");
        fileWriter.write(serviceRequest.getDescription() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeComputerServiceCSV(Session session) {
    List<ComputerService> serviceRequests =
        session
            .createQuery("SELECT e FROM ComputerService e", ComputerService.class)
            .getResultList();
    File movesFile = new File("computerServiceRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);

      for (ComputerService serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getDeviceType() + ",");
        fileWriter.write(serviceRequest.getModel() + ",");
        fileWriter.write(serviceRequest.getServiceType().ServiceType + ",");
        fileWriter.write(serviceRequest.getDescription() + ",");
        fileWriter.write(serviceRequest.getBestContact() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeInternalTransportCSV(Session session) {
    List<InternalTransport> serviceRequests =
        session
            .createQuery("SELECT e FROM InternalTransport e", InternalTransport.class)
            .getResultList();
    File movesFile = new File("internalTransportRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (InternalTransport serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getPatientID() + ",");
        fileWriter.write(serviceRequest.getTargetLocation() + ",");
        fileWriter.write(serviceRequest.getVision() + ",");
        fileWriter.write(serviceRequest.getHearing() + ",");
        fileWriter.write(serviceRequest.getConsciousness() + ",");
        fileWriter.write(serviceRequest.getHealthStatus() + ",");
        fileWriter.write(serviceRequest.getEquipment() + ",");
        fileWriter.write(serviceRequest.getMode() + ",");
        fileWriter.write(serviceRequest.isIsolation() + ",");
        fileWriter.write(serviceRequest.getPersonalItems() + ",");
        fileWriter.write(serviceRequest.getReason() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeSanitationCSV(Session session) {

    List<Sanitation> serviceRequests =
        session.createQuery("SELECT e FROM Sanitation e", Sanitation.class).getResultList();
    File movesFile = new File("sanitationRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (Sanitation serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getType() + ",");
        fileWriter.write(serviceRequest.getIsolation() + ",");
        fileWriter.write(serviceRequest.getBiohazard().BiohazardLevel + ",");
        fileWriter.write(serviceRequest.getDescription() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void makeSecurityCSV(Session session) {
    List<Security> serviceRequests =
        session.createQuery("SELECT e FROM Security e", Security.class).getResultList();
    File movesFile = new File("securityRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(movesFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (Security serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getIncidentReport() + ",");
        fileWriter.write(serviceRequest.getThreatType().ThreatType + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // make medicine delivery csv
  private static void makeMedicineDeliveryCSV(Session session) {
    List<MedicineDelivery> serviceRequests =
        session
            .createQuery("SELECT e FROM MedicineDelivery e", MedicineDelivery.class)
            .getResultList();
    File medicineFile = new File("medicineDeliveryRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(medicineFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (MedicineDelivery serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getPatientID() + ",");
        fileWriter.write(serviceRequest.getReason() + ",");
        fileWriter.write(serviceRequest.getMedicine() + ",");
        fileWriter.write(serviceRequest.getDosage() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // make equipment transport csv
  private static void makeEquipmentTransportCSV(Session session) {

    List<EquipmentTransport> serviceRequests =
        session
            .createQuery("SELECT e FROM EquipmentTransport e", EquipmentTransport.class)
            .getResultList();
    File equipmentFile = new File("equipmentTransportRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(equipmentFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (EquipmentTransport serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getEquipment() + ",");
        fileWriter.write(serviceRequest.getDescription() + ",");
        fileWriter.write(serviceRequest.getMoveFrom() + ",");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // make religion service csv
  private static void makeReligionCSV(Session session) {

    List<Religion> serviceRequests =
        session.createQuery("SELECT e FROM Religion e", Religion.class).getResultList();
    File religionFile = new File("religionServiceRequests.csv");
    try {
      FileWriter fileWriter = new FileWriter(religionFile);
      // fileWriter.write("nodeType,longName,shortName\n");

      for (Religion serviceRequest : serviceRequests) {

        fileWriter.write(serviceRequest.getPatientID() + ",");
        fileWriter.write(serviceRequest.getReligion() + ",");
        fileWriter.write(serviceRequest.getDescription() + "\n");
      }

      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
