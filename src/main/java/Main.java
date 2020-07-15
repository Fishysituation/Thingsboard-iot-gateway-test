import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.eclipse.paho.client.mqttv3.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main {

  private static String BROKER = "tcp://localhost:1883";

  public static void main(String[] args) throws MqttException {

    Set<IDevice> devices = new HashSet<>();

    // add normal thermometer
    IMqttClient client1 = new MqttClient(BROKER, UUID.randomUUID().toString());
    Thermometer1 therm1 = new Thermometer1(client1);
    devices.add(therm1);

    // add battery powered thermometer
    IMqttClient client2 = new MqttClient(BROKER, UUID.randomUUID().toString());
    Thermometer2 therm2 = new Thermometer2(client2);
    devices.add(therm2);

    // add battery powered thermometer
    IMqttClient client3 = new MqttClient(BROKER, UUID.randomUUID().toString());
    Thermometer3 therm3 = new Thermometer3(client3);
    devices.add(therm3);


    devices.forEach(d -> System.out.println(d.toString()));

    // keep publishing to broker at regular intervals until interrupted
    while (true) {
      System.out.println("Publishing...");
      // try to publish from all devices
      devices.forEach(
          d -> {
            try {
              d.publish();
              Thread.sleep(2000);
            } catch (Exception e) {
              System.out.println("Error while publishing");
            }
          });
      // wait 15 seconds, disconnect all devices if interrupted
      try {
        Thread.sleep(4000);
      } catch (InterruptedException e) {
        System.out.println("Disconnecting...");
        devices.forEach(
            d -> {
              try {
                d.disconnect();
              } catch (MqttException ex) {
                System.out.println("Error while disconnecting");
              }
            });
        break;
      }
    }
  }
}
