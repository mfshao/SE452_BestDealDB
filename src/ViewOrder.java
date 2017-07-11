import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@WebServlet("/ViewOrder")

public class ViewOrder extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Utilities utility = new Utilities(request, pw);
		if (!utility.isLoggedin()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please Login to View your Orders");
			response.sendRedirect("Login");
			return;
		}
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<form name ='ViewOrder' action='ViewOrder' method='get'>");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order</a>");
		pw.print("</h2><div class='entry'>");

		if (request.getParameter("Order") == null) {
			pw.print("<table align='center'><tr><td>Enter OrderNo &nbsp&nbsp<input name='orderID' type='text'></td>");
			pw.print("<td><input type='submit' name='Order' value='ViewOrder' class='btnbuy'></td></tr></table>");
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("ViewOrder")) {
			int orderID = Integer.parseInt(request.getParameter("orderID"));
			pw.print("<input type='hidden' name='orderID' value='" + orderID + "'>");

			OrderPayment op = MySQLDataStoreUtilities.getOrderByID(orderID);
			ArrayList<OrderItem> orderItemList = MySQLDataStoreUtilities.getOrderItemsByOrderID(orderID);

			if (op != null && !orderItemList.isEmpty()) {
				if (MySQLDataStoreUtilities.getUserNameByID(op.getUserID()).equals(utility.username())) {
				pw.print("<table  class='gridtable'>");
				pw.print("<tr>");
				pw.print("<td>orderID:</td>");
				pw.print("<td>userName:</td>");
				pw.print("<td>productOrdered:</td>");
				pw.print("<td>orderPrice:</td>");
				pw.print("<td>deliveryDate:</td>");
				pw.print("<td>action:</td></tr>");
				pw.print("<tr>");
				pw.print("<td>" + op.getOrderID() + ".</td><td>" + MySQLDataStoreUtilities.getUserNameByID(op.getUserID()) + "</td><td>");
				for (int i = 0; i < orderItemList.size(); i++) {
					OrderItem oi = orderItemList.get(i);
					if (i != 0) {
						pw.print("</br>");
					}
					pw.print(oi.getName());
				}
				pw.print("</td><td>" + op.getOrderPrice() + "</td><td>" + op.getDeliveryDate()
						+ "</td>");
				pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
				pw.print("</tr>");
				pw.print("</table>");
				} else {
					pw.print("<h4 style='color:red'>You don't have premission to view this order</h4>");
				}
			} else {
				pw.print("<h4 style='color:red'>You have not placed any order with this order id</h4>");
			}
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("CancelOrder")) {
			int orderID = 0;
			orderID = Integer.parseInt(request.getParameter("orderID"));
			MySQLDataStoreUtilities.deleteOrderByID(orderID);
			pw.print("<h4 style='color:red'>Your Order# " + orderID + " has been Cancelled</h4>");
		}
		pw.print("</form></div></div></div>");
		utility.printHtml("Footer.html");
	}

}
