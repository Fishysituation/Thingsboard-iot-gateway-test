import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.Random;

public abstract class AbstractThermometer implements IDevice {

  private static double START_TEMP = 15;
  private static double END_TEMP = 30;
  private static double START_HUM = 10;
  private static double END_HUM = 80;

  protected final IMqttClient client;
  private final Random generator;

  public AbstractThermometer(IMqttClient client) throws MqttException {
    this.client = client;
    this.generator = new Random();
    connect();
  }

  public String getID() {
    return client.getClientId().substring(0, 5);
  }

  @Override
  public void disconnect() throws MqttException {
    JSONObject json = new JSONObject();
    json.put("serialNumber", getID());
    client.publish("sensor/disconnect", new MqttMessage(json.toString().getBytes()));
    client.disconnect();
  }

  @Override
  public void publish() throws MqttException {
    client.publish(getTopic(), getMessage());
  }

  protected abstract String getTopic();

  protected abstract MqttMessage getMessage();

  // random double for temperature within range (startTemp, endTemp)
  protected double getTemperature() {
    return START_TEMP + (generator.nextDouble() * (END_TEMP - START_TEMP));
  }

  // random double for humidity within range (startHum, endHum)
  protected double getHumidity() {
    return START_HUM + (generator.nextDouble() * (END_HUM - START_HUM));
  }
}
