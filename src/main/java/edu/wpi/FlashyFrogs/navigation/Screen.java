package edu.wpi.FlashyFrogs.navigation;

import edu.wpi.FlashyFrogs.GeneratedExclusion;

@GeneratedExclusion
public enum Screen {
  ROOT("views/RequestsHome.fxml"),
  HOME("views/RequestsHome.fxml"),
  SERVICE_REQUEST("views/AllServiceRequests.fxml"),
  SANITATION_PAGE("views/SanitationService.fxml"),
  SECURITY_PAGE("views/SecurityService.fxml"),
  TRANSPORT_PAGE("views/Transport.fxml");
  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
