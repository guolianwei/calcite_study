
import java.sql.DriverManager
import org.apache.calcite.jdbc.CalciteConnection
import org.apache.calcite.adapter.java.ReflectiveSchema
import java.util.Properties
import com.lw.guo.datacombain.engin.JavaHrSchema
object CalciteTest {
  def main(args: Array[String]) = {
    Class.forName("org.apache.calcite.jdbc.Driver");
    val info = new Properties();
    info.setProperty("lex", "JAVA");
    val connection = DriverManager.getConnection("jdbc:calcite:", info);
    val calciteConnection =
      connection.asInstanceOf[CalciteConnection];
    val rootSchema = calciteConnection.getRootSchema();
 
    val hrs = new ReflectiveSchema(new JavaHrSchema())
    rootSchema.add("hr", hrs);
    val statement = calciteConnection.createStatement();
 
    val resultSet = statement.executeQuery(
      """select * from hr.emps as e 
        join hr.depts as d on e.deptno = d.deptno""");
 
    while (resultSet.next()) {
      (1 to resultSet.getMetaData.getColumnCount).foreach(x => print(resultSet.getObject(x) + "\t"));
      println("");
    }
 
    resultSet.close();
    statement.close();
    connection.close();
  }
}