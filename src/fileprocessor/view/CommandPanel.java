package fileprocessor.view;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fileprocessor.controller.CommandAPI;
import fileprocessor.model.ICommand;

import static org.junit.Assert.assertEquals;

public class CommandPanel extends JPanel {
	
	private ArrayList<JButton> commandButtons;
	private ArrayList<JLabel> commandResults;
	private JButton clearBtn;
	private JCheckBox checkboxAutorun;
	private JPanel panel;
	private PolyFilesUI parent;

	/**
	 * Create the panel.
	 */
	public CommandPanel(PolyFilesUI parent) {
		this.parent = parent;
		commandButtons = new ArrayList<JButton>();
		commandResults = new ArrayList<JLabel>();
		
		ArrayList<String> commands = CommandAPI.getInstance().getCommands();
		for(String command : commands) {
			commandButtons.add(new JButton(command));
			commandResults.add(new JLabel(""));
		}
		
		panel = new JPanel();
		
		clearBtn = new JButton("R�initialiser");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearResults(e);
			}
		});
		
		checkboxAutorun = new JCheckBox("Ex�cution automatique");
		
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
			commandButtons.get(i).addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sendCommand(((JButton)(e.getSource())).getText());
				}
			});
		}
		
		this.setLayout(groupLayout);
	}
	
	private void sendCommand(final String commandName) {
		try {
			CommandAPI.getInstance().addCommandToQueue(commandName, this.parent.getFilePanel().getSelectedFile().getPath(), new Observer() {
                @Override
                public void update(Observable o, Object arg) {
					if(o instanceof ICommand){
						ICommand command = (ICommand) o;
						getResultLabelForCommand(commandName).setText(command.getResult());
					}
                }
            });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendAllCommands() {
		for(JButton commandBtn : this.commandButtons) {
			this.sendCommand(commandBtn.getText());
		}
	}
	
	public boolean autorunIsChecked() {
		return this.checkboxAutorun.isSelected();
	}
	
	private JLabel getResultLabelForCommand(String commandName) {
		JLabel resultLabel = null;
		for(int i=0; i<commandButtons.size(); ++i) {
			if(commandButtons.get(i).getText().equals(commandName)) {
				resultLabel = commandResults.get(i);
				break;
			}
		}
		return resultLabel;
	}
	
	private void clearResults(ActionEvent e) {
		for(JLabel label : commandResults) {
			label.setText("");
		}
	}
}
