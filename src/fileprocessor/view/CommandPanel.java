package fileprocessor.view;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;

import fileprocessor.controller.CommandAPI;
import fileprocessor.model.ICommand;
import fileprocessor.model.MetaCommand;

public class CommandPanel extends JPanel implements Observer {

	private Map<MetaCommand, JButton> commandButtons;
	private Map<MetaCommand, JLabel> commandResults;
	private JButton clearBtn;
	private JCheckBox checkboxAutorun;
	private JPanel panel;
	private PolyFilesUI parent;
	private CommandAPI controller;
	private static final Object MUTEX_COMMANDS = new Object();

	/**
	 * Create the panel.
	 */
	public CommandPanel(PolyFilesUI parent) {
		this.parent = parent;
		commandButtons = new ConcurrentHashMap<>();
		commandResults = new ConcurrentHashMap<>();
		controller = new CommandAPI();
		controller.addObserver(this);

		Set<MetaCommand> commands = controller.getCommands();

		panel = new JPanel();
		
		clearBtn = new JButton("Réinitialiser");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearResults(e);
			}
		});
		
		checkboxAutorun = new JCheckBox("Exécution automatique");
		
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

		this.setLayout(groupLayout);
		updateCommands(controller.getCommands());
	}

	private void updateCommands(Set<MetaCommand> commands) {
		Set<MetaCommand> commandName;

		synchronized (MUTEX_COMMANDS) {
			commandName = commandButtons.keySet();
			for (MetaCommand s : commandName) {
				if (!commands.contains(s)) {
					deleteCommand(s);
				}
			}

			for (MetaCommand s : commands) {
				if (!commandName.contains(s)) {
					addCommand(s);
				}
			}
		}
	}

	private void addCommand(final MetaCommand s) {
		final JButton newButton = new JButton(s.getName());
		final JLabel newLabel = new JLabel("");

			commandButtons.put(s, newButton);
			commandResults.put(s, newLabel);


		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCommand(s);
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel.add(newButton);
				panel.add(newLabel);
				newLabel.setVisible(true);
				newButton.setVisible(true);
				panel.revalidate();
			}
		});
	}

	private void deleteCommand(final MetaCommand s) {
		final JLabel labelToDelete = commandResults.get(s);
		final JButton buttonToDelete = commandButtons.get(s);
		commandButtons.remove(s);
		commandResults.remove(s);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelToDelete.setVisible(false);
				buttonToDelete.setVisible(false);
				panel.remove(labelToDelete);
				panel.remove(buttonToDelete);
				panel.revalidate();
			}
		});
	}

	private void sendCommand(final MetaCommand commandName) {
		final JLabel textToUpdate = commandResults.get(commandName);
		try {
			controller.addCommandToQueue(commandName, this.parent.getFilePanel().getSelectedFile().getPath(), new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(o instanceof ICommand) {
						final ICommand command = (ICommand) o;
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {

								if(textToUpdate != null){
									if(command.getCodeResult().equals(ICommand.CommandCodeResult.SUCCESS)){
										textToUpdate.setText(command.getResult());
										textToUpdate.setForeground(Color.BLACK);
									}
									else{
										textToUpdate.setText(command.getResult());
										textToUpdate.setForeground(Color.RED);
									}
								}
							}
						});
					}
				}
			});
		} catch (Exception e) {
			textToUpdate.setText(e.getMessage());
			textToUpdate.setForeground(Color.RED);
		}
	}
	
	public void sendAllCommands() {
		for(MetaCommand commandName : commandButtons.keySet()) {
			this.sendCommand(commandName);
		}
	}
	
	public boolean autorunIsChecked() {
		return this.checkboxAutorun.isSelected();
	}

	private void clearResults(ActionEvent e) {
		for(MetaCommand m : commandResults.keySet()){
			final JLabel jLabel = commandResults.get(m);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					jLabel.setText("");
				}
			});
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof CommandAPI){
			updateCommands(controller.getCommands());
		}
	}
}
