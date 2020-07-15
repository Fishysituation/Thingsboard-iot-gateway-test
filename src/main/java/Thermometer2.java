import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

// class representing battery powered thermometer
public class Thermometer2 extends AbstractThermometer {

  public Thermometer2(IMqttClient client) throws MqttException {
    super(client);
  }

  @Override
  protected String getTopic() {
    return "/sensor/data/therm2";
  }

  @Override
  public void connect() throws MqttException {
    client.connect();
    JSONObject json = new JSONObject();
    json.put("serialNumber", getID());
    json.put("sensorType", "Thermometer 2");
    client.publish("sensor/connect", new MqttMessage(json.toString().getBytes()));
  }

  @Override
  protected MqttMessage getMessage() {
    JSONObject json = new JSONObject();
    json.put("serialNumber", getID());
    json.put("sensorType", "Thermometer");
    json.put("temp", getTemperature());
    json.put("sensorModel", "T1000");
    json.put("batteryLevel", 90);
    return new MqttMessage(json.toString().getBytes());
  }

  @Override
  public String toString() {
    return "Thermometer type 2: " + getID();
  }
}
