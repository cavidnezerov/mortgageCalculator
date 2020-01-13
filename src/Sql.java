import java.sql.*;
import java.util.List;

public class Sql {

    private String username = "hr";
    private String password = "HR";
    private String jdbcUrl = "jdbc:oracle:thin:@//10.251.82.9:1521/ORCL";
    private String jdbcDriver = "oracle.jdbc.driver.OracleDriver";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void sqlNew() {
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);

            String sql = "insert into customer " +
                    "values(customer_seq.nextval, ?, ?, ?)";
            System.out.println("Customer`s properties:");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, Static.inS());
            preparedStatement.setString(2, Static.inS());
            preparedStatement.setDate(3, Date.valueOf(Static.date()));

            int count = preparedStatement.executeUpdate();

            System.out.println("Do you want to approve to insert a new Customer:");
            System.out.println("(if you do say `yes` otherwise `no` or any case)");
            if (Static.inS().equalsIgnoreCase("yes")) {
                connection.commit();
                System.out.println(count + " Customer is added");
            } else {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Customer is not added");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Oracle Jdbc file is not found");
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("There is a error about the DB" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void sqlApply(){
        Mortgage mortgage;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            String sql = "select id, birth_date " +
                    "from customer " +
                    "where id = ? ";
            System.out.println("Enter the Customer`s id");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Static.inI());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            mortgage = new Mortgage(resultSet.getDate("birth_date").toLocalDate(), Static.inI(), Static.inB(), Static.date(), Static.inB(), Static.inB());
            System.out.println("Do you want to approve to insert your new mortgage:");
            System.out.println("(if you do say `yes` otherwise `no` or any case)");
            int id = resultSet.getInt("id");
            resultSet.close();
            preparedStatement.close();
            if (Static.inS().equalsIgnoreCase("yes")){
                sql = "insert into credit " +
                        "values(credit_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
                preparedStatement.setInt(1, id);
                preparedStatement.setBigDecimal(2, mortgage.getHomePrice());
                preparedStatement.setBigDecimal(3, mortgage.getInitialAmount());
                preparedStatement.setBigDecimal(4, mortgage.getAmount());
                preparedStatement.setBigDecimal(5, mortgage.getInterestAmount());
                preparedStatement.setDate(6, Date.valueOf(mortgage.getFirstPaymentDate()));
                preparedStatement.setDate(7, Date.valueOf(mortgage.getLastPaymentDate()));
                preparedStatement.setDate(8, Date.valueOf(mortgage.getStartDate()));
                preparedStatement.executeUpdate();
                resultSet=preparedStatement.getGeneratedKeys();
                resultSet.next();
                int creditId = resultSet.getInt(1);
                preparedStatement.close();
                mortgage.getMonthlyDetailList().forEach( m ->
                    {
                        String s = "insert into monthly_payment " +
                                "values(monthly_payment_seq.nextval, ?, ?, ?, ?, ?)";
                        try {
                            preparedStatement = connection.prepareStatement(s);
                            preparedStatement.setInt(1, creditId);
                            preparedStatement.setDate(2, Date.valueOf(m.getPaymentDate()));
                            preparedStatement.setBigDecimal(3, m.getBaseAmount());
                            preparedStatement.setBigDecimal(4, m.getInterestAmount());
                            preparedStatement.setBigDecimal(5, m.getTotalAmount());
                            preparedStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                );
                connection.commit();
                System.out.println("Your mortgage is approved");
            } else {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Your mortgage is not approved");
            }
        } catch (ClassNotFoundException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("There is a error about the DB" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
