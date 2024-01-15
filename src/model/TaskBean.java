package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class TaskBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// フィールド宣言
	private String id;							// 課題ID
	private String name;						// 課題名
	private int reviewNec;						// レビュー要否
	private ArrayList<String> fileNameList;	// ファイル名リスト
	private Timestamp dateTime;				// 最終更新時刻

	/**
	 * 課題IDを返すメソッド
	 * @return id 課題ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 課題IDを設定するメソッド
	 * @param id 課題ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 課題名を返すメソッド
	 * @return name 課題名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 課題名を設定するメソッド
	 * @param name 課題名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * レビュー要否を返すメソッド
	 * @return reviewNec レビュー要否
	 */
	public int getReviewNec() {
			return this.reviewNec;
	}

	/**
	 * レビュー要否を設定するメソッド
	 * @param reviewNec レビュー要否
	 */
	public void setReviewNec(int reviewNec) {
		this.reviewNec = reviewNec;
	}

	/**
	 * 課題ファイルリストを返すメソッド
	 * @return fileNameList 課題ファイルリスト
	 */
	public ArrayList<String> getFileNameList() {
		return this.fileNameList;
	}

	/**
	 * 課題ファイルリストを設定するメソッド
	 * @param list 課題ファイルリスト
	 */
	public void setFileNameList(ArrayList<String> list) {
		this.fileNameList = list;
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
