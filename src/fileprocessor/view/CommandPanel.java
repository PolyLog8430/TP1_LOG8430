package fileprocessor.view;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;
import fileprocessor.controller.CommandAPI;

public class CommandPanel extends JPanel {
	
	private ArrayList<JButton> commandButtons;
	private ArrayList<JLabel> commandResults;
	private JButton clearBtn;
	private JCheckBox checkboxAutorun;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public CommandPanel() {
		
		commandButtons = new ArrayList<JButton>();
		commandResults = new ArrayList<JLabel>();
		/*
		ArrayList<String> commands = CommandAPI.getInstance().getCommands();
		for(String command : commands) {
			commandButtons.add(new JButton(command));
			commandResults.add(new JLabel(""));
		}
		*/
		commandButtons.add(new JButton("test1"));
		commandResults.add(new JLabel("this is a test label"));
		commandButtons.add(new JButton("test2"));
		commandResults.add(new JLabel("a result here"));
		commandButtons.add(new JButton("test3"));
		commandResults.add(new JLabel("ohai"));
		
		JPanel panel = new JPanel();
		clearBtn = new JButton("Réinitialiser");
		JCheckBox checkboxAutorun = new JCheckBox("Exécution automatique");
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(clearBtn, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
							.addComponent(checkboxAutorun)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(clearBtn, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(checkboxAutorun))
					.addContainerGap())
		);
		panel.setLayout(new GridLayout(0, 2, 10, 10));
		for(int i=0; i<commandButtons.size(); ++i) {
			panel.add(commandButtons.get(i));
			panel.add(commandResults.get(i));
		}
		
		this.setLayout(groupLayout);
	}
}
