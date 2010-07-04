
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class AudioEffect extends Thread {
 
    public String file_name;
    private boolean circle_play = false;
    public AudioEffect(String wavfile_name) {
    	file_name = wavfile_name;
    }
    
    public void set_Circle_Play(boolean is_circle_play){
    	circle_play = is_circle_play;
    }
    
    public void run() {
        //File soundFile = new File( file_name );
        try {
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(file_name));
        	if(circle_play){
        		while(true)
        			playAudioStream( audioInputStream );
        	}
        	else
        		playAudioStream( audioInputStream );
        } catch ( Exception e ) { 
        	e.printStackTrace();
        }
    } 
    // playAudioFile
    public void playAudioStream( AudioInputStream audioInputStream ) {
        AudioFormat audioFormat = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat );
        if ( !AudioSystem.isLineSupported( info ) ) {
        	return;
        }
        try {
	        SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine( info );
	        dataLine.open( audioFormat );
	        if( dataLine.isControlSupported( FloatControl.Type.MASTER_GAIN ) ) {
	        	FloatControl volume = (FloatControl) dataLine.getControl( FloatControl.Type.MASTER_GAIN );
	        	volume.setValue( 0.5F );
	        }
	        	dataLine.start();
	        int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
	        byte [] buffer = new byte[ bufferSize ];
	        try {
		        int bytesRead = 0;
		        while ( bytesRead >= 0 ) {
			        bytesRead = audioInputStream.read( buffer, 0, buffer.length );
			        if ( bytesRead >= 0 ) {
			        	int framesWritten = dataLine.write( buffer, 0, bytesRead );
			        }
		        } // while
	        } catch ( IOException e ) {
	        e.printStackTrace();
	        }
	        dataLine.drain();
	        dataLine.close();
        } 
        catch ( LineUnavailableException e ) {
        	e.printStackTrace();
        }
    } 
}
