package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.UserDao;

/**
 * Servlet implementation class Session
 */
public class Session extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Session() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 遷移先を格納する変数
		String dist = "";
		dist = "/Login.jsp";

		// 画面遷移(フォワードを使ってJSPに表示を切り替える)
		ServletContext app = this.getServletContext();
		RequestDispatcher dispatcher = app.getRequestDispatcher(dist);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 文字コードを指定する
		request.setCharacterEncoding("UTF-8");

		int num = 0;	// 認証されたユーザー数を格納する変数

		// クライアントが送信したパラメータの取得
		String userId = request.getParameter("userId");		// ユーザーID
		String password = request.getParameter("password");	// パスワード


		// UserDaoクラスのインスタンスを生成する
		UserDao ud = new UserDao();
		num = ud.authDao(userId, password);
		System.out.println(num);
		if(num <= 0) {
			System.out.println("認証失敗");
		} else {
			System.out.println("認証成功");
		}

		response.sendRedirect("/Sample");
	}

}
