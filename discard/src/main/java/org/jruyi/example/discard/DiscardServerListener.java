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

import org.jruyi.common.StrUtil;
import org.jruyi.io.IBuffer;
import org.jruyi.io.ISession;
import org.jruyi.io.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DiscardServerListener extends SessionListener<IBuffer, Object> {

	private static final Logger c_logger = LoggerFactory.getLogger(DiscardServerListener.class);

	@Override
	public void onMessageReceived(ISession session, IBuffer inMsg) {
		c_logger.info(StrUtil.join("Got data >>", StrUtil.getLineSeparator(), inMsg));
	}
}
