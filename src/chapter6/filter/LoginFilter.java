package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest httprequest, ServletResponse httpresponse,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) httprequest;
		HttpServletResponse response = (HttpServletResponse) httpresponse;

		User user = (User) request.getSession().getAttribute("loginUser");
		if (user != null) {
			chain.doFilter(request, response); // サーブレットを実行
		} else {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインしてください");
			HttpSession session = request.getSession();
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./login");
			return;
		}

	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}
