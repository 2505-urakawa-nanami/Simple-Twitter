package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {
	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//編集画面表示
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		//編集画面に移動
		List<String> errorMessages = new ArrayList<String>();
		String idParameter = request.getParameter("message_id");
		if (StringUtils.isBlank(idParameter) || !idParameter.matches("^[0-9]*+$")) {
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		int messageId = Integer.parseInt(idParameter);
		Message message = new MessageService().selectEdit(messageId);
		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました");
			request.getSession().setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}
		request.setAttribute("message", message);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	//更新処理
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();
		String text = request.getParameter("text");
		int messageId = Integer.parseInt(request.getParameter("message_id"));

		if (!isValid(text, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			Message message = new MessageService().selectEdit(messageId);
			request.setAttribute("message", message);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}
		new MessageService().update(messageId, text);
		response.sendRedirect("./");
	}

	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
