package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import util.HashWithSHA256;

public class UserDao {
	// DB接続用の定数
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String DB_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	final String DB_USER_ID = "SAMPLE";
	final String DB_PASSWORD = "SAMPLE";
	// メンバ変数の用意
	Connection conn = null;
	PreparedStatement stmt = null;

	/**
	 * ユーザー登録メソッド
	 *
	 * @param regiBean Bean型のユーザー情報
	 * @param path 格納フォルダパス
	 * @return num 結果
	 * @throws SQLException
	 */
	public int registerDao(UserBean regiBean, String path) {
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 自動コミットを無効にする
			conn.setAutoCommit(false);

			// M_USERの登録
			// プレースホルダでSQL作成
			String sql = "INSERT INTO M_USER (USER_ID, USER_PASSWORD, USER_NAME, USER_PRIVILEGE) VALUES (?, ?, ?, ?)";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメーターセット
			stmt.setString(1, regiBean.getId());					// ユーザーID
			HashWithSHA256 hw = new HashWithSHA256();
			stmt.setString(2, hw.hash(regiBean.getPassword()));		// パスワード
			stmt.setString(3, regiBean.getName());					// ユーザー名
			stmt.setInt(4, regiBean.getPrivilege());				// 権限
			// SQLの実行
			num = stmt.executeUpdate();
			// ステートメントをクローズ
			stmt.close();

			// 一般ユーザーの場合、ユーザーフォルダの作成
			if(regiBean.getPrivilege() == 3) {
				Path p = Paths.get(path + "/" + regiBean.getId());
				Files.createDirectory(p);
			}

			// USER_ID_SEQUENCEを進める
			// SQL作成
			sql = "SELECT USER_ID_SEQUENCE.nextval FROM DUAL";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// SQLの実行
			stmt.executeQuery();
			// ステートメントをクローズ
			stmt.close();

			// コミット
			conn.commit();
		} catch (SQLException | ClassNotFoundException | IOException e) {
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
	 * ユーザー新規登録用のID取得メソッド
	 *
	 * @return id 新規登録用のユーザーID
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

			// ユーザーシーケンスの現在値を取得する
			String sql = "SELECT LAST_NUMBER FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = 'USER_ID_SEQUENCE'";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// SQLの実行
			rs = stmt.executeQuery();

			// USER_ID_SEQUENCEを次のレコードに進める
			rs.next();
			// LAST_NUMBERをnumに入れる
			int num = rs.getInt("LAST_NUMBER");
			// numを1増やす
			num++;
			id= "Z" + String.format("%06d", num);
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
		// return
		return id;
	}

