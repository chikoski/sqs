/*

 MidiPlayer.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/01/11

 */
package net.sqs2.sound;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {

	static {
		System.setProperty("javax.sound.midi.Receiver", "com.sun.media.sound.MidiProvider#SunMIDI1");
		System.setProperty("javax.sound.midi.Synthesizer", "#InternalSynth");
	}

	private boolean DEBUG = false;
	private MidiChannel channel = null;
	private Synthesizer synthesizer = null;
	// private Soundbank soundbank = null;

	Receiver receiver = null;

	public MidiPlayer(int instrument_id) {
		try {

			// MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			// MidiDevice outDevice = null;
			// MidiDevice inDevice = null;
			// System.out.println("available devices:");
			/*
			 * for (int i = 0; i < infos.length; i++) {
			 * System.out.println(infos[i].toString()); MidiDevice device =
			 * MidiSystem.getMidiDevice(infos[i]); if (! (device instanceof
			 * Synthesizer)) { if (device.getMaxReceivers() != 0) { outDevice =
			 * device; } if (device.getMaxTransmitters() != 0) { inDevice =
			 * device; } } }
			 */
			// System.out.println("Using MIDI OUT Device: " + outDevice);
			// System.out.println("Using MIDI IN Device: " + inDevice);
			receiver = MidiSystem.getReceiver();

			this.synthesizer = MidiSystem.getSynthesizer();
			// soundbank = synthesizer.getDefaultSoundbank();
			this.synthesizer.open();

			Instrument[] instruments = this.synthesizer.getDefaultSoundbank().getInstruments();
			this.synthesizer.loadInstrument(instruments[instrument_id]);

			this.channel = this.synthesizer.getChannels()[0];

		} catch (Exception ex) {
			if (DEBUG) {
				ex.printStackTrace();
			}
		}

	}

	public void noteOn(int noteNumber, int velocity) {
		if (receiver == null) {
			return;
		}
		try {
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.NOTE_ON, noteNumber, velocity);
			receiver.send(sm, 1);
		} catch (InvalidMidiDataException ignore) {
		}
		if (this.channel != null) {
			this.channel.noteOn(noteNumber, velocity);
		}
	}

	public void noteOff(int noteNumber, int velocity) {
		if (receiver == null) {
			return;
		}
		try {
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.NOTE_ON, noteNumber, velocity);
			receiver.send(sm, 1);
		} catch (InvalidMidiDataException ignore) {
		}
		if (this.channel != null) {
			this.channel.noteOff(noteNumber, velocity);
		}
	}

	public void close() {
		try {
			this.synthesizer.close();
		} catch (Exception e) {
			if (this.channel != null) {
				this.channel.allNotesOff();
			}
		}
		if (receiver != null) {
			receiver.close();
		}
	}
}
