
import java.io.File;
import javax.sound.sampled.*;

public class SoundManager {

    private static Clip backgroundClip;

    //播放一次性音效
    public static void playSoundEffect(String path, float volumeDb) {
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(volumeDb); // 直接用參數控制音量
                }
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    /**
     * 播放背景音樂（會自動 loop）
     */
    public static void playBackgroundMusic(String path, float volumeDb) {
        stopBackgroundMusic(); // 先停止舊的背景音樂
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(ais);
                if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(volumeDb);
                }
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // 循環播放
                backgroundClip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 停止背景音樂
     */
    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    /**
     * 播放一次性音效
     */
    public static void playSoundEffect(String path) {
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(path));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(-10f); // 可視需求調整，單位為 dB
                }

                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start(); // 開新執行緒，避免阻塞
    }

}
