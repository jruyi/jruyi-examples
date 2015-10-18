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

package org.jruyi.example.daytime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.jruyi.common.IService;
import org.jruyi.common.Properties;
import org.jruyi.io.IBuffer;
import org.jruyi.io.ISession;
import org.jruyi.io.ISessionService;
import org.jruyi.io.IoConstants;
import org.jruyi.io.SessionListener;
import org.jruyi.io.StringCodec;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

@Component(name = "jruyi.example.daytime", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
// we don't care the type of incoming/outgoing message, so use <Object, Object>
public class DaytimeServer extends SessionListener<Object, Object> {

	private ComponentFactory m_tcpServerFactory;

	private ComponentInstance m_tcpServer;
	private ISessionService m_ss;

	@Override
	public void onSessionOpened(ISession session) {
		// Current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss-zzz", Locale.ENGLISH);
		String daytime = dateFormat.format(new Date());

		// Allocate the buffer and construct the message
		IBuffer out = session.createBuffer();
		out.write(daytime, StringCodec.us_ascii());
		out.write("\r\n", StringCodec.us_ascii());

		// Send out the message
		m_ss.write(session, out);
	}

	@Override
	public void onMessageSent(ISession session, Object outMsg) {
		// Close the connection
		m_ss.closeSession(session);
	}

	@Reference(name = "tcpServerFactory", //
	target = "(" + ComponentConstants.COMPONENT_NAME + "=" + IoConstants.CN_TCPSERVER_FACTORY + ")")
	void bindTcpServerFactory(ComponentFactory factory) {
		m_tcpServerFactory = factory;
	}

	void unbindTcpServerFactory(ComponentFactory factory) {
		m_tcpServerFactory = null;
	}

	// This method is invoked when this component is being activated.
	void activate(Map<String, ?> properties) throws Exception {
		final Properties conf = new Properties(properties);
		conf.put(IoConstants.SERVICE_ID, "jruyi.example.daytime"); // Service ID
		conf.put("tcpNoDelay", true); // Disable Nagle's algorithm

		// Create the Session Service of TcpServer
		ComponentInstance tcpServer = m_tcpServerFactory.newInstance(conf);
		ISessionService ss = (ISessionService) tcpServer.getInstance();

		// Set SessionListener
		ss.setSessionListener(this);

		// Start the Session Service
		ss.start();
		m_tcpServer = tcpServer;
		m_ss = ss;
	}

	// This method is called when this component is being deactivated.
	void deactivate() {
		// Stop the Session Service
		m_tcpServer.dispose();
		m_tcpServer = null;
		m_ss = null;
	}
}
