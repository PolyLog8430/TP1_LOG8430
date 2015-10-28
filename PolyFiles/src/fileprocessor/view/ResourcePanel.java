package fileprocessor.view;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class ResourcePanel extends JPanel {
	
	private JTree tree;
	private FileTreeModel treeModel;
	private JScrollPane scroll;
	private JButton btnFilePicker;
	private JFileChooser filePicker;
	private File root = null;
	private File selectedFile = null;
	private PolyFilesUI parent;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public ResourcePanel(PolyFilesUI parent) {
		this.parent = parent;
		
		filePicker = new JFileChooser();
		filePicker.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JPanel localResourcePanel = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Ressource locale</body></html>", null, localResourcePanel, null);
		
		tree = new JTree();
		tree.setModel(new DefaultTreeModel(null));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if(path != null) {
					System.out.println(path.getLastPathComponent().toString());
					setSelectedFile(new File(path.getLastPathComponent().toString()));
				}
			}
		});
		scroll = new JScrollPane(tree);
		
		btnFilePicker = new JButton("Choisir un répertoire ou un fichier");
		GroupLayout gl_localResourcePanel = new GroupLayout(localResourcePanel);
		gl_localResourcePanel.setHorizontalGroup(
			gl_localResourcePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_localResourcePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_localResourcePanel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnFilePicker, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
						.addComponent(scroll, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_localResourcePanel.setVerticalGroup(
			gl_localResourcePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_localResourcePanel.createSequentialGroup()
					.addGap(5)
					.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnFilePicker, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addGap(6))
		);
		localResourcePanel.setLayout(gl_localResourcePanel);
		
		JPanel externalResourcePanel = new JPanel();
		tabbedPane.addTab("<html><body leftmargin=15 topmargin=8 marginwidth=15 marginheight=5>Ressource externe</body></html>", null, externalResourcePanel, null);
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblEntrezLuriDe = new JLabel("Entrez l'URI de la ressource externe :");
		GroupLayout gl_externalResourcePanel = new GroupLayout(externalResourcePanel);
		gl_externalResourcePanel.setHorizontalGroup(
			gl_externalResourcePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_externalResourcePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_externalResourcePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_externalResourcePanel.createSequentialGroup()
							.addComponent(lblEntrezLuriDe, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
							.addGap(217))
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_externalResourcePanel.setVerticalGroup(
			gl_externalResourcePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_externalResourcePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEntrezLuriDe, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(177, Short.MAX_VALUE))
		);
		externalResourcePanel.setLayout(gl_externalResourcePanel);
		btnFilePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFilePicker(e);
			}
		});
		setLayout(groupLayout);
	}
	
	private void openFilePicker(ActionEvent e) {
		if(filePicker.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.setRoot(filePicker.getSelectedFile());
		}
	}
	
	private void setRoot(File root) {
		this.root = root;
		this.updateTree();
	}
	
	private void updateTree() {
		if(this.root != null) {
			this.treeModel = new FileTreeModel(this.root);
			this.tree.setModel(this.treeModel);
		}
	}
	
	private void setSelectedFile(File file) {
		this.selectedFile = file;
		this.parent.getCommandPanel().clearResults();
		this.parent.getCommandPanel().updateEnableButtons();
		if(this.parent.getCommandPanel().autorunIsChecked()) {
			this.parent.getCommandPanel().sendAllCommands();
		}
	}
	
	public File getSelectedFile() {
		return this.selectedFile;
	}
}

class FileTreeModel implements TreeModel {
	
	private File root;
	
	public FileTreeModel(File root) {
		this.root = root;
	}

	@Override
	public Object getRoot() {
		return this.root;
	}

	@Override
	public Object getChild(Object parent, int index) {
		String[] children = ((File)parent).list();
		if ((children == null) || (index >= children.length)) {
			return null;
		}
		else {
			return new File((File) parent, children[index]);
		}
	}

	@Override
	public int getChildCount(Object parent) {
		String[] children = ((File)parent).list();
	    if (children == null) {
	    	return 0;
	    }
	    else {
	    	return children.length;
	    }
	}

	@Override
	public boolean isLeaf(Object node) {
		return ((File)node).isFile();
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		String[] children = ((File)parent).list();
	    if (children == null) {
	    	return -1;
	    }
	    String childname = ((File)child).getName();
	    for(int i=0; i<children.length; ++i) {
	      if (childname.equals(children[i])) {
	    	  return i;
	      }
	    }
	    return -1;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		
	}
	
	
	
}
