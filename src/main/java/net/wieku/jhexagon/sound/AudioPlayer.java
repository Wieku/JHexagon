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
	boolean paused;
	private boolean playing;
	
	static {
		context = new AudioContext(2048);
		context.start();
	}
	
	public AudioPlayer(File file) throws FileNotFoundException{
		if(!file.exists()) throw new FileNotFoundException(file.getAbsolutePath());
		
		try {
			player = new GranularSamplePlayer(context, sample = new Sample(file.getAbsolutePath(), Gdx.files.absolute(file.getAbsolutePath()).extension().equals("ogg")?true:false));

			player.setRate(speedGlide = new Glide(context, 1.0f, 0));
			player.setPitch(pitchGlide = new Glide(context, 1.0f, 0));
			player.setEnvelopeType(EnvelopeType.FINE);

			Gain gain = new Gain(context, 1, volumeGlide = new Glide(context, 1.0f, 0));

			gain.addInput(player);
			context.out.addDependent(getDetector(true));
			context.out.addDependent(getDetector(false));
			
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
		pitchGlide.setValue(pitch);
	}
	
	public int getPosition(){
		return (int) player.getPosition();
	}
	
	public int getLength(){
		return (int) sample.getLength();
	}
	
	public void setPosition(int milis){
		player.setPosition(milis);
	}
	
	public void play(){
		
		if(player.isDeleted()){
			player = new GranularSamplePlayer(context, sample);
		}
		
		if(!player.isPaused()){
			if(!playing){
				player.start(0);
			}
				playing = true;
		} else {
			player.pause(false);
		}
	}

	public void playRepeat(){

		if(player.isDeleted()){
			player = new GranularSamplePlayer(context, sample);
			player.setLoopType(LoopType.LOOP_FORWARDS);
			player.setLoopEnd(new Static(context, (float)player.getSample().getLength()));
			player.setKillOnEnd(false);
		}

		if(!player.isPaused()){
			if(!playing){
				player.start(0);
			}
			playing = true;
		} else {
			player.pause(false);
		}
	}

	public void pause(){
		player.pause(!(playing = false));
	}
	
	public boolean hasEnded(){
		return player.isDeleted();
	}
	
	public void setBeatListener(BeatListener listener){
		this.listener = listener;
	}
	
	public void stop(){
		playing = false;
		player.reset();
	}
	
	public void close(){
		player.kill();
	}
	
}

