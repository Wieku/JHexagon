package net.wieku.jhexagon.resources;

import java.io.File;
import java.io.FileNotFoundException;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.Gdx;


public class AudioPlayer {

	Music music;
	boolean ended;


	public AudioPlayer(FileHandle file) throws FileNotFoundException{
		if(!file.exists()) throw new IllegalStateException("Cannot find audio");
		
		music = Gdx.audio.newMusic(file);
		music.setLooping(true);
		music.setOnCompletionListener(music1 -> {ended = true;
			System.out.println("ded");});
	}

	
	public void setVolume(float volume){
		music.setVolume(volume);
	}
	
	public float getPosition(){
		return music.getPosition();
	}
	
	public void setPosition(float milis){
		music.setPosition(milis);
	}
	
	public void play(){
		music.play();
		ended = false;
	}

	public void pause(){
		music.pause();
	}
	
	public boolean hasEnded(){
		return ended;
	}
	
	public void stop(){
		music.stop();
		ended = true;
	}


	public boolean isPaused() {
		return !music.isPlaying() && !ended;
	}
}

/*
package net.wieku.jhexagon.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.beadsproject.beads.analysis.FeatureExtractor;
import net.beadsproject.beads.analysis.featureextractors.FFT;
import net.beadsproject.beads.analysis.featureextractors.PeakDetector;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.featureextractors.SpectralDifference;
import net.beadsproject.beads.analysis.featureextractors.SpectralPeaks;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.TimeStamp;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.GranularSamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.EnvelopeType;

import com.badlogic.gdx.Gdx;
import net.beadsproject.beads.ugens.SamplePlayer.LoopType;
import net.beadsproject.beads.ugens.Static;

public class AudioPlayer {

	GranularSamplePlayer player;
	static AudioContext context;
	BeatListener listener;
	Sample sample;
	Glide volumeGlide;
	Glide pitchGlide;
	Glide speedGlide;
	Gain gain;
	ShortFrameSegmenter lowDetector;
	ShortFrameSegmenter highDetector;
	File file;

	static {
		context = new AudioContext(2048);
		context.start();
	}

	public AudioPlayer(File file) throws FileNotFoundException{
		this.file = file;
		if(!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());

		try {
			sample = new Sample(file.getAbsolutePath());
			speedGlide = new Glide(context, 1.0f, 0);
			pitchGlide = new Glide(context, 1.0f, 0);

			gain = new Gain(context, 1, volumeGlide = new Glide(context, 1.0f, 0));

			context.out.addInput(gain);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ShortFrameSegmenter getDetector(boolean low){

		ShortFrameSegmenter sfs = new ShortFrameSegmenter(context);

		sfs.setChunkSize(2048);
		sfs.setHopSize(441);
		sfs.addInput(player);

		FFT fft = new FFT();
		PowerSpectrum ps = new PowerSpectrum();

		sfs.addListener(fft);
		fft.addListener(ps);

		System.out.println(context.getSampleRate());

		SpectralDifference sd = new SpectralDifference(context.getSampleRate());
		//if(low)
			//sd.setFreqWindow(0, 300f);
		//else
		//	sd.setFreqWindow(301f, context.getSampleRate());
		ps.addListener(sd);

		PeakDetector beatDetector = new PeakDetector();

		sd.addListener(beatDetector);
		beatDetector.setNumberOfFeatures(1);
		beatDetector.setThreshold((low?0.6f:0.2f));
		beatDetector.setAlpha((low?0.9f:0.2f));

		beatDetector.addMessageListener(new Bead() {
			@Override
			protected void messageReceived(Bead arg0) {
				if(listener != null)
					if(low)
						listener.onBeatLow();
					else
						listener.onBeatHigh();
			}
		});

		return sfs;
	}

	public void glideVolume(float volume, float time){
		volumeGlide.setGlideTime(time);
		volumeGlide.setValue(volume);
	}

	public void setVolume(float volume){
		volumeGlide.setValueImmediately(volume);
	}

	public void glideSpeed(float speed, float time){
		speedGlide.setGlideTime(time);
		speedGlide.setValue(speed);
	}

	public void setSpeed(float speed){
		speedGlide.setValueImmediately(speed);
	}

	public void glidePitch(float pitch, float time){
		pitchGlide.setGlideTime(time);
		pitchGlide.setValue(pitch);
	}

	public void setPitch(float pitch){
		pitchGlide.setValueImmediately(pitch);
	}

	public int getPosition(){
		return (int) player.getPosition();
	}

	public int getLength(){
		return (int) sample.getLength();
	}

	public void setPosition(double milis){
		player.setPosition(milis);
	}

	public void play(){

		if(player == null) setup();

		player.start();
	}

	public void playRepeat(){

		play();

	}

	public void pause(){
		player.pause(true);
	}

	public boolean hasEnded(){
		return player == null || player.isDeleted();
	}

	public void setBeatListener(BeatListener listener){
		this.listener = listener;
	}

	public void stop(){

		player.reset();
		context.out.removeDependent(lowDetector);
		context.out.removeDependent(highDetector);
		player.removeAllConnections(gain);
		gain.removeAllConnections(player);
		player = null;

	}

	public void setup(){
		try {
			player = new GranularSamplePlayer(context, sample = new Sample(file.getAbsolutePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.setRate(speedGlide);
		player.setPitch(pitchGlide);
		player.setEnvelopeType(EnvelopeType.FINE);
		player.setKillOnEnd(false);

		context.out.addDependent(lowDetector = getDetector(true));
		context.out.addDependent(highDetector = getDetector(false));

		gain.addInput(player);
	}

}
 */