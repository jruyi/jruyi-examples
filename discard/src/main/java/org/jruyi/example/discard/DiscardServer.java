/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jruyi.example.discard;

import org.jruyi.core.INioService;
import org.jruyi.core.ITcpServerConfiguration;
import org.jruyi.core.RuyiCore;
import org.jruyi.io.IBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscardServer {

	private static final Logger c_logger = LoggerFactory.getLogger(DiscardServer.class);

	static INioService<IBuffer, Object, ? extends ITcpServerConfiguration> s_tcpServer;

	static class ShutdownHook extends Thread {

		@Override
		public void run() {
			s_tcpServer.stop();
		}
	}

	public static void main(String[] args) {
		try {
			// Build an Nio Service of type TcpServer
			final INioService<IBuffer, Object, ? extends ITcpServerConfiguration> tcpServer = RuyiCore
					.newTcpServerBuilder()
					.port(10009)
					.serviceId("jruyi.example.discard")
					.build();

			// Set sessionListener
			tcpServer.sessionListener(new DiscardServerListener());

			// Start tcpServer
			tcpServer.start();

			// To shutdown gracefully
			s_tcpServer = tcpServer;
			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		} catch (Throwable t) {
			c_logger.error("Error", t);
		}
	}
}
