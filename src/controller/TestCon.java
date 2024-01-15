package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.TaskDao;

/**
 * Servlet implementation class TestCon
 */
public class TestCon extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestCon() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 遷移先を格納する変数
		String dist = "";
		dist = "/Test.jsp";

		System.out.println("doGet:TestCon");

		// 画面遷移(フォワードを使ってJSPに表示を切り替える)
		ServletContext app = this.getServletContext();
		RequestDispatcher dispatcher = app.getRequestDispatcher(dist);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 遷移先を格納する変数
		String dist = "";
		dist = "/Test.jsp";

		System.out.println("doPost:TestCon");

//		UserDao ud = new UserDao();
//		System.out.println("privilege:" + ud.getPrivilege("Z000001"));

//		String userId = ud.getNextId();
//		UserBean regiBean = new UserBean();
//		regiBean.setId(userId);
//		regiBean.setPassword("test");
//		regiBean.setName("test");
//		regiBean.setPrivilege(3);
//		String path = this.getServletContext().getRealPath("folders");
//		ud.createDao(regiBean, path);
//		regiBean.setId("Z000006");
//		regiBean.setName("test6");
//		regiBean.setPassword("test6");
//		ud.updateDao(regiBean);
//		ud.alterPassword("Z000007", "test6");
//		ud.deleteDao("Z000021", path);

//		ArrayList<UserBean> ubList = ud.selectSearchDao("", 3);
//		for(UserBean ub : ubList) {
//			System.out.println(ub.getId());
//		}

		TaskDao td = new TaskDao();
		System.out.println("TaskID:" + td.getNextId());


		// 画面遷移(フォワードを使ってJSPに表示を切り替える)
		ServletContext app = this.getServletContext();
		RequestDispatcher dispatcher = app.getRequestDispatcher(dist);
		dispatcher.forward(request, response);
	}

}
