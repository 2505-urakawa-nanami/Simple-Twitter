package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {


    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public MessageDao() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    public void insert(Connection connection, Message message) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO messages ( ");
            sql.append("    user_id, ");
            sql.append("    text, ");
            sql.append("    created_date, ");
            sql.append("    updated_date ");
            sql.append(") VALUES ( ");
            sql.append("    ?, ");                  // user_id
            sql.append("    ?, ");                  // text
            sql.append("    CURRENT_TIMESTAMP, ");  // created_date
            sql.append("    CURRENT_TIMESTAMP ");   // updated_date
            sql.append(")");

            ps = connection.prepareStatement(sql.toString());

            ps.setInt(1, message.getUserId());
            ps.setString(2, message.getText());

            ps.executeUpdate();
        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }
    public void delete(Connection connection, int messageId) {

  	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
          " : " + new Object(){}.getClass().getEnclosingMethod().getName());

          PreparedStatement ps = null;
          try {
              StringBuilder sql = new StringBuilder();
              sql.append("DELETE FROM messages ");
              sql.append("WHERE id = ?");

              ps = connection.prepareStatement(sql.toString());

              ps.setInt(1, messageId);
              ps.executeUpdate();
          } catch (SQLException e) {
  		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
              throw new SQLRuntimeException(e);
          } finally {
              close(ps);
          }
      }
    public  Message edit(Connection connection, int messageId) {

    	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
            " : " + new Object(){}.getClass().getEnclosingMethod().getName());

            PreparedStatement ps = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT ");
                sql.append("    messages.id as id, ");
                sql.append("    messages.user_id as user_id, ");
                sql.append("    messages.text as text, ");
                sql.append("    messages.created_date as created_date, ");
                sql.append("    messages.updated_date as updated_date ");
                sql.append("FROM messages ");
                sql.append("WHERE id = ?");

                ps = connection.prepareStatement(sql.toString());
                ps.setInt(1, messageId);
                ResultSet rs = ps.executeQuery();
                return toMessage(rs);
            } catch (SQLException e) {
    		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
                throw new SQLRuntimeException(e);
            } finally {
                close(ps);
            }
        }
    private Message toMessage(ResultSet rs) throws SQLException {


  	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
          " : " + new Object(){}.getClass().getEnclosingMethod().getName());
          try {
              while (rs.next()) {
                  Message message = new Message();
                  message.setId(rs.getInt("id"));
                  message.setUserId(rs.getInt("user_id"));
                  message.setText(rs.getString("text"));
                  message.setCreatedDate(rs.getTimestamp("created_date"));
                  message.setCreatedDate(rs.getTimestamp("updated_date"));

                  return message;
              }
          } finally {
              close(rs);
          }
		return null;
      }
    public void update(Connection connection, int messageId, String text) {

    	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
            " : " + new Object(){}.getClass().getEnclosingMethod().getName());

            PreparedStatement ps = null;
            try {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE messages SET ");
                sql.append("text = ?,");
                sql.append("updated_date = CURRENT_TIMESTAMP ");
                sql.append("WHERE id = ?");

                ps = connection.prepareStatement(sql.toString());

                ps.setString(1, text);
                ps.setInt(2, messageId);
                ps.executeUpdate();
            } catch (SQLException e) {
    		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
                throw new SQLRuntimeException(e);
            } finally {
                close(ps);
            }
        }
}