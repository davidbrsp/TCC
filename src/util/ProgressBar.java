package util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

// retirado de: http://javafree.uol.com.br/topic-859623-ProgressBar.html
public class ProgressBar extends JFrame {
    private static final long serialVersionUID = -3510692886926771783L;
    public JProgressBar pbar;
    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 100;

    public ProgressBar() {
	// JFrame frame = new JFrame
	super("Progress Bar Example");

	JPanel painel = new JPanel();
	pbar = new JProgressBar();
	pbar.setMinimum(MY_MINIMUM);
	pbar.setMaximum(MY_MAXIMUM);
	pbar.setStringPainted(true);
	painel.add(pbar);
	this.add(painel);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// this.setContentPane(pbar);
	this.setLocation(800, 300);
	this.setVisible(true);
	this.pack();
    }

    public void atualizaBarra(int valor) {
	pbar.setValue(valor);
	// pbar.repaint();
	// pbar.requestFocus();
	// this.repaint();
	// this.requestFocus();
    }

    public static void main(String args[]) {

	final ProgressBar barra = new ProgressBar();

	// JFrame frame = new JFrame("Progress Bar Example");
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// frame.setContentPane(barra);
	// frame.pack();
	// frame.setVisible(true);
	// frame.setLocation(400, 300);

	for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
	    final int percent = i;
	    try {
		SwingUtilities.invokeAndWait(new Runnable() {
		    public void run() {
			barra.atualizaBarra(percent);
		    }
		});
		Thread.sleep(0);
	    } catch (InterruptedException e) {
		;
	    } catch (InvocationTargetException ite) {
		;
	    }
	}
    }
}
