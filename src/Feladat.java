import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;

public class Feladat {
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/konyvtar";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final int BATCH_SIZE = 20;

    public void importKolcsonzok(String filePath) throws SQLException, IOException {
        String sql = "INSERT INTO Kolcsonzok (nev, szulIdo) VALUES (?, ?)";

        try (
                Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                BufferedReader lineReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
                PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            String lineText;
            int count = 0;

            lineReader.readLine(); 
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(";");
                statement.setString(1, data[0]);
                statement.setDate(2, Date.valueOf(data[1]));
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            System.out.println("A kolcsonzok táblába sikeresen beillesztett sorok száma: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importKolcsonzesek(String filePath) throws SQLException, IOException {
        String sql = "INSERT INTO Kolcsonzesek (kolcsonzokId, iro, mufaj, cim) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                BufferedReader lineReader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8));
                PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);

            String lineText;
            int count = 0;

            lineReader.readLine(); 
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(";");
                statement.setInt(1, Integer.parseInt(data[0]));
                statement.setString(2, data[1]);
                statement.setString(3, data[2]);
                statement.setString(4, data[3]);
                statement.addBatch();

                if (++count % BATCH_SIZE == 0) {
                    statement.executeBatch();
                }
            }

            statement.executeBatch();
            connection.commit();
            System.out.println("A kolcsonzesek táblába sikeresen beillesztett sorok száma: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
