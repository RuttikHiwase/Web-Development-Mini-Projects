package appointment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ServerHealthBuddy.ServerHealthBuddy.Converter;

public class AppointmentController {

	
	public String getDoctorList() {
		String result = "Data not Available";
	      try {
	    	  
	    	  Class.forName("com.mysql.jdbc.Driver");
			  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
			  Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	  	      ResultSet rs = stmt.executeQuery("SELECT id, doctor_name, doctor_address, hospital_name FROM doctor_details where is_verified = 1;");

	  	    if(rs.next()){
  				rs.previous();
  				JSONArray array = Converter.convertToJSON(rs);
  				result = array.toString();
  			}else {
  				result = "Invalid UserName or Password";
  			}
			  
	      }catch (Exception e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		  	}
		
		return result;
	}
	
	public String getUserAppointmentList(String data) {
		String result = "Data not Available";
	      try {
	    	  JSONObject json = new JSONObject(data);
		  		String user_id = json.getString("user_id");
		  		
	    	  Class.forName("com.mysql.jdbc.Driver");
			  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
			  Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	  	      ResultSet rs = stmt.executeQuery("SELECT p.*, s.doctor_name, s.doctor_mobile, s.doctor_address, s.doctor_speciality, s.hospital_name FROM appointment_details AS p INNER JOIN doctor_details as s ON p.doctor_id = s.id WHERE user_id ="+ user_id+ " group by p.appointment_id order by p.appointment_date DESC;");
	  	    if(rs.next()){
  				rs.previous();
  				JSONArray array = Converter.convertToJSON(rs);
  				result = array.toString();
  			}else {
  				result = "Invalid UserName or Password";
  			}
			  
	      }catch (Exception e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		  	}
		
		return result;
	}
	
	
	public String getDoctorAppointmentList(String data) {
		String result = "Data not Available";
	      try {
	    	    JSONObject json = new JSONObject(data);
		  		String doctor_id = json.getString("doctor_id");
		  	//	String doctor_id = "2";
		  		
	    	  Class.forName("com.mysql.jdbc.Driver");
			  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
			  Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	  	      ResultSet rs = stmt.executeQuery("SELECT p.*, s.user_name, s.user_mobile, s.user_address, s.user_email FROM healthbuddy.appointment_details AS p INNER JOIN healthbuddy.user_details as s ON p.user_id = s.id WHERE doctor_id ="+ doctor_id+ " group by p.appointment_id order by p.appointment_date DESC;");

	  	    if(rs.next()){
  				rs.previous();
  				JSONArray array = Converter.convertToJSON(rs);
  				result = array.toString();
  			}
			  
	      }catch (Exception e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		  	}
		
		return result;
	}

 public String scheduleAppointment(String data) {
		String output = "Error Scheduling Appointment. Please try later";
		try {
			  Class.forName("com.mysql.jdbc.Driver");
			  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
			  Statement stmt = null;
		      stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		      
	  	      ResultSet rs = stmt.executeQuery("SELECT id FROM appointment_details order by id desc limit 1;");
	  	      int appointment_id = 1;
	  			if(rs.next()){
	  				appointment_id = rs.getInt("id");
	  				appointment_id = appointment_id +1;
	  			}
		      
	  			
		      JSONObject json = new JSONObject(data);
			  String appointment_id_String = "APP0"+appointment_id;
			  String doctor_id = json.getString("doctor_id");
			  String user_id = json.getString("user_id");
			  String appointment_date = json.getString("appointment_date");
			  String appointment_mode = json.getString("appointment_mode");
			  String appointment_status = "Pending";
			  
		      String insertAppointment = "INSERT INTO `appointment_details`\r\n"
		      		+ "(`appointment_id`,\r\n"
		      		+ "`doctor_id`,\r\n"
		      		+ "`user_id`,\r\n"
		      		+ "`appointment_date`,\r\n"
		      		+ "`appointment_mode`,\r\n"
		      		+ "`appointment_status`)\r\n"
		      		+ "VALUES\r\n"
		      		+ "(\r\n"
		      		+ "'"+appointment_id_String+"',\r\n"
		      		+ "'"+doctor_id+"',\r\n"
		      		+ "'"+user_id+"',\r\n"
		      		+ "'"+appointment_date+"',\r\n"
		      		+ "'"+appointment_mode+"',\r\n"
		      		+ "'"+appointment_status+"')";
		      stmt.executeUpdate(insertAppointment);
			  output = "Appointment Scheduled Successfully";

			  } catch (SQLException e) {
				  e.printStackTrace();
			  System.out.println("Error while connecting to the database");
			} 
			  catch (ClassNotFoundException e) { // TODO Auto-generated catch block 
				  e.printStackTrace(); 
				  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return output;
	 
 }


public String acceptAppointment(String data) {
	String output = "Error Updating Appointment. Please try later";
	try {
		  Class.forName("com.mysql.jdbc.Driver");
		  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
		  Statement stmt = null;
	      stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	      
	      JSONObject json = new JSONObject(data);
		  String appointment_id = json.getString("appointment_id");
		  
	      String updateUser = "UPDATE appointment_details SET `appointment_status` = 'Confirmed' WHERE (`appointment_id` = '"+appointment_id+"');";
	      stmt.executeUpdate(updateUser);
		  output = "Appointment Accepted";

		  } catch (SQLException e) {
			  e.printStackTrace();
		  System.out.println("Error while connecting to the database");
		} 
		  catch (ClassNotFoundException e) { // TODO Auto-generated catch block 
			  e.printStackTrace(); 
			  } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	return output;
}

public String rejectAppointment(String data) {
	String output = "Error Rejecting Appointment. Please try later";
	try {
		  Class.forName("com.mysql.jdbc.Driver");
		  Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthbuddy", "root", "root");//Establishing connection
		  Statement stmt = null;
	      stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	      
	      JSONObject json = new JSONObject(data);
		  String appointment_id = json.getString("appointment_id");
		  String rejection_reason = json.getString("rejection_reason");

		  
	      String updateUser = "UPDATE appointment_details SET `appointment_status` = 'Rejected', `rejection_reason` = '"+rejection_reason +"' WHERE (`appointment_id` = '"+appointment_id+"');";
	      stmt.executeUpdate(updateUser);
		  output = "Appointment Rejected";

		  } catch (SQLException e) {
			  e.printStackTrace();
		  System.out.println("Error while connecting to the database");
		} 
		  catch (ClassNotFoundException e) { // TODO Auto-generated catch block 
			  e.printStackTrace(); 
			  } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	return output;
}
}
