package fileprocessor.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;

public class PolyFilesUI {

	private JFrame frame;
	private ResourcePanel resourcePanel;
	private CommandPanel commandPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PolyFilesUI window = new PolyFilesUI();
					window.frame.setTitle("PolyFichiers");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PolyFilesUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(0, 0, 1024, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		resourcePanel = new ResourcePanel(this);
		frame.getContentPane().add(resourcePanel);
		
		commandPanel = new CommandPanel(this);
		frame.getContentPane().add(commandPanel);
		
	}
	
	public ResourcePanel getResourcePanel() {
		return this.resourcePanel;
	}
	
	public CommandPanel getCommandPanel() {
		return this.commandPanel;
	}
}
