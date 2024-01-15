package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// フィールド宣言
	private String id;				// ユーザーID
	private String password;		// パスワード
	private String name;			// ユーザー名
	private int privilege;			// 権限
	private Timestamp dateTime;	// 最終更新時刻

	/**
	 * ユーザーIDを返すメソッド
	 * @return id ユーザーID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ユーザーIDを設定するメソッド
	 * @param id ユーザーID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * パスワードを返すメソッド
	 * @return password パスワード
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * パスワードを設定するメソッド
	 * @param password パスワード
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * ユーザー名を返すメソッド
	 * @return name ユーザー名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ユーザー名を設定するメソッド
	 * @param name ユーザー名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 権限を返すメソッド
	 * @return privilege 権限
	 */
	public int getPrivilege() {
		return this.privilege;
	}

	/**
	 * 権限を設定するメソッド
	 * @param privilege 権限
	 */
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	/**
	 * 最終更新時刻を返すメソッド
	 * @return dateTime 最終更新時刻
	 */
	public Timestamp getDateTime() {
		return this.dateTime;
	}

 	/**
	 * 最終更新時刻を設定するメソッド
	 * @param dateTime 最終更新時刻
	 */
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
}
