package fileprocessor.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;

public class PolyFilesUI {

	private JFrame frame;
	private FilePanel filePanel;
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
		frame.setBounds(100, 100, 762, 293);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		filePanel = new FilePanel(this);
		frame.getContentPane().add(filePanel);
		
		commandPanel = new CommandPanel(this);
		frame.getContentPane().add(commandPanel);
		
	}
	
	public FilePanel getFilePanel() {
		return this.filePanel;
	}
	
	public CommandPanel getCommandPanel() {
		return this.commandPanel;
	}
}
