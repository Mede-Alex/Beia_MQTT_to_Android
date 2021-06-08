import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MQTTClient(context: Context?,
                 serverURI: String,
                 clientID: String = "") {
    private var mqttClient = MqttAndroidClient(context, serverURI, clientID)

    fun connect(username:   String               = "",
                password:   String               = "",
                cbConnect:  IMqttActionListener  = defaultCbConnect,
                cbClient:   MqttCallback         = defaultCbClient) {
        mqttClient.setCallback(cbClient)
        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()

        try {
            mqttClient.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic:        String,
                  qos:          Int                 = 1,
                  cbSubscribe:  IMqttActionListener = defaultCbSubscribe) {
        try {
            mqttClient.subscribe(topic, qos, null, cbSubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun unsubscribe(topic:          String,
                    cbUnsubscribe:  IMqttActionListener = defaultCbUnsubscribe) {
        try {
            mqttClient.unsubscribe(topic, null, cbUnsubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic:      String,
                msg:        String,
                qos:        Int                 = 1,
                retained:   Boolean             = false,
                cbPublish:  IMqttActionListener = defaultCbPublish) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, cbPublish)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect(cbDisconnect: IMqttActionListener = defaultCbDisconnect ) {
        try {
            mqttClient.disconnect(null, cbDisconnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}

mqttClient.connect(
username,
pwd,
object : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        Log.d(this.javaClass.name, "Connection success")

        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        Log.d(this.javaClass.name, "Connection failure: ${exception.toString()}")

        Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}",
            Toast.LENGTH_SHORT).show()
    }
},
object : MqttCallback {
    override fun messageArrived(topic: String?, message: MqttMessage?) {
        val msg = "Receive message: ${message.toString()} from topic: $topic"
        Log.d(this.javaClass.name, msg)

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun connectionLost(cause: Throwable?) {
        Log.d(this.javaClass.name, "Connection lost ${cause.toString()}")
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Log.d(this.javaClass.name, "Delivery complete")
    }
})



mqttClient.publish(
topic,
message,
1,
false,
object : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        val msg ="Publish message: $message to topic: $topic"
        Log.d(this.javaClass.name, msg)

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        Log.d(this.javaClass.name, "Failed to publish message to topic")
    }
})

mqttClient.subscribe(
topic,
1,
object : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        val msg = "Subscribed to: $topic"
        Log.d(this.javaClass.name, msg)

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        Log.d(this.javaClass.name, "Failed to subscribe: $topic")
    }
})


mqttClient.disconnect(
object : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        Log.d(this.javaClass.name, "Disconnected")

        Toast.makeText(context, "MQTT Disconnection success", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        Log.d(this.javaClass.name, "Failed to disconnect")
    }
})