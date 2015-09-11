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

package org.jruyi.example.echo;

import org.jruyi.core.INioService;
import org.jruyi.core.ITcpServerConfiguration;
import org.jruyi.core.RuyiCore;
import org.jruyi.io.IBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServer {

	private static final Logger c_logger = LoggerFactory.getLogger(EchoServer.class);

	static class ShutdownHook extends Thread {

		private final INioService<IBuffer, IBuffer, ? extends ITcpServerConfiguration> m_tcpServer;

		ShutdownHook(INioService<IBuffer, IBuffer, ? extends ITcpServerConfiguration> tcpServer) {
			m_tcpServer = tcpServer;
		}

		@Override
		public void run() {
			m_tcpServer.stop();
		}
	}

	public static void main(String[] args) {
		try {
			// Build an Nio Service of type TcpServer
			final INioService<IBuffer, IBuffer, ? extends ITcpServerConfiguration> tcpServer = RuyiCore
					.newTcpServerBuilder()
					.port(10007)
					.serviceId("jruyi.example.echo")
					.build();

			// Set sessionListener
			tcpServer.sessionListener(new EchoServerListener(tcpServer));

			// Start tcpServer
			tcpServer.start();

			// To shutdown gracefully
			Runtime.getRuntime().addShutdownHook(new ShutdownHook(tcpServer));
		} catch (Throwable t) {
			c_logger.error("Failed to start echo service", t);
		}
	}
}
