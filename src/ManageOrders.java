import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/ManageOrders")

public class ManageOrders extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Utilities utility = new Utilities(request, pw);

		if (!utility.isLoggedin()) {
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please Login to Manage Orders");
			response.sendRedirect("Login");
			return;
		}
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		pw.print("<form name ='ManageOrders' action='ManageOrders' method='post'>");
		pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
		pw.print("<a style='font-size: 24px;'>Order</a>");
		pw.print("</h2><div class='entry'>");

		ArrayList<OrderPayment> orderPaymentList = MySQLDataStoreUtilities.getAllOrders();

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("ManageOrder")) {
			orderPaymentList = MySQLDataStoreUtilities.getAllOrders();

			pw.print("<table  class='gridtable'>");
			pw.print("<tr><td>select:</td>");
			pw.print("<td>orderID:</td>");
			pw.print("<td>userName:</td>");
			pw.print("<td>productOrdered:</td>");
			pw.print("<td>orderPrice:</td>");
			pw.print("<td>userAddress:</td>");
			pw.print("<td>creditCardNo:</td>");
			pw.print("<td>deliveryDate:</td>");
			pw.print("<td>Update:</td>");
			pw.print("<td>Delete:</td></tr>");

			if (!orderPaymentList.isEmpty()) {
				for (OrderPayment op : orderPaymentList) {
					int orderID = op.getOrderID();
					ArrayList<OrderItem> orderItemList = MySQLDataStoreUtilities.getOrderItemsByOrderID(orderID);
					if (!orderItemList.isEmpty()) {
						pw.print("<tr>");
						pw.print("<td><input type='radio' name='orderID' value='" + op.getOrderID() + "'></td>");
						pw.print("<td>" + op.getOrderID() + ".</td><td>"
								+ MySQLDataStoreUtilities.getUserNameByID(op.getUserID()) + "</td><td>");
						for (int i = 0; i < orderItemList.size(); i++) {
							OrderItem oi = orderItemList.get(i);
							if (i != 0) {
								pw.print("</br>");
							}
							pw.print(oi.getName());
						}
						pw.print("</td><td>" + op.getOrderPrice() + "</td>");
						pw.print("<td><input type='text' size='4' name='userAddress" + op.getOrderID() + "' value='"
								+ op.getUserAddress() + "'></td>");
						pw.print("<td><input type='text' size='4' name='creditCardNo" + op.getOrderID() + "' value='"
								+ op.getCreditCardNo() + "'></td>");
						pw.print("<td><input type='text' size='4' name='deliveryDate" + op.getOrderID() + "' value='"
								+ op.getDeliveryDate() + "'></td>");
						pw.print("<td><input type='submit' name='Order' value='UpdateOrder' class='btnbuy'></td>");
						pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
						pw.print("</tr>");
					}
				}
			} else {
				pw.print("<h4 style='color:red'>No order available</h4>");
			}
			pw.print("</table>");
		}

		if (request.getParameter("Order") != null && request.getParameter("Order").equals("CancelOrder")) {
			if (request.getParameter("orderID") == null) {
				pw.print("<h4 style='color:red'>Please select an order by using radio button</h4>");
				pw.print("</form></div></div></div>");
				utility.printHtml("Footer.html");
				return;
			}

			int orderID = Integer.parseInt(request.getParameter("orderID"));
			MySQLDataStoreUtilities.deleteOrderByID(orderID);
			pw.print("<h4 style='color:red'>Order# " + orderID + " has been Cancelled</h4>");
		}
		
		if (request.getParameter("Order") != null && request.getParameter("Order").equals("UpdateOrder")) {
			if (request.getParameter("orderID") == null) {
				pw.print("<h4 style='color:red'>Please select an order by using radio button</h4>");
				pw.print("</form></div></div></div>");
				utility.printHtml("Footer.html");
				return;
			}

			int orderID = Integer.parseInt(request.getParameter("orderID"));
			String userAddress = request.getParameter("userAddress" + orderID);
			String creditCardNo = request.getParameter("creditCardNo" + orderID);
			String deliveryDate = request.getParameter("deliveryDate" + orderID);
			
			MySQLDataStoreUtilities.updateOrderByID(orderID, userAddress, creditCardNo, deliveryDate);
			pw.print("<h4 style='color:red'>Order# " + orderID + " has been Updated</h4>");
		}
		
		pw.print("</form></div></div></div>");
		utility.printHtml("Footer.html");
	}
}
