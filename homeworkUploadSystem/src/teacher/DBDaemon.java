package teacher;

import java.awt.event.*;
import javax.swing.*;

public class DBDaemon {
	private static DBDaemon uniqueInstance = null;

	public static DBDaemon instance(int DELAY) {
		if (uniqueInstance == null)
			uniqueInstance = new DBDaemon(DELAY);
		return uniqueInstance;
	}

	private DBDaemon(int DELAY) {
		Timer timer = new Timer(DELAY, new ShotTimerListener());
		timer.start();
		check();
	}

	public void check() {
		System.out.println("OK");
	}

	private class ShotTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			check();
		}
	}
}