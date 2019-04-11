package es.studium.PracticaSegundoTrimestre;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ModCli implements WindowListener, ActionListener, TextListener{
	JFrame ventanaModCli = new JFrame ("Modificar cliente:");
	JLabel lblNombreCli = new JLabel ("Nombre:");
	JLabel lblDireccionCli = new JLabel ("Direcci�n:");
	JLabel lblTelefonoCli = new JLabel ("Tel�fono:");

	JTextField txtNombreCli = new JTextField(15);
	JTextField txtDireccionCli = new JTextField(15);
	JTextField txtTelefonoCli = new JTextField(15);

	JButton btnCrear = new JButton("Modificar Cliente");
	JButton btnLimpiar = new JButton("Limpiar");

	JPanel pnlPanel = new JPanel();
	JPanel pnlPanel2 = new JPanel();
	JPanel pnlPanel3 = new JPanel();
	JPanel pnlPanel4 = new JPanel();
	int idCli = 0;

	public ModCli(int id) 
	{
		idCli = id;
		ResultSet rs = ejecutarSelect("SELECT * FROM clientes",conectar("TallerJava","root","Studium2018;"));
		try {
			rs.next();
			txtNombreCli.selectAll();
			txtNombreCli.setText(rs.getString("nombreCliente"));
			txtDireccionCli.selectAll();
			txtDireccionCli.setText(rs.getString("DireccionCliente"));
			txtTelefonoCli.selectAll();
			txtTelefonoCli.setText(Integer.toString(rs.getInt("telefonoCliente")));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		desconectar(conectar("TallerJava","root","Studium2018;"));

		ventanaModCli.setLayout(new GridLayout(4,2));
		ventanaModCli.setLocationRelativeTo(null);
		ventanaModCli.setSize(400,300);

		pnlPanel.setLayout(new FlowLayout());
		pnlPanel2.setLayout(new FlowLayout());
		pnlPanel3.setLayout(new FlowLayout());
		pnlPanel4.setLayout(new FlowLayout());

		pnlPanel.add(lblNombreCli);
		pnlPanel.add(txtNombreCli);
		ventanaModCli.add(pnlPanel);

		pnlPanel2.add(lblDireccionCli);
		pnlPanel2.add(txtDireccionCli);
		ventanaModCli.add(pnlPanel2);

		pnlPanel3.add(lblTelefonoCli);
		pnlPanel3.add(txtTelefonoCli);
		ventanaModCli.add(pnlPanel3);

		pnlPanel4.add(btnCrear);
		btnCrear.addActionListener(this);
		pnlPanel4.add(btnLimpiar);
		btnLimpiar.addActionListener(this);
		ventanaModCli.add(pnlPanel4);

		ventanaModCli.addWindowListener(this);
		ventanaModCli.setVisible(true);

	}
	public static void main(String[] args) {

	}
	@Override
	public void textValueChanged(TextEvent e) {}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{

		if (btnCrear.equals(ae.getSource())) {

			if(txtNombreCli.getText().equals("")) {
				JOptionPane.showMessageDialog(null,"Error, Nombre de Cliente Vac�o","Nombre vac�o", JOptionPane.ERROR_MESSAGE);

			} else {
				ejecutarIDA("UPDATE clientes SET nombreCliente = '"+txtNombreCli.getText()+"', direccionCliente = '"+txtDireccionCli.getText()+"', telefonoCliente ="+txtTelefonoCli.getText()+" WHERE idCliente ="+idCli+";",conectar("TallerJava","root","Studium2018;"));
				desconectar(conectar("TallerJava","root","Studium2018;"));
			}

		} else if (btnLimpiar.equals(ae.getSource())) {
			txtNombreCli.selectAll();
			txtNombreCli.setText("");
			txtDireccionCli.selectAll();
			txtDireccionCli.setText("");
			txtTelefonoCli.selectAll();
			txtTelefonoCli.setText("");
		}

	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {

		if(ventanaModCli.isActive()) {
			ventanaModCli.setVisible(false);
		}else {
			//System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}

	public Connection conectar(String baseDatos, String usuario, String clave)
	{
		String driver = "com.mysql.jdbc.Driver";
		String url ="jdbc:mysql://localhost:3306/"+baseDatos+"?autoReconnect=true&useSSL=false";
		String login = usuario;
		String password = clave;
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, login,password);
		}
		catch (ClassNotFoundException cnfe)
		{
			JOptionPane.showMessageDialog(null,cnfe.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
		return connection;
	}

	public void desconectar(Connection c) 
	{
		try
		{
			if(c!=null)
			{
				c.close();
			}
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public ResultSet ejecutarSelect(String sentencia, Connection c) 
	{

		try
		{
			Statement statement = c.createStatement();
			ResultSet rs= statement.executeQuery(sentencia);
			return rs;
		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

	}

	public void ejecutarIDA(String sentencia, Connection c) 
	{
		try
		{
			Statement statement = c.createStatement();
			statement.executeUpdate(sentencia);
			JOptionPane.showMessageDialog(null,"Cliente modificado","Cliente modificado con �xito", JOptionPane.INFORMATION_MESSAGE);

		}
		catch(SQLException e)
		{
			JOptionPane.showMessageDialog(null,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
		}

	}
}


