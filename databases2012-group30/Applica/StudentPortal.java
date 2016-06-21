import java.sql.*; // JDBC stuff.
import java.io.*; // Reading user input.

public class StudentPortal {
    /*
     * This is the driving engine of the program. It parses the command-line
     * arguments and calls the appropriate methods in the other classes.
     *
     * You should edit this file in two ways: 1) Insert your database username
     * and password (no @medic1!) in the proper places. 2) Implement the three
     * functions getInformation, registerStudent and unregisterStudent.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                String url = "jdbc:oracle:thin:@tycho.ita.chalmers.se:1521/kingu.ita.chalmers.se";
                String userName = "vtda357_030"; // Your username goes here!
                String password = "vtda357_030"; // Your password goes here!
                Connection conn = DriverManager.getConnection(url, userName,
                        password);

                String student = args[0]; // This is the identifier for the
                                            // student.
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(System.in));
                System.out.println("Welcome!");
                while (true) {
                    System.out.println("Please choose a mode of operation:");
                    System.out.print("? > ");
                    String mode = input.readLine();
                    if ((new String("information")).startsWith(mode
                            .toLowerCase())) {
                        /* Information mode */
                        getInformation(conn, student);
                    } else if ((new String("register")).startsWith(mode
                            .toLowerCase())) {
                        /* Register student mode */
                        System.out.print("Register for what course? > ");
                        String course = input.readLine();
                        registerStudent(conn, student, course);
                    } else if ((new String("unregister")).startsWith(mode
                            .toLowerCase())) {
                        /* Unregister student mode */
                        System.out.print("Unregister from what course? > ");
                        String course = input.readLine();
                        unregisterStudent(conn, student, course);
                    } else if ((new String("quit")).startsWith(mode
                            .toLowerCase())) {
                        System.out.println("Goodbye!");
                        break;
                    } else {
                        System.out
                                .println("Unknown argument, please choose either "
                                        + "information, register, unregister or quit!");
                        continue;
                    }
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.exit(2);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(2);
            }
        } else {
            System.err.println("Wrong number of arguments");
            System.exit(3);
        }
    }

    static void getInformation(Connection conn, String student)
            throws SQLException {
        System.out.println("Information for student " + student);
        System.out.println("------------------------------------");

        // Create statement!
        Statement stmnt = conn.createStatement();

        ResultSet rs = stmnt
                .executeQuery("SELECT name, program, branch FROM StudentsFollowing "
                        + "WHERE id = " + student);

        rs.next(); // Fetch first and only row with student info

        System.out.println("Name: " + rs.getString("name"));
        System.out.println("Line: " + rs.getString("program"));
        String branch = rs.getString("branch");
        System.out.println("Branch: " + (branch.equals("NA") ? "-" : branch));
        rs.close();

        System.out.println();

        System.out.println("Read courses(code, credits: grade):");

        rs = stmnt.executeQuery("SELECT course, credits, grade "
                + "FROM FinishedCourses WHERE student = " + student);
        while (rs.next()) { // Find all finished courses
            System.out.println(rs.getString("course") + ", "
                    + rs.getString("credits") + ": " + rs.getString("grade"));
        }
        rs.close();

        System.out.println();

        System.out.println("Registered courses(code, credits: status):");

        // First, find registered courses
        rs = stmnt.executeQuery("SELECT course, credits, waitingstatus "
                + "FROM Registrations " + "WHERE student = " + student
                + " AND waitingstatus = 'Registered On'");
        while (rs.next()) {
            System.out.println(rs.getString("course") + ", "
                    + rs.getString("credits") + ": "
                    + rs.getString("waitingstatus"));
        }
        rs.close();

        // Second, find position from queued courses
        String query = "SELECT Registrations.course AS course, credits, queuenumber "
                + "FROM Registrations "
                + "JOIN CourseQueuePositions ON CourseQueuePositions.id = Registrations.student AND "
                + "CourseQueuePositions.course = Registrations.course AND "
                + "WaitingStatus = 'Queued For' "
                + "WHERE student = "
                + student;
        rs = stmnt.executeQuery(query);
        while (rs.next()) {
            System.out.println(rs.getString("course") + ", "
                    + rs.getString("credits") + ": waiting as number "
                    + rs.getString("queuenumber"));
        }
        rs.close();
        System.out.println();

        query = "SELECT numberOfSeminar, mathCredits, researchCredits, credits, graduation "
                + "FROM PathToGraduation " + "WHERE id = " + student;
        rs = stmnt.executeQuery(query);

        rs.next();
        String graduates = (rs.getInt("graduation") == 1 ? "yes" : "no");
        System.out.println("Seminar courses taken: "
                + rs.getString("numberOfSeminar"));
        System.out
                .println("Math credits taken: " + rs.getString("mathCredits"));
        System.out.println("Research credits taken: "
                + rs.getString("researchCredits"));
        System.out.println("Total credits taken: " + rs.getString("credits"));
        System.out.println("Fulfills the requirements for graduation: "
                + graduates);

        System.out
                .println("-----------------------------------------------------");
        System.out.println();

        // Clean up resources
        rs.close();
        stmnt.close();
    }

    static void registerStudent(Connection conn, String student, String course) {
        course = formatCourse(course);
        
        try{
            Statement stmnt = conn.createStatement();
            String query = "INSERT INTO Registrations VALUES("+student+", 'your name', '"+course+"', 0.0, 'registered on')";
           
            int result = stmnt.executeUpdate(query);
           
            if(result != 0){
                ResultSet rs = stmnt.executeQuery("SELECT COUNT(*) AS registered "
                        + "FROM Registrations " + "WHERE student = " + student
                        + " AND waitingstatus = 'Registered On' AND course = '"+course+"'");
               
                rs.next();
               
                if(rs.getInt("registered") > 0){
                    System.out.println("You are registered for "+course);
                }
                else{
                    query = "SELECT queuenumber "
                        + "FROM Registrations "
                        + "JOIN CourseQueuePositions ON CourseQueuePositions.id = Registrations.student AND "
                        + "CourseQueuePositions.course = Registrations.course AND "
                        + "WaitingStatus = 'Queued For' "
                        + "WHERE student = "
                        + student;
                    rs = stmnt.executeQuery(query);
                   
                    rs.next();
                    System.out.println("You are queued for the course "+course+" on position "+rs.getInt("queuenumber"));
                }
                rs.close();
            }
            else{
                System.out.println("Could not register to course "+course);
            }
            stmnt.close();
        }
        catch(SQLException sqle){
            String s = sqle.getMessage();
            s = s.substring(0, s.indexOf('\n'))+" "+course+"\n";
            s += "Check that you're not already registered, have passed the course or haven't completed all prerequisites!";
            System.out.println(s);
            System.out.println();
        }
    }

    static void unregisterStudent(Connection conn, String student, String course) {
        course = formatCourse(course);
       
        try{
            Statement stmnt = conn.createStatement();
            String query = "DELETE FROM Registrations WHERE student = "+student+" AND course = '"+course+"'";
           
            int result = stmnt.executeUpdate(query);
           
            if(result != 0){
                System.out.println("You are now unregistered from "+course);
            }
            else{
                System.out.println("Could not unregister from course "+course);
            }
            stmnt.close();
        }
        catch(SQLException sqle){
            String s = sqle.getMessage();
            int index = (s.indexOf('\n') >= 0 ? s.indexOf('\n') : s.length());
            s = s.substring(0, index)+" "+course+"\n";
            System.out.println(s);
        }
        System.out.println();
    }

    /**
     * Convert tda357 to TDA357.
     */
    private static String formatCourse(String course) {
        return course.substring(0, 3).toUpperCase()+course.substring(3, 6);
    }
}