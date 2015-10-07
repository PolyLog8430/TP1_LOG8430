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

public class FilePanel extends JPanel {
	
	private JTree tree;
	private FileTreeModel treeModel;
	private JScrollPane scroll;
	private JButton btnFilePicker;
	private JFileChooser filePicker;
	private File root = null;
	private File selectedFile = null;
	private PolyFilesUI parent;

	/**
	 * Create the panel.
	 */
	public FilePanel(PolyFilesUI parent) {
		this.parent = parent;
		
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
		
		filePicker = new JFileChooser();
		filePicker.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		btnFilePicker = new JButton("Choisir un répertoire ou un fichier");
		btnFilePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFilePicker(e);
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnFilePicker, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addComponent(scroll, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnFilePicker, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		this.setLayout(groupLayout);
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
