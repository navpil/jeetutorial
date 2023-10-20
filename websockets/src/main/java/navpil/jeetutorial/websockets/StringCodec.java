package navpil.jeetutorial.websockets;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class StringCodec implements Encoder.Text<String>, Decoder.Text<String> {

    @Override
    public String decode(String s) {
        return s;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public String encode(String s) {
        return s;
    }

    @Override
    public void init(EndpointConfig config) {
        //no need
    }

    @Override
    public void destroy() {
        //no need
    }
}
