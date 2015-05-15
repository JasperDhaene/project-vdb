package main;

import car.RaceCar;
import control.Controller;
import control.RallyController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 * Loader for the project. Define your own controller, set username, track and manual control to launch a run.
 *
 * @author jnieland
 */
public class Main {

    public static void main(String[] args) {
        try {
            /**
             * Start a run with given settings: - username: used for statistics,
             * see http://ddcmstud.ugent.be/vagedb/scores.php for the leaderboard
             * trackname: possible values are: - spafrancorchamps1024 - silverstone1024 - interlagos1024 - texas1024
             * controller, written by you - manual control, only non-manual laps are recorded for the leaderboard
             */


            Controller controller = new RallyController();
            String trackname = "interlagos1024";

            RaceCar app = new RaceCar("DK Lightning McQueen", trackname, controller, false);

            app.setDisplayFps(false);
            app.setDisplayStatView(false);
            app.start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
