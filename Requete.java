
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the templaSSte in the editor.
 */

/**
 *
 * @author milie
 */
public class Requete extends javax.swing.JFrame {
	boolean autocommit = true;

    private final Connection connection;
    int row_count= 0; //nb de lignes à afficher
    int line = 1;
    public void go() {
		if(! requetezone.getText().equals("")) {
			try {	
				executeStatement(requetezone.getText());
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
			}
			finally{reset();}
		}
		reset();
	}

	////////////////////////// Code à revoir //////////////////////////
	// Commit
	public void commit() {
		try {
			connection.commit();
			System.out.println("Commit r�ussi");
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{reset();}
	}

	// Active ou desactive le mode autocommit
	public void autocommit() {
		try {
			connection.setAutoCommit(! autocommit);
			autocommit = ! autocommit;
			System.out.println("Mode autocommit : "+autocommit);
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{reset();}
	}

	/* Action du bouton reset

	et aussi un bouton pour lancer reset
	et la c'est toi qui choisis ce que fait ton reset
	fais des choix simples genre vider l'espace des requetes uniquement*/

	// Rollback
	public void rollback() {
		try {
			connection.rollback();
			System.out.println("Rollback r�ussi");
		}
		catch(SQLException e) {System.out.println("Erreur : "+e.getMessage());}
		finally{reset();}
	}
	////////////////////////// Fin code à revoir //////////////////////////

	public Requete (Connection connection){
		initComponents();
		this.connection = connection;
		setVisible(true);

		go.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				go();
			}
		});
		commit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				commit();
			}
		});

		rollback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				rollback();
			}
		});



	}
    public void reset() {
		requetezone.setText(" ");
		line = 1;
	}

    /**
     *
     * @param sql
     * @throws SQLException
     */
    public void executeStatement(String sql) throws SQLException {
			System.out.println("Exec "+sql);
			Statement statement = null;
			try {
				statement = connection.createStatement();
				if(statement.execute(sql)) { // true si retourne un ResultSet
					processResults(statement.getResultSet(), afficheField);
					if (sql.contains("insert") || sql.contains("delete") ) {
						String table = statement.getResultSet().getMetaData().getTableName(1);
						executeStatement("SELECT * FROM " + table);
						System.out.println("SELECT * FROM " + table);
					}
				}

			}
			catch(SQLException e) {
				throw e;
			}
			finally {
				if(statement != null) statement.close();
			}
		}
	public void processResults(ResultSet rs, JTable table) throws SQLException {
		try {
			DefaultTableModel tableModel;
			tableModel = new DefaultTableModel();

			//Retrieve meta data from ResultSet
			ResultSetMetaData metaData = rs.getMetaData();

			//Get number of columns from meta data
			int columnCount = metaData.getColumnCount();

			//Get all column names from meta data and add columns to table model
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
				tableModel.addColumn(metaData.getColumnLabel(columnIndex));
			}

			//Create array of Objects with size of column count from meta data
			Object[] row = new Object[columnCount];

			//Scroll through result set
			while (rs.next()){
				//Get object from column with specific index of result set to array of objects
				for (int i = 0; i < columnCount; i++){
					row[i] = rs.getObject(i+1);
				}
				//Now add row to table model with that array of objects as an argument
				tableModel.addRow(row);
			}

			//Now add that table model to your table and you are done :D
			table.setModel(tableModel);
			tableModel.fireTableDataChanged();

		}
		catch(SQLException e) {throw e;}
		finally{
			try{rs.close(); }
			catch(SQLException e) {}
		}

	}







    private void initComponents() {

        Requetepanel = new javax.swing.JPanel();
        go = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        requetezone = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        afficheField = new javax.swing.JTable();
		commit = new javax.swing.JButton();
		rollback = new javax.swing.JButton();
		reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 500));

        go.setText("Requete");
		commit.setText("Commit");
		rollback.setText("Rollback");

		commit.setBounds(350, 300, 150, 30);
		go.setBounds(300, 300, 150, 30);
		rollback.setBounds(400, 300, 150, 30);



        requetezone.setColumns(20);
        requetezone.setRows(5);
        jScrollPane1.setViewportView(requetezone);


        jScrollPane2.setViewportView(afficheField);

        javax.swing.GroupLayout RequetepanelLayout = new javax.swing.GroupLayout(Requetepanel);
        Requetepanel.setLayout(RequetepanelLayout);
        RequetepanelLayout.setHorizontalGroup(
            RequetepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RequetepanelLayout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(go)
				.addComponent(commit)
				.addComponent(rollback)
                .addContainerGap(261, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
        );
        RequetepanelLayout.setVerticalGroup(
            RequetepanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RequetepanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addComponent(go)
				.addComponent(commit)
				.addComponent(rollback)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Requetepanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Requetepanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );


        pack();
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration
    private javax.swing.JPanel Requetepanel;
    private javax.swing.JButton go;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable afficheField;
    private javax.swing.JTextArea requetezone;
	private javax.swing.JButton rollback;
	private javax.swing.JButton commit;

	private javax.swing.JButton reset;
    // End of variables declaration
}
