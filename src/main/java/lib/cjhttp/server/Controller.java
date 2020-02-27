package lib.cjhttp.server;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface Controller {

	public Router route();

}
