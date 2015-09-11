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
import org.jruyi.io.IBuffer;
import org.jruyi.io.ISession;
import org.jruyi.io.SessionListener;

class EchoServerListener extends SessionListener<IBuffer, IBuffer> {

	private final INioService<IBuffer, IBuffer, ? extends ITcpServerConfiguration> m_tcpServer;

	EchoServerListener(INioService<IBuffer, IBuffer, ? extends ITcpServerConfiguration> tcpServer) {
		m_tcpServer = tcpServer;
	}

	@Override
	public void onMessageReceived(ISession session, IBuffer inMsg) {
		// Send back whatever is received.
		m_tcpServer.write(session, inMsg);
	}
}
