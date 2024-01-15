package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskDao {
	// DB接続用の定数
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	final String DB_USER_ID = "SAMPLE";
	final String DB_PASSWORD = "SAMPLE";
	// メンバ変数の用意
	Connection conn = null;
	PreparedStatement stmt = null;

	/**
	 * 課題登録メソッド
	 *
	 * @param name 課題名
	 * @return num 結果
	 * @throws SQLException
	 */
	public int registerDao(TaskBean regiBean) {
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 自動コミットを無効にする
			conn.setAutoCommit(false);

			// M_TASKの登録
			// プレースホルダでSQL作成
			String sql = "INSERT INTO M_TASK (TASK_ID, TASK_NAME, REVIEW_NECESSITY) VALUES (?, ?, ?)";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメーターセット
			stmt.setString(1, regiBean.getId());		// 課題ID
			stmt.setString(2, regiBean.getName());		// 課題名
			stmt.setInt(3, regiBean.getReviewNec());	// レビュー要否
			// SQLの実行
			num = stmt.executeUpdate();
			// ステートメントをクローズ
			stmt.close();

			// TASK_FILENAMEの登録
			for(String fileName : regiBean.getFileNameList()) {
				// プレースホルダでSQL作成
				sql = "INSERT INTO TASK_FILENAME (TASK_ID, TASK_FILENAME) VALUES(?, ?)";
				// SQLをプリコンパイル
				stmt = conn.prepareStatement(sql);
				// パラメーターセット
				stmt.setString(1, regiBean.getId());	// 課題ID
				stmt.setString(2, fileName);			// ファイル名
				// SQLの実行
				num = stmt.executeUpdate();
				// ステートメントをクローズ
				stmt.close();
			}

			// TASK_ID_SEQUENCEを進める
			// SQL作成
			sql = "SELECT TASK_ID_SEQUENCE.nextval FROM DUAL";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// SQLの実行
			stmt.executeQuery();
			// ステートメントをクローズ
			stmt.close();

			// コミット
			conn.commit();
		} catch (SQLException | ClassNotFoundException e) {
			// エラーが発生した場合、numに-1を入れる
			num = -1;
			e.printStackTrace();
		} finally {
			try {
				// ステートメント実行していたらステートメントをクローズ
				if(stmt != null) {
					stmt.close();
				}
				// データベース接続していたらデータベースをクローズ
				if(conn != null) {
					// ロールバックしてからクローズ
					conn.rollback();
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 実行結果をreturn
		return num;
	}

	/**
	 * 課題新規登録用のID取得メソッド
	 *
	 * @return id 新規登録用の課題ID
	 * @throws SQLException
	 */
	public String getNextId() {
		// SELECTしたデータを格納する変数宣言
		ResultSet rs = null;
		// return用変数
		String id = "";

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 課題シーケンスの現在値を取得する
			String sql = "SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'TASK_ID_SEQUENCE'";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// SQLの実行
			rs = stmt.executeQuery();

			// TASK_ID_SEQUENCEを次のレコードに進める
			rs.next();
			// LAST_NUMBERをnumに入れる
			int num = rs.getInt("LAST_NUMBER");
			id= "T" + String.format("%06d", num);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// ステートメント実行していたらステートメントをクローズ
				if(stmt != null) {
					stmt.close();
				}
				// データベース接続していたらデータベースをクローズ
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
}