	/**
	 * パスワード変更メソッド
	 *
	 * @param id ユーザー名
	 * @param password 新しいパスワード
	 * @return num 結果
	 * @throws SQLException
	 */
	public int alterPassword(String id, String password) {
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 自動コミットを無効にする
			conn.setAutoCommit(false);

			// M_USERの更新(パスワードのみ)
			// プレースホルダでSQL作成
			String sql = "UPDATE M_USER SET USER_PASSWORD = ?, UPDATE_DATETIME = ? WHERE USER_ID = ?";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメータセット
			HashWithSHA256 hw = new HashWithSHA256();
			stmt.setString(1, hw.hash(password));									// 新しいパスワード
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));		// 最終更新時刻
			stmt.setString(3, id);													// ユーザーID
			// SQLの実行
			num = stmt.executeUpdate();
			// ステートメントをクローズ
			stmt.close();

			// コミット
			conn.commit();
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
	 * ユーザー更新メソッド
	 *
	 * @param updateBean Bean型のユーザー更新情報
	 * @return num 結果
	 * @throws SQLException
	 */
	public int updateDao(UserBean updateBean) {
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 自動コミットを無効にする
			conn.setAutoCommit(false);

			// M_USERの更新
			// プレースホルダでSQL作成
			String sql = "UPDATE M_USER SET USER_PASSWORD = ?, USER_NAME = ?, UPDATE_DATETIME = ? WHERE USER_ID = ?";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメータセット
			HashWithSHA256 hw = new HashWithSHA256();
			stmt.setString(1, hw.hash(updateBean.getPassword()));					// パスワード
			stmt.setString(2, updateBean.getName());								// ユーザー名
			stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));		// 最終更新時刻
			stmt.setString(4, updateBean.getId());									// ユーザーID
			// SQLの実行
			num = stmt.executeUpdate();
			// ステートメントをクローズ
			stmt.close();

			// コミット
			conn.commit();
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
	 * ユーザー削除メソッド
	 *
	 * @param id ユーザーID
	 * @param path 格納フォルダパス
	 * @return num 結果
	 * @throws SQLException
	 */
	public int deleteDao(String id, String path) {
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// 自動コミットを無効にする
			conn.setAutoCommit(false);

			// STOCK_TABLEのレコードを削除
			// プレースホルダでSQL作成
			String sql = "DELETE M_USER WHERE USER_ID = ?";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメータセット
			stmt.setString(1, id);		// ユーザーID
			// SQLの実行
			num = stmt.executeUpdate();
			// ステートメントをクローズ
			stmt.close();

			// ユーザーフォルダが存在する場合、フォルダを削除する
			Path p = Paths.get(path + "/" + id);
			if(Files.exists(p)) {
				Files.delete(p);
			}
			// コミット
			conn.commit();
		} catch (SQLException | ClassNotFoundException | IOException e) {
			// numに0を入れる
			num = 0;
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
		// 試行結果のreturn
		return num;
	}

	/**
	 * ユーザー検索メソッド
	 * DB内で検索条件に一致する商品を全て返す
	 *
	 * @param name ユーザー名
	 * @param privilege 権限
	 * @return list 検索条件に一致した商品のリスト
	 * @throws SQLException
	 */
	public ArrayList<UserBean> selectSearchDao(String name, int privilege) {
		// SELECTしたデータを格納する変数宣言
		ResultSet rs = null;
		// return用変数
		ArrayList<UserBean> list = new ArrayList<>();

		String whereName;		// ユーザー名についてのWhere句文字列

		// ユーザー名が入力されていないかチェックする
		if(name.equals("")) {
			// 入力されていない場合、NULLでないことを条件とするSQL文にする
			whereName = "IS NOT NULL";
		} else {
			// 入力されていた場合、プレースホルダで部分一致を条件とするSQL文にする
			whereName = "LIKE ?";
		}

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// ユーザー検索するSQL文
			String sql = "SELECT * FROM M_USER WHERE USER_NAME " + whereName + " AND USER_PRIVILEGE = ? ORDER BY USER_ID ASC";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			int paramIndex = 1;	// パラメータの番号を格納する変数
			// パラメータセット
			// nameが入力されているかチェックする
			if(!name.equals("")) {
				// 入力されていた場合
				stmt.setString(paramIndex, "%" + name + "%");
				// paramIndexを1増やす
				paramIndex++;
			}
			stmt.setInt(paramIndex, privilege);	// 権限
			// SQLの実行
			rs = stmt.executeQuery();

			// 取得したレコードの数だけ繰り返す
			while(rs.next()) {
				// UserBeanクラスのインスタンスを生成
				UserBean ub = new UserBean();
				// フィールドに値をセットする
				ub.setId(rs.getString(1));			// ユーザーID
				ub.setName(rs.getString(3));		// ユーザー名
				ub.setPrivilege(rs.getInt(4));		// 権限
				// ibをlistに加える
				list.add(ub);
			}
			// リザルトセットをクローズ
			rs.close();
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
		// listを返す
		return list;
	}

	/**
	 * ログイン認証メソッド
	 *
	 * @param id ユーザーID
	 * @param password パスワード
	 * @return num 結果
	 * @throws SQLException
	 */
	public int authDao(String id, String password) {
		// SELECTしたデータを格納する変数宣言
		ResultSet rs = null;
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// プレースホルダでSQL作成
			String sql = "SELECT COUNT(*) FROM M_USER WHERE USER_ID = ? AND USER_PASSWORD = ?";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメーターセット
			stmt.setString(1,  id);		// ユーザーID
			HashWithSHA256 hw = new HashWithSHA256();
			stmt.setString(2, hw.hash(password));	// パスワード
			// SQLの実行
			rs = stmt.executeQuery();

			// 結果を取得
			rs.next();
			num = rs.getInt(1);
			// リザルトセットをクローズ
			rs.close();
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
	 * 権限取得メソッド
	 *
	 * @param id ユーザーID
	 * @return num 結果
	 * @throws SQLException
	 */
	public int getPrivilege(String id) {
		// SELECTしたデータを格納する変数宣言
		ResultSet rs = null;
		// return用変数
		int num = 0;

		try {
			// OracleJDBCドライバのロード
			Class.forName(JDBC_DRIVER);
			// DBに接続(URL, USER_ID, PASSWORD)
			conn = DriverManager.getConnection(DB_URL, DB_USER_ID, DB_PASSWORD);

			// プレースホルダでSQL作成
			String sql = "SELECT USER_PRIVILEGE FROM M_USER WHERE USER_ID = ?";
			// SQLをプリコンパイル
			stmt = conn.prepareStatement(sql);
			// パラメーターセット
			stmt.setString(1,  id);		// ユーザーID
			// SQLの実行
			rs = stmt.executeQuery();

			// 結果を取得
			rs.next();
			num = rs.getInt(1);
			// リザルトセットをクローズ
			rs.close();
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
		// 実行結果をreturn
		return num;
	}

	/**
	 * ユーザー名の形式チェックメソッド
	 *
	 * @param name ユーザー名
	 * @return num 結果
	 * @throws SQLException
	 */
	public int checkNameFormat(String name) {
		// return用変数
		int num = 0;

		// 文字列の長さをチェックする
		int len = name.length();
		if(len > 20) {
			num -= 1;
		}

		// 結果のreturn
		return num;
	}

	/**
	 * パスワードの形式チェックメソッド
	 *
	 * @param password パスワード
	 * @return num 結果
	 * @throws SQLException
	 */
	public int checkPasswordFormat(String password) {
		// return用変数
		int num = 0;

		// 文字列の長さをチェックする
		int len = password.length();
		if(len < 8 || 16 < len) {
			num -= 1;
		}

		// 半角英数記号以外の文字チェック
		if(!password.matches("^[a-zA-Z0-9!-/:-@\\[-`\\{-~]*$")) {
			num -= 2;
		}

		// 英数の混在チェック
		if(!(password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*"))) {
			num -= 4;
		}

		// 結果のreturn
		return num;
	}
}
