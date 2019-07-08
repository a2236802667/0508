package com.chungjunming;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SecKillServlet
 */
@WebServlet("/doseckill")
public class SecKillServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SecKillServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String userid = new Random().nextInt(50000) + "";
		String prodid = request.getParameter("prodid");
		
//		boolean if_success = SecKill_redis.doSecKill(userid, prodid);
		
		boolean if_success = SecKill_redisByScript.doSecKill(userid, prodid);
		
		response.getWriter().print(if_success);
		
	}
	
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
