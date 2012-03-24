/*

 SoundManager.java

 Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2008/01/13

 */

package net.sqs2.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.omr.model.MarkReaderConfiguration;

public class SoundManager implements LineListener {

	private static SoundManager singleton;
	private Clip prevClip = null;
	private Map<String, Clip> clipMap = new HashMap<String, Clip>();

	public static SoundManager getInstance() {
		synchronized (SoundManager.class) {
			if (singleton == null) {
				singleton = new SoundManager();
			}
			return singleton;
		}
	}

	public void update(LineEvent event) {
		// when play stopped or play to the end state
		if (event.getType() == LineEvent.Type.STOP) {
			Clip clip = (Clip) event.getSource();
			clip.stop();
			clip.setFramePosition(0); // rewind playing position
		}
	}

	public void play(String name) {
		if (! MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_SOUND)) {
			return;
		}
		if (this.prevClip != null) {
			// this.prevClip.stop();
			// this.prevClip.setFramePosition(0);
		}
		Clip clip = this.clipMap.get(name);
		if (clip == null) {
			try {
				clip = load(name);
				if (clip == null) {
					Logger.getLogger(getClass().getName()).severe("clip is null");
					return;
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
			this.clipMap.put(name, clip);
		}
		clip.start();
		this.prevClip = clip;
	}

	public void play(URL url) {
		if (! MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_SOUND)) {
			return;
		}
		if (this.prevClip != null) {
			// this.prevClip.stop();
			// this.prevClip.setFramePosition(0);
		}
		Clip clip = this.clipMap.get(url.toString());
		if (clip == null) {
			try {
				clip = load(url);
				if (clip == null) {
					return;
				}
			} catch (IOException ex) {
				return;
			}
			this.clipMap.put(url.toString(), clip);
		}
		clip.start();
		this.prevClip = clip;
	}

	public Clip load(URL url) throws IOException {
		InputStream in = url.openStream();
		return load(url.toString(), in);
	}

	public Clip load(String name) throws IOException {
		String url = MarkReaderJarURIContext.getSoundBaseURI() + name;
		Logger.getLogger(getClass().getName()).info(url);
		// InputStream in = ExigridEngine.class.getResource(name).openStream();
		InputStream in = new URL(url).openStream();
		// ExigridEngine.class.getResource(name).openStream();
		return load(name, in);
	}

	public Clip load(String name, File filename) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
		return load(name, in);
	}

	public Clip load(String name, InputStream in) {
		try {
			// open audio stream
			AudioInputStream stream = AudioSystem.getAudioInputStream(in);

			DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());

			// make empty audio clip
			Clip clip = (Clip) AudioSystem.getLine(info);
			// observe clip
			clip.addLineListener(this);
			// open audio stream as clip
			clip.open(stream);
			// register clip
			this.clipMap.put(name, clip);
			// close stream
			stream.close();
			return clip;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException ignore) {
			// e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Logger.getLogger(getClass().getName()).warning(e.getMessage());
		}
		return null;
	}
}
