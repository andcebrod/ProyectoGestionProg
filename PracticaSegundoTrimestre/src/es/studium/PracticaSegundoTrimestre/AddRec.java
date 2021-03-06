package es.studium.PracticaSegundoTrimestre;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddRec implements WindowListener, ActionListener{

	String driver = "com.mysql.jdbc.Driver";
	String url ="jdbc:mysql://localhost:3306/TallerJava?autoReconnect=true&useSSL=false";
	String login = "usuarioTaller";
	String password = "Studium2018;";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	
	String user;
	
	JFrame ventanaAddRec = new JFrame ("A�adir Recambio");
	JLabel lblDescripcionRec = new JLabel ("Descripci�n:");
	JLabel lblUnidadesRec = new JLabel ("Unidades:");
	JLabel lblPrecioRec = new JLabel ("Precio:");
	
	JTextField txtDescripcionRec = new JTextField(10);
	JTextField txtUnidadesRec = new JTextField(10);
	JTextField txtPrecioRec = new JTextField(10);
	

	JButton btnCrear = new JButton("Crear Recambio");
	JButton btnLimpiar = new JButton("Limpiar");

	
	JPanel pnlPanel = new JPanel();
	JPanel pnlPanel2 = new JPanel();
	JPanel pnlPanel3 = new JPanel();
	JPanel pnlPanel4 = new JPanel();

	
	public AddRec(String usuario) {
		user = usuario;
		ventanaAddRec.setLayout(new GridLayout(4,2));
		ventanaAddRec.setLocationRelativeTo(null);
		ventanaAddRec.setSize(400,300);
		
		pnlPanel.setLayout(new FlowLayout());
		pnlPanel2.setLayout(new FlowLayout());
		pnlPanel3.setLayout(new FlowLayout());
		pnlPanel4.setLayout(new FlowLayout());
		
		
		pnlPanel.add(lblDescripcionRec);
		pnlPanel.add(txtDescripcionRec);
		ventanaAddRec.add(pnlPanel);
		
		pnlPanel2.add(lblUnidadesRec);
		pnlPanel2.add(txtUnidadesRec);
		ventanaAddRec.add(pnlPanel2);
		
		pnlPanel3.add(lblPrecioRec);
		pnlPanel3.add(txtPrecioRec);
		ventanaAddRec.add(pnlPanel3);
		
		pnlPanel4.add(btnCrear);
		btnCrear.addActionListener(this);
		pnlPanel4.add(btnLimpiar);
		btnLimpiar.addActionListener(this);
		ventanaAddRec.add(pnlPanel4);
		ventanaAddRec.addWindowListener(this);
		ventanaAddRec.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		
		if (btnCrear.equals(ae.getSource())) {
			
			txtDescripcionRec.selectAll();
			String Descripcion = txtDescripcionRec.getText();
			txtUnidadesRec.selectAll();
			String Unidades = txtUnidadesRec.getText();
			txtPrecioRec.selectAll();
			String Precio = txtPrecioRec.getText();
			
			try
			{
				Class.forName(driver);
				String sentencia = "INSERT INTO recambios VALUES (null,'"+Descripcion+"', "+Unidades+", "+Precio+");";
				connection = DriverManager.getConnection(url, login,password);
				statement =connection.createStatement();
				statement.executeUpdate(sentencia);
				JOptionPane.showMessageDialog(null,"Recambio creado","Recambio Creado con �xito", JOptionPane.INFORMATION_MESSAGE);
				Calendar horaFecha = Calendar.getInstance();
				int hora,minutos,dia,mes,anyo;
				hora = horaFecha.get(Calendar.HOUR_OF_DAY);
				minutos = horaFecha.get(Calendar.MINUTE);
				dia = horaFecha.get(Calendar.DAY_OF_MONTH);
				mes = horaFecha.get(Calendar.MONTH)+1;
				anyo = horaFecha.get(Calendar.YEAR);
				try {
					FileWriter fw = new FileWriter("movimientos.log", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter outPut = new PrintWriter(bw);
					outPut.print("["+dia+"/"+mes+"/"+anyo+"]["+hora+":"+minutos+"] "+"["+user+"]"+"["+sentencia+"]"+"\n");
					outPut.close();
					bw.close();
					fw.close();
				} catch(IOException ioe) {
					System.out.print("Error");
				}
			}
			catch (ClassNotFoundException cnfe)
			{
				JOptionPane.showMessageDialog(null,"Error",cnfe.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			catch (SQLException sqle)
			{
				JOptionPane.showMessageDialog(null,"Error",sqle.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				try
				{
					if(connection!=null)
					{
						connection.close();
					}
				}
				catch (SQLException e)
				{
					JOptionPane.showMessageDialog(null,"Error",e.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
		} else if (btnLimpiar.equals(ae.getSource())) {
			txtDescripcionRec.selectAll();
			txtDescripcionRec.setText("");
			txtUnidadesRec.selectAll();
			txtUnidadesRec.setText("");
			txtPrecioRec.selectAll();
			txtPrecioRec.setText("");
			
		}
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowClosing(WindowEvent arg0) {
		
		if(ventanaAddRec.isActive()) {
			ventanaAddRec.setVisible(false);
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


}
