package com.diozero.internal.spi;

/*
 * #%L
 * Device I/O Zero - Core
 * %%
 * Copyright (C) 2016 diozero
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */


import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractDevice implements DeviceInterface {
	private static final Logger logger = LogManager.getLogger(AbstractDevice.class);
	
	private String key;
	private DeviceFactoryInterface deviceFactory;
	
	public AbstractDevice(String key, DeviceFactoryInterface deviceFactory) {
		this.key = key;
		this.deviceFactory = deviceFactory;
	}
	
	@Override
	public final String getKey() {
		return key;
	}
	
	@Override
	public boolean isOpen() {
		return deviceFactory.isDeviceOpened(key);
	}
	
	@Override
	public final void close() {
		logger.debug("close(), key=" + key);
		if (!isOpen()) {
			logger.warn("Device '" + key + "' already closed");
			return;
		}
		
		try {
			closeDevice();
		} catch (IOException e) {
			logger.error("Error closing device " + key + ": " + e, e);
		}
		deviceFactory.deviceClosed(this);
	}
	
	protected abstract void closeDevice() throws IOException;
}
