import org.eclipse.paho.client.mqttv3.MqttException;

public interface IDevice {
  String getID();

  void publish() throws MqttException;

  void connect() throws MqttException;

  void disconnect() throws MqttException;
}
