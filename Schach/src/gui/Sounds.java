package gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Convenience Class containing all sounds used by view and a convenience function to play them
 */
class Sounds {

    static  final Media FIGURE_MOVED = new Media(Sounds.class.getResource("sounds/figure_move.wav").toString());
    static  final Media FIGURE_BEAT = new Media(Sounds.class.getResource("sounds/figure_beat.wav").toString());
    static  final Media GAME_FINISHED = new Media(Sounds.class.getResource("sounds/game_finished.wav").toString());
    static  final Media PAWN_UPGRADE = new Media(Sounds.class.getResource("sounds/peasant_upgrade.wav").toString());
    static  final Media CHECK_ALARM = new Media(Sounds.class.getResource("sounds/check_alarm.wav").toString());

    /**
     * Plays a sound
     * @param media The sound to be played by the function
     */
    static void playSound(Media media){
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}
