package panda.tool.esort;

import panda.util.progressbar.ConsoleGaugeBar;
import panda.util.progressbar.ConsolePercentBar;
import panda.util.progressbar.ConsoleRemainTimeBar;
import panda.util.progressbar.ConsoleWindmillBar;

/**
 * @author fsoft
 */
public class Main {

	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		try {
			ConsolePercentBar p = new ConsolePercentBar();
			p.start();
			for (int i = 0; i <= 100; i++) {
				Thread.sleep(10);
				p.setValue(i);
			}
			p.stop();
			System.out.println();

			ConsoleGaugeBar g = new ConsoleGaugeBar();
			g.start();
			for (int i = 0; i <= 100; i++) {
				Thread.sleep(10);
				g.setValue(i);
			}
			g.stop();
			System.out.println();
			
			g = new ConsoleGaugeBar();
			g.setDrawPercent(true);
			g.start();
			for (int i = 0; i <= 100; i++) {
				Thread.sleep(10);
				g.setValue(i);
			}
			g.stop();
			System.out.println();
			
			ConsoleWindmillBar w = new ConsoleWindmillBar();
			w.start();
			Thread.sleep(3000);
			w.stop();
			System.out.println();
			
			System.out.print("Remain time: ");
			ConsoleRemainTimeBar r = new ConsoleRemainTimeBar();
			r.start();
			for (int i = 0; i <= 100; i++) {
				Thread.sleep(300);
				r.setValue(i);
			}
			r.stop();
			System.out.println();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
